package io.github.Leonardo0013YT.UltraSkyWars.modules.SpecialItems;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.config.Settings;
import io.github.Leonardo0013YT.UltraSkyWars.interfaces.Injection;
import io.github.Leonardo0013YT.UltraSkyWars.modules.SpecialItems.cmds.SpecialItemsCMD;
import io.github.Leonardo0013YT.UltraSkyWars.modules.SpecialItems.listeners.SpecialItemsListener;
import io.github.Leonardo0013YT.UltraSkyWars.modules.SpecialItems.managers.ItemManager;
import lombok.Getter;

@Getter
public class InjectionSpecialItems implements Injection {

    private Settings special_items;
    private ItemManager im;

    @Override
    public void loadInjection(UltraSkyWars plugin) {
        this.special_items = new Settings("modules/special_items", true, false);
        this.im = new ItemManager(plugin, this);
        plugin.getCommand("swi").setExecutor(new SpecialItemsCMD(plugin, this));
        plugin.getServer().getPluginManager().registerEvents(new SpecialItemsListener(plugin, this), plugin);
    }

    @Override
    public void reload() {

    }

    @Override
    public void disable() {

    }

}