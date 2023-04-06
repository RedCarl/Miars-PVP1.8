package io.github.Leonardo0013YT.UltraSkyWarsSetup.setup;

import io.github.Leonardo0013YT.UltraSkyWars.utils.Utils;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class ItemSetup {

    private ItemStack item, display;
    private boolean center, refill;
    private int chance;
    private ArrayList<String> modes;

    public ItemSetup(ItemStack item, boolean center, boolean refill, int chance, ArrayList<String> modes) {
        this.item = item;
        this.display = item.clone();
        this.center = center;
        this.refill = refill;
        this.chance = chance;
        this.modes = modes;
        ItemMeta im = display.getItemMeta();
        im.setDisplayName("§a" + toString());
        im.setLore(Arrays.asList("§7", "§eCenter: §b" + center, "§eRefill: §b" + refill, "§eChance: §b" + chance, "§eModes: §b" + Utils.formatList(modes), "§7"));
        display.setItemMeta(im);
    }

    public ItemStack getItem() {
        return item;
    }

    public boolean isRefill() {
        return refill;
    }

    public ArrayList<String> getModes() {
        return modes;
    }

    public boolean isCenter() {
        return center;
    }

    public int getChance() {
        return chance;
    }

    public ItemStack getDisplay() {
        return display;
    }

    @Override
    public String toString() {
        return item.getType().name().toLowerCase() + "_" + item.getAmount() + "_" + item.getDurability() + "_" + item.getData().getData() + "_" + ThreadLocalRandom.current().nextInt(0, 999);
    }

}