package codes.mii.chatgptbot;

import java.util.List;

public class ChatGPTInput {
    public String username;
    public String message;
    public List<Double> position;

    public ChatGPTInput(String username, String message, List<Double> position) {
        this.username = username;
        this.message = message;
        this.position = position;
    }
}
