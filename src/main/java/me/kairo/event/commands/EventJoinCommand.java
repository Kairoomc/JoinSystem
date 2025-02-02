package me.kairo.event.commands;

import me.kairo.event.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

public class EventJoinCommand implements CommandExecutor {

    private final Main plugin;

    public EventJoinCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 3) {
            sender.sendMessage("§cUsage: /eventjoin <nom> <nombre> <commande>");
            return false;
        }

        String eventName = args[0];
        int maxPlayers;
        try {
            maxPlayers = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage("§cLe nombre de joueurs doit être un chiffre !");
            return false;
        }

        String commandToExecute = String.join(" ", args).substring(args[0].length() + args[1].length() + 2);

        ConfigurationSection eventsSection = plugin.getConfig().getConfigurationSection("events");
        if (eventsSection == null) {
            eventsSection = plugin.getConfig().createSection("events");
        }

        ConfigurationSection newEvent = eventsSection.createSection(eventName);
        newEvent.set("nombreDeJoueurs", maxPlayers);

        List<String> commandList = new ArrayList<>();
        commandList.add(commandToExecute);
        newEvent.set("commandesConsole", commandList);

        plugin.saveConfig();
        sender.sendMessage("§aL'événement " + eventName + " a été créé avec succès !");
        return true;
    }
}
