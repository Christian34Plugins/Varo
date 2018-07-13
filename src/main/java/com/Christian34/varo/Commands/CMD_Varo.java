package com.Christian34.varo.Commands;

import com.Christian34.varo.Inventory;
import com.Christian34.varo.User;
import com.Christian34.varo.Varo;
import com.Christian34.varo.Setup;
import com.Christian34.varo.Files.File_Config;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CMD_Varo implements CommandExecutor {

    private Setup setup = new Setup();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player) sender;
        User user = new User(p);
        if (cmd.getName().equalsIgnoreCase("varo")) {
            if (args.length >= 1) {
                if (args[0].equalsIgnoreCase("setup")) {
                    setup.cmd(sender, args);
                } else if (args[0].equalsIgnoreCase("reload")) {
                    File_Config.saveConfig();
                } else if (args[0].equalsIgnoreCase("help")) {
                    user.sendMessage("all commands");
                } else if (args[0].equalsIgnoreCase("gamestate")) {
                    user.sendMessage(Varo.getGameStateManager().getCurrentGameState().toString());
                } else if (args[0].equalsIgnoreCase("test")) {
                    Inventory.getTeamsList(p, false, false);
                } else {
                    Bukkit.dispatchCommand(p, "varo help");
                }
            } else {
                user.sendMessage("Du benutzt §6Varo §7Version §6" + Varo.getPlugin().getDescription().getVersion() + " §7von §6Christian34§7." + "\n§7Benutze §6/varo help §7um alle Befehle zu sehen.");
            }
        }
        return false;
    }

}