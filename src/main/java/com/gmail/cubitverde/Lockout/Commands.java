package com.gmail.cubitverde.Lockout;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
            return;
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
            return;
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
            return;
        }

        Course course = Utilities.GetCourse(name);
        sender.sendMessage(" ");
        sender.sendMessage(ChatColor.DARK_GREEN + "[Lockout] Information for course " + ChatColor.GREEN + name + ChatColor.DARK_GREEN + ":");
        Location start = course.getStart();
        if (start == null) {
            sender.sendMessage(ChatColor.DARK_GREEN + " - Start location: Not set");
        } else {
            sender.sendMessage(ChatColor.DARK_GREEN + " - Start location: " + start.getBlockX() + " " + start.getBlockY() + " " + start.getBlockZ());
        }
        Location lobby = course.getLobby();
        if (lobby == null) {
            sender.sendMessage(ChatColor.DARK_GREEN + " - Lobby location: Not set");
        } else {
            sender.sendMessage(ChatColor.DARK_GREEN + " - Lobby location: " + lobby.getBlockX() + " " + lobby.getBlockY() + " " + lobby.getBlockZ());
        }
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

    static void CommandTime(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(ChatColor.DARK_GREEN + "[Lockout] Command usage: " + ChatColor.GREEN + "/lockout time <name> <time>");
            return;
        }

        String name = args[1].toLowerCase();
        if (Utilities.CheckNameAvailable(name)) {
            sender.sendMessage(ChatColor.DARK_GREEN + "[Lockout] There is no course named " + ChatColor.GREEN + name + ChatColor.DARK_GREEN + ".");
            return;
        }

        int time = Utilities.ReadTime(args[2]);
        if (time <= 0) {
            sender.sendMessage(ChatColor.DARK_GREEN + "[Lockout] Invalid time input: " + ChatColor.GREEN + args[2] + ChatColor.DARK_GREEN + ". Time must be a positive integer followed by h, m, or s.");
            return;
        }

        Course course = Utilities.GetCourse(name);
        course.setTime(time);
        Utilities.SaveCourse(course);
        sender.sendMessage(ChatColor.DARK_GREEN + "[Lockout] Set time limit for course " + ChatColor.GREEN + name + ChatColor.DARK_GREEN + " to " + ChatColor.GREEN + Utilities.ConvertSeconds(time) + ChatColor.DARK_GREEN + ".");
    }

    static void CommandAttempts(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(ChatColor.DARK_GREEN + "[Lockout] Command usage: " + ChatColor.GREEN + "/lockout attempts <name> <number>");
            return;
        }

        String name = args[1].toLowerCase();
        if (Utilities.CheckNameAvailable(name)) {
            sender.sendMessage(ChatColor.DARK_GREEN + "[Lockout] There is no course named " + ChatColor.GREEN + name + ChatColor.DARK_GREEN + ".");
            return;
        }

        int attempts;
        try {
            attempts = Integer.parseInt(args[2]);
        } catch (Exception e) {
            sender.sendMessage(ChatColor.DARK_GREEN + "[Lockout] Invalid attempts input: " + ChatColor.GREEN + args[2] + ChatColor.DARK_GREEN + ". Attempts must be a positive integer.");
            return;
        }

        if (attempts <= 0) {
            sender.sendMessage(ChatColor.DARK_GREEN + "[Lockout] Invalid attempts input: " + ChatColor.GREEN + args[2] + ChatColor.DARK_GREEN + ". Attempts must be a positive integer.");
            return;
        }

        Course course = Utilities.GetCourse(name);
        course.setAttempts(attempts);
        Utilities.SaveCourse(course);
        sender.sendMessage(ChatColor.DARK_GREEN + "[Lockout] Set attempts for course " + ChatColor.GREEN + name + ChatColor.DARK_GREEN + " to " + ChatColor.GREEN + attempts + ChatColor.DARK_GREEN + ".");
    }

    static void CommandSetlobby(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_GREEN + "[Lockout] This command can only be used by players.");
            return;
        }
        if (args.length < 2) {
            sender.sendMessage(ChatColor.DARK_GREEN + "[Lockout] Command usage: " + ChatColor.GREEN + "/lockout setlobby <name>");
            return;
        }

        String name = args[1].toLowerCase();
        if (Utilities.CheckNameAvailable(name)) {
            sender.sendMessage(ChatColor.DARK_GREEN + "[Lockout] There is no course named " + ChatColor.GREEN + name + ChatColor.DARK_GREEN + ".");
            return;
        }

        Player player = (Player) sender;
        Course course = Utilities.GetCourse(name);
        course.setLobby(player.getLocation());
        Utilities.SaveCourse(course);
        sender.sendMessage(ChatColor.DARK_GREEN + "[Lockout] The lobby for course " + ChatColor.GREEN + name + ChatColor.DARK_GREEN + " has been changed to your current location.");
    }

    static void CommandSetstart(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_GREEN + "[Lockout] This command can only be used by players.");
            return;
        }
        if (args.length < 2) {
            sender.sendMessage(ChatColor.DARK_GREEN + "[Lockout] Command usage: " + ChatColor.GREEN + "/lockout setstart <name>");
            return;
        }

        String name = args[1].toLowerCase();
        if (Utilities.CheckNameAvailable(name)) {
            sender.sendMessage(ChatColor.DARK_GREEN + "[Lockout] There is no course named " + ChatColor.GREEN + name + ChatColor.DARK_GREEN + ".");
            return;
        }

        Player player = (Player) sender;
        Course course = Utilities.GetCourse(name);
        course.setStart(player.getLocation());
        Utilities.SaveCourse(course);
        sender.sendMessage(ChatColor.DARK_GREEN + "[Lockout] The start for course " + ChatColor.GREEN + name + ChatColor.DARK_GREEN + " has been changed to your current location.");
    }

    static void CommandStart(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(ChatColor.DARK_GREEN + "[Lockout] Command usage: " + ChatColor.GREEN + "/lockout start <name> <player>");
            return;
        }

        String name = args[1].toLowerCase();
        if (Utilities.CheckNameAvailable(name)) {
            sender.sendMessage(ChatColor.DARK_GREEN + "[Lockout] There is no course named " + ChatColor.GREEN + name + ChatColor.DARK_GREEN + ".");
            return;
        }

        String playerName = args[2].toLowerCase();
        Player player = Lockout.plugin.getServer().getPlayer(playerName);
        if (player == null || !player.isOnline()) {
            sender.sendMessage(ChatColor.DARK_GREEN + "[Lockout] Player " + ChatColor.GREEN + playerName + ChatColor.DARK_GREEN + " is not online and can not start the course.");
            return;
        }

        if (Utilities.CheckPlayerInCourse(playerName)) {
            sender.sendMessage(ChatColor.DARK_GREEN + "[Lockout] Player " + ChatColor.GREEN + playerName + ChatColor.DARK_GREEN + " is already in a course.");
            return;
        }

        Course course = Utilities.GetCourse(name);
        if (Utilities.CheckPlayerAttempts(playerName) >= course.getAttempts()) {
            sender.sendMessage(ChatColor.DARK_GREEN + "[Lockout] Player " + ChatColor.GREEN + playerName + ChatColor.DARK_GREEN + " has reached the maximum number of attempts for that course.");
            player.sendMessage(ChatColor.DARK_GREEN + "[Lockout] You have reached the maximum number of attempts for that course.");
            return;
        }

        Utilities.StartCourseForPlayer(course, player);
        sender.sendMessage(ChatColor.DARK_GREEN + "[Lockout] Player " + ChatColor.GREEN + playerName + ChatColor.DARK_GREEN + " has started course " + ChatColor.GREEN + name + ChatColor.DARK_GREEN + ".");
    }

    static void CommandEnd(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ChatColor.DARK_GREEN + "[Lockout] Command usage: " + ChatColor.GREEN + "/lockout end <player>");
            return;
        }

        String playerName = args[1].toLowerCase();
        Player player = Lockout.plugin.getServer().getPlayer(playerName);
        if (player == null || !player.isOnline()) {
            sender.sendMessage(ChatColor.DARK_GREEN + "[Lockout] Player " + ChatColor.GREEN + playerName + ChatColor.DARK_GREEN + " is not online and can not end the course.");
            return;
        }

        if (!Utilities.CheckPlayerInCourse(playerName)) {
            sender.sendMessage(ChatColor.DARK_GREEN + "[Lockout] Player " + ChatColor.GREEN + playerName + ChatColor.DARK_GREEN + " is not in a course.");
            return;
        }

        Course course = Utilities.GetPlayerCourse(playerName);
        if (course == null) {
            sender.sendMessage(ChatColor.DARK_GREEN + "[Lockout] Player " + ChatColor.GREEN + playerName + ChatColor.DARK_GREEN + " is not in a course.");
            return;
        }
        String name = course.getName();

        Utilities.EndCourseForPlayer(course, player);
        sender.sendMessage(ChatColor.DARK_GREEN + "[Lockout] Player " + ChatColor.GREEN + playerName + ChatColor.DARK_GREEN + " has started course " + ChatColor.GREEN + name + ChatColor.DARK_GREEN + ".");
    }


}
