package io.github.Leonardo0013YT.UltraSkyWars.modules.soulwell;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.config.Settings;
import io.github.Leonardo0013YT.UltraSkyWars.interfaces.Injection;
import io.github.Leonardo0013YT.UltraSkyWars.modules.soulwell.listeners.GameListener;
import io.github.Leonardo0013YT.UltraSkyWars.modules.soulwell.listeners.MenuListener;
import io.github.Leonardo0013YT.UltraSkyWars.modules.soulwell.listeners.PlayerListener;
import io.github.Leonardo0013YT.UltraSkyWars.modules.soulwell.managers.SoulWellManager;
import io.github.Leonardo0013YT.UltraSkyWars.modules.soulwell.menus.SoulWellMenu;
import lombok.Getter;

@Getter
public class InjectionSoulWell implements Injection {

    private UltraSkyWars plugin;
    private Settings soulwell;
    private SoulWellManager swm;
    private SoulWellMenu wel;

    @Override
    public void loadInjection(UltraSkyWars plugin) {
        this.plugin = plugin;
        this.soulwell = new Settings("modules/soulwell", true, false);
        this.swm = new SoulWellManager(plugin, this);
        this.wel = new SoulWellMenu(plugin, this);
        plugin.getServer().getPluginManager().registerEvents(new PlayerListener(plugin, this), plugin);
        plugin.getServer().getPluginManager().registerEvents(new MenuListener(plugin, this), plugin);
        if (!plugin.getCm().isBungeeModeLobby()) {
            plugin.getServer().getPluginManager().registerEvents(new GameListener(plugin, this), plugin);
        }
    }

    @Override
    public void reload() {

    }

    @Override
    public void disable() {
    }

}