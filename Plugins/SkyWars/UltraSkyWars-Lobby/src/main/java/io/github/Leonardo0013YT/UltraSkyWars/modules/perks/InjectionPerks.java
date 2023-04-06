package io.github.Leonardo0013YT.UltraSkyWars.modules.perks;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.config.Settings;
import io.github.Leonardo0013YT.UltraSkyWars.interfaces.Injection;
import io.github.Leonardo0013YT.UltraSkyWars.modules.perks.listeners.PerksListener;
import io.github.Leonardo0013YT.UltraSkyWars.modules.perks.managers.PerkManager;
import org.bukkit.Bukkit;

public class InjectionPerks implements Injection {

    private Injection instance;
    private PerkManager pem;
    private Settings perks;

    @Override
    public void loadInjection(UltraSkyWars main) {
        instance = this;
        perks = new Settings("modules/perks", true, false);
        pem = new PerkManager(this);
        if (!main.getCm().isBungeeModeLobby()) {
            Bukkit.getServer().getPluginManager().registerEvents(new PerksListener(main, this), main);
        }
    }

    @Override
    public void reload() {
        perks.reload();
        pem.loadPerks();
    }

    @Override
    public void disable() {

    }

    public Settings getPerks() {
        return perks;
    }

    public PerkManager getPem() {
        return pem;
    }

    public Injection getInstance() {
        return instance;
    }
}