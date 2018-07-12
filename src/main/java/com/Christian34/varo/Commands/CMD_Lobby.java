/*
 * Copyright (c) 2018.
 * created by Christian34
 */

package com.Christian34.varo.Commands;

import com.Christian34.varo.Lobby.Lobby;
import com.Christian34.varo.User;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CMD_Lobby implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        User user = new User((Player) sender);
        if (cmd.getName().equalsIgnoreCase("lobby")) {
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("setspawn")) {
                    Location loc = user.getLocation();
                    Lobby.setSpawn(loc);
                    user.sendMessage("§aLobby wurde gesetzt!");
                } else {
                    user.performCommand("varo help");
                }
            } else {
                user.tpToLobby(true);
            }
        }
        return false;
    }

}