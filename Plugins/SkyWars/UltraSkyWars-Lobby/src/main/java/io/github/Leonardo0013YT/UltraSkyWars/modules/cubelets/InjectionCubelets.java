package io.github.Leonardo0013YT.UltraSkyWars.modules.cubelets;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.config.Settings;
import io.github.Leonardo0013YT.UltraSkyWars.interfaces.Injection;
import io.github.Leonardo0013YT.UltraSkyWars.modules.cubelets.listeners.CubeletsListener;
import io.github.Leonardo0013YT.UltraSkyWars.modules.cubelets.managers.CubeletsManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;

import java.util.ArrayList;

public class InjectionCubelets implements Injection {

    private CubeletsManager cbm;
    private Settings cubelets;
    private ArrayList<Entity> entities = new ArrayList<>();

    @Override
    public void loadInjection(UltraSkyWars main) {
        cubelets = new Settings("modules/cubelets", true, false);
        cbm = new CubeletsManager(main, this);
        Bukkit.getServer().getPluginManager().registerEvents(new CubeletsListener(main, this), main);
    }

    public void reload() {
        cubelets.reload();
        cbm.reload();
    }

    @Override
    public void disable() {

    }

    public ArrayList<Entity> getEntities() {
        return entities;
    }

    public Settings getCubelets() {
        return cubelets;
    }

    public CubeletsManager getCbm() {
        return cbm;
    }
}