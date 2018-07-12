/*
 * Copyright (c) 2018.
 * created by Christian34
 */

package com.Christian34.varo.PlayerStates;

public abstract class PlayerState {

    public static final int
            NONE = 0,
            PLAY = 1,
            SPECTATOR = 2;

    public abstract void start();

    public abstract  void stop();

}