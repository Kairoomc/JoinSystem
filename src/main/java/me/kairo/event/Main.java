package me.kairo.event;

import me.kairo.event.commands.EventJoinCommand;
import me.kairo.event.commands.ResetJoinCommand;
import me.kairo.event.commands.ToggleEventCommand;
import me.kairo.event.listeners.PlayerJoinListener;
import me.kairo.event.manager.ConfigManager;
import me.kairo.event.manager.DataBaseManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private ConfigManager configManager;
    private DataBaseManager databaseManager;

    @Override
    public void onEnable() {
        configManager = new ConfigManager(this);
        databaseManager = new DataBaseManager(this);

        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        getCommand("eventjoin").setExecutor(new EventJoinCommand(this));
        getCommand("resetjoin").setExecutor(new ResetJoinCommand(this));
        getCommand("toggleevent").setExecutor(new ToggleEventCommand(this));
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public DataBaseManager getDatabaseManager() {
        return databaseManager;
    }
}