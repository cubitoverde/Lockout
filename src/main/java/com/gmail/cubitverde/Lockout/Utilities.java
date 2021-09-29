package com.gmail.cubitverde.Lockout;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utilities {
    static void ShowCommands(CommandSender sender) {
        sender.sendMessage(" ");
        sender.sendMessage(ChatColor.DARK_GREEN + "---------< " + ChatColor.GREEN + "[Lockout]" + ChatColor.DARK_GREEN + " >---------");
        sender.sendMessage(" ");
        sender.sendMessage(ChatColor.GREEN + " /lockout new <name>" + ChatColor.WHITE + " - Create a new course.");
        sender.sendMessage(ChatColor.GREEN + " /lockout delete <name>" + ChatColor.WHITE + " - Delete a course.");
        sender.sendMessage(ChatColor.GREEN + " /lockout list" + ChatColor.WHITE + " - List all available courses.");
        sender.sendMessage(ChatColor.GREEN + " /lockout info <name>" + ChatColor.WHITE + " - Check a course's information.");
        sender.sendMessage(" ");
        sender.sendMessage(ChatColor.GREEN + " /lockout time <name> <time>" + ChatColor.WHITE + " - Set the time limit for a course.");
        sender.sendMessage(ChatColor.GREEN + " /lockout attempts <name> <number>" + ChatColor.WHITE + " - Set the number of times a player can attempt the course.");
        sender.sendMessage(ChatColor.GREEN + " /lockout setlobby <name>" + ChatColor.WHITE + " - Set the lobby for a course.");
        sender.sendMessage(ChatColor.GREEN + " /lockout setstart <name>" + ChatColor.WHITE + " - Set the starting point for a course.");
        sender.sendMessage(" ");
        sender.sendMessage(ChatColor.GREEN + " /lockout start <name> <player>" + ChatColor.WHITE + " - Make a player start a course.");
        sender.sendMessage(ChatColor.GREEN + " /lockout scores <name>" + ChatColor.WHITE + " - List the players that completed a course.");
        sender.sendMessage(ChatColor.GREEN + " /lockout remove <name> <player>" + ChatColor.WHITE + " - Remove a player's score from a course.");
        sender.sendMessage(" ");
    }

    static Course NewCourse(String name) {
        Course course = new Course();
        course.setName(name);
        course.setStart(null);
        course.setLobby(null);
        course.setTime(300);
        course.setAttempts(1);
        course.setScores(null);

        return course;
    }

    static boolean CheckNameAvailable(String name) {
        FileConfiguration config = Lockout.plugin.getConfig();

        if (config.getConfigurationSection("Lockout.Courses." + name) != null) {
            return false;
        } else {
            return true;
        }
    }

    static void SaveCourse(Course course) {
        FileConfiguration config = Lockout.plugin.getConfig();

        String name = config.getName();
        config.set("Lockout.Courses." + name + ".Start", course.getStart());
        config.set("Lockout.Courses." + name + ".Lobby", course.getLobby());
        config.set("Lockout.Courses." + name + ".Time", course.getTime());
        config.set("Lockout.Courses." + name + ".Attempts", course.getAttempts());
        if (course.getScores() != null) {
            for (String string : course.getScores().keySet()) {
                config.set("Lockout.Courses." + name + ".Scores." + string, course.getScores().get(string));
            }
        }

        Lockout.plugin.saveConfig();
    }

    static void DeleteCourse(String name) {
        FileConfiguration config = Lockout.plugin.getConfig();

        config.set("Lockout.Courses." + name, null);

        Lockout.plugin.saveConfig();
    }

    static List<String> NamesList() {
        FileConfiguration config = Lockout.plugin.getConfig();
        List<String> list = new ArrayList<>();

        if (config.getConfigurationSection("Lockout.Courses") != null) {
            list.addAll(config.getConfigurationSection("Lockout.Courses").getKeys(false));
        }

        return list;
    }

    static Course GetCourse(String name) {
        FileConfiguration config = Lockout.plugin.getConfig();

        Course course = new Course();
        course.setStart(config.getLocation("Lockout.Courses." + name + ".Start"));
        course.setLobby(config.getLocation("Lockout.Courses." + name + ".Lobby"));
        course.setTime(config.getInt("Lockout.Courses." + name + ".Time"));
        course.setAttempts(config.getInt("Lockout.Courses." + name + ".Attempts"));
        if (config.getConfigurationSection("Lockout.Courses." + name + ".Scores") != null) {
            Map<String, Integer> scores = new HashMap<>();
            for (String key : config.getConfigurationSection("Lockout.Courses." + name + ".Scores").getKeys(false)) {
                scores.put(key, config.getInt("Lockout.Courses." + name + ".Scores." + key));
            }
            course.setScores(scores);
        }

        return course;
    }

    static String ConvertSeconds(int time) {
        int hours = time / 3600;
        int minutes = time % 3600 / 60;
        int seconds = time % 60;

        String string = "" + hours + "h " + minutes + "m " + seconds + "s";

        return string;
    }

}
