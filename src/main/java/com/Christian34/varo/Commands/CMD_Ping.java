/*
 * Copyright (c) 2018.
 * created by Christian34
 */

package com.Christian34.varo.Commands;

import com.Christian34.varo.Chat.Chat;
import com.Christian34.varo.Varo;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CMD_Ping implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("ping")) {
            Player p = (Player) sender;
            p.sendMessage(Varo.chat().getMessage("ping-msg", p));
        }
        return false;
    }

}