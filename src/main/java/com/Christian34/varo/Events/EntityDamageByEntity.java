/*
 * Copyright (c) 2018.
 * created by Christian34
 */

package com.Christian34.varo.Events;

import com.Christian34.varo.GameStates.StartState;
import com.Christian34.varo.Lobby.Lobby;
import com.Christian34.varo.Varo;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityDamageByEntity implements Listener {

    @EventHandler
    public void damageEvent(EntityDamageByEntityEvent e) {
        if (Varo.getGameStateManager().getCurrentGameState() instanceof StartState) {
            e.setCancelled(true);
        }
        if (e.getDamager().getLocation().getWorld().equals(Lobby.getWorld())) {
            e.setCancelled(true);
        }
    }

}