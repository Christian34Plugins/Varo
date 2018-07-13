/*
 * Copyright (c) 2018.
 * created by Christian34
 */

package com.Christian34.varo.Events;

import com.Christian34.varo.GameStates.StartState;
import com.Christian34.varo.Lobby.Lobby;
import com.Christian34.varo.Varo;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

import com.Christian34.varo.Files.File_Config;

public class FoodChangeEvent implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void foodChangeEvent(FoodLevelChangeEvent e) {
        Player p = (Player) e.getEntity();
        if (Varo.getGameStateManager().getCurrentGameState() instanceof StartState) {
            e.setCancelled(true);
        }
        if (p.getLocation().getWorld().equals(Lobby.getWorld())) {
            e.setCancelled(true);
        }

    }

}