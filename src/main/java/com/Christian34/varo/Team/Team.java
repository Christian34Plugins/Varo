/*
 * Copyright (c) 2018.
 * created by Christian34
 */

package com.Christian34.varo.Team;

import com.Christian34.varo.Chat.Chat;
import com.Christian34.varo.Files.FileManager;
import com.Christian34.varo.Files.File_Config;
import com.Christian34.varo.Files.File_Teams;
import com.Christian34.varo.User;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.nio.file.attribute.FileTime;
import java.util.List;

public class Team {

    private Player player;
    private String team;
    private String leader;
    private String color;
    private TeamListener listener = new TeamListener();

    public TeamListener teamListener() {
        return listener;
    }

    /**
     * get the team by player
     */
    public Team(User user) {
        this.player = user.player;
        this.team = getTeam();
        if (team.equalsIgnoreCase("none")) {
            this.leader = "none";
            this.color = "none";
        } else {
            this.team = getTeam();
            this.leader = File_Teams.data.getString("teams." + team + ".leader");
            this.color = File_Teams.data.getString("teams." + team + ".color");
        }
    }

    /**
     * get the team by player
     */
    public Team(Player user) {
        this.player = user;
        this.team = getTeam();
        if (team.equalsIgnoreCase("none")) {
            leader = "none";
            color = "none";
        } else {
            this.team = getTeam();
            this.leader = File_Teams.data.getString("teams." + team + ".leader");
            this.color = File_Teams.data.getString("teams." + team + ".color");
        }
    }

    /**
     * get the team by String
     */
    public Team(String team) {
        this.team = team;
        this.leader = File_Teams.data.getString("teams." + team + ".leader");
        this.color = File_Teams.data.getString("teams." + team + ".color");
    }

    /**
     * create a new team
     *
     * @param name   name of the team
     * @param prefix prefix
     * @param color  color
     **/
    public Team(Player player, String name, String prefix, String color) {
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
        return team;
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

    public String getTeam() {
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

    public void joinTeam(String team) {
        if (isTeam(team)) {
            if (!getTeam().equalsIgnoreCase(team)) {
                if (File_Teams.data.getStringList("teams." + team + " .members").size() < File_Config.data.getInt("config.game.players-per-team")) {
                    leaveTeam(false);
                    List<String> list = File_Teams.data.getStringList("teams." + team + ".members");
                    list.add(player.getName());
                    File_Teams.data.set("teams." + team + ".members", list);
                    File_Teams.save();
                    this.team = team;
                    this.leader = File_Teams.data.getString("teams." + team + ".leader");
                    this.color = File_Teams.data.getString("teams." + team + ".color");
                    player.sendMessage(Chat.getPrefix() + "§aDu bist dem Team erfolgreich beigetreten!");
                } else {
                    player.sendMessage(Chat.getPrefix() + "§cTeam ist voll!");
                }
            } else {
                player.sendMessage(Chat.getPrefix() + "§cDu bist bereits in diesem Team!");
            }
        } else {
            player.sendMessage(Chat.getPrefix() + "§cTeam wurde nicht gefunden!");
        }
    }

    private boolean isTeam(String team) {
        return File_Teams.data.getStringList("teams.list").contains(team);
    }

    public void leaveTeam(Boolean sendNotification) {
        if (!team.equals("none")) {
            List<String> members = File_Teams.data.getStringList("teams." + team + ".members");
            members.remove(player.getName());
            if (getTeamMembers().size() == 1) {
                deleteTeam();
            } else if (getLeader().equals(player.getName())) {
                File_Teams.data.set("teams." + team + ".leader", members.get(0));
                Bukkit.getPlayer(members.get(0)).sendMessage(Chat.getPrefix() + "§aDu bist nun der Leiter von Team " + team + "!");
            }
            File_Teams.data.set("teams." + team + ".members", members);
            if (sendNotification) {
                player.sendMessage(Chat.getPrefix() + "§aDu hast das Team " + team + " verlassen");
            }
            saveData();
        } else if (getTeam().equals("none")) {
            if (sendNotification) {
                player.sendMessage(Chat.getPrefix() + "§cDu bist in keinem Team!");
            }
        } else {
            player.sendMessage(Chat.getPrefix() + "§cEin Fehler ist aufgetreten...");
        }
    }

    public List getTeamMembers() {
        return File_Teams.data.getStringList("teams." + team + ".members");
    }

    private void deleteTeam() {
        List teamslist = File_Teams.data.getStringList("teams.list");
        teamslist.remove(team);
        File_Teams.data.set("teams.list", teamslist);
        File_Teams.data.set("teams." + team, null);
        saveData();
    }

    public Boolean isSetup() {
        return TeamListener.isSetup.get(player.getName());
    }

}