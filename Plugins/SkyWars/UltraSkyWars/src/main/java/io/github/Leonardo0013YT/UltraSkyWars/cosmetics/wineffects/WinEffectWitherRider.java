package io.github.Leonardo0013YT.UltraSkyWars.cosmetics.wineffects;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.interfaces.WinEffect;
import io.github.Leonardo0013YT.UltraSkyWars.superclass.Game;
import io.github.Leonardo0013YT.UltraSkyWars.xseries.XSound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wither;
import org.bukkit.metadata.FixedMetadataValue;

public class WinEffectWitherRider implements WinEffect {

    private Wither wither;

    @Override
    public void start(Player p, Game game) {
        wither = p.getWorld().spawn(p.getLocation(), Wither.class);
        wither.setPassenger(p);
        wither.setMetadata("NO_TARGET", new FixedMetadataValue(UltraSkyWars.get(), ""));
        p.getWorld().playSound(p.getLocation(), XSound.ENTITY_WITHER_SPAWN.parseSound(), 1.0f, 1.0f);
    }

    @Override
    public void stop() {
        if (wither != null) {
            wither.remove();
        }
    }

    @Override
    public WinEffect clone() {
        return new WinEffectWitherRider();
    }

}