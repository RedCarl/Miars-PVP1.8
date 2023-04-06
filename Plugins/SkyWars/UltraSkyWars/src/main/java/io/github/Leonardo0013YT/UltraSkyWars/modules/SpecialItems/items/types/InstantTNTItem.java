package io.github.Leonardo0013YT.UltraSkyWars.modules.SpecialItems.items.types;

import io.github.Leonardo0013YT.UltraSkyWars.modules.SpecialItems.InjectionSpecialItems;
import io.github.Leonardo0013YT.UltraSkyWars.modules.SpecialItems.items.SpecialItem;
import lombok.Getter;

@Getter
public class InstantTNTItem extends SpecialItem {

    private final int fuse_ticks;

    public InstantTNTItem(InjectionSpecialItems isi) {
        this.fuse_ticks = isi.getSpecial_items().getInt("items.instantTNT.fuse_ticks");
    }

}