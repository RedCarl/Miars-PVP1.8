package io.github.Leonardo0013YT.UltraSkyWars.cosmetics.windances;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.interfaces.WinDance;
import io.github.Leonardo0013YT.UltraSkyWars.superclass.Game;
import io.github.Leonardo0013YT.UltraSkyWars.team.Team;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class WinDanceDestroyIsland implements WinDance, Cloneable {

    private static boolean loaded = false;
    private static int spawnLaterTick, amountTNT, perFuseAmount;
    private BukkitTask task;

    @Override
    public void loadCustoms(UltraSkyWars plugin, String path) {
        if (!loaded) {
            spawnLaterTick = plugin.getWindance().getIntOrDefault(path + ".spawnLaterTick", 20);
            amountTNT = plugin.getWindance().getIntOrDefault(path + ".amountTNT", 4);
            perFuseAmount = plugin.getWindance().getIntOrDefault(path + ".perFuseAmount", 15);
            loaded = true;
        }
    }

    @Override
    public void start(Player p, Game game) {
        World world = game.getSpectator().getWorld();
        task = new BukkitRunnable() {
            public void run() {
                if (p == null || !p.isOnline() || !world.getName().equals(p.getWorld().getName())) {
                    stop();
                    return;
                }
                for (Team team : game.getTeams().values()) {
                    explode(team.getSpawn().clone());
                }
            }
        }.runTaskLater(UltraSkyWars.get(), spawnLaterTick);
    }

    private void explode(Location loc) {
        loc.setY(loc.getWorld().getHighestBlockYAt(loc.getBlockX(), loc.getBlockZ()));
        loc.getWorld().strikeLightning(loc);
        int pa = perFuseAmount;
        for (int i = 0; i < amountTNT; i++) {
            TNTPrimed tnt = loc.getWorld().spawn(loc, TNTPrimed.class);
            tnt.setFuseTicks(pa);
            pa += perFuseAmount;
        }
    }

    @Override
    public void stop() {
        if (task != null) {
            task.cancel();
        }
    }

    @Override
    public WinDance clone() {
        return new WinDanceDestroyIsland();
    }
}