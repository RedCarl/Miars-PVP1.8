package io.github.Leonardo0013YT.UltraSkyWars.cosmetics.killeffects;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.interfaces.KillEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class KillEffectCookieFountain implements KillEffect, Cloneable {

    private static boolean loaded = false;
    private static double xRandom, yRandom, zRandom, flowersAmount;
    private static int delayDelete;
    private ArrayList<Item> it = new ArrayList<>();
    private BukkitTask task;

    @Override
    public void loadCustoms(UltraSkyWars plugin, String path) {
        if (!loaded) {
            xRandom = plugin.getKilleffect().getDoubleOrDefault(path + ".xRandom", 0.35);
            yRandom = plugin.getKilleffect().getDoubleOrDefault(path + ".yRandom", 0.5);
            zRandom = plugin.getKilleffect().getDoubleOrDefault(path + ".xRandom", 0.35);
            flowersAmount = plugin.getKilleffect().getIntOrDefault(path + ".roundsDuration", 20);
            delayDelete = plugin.getKilleffect().getIntOrDefault(path + ".delayDelete", 40);
            loaded = true;
        }
    }

    @Override
    public void start(Player p, Player death, Location loc) {
        task = new BukkitRunnable() {
            int executes = 0;

            @Override
            public void run() {
                if (death == null || !death.isOnline()) {
                    stop();
                    return;
                }
                if (executes >= flowersAmount) {
                    stop();
                    return;
                }
                for (int i = 0; i < 2; i++) {
                    it.add(spawnCookie(loc, random(-xRandom, xRandom), yRandom, random(-zRandom, zRandom)));
                }
                executes++;
            }
        }.runTaskTimer(UltraSkyWars.get(), 2, 2);
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Item itemStack : it) {
                    itemStack.remove();
                }
            }
        }.runTaskLater(UltraSkyWars.get(), delayDelete);
    }

    @Override
    public void stop() {
        if (task != null) {
            task.cancel();
        }
        for (Item itemStack : it) {
            itemStack.remove();
        }
        it.clear();
    }

    @Override
    public KillEffect clone() {
        return new KillEffectCookieFountain();
    }

    protected double random(double d, double d2) {
        return d + ThreadLocalRandom.current().nextDouble() * (d2 - d);
    }

    private Item spawnCookie(Location location, double d, double d2, double d3) {
        Item item = location.getWorld().dropItem(location, new ItemStack(Material.COOKIE));
        item.setVelocity(new Vector(d, d2, d3));
        item.setPickupDelay(Integer.MAX_VALUE);
        return item;
    }

}