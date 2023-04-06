package io.github.Leonardo0013YT.UltraSkyWars.cosmetics.wineffects;

import io.github.Leonardo0013YT.UltraSkyWars.interfaces.WinEffect;
import io.github.Leonardo0013YT.UltraSkyWars.superclass.Game;
import org.bukkit.entity.Player;

public class WinEffectShield implements WinEffect {

    @Override
    public void start(Player p, Game game) {

    }

    @Override
    public void stop() {

    }

    @Override
    public WinEffect clone() {
        return new WinEffectShield();
    }

}