package ai.qodo.chuck;

import dev.langchain4j.model.openai.OpenAiChatModel;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CommandLine {
    public static final String OPENAI_API_KEY = "OPENAI_API_KEY";
    private static final Logger log = Logger.getLogger(CommandLine.class.getName());
    private final Joke joke;
    private final CommandFlags flags;
    private final LLMCaller caller;
    private final Scrubber scrubber;
    private final ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

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
    public void start() {
        LoggerConfig.configureGlobalLogger(flags);
        log.info(flags.toString());
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.println("Enter your name or (Q)uit:");
                String input = scanner.nextLine();
                if (input.equalsIgnoreCase("Q")) {
                    break;
                }
                author(input, flags.getFlagValue("output"));
            }
        } finally {
            System.out.println("Shutting Down...");
            executor.shutdown();
            try {
                if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
        System.out.println("Good Bye!");
    }

    private void author(final String name, final Optional<String> path) {
        executor.submit(() -> {
            String jokeResponse = joke.joke(name);
            String response = caller.call(scrubber.scrub(jokeResponse));
            if (flags.hasFlag("console")) {
                System.out.println(response);
            }
            if (path.isPresent()) {
                String outputFileName;
                String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String output = path.get();
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
                try (FileWriter fileWriter = new FileWriter(outputFileName, true)) {
                    fileWriter.write(response + System.lineSeparator());
                } catch (IOException e) {
                    log.log(Level.SEVERE, "Failed to write response to file", e);
                }
            }
        });
    }

}