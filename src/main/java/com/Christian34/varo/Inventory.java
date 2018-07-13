/*
 * Copyright (c) 2018.
 * created by Christian34
 */

package com.Christian34.varo;

import com.Christian34.varo.Files.FileManager;
import com.Christian34.varo.Files.File_Config;
import com.Christian34.varo.Team.Team;
import com.Christian34.varo.Team.TeamSetup;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.HashMap;

public class Inventory implements Listener {

    private static final int[] SLOTS = {11, 12, 13, 14, 15, 20, 21, 22, 23, 24, 29, 30, 31, 32, 33, 38, 39, 40, 41, 42};
    private static final int[] WHITE_GLASS = {1, 7, 10, 16, 19, 25, 28, 34, 37, 43, 46, 52};
    private static final int[] GRAY_GLASS = {2, 3, 4, 5, 6};
    private static final int[] LIGHT_GRAY_GLASS = {0, 8, 9, 17, 18, 26, 27, 35, 36, 44, 45, 53};
    private static final int[] PAGE = {0, 0, 20, 40, 60, 80, 100, 120, 140, 160, 180, 200, 220, 240, 260, 280, 300};
    private static HashMap<String, Integer> latestPage = new HashMap<>();
    private org.bukkit.inventory.Inventory inv;
    private ItemStack[] contents;

    public Inventory(int lines, String title) {
        inv = Bukkit.createInventory(null, 9 * lines, title);
        contents = inv.getContents();
    }

    public void setContent(Material material, String displayName, ArrayList lore, int durability, int location, Player player) {
        ItemStack var = new ItemStack(material, 1);
        if (durability != 0) {
            var.setDurability((short) durability);
        }
        if (material != Material.SKULL_ITEM) {
            ItemMeta meta = var.getItemMeta();
            meta.setDisplayName(displayName);
            meta.setLore(lore);
            var.setItemMeta(meta);
        } else {
            SkullMeta sm = (SkullMeta) var.getItemMeta();
            sm.setDisplayName(displayName);
            sm.setLore(lore);
            sm.setOwningPlayer(player);
            var.setItemMeta(sm);
        }
        contents[location] = var;
        inv.setContents(contents);
    }

    public void setPlaceholder(int line, Material material, int data) {
        ItemStack placeholder = new ItemStack(material, 1, (byte) data);
        int counter = 0;
        if (line != 1) {
            counter = line * 9 - 9;
        }
        for (int i = counter; i <= counter + 8; i++) {
            contents[i] = placeholder;
        }
        inv.setContents(contents);
    }

    public org.bukkit.inventory.Inventory getInventory() {
        return inv;
    }

    @Deprecated
    public static void getTeamsList(Player p, Boolean getNextPage, Boolean getPrevPage) {
        Inventory invTeamList = new Inventory(6, "§7§l》§eTeams");
        invTeamList.setPlaceholder(1, Material.STAINED_GLASS_PANE, 15);
        Integer[] placeholder = {1, 7, 10, 16, 19, 25, 28, 34, 37, 43, 46, 52};
        for (int i : placeholder) {
            invTeamList.setContent(Material.STAINED_GLASS_PANE, "§0", null, 0, i, p);
        }
        User user = new User(p);

        if (TeamSetup.isSetup.get(p.getName()) != null && TeamSetup.isSetup.get(p.getName())) {
            ArrayList<String> lore = new ArrayList<>();
            lore.add("§5Du bist bereits im Setup");
            invTeamList.setContent(Material.MAGMA_CREAM, "§7§l》§c§lTeam erstellen", lore, 0, 49, p);
        } else if (user.getTeam() == null) {
            invTeamList.setContent(Material.MAGMA_CREAM, "§7§l》§a§lTeam erstellen", null, 0, 49, p);
        }

        ArrayList<String> teamsList = new ArrayList<>();
        for (String currentTeam : FileManager.getTeams().getStringList("teams.list")) {
            teamsList.add(FileManager.getTeams().getString("teams." + currentTeam + ".name"));
        }

        if (!(user.getTeam() == null)) {
            invTeamList.setContent(Material.BARRIER, "§cTeam verlassen", null, 0, 49, p);
        }

        if (getPrevPage && latestPage.get(p.getName()) == 2) {
            getPrevPage = false;
        }

        if (getNextPage) {
            int x = latestPage.get(p.getName());
            x++;
            latestPage.put(p.getName(), x);
            int max = (teamsList.size() < PAGE[x + 1]) ? teamsList.size() : PAGE[x + 1];
            for (int i = PAGE[x]; i < max; i++) {
                String currentTeam = FileManager.getTeams().getString("teams." + teamsList.get(i) + ".name");
                invTeamList.setContent(Material.PAPER, "§5§l" + currentTeam, getLore(currentTeam), 0, SLOTS[i - PAGE[x]], p);
            }
            if (!(teamsList.size() <= PAGE[x + 1] || latestPage.get(p.getName()) == 15)) {
                invTeamList.setContent(Material.ARROW, "§eNext Page", null, 0, 50, p);
            }
        } else if (getPrevPage) {
            int x = latestPage.get(p.getName());


            int max = (teamsList.size() < PAGE[x + 1]) ? teamsList.size() : PAGE[x + 1];
            for (int i = PAGE[x]; i < max; i++) {
                String currentTeam = FileManager.getTeams().getString("teams." + teamsList.get(i) + ".name");
                invTeamList.setContent(Material.PAPER, "§5§l" + currentTeam, getLore(currentTeam), 0, SLOTS[i - PAGE[x]], p);
            }
            x--;
            latestPage.put(p.getName(), x);
        } else {
            latestPage.put(p.getName(), 1);
            for (int i = 0; i < teamsList.size(); i++) {
                String currentTeam = FileManager.getTeams().getString("teams." + teamsList.get(i) + ".name");
                if (i < SLOTS.length) {
                    invTeamList.setContent(Material.PAPER, "§5§l" + currentTeam, getLore(currentTeam), 0, SLOTS[i], p);
                }
            }
        }
        if (!(teamsList.size() <= 20)) {
            invTeamList.setContent(Material.ARROW, "§eNext Page", null, 0, 50, p);
        }
        if (latestPage.get(p.getName()) > 1) {
            invTeamList.setContent(Material.ARROW, "§ePrevious Page", null, 0, 48, p);
        }
        p.openInventory(invTeamList.getInventory());
    }

