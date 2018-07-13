/*
 * Copyright (c) 2018.
 * created by Christian34
 */

package com.Christian34.varo.Events;

import com.Christian34.varo.GameStates.StartState;
import com.Christian34.varo.Inventorys.InventorySettings;
import com.Christian34.varo.Lobby.Lobby;
import com.Christian34.varo.User;
import com.Christian34.varo.Varo;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import com.Christian34.varo.Commands.CMD_Build;

public class InvClickEvent implements Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void invClickEvent(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        User user = new User(p);
        if (Varo.getGameStateManager().getCurrentGameState() instanceof StartState) {
            e.setCancelled(true);
        }
        if (e.getClickedInventory() != null && e.getClickedInventory().getName() != null) {
            String clickedInv = e.getClickedInventory().getName();
            if (clickedInv.equalsIgnoreCase("setup") || clickedInv.equalsIgnoreCase("§3Spieler") || clickedInv.equalsIgnoreCase("§bLobby") || clickedInv.equalsIgnoreCase("§aAllgemeine Einstellungen")) {
                e.setCancelled(true);
            }
            if (e.getCurrentItem() != null && e.getCurrentItem().getItemMeta() != null) {
                if (e.getCurrentItem().getItemMeta().getDisplayName() != null) {
                    switch (e.getCurrentItem().getItemMeta().getDisplayName()) {
                        case "§7§l》§c§lBeenden":
                            p.closeInventory();
                            break;
                        case "§eNext Page":
                            com.Christian34.varo.Inventory.getTeamsList(p, true, false);
                            break;
                        case "§ePrevious Page":
                            com.Christian34.varo.Inventory.getTeamsList(p, false, true);
                            break;
                        case "§7§l》§c§lZurück":
                            switch (clickedInv) {
                                case "§7§l》§eTeam":
                                case "§7§l》§eSpieler Einstellungen":
                                    p.openInventory(InventorySettings.getSetupMenu(p));
                                    break;
                                case "§3Spieler":
                                case "§bLobby":
                                case "§aAllgemeine Einstellungen":
                                    user.performCommand("varo setup");
                                    break;
                                case "§7§l》§eSpieler":
                                    p.openInventory(InventorySettings.teamSettings(user));
                                    break;
                            }
                            e.setCancelled(true);
                    }
                }
            }
        }

        if (p.getLocation().getWorld().equals(Lobby.getWorld())) {
            if (CMD_Build.inBuildMode(p)) {
                e.setCancelled(false);
                return;
            }
            e.setCancelled(true);
        }
    }

}