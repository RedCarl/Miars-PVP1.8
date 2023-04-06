package io.github.Leonardo0013YT.UltraSkyWars.modules.perks.listeners;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.modules.perks.InjectionPerks;
import org.bukkit.event.Listener;

public class PerksListener implements Listener {

    private final UltraSkyWars plugin;
    private final InjectionPerks injectionPerks;

    public PerksListener(UltraSkyWars plugin, InjectionPerks injectionPerks) {
        this.plugin = plugin;
        this.injectionPerks = injectionPerks;
    }

}