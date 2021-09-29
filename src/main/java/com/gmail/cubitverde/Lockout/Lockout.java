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

        switch (args[0]) {
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
            default: {
                Utilities.ShowCommands(sender);
            }
        }

        return true;
    }
}
