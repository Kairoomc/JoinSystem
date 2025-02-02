package me.kairo.event.listeners;

import me.kairo.event.Main;
import me.kairo.event.manager.ConfigManager;
import me.kairo.event.manager.DataBaseManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;
import java.util.UUID;

public class PlayerJoinListener implements Listener {

    private final Main plugin;
    private final DataBaseManager databaseManager;

    public PlayerJoinListener(Main plugin) {
        this.plugin = plugin;
        this.databaseManager = plugin.getDatabaseManager();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        ConfigurationSection eventsSection = plugin.getConfig().getConfigurationSection("events");
        if (eventsSection == null) return;

        for (String eventName : eventsSection.getKeys(false)) {
            if (!eventsSection.getBoolean(eventName + ".active")) continue;

            int maxPlayers = plugin.getConfig().getInt("events." + eventName + ".nombreDeJoueurs");
            List<String> commands = plugin.getConfig().getStringList("events." + eventName + ".commandesConsole");

            if (!databaseManager.hasReceivedReward(uuid, eventName)) {
                databaseManager.addRewardedPlayer(uuid, eventName);
                for (String cmd : commands) {
                    String commandToRun = cmd.replace("<joueur>", player.getName());
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commandToRun);
                }
                player.sendMessage(ChatColor.GREEN + "[Événement " + eventName + "] 🎉 Vous avez reçu une récompense !");
            }
        }
    }
}