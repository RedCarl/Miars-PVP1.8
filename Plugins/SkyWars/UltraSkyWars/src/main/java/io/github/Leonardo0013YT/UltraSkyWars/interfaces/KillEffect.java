package io.github.Leonardo0013YT.UltraSkyWars.interfaces;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface KillEffect {

    void loadCustoms(UltraSkyWars plugin, String path);

    void start(Player p, Player death, Location loc);

    void stop();
}
