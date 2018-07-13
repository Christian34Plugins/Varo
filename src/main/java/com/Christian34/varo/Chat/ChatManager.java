/*
 * Copyright (c) 2018.
 * created by Christian34
 */

package com.Christian34.varo.Chat;

import org.bukkit.entity.Player;

public abstract class ChatManager {

    public static final int LANG_EN = 0,
            LANG_DE = 1;

    public abstract String getMessage(String key, Player player);

}