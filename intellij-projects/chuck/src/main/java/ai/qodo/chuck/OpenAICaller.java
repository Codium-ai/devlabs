package ai.qodo.chuck;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.output.Response;

import java.util.logging.Level;
import java.util.logging.Logger;

import static dev.langchain4j.model.openai.OpenAiChatModelName.GPT_4_O;

public class OpenAICaller implements LLMCaller {
    private static final Logger log = Logger.getLogger(OpenAICaller.class.getName());
    private final OpenAiChatModel.OpenAiChatModelBuilder chatModelBuilder;

    public OpenAICaller(OpenAiChatModel.OpenAiChatModelBuilder chatModelBuilder) {
        this.chatModelBuilder = chatModelBuilder;
    }


    public String call(String joke) {
        String apiKey = System.getenv(CommandLine.OPENAI_API_KEY); // Replace with your actual OpenAI API key
        ChatLanguageModel model = chatModelBuilder.apiKey(apiKey).modelName(GPT_4_O).maxTokens(500).build();
        // Initialize OpenAI client with your API key
        ChatMessage systemChat = new SystemMessage("You will channel the essence of George Carlin as a comedian for " + "turning the joke into a funny story that is at least 2 paragraphs and no longer than a page.");
        ChatMessage userChat = new UserMessage(joke);
        try {
            Response<AiMessage> message = model.generate(systemChat, userChat);
            return message.content().text();
        } catch (Exception e) {
            log.log(Level.SEVERE, "Failed to generate response", e);
            return "Failed to generate funny story by calling openAI " + e.getMessage();
        }
    }
}
