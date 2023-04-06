package io.github.Leonardo0013YT.UltraSkyWars.managers;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class TaskManager {

    private UltraSkyWars plugin;

    public TaskManager(UltraSkyWars plugin) {
        this.plugin = plugin;
        loadTasks();
    }

    public void loadTasks() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, Utils::updateSB, 20L, 20L);
        new BukkitRunnable() {
            @Override
            public void run() {
                plugin.getDb().loadTopKills();
                plugin.getDb().loadTopWins();
                plugin.getDb().loadTopDeaths();
                plugin.getDb().loadTopCoins();
                plugin.getDb().loadTopElo();
                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getTop().createTops());
            }
        }.runTaskTimerAsynchronously(plugin, (20 * 60) * 5, (20 * 60) * 5);
        new BukkitRunnable() {
            @Override
            public void run() {
                plugin.getTop().createTops();
            }
        }.runTaskLater(plugin, 20 * 30);
        if (plugin.getCm().isBungeeModeEnabled()) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (plugin.getCm().isBungeeModeEnabled()) {
                        if (plugin.getBm().getRedis() != null) {
                            plugin.getBm().getRedis().sendPing();
                        }
                    }
                }
            }.runTaskTimer(plugin, 20 * 30, 20 * 30);
        }
    }


}