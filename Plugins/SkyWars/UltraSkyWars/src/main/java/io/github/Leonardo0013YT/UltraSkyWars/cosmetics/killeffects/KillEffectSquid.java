package io.github.Leonardo0013YT.UltraSkyWars.cosmetics.killeffects;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.enums.CustomSound;
import io.github.Leonardo0013YT.UltraSkyWars.interfaces.KillEffect;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Squid;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class KillEffectSquid implements KillEffect, Cloneable {

    private static int lavaAmount, explosionAmount;
    private static boolean loaded = false;
    private BukkitTask task;
    private int pased = 0;

    @Override
    public void loadCustoms(UltraSkyWars plugin, String path) {
        if (!loaded) {
            lavaAmount = plugin.getKilleffect().getIntOrDefault(path + ".lavaAmount", 1);
            explosionAmount = plugin.getKilleffect().getIntOrDefault(path + ".explosionAmount", 1);
            loaded = true;
        }
    }

    @Override
    public void start(Player p, Player death, Location loc) {
        Squid squid = loc.getWorld().spawn(loc, Squid.class);
        squid.setNoDamageTicks(Integer.MAX_VALUE);
        squid.setMetadata("KILLEFFECT", new FixedMetadataValue(UltraSkyWars.get(), "KILLEFFECT"));
        UltraSkyWars plugin = UltraSkyWars.get();
        task = new BukkitRunnable() {
            @Override
            public void run() {
                if (death == null || !death.isOnline()) {
                    squid.remove();
                    cancel();
                    return;
                }
                pased++;
                if (pased >= 20) {
                    squid.remove();
                    cancel();
                    return;
                }
                Location loc = squid.getLocation().clone().add(0, 0.3 * pased, 0);
                squid.teleport(loc);
                loc.getWorld().playEffect(loc, Effect.EXPLOSION, explosionAmount);
                loc.getWorld().playEffect(loc, Effect.LAVADRIP, lavaAmount);
                CustomSound.KILLEFFECTS_SQUID.reproduce(p);
            }
        }.runTaskTimer(plugin, 0, 2);
    }

    @Override
    public void stop() {
        if (task != null) {
            task.cancel();
        }
    }

    @Override
    public KillEffect clone() {
        return new KillEffectSquid();
    }

}