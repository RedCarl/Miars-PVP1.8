package io.github.Leonardo0013YT.UltraSkyWars.modules.pwt;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.interfaces.Injection;
import io.github.Leonardo0013YT.UltraSkyWars.modules.pwt.listeners.PWTListener;
import org.bukkit.Bukkit;

public class InjectionPWT implements Injection {

    @Override
    public void loadInjection(UltraSkyWars main) {
        Bukkit.getServer().getPluginManager().registerEvents(new PWTListener(), main);
    }

    @Override
    public void reload() {

    }

    @Override
    public void disable() {

    }

}