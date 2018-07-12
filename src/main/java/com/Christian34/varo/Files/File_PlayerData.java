/*
 * Copyright (c) 2018.
 * created by Christian34
 */

package com.Christian34.varo.Files;

import com.Christian34.varo.Chat.Chat;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class File_PlayerData {

    public FileConfiguration data = null;
    private File file = null;
    private Player player;

    public File_PlayerData(Player player) {
        this.player = player;
        loadData();
        FileConfiguration data = this.data;
        Date currentDate = new Date();
        String formattedDate = new SimpleDateFormat("MM/dd/YYYY - HH:mm:ss").format(currentDate);
        try {
            data.save(file);
            data.options().copyDefaults(true);
            data.options().header("############################################################\n" +
                    "# +------------------------------------------------------+ #\n" +
                    "# |           - File_Data of " + player.getName() + " -               | #\n" +
                    "# |              Plugin by Christian34	 			           | #\n" +
                    "# +------------------------------------------------------+ #\n" +
                    "############################################################\n");
            data.addDefault("data.username", player.getName());
            data.addDefault("data.uuid", player.getUniqueId().toString());
            data.addDefault("data.lastseen", formattedDate);
            data.addDefault("data.state", 1);
            data.addDefault("data.allow-private-messages", true);
            data.addDefault("data.lang", File_Config.data.getString("config.lang"));
            data.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadData() {
        try {
            file = new File("plugins/Varo/playerdata", player.getUniqueId() + ".yml");
            data = YamlConfiguration.loadConfiguration(file);
        } catch (Exception e) {
            throw new RuntimeException(Chat.getPrefix() + "Player could not be loaded!");
        }
    }

    public void save() {
        FileConfiguration teams = data;
        try {
            teams.save(file);
            loadData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}