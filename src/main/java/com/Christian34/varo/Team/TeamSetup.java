/*
 * Copyright (c) 2018.
 * created by Christian34
 */

package com.Christian34.varo.Team;

import com.Christian34.varo.Chat.Chat;
import com.Christian34.varo.Files.File_Teams;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.concurrent.ConcurrentHashMap;

public class TeamSetup implements Listener {

    public static ConcurrentHashMap<String, Boolean> isSetup = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String, Integer> setup = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String, Integer> state = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, ConcurrentHashMap<Integer, String>> data = new ConcurrentHashMap<>();
    public ChatColor[] wool = {ChatColor.BLACK, ChatColor.DARK_BLUE, ChatColor.DARK_GREEN, ChatColor.WHITE, ChatColor.AQUA, ChatColor.DARK_RED, ChatColor.DARK_PURPLE, ChatColor.GOLD, ChatColor.GRAY, ChatColor.DARK_GRAY, ChatColor.BLUE, ChatColor.GREEN, ChatColor.LIGHT_PURPLE, ChatColor.YELLOW};

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onChatMessage(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        if (isSetup.get(p.getName()) != null && isSetup.get(p.getName())) {
            if (e.getMessage().contains("exit") || e.getMessage().contains("quit")) {
                isSetup.put(p.getName(), false);
                setup.put(p.getName(), 1);
                state.put(p.getName(), 0);
                e.setCancelled(true);
                p.sendMessage(Chat.getPrefix() + "Setup wurde abgebrochen!");
                return;
            }
            if (state.get(p.getName()) == 1) {
                p.sendMessage("\n" + Chat.separator + "\n" + "§7Möchtest du §5" + e.getMessage() + " §7speichern?");
                saveData(p, setup.get(p.getName()) - 1, e.getMessage());
                TextComponent msg = new TextComponent("§7Hier Klicken:");
                TextComponent y = new TextComponent(" §a[JA]");
                y.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/team create save"));
                y.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§5Klick mich").create()));
                TextComponent n = new TextComponent(" §c[ÄNDERN]");
                n.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/team create edit"));
                n.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§5Klick mich").create()));
                msg.addExtra(y);
                msg.addExtra(n);
                p.spigot().sendMessage(msg);
                p.sendMessage(Chat.separator);
            }
            e.setCancelled(true);
        }
    }

    /**
     * @param player User
     * @param key    Key for HashMap
     * @param toSave Data to save
     */
    public void saveData(Player player, Integer key, String toSave) {
        ConcurrentHashMap<Integer, String> data2 = data.get(player.getName());
        if (data2 == null) data2 = new ConcurrentHashMap<>();
        data2.put(key, toSave);
        data.put(player.getName(), data2);
    }

    /**
     * @param player User
     * @param key    Key for HashMap
     * @return returns Data from HashMap
     */
    public String getData(Player player, Integer key) {
        try {
            ConcurrentHashMap<Integer, String> data2 = data.get(player.getName());
            return data2.get(key);
        } catch (Exception e) {
            return "none";
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void invClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getClickedInventory() != null && e.getClickedInventory().getName() != null && e.getCurrentItem() != null) {
            if (e.getInventory().getName().equalsIgnoreCase("§7§l》§5Wähle eine Farbe aus")) {
                if (e.getCurrentItem().getType() == Material.WOOL) {
                    int durability = e.getCurrentItem().getDurability();
                            /*
                              FIX: null
                             */
                    saveData(p, 2, wool[durability].toString());
                    TeamSetup.setup.put(p.getName(), 4);
                    p.closeInventory();
                    Bukkit.dispatchCommand(p, "team create");
                }
            }
        }
    }



}