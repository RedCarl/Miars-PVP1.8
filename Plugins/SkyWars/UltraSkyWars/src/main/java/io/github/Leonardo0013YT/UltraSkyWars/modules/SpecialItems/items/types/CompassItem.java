package io.github.Leonardo0013YT.UltraSkyWars.modules.SpecialItems.items.types;

import io.github.Leonardo0013YT.UltraSkyWars.modules.SpecialItems.InjectionSpecialItems;
import io.github.Leonardo0013YT.UltraSkyWars.modules.SpecialItems.items.SpecialItem;
import lombok.Getter;

@Getter
public class CompassItem extends SpecialItem {

    private final int time;
    private final double range;
    private final String noTarget, targeted, countdown;

    public CompassItem(InjectionSpecialItems isi) {
        this.range = isi.getSpecial_items().getDouble("items.compass.range");
        this.time = isi.getSpecial_items().getInt("items.compass.time");
        this.noTarget = isi.getSpecial_items().get("items.compass.noTarget");
        this.targeted = isi.getSpecial_items().get("items.compass.targeted");
        this.countdown = isi.getSpecial_items().get("items.compass.countdown");
    }

}