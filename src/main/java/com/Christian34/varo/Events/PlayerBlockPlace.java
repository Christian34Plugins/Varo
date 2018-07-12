package com.Christian34.varo.Events;

import com.Christian34.varo.GameStates.StartState;
import com.Christian34.varo.Lobby.Lobby;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import com.Christian34.varo.Varo;
import com.Christian34.varo.Commands.CMD_Build;

public class PlayerBlockPlace implements Listener {

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void blockPlace(BlockPlaceEvent e) {
        Player p = e.getPlayer();
        if (Varo.getGameStateManager().getCurrentGameState() instanceof StartState) {
            e.setCancelled(true);
        }
        if (p.getLocation().getWorld().equals(Lobby.getWorld())) {
            if (!CMD_Build.inBuildMode(p)) {
                e.setCancelled(true);
            }
        }
        if (p.getInventory().getItemInMainHand().getItemMeta().hasDisplayName()) {
            e.setBuild(false);
            e.setCancelled(true);
        }
    }

}