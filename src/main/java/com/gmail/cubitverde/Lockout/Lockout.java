package com.gmail.cubitverde.Lockout;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public class Lockout extends JavaPlugin {
    static Lockout plugin;

    @Override
    public void onEnable() {
        plugin = this;
        BukkitTask t1 = new Clock().runTaskTimer(this, 20, 20);

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
                break;
            }
            case "delete": {
                Commands.CommandDelete(sender, args);
                break;
            }
            case "list": {
                Commands.CommandList(sender);
                break;
            }
            case "info": {
                Commands.CommandInfo(sender, args);
                break;
            }
            case "time": {
                Commands.CommandTime(sender, args);
                break;
            }
            case "attempts": {
                Commands.CommandAttempts(sender, args);
                break;
            }
            case "setlobby": {
                Commands.CommandSetlobby(sender, args);
                break;
            }
            case "setstart": {
                Commands.CommandSetstart(sender, args);
                break;
            }
            case "start": {
                Commands.CommandStart(sender, args);
                break;
            }
            default: {
                Utilities.ShowCommands(sender);
                break;
            }
        }

        return true;
    }
}
