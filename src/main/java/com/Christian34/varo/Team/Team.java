/*
 * Copyright (c) 2018.
 * created by Christian34
 */

package com.Christian34.varo.Team;

import com.Christian34.varo.Chat.Chat;
import com.Christian34.varo.Files.FileManager;
import com.Christian34.varo.Files.File_Teams;
import com.Christian34.varo.User;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;

public class Team {

    private Player player;
    private String name;
    private String leader;
    private String color;
    private TeamSetup listener = new TeamSetup();

    public TeamSetup teamListener() {
        return listener;
    }

    /**
     * get the name by player
     */
    public Team(User user) {
        this.player = user.player;
        this.name = getTeamName();
        if (name.equalsIgnoreCase("none")) {
            this.leader = "none";
            this.color = "none";
        } else {
            this.name = getName();
            this.leader = File_Teams.data.getString("teams." + name + ".leader");
            this.color = File_Teams.data.getString("teams." + name + ".color");
        }
    }

    /**
     * get the name by player
     */
    public Team(Player user) {
        this.player = user;
        this.name = getTeamName();
        if (name.equalsIgnoreCase("none")) {
            leader = "none";
            color = "none";
        } else {
            this.name = getTeamName();
            this.leader = File_Teams.data.getString("teams." + name + ".leader");
            this.color = File_Teams.data.getString("teams." + name + ".color");
        }
    }

    /**
     * get the name by String
     */
    public Team(String team) {
        this.name = team;
        this.leader = File_Teams.data.getString("teams." + team + ".leader");
        this.color = File_Teams.data.getString("teams." + team + ".color");
    }

    /**
     * create a new name
     *
     * @param name   name of the name
     * @param prefix prefix
     * @param color  color
     **/
    public void createTeam(String name, String prefix, String color) {
        User user = new User(player);
        if (FileManager.getTeams().getString("teams." + name + ".name") != null && FileManager.getTeams().getStringList("teams.list").contains(name)) {
            user.sendMessage("§cTeam konnte nicht erstellt werden.");
            return;
        }
        List<String> list = FileManager.getTeams().getStringList("teams.list");
        list.add(name);
        FileManager.getTeams().set("teams.list", list);
        FileManager.getTeams().set("teams." + name + ".name", name);
        FileManager.getTeams().set("teams." + name + ".prefix", prefix);
        FileManager.getTeams().set("teams." + name + ".color", color);
        FileManager.getTeams().set("teams." + name + ".leader", player.getName());
        String[] players = {player.getName()};
        FileManager.getTeams().set("teams." + name + ".members", players);
        FileManager.saveTeams();
        user.sendMessage("§aTeam wurde erfolgreich erstellt!");
    }

    public String getName() {
        return name;
    }

    public String getLeader() {
        return leader;
    }

    public String getColor() {
        return color;
    }

    public FileConfiguration getData() {
        return File_Teams.data;
    }

    public void saveData() {
        File_Teams.save();
    }

    private String getTeamName() {
        List<String> list = File_Teams.data.getStringList("teams.list");
        for (String team : list) {
            List<String> players = File_Teams.data.getStringList("teams." + team + ".members");
            for (String member : players) {
                Player target = Bukkit.getPlayer(member);
                if (target == player) {
                    return File_Teams.data.getString("teams." + team + ".name");
                }
            }
        }
        return "none";
    }

    public Boolean leaveTeam() {
        if (!name.equals("none")) {
            List<String> members = File_Teams.data.getStringList("teams." + name + ".members");
            members.remove(player.getName());
            if (getTeamMembers().size() == 1) {
                deleteTeam();
            } else if (getLeader().equals(player.getName())) {
                File_Teams.data.set("teams." + name + ".leader", members.get(0));
                Bukkit.getPlayer(members.get(0)).sendMessage(Chat.getPrefix() + "§aDu bist nun der Leiter von Team " + name + "!");
            }
            File_Teams.data.set("teams." + name + ".members", members);
            saveData();
            return true;
        } else {
            return false;
        }
    }

    public List getTeamMembers() {
        return File_Teams.data.getStringList("teams." + name + ".members");
    }

    private void deleteTeam() {
        List teamslist = File_Teams.data.getStringList("teams.list");
        teamslist.remove(name);
        File_Teams.data.set("teams.list", teamslist);
        File_Teams.data.set("teams." + name, null);
        saveData();
    }

    public Boolean isSetup() {
        return TeamSetup.isSetup.get(player.getName());
    }

    public static boolean isTeam(String team) {
        return File_Teams.data.getStringList("teams.list").contains(team);
    }

}