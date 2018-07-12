/*
 * Copyright (c) 2018.
 * created by Christian34
 */

package com.Christian34.varo.Files;

import org.bukkit.configuration.file.FileConfiguration;

public class FileManager {

    private File_Config config;
    private File_Data data;
    private File_Teams teams;

    public void init() {
        config = new File_Config();
        data = new File_Data();
        teams = new File_Teams();
    }

    public static FileConfiguration getConfig() {
        return File_Config.data;
    }

    public static void saveConfig() {
        File_Config.saveConfig();
    }

    public static FileConfiguration getData() {
        return File_Data.data;
    }

    public static void saveData() {
        File_Data.saveData();
    }

    public static FileConfiguration getTeams() {
        return File_Teams.data;
    }

    public static void saveTeams() {
        File_Teams.save();
    }

}