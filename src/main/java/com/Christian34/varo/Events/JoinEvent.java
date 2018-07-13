/*
 * Copyright (c) 2018.
 * created by Christian34
 */

package com.Christian34.varo.Events;

import com.Christian34.varo.*;
import com.Christian34.varo.Chat.Chat;
import com.Christian34.varo.Files.File_Config;
import com.Christian34.varo.Files.File_Data;
import com.Christian34.varo.GameStates.IngameState;
import com.Christian34.varo.GameStates.LobbyState;
import com.Christian34.varo.GameStates.NoGameState;
import com.Christian34.varo.Lobby.Lobby;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import com.Christian34.varo.Lobby.Scoreboard;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class JoinEvent implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        User user = new User(p);
        e.setJoinMessage(Varo.chat().getMessage("join-message", p));
        user.setLastSeen();
        if (Varo.getGameStateManager().getCurrentGameState() instanceof LobbyState) {
            user.resetPlayer();
            Lobby.getLobbyHotbar(user);
            p.setScoreboard(Scoreboard.getScoreboard(p));
            user.tpToLobby();
        } else if (Varo.getGameStateManager().getCurrentGameState() instanceof NoGameState) {
            if (!p.hasPermission("varo.setup")) {
                p.kickPlayer("§cVaro ist noch nicht verfügbar!");
            } else {
                if (File_Data.data.get("data.game.positions.pos" + File_Config.getPlayers() + ".x") == null) {
                    user.resetPlayer();
                    TextComponent msg = new TextComponent(Chat.getPrefix() + "§7Es wurde noch kein Spiel erstellt...\n§7Möchtest du jetzt eines erstellen?");
                    TextComponent msg2 = new TextComponent(" §r§e§lKlick mich");
                    msg2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/varo setup"));
                    msg2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§5Klick mich").create()));
                    msg.addExtra(msg2);
                    p.spigot().sendMessage(msg);

                    ItemStack setup = new ItemStack(Material.NETHER_STAR, 1);
                    ItemMeta meta = setup.getItemMeta();
                    meta.setDisplayName("§7§l》§e§lSetup§7§l《");
                    setup.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
                    setup.setItemMeta(meta);
                    p.getInventory().setItem(4, setup);
                }
            }
        } else if (Varo.getGameStateManager().getCurrentGameState() instanceof IngameState) {
            user.startTimer();
        }
    }

}