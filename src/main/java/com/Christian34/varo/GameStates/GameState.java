/*
 * Copyright (c) 2018.
 * created by Christian34
 */

package com.Christian34.varo.GameStates;

public abstract class GameState {

    public static final int NOGAME_STATE = 0,
            LOBBY_STATE = 1,
            START_STATE = 2,
            INGAME_STATE = 3,
            ENDING_STATE = 4;

    public abstract void start();

    public abstract void stop();

}