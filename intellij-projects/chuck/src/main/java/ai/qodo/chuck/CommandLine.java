package ai.qodo.chuck;

import dev.langchain4j.model.openai.OpenAiChatModel;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CommandLine {
    public static final String OPENAI_API_KEY = "OPENAI_API_KEY";
    private static final Logger log = Logger.getLogger(CommandLine.class.getName());
    private final Joke joke;
    private final CommandFlags flags;
    private final LLMCaller caller;
    private final Scrubber scrubber;

    public CommandLine(Joke joke, CommandFlags flags, LLMCaller caller, Scrubber scrubber) {
        this.joke = joke;
        this.flags = flags;
        this.caller = caller;
        this.scrubber = scrubber;
    }

    public static void main(String[] args) {
        CommandFlags commandFlags = new CommandFlags(args);
        LLMCaller llmCaller = new OpenAICaller(OpenAiChatModel.builder());
        CommandLine commandLine = new CommandLine(new ChuckJoke(), commandFlags, llmCaller, new SwearScrubber());
        commandLine.start();
    }

    private void openAPIKey(CommandFlags flags) {
        if (flags.hasFlag(OPENAI_API_KEY)) {
            String apiKey = flags.getFlagValue(OPENAI_API_KEY);
            if (apiKey != null) {
                System.setProperty(OPENAI_API_KEY, apiKey);
                log.info(OPENAI_API_KEY + " environment variable set.");
            } else {
                log.warning(OPENAI_API_KEY + " flag is present but no value is provided.");
            }
        } else {
            log.info(OPENAI_API_KEY + " flag not found make sure you set your environment variable.");
        }
    }

    public void start() {
        System.out.println(flags);
        openAPIKey(flags);
        LoggerConfig.configureGlobalLogger(flags);
        Scanner scanner = new Scanner(System.in);
        FileWriter fileWriter = null;
        boolean writeToFile = false;
        String output = flags.getFlagValue("output");
        if (flags.hasFlag("output")) {
            if (output != null) {
                try {
                    fileWriter = new FileWriter(output, true);
                    writeToFile = true;
                } catch (IOException e) {
                    log.log(Level.SEVERE, "Failed to open file for writing", e);
                }
            }
        }
        while (true) {
            System.out.println("Enter your name or (Q)uit:");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("Q")) {
                break;
            }
            String jokeResponse = joke.joke(input);
            String response = caller.call(scrubber.scrub(jokeResponse));
            System.out.println(response);
            if (writeToFile) {
                try {
                    fileWriter.write(response + System.lineSeparator());
                } catch (IOException e) {
                    log.log(Level.SEVERE, "Failed to write response to file", e);
                }
            }
        }
        if (fileWriter != null) {
            try {
                fileWriter.close();
            } catch (IOException e) {
                log.log(Level.SEVERE, "Unable to close file " + output, e);
            }
        }
        System.out.println("Good Bye!");
    }
}