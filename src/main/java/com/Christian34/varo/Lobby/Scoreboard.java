package com.Christian34.varo.Lobby;

import java.util.List;

import com.Christian34.varo.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.ScoreboardManager;
import com.Christian34.varo.Files.File_Config;

import me.clip.placeholderapi.PlaceholderAPI;

public class Scoreboard implements Listener {

    private static int latest = 1;
    private static String a = "§r";

    public static org.bukkit.scoreboard.Scoreboard getScoreboard(Player p) {
        User user = new User(p);
        latest = 1;
        a = "§r";
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        org.bukkit.scoreboard.Scoreboard scoreboard = manager.getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("aaa", "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(File_Config.data.getString("config.lobby.scoreboard.header"));
        List<String> content = File_Config.data.getStringList("config.lobby.scoreboard.content");

        int counter = content.size();

        for (int i = 0; i < counter; i++) {
            int a = (counter - i) - 1;
            if (content.get(a).equalsIgnoreCase("%empty%")) {
                objective.getScore(empty()).setScore(i);
            } else if (content.get(a).contains("%team%")) {
                String text = user.getTeam() == null ? "§bnone" : content.get(a).replace("%team%", user.getTeam().getName());
                objective.getScore(text).setScore(i);
            } else {
                String line = PlaceholderAPI.setPlaceholders(p, content.get(a));
                objective.getScore(line).setScore(i);
            }
        }
        return scoreboard;
    }

    private static String empty() {
        for (int i = 1; i <= latest; i++) {
            a = (i == 1) ? "§r" : (a + "§r");
        }
        latest++;
        return a;
    }

}