package http

import com.davidparry.refactor.Application
import org.springframework.boot.SpringApplication
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.http.*
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.HttpServerErrorException
import org.springframework.web.client.RestTemplate
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit

class ControllerSpec extends Specification {
    @Shared
    @AutoCleanup
    ConfigurableApplicationContext context

    void setupSpec() {
        Future future = Executors
                .newSingleThreadExecutor().submit(new Callable() {
            @Override
            ConfigurableApplicationContext call() throws Exception {
                return (ConfigurableApplicationContext) SpringApplication
                        .run(Application.class)
            }
        })
        context = future.get(60, TimeUnit.SECONDS)
    }

    void "Integration Get Test should return  #status with #body using endpoint #endpoint"() {
        when:
        ResponseEntity entity = new RestTemplate().getForEntity("http://localhost:8585/${endpoint}", String.class)

        then:
        entity.statusCode == status
        entity.body == body

        where:
        status        | body                                | endpoint
        HttpStatus.OK | '{"id":1234,"name":"John Smith"}'   | 'profile/1234'
        HttpStatus.OK | '[{"id":1234,"name":"John Smith"}]' | 'profile/users'
        HttpStatus.OK | null                                | 'profile/0000'
    }

    void "Integration Post Test with query after Post Test"() {
        given:
        String postBody = '{"id":3456,"name":"Automated Integration Test User"}'

        when:
        ResponseEntity entity = new RestTemplate().postForEntity("http://localhost:8585/profile/",
                new HttpEntity<String>(postBody, getJsonHeaders(MediaType.APPLICATION_JSON)),
                String.class)
        ResponseEntity query = new RestTemplate().getForEntity('http://localhost:8585/profile/users', String.class)

        then:
        entity.statusCode == HttpStatus.OK
        entity.body == postBody
        query.statusCode == HttpStatus.OK
        query.body == '[{"id":1234,"name":"John Smith"},{"id":3456,"name":"Automated Integration Test User"}]'
    }

    void "Failure Scenarios for various HTTP methods"() {
        when:
        HttpHeaders headers = getJsonHeaders(header)
        HttpEntity<String> entity = new HttpEntity<>(body, headers)
        new RestTemplate().exchange(url, method, entity, String.class)

        then:
        thrown(ex)

        where:
        method            | header                     | url                                  | body                               | ex
        HttpMethod.GET    | MediaType.APPLICATION_JSON | "http://localhost:8585/invalid"      | ''                                 | HttpClientErrorException.NotFound
        HttpMethod.GET    | MediaType.APPLICATION_JSON | "http://localhost:8585/"             | ''                                 | HttpClientErrorException.NotFound
        HttpMethod.GET    | MediaType.APPLICATION_JSON | "http://localhost:8585/profiles"     | ''                                 | HttpClientErrorException.NotFound
        HttpMethod.GET    | MediaType.APPLICATION_JSON | "http://localhost:8585/profile"      | ''                                 | HttpClientErrorException.NotFound
        HttpMethod.GET    | MediaType.APPLICATION_JSON | "http://localhost:8585/profile/user" | ''                                 | HttpClientErrorException.BadRequest
        HttpMethod.PUT    | MediaType.APPLICATION_JSON | "http://localhost:8585/nonexistent"  | '{}'                               | HttpClientErrorException.NotFound
        HttpMethod.PUT    | MediaType.APPLICATION_JSON | "http://localhost:8585/profile"      | '{}'                               | HttpClientErrorException.NotFound
        HttpMethod.DELETE | MediaType.APPLICATION_JSON | "http://localhost:8585/nonexistent"  | ''                                 | HttpClientErrorException.NotFound
        HttpMethod.POST   | MediaType.TEXT_HTML        | "http://localhost:8585/profile/"     | '{}'                               | HttpClientErrorException.UnsupportedMediaType
        HttpMethod.POST   | MediaType.APPLICATION_JSON | "http://localhost:8585/profile/"     | '{"not-field":3456,"name":"FAIL"}' | HttpServerErrorException.InternalServerError

    }

    static HttpHeaders getJsonHeaders(MediaType type) {
        HttpHeaders headers = new HttpHeaders()
        headers.setContentType(type)
        return headers
    }
}
