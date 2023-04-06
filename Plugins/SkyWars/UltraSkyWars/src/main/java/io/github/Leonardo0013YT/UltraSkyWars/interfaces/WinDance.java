package io.github.Leonardo0013YT.UltraSkyWars.interfaces;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.superclass.Game;
import org.bukkit.entity.Player;

public interface WinDance {

    void loadCustoms(UltraSkyWars plugin, String path);

    void start(Player p, Game game);

    void stop();

}
