package io.github.Leonardo0013YT.UltraSkyWars.modules.perks.perks;

import io.github.Leonardo0013YT.UltraSkyWars.modules.perks.InjectionPerks;
import io.github.Leonardo0013YT.UltraSkyWars.superclass.Perk;

public class PotionPerk extends Perk {

    private final int duration, amplifier;

    public PotionPerk(InjectionPerks plugin, String path, int duration, int amplifier) {
        super(plugin, path);
        this.duration = plugin.getPerks().getIntOrDefault(path + ".duration", duration) * 20;
        this.amplifier = plugin.getPerks().getIntOrDefault(path + ".amplifier", amplifier);
    }

    public int getDuration() {
        return duration;
    }

    public int getAmplifier() {
        return amplifier;
    }
}