/*
 * Copyright (c) 2018.
 * created by Christian34
 */

package com.Christian34.varo.PlayerStates;

import com.Christian34.varo.User;
import org.bukkit.entity.Player;

public class PlayerStateManager {

    private PlayerState[] playerStates = new PlayerState[3];

    public PlayerStateManager() {
        playerStates[0] = null;
        playerStates[PlayerState.PLAY] = new PlayState();
        playerStates[PlayerState.SPECTATOR] = new SpectatorState();
    }

    public void setPlayerState(Player p, int index) {
        User user = new User(p);

        if (getCurrentPlayerState(p) != null) {
            getCurrentPlayerState(p).stop();
        }

        user.getPlayerData().set("data.state", index);
        user.saveData();
        getCurrentPlayerState(p).start();
    }

    public PlayerState getCurrentPlayerState(Player p) {
        User user = new User(p);
        if (user.getPlayerData().getInt("data.state") == 2) {
            return playerStates[PlayerState.SPECTATOR];
        } else if (user.getPlayerData().getInt("data.state") == 1) {
            return playerStates[PlayerState.PLAY];
        } else {
            return null;
        }
    }

}