package io.github.Leonardo0013YT.UltraSkyWars.cosmetics.windances;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.interfaces.WinDance;
import io.github.Leonardo0013YT.UltraSkyWars.superclass.Game;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;

import java.util.concurrent.ThreadLocalRandom;

public class WinDanceWolfs implements WinDance, Cloneable {

    private static boolean loaded = false;

    @Override
    public void loadCustoms(UltraSkyWars plugin, String path) {
        if (!loaded) {
            loaded = true;
        }
    }

    @Override
    public void start(Player p, Game game) {
        World world = game.getSpectator().getWorld();
        for (int i = 0; i < 20; i++) {
            int x = ThreadLocalRandom.current().nextInt(0, 4);
            int z = ThreadLocalRandom.current().nextInt(0, 4);
            Location center = p.getLocation().clone().add(x, 1, z);
            Wolf wolf = world.spawn(center, Wolf.class);
            wolf.setOwner(p);
            wolf.setSitting(ThreadLocalRandom.current().nextBoolean());
        }
    }

    @Override
    public void stop() {

    }

    @Override
    public WinDance clone() {
        return new WinDanceWolfs();
    }

}