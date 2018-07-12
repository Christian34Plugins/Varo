/*
 * Copyright (c) 2018.
 * created by Christian34
 */

package com.Christian34.varo.GameStates;

import com.Christian34.varo.Files.File_Data;

public class GameStateManager {

    private GameState[] gameStates = new GameState[5];
    private GameState currentGameState;

    public GameStateManager() {
        gameStates[GameState.LOBBY_STATE] = new LobbyState();
        gameStates[GameState.START_STATE] = new StartState();
        gameStates[GameState.INGAME_STATE] = new IngameState();
        gameStates[GameState.ENDING_STATE] = new EndingState();
        gameStates[GameState.NOGAME_STATE] = new NoGameState();
    }

    public void setGameState(int index) {
        if (this.currentGameState != null) {
            this.currentGameState.stop();
        }
        this.currentGameState = gameStates[index];
        File_Data.data.set("data.game.state", index);
        File_Data.saveData();
        File_Data.loadData();
    }

    public void startCurrentGameState() {
        currentGameState.start();
    }

    public void stopCurrentGameState() {
        currentGameState.stop();
        currentGameState = null;
    }

    public GameState getCurrentGameState() {
        return currentGameState;
    }

}