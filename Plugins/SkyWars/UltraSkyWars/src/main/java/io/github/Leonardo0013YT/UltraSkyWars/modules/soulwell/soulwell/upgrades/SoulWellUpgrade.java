package io.github.Leonardo0013YT.UltraSkyWars.modules.soulwell.soulwell.upgrades;

import io.github.Leonardo0013YT.UltraSkyWars.modules.soulwell.InjectionSoulWell;
import io.github.Leonardo0013YT.UltraSkyWars.utils.ItemBuilder;
import io.github.Leonardo0013YT.UltraSkyWars.utils.NBTEditor;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@Getter
public class SoulWellUpgrade {

    private Material material;
    private short data;
    private int amount, level;
    private double price;
    private String name, lore;
    private String type;
    private String key;

    public SoulWellUpgrade(InjectionSoulWell is, String path, String type, String key) {
        this.type = type;
        this.key = key;
        this.level = is.getSoulwell().getInt(path + ".level");
        this.material = Material.valueOf(is.getSoulwell().get(path + ".material"));
        this.data = (short) is.getSoulwell().getInt(path + ".data");
        this.amount = is.getSoulwell().getInt(path + ".amount");
        this.price = is.getSoulwell().getDouble(path + ".price");
        this.name = is.getSoulwell().get(path + ".name");
        this.lore = is.getSoulwell().get(path + ".lore");
    }

    public ItemStack getIcon(String status) {
        ItemStack icon = ItemBuilder.item(material, 1, data, name, lore.replaceAll("<status>", status).replaceAll("<price>", String.valueOf(price)).replaceAll("<amount>", String.valueOf(amount)));
        icon = NBTEditor.set(icon, level, "SOULWELL", "UPGRADE", "LEVEL");
        return NBTEditor.set(icon, key, "SOULWELL", "UPGRADE", "KEY");
    }

}