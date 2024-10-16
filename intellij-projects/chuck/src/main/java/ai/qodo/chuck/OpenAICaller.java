package ai.qodo.chuck;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.output.Response;

import static dev.langchain4j.model.openai.OpenAiChatModelName.GPT_4_O;

public class OpenAICaller implements LLMCaller {
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
        Response<AiMessage> message = model.generate(systemChat, userChat);
        return message.content().text();
    }
}
