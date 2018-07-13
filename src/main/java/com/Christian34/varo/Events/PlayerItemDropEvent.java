/*
 * Copyright (c) 2018.
 * created by Christian34
 */

package com.Christian34.varo.Events;

import com.Christian34.varo.Files.File_Config;
import com.Christian34.varo.GameStates.StartState;
import com.Christian34.varo.Lobby.Lobby;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import com.Christian34.varo.Varo;

public class PlayerItemDropEvent implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onDrop(PlayerDropItemEvent e) {
        Player p = e.getPlayer();
        if (Varo.getGameStateManager().getCurrentGameState() instanceof StartState) {
            e.setCancelled(true);
        }
        if (p.getLocation().getWorld() == Lobby.getWorld()) {
            e.setCancelled(true);
        }
    }

}