    @Deprecated
    public static void getTeamsInv(Player player) {
        org.bukkit.inventory.Inventory inv = Bukkit.createInventory(null, InventoryType.HOPPER, "§7§l》§eTeams");
        ItemStack[] contents = inv.getContents();
        ItemStack create = new ItemStack(Material.MAGMA_CREAM);
        ItemMeta metaEdit = create.getItemMeta();
        if (TeamSetup.isSetup.get(player.getName()) != null && TeamSetup.isSetup.get(player.getName())) {
            metaEdit.setDisplayName("§7§l》§c§lTeam erstellen");
            ArrayList<String> lore = new ArrayList<>();
            lore.add("§5Du bist bereits im Setup");
            metaEdit.setLore(lore);
        } else {
            metaEdit.setDisplayName("§7§l》§a§lTeam erstellen");
        }
        create.setItemMeta(metaEdit);
        ItemStack exit = new ItemStack(Material.BARRIER);
        ItemMeta metaExit = exit.getItemMeta();
        metaExit.setDisplayName("§7§l》§c§lBeenden");
        exit.setItemMeta(metaExit);
        contents[0] = create;
        contents[4] = exit;
        inv.setContents(contents);

        player.openInventory(inv);
    }

    @Deprecated
    public static void invPlayerList(Player p) {
        Player headName;
        org.bukkit.inventory.Inventory players = Bukkit.createInventory(null, 54, "§7§l》§eSpieler");
        ItemStack[] contents = players.getContents();
        ArrayList<String> online = new ArrayList<>();
        ItemMeta meta = new ItemStack(Material.SKULL).getItemMeta();
        meta.setDisplayName("§0");
        for (Player player : Bukkit.getOnlinePlayers()) {
            online.add(player.getName());
        }
        ItemStack skull = new ItemStack(Material.SKULL_ITEM);
        for (int i = 0; i < online.size(); i++) {
            headName = Bukkit.getPlayer(online.get(i));
            skull.setDurability((short) 3);
            SkullMeta sm = (SkullMeta) skull.getItemMeta();
            sm.setDisplayName("§7§l》§e§l" + headName.getName() + "§7§l《");
            sm.setOwningPlayer(headName);
            skull.setItemMeta(sm);
            contents[SLOTS[i]] = skull;
            players.setContents(contents);
        }
        players.setContents(contents(meta, contents, players));

        p.openInventory(players);
    }

    @Deprecated
    private static ItemStack[] contents(ItemMeta meta, ItemStack[] contents, org.bukkit.inventory.Inventory inv) {
        for (int x : WHITE_GLASS) {
            ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 0);
            item.setItemMeta(meta);
            contents[x] = item;
            inv.setContents(contents);
        }
        for (int x : GRAY_GLASS) {
            ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 15);
            item.setItemMeta(meta);
            contents[x] = item;
            inv.setContents(contents);
        }
        for (int x : LIGHT_GRAY_GLASS) {
            ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 7);
            item.setItemMeta(meta);
            contents[x] = item;
            inv.setContents(contents);
        }
        return contents;
    }

    @Deprecated
    private static ArrayList<String> getLore(String currentTeam) {
        ArrayList<String> lore = new ArrayList<>();
        Team team = new Team(currentTeam);
        lore.add("§7§l》§aLeader: §r§7" + team.getLeader());
        lore.add("§7§l》§aMembers: §r§7" + team.getTeamMembers().size() + "§7/" + File_Config.data.getInt("config.game.players-per-team"));
        lore.add("§7§l》§aColor: §r§7" + ChatColor.translateAlternateColorCodes('&', team.getColor() + "Color"));
        lore.add(null);
        lore.add("§9§oClick to join");
        lore.add(null);
        return lore;
    }

}