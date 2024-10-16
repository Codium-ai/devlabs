package ai.qodo.chuck;

import dev.langchain4j.model.openai.OpenAiChatModel;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
                log.fine(OPENAI_API_KEY + " environment variable set.");
            } else {
                log.warning(OPENAI_API_KEY + " flag is present but no value is provided.");
            }
        } else {
            log.warning(OPENAI_API_KEY + " flag not found make sure you set your environment variable.");
        }
    }

    public void start() {
        LoggerConfig.configureGlobalLogger(flags);
        log.info(flags.toString());
        openAPIKey(flags);
        Scanner scanner = new Scanner(System.in);
        String output = flags.getFlagValue("output");
        while (true) {
            System.out.println("Enter your name or (Q)uit:");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("Q")) {
                break;
            }
            author(input, output);
        }
        System.out.println("Good Bye!");
    }

    private void author(final String name, final String output) {
        Thread.ofVirtual().start(() -> {
            String jokeResponse = joke.joke(name);
            String response = caller.call(scrubber.scrub(jokeResponse));
            if(flags.hasFlag("console")) {
                System.out.println(response);
            }
            if (output != null) {
                try {
                    String outputFileName;
                    String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    int dotIndex = output.lastIndexOf('.');
                    if (dotIndex != -1) {
                        // Split the file name and extension
                        String baseName = output.substring(0, dotIndex);
                        String extension = output.substring(dotIndex);
                        // Construct the new file name
                        outputFileName = baseName + "_" + name + "_" + timestamp + extension;
                    } else {
                        // If no extension, just append name and timestamp
                        outputFileName = output + "_" + name + "_" + timestamp;
                    }
                    FileWriter fileWriter = new FileWriter(outputFileName, true);
                    fileWriter.write(response + System.lineSeparator());
                    fileWriter.close();
                } catch (IOException e) {
                    log.log(Level.SEVERE, "Failed to write response to file", e);
                }
            }
        });
    }

}