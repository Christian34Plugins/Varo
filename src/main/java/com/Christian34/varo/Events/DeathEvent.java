/*
 * Copyright (c) 2018.
 * created by Christian34
 */

package com.Christian34.varo.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathEvent implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();
        if (e.getEntity().getKiller() instanceof Player) {
            e.setDeathMessage("§e" + p.getName() + " §7wurde von §e" + p.getKiller() + " §7getötet!");
        } else {
            e.setDeathMessage("§e" + p.getName() + " §7ist gestorben!");
        }
    }

}