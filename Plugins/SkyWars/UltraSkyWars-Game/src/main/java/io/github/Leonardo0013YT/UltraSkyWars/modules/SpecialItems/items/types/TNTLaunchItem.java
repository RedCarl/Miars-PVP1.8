package io.github.Leonardo0013YT.UltraSkyWars.modules.SpecialItems.items.types;

import io.github.Leonardo0013YT.UltraSkyWars.modules.SpecialItems.InjectionSpecialItems;
import io.github.Leonardo0013YT.UltraSkyWars.modules.SpecialItems.items.SpecialItem;
import lombok.Getter;

@Getter
public class TNTLaunchItem extends SpecialItem {

    private final double multiply;
    private final int noFall;

    public TNTLaunchItem(InjectionSpecialItems isi) {
        this.multiply = isi.getSpecial_items().getInt("items.TNTLaunch.multiply");
        this.noFall = isi.getSpecial_items().getInt("items.TNTLaunch.noFall");
    }

}