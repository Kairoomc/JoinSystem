package me.kairo.event.commands;

import me.kairo.event.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

public class ToggleEventCommand implements CommandExecutor {
    private final Main main;
    public ToggleEventCommand(Main main) {
        this.main = main;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 1) {
            sender.sendMessage("§cUsage: /toggleevent <nom>");
            return false;
        }

        String eventName = args[0];
        ConfigurationSection eventsSection = main.getConfig().getConfigurationSection("events");
        if (eventsSection == null || !eventsSection.contains(eventName)) {
            sender.sendMessage("§cL'événement " + eventName + " n'existe pas.");
            return false;
        }

        boolean isActive = eventsSection.getBoolean(eventName + ".active");
        eventsSection.set(eventName + ".active", !isActive);
        main.saveConfig();

        sender.sendMessage("§aL'événement " + eventName + " est maintenant " + (isActive ? "§cDÉSACTIVÉ" : "§aACTIVÉ") + "§a !");
        return true;
    }
}
