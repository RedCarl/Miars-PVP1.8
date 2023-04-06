package io.github.Leonardo0013YT.UltraSkyWars.cosmetics.wineffects;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.interfaces.WinEffect;
import io.github.Leonardo0013YT.UltraSkyWars.superclass.Game;
import io.github.Leonardo0013YT.UltraSkyWars.xseries.XSound;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

public class WinEffectDragonRider implements WinEffect {

    private EnderDragon dragon;

    @Override
    public void start(Player p, Game game) {
        dragon = p.getWorld().spawn(p.getLocation(), EnderDragon.class);
        dragon.setPassenger(p);
        dragon.setMetadata("NO_TARGET", new FixedMetadataValue(UltraSkyWars.get(), ""));
        p.getWorld().playSound(p.getLocation(), XSound.ENTITY_ENDER_DRAGON_GROWL.parseSound(), 1.0f, 1.0f);
    }

    @Override
    public void stop() {
        if (dragon != null) {
            dragon.remove();
        }
    }

    @Override
    public WinEffect clone() {
        return new WinEffectDragonRider();
    }

}