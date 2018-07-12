/*
 * Copyright (c) 2018.
 * created by Christian34
 */

package com.Christian34.varo.GameStates;

import com.Christian34.varo.Chat.Chat;
import com.Christian34.varo.Files.File_Data;
import com.Christian34.varo.User;
import com.Christian34.varo.Varo;
import com.Christian34.varo.PlayerStates.SpectatorState;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class StartState extends GameState {

    private int count, countdown;

    @Override
    public void start() {
        int count = 1;
        for (Player p : Bukkit.getOnlinePlayers()) {
            User user = new User(p);
            if (Varo.getPlayerStateManager().getCurrentPlayerState(p) instanceof SpectatorState) {
                user.sendMessage("Du bist im Spectator Modus");
            } else {
                double x = File_Data.data.getInt("playerData.game.positions.pos" + count + ".x");
                double y = File_Data.data.getInt("playerData.game.positions.pos" + count + ".y");
                double z = File_Data.data.getInt("playerData.game.positions.pos" + count + ".z");
                float yaw = File_Data.data.getInt("playerData.game.positions.pos" + count + ".yaw");
                float pitch = File_Data.data.getInt("playerData.game.positions.pos" + count + ".pitch");
                Location loc = new Location(Bukkit.getWorld("world"), x, y, z, yaw, pitch);
                p.teleport(loc);
                user.resetPlayer();
                count++;
            }
        }
        Bukkit.getWorld("world").setThundering(false);
        Bukkit.getWorld("world").setTime(0L);
        startCountdown();
    }

    @Override
    public void stop() {
        Chat.broadcast("§aMögen die Spiele beginnen! Viel Spaß!");
    }

    private void startCountdown() {
        countdown = 60;
        count = Bukkit.getScheduler().scheduleSyncRepeatingTask(Varo.getPlugin(), () -> {
            if (countdown == 60 || countdown == 30 || countdown == 10 || countdown <= 10) {
                if (countdown == 1) {
                    Chat.broadcast("§7Spiel startet in §eeiner §7Sekunde");
                    Bukkit.getScheduler().cancelTask(count);
                    Varo.getGameStateManager().setGameState(GameState.INGAME_STATE);
                    countdown = 60;
                } else if (countdown != 1) {
                    Chat.broadcast("§7Spiel startet in §e" + countdown + " §7Sekunden");
                }
                countdown--;
            } else {
                countdown--;
            }

        }, 0L, 20L);
    }

}