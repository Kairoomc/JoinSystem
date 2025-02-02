package me.kairo.event.manager;

import me.kairo.event.Main;
import org.bukkit.configuration.file.FileConfiguration;
import java.util.List;

public class ConfigManager {

    private final Main plugin;
    private final FileConfiguration config;

    public ConfigManager(Main plugin) {
        this.plugin = plugin;
        plugin.saveDefaultConfig();
        this.config = plugin.getConfig();
    }

    public int getMaxPlayers() {
        return config.getInt("nombreDeJoueurs", 5);
    }

    public String getConsoleCommand() {
        return config.getString("commandeConsole", "lp user <joueur> permission set tag.100er true");
    }

    public List<String> getRewardedPlayers() {
        return config.getStringList("rewardedPlayers");
    }

    public void saveRewardedPlayers(List<String> players) {
        config.set("rewardedPlayers", players);
        plugin.saveConfig();
    }
}