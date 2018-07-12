/*
 * Copyright (c) 2018.
 * created by Christian34
 */

package com.Christian34.varo.Commands;

import com.Christian34.varo.Chat.Chat;
import com.Christian34.varo.User;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CMD_LastSeen implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("lastseen")) {
            Player p = (Player) sender;
            User user = new User(p);
            if (args.length > 0) {
                OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
                try {
                    User usr = new User((Player) target);
                    p.sendMessage(Chat.getPrefix() + usr.getPlayerData().getString("data.username") + " wurde zuletzt am §e" + usr.lastSeen().replace("-", "§7um§e") + " §7gesehen.");
                } catch (Exception e) {
                    p.sendMessage(Chat.getPrefix() + "§cSpieler wurde nicht gefunden!");
                }
            } else {
                user.performCommand("varo help");
            }

        }
        return false;
    }

}