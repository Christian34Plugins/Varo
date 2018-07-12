/*
 * Copyright (c) 2018.
 * created by Christian34
 */

package com.Christian34.varo;

import com.Christian34.varo.Files.FileManager;
import com.Christian34.varo.GameStates.GameStateManager;
import com.Christian34.varo.Chat.Chat;
import com.Christian34.varo.Inventorys.*;
import com.Christian34.varo.PlayerStates.PlayerStateManager;
import com.Christian34.varo.Lobby.*;
import com.Christian34.varo.Team.TeamListener;
import com.Christian34.varo.Commands.*;
import com.Christian34.varo.Events.*;
import com.Christian34.varo.Modules.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Varo extends JavaPlugin implements Listener {

    private static Plugin plugin;
    private static GameStateManager gameStateManager;
    private static PlayerStateManager playerStateManager;
    private static Chat chat = null;

    @Override
    public void onEnable() {
        plugin = this;
        FileManager fileManager = new FileManager();
        fileManager.init();
        chat = new Chat(this);
        Lobby.init();
        gameStateManager = new GameStateManager();
        gameStateManager.setGameState(FileManager.getData().getInt("data.game.state"));
        playerStateManager = new PlayerStateManager();
        if (!registerEvents()) {
            getServer().getPluginManager().disablePlugin(this);
            throw new RuntimeException(Chat.getPrefix() + "Plugin could not be loaded!");
        }
        getCommand("varo").setExecutor(new CMD_Varo());
        getCommand("build").setExecutor(new CMD_Build());
        getCommand("start").setExecutor(new CMD_Start());
        getCommand("lobby").setExecutor(new CMD_Lobby());
        getCommand("ping").setExecutor(new CMD_Ping());
        getCommand("lastseen").setExecutor(new CMD_LastSeen());
        getCommand("team").setExecutor(new CMD_Team());
        if (!Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            getServer().getPluginManager().disablePlugin(this);
            throw new RuntimeException(Chat.getPrefix() + "§cPlaceholderAPI was not found! Please install it to use the Plugin!");
        }
        update();
        Bukkit.getServer().getConsoleSender().sendMessage(Chat.getPrefix() + "§aPlugin has been enabled.");
    }

    private boolean registerEvents() {
        try {
            getServer().getPluginManager().registerEvents(this, this);
            getServer().getPluginManager().registerEvents(new JoinEvent(), this);
            getServer().getPluginManager().registerEvents(new PlayerBlockBreak(), this);
            getServer().getPluginManager().registerEvents(new PlayerBlockPlace(), this);
            getServer().getPluginManager().registerEvents(new PlayerChatEvent(), this);
            getServer().getPluginManager().registerEvents(new PlayerInteract(), this);
            getServer().getPluginManager().registerEvents(new PlayerItemDropEvent(), this);
            getServer().getPluginManager().registerEvents(new PlayerLeaveEvent(), this);
            getServer().getPluginManager().registerEvents(new FoodChangeEvent(), this);
            getServer().getPluginManager().registerEvents(new EntityDamage(), this);
            getServer().getPluginManager().registerEvents(new EntityDamageByEntity(), this);
            getServer().getPluginManager().registerEvents(new Scoreboard(), this);
            getServer().getPluginManager().registerEvents(new InvClickEvent(), this);
            getServer().getPluginManager().registerEvents(new Backpack(), this);
            getServer().getPluginManager().registerEvents(new TeamListener(), this);
            getServer().getPluginManager().registerEvents(new ToxicRain(), this);
            getServer().getPluginManager().registerEvents(new Setup(), this);
            getServer().getPluginManager().registerEvents(new GravityGrenade(), this);
            getServer().getPluginManager().registerEvents(new InventoryPlayerList(), this);
            getServer().getPluginManager().registerEvents(new InventorySettings(), this);
            getServer().getPluginManager().registerEvents(new InventoryTeams(), this);
            getServer().getPluginManager().registerEvents(new ChestLock(), this);
            getServer().getPluginManager().registerEvents(new DeathEvent(), this);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void update() {
        new BukkitRunnable() {
            public void run() {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.setScoreboard(Scoreboard.getScoreboard(p));
                }
            }
        }.runTaskTimer(this, 0, 20 * 5);
    }

    public static GameStateManager getGameStateManager() {
        return gameStateManager;
    }

    public static PlayerStateManager getPlayerStateManager() {
        return playerStateManager;
    }

    public static Plugin getPlugin() {
        return plugin;
    }

    public static Chat chat() {
        return chat;
    }

}