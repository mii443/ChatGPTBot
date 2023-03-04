package codes.mii.chatgptbot;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Objects;

import static org.bukkit.Bukkit.getServer;

public class OnAsyncPlayerChatEvent implements Listener {
    @EventHandler
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent e) {
        FileConfiguration config = ChatGPTBot.getPlugin(ChatGPTBot.class).getConfig();

        // check prefix
        String prefix = config.getString("chat_prefix");
        if (!e.getMessage().startsWith(Objects.requireNonNull(prefix))) return;

        String message = e.getMessage().replaceFirst(prefix, "");
        String username = e.getPlayer().getName();
        Location location = e.getPlayer().getLocation();
        List<Double> position = List.of(location.getX(), location.getY(), location.getZ());

        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                // generate response
                ChatGPTInput request = new ChatGPTInput(username, message, position);
                ChatGPTResponse response = ChatGPT.chat(request);

                // format
                String response_message = Objects.requireNonNull(config.getString("response_format"))
                        .replace("%TO%", response.to)
                        .replace("%MESSAGE%", response.response_message);
                getServer().broadcastMessage(response_message);

                // dispatch commands
                getServer().getScheduler().runTask(ChatGPTBot.getPlugin(ChatGPTBot.class), () -> {
                    response.commands.forEach(raw_command -> {
                        String command = raw_command.replaceFirst("/", "");
                        getServer().getLogger().info("ChatGPT: /" + command);

                        if (config.getBoolean("dispatch_command_as_player")) {
                            getServer().dispatchCommand(e.getPlayer(), command);
                        } else {
                            getServer().dispatchCommand(ChatGPTBot.getPlugin(ChatGPTBot.class).getServer().getConsoleSender(), command);
                        }
                    });
                });
            }
        };

        Thread chatgptThread = new Thread(runnable);
        chatgptThread.start();
    }
}
