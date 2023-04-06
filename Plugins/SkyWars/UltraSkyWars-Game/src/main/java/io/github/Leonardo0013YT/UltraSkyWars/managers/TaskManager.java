package io.github.Leonardo0013YT.UltraSkyWars.managers;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.cosmetics.Balloon;
import io.github.Leonardo0013YT.UltraSkyWars.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class TaskManager {

    private UltraSkyWars plugin;
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
            plugin.getCos().getBalloons().values().stream().filter(Balloon::isAnimated).filter(Balloon::needUpdate).forEach(Balloon::update);
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getGm().update());
        }, 20L, 20L);
        if (plugin.getCm().isBungeeModeEnabled()) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    plugin.getGm().getBungee().debug();
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