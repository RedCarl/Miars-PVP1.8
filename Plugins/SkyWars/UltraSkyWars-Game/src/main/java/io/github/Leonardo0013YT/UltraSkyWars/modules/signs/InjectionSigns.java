package io.github.Leonardo0013YT.UltraSkyWars.modules.signs;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.config.Settings;
import io.github.Leonardo0013YT.UltraSkyWars.interfaces.Injection;
import io.github.Leonardo0013YT.UltraSkyWars.modules.signs.listeners.SignsListener;
import io.github.Leonardo0013YT.UltraSkyWars.modules.signs.managers.SignsManager;
import org.bukkit.Bukkit;

public class InjectionSigns implements Injection {

    private SignsManager sim;
    private Settings signs;

    @Override
    public void loadInjection(UltraSkyWars main) {
        signs = new Settings("modules/signs", false, false);
        sim = new SignsManager(main, this);
        Bukkit.getServer().getPluginManager().registerEvents(new SignsListener(main, this), main);
    }

    @Override
    public void reload() {
        signs.reload();
        sim.reload();
    }

    @Override
    public void disable() {

    }

    public SignsManager getSim() {
        return sim;
    }

    public Settings getSigns() {
        return signs;
    }
}