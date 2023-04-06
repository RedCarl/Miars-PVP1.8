package io.github.Leonardo0013YT.UltraSkyWars.cosmetics.windances;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.interfaces.WinDance;
import io.github.Leonardo0013YT.UltraSkyWars.superclass.Game;
import io.github.Leonardo0013YT.UltraSkyWars.xseries.XSound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class WinDanceDayNight implements WinDance, Cloneable {

    private static boolean loaded = false;
    private static int perTickTime, taskTick;
    private BukkitTask task;

    @Override
    public void loadCustoms(UltraSkyWars plugin, String path) {
        if (!loaded) {
            perTickTime = plugin.getWindance().getIntOrDefault(path + ".perTickTime", 700);
            taskTick = plugin.getWindance().getIntOrDefault(path + ".taskTick", 1);
            loaded = true;
        }
    }

    @Override
    public void start(Player p, Game game) {
        World world = game.getSpectator().getWorld();
        task = new BukkitRunnable() {
            public void run() {
                if (p == null || !p.isOnline() || !world.getName().equals(p.getWorld().getName())) {
                    stop();
                    return;
                }
                p.getWorld().setTime(p.getWorld().getTime() + perTickTime);
                p.playSound(p.getLocation(), XSound.BLOCK_NOTE_BLOCK_HAT.parseSound(), 0.01f, 0.01f);
            }
        }.runTaskTimer(UltraSkyWars.get(), 0, taskTick);
    }

    @Override
    public void stop() {
        if (task != null) {
            task.cancel();
        }
    }

    @Override
    public WinDance clone() {
        return new WinDanceDayNight();
    }
}