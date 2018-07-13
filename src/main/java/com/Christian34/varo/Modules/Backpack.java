/*
 * Copyright (c) 2018.
 * created by Christian34
 */

package com.Christian34.varo.Modules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.Christian34.varo.Varo;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import com.Christian34.varo.Files.File_PlayerData;

public class Backpack implements Listener {

    @SuppressWarnings("deprecation")
    public Backpack() {
        ItemStack bpack = new ItemStack(Material.CHEST);
        ItemMeta bpackMeta = bpack.getItemMeta();
        bpackMeta.setLore(Arrays.asList("ID: RFKrBgPbqy6THSWlaZMVZV", null));
        bpackMeta.setDisplayName("§7§l》§3§lBackpack§7§l《");
        bpack.setItemMeta(bpackMeta);
        ShapedRecipe bp = new ShapedRecipe(bpack);

        bp.shape("***", "*%*", "*§*");
        bp.setIngredient('*', Material.LEATHER);
        bp.setIngredient('%', Material.CHEST);
        bp.setIngredient('§', Material.DIAMOND);
        Varo.getPlugin().getServer().addRecipe(bp);
    }

    @EventHandler(priority = EventPriority.HIGHEST,ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        File_PlayerData data = new File_PlayerData(p);
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (e.getItem() != null && e.getItem().getType() == Material.CHEST) {
                if (e.getItem().getItemMeta().hasLore() && e.getItem().getItemMeta().getLore().contains("ID: RFKrBgPbqy6THSWlaZMVZV")) {
                    Inventory backpack = Bukkit.createInventory(null, 9, "§3Backpack");
                    ItemStack[] contents = backpack.getContents();
                    try {
                        List<?> list = data.data.getList("data.backpack");
                        for (int i = 0; i < list.size(); i++) {
                            contents[i] = (ItemStack) list.get(i);
                        }
                    } catch (Exception ex) {
                        p.openInventory(backpack);
                    }
                    backpack.setContents(contents);
                    p.openInventory(backpack);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBackpackClose(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();
        if (e.getInventory().getName().equals("§3Backpack")) {
            ArrayList<ItemStack> list = new ArrayList<ItemStack>();
            Inventory backpack = e.getInventory();
            ItemStack[] contents = backpack.getContents();
            for (ItemStack item : contents) {
                if (item != null) {
                    list.add(item);
                }
            }
            File_PlayerData playerData = new File_PlayerData(p);
            playerData.data.set("data.backpack", list);
            playerData.save();
        }
    }

}