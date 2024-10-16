package ai.qodo.chuck;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SwearScrubber implements Scrubber {
    private final Logger logger = Logger.getLogger(SwearScrubber.class.getName());
    private final Set<String> swearWords = new HashSet<>();

    public SwearScrubber() {
        loadSwearWords();
    }

    private void loadSwearWords() {
        try (InputStream inputStream = getClass().getResourceAsStream("/swearwords.txt"); BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                swearWords.add(line.trim());
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error loading swearwords", e);
        }
    }

    public String scrub(String joke) {
        if (joke == null || joke.isEmpty()) {
            return joke;
        }

        String[] words = joke.split("\\s+");
        StringBuilder scrubbedJoke = new StringBuilder();

        for (String word : words) {
            if (isSwearWord(word)) {
                scrubbedJoke.append("Bleep");
            } else {
                scrubbedJoke.append(word);
            }
            scrubbedJoke.append(" ");
        }

        // Remove the trailing space
        return scrubbedJoke.toString().trim();
    }


    public boolean isSwearWord(String word) {
        return swearWords.contains(word.toLowerCase());
    }
}