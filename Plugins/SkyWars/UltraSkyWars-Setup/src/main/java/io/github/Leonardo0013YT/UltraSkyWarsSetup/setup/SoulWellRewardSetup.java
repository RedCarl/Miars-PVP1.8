package io.github.Leonardo0013YT.UltraSkyWarsSetup.setup;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.enums.RewardType;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;

public class SoulWellRewardSetup {

    private String name;
    private ArrayList<String> cmds = new ArrayList<>();
    private ItemStack icon = new ItemStack(Material.ARROW, 1);
    private RewardType type;
    private double chance;

    public SoulWellRewardSetup(String name, RewardType type, double chance) {
        this.name = name;
        this.type = type;
        this.chance = chance;
    }

    public ArrayList<String> getCmds() {
        return cmds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ItemStack getIcon() {
        ItemStack icon = this.icon.clone();
        ItemMeta iconM = icon.getItemMeta();
        iconM.setDisplayName(name.replaceAll("&", "§"));
        String s = UltraSkyWars.get().getLang().get(null, "menus.soulwellmenu.item.loreItem").replace("<rarity>", UltraSkyWars.get().getLang().get(null, "soulwell.rarity." + type.name().toLowerCase())).replaceAll("<chance>", String.valueOf(chance));
        iconM.setLore(s.isEmpty() ? new ArrayList<>() : Arrays.asList(s.split("\\n")));
        icon.setItemMeta(iconM);
        return icon;
    }

    public void setIcon(ItemStack icon) {
        this.icon = icon;
    }

    public double getChance() {
        return chance;
    }

    public void setChance(double chance) {
        this.chance = chance;
    }

    public RewardType getType() {
        return type;
    }

    public void setType(RewardType type) {
        this.type = type;
    }
}