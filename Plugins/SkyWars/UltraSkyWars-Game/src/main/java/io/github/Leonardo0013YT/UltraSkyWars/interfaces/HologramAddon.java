package io.github.Leonardo0013YT.UltraSkyWars.interfaces;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface HologramAddon {

    void createHologram(Location spawn, List<String> lines);

    void createHologram(Location spawn, List<String> lines, ItemStack item);

    void deleteHologram(Location spawn);

    boolean hasHologram(Location spawn);

    void remove();
}