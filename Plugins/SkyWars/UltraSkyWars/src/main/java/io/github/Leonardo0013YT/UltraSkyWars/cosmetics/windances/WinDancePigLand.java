package io.github.Leonardo0013YT.UltraSkyWars.cosmetics.windances;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.interfaces.WinDance;
import io.github.Leonardo0013YT.UltraSkyWars.superclass.Game;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class WinDancePigLand implements WinDance, Cloneable {

    private static boolean loaded = false;
    private static int maxOfCenter, firstUp, maxRandomUp, taskTick;
    private final Random random;
    private BukkitTask task;

    public WinDancePigLand() {
        this.task = null;
        this.random = ThreadLocalRandom.current();
    }

    @Override
    public void loadCustoms(UltraSkyWars plugin, String path) {
        if (!loaded) {
            maxOfCenter = plugin.getWindance().getIntOrDefault(path + ".maxOfCenter", 25);
            firstUp = plugin.getWindance().getIntOrDefault(path + ".firstUp", 110);
            maxRandomUp = plugin.getWindance().getIntOrDefault(path + ".maxRandomUp", 5);
            taskTick = plugin.getWindance().getIntOrDefault(path + ".taskTick", 5);
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
                for (int i = 0; i < 12; i++) {
                    Pig p1 = world.spawn(new Location(world, random.nextInt(maxOfCenter), firstUp + random.nextInt(maxRandomUp), random.nextInt(maxOfCenter)), Pig.class);
                    p1.setMetadata("NO_TARGET", new FixedMetadataValue(UltraSkyWars.get(), ""));
                    p1.setNoDamageTicks(Integer.MAX_VALUE);
                }
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
        return new WinDancePigLand();
    }

}