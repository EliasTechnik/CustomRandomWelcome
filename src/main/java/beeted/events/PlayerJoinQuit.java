package beeted.events;

import me.clip.placeholderapi.PlaceholderAPI;
import beeted.customwelcome.CustomWelcome;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import java.util.Random;

import java.util.List;

public class PlayerJoinQuit implements Listener {
    private final CustomWelcome plugin;

    public PlayerJoinQuit(CustomWelcome plugin) {
        this.plugin = plugin;
    }

    private String getRandomJoinMessage(FileConfiguration config){
        List<String> joinMotdList = config.getStringList("config.join-message");
        Random rand = new Random();
        return joinMotdList.get(rand.nextInt(joinMotdList.size() - 1));
    }

    private String getRandomQuitMessage(FileConfiguration config){
        List<String> quitMotdList = config.getStringList("config.quit-message");
        Random rand = new Random();
        return quitMotdList.get(rand.nextInt(quitMotdList.size() - 1));
    }

    private void spawnMultilineMessage(String message, Player player){
        String[] messageList = message.split("/n");

        for(int i = 0; i < messageList.length; i++){
            String motdtext = PlaceholderAPI.setPlaceholders(player, messageList[i]);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', motdtext));
        }
    }

    private void broadcastMultilineMessage(String message, Player player){
        String[] messageList = message.split("/n");

        for(int i = 0; i < messageList.length; i++){
            String motdtext = PlaceholderAPI.setPlaceholders(player, messageList[i]);
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', motdtext));
        }
    }

    @EventHandler
    public void Join(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        FileConfiguration config = plugin.getConfig();

        if (config.getString("config.enable-join-message").equals("true")) {
            broadcastMultilineMessage(getRandomJoinMessage(config), player);
        }

        if (config.getString("motd.enable-motd-message").equals("true")) {
            List<String> messages = plugin.getConfig().getStringList("motd.motd-message");

            for (int i = 0; i < messages.size(); i++) {
                String motdtext = PlaceholderAPI.setPlaceholders(player, messages.get(i));
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', motdtext));
            }
        }
        event.setJoinMessage(null);
    }

    @EventHandler
    public void Quit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        FileConfiguration config = plugin.getConfig();

        if (config.getString("config.enable-quit-message").equals("true")) {
            broadcastMultilineMessage(getRandomQuitMessage(config),player);
            //event.setQuitMessage(ChatColor.translateAlternateColorCodes('&', message));
        }
        event.setQuitMessage(null);
    }
}
