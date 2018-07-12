/*
 * Copyright (c) 2018.
 * created by Christian34
 */

package com.Christian34.varo.Files;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import com.Christian34.varo.GameStates.GameState;

public class File_Data {

    private static File file;
    public static FileConfiguration data;

    File_Data() {
        file = new File("plugins/Varo", "data.yml");
        data = YamlConfiguration.loadConfiguration(file);
        data.options().copyDefaults(true);
        data.options().header("############################################################\n" +
                "# +------------------------------------------------------+ #\n" +
                "# |                  - Data File -                       | #\n" +
                "# |              Plugin by Christian34	 		 	           | #\n" +
                "# +------------------------------------------------------+ #\n" +
                "############################################################\n");
        data.addDefault("data.game.state", GameState.NOGAME_STATE);
        saveData();
    }

    public static void saveData() {
        try {
            data.save(file);
            file = new File("plugins/Varo", "data.yml");
            data = YamlConfiguration.loadConfiguration(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadData() {
        file = new File("plugins/Varo", "data.yml");
        data = YamlConfiguration.loadConfiguration(file);
    }

}