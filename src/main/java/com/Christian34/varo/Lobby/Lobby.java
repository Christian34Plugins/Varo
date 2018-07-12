/*
 * Copyright (c) 2018.
 * created by Christian34
 */

package com.Christian34.varo.Lobby;

import com.Christian34.varo.Chat.Chat;
import com.Christian34.varo.Files.File_Config;
import com.Christian34.varo.Files.File_Data;
import com.Christian34.varo.User;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Lobby {

    public static void init() {
        WorldCreator c = new WorldCreator("lobby");
        c.createWorld();
        //TODO: check config.enabled
        File_Config.data.set("config.enabled", true);
        File_Config.saveConfig();
    }

    public static void tpLobby(Player p, Boolean sendMessage) {
        double x = File_Data.data.getDouble("data.lobby.spawn.x");
        double y = File_Data.data.getDouble("data.lobby.spawn.y");
        double z = File_Data.data.getDouble("data.lobby.spawn.z");
        double yaw = File_Data.data.getDouble("data.lobby.spawn.yaw");
        double pitch = File_Data.data.getDouble("data.lobby.spawn.pitch");
        Location loc = new Location(Bukkit.getWorld("lobby"), x, y, z, (float) yaw, (float) pitch);
        p.teleport(loc);
        if (sendMessage) {
            p.sendMessage(Chat.getPrefix() + "§aDu wurdest in die Lobby teleportiert!");
        }
    }

    public static World getWorld() {
        return Bukkit.getWorld("lobby");
    }

    public static void setSpawn(Location loc) {
        File_Data.data.set("data.lobby.spawn.x", loc.getX());
        File_Data.data.set("data.lobby.spawn.y", loc.getY());
        File_Data.data.set("data.lobby.spawn.z", loc.getZ());
        File_Data.data.set("data.lobby.spawn.yaw", loc.getYaw());
        File_Data.data.set("data.lobby.spawn.pitch", loc.getPitch());
        Bukkit.getWorld("lobby").setSpawnLocation((int) loc.getX(), (int) loc.getY(), (int) loc.getZ());
        File_Data.saveData();
        File_Data.loadData();
    }

    public static void getLobbyHotbar(User user) {
        user.resetPlayer();

        ItemMeta meta;

        ItemStack team = new ItemStack(Material.BED, 1);
        team.setDurability((short) 14);
        meta = team.getItemMeta();
        meta.setDisplayName("§7§l》§e§lTeams§7§l《");
        team.setItemMeta(meta);
        user.player.getInventory().setItem(0, team);

        ItemStack item = new ItemStack(Material.NETHER_STAR, 1);
        meta = item.getItemMeta();
        meta.setDisplayName("§7§l》§e§lEinstellungen§7§l《");
        item.setItemMeta(meta);
        user.player.getInventory().setItem(4, item);

        ItemStack rules = new ItemStack(Material.BOOK, 1);
        meta = rules.getItemMeta();
        meta.setDisplayName("§7§l》§e§lInformationen§7§l《");
        rules.setItemMeta(meta);
        user.player.getInventory().setItem(8, rules);
    }

}