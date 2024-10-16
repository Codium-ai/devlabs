package ai.qodo.chuck;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChuckJoke implements Joke {
    private static final Logger logger = Logger.getLogger(ChuckJoke.class.getName());
    private static final String JOKE_API_URL = "https://api.chucknorris.io/jokes/random";
    private static final String DEFAULT_JOKE = "If a tree falls in the woods, not only did Chuck Norris hear it, he " + "probably kicked that motherfucker over too.";
    private static final Pattern pattern = Pattern.compile("Chuck Norris", Pattern.CASE_INSENSITIVE);

    public String joke(String name) {
        String joke = DEFAULT_JOKE;
        try {
            URL url = new URL(JOKE_API_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                JSONObject jsonResponse = new JSONObject(response.toString());
                joke = jsonResponse.getString("value");
            } else {
                System.out.println("GET request failed. Response Code: " + responseCode);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to contact Chuck ", e);
        }
        Matcher matcher = pattern.matcher(joke);
        return matcher.replaceAll(name);
    }
}