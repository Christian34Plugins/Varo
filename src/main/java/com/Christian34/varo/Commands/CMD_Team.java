/*
 * Copyright (c) 2018.
 * created by Christian34
 */

package com.Christian34.varo.Commands;

import com.Christian34.varo.Chat.Chat;
import com.Christian34.varo.Chat.ChatType;
import com.Christian34.varo.Team.Team;
import com.Christian34.varo.Team.TeamSetup;
import com.Christian34.varo.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CMD_Team implements CommandExecutor {

    private int[] slots = {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25};
    private int[] whiteGlass = {1, 2, 3, 4, 5, 6, 7, 28, 29, 30, 31, 32, 33, 34};
    private int[] grayGlass = {0, 8, 9, 17, 18, 26, 27, 35};
    private ChatColor[] color = {ChatColor.BLACK, ChatColor.DARK_BLUE, ChatColor.DARK_GREEN, ChatColor.WHITE, ChatColor.AQUA, ChatColor.DARK_RED, ChatColor.DARK_PURPLE, ChatColor.GOLD, ChatColor.GRAY, ChatColor.DARK_GRAY, ChatColor.BLUE, ChatColor.GREEN, ChatColor.LIGHT_PURPLE, ChatColor.YELLOW};

    private int getWoolColor(ChatColor c) {
        switch (c) {
            case BLACK:
                return 15;
            case DARK_BLUE:
                return 11;
            case DARK_GREEN:
                return 13;
            case DARK_AQUA:
                return 9;
            case DARK_RED:
                return 14;
            case DARK_PURPLE:
                return 10;
            case GOLD:
                return 1;
            case GRAY:
                return 8;
            case DARK_GRAY:
                return 7;
            case BLUE:
                return 3;
            case GREEN:
                return 5;
            case AQUA:
                return 9;
            case RED:
                return 14;
            case LIGHT_PURPLE:
                return 6;
            case YELLOW:
                return 4;
            case WHITE:
                return 0;
            case BOLD:
                return -1;
            case ITALIC:
                return -1;
            case MAGIC:
                return -1;
            case RESET:
                return -1;
            case STRIKETHROUGH:
                return -1;
            case UNDERLINE:
                return -1;
        }
        return 8;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("team")) {
            Player p = (Player) sender;
            User user = new User((Player) sender);
            Team team = user.getTeam();
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("create")) {
                    if (team.getName().equals("none")) {
                        TeamSetup.isSetup.put(p.getName(), true);
                        if (args.length == 2) {
                            if (args[1].equalsIgnoreCase("save")) {
                                if (TeamSetup.setup.get(p.getName()) != null) {
                                    TeamSetup.setup.put(p.getName(), TeamSetup.setup.get(p.getName()) + 1);
                                    TeamSetup.state.put(p.getName(), 0);
                                    Bukkit.dispatchCommand(p, "team create");
                                }
                            } else if (args[1].equalsIgnoreCase("edit")) {
                                Bukkit.dispatchCommand(p, "team create");
                            } else {
                                Bukkit.dispatchCommand(p, "varo help");
                            }
                        } else if (TeamSetup.setup.get(p.getName()) == null || TeamSetup.setup.get(p.getName()) == 1) {
                            Chat.clear(p);
                            sender.sendMessage(Chat.getPrefix() + "§eWie soll dein Team heißen?\n§7Schreibe die Antwort in den Chat oder beende mit \"quit\"");
                            TeamSetup.isSetup.put(p.getName(), true);
                            TeamSetup.state.put(p.getName(), 1);
                            TeamSetup.setup.put(p.getName(), 1);
                        } else if (TeamSetup.setup.get(p.getName()) == 2) {
                            Chat.clear(p);
                            sender.sendMessage(Chat.getPrefix() + "§eSetze nun das Prefix\n§7Schreibe die Antwort in den Chat oder beende mit \"quit\"");
                            TeamSetup.state.put(p.getName(), 1);
                            TeamSetup.setup.put(p.getName(), 2);
                        } else if (TeamSetup.setup.get(p.getName()) == 3) {
                            Chat.clear(p);
                            Inventory colors = Bukkit.createInventory(null, 36, "§7§l》§5Wähle eine Farbe aus");
                            ItemStack[] contents = colors.getContents();
                            ItemStack create = new ItemStack(Material.MAGMA_CREAM);
                            ItemMeta meta = create.getItemMeta();
                            meta.setDisplayName("§0");

                            int color = 0;
                            for (int x : slots) {
                                ItemStack wool = new ItemStack(Material.WOOL, 1);
                                wool.setDurability((short) getWoolColor(this.color[color]));
                                ItemMeta colorMeta = wool.getItemMeta();
                                colorMeta.setDisplayName("§7§l》" + this.color[color] + "Color");
                                wool.setItemMeta(colorMeta);
                                contents[x] = wool;
                                color++;
                            }

                            for (int x : whiteGlass) {
                                ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 0);
                                item.setItemMeta(meta);
                                contents[x] = item;
                            }

                            for (int x : grayGlass) {
                                ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 15);
                                item.setItemMeta(meta);
                                contents[x] = item;
                            }

                            colors.setContents(contents);
                            p.openInventory(colors);
                            TeamSetup.state.put(p.getName(), 1);
                            TeamSetup.setup.put(p.getName(), 3);
                        } else if (TeamSetup.setup.get(p.getName()) == 4) {
                            TeamSetup.isSetup.put(p.getName(), false);
                            Team newTeam = new Team(p);
                            newTeam.createTeam(team.teamListener().getData(p, 0), team.teamListener().getData(p, 1), team.teamListener().getData(p, 2));
                        }
                    } else {
                        p.sendMessage(Chat.getPrefix() + "§cDu bist bereits in einem Team!");
                    }
                } else if (args[0].equalsIgnoreCase("leave")) {
                    if (team.leaveTeam()) {
                        user.sendMessage("Du hast das Team verlassen!", ChatType.SUCCESS);
                    } else {
                        user.sendMessage("§cDu bist in keinem Team!", ChatType.ERROR);
                    }
                } else {
                    Bukkit.dispatchCommand(p, "varo help");
                }
            } else {
                Bukkit.dispatchCommand(p, "varo help");
            }
        }
        return false;
    }

}