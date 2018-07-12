package com.Christian34.varo.Events;

import com.Christian34.varo.Files.FileManager;
import com.Christian34.varo.Chat.Chat;
import com.Christian34.varo.User;
import com.Christian34.varo.Varo;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerChatEvent implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onChatMessage(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        User user = new User(p);
        String msg = e.getMessage();
        if (p.isOp()) {
            msg = ChatColor.translateAlternateColorCodes('&', msg);
        }
        String chat;
        String team = user.getTeam().getName();
        if (team.equals("none")) {
            chat = Varo.chat().getMessage("chat", p);
        } else {
            chat = Varo.chat().getMessage("chat-team", p);
            chat = chat.replace("%team%", FileManager.getTeams().getString("teams." + team + ".color") + FileManager.getTeams().getString("teams." + team + ".prefix"));
        }
        chat = chat.replace("%msg%", msg);
        e.setFormat(PlaceholderAPI.setPlaceholders(p, chat));
    }

}