package com.Christian34.varo.Modules;

import com.Christian34.varo.Chat.Chat;
import com.Christian34.varo.Varo;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class ToxicRain implements Listener {

    private boolean isToxic = false;
    private BukkitTask i = null;
    private Integer scheduler = null;

    private void scheduler() {
        try {
            if (!i.isCancelled()) {
                return;
            }
        } catch (Exception ignored) {
        }
        i = new BukkitRunnable() {
            public void run() {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (!isSolidBlockAbove(p) && p.getLocation().getWorld() == Bukkit.getWorld("world") && Bukkit.getWorld("world").hasStorm()) {
                        Double health = p.getHealth();
                        if (health > 15D) {
                            p.damage(2D);
                        } else if (health == 4) {
                            p.damage(0.4D);
                        } else if (health == 3) {
                            p.damage(0.3D);
                        } else if (health == 2) {
                            p.damage(0.2D);
                        } else if (health == 1) {
                            p.damage(0.1D);
                        } else {
                            p.damage(0.5D);
                        }
                    }
                }
            }
        }.runTaskTimer(Varo.getPlugin(), 0, 20);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void WeatherEvent(WeatherChangeEvent e) {
        try {
            i.cancel();
            Bukkit.getScheduler().cancelTask(scheduler);
        } catch (Exception ignored) {
        }
        scheduler = Bukkit.getScheduler().scheduleSyncDelayedTask(Varo.getPlugin(), () -> {
            if (Bukkit.getWorld("world").hasStorm()) {
                isToxic = rnd();
                if (isToxic) {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        if (p.getLocation().getWorld() == Bukkit.getWorld("world")) {
                            p.sendMessage(Chat.getPrefix() + "§5Gift Regen");
                        }
                    }
                    scheduler();
                }
            }
        }, 60L);
    }

    private boolean rnd() {
        int rnd = (int) (Math.random() * 5);
        return rnd == 1;
    }

    private static boolean isSolidBlockAbove(Player p) {
        for (int i = 1; i <= 3; i++) {
            Material b = p.getLocation().add(0.0D, 2D + i, 0.0D).getBlock().getType();
            if (b != Material.AIR) {
                return true;
            }
        }
        return false;
    }

}