package io.github.Leonardo0013YT.UltraSkyWars.cosmetics.wineffects;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.enums.CustomSound;
import io.github.Leonardo0013YT.UltraSkyWars.interfaces.WinEffect;
import io.github.Leonardo0013YT.UltraSkyWars.superclass.Game;
import org.bukkit.Location;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class WinEffectChicken implements WinEffect {

    private final ArrayList<Chicken> chickens = new ArrayList<>();
    private BukkitTask task;

    @Override
    public void start(Player p, Game game) {
        UltraSkyWars plugin = UltraSkyWars.get();
        task = new BukkitRunnable() {
            final String name = game.getSpectator().getWorld().getName();

            @Override
            public void run() {
                if (p == null || !p.isOnline() || !name.equals(p.getWorld().getName())) {
                    stop();
                    return;
                }
                Chicken chicken = spawnChicken(p.getLocation(), random(-0.5, 0.5), random(-0.5, 0.5));
                chicken.getLocation().getWorld().playSound(chicken.getLocation(), CustomSound.WINEFFECTS_CHICKEN.getSound(), CustomSound.WINEFFECTS_CHICKEN.getVolume(), CustomSound.WINEFFECTS_CHICKEN.getPitch());
                chickens.add(chicken);
                for (Chicken c : new ArrayList<>(chickens)) {
                    if (c.getTicksLived() > 30) {
                        c.remove();
                        plugin.getVc().getNMS().broadcastParticle(c.getLocation(), 0, 0, 0, 0, "REDSTONE", 1000, 10);
                        CustomSound.WINEFFECTS_CHICKEN.reproduce(p);
                        chickens.remove(c);
                    }
                }
            }
        }.runTaskTimer(plugin, 5, 5);
    }

    @Override
    public void stop() {
        chickens.clear();
        if (task != null) {
            task.cancel();
        }
    }

    @Override
    public WinEffect clone() {
        return new WinEffectChicken();
    }

    protected double random(double d, double d2) {
        return d + ThreadLocalRandom.current().nextDouble() * (d2 - d);
    }

    private Chicken spawnChicken(Location location, double d, double d3) {
        Chicken chicken = location.getWorld().spawn(location, Chicken.class);
        chicken.setVelocity(new Vector(d, 1.5, d3));
        return chicken;
    }

}