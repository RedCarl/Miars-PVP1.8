package io.github.Leonardo0013YT.UltraSkyWars.modules.soulwell.soulwell;

import io.github.Leonardo0013YT.UltraSkyWars.modules.soulwell.InjectionSoulWell;
import io.github.Leonardo0013YT.UltraSkyWars.utils.ItemBuilder;
import io.github.Leonardo0013YT.UltraSkyWars.utils.NBTEditor;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@Getter
public class SoulWellShop {

    private Material material;
    private short data;
    private int amount, slot, give;
    private double price;
    private String name, lore;
    private InjectionSoulWell is;
    private String key;

    public SoulWellShop(InjectionSoulWell is, String path, String key) {
        this.is = is;
        this.key = key;
        this.material = Material.valueOf(is.getSoulwell().get(path + ".material"));
        this.data = (short) is.getSoulwell().getInt(path + ".data");
        this.amount = is.getSoulwell().getInt(path + ".amount");
        this.slot = is.getSoulwell().getInt(path + ".slot");
        this.give = is.getSoulwell().getInt(path + ".give");
        this.price = is.getSoulwell().getDouble(path + ".price");
        this.name = is.getSoulwell().get(path + ".name");
        this.lore = is.getSoulwell().get(path + ".lore");
    }

    public ItemStack getIcon(double price) {
        ItemStack icon = ItemBuilder.item(material, amount, data, name, lore.replaceAll("<status>", (price >= this.price) ? is.getSoulwell().get("buy") : is.getSoulwell().get("noMoney")).replaceAll("<price>", String.valueOf(this.price)).replaceAll("<souls>", String.valueOf(give)));
        return NBTEditor.set(icon, key, "SOULWELL", "SHOP", "SOULS");
    }

}