package io.github.Leonardo0013YT.UltraSkyWars.cosmetics.killeffects;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.interfaces.KillEffect;
import io.github.Leonardo0013YT.UltraSkyWars.utils.Utils;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Random;

public class KillEffectFirework implements KillEffect, Cloneable {

    private Random random;

    public KillEffectFirework() {
        this.random = new Random();
    }

    @Override
    public void loadCustoms(UltraSkyWars plugin, String path) {
    }

    @Override
    public void start(Player p, Player death, Location loc) {
        if (death == null || !death.isOnline()) {
            return;
        }
        Utils.firework(death.getLocation());
    }

    @Override
    public void stop() {
    }

    @Override
    public KillEffect clone() {
        return new KillEffectFirework();
    }

}