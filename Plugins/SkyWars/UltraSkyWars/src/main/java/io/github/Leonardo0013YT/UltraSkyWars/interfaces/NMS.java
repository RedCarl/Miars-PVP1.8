package io.github.Leonardo0013YT.UltraSkyWars.interfaces;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;

public interface NMS {

    Vehicle spawnHorse(Location loc, Player p);

    Collection<Integer> spawn(Player p, Location loc, ItemStack head);

    void destroy(Player p, Collection<Integer> id);

    void followPlayer(Player player, LivingEntity entity, double d);

    void displayParticle(Player p, Location location, float offsetX, float offsetY, float offsetZ, int speed, String enumParticle, int amount);

    void broadcastParticle(Location location, float offsetX, float offsetY, float offsetZ, int speed, String enumParticle, int amount, double range);

    boolean isParticle(String particle);

}