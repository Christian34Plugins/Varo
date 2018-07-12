/*
 * Copyright (c) 2018.
 * created by Christian34
 */

package com.Christian34.varo.Files;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class File_Config {

    private static File file;
    public static FileConfiguration data;

    public File_Config() {
        file = new File("plugins/Varo", "config.yml");
        data = YamlConfiguration.loadConfiguration(file);
        data.options().copyDefaults(true);
        data.options().header("############################################################\n" +
                "# +------------------------------------------------------+ #\n" +
                "# |                    - File_Config -                        | #\n" +
                "# |              Plugin by Christian34	 			   | #\n" +
                "# +------------------------------------------------------+ #\n" +
                "############################################################\n");
        data.addDefault("config.enabled", false);
        data.addDefault("config.lang", "de_DE");
        data.addDefault("config.lobby.scoreboard.header", "§7§l》 §f§lVARO §7§l《");
        String[] content = {"%empty%", "§fSpieler online§7:", "§a%server_online%§7/%server_max_players%", "%empty%", "§fTeam§7:", "§b%team%", "%empty%", "§fPing§7:", "§a%player_ping%"};
        data.addDefault("config.lobby.scoreboard.content", content);
        data.addDefault("config.allow-commands", false);
        data.addDefault("config.game.players", 4);
        data.addDefault("config.game.players-per-team", 2);
        data.addDefault("config.game.max-play-time", 15);
        data.addDefault("config.game.allow-respawn", true);
        saveConfig();
    }

    public static void saveConfig() {
        try {
            data.save(file);
            file = new File("plugins/Varo", "config.yml");
            data = YamlConfiguration.loadConfiguration(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

   /* public static void loadConfig() {
        file = new File("plugins/Varo", "config.yml");
        data = YamlConfiguration.loadConfiguration(file);
    } */

    public static Integer getPlayers() {
        return data.getInt("config.game.players");
    }

    public static Integer getPlayersPerTeam() {
        return data.getInt("config.game.players-per-team");
    }

}