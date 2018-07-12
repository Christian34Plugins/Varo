/*
 * Copyright (c) 2018.
 * created by Christian34
 */

package com.Christian34.varo;

import com.Christian34.varo.Chat.Chat;
import com.Christian34.varo.Chat.ChatManager;
import com.Christian34.varo.Chat.ChatType;
import com.Christian34.varo.Files.File_Config;
import com.Christian34.varo.Files.File_PlayerData;
import com.Christian34.varo.Lobby.Lobby;
import com.Christian34.varo.PlayerStates.PlayerStateManager;
import com.Christian34.varo.Team.Team;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class User {

    public final Player player;
    private final UUID uuid;
    private File_PlayerData playerFile;
    private FileConfiguration playerData;
    private Team team;

    public User(Player player) {
        if (player != null) {
            this.player = player;
            this.uuid = player.getUniqueId();
            this.playerFile = new File_PlayerData(player);
            this.playerData = playerFile.data;
            this.team = new Team(player);
        } else {
            this.player = null;
            this.uuid = null;
            this.playerFile = null;
            this.playerData = null;
        }
    }

    public void performCommand(String command) {
        Bukkit.dispatchCommand(player, command);
    }

    public FileConfiguration getPlayerData() {
        return playerData;
    }

    public void saveData() {
        playerFile.save();
    }

    public String lastSeen() {
        return playerData.getString("data.lastseen");
    }

    public void setLastSeen() {
        Date currentDate = new Date();
        String date = new SimpleDateFormat("dd.MM.YYYY - HH:mm").format(currentDate);
        playerData.set("data.lastseen", date);
        playerFile.save();
    }

    public void resetPlayer() {
        player.getInventory().clear();
        player.setHealth(20);
        player.setGameMode(GameMode.SURVIVAL);
        player.resetPlayerTime();
        player.resetPlayerWeather();
        player.resetTitle();
        player.setSneaking(false);
        player.getInventory().setArmorContents(null);
        player.getInventory().setItemInOffHand(null);
        player.setFoodLevel(20);
        player.setExp(0);
    }

    public int getState() {
        return playerData.getInt("data.state");
    }

    public void setState(Integer index) {
        PlayerStateManager manager = new PlayerStateManager();
        manager.setPlayerState(player, index);
    }

    public void tpToLobby() {
        Lobby.tpLobby(player, false);
    }

    public void tpToLobby(Boolean sendMessage) {
        Lobby.tpLobby(player, sendMessage);
    }

    public void sendMessage(String message) {
        player.sendMessage(Varo.chat().getMessage("prefix", player) + message);
    }

    public void sendMessage(String message, ChatType chatType) {
        ChatColor color;
        switch (chatType) {
            case INFO:
                color = ChatColor.YELLOW;
                break;
            case SUCCESS:
                color = ChatColor.GREEN;
                break;
            case ERROR:
                color = ChatColor.RED;
                break;
            default:
                color = ChatColor.GRAY;
        }
        player.sendMessage(Chat.getPrefix() + color + message);
    }

    public Integer getLang() {
        if (playerData.getString("data.lang").equalsIgnoreCase("de_DE")) {
            return ChatManager.LANG_DE;
        } else {
            return ChatManager.LANG_EN;
        }
    }

    public void startTimer() {
        new BukkitRunnable() {
            @Override
            public void run() {
                player.kickPlayer(Chat.getPrefix() + "\n§cDeine Spielzeit ist abgelaufen!");
            }
        }.runTaskLater(Varo.getPlugin(), 20 * 60 * File_Config.data.getInt("config.game.max-play-time"));
    }

    public Location getLocation() {
        return player.getLocation();
    }

    public Team getTeam() {
        return (team.getName().equals("none") ? null : team);
    }

}