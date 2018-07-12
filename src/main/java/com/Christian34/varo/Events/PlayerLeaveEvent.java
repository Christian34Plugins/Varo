package com.Christian34.varo.Events;

import com.Christian34.varo.User;
import com.Christian34.varo.Varo;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLeaveEvent implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerleave(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        User user = new User(p);
        user.setLastSeen();
        e.setQuitMessage(Varo.chat().getMessage("quit-message", p));
    }

}