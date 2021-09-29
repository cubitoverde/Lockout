package com.gmail.cubitverde.Lockout;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class Lockout extends JavaPlugin {
    static Lockout plugin;

    @Override
    public void onEnable() {
        plugin = this;

    }

    @Override
    public void onDisable() {

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args == null || args.length < 1) {
            Utilities.ShowCommands(sender);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "new": {
                Commands.CommandNew(sender, args);
            }
            case "delete": {
                Commands.CommandDelete(sender, args);
            }
            case "list": {
                Commands.CommandList(sender);
            }
            case "info": {
                Commands.CommandInfo(sender, args);
            }
            case "time": {
                Commands.CommandTime(sender, args);
            }
            case "attempts": {
                Commands.CommandAttempts(sender, args);
            }
            case "setlobby": {
                Commands.CommandSetlobby(sender, args);
            }
            case "setstart": {
                Commands.CommandSetstart(sender, args);
            }
            default: {
                Utilities.ShowCommands(sender);
            }
        }

        return true;
    }
}
