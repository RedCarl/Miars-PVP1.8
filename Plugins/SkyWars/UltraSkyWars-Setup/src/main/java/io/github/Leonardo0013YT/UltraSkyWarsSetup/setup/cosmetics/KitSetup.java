package io.github.Leonardo0013YT.UltraSkyWarsSetup.setup.cosmetics;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.HashMap;

public class KitSetup {

    private UltraSkyWars plugin;
    private Player p;
    private HashMap<Integer, KitLevelSetup> levels = new HashMap<>();
    private KitLevelSetup kls;
    private int slot = 10, page = 1;
    private String name;
    private String permission;

    public KitSetup(UltraSkyWars plugin, Player p, String name) {
        this.plugin = plugin;
        this.p = p;
        this.name = name;
        this.permission = "ultraskywars.kit." + name;
    }

    public Player getP() {
        return p;
    }

    public void saveKit(Player p) {
        int id = plugin.getKm().getNextId();
        plugin.getKits().set("kits." + name + ".id", id);
        plugin.getKits().set("kits." + name + ".name", name);
        plugin.getKits().set("kits." + name + ".slot", slot);
        plugin.getKits().set("kits." + name + ".page", page);
        plugin.getKits().set("kits." + name + ".permission", permission);
        for (int level : levels.keySet()) {
            KitLevelSetup nivel = levels.get(level);
            plugin.getKits().set("kits." + name + ".levels." + level + ".level", level);
            plugin.getKits().set("kits." + name + ".levels." + level + ".permission", permission + "." + level);
            ItemStack icon = nivel.getIcon().clone();
            ItemMeta im = icon.getItemMeta();
            im.setDisplayName("§a" + name);
            im.setLore(Arrays.asList("§eDescription:", "§7Default kit lore change in kits.yml.", "§7", "<price>", "§7", "<level>", "§7", "<status>"));
            icon.setItemMeta(im);
            plugin.getKits().set("kits." + name + ".levels." + level + ".icon", icon);
            plugin.getKits().set("kits." + name + ".levels." + level + ".slot", nivel.getSlot());
            plugin.getKits().set("kits." + name + ".levels." + level + ".price", nivel.getPrice());
            plugin.getKits().set("kits." + name + ".levels." + level + ".isBuy", nivel.isBuy());
            plugin.getKits().set("kits." + name + ".levels." + level + ".armor", nivel.getArmors());
            plugin.getKits().set("kits." + name + ".levels." + level + ".inv", nivel.getInv());
        }
        plugin.getKits().save();
        plugin.getKm().setLastID(id + 1);
    }

    public void saveKitLevel(Player p) {
        if (kls.getInv() == null && kls.getArmors() == null) {
            p.sendMessage(plugin.getLang().get(p, "setup.kits.notSet.noInv"));
            return;
        }
        levels.put(levels.size() + 1, kls);
        kls = null;
        p.sendMessage(plugin.getLang().get(p, "setup.kitlevel.save"));
    }

    public KitLevelSetup getKls() {
        return kls;
    }

    public void setKls(KitLevelSetup kls) {
        this.kls = kls;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public HashMap<Integer, KitLevelSetup> getLevels() {
        return levels;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}