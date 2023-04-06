package io.github.Leonardo0013YT.UltraSkyWars.managers;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.cosmetics.Balloon;
import io.github.Leonardo0013YT.UltraSkyWars.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class TaskManager {

    private final UltraSkyWars plugin;
    private int amount = 0;

    public TaskManager(UltraSkyWars plugin) {
        this.plugin = plugin;
        loadTasks();
    }

    public void loadTasks() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            if (amount >= 5) {
                Utils.updateSB();
                amount = 0;
            }
            amount++;
            plugin.getCos().getAnimatedBalloons().values().stream().filter(Balloon::isAnimated).filter(Balloon::needUpdate).forEach(Balloon::update);
        }, 20L, 20L);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            plugin.getDb().loadTopKills();
            plugin.getDb().loadTopWins();
            plugin.getDb().loadTopDeaths();
            plugin.getDb().loadTopCoins();
            plugin.getDb().loadTopElo();
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                plugin.getGm().updateFinish();
                plugin.getTop().createTops();
            });
        }), 6000L, 6000L);
        new BukkitRunnable() {
            @Override
            public void run() {
                plugin.getTop().createTops();
            }
        }.runTaskLater(plugin, 20 * 10);
    }


}