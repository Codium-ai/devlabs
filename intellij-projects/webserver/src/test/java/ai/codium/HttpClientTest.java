package ai.codium;

import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class HttpClientTest {
    private static final int MAX_RETRIES = 3;
    private static final Duration TIMEOUT = Duration.ofSeconds(10);
    private static final Duration CLIENT_TIMEOUT = Duration.ofSeconds(60);
    private static final String HOST = "http://localhost:8080/";
    private static final URI INDEX_PAGE = URI.create(HOST + "index.html");
    private static final URI BIO_PAGE = URI.create(HOST + "bio.html");
    private static final List<URI> PAGES = List.of(INDEX_PAGE, BIO_PAGE);
    private static final URI DELETE_PAGE = URI.create(HOST + "delete.html");
    private static final int NUMBER_OF_REQUESTS = 6;

    @Test
    public void testMultipleHttpGetRequests() throws Exception {

        HttpClient client = HttpClient.newBuilder().connectTimeout(CLIENT_TIMEOUT).build();

        List<CompletableFuture<HttpResponse<String>>> futures = new ArrayList<>(NUMBER_OF_REQUESTS);
        for (int i = 0; i < NUMBER_OF_REQUESTS; i++) {
            CompletableFuture<HttpResponse<String>> future = testMultipleHttpRequests(client, PAGES.get(i % 2), "GET", null);
            futures.add(future);
        }

        CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        allOf.get();

        boolean allSuccess = futures.stream().map(CompletableFuture::join).allMatch(response -> response.statusCode() == 200);

        assertTrue(allSuccess, "All HTTP GET requests should return status code 200");
    }

    @Test
    public void testMultipleHttpPostRequests() throws Exception {

        HttpClient client = HttpClient.newBuilder().connectTimeout(CLIENT_TIMEOUT).build();

        List<CompletableFuture<HttpResponse<String>>> futures = new ArrayList<>(NUMBER_OF_REQUESTS);
        for (int i = 0; i < NUMBER_OF_REQUESTS; i++) {
            CompletableFuture<HttpResponse<String>> future = testMultipleHttpRequests(client, PAGES.get(i % 2), "POST", "Sample POST body");
            futures.add(future);
        }

        CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        allOf.get();

        boolean allSuccess = futures.stream().map(CompletableFuture::join).allMatch(response -> response.statusCode() == 200);

        assertTrue(allSuccess, "All HTTP POST requests should return status code 200");
    }

    @Test
    public void testMultipleHttpPutRequests() throws Exception {

        HttpClient client = HttpClient.newBuilder().connectTimeout(CLIENT_TIMEOUT).build();

        List<CompletableFuture<HttpResponse<String>>> futures = new ArrayList<>(NUMBER_OF_REQUESTS);
        for (int i = 0; i < NUMBER_OF_REQUESTS; i++) {
            CompletableFuture<HttpResponse<String>> future = testMultipleHttpRequests(client, PAGES.get(i % 2), "PUT", "Sample PUT body");
            futures.add(future);
        }

        CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        allOf.get();

        boolean allSuccess = futures.stream().map(CompletableFuture::join).allMatch(response -> response.statusCode() == 200);

        assertTrue(allSuccess, "All HTTP PUT requests should return status code 200");
    }

    @Test
    public void testMultipleHttpDeleteRequests() throws Exception {

        HttpClient client = HttpClient.newBuilder().connectTimeout(CLIENT_TIMEOUT).build();

        List<CompletableFuture<HttpResponse<String>>> futures = new ArrayList<>(NUMBER_OF_REQUESTS);
        for (int i = 0; i < NUMBER_OF_REQUESTS; i++) {
            CompletableFuture<HttpResponse<String>> future = testMultipleHttpRequests(client, DELETE_PAGE, "DELETE", null);
            futures.add(future);
        }

        CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        allOf.get();

        boolean allSuccess = futures.stream().map(CompletableFuture::join).allMatch(response -> {
            int statusCode = response.statusCode();
            return statusCode == 200 || statusCode == 204 || statusCode == 404 || statusCode == 403 || statusCode == 409;
        });

        assertTrue(allSuccess, "All HTTP DELETE requests should return one of the status codes: 200, 204, 404, 403, 409");
    }

    @Test
    public void testMultipleHttpPatchRequests() throws Exception {

        HttpClient client = HttpClient.newBuilder().connectTimeout(CLIENT_TIMEOUT).build();

        List<CompletableFuture<HttpResponse<String>>> futures = new ArrayList<>(NUMBER_OF_REQUESTS);
        for (int i = 0; i < NUMBER_OF_REQUESTS; i++) {
            CompletableFuture<HttpResponse<String>> future = testMultipleHttpRequests(client, PAGES.get(i % 2), "PATCH", "Sample PATCH body");
            futures.add(future);
        }

        CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        allOf.get();

        boolean allSuccess = futures.stream().map(CompletableFuture::join).allMatch(response -> response.statusCode() == 200);

        assertTrue(allSuccess, "All HTTP DELETE requests should return status code 200");
    }

    public CompletableFuture<HttpResponse<String>> testMultipleHttpRequests(HttpClient client, URI path, String method, String body) throws ExecutionException, InterruptedException {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder().uri(path).timeout(TIMEOUT);

        if ("POST".equalsIgnoreCase(method) || "PUT".equalsIgnoreCase(method) || "PATCH".equalsIgnoreCase(method)) {
            requestBuilder.method(method, HttpRequest.BodyPublishers.ofString(body));
        } else {
            requestBuilder.method(method, HttpRequest.BodyPublishers.noBody());
        }

        HttpRequest request = requestBuilder.build();
        CompletableFuture<HttpResponse<String>> responseFuture = sendWithRetry(client, request, MAX_RETRIES);
        return responseFuture;
    }

    private CompletableFuture<HttpResponse<String>> sendWithRetry(HttpClient client, HttpRequest request, int retries) {
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).exceptionally(ex -> {
            if (retries > 0 && (ex instanceof HttpTimeoutException || ex.getCause() instanceof java.net.SocketException)) {
                System.err.println("Retrying due to: " + ex.getClass().getName() + " - " + ex.getMessage());
                return client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).join();
            } else {
                throw new RuntimeException(ex);
            }
        });
    }
}