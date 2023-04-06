package io.github.Leonardo0013YT.UltraSkyWars.cosmetics.wineffects;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.enums.CustomSound;
import io.github.Leonardo0013YT.UltraSkyWars.interfaces.WinEffect;
import io.github.Leonardo0013YT.UltraSkyWars.superclass.Game;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;

public class WinEffectVulcanWool implements WinEffect, Cloneable {

    private Collection<FallingBlock> fires = new ArrayList<>();
    private BukkitTask task;

    @Override
    public void start(Player p, Game game) {
        UltraSkyWars plugin = UltraSkyWars.get();
        String name = game.getSpectator().getWorld().getName();
        task = new BukkitRunnable() {
            @Override
            public void run() {
                if (p == null || !p.isOnline() || !name.equals(p.getWorld().getName())) {
                    stop();
                    return;
                }
                CustomSound.WINEFFECTS_VULCANWOOL.reproduce(p);
                FallingBlock fallingBlock = spawnWool(p.getLocation(), random(-0.5, 0.5), random(-0.5, 0.5));
                fallingBlock.setDropItem(false);
                fires.add(fallingBlock);
            }
        }.runTaskTimer(plugin, 0, 2);
    }

    @Override
    public void stop() {
        if (task != null) {
            task.cancel();
        }
        for (FallingBlock fb : fires) {
            if (fb == null) continue;
            if (!fb.isDead()) {
                fb.remove();
            } else if (fb.isOnGround()) {
                fb.getLocation().getBlock().setType(Material.AIR);
            }
        }
    }

    @Override
    public WinEffect clone() {
        return new WinEffectVulcanWool();
    }

    protected double random(double d, double d2) {
        return d + ThreadLocalRandom.current().nextDouble() * (d2 - d);
    }

    private FallingBlock spawnWool(Location location, double d, double d3) {
        @SuppressWarnings("deprecation")
        FallingBlock fallingBlock = location.getWorld().spawnFallingBlock(location, Material.valueOf("WOOL"), (byte) ThreadLocalRandom.current().nextInt(15));
        fallingBlock.setVelocity(new Vector(d, 0.75, d3));
        return fallingBlock;
    }

}
