/*
 * Copyright (c) 2018.
 * created by Christian34
 */

package com.Christian34.varo.Chat;

import com.Christian34.varo.Files.File_Config;
import com.Christian34.varo.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class Chat {

    private ChatManager[] messages = new ChatManager[2];
    private static String prefix;
    private de_DE de;
    private en_EN en;

    public Chat(Plugin plugin) {
        messages[ChatManager.LANG_DE] = new de_DE();
        messages[ChatManager.LANG_EN] = new en_EN();
        File messages = new File("plugins/Varo/messages");
        if (!messages.exists()) {
            messages.mkdir();
            plugin.saveResource("de_DE.yml", false);
            plugin.saveResource("en_EN.yml", false);
            new File("plugins/Varo/de_DE.yml").renameTo(new File("plugins/Varo/messages/de_DE.yml"));
            new File("plugins/Varo/en_EN.yml").renameTo(new File("plugins/Varo/messages/en_EN.yml"));
        }
        de = new de_DE();
        en = new en_EN();
        prefix = getMessage("prefix", null);

    }

    public String getMessage(String key, Player target) {
        if (target != null) {
            User user = new User(target);
            return messages[user.getLang()].getMessage(key, target);
        } else {
            if (File_Config.data.getString("config.lang").equalsIgnoreCase("de_DE")) {
                return de.getMessage(key, null);
            } else {
                return en.getMessage(key, null);
            }
        }
    }

    public static void clear(Player player) {
        for (int i = 1; i <= 120; i++) {
            player.sendMessage(" ");
            i++;
        }
    }

    public static String getPrefix() {
        return prefix;
    }

    public static String separator = "§7===================================";

    public static void broadcast(String message) {
        Bukkit.broadcastMessage(Chat.getPrefix() + message);
    }

}