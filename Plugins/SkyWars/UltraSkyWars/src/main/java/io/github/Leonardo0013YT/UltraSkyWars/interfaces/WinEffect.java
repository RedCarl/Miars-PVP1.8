package io.github.Leonardo0013YT.UltraSkyWars.interfaces;

import io.github.Leonardo0013YT.UltraSkyWars.superclass.Game;
import org.bukkit.entity.Player;

public interface WinEffect {

    void start(Player p, Game game);

    void stop();

    WinEffect clone();

}