package codes.mii.chatgptbot;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import com.google.gson.Gson;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class ChatGPT {
    public static ChatGPTResponse chat(ChatGPTInput input) {
        FileConfiguration config = ChatGPTBot.getPlugin(ChatGPTBot.class).getConfig();

        Gson gson = new Gson();
        String message = config.getString("base_input") + "\n" + gson.toJson(input);

        OpenAiService service = new OpenAiService(config.getString("openai_api_key"));
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .model(config.getString("model"))
                .messages(List.of(new ChatMessage("user", message)))
                .build();

        String response = service.createChatCompletion(chatCompletionRequest).getChoices().get(0).getMessage().getContent();

        return gson.fromJson(response, ChatGPTResponse.class);
    }
}
