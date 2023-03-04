package codes.mii.chatgptbot;

import java.util.List;

public class ChatGPTResponse {
    public String response_message;
    public String to;
    public List<String> commands;

    public ChatGPTResponse(String response_message, String to, List<String> commands) {
        this.response_message = response_message;
        this.to = to;
        this.commands = commands;
    }
}
