package io.github.Leonardo0013YT.UltraSkyWars.modules.soulwell.listeners;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.modules.soulwell.InjectionSoulWell;
import org.bukkit.event.Listener;

public class GameListener implements Listener {

    private UltraSkyWars plugin;
    private InjectionSoulWell is;

    public GameListener(UltraSkyWars plugin, InjectionSoulWell is) {
        this.plugin = plugin;
        this.is = is;
    }

}