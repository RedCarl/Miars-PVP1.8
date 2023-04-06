package io.github.Leonardo0013YT.UltraSkyWars.modules.cubelets.managers;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.data.SWPlayer;
import io.github.Leonardo0013YT.UltraSkyWars.modules.cubelets.InjectionCubelets;
import io.github.Leonardo0013YT.UltraSkyWars.modules.cubelets.cubelets.Cubelets;
import io.github.Leonardo0013YT.UltraSkyWars.utils.Utils;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class CubeletsManager {

    private HashMap<Location, Cubelets> cubelets = new HashMap<>();
    private UltraSkyWars plugin;
    private InjectionCubelets injectionCubelets;

    public CubeletsManager(UltraSkyWars plugin, InjectionCubelets injectionCubelets) {
        this.plugin = plugin;
        this.injectionCubelets = injectionCubelets;
        loadCubelets();
    }

    public void loadCubelets() {
        cubelets.clear();
        if (plugin.getConfig().isSet("cubelets")) {
            ConfigurationSection soul = plugin.getConfig().getConfigurationSection("cubelets");
            for (String s : soul.getKeys(false)) {
                Location loc = Utils.getStringLocation(plugin.getConfig().getString("cubelets." + s + ".loc"));
                if (loc == null) continue;
                cubelets.put(loc, new Cubelets(plugin, injectionCubelets, loc));
            }
        }
    }

    public void executeCubelet(Player p) {
        int random = ThreadLocalRandom.current().nextInt(0, 101);
        if (random < plugin.getCm().getCubeletChance()) {
            SWPlayer sw = plugin.getDb().getSWPlayer(p);
            sw.addCubelets(1);
            p.sendMessage(plugin.getLang().get(p, "winCubelet"));
        }
    }

    public HashMap<Location, Cubelets> getCubelets() {
        return cubelets;
    }

    public void reload() {
        cubelets.values().forEach(Cubelets::reload);
    }

}