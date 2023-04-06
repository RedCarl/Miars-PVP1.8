package io.github.Leonardo0013YT.UltraSkyWars.objects;

import io.github.Leonardo0013YT.UltraSkyWars.modules.perks.InjectionPerks;
import lombok.Getter;

@Getter
public class PerkLevel {

    private final int level, price, percent, probability;

    public PerkLevel(InjectionPerks plugin, String path) {
        this.level = plugin.getPerks().getInt(path + ".level");
        this.price = plugin.getPerks().getInt(path + ".price");
        this.percent = plugin.getPerks().getInt(path + ".percent");
        this.probability = plugin.getPerks().getInt(path + ".probability");
    }

}