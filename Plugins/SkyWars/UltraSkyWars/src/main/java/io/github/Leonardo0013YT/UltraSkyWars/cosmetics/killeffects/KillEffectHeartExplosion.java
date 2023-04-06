package io.github.Leonardo0013YT.UltraSkyWars.cosmetics.killeffects;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.interfaces.KillEffect;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class KillEffectHeartExplosion implements KillEffect, Cloneable {

    private UltraSkyWars plugin;

    @Override
    public void loadCustoms(UltraSkyWars plugin, String path) {
        this.plugin = plugin;
    }

    @Override
    public void start(Player p, Player death, Location loc) {
        if (death == null || !death.isOnline()) {
            return;
        }
        for (int i = 0; i < 10; i++) {
            plugin.getVc().getNMS().broadcastParticle(death.getLocation(), 0, 0, 0, 2, "HEART", 5, 5);
        }
    }

    @Override
    public void stop() {
    }

    @Override
    public KillEffect clone() {
        return new KillEffectHeartExplosion();
    }

}