package io.github.Leonardo0013YT.UltraSkyWars.interfaces;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface KillEffect {

    void start(Player p, Player death, Location loc);

    void stop();
}
