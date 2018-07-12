/*
 * Copyright (c) 2018.
 * created by Christian34
 */

package com.Christian34.varo.Inventorys;

import com.Christian34.varo.Chat.ChatType;
import com.Christian34.varo.User;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class InventorySettings implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (p.getLocation().getWorld() == Bukkit.getServer().getWorld("lobby")) {
            if (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) {
                if (e.getItem() != null && e.getItem().getItemMeta().hasDisplayName()) {
                    if (e.getItem().getType() == Material.NETHER_STAR && e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§7§l》§e§lEinstellungen§7§l《")) {
                        p.openInventory(getSetupMenu(p));
                        e.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void invClickEvent(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        User user = new User(p);
        ItemStack crntItem = e.getCurrentItem();
        Inventory inv = e.getClickedInventory();
        if (inv != null && inv.getName() != null) {
            switch (inv.getName()) {
                case "§7§l》§eEinstellungen":
                    if (crntItem != null && crntItem.getItemMeta() != null && crntItem.getItemMeta().hasDisplayName()) {
                        switch (e.getCurrentItem().getItemMeta().getDisplayName()) {
                            case "§eTeam":
                                if (user.getTeam() != null) {
                                    p.openInventory(teamSettings(user));
                                } else {
                                    p.closeInventory();
                                    user.sendMessage("Du bist in keinem Team!", ChatType.ERROR);
                                }
                                break;
                            case "§eSpieler Einstellungen":
                                p.openInventory(playerSettings(user));
                                break;
                            case "§7§l》§c§lBeenden":
                                p.closeInventory();
                                break;
                        }
                        e.setCancelled(true);
                    }
                    break;
                case "§7§l》§eTeam":
                    if (crntItem != null && crntItem.getItemMeta() != null && crntItem.getItemMeta().hasDisplayName()) {
                        if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§3Spieler")) {
                            p.openInventory(invTeamMembers(user));
                            e.setCancelled(true);
                        }
                    }
                    break;
                case "§7§l》§eSpieler Einstellungen":
                    if (crntItem != null && crntItem.getItemMeta() != null && crntItem.getItemMeta().hasDisplayName()) {
                        if (crntItem.getItemMeta().getDisplayName().equals("§a✔")) {
                            ItemStack item = new ItemStack(Material.INK_SACK, 1, DyeColor.GRAY.getDyeData());
                            ItemMeta meta = item.getItemMeta();
                            meta.setDisplayName("§7✘");
                            item.setItemMeta(meta);
                            inv.setItem(28, item);
                        } else if (crntItem.getItemMeta().getDisplayName().equals("§7✘")) {
                            ItemStack item = new ItemStack(Material.INK_SACK, 1, DyeColor.GREEN.getDyeData());
                            ItemMeta meta = item.getItemMeta();
                            meta.setDisplayName("§a✔");
                            item.setItemMeta(meta);
                            inv.setItem(28, item);
                        }
                    }
                    break;
            }
        }

    }

    @EventHandler
    public void invCloseEvent(InventoryCloseEvent e) {
        if (e.getInventory().getName().equals("§7§l》§eSpieler Einstellungen")) {
            User user = new User((Player) e.getPlayer());
            if (e.getInventory().getItem(28).getItemMeta().getDisplayName().equals("§a✔")) {
                user.getPlayerData().set("data.allow-private-messages", true);
                user.saveData();
            } else {
                user.getPlayerData().set("data.allow-private-messages", false);
                user.saveData();
            }
        }
    }

    public static Inventory teamSettings(User user) {
        com.Christian34.varo.Inventory team = new com.Christian34.varo.Inventory(5, "§7§l》§eTeam");
        team.setPlaceholder(1, Material.STAINED_GLASS_PANE, 7);
        team.setPlaceholder(5, Material.STAINED_GLASS_PANE, 7);
        team.setContent(Material.SKULL_ITEM, "§3Spieler", null, 0, 20, user.player);
        team.setContent(Material.SLIME_BALL, "§7§l》§c§lZurück", null, 0, 44, null);
        return team.getInventory();
    }

    private Inventory playerSettings(User user) {
        com.Christian34.varo.Inventory player = new com.Christian34.varo.Inventory(6, "§7§l》§eSpieler Einstellungen");
        player.setPlaceholder(1, Material.STAINED_GLASS_PANE, 7);
        player.setPlaceholder(6, Material.STAINED_GLASS_PANE, 7);
        player.setContent(Material.BOOK_AND_QUILL, "§ePrivat Nachrichten erlauben", null, 0, 19, null);

        if (user.getPlayerData().getBoolean("data.allow-private-messages")) {
            player.setContent(Material.INK_SACK, "§a✔", null, DyeColor.GREEN.getDyeData(), 28, null);
        } else {
            player.setContent(Material.INK_SACK, "§7✘", null, DyeColor.GRAY.getDyeData(), 28, null);
        }
        player.setContent(Material.SLIME_BALL, "§7§l》§c§lZurück", null, 0, 53, null);
        return player.getInventory();
    }

    private Inventory invTeamMembers(User user) {
        com.Christian34.varo.Inventory team = new com.Christian34.varo.Inventory(5, "§7§l》§eSpieler");
        team.setPlaceholder(1, Material.STAINED_GLASS_PANE, 7);
        team.setPlaceholder(5, Material.STAINED_GLASS_PANE, 7);
        Integer count = 9;
        for (Object target : user.getTeam().getTeamMembers()) {
            String usr = target.toString();
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(usr);
            team.setContent(Material.SKULL_ITEM, "§7§l》§3" + offlinePlayer.getName(), null, 0, count, (Player) offlinePlayer);
            count++;
        }
        team.setContent(Material.SLIME_BALL, "§7§l》§c§lZurück", null, 0, 44, null);
        return team.getInventory();
    }

    public static Inventory getSetupMenu(Player player) {
        com.Christian34.varo.Inventory inv = new com.Christian34.varo.Inventory(5, "§7§l》§eEinstellungen");
        inv.setPlaceholder(1, Material.STAINED_GLASS_PANE, 7);
        inv.setPlaceholder(5, Material.STAINED_GLASS_PANE, 7);
        inv.setContent(Material.SLIME_BALL, "§7§l》§c§lBeenden", null, 0, 44, null);
        inv.setContent(Material.BED, "§eTeam", null, 14, 21, null);
        inv.setContent(Material.SKULL_ITEM, "§eSpieler Einstellungen", null, 3, 23, player);
        return inv.getInventory();
    }

}