package io.github.Leonardo0013YT.UltraSkyWars.cosmetics.killeffects;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.interfaces.KillEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class KillEffectFlowerPower implements KillEffect, Cloneable {

    private static double xRandom, yRandom, zRandom, flowersAmount;
    private static int delayDelete;
    private static boolean loaded = false;
    private ItemStack[] items = {new ItemStack(Material.RED_ROSE, 1, (short) 0), new ItemStack(Material.RED_ROSE, 1, (short) 1), new ItemStack(Material.RED_ROSE, 1, (short) 2), new ItemStack(Material.RED_ROSE, 1, (short) 3), new ItemStack(Material.RED_ROSE, 1, (short) 4), new ItemStack(Material.RED_ROSE, 1, (short) 5), new ItemStack(Material.RED_ROSE, 1, (short) 6), new ItemStack(Material.RED_ROSE, 1, (short) 7), new ItemStack(Material.RED_ROSE, 1, (short) 8), new ItemStack(Material.YELLOW_FLOWER, 1, (short) 0)};

    @Override
    public void loadCustoms(UltraSkyWars plugin, String path) {
        if (!loaded) {
            xRandom = plugin.getKilleffect().getDoubleOrDefault(path + ".xRandom", 0.35);
            yRandom = plugin.getKilleffect().getDoubleOrDefault(path + ".yRandom", 0.5);
            zRandom = plugin.getKilleffect().getDoubleOrDefault(path + ".xRandom", 0.35);
            flowersAmount = plugin.getKilleffect().getIntOrDefault(path + ".flowersAmount", 10);
            delayDelete = plugin.getKilleffect().getIntOrDefault(path + ".delayDelete", 40);
            loaded = true;
        }
    }

    @Override
    public void start(Player p, Player death, Location loc) {
        if (death == null || !death.isOnline()) {
            return;
        }
        ArrayList<Item> it = new ArrayList<>();
        for (int i = 0; i < flowersAmount; i++) {
            it.add(spawnFlower(loc, random(-xRandom, xRandom), yRandom, random(-zRandom, zRandom)));
        }
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
    }

    @Override
    public KillEffect clone() {
        return new KillEffectFlowerPower();
    }

    protected double random(double d, double d2) {
        return d + ThreadLocalRandom.current().nextDouble() * (d2 - d);
    }

    private Item spawnFlower(Location location, double d, double d2, double d3) {
        Item item = location.getWorld().dropItem(location, items[ThreadLocalRandom.current().nextInt(items.length)]);
        item.setVelocity(new Vector(d, d2, d3));
        item.setPickupDelay(Integer.MAX_VALUE);
        return item;
    }

}