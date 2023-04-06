package io.github.Leonardo0013YT.UltraSkyWarsSetup.setup.cosmetics;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.utils.ItemBuilder;
import io.github.Leonardo0013YT.UltraSkyWars.xseries.XMaterial;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BalloonSetup {

    private Player p;
    private ItemStack icon;
    private String name, permission, url;
    private int slot, page, price;
    private boolean isBuy;

    public BalloonSetup(Player p, String name) {
        this.p = p;
        this.name = name;
        this.permission = "ultraskywars.ballons." + name;
        this.url = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjVjNGY3ZjRiMzJkOGYwNWRlZmZkYTBlMmQ3YTJkY2YyN2JjODEwYWExYTUwYjM3YjNjY2Q3OGZkNzc3NmFmZCJ9fX0=";
        this.icon = ItemBuilder.createSkull("§aDefault", "§7This is a default balloon", url);
        this.slot = 10;
        this.page = 1;
        this.price = 500;
        this.isBuy = true;
    }

    public void saveBalloon(Player p) {
        UltraSkyWars plugin = UltraSkyWars.get();
        plugin.getBalloon().set("balloons." + name + ".id", plugin.getCos().getNextBalloonsId());
        plugin.getBalloon().set("balloons." + name + ".name", name);
        plugin.getBalloon().set("balloons." + name + ".permission", permission);
        plugin.getBalloon().set("balloons." + name + ".url", url);
        plugin.getBalloon().set("balloons." + name + ".icon", ItemBuilder.item(XMaterial.PLAYER_HEAD, 1, "§a" + name, "§7This is a default lore."));
        plugin.getBalloon().set("balloons." + name + ".slot", slot);
        plugin.getBalloon().set("balloons." + name + ".page", page);
        plugin.getBalloon().set("balloons." + name + ".price", price);
        plugin.getBalloon().set("balloons." + name + ".isBuy", isBuy);
        plugin.getBalloon().save();
        p.sendMessage(plugin.getLang().get(p, "setup.balloons.save"));
        plugin.getCos().reload();
    }

    public ItemStack getIcon() {
        return icon;
    }

    public void setIcon(ItemStack icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public boolean isBuy() {
        return isBuy;
    }

    public void setBuy(boolean buy) {
        isBuy = buy;
    }
}