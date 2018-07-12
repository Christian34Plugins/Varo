package com.Christian34.varo.Chat;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;

import me.clip.placeholderapi.PlaceholderAPI;

public class de_DE extends ChatManager {

    private File file = new File("plugins/Varo/messages/de_DE.yml");
    private FileConfiguration data = YamlConfiguration.loadConfiguration(file);

    @Override
    public String getMessage(String key, Player player) {
        if (key.equalsIgnoreCase("prefix")) {
            String prefix = "§7[§bVaro§7] ";
            prefix = prefix.replace("§b", data.getString("prefix-color"));
            return prefix;
        } else {
            return PlaceholderAPI.setPlaceholders(player, data.getString(key));
        }
    }

}