package io.github.Leonardo0013YT.UltraSkyWars.cosmetics;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.superclass.Cosmetic;
import io.github.Leonardo0013YT.UltraSkyWars.utils.Utils;
import io.github.Leonardo0013YT.UltraSkyWars.xseries.XSound;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class KillSound extends Cosmetic {

    private Sound sound;
    private float vol1, vol2;
    private ItemStack icon;

    public KillSound(UltraSkyWars plugin, String s) {
        super(plugin.getKillsound(), s, "killsound");
        Optional<XSound> xs = XSound.matchXSound(plugin.getKillsound().get(s + ".sound"));
        if (xs.isPresent()) {
            this.sound = xs.get().parseSound();
        } else {
            this.sound = XSound.ENTITY_PLAYER_LEVELUP.parseSound();
        }
        this.icon = Utils.getIcon(plugin.getKillsound(), s);
        this.vol1 = (float) plugin.getKillsound().getConfig().getDouble(s + ".vol1");
        this.vol2 = (float) plugin.getKillsound().getConfig().getDouble(s + ".vol1");
        plugin.getCos().setLastPage("KillSound", page);
    }

    public Sound getSound() {
        return sound;
    }

    public float getVol1() {
        return vol1;
    }

    public float getVol2() {
        return vol2;
    }

    public void execute(Player k, Player d) {
        k.playSound(k.getLocation(), sound, getVol1(), getVol2());
        d.playSound(d.getLocation(), sound, getVol1(), getVol2());
    }

}