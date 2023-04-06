package io.github.Leonardo0013YT.UltraSkyWarsSetup.setup.cosmetics;

import io.github.Leonardo0013YT.UltraSkyWars.xseries.XMaterial;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class KitLevelSetup {

    private Player p;
    private ItemStack[] inv = new ItemStack[]{}, armors = new ItemStack[]{};
    private ItemStack icon = new ItemStack(Material.DIAMOND_SWORD, 1);
    private int price = 100, slot = 10;
    private boolean buy = true;

    public KitLevelSetup(Player p) {
        this.p = p;
    }

    public Player getP() {
        return p;
    }

    public boolean isBuy() {
        return buy;
    }

    public void setBuy(boolean buy) {
        this.buy = buy;
    }

    public ItemStack[] getInv() {
        return inv;
    }

    public void setInv(ItemStack[] inv) {
        this.inv = inv;
    }

    public ItemStack[] getArmors() {
        return armors;
    }

    public void setArmors(ItemStack[] armors) {
        this.armors = armors;
    }

    public ItemStack getIcon() {
        if (icon == null || icon.getType().equals(Material.AIR)) {
            return new ItemStack(XMaterial.DIAMOND_SWORD.parseMaterial());
        }
        return icon;
    }

    public void setIcon(ItemStack icon) {
        this.icon = icon;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

}