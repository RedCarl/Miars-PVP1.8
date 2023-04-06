package io.github.Leonardo0013YT.UltraSkyWars.cosmetics.windances;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.interfaces.WinDance;
import io.github.Leonardo0013YT.UltraSkyWars.superclass.Game;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.concurrent.ThreadLocalRandom;

public class WinDanceMeteors implements WinDance, Cloneable {

    private static boolean loaded = false;
    private static int firstUp, taskTick;
    private static double maxOfCenter;
    private BukkitTask task;

    public WinDanceMeteors() {
        this.task = null;
    }

    @Override
    public void loadCustoms(UltraSkyWars plugin, String path) {
        if (!loaded) {
            maxOfCenter = plugin.getWindance().getDoubleOrDefault(path + ".maxOfCenter", 1);
            firstUp = plugin.getWindance().getIntOrDefault(path + ".firstUp", 110);
            taskTick = plugin.getWindance().getIntOrDefault(path + ".taskTick", 2);
            loaded = true;
        }
    }

    @Override
    public void start(Player p, Game game) {
        World world = game.getSpectator().getWorld();
        Location center = new Location(world, game.getBorderX(), firstUp, game.getBorderZ());
        task = new BukkitRunnable() {
            public void run() {
                if (p == null || !p.isOnline() || !world.getName().equals(p.getWorld().getName())) {
                    stop();
                    return;
                }
                Fireball fb = world.spawn(center, Fireball.class);
                fb.setVelocity(new Vector(ThreadLocalRandom.current().nextDouble(-maxOfCenter, maxOfCenter), -1.5, ThreadLocalRandom.current().nextDouble(-maxOfCenter, maxOfCenter)));
            }
        }.runTaskTimer(UltraSkyWars.get(), taskTick, taskTick);
    }

    @Override
    public void stop() {
        if (task != null) {
            task.cancel();
        }
    }

    @Override
    public WinDance clone() {
        return new WinDanceMeteors();
    }

}