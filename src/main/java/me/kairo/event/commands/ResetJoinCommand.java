package me.kairo.event.commands;

import me.kairo.event.Main;
import me.kairo.event.manager.ConfigManager;
import me.kairo.event.manager.DataBaseManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;

public class ResetJoinCommand implements CommandExecutor {

    private final ConfigManager configManager;
    private final DataBaseManager databaseManager;


    public ResetJoinCommand(Main plugin) {
        this.configManager = plugin.getConfigManager();
        this.databaseManager = plugin.getDatabaseManager();

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            databaseManager.resetEvent(args[0]);
            sender.sendMessage("§a✔ L'événement " + args[0] + " a été réinitialisé !");
        } else {
            databaseManager.resetAllEvents();
            sender.sendMessage("§a✔ Tous les événements ont été réinitialisés !");
        }
        return true;
    }
}