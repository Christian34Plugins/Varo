/*
 * Copyright (c) 2018.
 * created by Christian34
 */

package com.Christian34.varo.Modules;

import com.Christian34.varo.Chat.ChatType;
import com.Christian34.varo.GameStates.IngameState;
import com.Christian34.varo.User;
import com.Christian34.varo.Varo;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ChestLock implements Listener {

    private BlockFace faces[] = {BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST};

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void sign(SignChangeEvent e) {
        Player p = e.getPlayer();
        if (p.getWorld() == Bukkit.getWorld("world")) {
            if (Varo.getGameStateManager().getCurrentGameState() instanceof IngameState) {
                User user = new User(p);
                if (e.getLine(0).equals("[Lock]")) {
                    e.setLine(0, "[§cLocked§0]");
                    e.setLine(1, "§0" + user.getTeam());
                    user.sendMessage("Truhe wurde gesichert!", ChatType.SUCCESS);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void openChest(InventoryOpenEvent e) {
        Player p = (Player) e.getPlayer();
        if (p.getWorld() == Bukkit.getWorld("world")) {
            if (Varo.getGameStateManager().getCurrentGameState() instanceof IngameState) {
                if (e.getInventory().getType() == InventoryType.CHEST) {
                    User user = new User(p);
                    Block chest = e.getInventory().getLocation().getBlock();
                    Block s = getSign(chest);
                    if (s != null) {
                        org.bukkit.block.Sign sign = (org.bukkit.block.Sign) s.getState();
                        if (isLocked(chest)) {
                            if (!hasAccess(chest, user)) {
                                user.sendMessage("Truhe ist gesichert!", ChatType.ERROR);
                                e.setCancelled(true);
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void destroyChest(BlockBreakEvent e) {
        Material block = e.getBlock().getType();
        if (block == Material.CHEST || block == Material.WALL_SIGN) {
            Player p = e.getPlayer();
            User user = new User(p);
            if (block == Material.CHEST) {
                if (isLocked(e.getBlock()) && !hasAccess(e.getBlock(), user)) {
                    e.setCancelled(true);
                }
            } else {
                org.bukkit.block.Sign sign = (org.bukkit.block.Sign) e.getBlock().getState();
                if (sign.getLine(0).equals("[§cLocked§0]")) {
                    if (!sign.getLine(1).equals(user.getTeam())) {
                        e.setCancelled(true);
                    }
                }
            }
        }
    }

    private Block getSign(Block chest) {
        for (BlockFace face : faces) {
            Block sign = chest.getRelative(face);
            if (sign.getType() == Material.WALL_SIGN) {
                return sign;
            }
        }
        return null;
    }

    private boolean isLocked(Block chest) {
        Block s = getSign(chest);
        if (s == null) return false;
        org.bukkit.block.Sign sign = (org.bukkit.block.Sign) s.getState();
        return sign.getLine(0).equals("[§cLocked§0]");
    }

    private boolean hasAccess(Block chest, User user) {
        Block s = getSign(chest);
        org.bukkit.block.Sign sign = (org.bukkit.block.Sign) s.getState();
        return sign.getLine(1).equals(user.getTeam());
    }

}