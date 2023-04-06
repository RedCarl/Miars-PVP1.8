package io.github.Leonardo0013YT.UltraSkyWars.modules.soulwell.soulwell;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import org.bukkit.Location;

public class SoulWell {

    private UltraSkyWars plugin;
    private Location loc, hologram;

    public SoulWell(UltraSkyWars plugin, Location loc) {
        this.plugin = plugin;
        this.loc = loc;
        this.hologram = loc.clone().add(0.5, 0, 0.5);
    }

    public void reload() {
        if (plugin.getAdm().hasHologramPlugin()) {
            plugin.getAdm().createHologram(hologram, plugin.getLang().getList("holograms.soulwell"));
        }
    }

    public Location getHologram() {
        return hologram;
    }

    public Location getLoc() {
        return loc;
    }

}