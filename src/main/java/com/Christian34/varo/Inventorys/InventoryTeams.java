/*
 * Copyright (c) 2018.
 * created by Christian34
 */

package com.Christian34.varo.Inventorys;

import com.Christian34.varo.Files.FileManager;
import com.Christian34.varo.GameStates.StartState;
import com.Christian34.varo.User;
import com.Christian34.varo.Varo;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class InventoryTeams implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (Varo.getGameStateManager().getCurrentGameState() instanceof StartState) {
            e.setCancelled(true);
        }
        if (p.getLocation().getWorld() == Bukkit.getServer().getWorld("lobby")) {
            if (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) {
                if (e.getItem() != null && e.getItem().getItemMeta().hasDisplayName()) {
                    if (e.getItem().getType() == Material.BED && e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§7§l》§e§lTeams§7§l《")) {
                        if (FileManager.getTeams().getStringList("teams.list").isEmpty()) {
                            com.Christian34.varo.Inventory.getTeamsInv(p);
                        } else {
                            com.Christian34.varo.Inventory.getTeamsList(p, false, false);
                        }
                    }
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void invClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        User user = new User(p);
        if (e.getClickedInventory() != null && e.getClickedInventory().getName() != null && e.getCurrentItem() != null) {
            if (e.getCurrentItem().getItemMeta() != null && e.getCurrentItem().getItemMeta().hasDisplayName()) {
                if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§7§l》§a§lTeam erstellen")) {
                    user.performCommand("team create");
                    p.closeInventory();
                } else if (e.getClickedInventory().getName().equalsIgnoreCase("§7§l》§eTeams")) {
                    if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§cTeam verlassen")) {
                        p.closeInventory();
                        Bukkit.dispatchCommand(p, "team leave");
                    } else {
                        String name = e.getCurrentItem().getItemMeta().getDisplayName();
                        for (int i = 0; i < name.length(); i++) {
                            if (name.charAt(i) == '§') {
                                name = name.substring(i + 1);
                                name = name.substring(i);
                            }
                        }
                        if (e.getCurrentItem() != null & e.getCurrentItem().getType() == Material.PAPER) {
                            user.joinTeam(name);
                        }
                        p.closeInventory();
                        if (FileManager.getTeams().getStringList("teams.list").isEmpty()) {
                            com.Christian34.varo.Inventory.getTeamsInv(p);
                        } else {
                            com.Christian34.varo.Inventory.getTeamsList(p, false, false);
                        }
                    }
                }
            }
        }
    }

}