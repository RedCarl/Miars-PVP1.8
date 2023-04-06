package io.github.Leonardo0013YT.UltraSkyWars.modules.soulwell.soulwell.upgrades;

import io.github.Leonardo0013YT.UltraSkyWars.modules.soulwell.InjectionSoulWell;
import io.github.Leonardo0013YT.UltraSkyWars.utils.ItemBuilder;
import io.github.Leonardo0013YT.UltraSkyWars.xseries.XMaterial;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

@Getter
public class SoulWellAngelOfDeath {

    private int level, probability;
    private double price;
    private String name, lore;
    private InjectionSoulWell is;
    private String key;

    public SoulWellAngelOfDeath(InjectionSoulWell is, String path, String key) {
        this.is = is;
        this.key = key;
        this.level = is.getSoulwell().getInt(path + ".level");
        this.probability = is.getSoulwell().getInt(path + ".probability");
        this.price = is.getSoulwell().getDouble(path + ".price");
        this.name = is.getSoulwell().get(path + ".name");
        this.lore = is.getSoulwell().get(path + ".lore");
    }

    public ItemStack getIcon() {
        ItemStack icon = ItemBuilder.item(XMaterial.WITHER_SKELETON_SKULL, name, lore.replaceAll("<desc>", is.getSoulwell().get("angelLore")));
        return icon;
    }

}