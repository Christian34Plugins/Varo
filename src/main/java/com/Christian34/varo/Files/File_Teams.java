/*
 * Copyright (c) 2018.
 * created by Christian34
 */

package com.Christian34.varo.Files;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class File_Teams {

    public static File file;
    public static FileConfiguration data;

    File_Teams() {
        file = new File("plugins/Varo/teams.yml");
        data = YamlConfiguration.loadConfiguration(file);
        FileConfiguration teams = data;
        try {
            teams.save(file);
            teams.options().copyDefaults(true);
            teams.options().header("############################################################\n" +
                    "# +------------------------------------------------------+ #\n" +
                    "# |       				File_Teams						   | #\n" +
                    "# |              Plugin by Christian34	 			   | #\n" +
                    "# +------------------------------------------------------+ #\n" +
                    "############################################################\n");
            String a[] = {};
            teams.addDefault("teams.list", a);
            teams.save(file);
        } catch (IOException e) {
            throw new RuntimeException("error 1");
        }
    }

    public static void save() {
        try {
            data.save(file);
            file = new File("plugins/Varo/teams.yml");
            data = YamlConfiguration.loadConfiguration(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}