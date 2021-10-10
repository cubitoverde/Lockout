package com.gmail.cubitverde.Lockout;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

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
        sender.sendMessage(ChatColor.GREEN + " /lockout end <player>" + ChatColor.WHITE + " - Successfully makes a player finish the course.");
        sender.sendMessage(ChatColor.GREEN + " /lockout scores <name>" + ChatColor.WHITE + " - List the players that completed a course.");
        sender.sendMessage(ChatColor.GREEN + " /lockout removescore <name> <player>" + ChatColor.WHITE + " - Remove a player's score from a course.");
        sender.sendMessage(ChatColor.GREEN + " /lockout refundattempt <name> <player>" + ChatColor.WHITE + " - Refunds one attempt from a player for a course.");
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

        if (config.getConfigurationSection("Lockout.Courses." + name) == null) {
            return true;
        } else {
            return false;
        }
    }

    static void SaveCourse(Course course) {
        FileConfiguration config = Lockout.plugin.getConfig();

        String name = course.getName();
        config.set("Lockout.Courses." + name, null);
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
        course.setName(name);
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

    static int ReadTime(String string) {
        int length = string.length();

        int time;
        try {
            time = Integer.parseInt(string.substring(0, length - 1));
        } catch (Exception e) {
            return -1;
        }

        if (string.charAt(length - 1) == 'h' || string.charAt(length - 1) == 'm' || string.charAt(length - 1) == 's') {
            switch (string.charAt(length - 1)) {
                case 'h':
                    time *= 60;
                case 'm':
                    time *= 60;
                case 's':
                    return time;
            }
        }

        return -1;
    }

    static boolean CheckPlayerInCourse(String playerName) {
        FileConfiguration config = Lockout.plugin.getConfig();

        if (config.getString("Lockout.Players." + playerName + ".Course") == null) {
            return false;
        } else {
            return true;
        }
    }

    static void StartCourseForPlayer(Course course, Player player) {
        FileConfiguration config = Lockout.plugin.getConfig();

        String playerName = player.getName();
        String name = course.getName();
        player.sendMessage(ChatColor.DARK_GREEN + "[Lockout] You are starting course " + ChatColor.GREEN + name + ChatColor.DARK_GREEN + ".");

        config.set("Lockout.Players." + playerName + ".Course", name);
        if (config.getConfigurationSection("Lockout.Players." + playerName + ".Attempts") == null) {
            config.set("Lockout.Players." + playerName + ".Attempts." + name, 1);
        } else {
            int attempts = config.getInt("Lockout.Players." + playerName + ".Attempts." + name);
            config.set("Lockout.Players." + playerName + ".Attempts." + name, ++attempts);
        }
        config.set("Lockout.Players." + playerName + ".Time", course.getTime());
        player.teleport(course.getStart());

        Lockout.plugin.saveConfig();
    }

    static int CheckPlayerAttempts(String playerName, Course course) {
        FileConfiguration config = Lockout.plugin.getConfig();
        String name = course.getName();

        if (config.getString("Lockout.Players." + playerName + ".Attempts." + name) == null) {
            return 0;
        } else {
            int attempts = config.getInt("Lockout.Players." + playerName + ".Attempts." + name);
            return attempts;
        }
    }

    static Course GetPlayerCourse(String playerName) {
        FileConfiguration config = Lockout.plugin.getConfig();

        if (config.getString("Lockout.Players." + playerName + ".Course") == null) {
            return null;
        } else {
            String name = config.getString("Lockout.Players." + playerName + ".Course");
            Course course = GetCourse(name);
            return course;
        }
    }

    static void EndCourseForPlayer(Course course, Player player) {
        FileConfiguration config = Lockout.plugin.getConfig();

        String playerName = player.getName();
        String name = course.getName();
        player.sendMessage(ChatColor.DARK_GREEN + "[Lockout] You have finished course " + ChatColor.GREEN + name + ChatColor.DARK_GREEN + ".");

        int time = config.getInt("Lockout.Players." + playerName + ".Time");
        config.set("Lockout.Players." + playerName + ".Course", null);
        config.set("Lockout.Players." + playerName + ".Time", null);
        player.teleport(course.getLobby());
        if (course.getScores() == null) {
            Map<String, Integer> scores = new HashMap<>();
            scores.put(playerName, time);
            course.setScores(scores);
        } else {
            Map<String, Integer> scores = course.getScores();
            if (scores.keySet().contains(playerName)) {
                if (scores.get(playerName) < time) {
                    scores.put(playerName, time);
                }
            } else {
                scores.put(playerName, time);
            }
            course.setScores(scores);
        }

        SaveCourse(course);
        Lockout.plugin.saveConfig();
    }

    static void ListScores(CommandSender sender, Course course) {
        String name = course.getName();
        Map<String, Integer> scores = course.getScores();
        if (scores == null || scores.size() == 0) {
            sender.sendMessage(ChatColor.DARK_GREEN + "[Lockout] There are no scores in course " + ChatColor.GREEN + name + ChatColor.DARK_GREEN + ".");
            return;
        }

        sender.sendMessage(" ");
        sender.sendMessage(ChatColor.DARK_GREEN + "[Lockout] List of scores for course " + ChatColor.GREEN + name + ChatColor.DARK_GREEN + ":");
        sender.sendMessage(ChatColor.DARK_GREEN + "(Scores are meassured by " + ChatColor.GREEN + "how much time was left" + ChatColor.DARK_GREEN + " when completing the course).");
        sender.sendMessage(" ");

        Map<String, Integer> sorted = scores.entrySet().stream().sorted(Map.Entry.comparingByValue()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        for (String playerName : sorted.keySet()) {
            sender.sendMessage(ChatColor.DARK_GREEN + " - " + ChatColor.GREEN + playerName + ChatColor.DARK_GREEN + " - " + ConvertSeconds(sorted.get(playerName)));
        }
        sender.sendMessage(" ");
    }

    static void SavePlayerAttempts(String playerName, Course course, int attempts) {
        FileConfiguration config = Lockout.plugin.getConfig();
        String name = course.getName();

        config.set("Lockout.Players." + playerName + ".Attempts." + name, attempts);
    }

}
