package com.Christian34.varo.Commands;

import java.util.ArrayList;
import java.util.HashMap;

import com.Christian34.varo.Chat.ChatType;
import com.Christian34.varo.Lobby.Lobby;
import com.Christian34.varo.User;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CMD_Build implements CommandExecutor {

    private static ArrayList<String> builders = new ArrayList<>();
    private HashMap<String, ItemStack[]> inventory = new HashMap<>();

    public static Boolean inBuildMode(Player p) {
        return builders.contains(p.getName());
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player) sender;
        User user = new User(p);
        if (cmd.getName().equalsIgnoreCase("build")) {
            if (p.hasPermission("varo.build")) {
                if (inBuildMode(p)) {
                    builders.remove(p.getName());
                    p.setGameMode(GameMode.SURVIVAL);
                    user.sendMessage("Du kannst nun nicht mehr bauen!", ChatType.NORMAL);
                    if (p.getLocation().getWorld() == Lobby.getWorld()) {
                        Lobby.getLobbyHotbar(user);
                    } else {
                        ItemStack[] oldInv = inventory.get(p.getName());
                        p.getInventory().setContents(oldInv);
                    }
                } else {
                    builders.add(p.getName());
                    p.setGameMode(GameMode.CREATIVE);
                    user.sendMessage("Du kannst nun bauen!", ChatType.SUCCESS);
                    org.bukkit.inventory.Inventory oldInv = p.getInventory();
                    ItemStack[] contents = oldInv.getContents();
                    inventory.put(p.getName(), contents);
                    p.getInventory().clear();
                }
            }
        }
        return false;
    }

}