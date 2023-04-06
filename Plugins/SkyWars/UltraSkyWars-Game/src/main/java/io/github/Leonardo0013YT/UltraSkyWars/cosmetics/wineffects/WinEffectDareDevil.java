package io.github.Leonardo0013YT.UltraSkyWars.cosmetics.wineffects;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.interfaces.WinEffect;
import io.github.Leonardo0013YT.UltraSkyWars.superclass.Game;
import io.github.Leonardo0013YT.UltraSkyWars.xseries.XSound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.metadata.FixedMetadataValue;

public class WinEffectDareDevil implements WinEffect {

    private Vehicle horse;

    @Override
    public void start(Player p, Game game) {
        horse = UltraSkyWars.get().getVc().getNMS().spawnHorse(p.getLocation(), p);
        horse.setPassenger(p);
        horse.setMetadata("NO_TARGET", new FixedMetadataValue(UltraSkyWars.get(), ""));
        p.getWorld().playSound(p.getLocation(), XSound.ENTITY_ENDER_DRAGON_GROWL.parseSound(), 1.0f, 1.0f);
    }

    @Override
    public void stop() {
        if (horse != null) {
            horse.remove();
        }
    }

    @Override
    public WinEffect clone() {
        return new WinEffectDareDevil();
    }

}