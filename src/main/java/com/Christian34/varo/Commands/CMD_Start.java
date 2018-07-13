/*
 * Copyright (c) 2018.
 * created by Christian34
 */

package com.Christian34.varo.Commands;

import com.Christian34.varo.Chat.ChatType;
import com.Christian34.varo.GameStates.*;
import com.Christian34.varo.User;
import com.Christian34.varo.Varo;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CMD_Start implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("start")) {
            Player p = (Player) sender;
            User user = new User(p);
            if (p.hasPermission("varo.start")) {
                if (Varo.getGameStateManager().getCurrentGameState() instanceof LobbyState) {
                    for (Player target : Bukkit.getOnlinePlayers()) {
                        User user_target = new User(target);
                        if (user_target.getTeam().getName().equalsIgnoreCase("none")) {
                            user.sendMessage("Es müssen alle Spieler in einem Team sein!", ChatType.ERROR);
                            return false;
                        }
                    }
                    Varo.getGameStateManager().setGameState(GameState.START_STATE);
                    Varo.getGameStateManager().startCurrentGameState();
                } else {
                    user.sendMessage("Spiel konnte nicht gestartet werden!", ChatType.ERROR);
                }
            } else {
                p.sendMessage("NO PERMS");
            }
        }
        return false;
    }

}