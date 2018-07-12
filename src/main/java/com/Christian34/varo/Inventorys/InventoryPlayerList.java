/*
 * Copyright (c) 2018.
 * created by Christian34
 */

package com.Christian34.varo.Inventorys;

import com.Christian34.varo.GameStates.StartState;
import com.Christian34.varo.Varo;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class InventoryPlayerList implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (Varo.getGameStateManager().getCurrentGameState() instanceof StartState) {
            e.setCancelled(true);
        }
        if (p.getLocation().getWorld() == Bukkit.getServer().getWorld("lobby")) {
            if (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) {
                if (e.getItem() != null && e.getItem().getItemMeta().hasDisplayName()) {
                    if (e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§7§l》§e§lSpieler§7§l《")) {
                        com.Christian34.varo.Inventory.invPlayerList(p);
                    }
                }
            }
        }
    }

}