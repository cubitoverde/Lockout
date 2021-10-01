package com.gmail.cubitverde.Lockout;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Clock extends BukkitRunnable {
    @Override
    public void run() {
        FileConfiguration config = Lockout.plugin.getConfig();

        for (Player player : Lockout.plugin.getServer().getOnlinePlayers()) {
            String playerName = player.getName();
            if (!Utilities.CheckPlayerInCourse(playerName)) {
                continue;
            }

            int time = config.getInt("Lockout.Players." + playerName + ".Time");

            if (time >= 0) {
                if (time > 120) {
                    player.sendTitle(" ", ChatColor.GREEN + Utilities.ConvertSeconds(time), 0, 25, 1);
                } else if (time > 30) {
                    player.sendTitle(" ", ChatColor.GOLD + Utilities.ConvertSeconds(time), 0, 25, 1);
                } else {
                    player.sendTitle(" ", ChatColor.DARK_RED + Utilities.ConvertSeconds(time), 0, 25, 1);
                }
                config.set("Lockout.Players." + playerName + ".Time", --time);
            } else {
                String name = config.getString("Lockout.Players." + playerName + ".Course");
                Course course = Utilities.GetCourse(name);
                player.teleport(course.getLobby());
                config.set("Lockout.Players." + playerName + ".Time", null);
                config.set("Lockout.Players." + playerName + ".Course", null);
                player.sendMessage(ChatColor.DARK_GREEN + "[Lockout] The time given to complete course " + ChatColor.GREEN + name + ChatColor.DARK_GREEN + " is over.");
            }
        }

        Lockout.plugin.saveConfig();
    }
}
