package io.github.Leonardo0013YT.UltraSkyWars.modules.lprotection;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.config.Settings;
import io.github.Leonardo0013YT.UltraSkyWars.interfaces.Injection;
import io.github.Leonardo0013YT.UltraSkyWars.modules.lprotection.listeners.LobbyListener;
import io.github.Leonardo0013YT.UltraSkyWars.modules.lprotection.managers.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class InjectionLProtection implements Injection {

    private Settings lobbyOptions;
    private ConfigManager cm;

    @Override
    public void loadInjection(UltraSkyWars main) {
        lobbyOptions = new Settings("modules/lobbyOptions", true, false);
        cm = new ConfigManager(main, this);
        Bukkit.getServer().getPluginManager().registerEvents(new LobbyListener(main, this), main);
        if (cm.isNoDayCycle()) {
            World w = Bukkit.getWorld(cm.getLobbyWorld());
            if (w != null) {
                w.setGameRuleValue("doDaylightCycle", "false");
            }
        }
    }

    @Override
    public void reload() {
        lobbyOptions.reload();
        cm.reload();
    }

    @Override
    public void disable() {

    }

    public Settings getLobbyOptions() {
        return lobbyOptions;
    }

    public ConfigManager getCm() {
        return cm;
    }
}