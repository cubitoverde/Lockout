package com.gmail.cubitverde.Lockout;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.Map;

public class Commands {
    static void CommandNew(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ChatColor.DARK_GREEN + "[Lockout] Command usage: " + ChatColor.GREEN + "/lockout new <name>");
            return;
        }

        String name = args[1].toLowerCase();
        if (!Utilities.CheckNameAvailable(name)) {
            sender.sendMessage(ChatColor.DARK_GREEN + "[Lockout] There is already a course named " + ChatColor.GREEN + name + ChatColor.DARK_GREEN + ".");
        }
        Course course = Utilities.NewCourse(name);
        Utilities.SaveCourse(course);
        sender.sendMessage(ChatColor.DARK_GREEN + "[Lockout] A new course named " + ChatColor.GREEN + name + ChatColor.DARK_GREEN + " has been created.");
    }

    static void CommandDelete(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ChatColor.DARK_GREEN + "[Lockout] Command usage: " + ChatColor.GREEN + "/lockout delete <name>");
            return;
        }

        String name = args[1].toLowerCase();
        if (Utilities.CheckNameAvailable(name)) {
            sender.sendMessage(ChatColor.DARK_GREEN + "[Lockout] There is no course named " + ChatColor.GREEN + name + ChatColor.DARK_GREEN + ".");
        }
        Utilities.DeleteCourse(name);
        sender.sendMessage(ChatColor.DARK_GREEN + "[Lockout] The course named " + ChatColor.GREEN + name + ChatColor.DARK_GREEN + " has been deleted.");
    }

    static void CommandList(CommandSender sender) {
        List<String> list = Utilities.NamesList();

        sender.sendMessage(ChatColor.DARK_GREEN + "[Lockout] List of available courses:");
        for (String name : list) {
            sender.sendMessage(ChatColor.DARK_GREEN + " - " + ChatColor.GREEN + name);
        }

    }

    static void CommandInfo(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ChatColor.DARK_GREEN + "[Lockout] Command usage: " + ChatColor.GREEN + "/lockout info <name>");
            return;
        }

        String name = args[1].toLowerCase();
        if (Utilities.CheckNameAvailable(name)) {
            sender.sendMessage(ChatColor.DARK_GREEN + "[Lockout] There is no course named " + ChatColor.GREEN + name + ChatColor.DARK_GREEN + ".");
        }

        Course course = Utilities.GetCourse(name);
        sender.sendMessage(" ");
        sender.sendMessage(ChatColor.DARK_GREEN + "[Lockout] Information for course " + ChatColor.GREEN + name + ChatColor.DARK_GREEN + ":");
        Location start = course.getStart();
        sender.sendMessage(ChatColor.DARK_GREEN + " - Start location: " + start.getBlockX() + " " + start.getBlockY() + " " + start.getBlockZ());
        Location lobby = course.getLobby();
        sender.sendMessage(ChatColor.DARK_GREEN + " - Lobby location: " + lobby.getBlockX() + " " + lobby.getBlockY() + " " + lobby.getBlockZ());
        int time = course.getTime();
        sender.sendMessage(ChatColor.DARK_GREEN + " - Time limit: " + ChatColor.GREEN + Utilities.ConvertSeconds(time));
        int attempts = course.getAttempts();
        sender.sendMessage(ChatColor.DARK_GREEN + " - Max attempts: " + ChatColor.GREEN + attempts);
        Map<String, Integer> scores = course.getScores();
        int size = 0;
        if (scores != null) {
            size = scores.size();
        }
        sender.sendMessage(ChatColor.DARK_GREEN + " - Scores: " + size);
        sender.sendMessage(" ");
    }
}
