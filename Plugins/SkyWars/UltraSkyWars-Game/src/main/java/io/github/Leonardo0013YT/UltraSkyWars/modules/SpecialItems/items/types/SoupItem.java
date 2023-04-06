package io.github.Leonardo0013YT.UltraSkyWars.modules.SpecialItems.items.types;

import io.github.Leonardo0013YT.UltraSkyWars.modules.SpecialItems.InjectionSpecialItems;
import io.github.Leonardo0013YT.UltraSkyWars.modules.SpecialItems.items.SpecialItem;
import lombok.Getter;

@Getter
public class SoupItem extends SpecialItem {

    private final String maxHealth;
    private final double health;

    public SoupItem(InjectionSpecialItems isi) {
        this.maxHealth = isi.getSpecial_items().get("items.soup.maxHealth");
        this.health = isi.getSpecial_items().getDouble("items.soup.hearts");
    }

}