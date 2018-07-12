package com.Christian34.varo.Events;

import com.Christian34.varo.GameStates.StartState;
import com.Christian34.varo.Lobby.Lobby;
import com.Christian34.varo.Varo;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityDamage implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void entityDamage(EntityDamageEvent e) {
        if (Varo.getGameStateManager().getCurrentGameState() instanceof StartState) e.setCancelled(true);
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if (p.getLocation().getWorld().equals(Lobby.getWorld())) e.setCancelled(true);
        }
    }

}