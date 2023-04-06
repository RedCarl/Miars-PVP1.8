package io.github.Leonardo0013YT.UltraSkyWars.modules.mobfriends;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.interfaces.Injection;
import io.github.Leonardo0013YT.UltraSkyWars.modules.mobfriends.listeners.MobFriendsListener;
import io.github.Leonardo0013YT.UltraSkyWars.modules.mobfriends.listeners.TargetListener;
import org.bukkit.Bukkit;

public class InjectionMobFriends implements Injection {

    @Override
    public void loadInjection(UltraSkyWars main) {
        Bukkit.getServer().getPluginManager().registerEvents(new MobFriendsListener(main), main);
        Bukkit.getServer().getPluginManager().registerEvents(new TargetListener(main), main);
    }

    @Override
    public void reload() {

    }

    @Override
    public void disable() {

    }

}