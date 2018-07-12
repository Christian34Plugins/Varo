package com.Christian34.varo.Events;

import com.Christian34.varo.GameStates.StartState;
import com.Christian34.varo.Lobby.Lobby;
import com.Christian34.varo.Varo;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import com.Christian34.varo.Commands.CMD_Build;

public class PlayerBlockBreak implements Listener {

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void blockBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        if (Varo.getGameStateManager().getCurrentGameState() instanceof StartState) {
            e.setCancelled(true);
        }
        if (p.getLocation().getWorld().equals(Lobby.getWorld())) {
            if (!CMD_Build.inBuildMode(p)) {
                e.setCancelled(true);
            }
        }
    }

}