package codes.mii.chatgptbot;

import org.bukkit.plugin.java.JavaPlugin;

public final class ChatGPTBot extends JavaPlugin {
    @Override
    public void onEnable() {
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new OnAsyncPlayerChatEvent(), this);
    }
}
