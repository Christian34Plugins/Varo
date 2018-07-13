/*
 * Copyright (c) 2018.
 * created by Christian34
 */

package com.Christian34.varo.Events;

import com.Christian34.varo.GameStates.StartState;
import com.Christian34.varo.Varo;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteract implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent e) {
        if (Varo.getGameStateManager().getCurrentGameState() instanceof StartState) {
            e.setCancelled(true);
        }
        if (e.getItem() != null && e.getItem().hasItemMeta() && e.getItem().getItemMeta().hasDisplayName() && e.getItem().getItemMeta().getDisplayName().equals("§7§l》§e§lSetup§7§l《")) {
            Bukkit.dispatchCommand(e.getPlayer(), "varo setup");
        }
    }

}