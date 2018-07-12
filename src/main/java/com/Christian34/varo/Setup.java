/*
 * Copyright (c) 2018.
 * created by Christian34
 */

package com.Christian34.varo;

import com.Christian34.varo.Chat.Chat;
import com.Christian34.varo.Chat.ChatType;
import com.Christian34.varo.Files.File_Config;
import com.Christian34.varo.Files.File_Data;
import com.Christian34.varo.GameStates.GameState;
import com.Christian34.varo.Lobby.Lobby;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class Setup implements Listener {

    private ConcurrentHashMap<String, Boolean> save = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, Boolean> error = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, Integer> loc = new ConcurrentHashMap<>();

    public void cmd(CommandSender sender, String[] args) {
        Player p = (Player) sender;
        User user = new User(p);
        if (p.hasPermission("varo.setup")) {
            if (args.length == 1) {
                Inventory setup = Bukkit.createInventory(null, 9, "Setup");
                ItemStack[] contents = setup.getContents();

                for (int i = 0; i < 9; i++) {
                    contents[i] = createItem(Material.STAINED_GLASS_PANE, 1, 7, " ", null);
                }

                ArrayList<String> info = new ArrayList<>();

                info.add("§7§l》§bSpieler: §7" + File_Config.data.getInt("config.game.players"));
                info.add("§7§l》§bTeams: §7" + File_Config.data.getInt("config.game.players") / File_Config.data.getInt("config.game.players-per-team"));
                contents[0] = createItem(Material.SIGN, 1, 0, "§6Informationen", info);

                ItemStack teams = new ItemStack(Material.SKULL_ITEM);
                teams.setDurability((short) 3);
                SkullMeta sm = (SkullMeta) teams.getItemMeta();
                sm.setDisplayName("§3Spieler");
                sm.setOwningPlayer(p);
                teams.setItemMeta(sm);
                contents[3] = teams;

                contents[4] = createItem(Material.ENDER_PEARL, 1, 0, "§bLobby", null);
                contents[5] = createItem(Material.PAPER, 1, 0, "§aAllgemeine Einstellungen", null);
                contents[8] = createItem(Material.SLIME_BALL, 1, 0, "§7§l》§c§lBeenden", null);

                setup.setContents(contents);
                p.openInventory(setup);
            } else if (args.length == 2 || args.length == 3) {
                if (args[1].equalsIgnoreCase("loc")) {
                    if (args.length == 2) {
                        if (error.get(p.getName()) != null && error.get(p.getName())) {
                            Chat.clear(p);
                            p.sendMessage(Chat.getPrefix() + "§cEin Fehler ist aufgetreten!\n§7Vergewissere dich, dass du " +
                                    "in der richtigen Welt bist! Hast du das gemacht, klick auf \"fortfahren\"");
                            p.sendMessage("\n" + Chat.separator + "\n");
                            TextComponent msg = new TextComponent("§7Hier Klicken:");
                            TextComponent y = new TextComponent(" §a[FORTFAHREN]");
                            y.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/varo setup loc"));
                            y.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§5Klick mich").create()));
                            msg.addExtra(y);
                            p.spigot().sendMessage(msg);
                            p.sendMessage(Chat.separator);
                            error.put(p.getName(), false);
                            save.put(p.getName(), false);
                            return;
                        }
                        if (save.get(p.getName()) == null || !(save.get(p.getName()))) {
                            loc.putIfAbsent(p.getName(), 1);
                            if (!(loc.get(p.getName()) > File_Config.getPlayers())) {
                                if (File_Data.data.get("playerData.game.positions.pos" + loc.get(p.getName()) + ".x") == null) {
                                    Chat.clear(p);
                                    p.sendMessage(Chat.getPrefix() + "§eSpawnpunkte der Spieler festlegen\n" +
                                            "§7Stelle dich dafür auf einen Spawnpunkt und klicke auf \"Speichern.\"");
                                    p.sendMessage("\n" + Chat.separator + "\n" + "§7Möchtest du §5Position " + loc.get(p.getName()) + " §7speichern?");
                                    TextComponent msg = new TextComponent("§7Hier Klicken:");
                                    TextComponent y = new TextComponent(" §a[SPEICHERN]");
                                    y.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/varo setup loc"));
                                    y.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§5Klick mich").create()));
                                    msg.addExtra(y);
                                    p.spigot().sendMessage(msg);
                                    p.sendMessage(Chat.separator);
                                    save.put(p.getName(), true);
                                } else {
                                    Chat.clear(p);
                                    p.sendMessage(Chat.getPrefix() + "§cEin Fehler ist aufgetreten!\n" +
                                            "§7Es wurden bereits alle Spawnpunkte gesetzt. Möchtest du sie zurücksetzen? Klicke auf \"Zurücksetzen\"");
                                    p.sendMessage("\n" + Chat.separator + "\n");
                                    TextComponent msg = new TextComponent("§7Hier Klicken:");
                                    TextComponent y = new TextComponent(" §a[ZURÜCKSETZEN]");
                                    y.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/varo setup loc reset"));
                                    y.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§5Klick mich").create()));
                                    msg.addExtra(y);
                                    p.spigot().sendMessage(msg);
                                    p.sendMessage(Chat.separator);
                                }
                            } else {
                                Chat.clear(p);
                                user.tpToLobby();
                                Lobby.getLobbyHotbar(user);
                                user.sendMessage("§aEs wurden alle Spawnpunkte gesetzt!\n" +
                                        "§7Es wurden alle Spawnpunkte gesetzt und Varo kann nun gespielt werden.");
                                Varo.getGameStateManager().setGameState(GameState.LOBBY_STATE);
                                save.put(p.getName(), false);
                                error.put(p.getName(), false);
                                loc.put(p.getName(), 1);
                            }
                        } else if (save.get(p.getName())) {
                            Integer pos = loc.get(p.getName());
                            Location loc = p.getLocation();
                            if (loc.getWorld() != Lobby.getWorld()) {
                                File_Data.data.set("playerData.game.positions.pos" + pos + ".x", loc.getX());
                                File_Data.data.set("playerData.game.positions.pos" + pos + ".y", loc.getY());
                                File_Data.data.set("playerData.game.positions.pos" + pos + ".z", loc.getZ());
                                File_Data.data.set("playerData.game.positions.pos" + pos + ".yaw", loc.getYaw());
                                File_Data.data.set("playerData.game.positions.pos" + pos + ".pitch", loc.getPitch());
                                pos++;
                                this.loc.put(p.getName(), pos);
                                save.put(p.getName(), false);
                                File_Data.saveData();
                            } else {
                                error.put(p.getName(), true);
                            }
                            Bukkit.dispatchCommand(p, "varo setup loc");
                        }
                    } else {
                        if (args[2].equalsIgnoreCase("reset")) {
                            File_Data.data.set("playerData.game.positions", null);
                            File_Data.saveData();
                            File_Data.loadData();
                            Chat.clear(p);
                            user.sendMessage("§cSpawnpunkte wurden gelöscht!\n" +
                                    "§7Es wurden alle Spawnpunkte zurückgesetzt! Du kannst sie in den Einstellungen wieder platzieren.");
                        }
                    }
                } else {
                    Bukkit.dispatchCommand(p, "varo help");
                }
            } else {
                Bukkit.dispatchCommand(p, "varo help");
            }
        } else {
            user.sendMessage("Du hast dafür keine Berechtigung!", ChatType.ERROR);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void invClickEvent(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        Inventory dummy = Bukkit.createInventory(null, 9);
        ItemStack[] contents = dummy.getContents();
        for (int i = 0; i < 9; i++) {
            contents[i] = createItem(Material.STAINED_GLASS_PANE, 1, 7, " ", null);
        }
        contents[8] = createItem(Material.SLIME_BALL, 1, 0, "§7§l》§c§lZurück", null);
        if (e.getClickedInventory() != null && e.getClickedInventory().getName().equalsIgnoreCase("setup")) {
            if (e.getCurrentItem() != null && e.getCurrentItem().getItemMeta() != null) {
                if (e.getCurrentItem().getItemMeta().getDisplayName() != null) {
                    if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§3Spieler")) {
                        Inventory teams = Bukkit.createInventory(null, 9, "§3Spieler");
                        contents[2] = createItem(Material.WOOL, 10, 14, "§c-10", null);
                        contents[3] = createItem(Material.WOOL, 1, 14, "§c-1", null);
                        int amount = File_Config.getPlayers() == 0 ? 4 : File_Config.getPlayers();
                        contents[4] = createItem(Material.BOOK, amount, 0, "§3Spieler", null);
                        contents[5] = createItem(Material.WOOL, 1, 5, "§a+1", null);
                        contents[6] = createItem(Material.WOOL, 10, 5, "§a+10", null);
                        teams.setContents(contents);
                        p.openInventory(teams);
                    } else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§bLobby")) {
                        Inventory lobby = Bukkit.createInventory(null, 9, "§bLobby");
                        contents[4] = createItem(Material.ENDER_PEARL, 1, 0, "§bLobby festlegen", null);
                        lobby.setContents(contents);
                        p.openInventory(lobby);
                    } else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§aAllgemeine Einstellungen")) {
                        Inventory settings = Bukkit.createInventory(null, 9, "§aAllgemeine Einstellungen");
                        settings.setItem(6, new ItemStack(Material.ENDER_PEARL));
                        contents[6] = createItem(Material.ENDER_PEARL, 1, 0, "§6Spawnpunkte festlegen", null);
                        contents[1] = createItem(Material.WOOL, 1, 14, "§c-1", null);
                        int amount = File_Config.getPlayersPerTeam() == 0 ? 4 : File_Config.getPlayersPerTeam();
                        contents[2] = createItem(Material.BOOK, amount, 0, "§3Spieler pro Team", null);
                        contents[3] = createItem(Material.WOOL, 1, 5, "§a+1", null);
                        settings.setContents(contents);
                        p.openInventory(settings);
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void updateInv(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        User user = new User(p);
        ItemStack[] contents;
        try {
            contents = e.getClickedInventory().getContents();
        } catch (Exception e1) {
            return;
        }
        if (e.getClickedInventory().getName() != null && e.getClickedInventory().getName().equalsIgnoreCase("§3Spieler")) {
            if (e.getCurrentItem().getItemMeta() != null) {
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§a+1")) {
                    if (contents[4].getAmount() == 64) {
                        return;
                    } else {
                        contents[4].setAmount(contents[4].getAmount() + 1);
                    }
                } else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§a+10")) {
                    if (contents[4].getAmount() + 10 > 64) {
                        contents[4].setAmount(64);
                    } else {
                        contents[4].setAmount(contents[4].getAmount() + 10);
                    }
                } else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§c-1")) {
                    if (contents[4].getAmount() - 1 < 4) {
                        return;
                    } else {
                        contents[4].setAmount(contents[4].getAmount() - 1);
                    }
                } else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§c-10")) {
                    if (contents[4].getAmount() - 10 < 4) {
                        contents[4].setAmount(4);
                    } else {
                        contents[4].setAmount(contents[4].getAmount() - 10);
                    }
                }
            }
            if (File_Config.getPlayers() != contents[4].getAmount()) {
                File_Config.data.set("config.game.players-per-team", contents[4].getAmount() % 2 == 0 ? 2 : 1);
                File_Config.data.set("config.game.players", contents[4].getAmount());
                File_Config.saveConfig();
            }
        } else if (e.getClickedInventory().getName() != null && e.getClickedInventory().getName().equalsIgnoreCase("§bLobby")) {
            if (e.getCurrentItem().getType() == Material.ENDER_PEARL) {
                if (e.getWhoClicked().getLocation().getWorld() != Bukkit.getWorld("world")) {
                    Bukkit.dispatchCommand(p, "lobby setspawn");
                } else {
                    user.sendMessage("Du kannst nicht in dieser Welt die Lobby platzieren!", ChatType.ERROR);
                }
            }
        } else if (e.getClickedInventory().getName() != null && e.getClickedInventory().getName().equalsIgnoreCase("§aAllgemeine Einstellungen")) {
            if (e.getCurrentItem().getType() == Material.WOOL) {
                if (e.getCurrentItem().getItemMeta().getDisplayName().contains("+1")) {
                    if (contents[2].getAmount() + 1 <= (File_Config.getPlayers() / 2)) {
                        int players = File_Config.getPlayers();
                        for (int i = 1; i <= 10; i++) {
                            if (players % (contents[2].getAmount() + i) == 0) {
                                if (!(contents[2].getAmount() + i >= players)) {
                                    contents[2].setAmount(contents[2].getAmount() + i);
                                }
                                break;
                            }
                        }
                    }
                } else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("-1")) {
                    if (contents[2].getAmount() != 1) {
                        if (contents[3].getAmount() - 1 <= (File_Config.getPlayers() / 2)) {
                            int players = File_Config.getPlayers();
                            for (int i = 1; i <= 10; i++) {
                                if (players % (contents[2].getAmount() - i) == 0) {
                                    if (!(contents[2].getAmount() - i >= players)) {
                                        contents[2].setAmount(contents[2].getAmount() - i);
                                    }
                                    break;
                                }
                            }
                        }
                    } else {
                        return;
                    }
                }
                File_Config.data.set("config.game.players-per-team", contents[2].getAmount());
                File_Config.saveConfig();
            } else if (e.getCurrentItem().getType() == Material.ENDER_PEARL) {
                if (!p.getWorld().getName().equals("world")) {
                    p.teleport(Bukkit.getWorld("world").getSpawnLocation());
                }
                Bukkit.dispatchCommand(p, "varo setup loc");
                p.closeInventory();
            }
        }
    }

    private ItemStack createItem(Material material, Integer amount, int damage, String displayName, ArrayList<String> lore) {
        ItemStack itemStack = new ItemStack(material, amount, (byte) damage);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(displayName);
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

}