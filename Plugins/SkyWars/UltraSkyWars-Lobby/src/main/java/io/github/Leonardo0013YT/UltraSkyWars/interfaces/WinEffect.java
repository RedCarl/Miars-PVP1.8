package io.github.Leonardo0013YT.UltraSkyWars.interfaces;

import org.bukkit.entity.Player;

public interface WinEffect {

    void start(Player p);

    void stop();

    WinEffect clone();

}