package io.github.Leonardo0013YT.UltraSkyWars.inventories;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.superclass.UltraInventory;
import io.github.Leonardo0013YT.UltraSkyWars.utils.ItemBuilder;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class KitsPerksMenu extends UltraInventory {

    public KitsPerksMenu(UltraSkyWars plugin, String name) {
        super(name);
        this.title = plugin.getLang().get("menus." + name + ".title");
        reload();
    }

    @Override
    public void reload() {
        UltraSkyWars plugin = UltraSkyWars.get();
        if (plugin.getMenus().isSet("menus." + name)) {
            this.rows = plugin.getMenus().getInt("menus." + name + ".rows");
            Map<Integer, ItemStack> config = new HashMap<>();
            Map<Integer, ItemStack> contents = new HashMap<>();
            if (plugin.getMenus().getConfig().isSet("menus." + name + ".items")) {
                ConfigurationSection conf = plugin.getMenus().getConfig().getConfigurationSection("menus." + name + ".items");
                for (String c : conf.getKeys(false)) {
                    int slot = Integer.parseInt(c);
                    ItemStack litem = plugin.getMenus().getConfig().getItemStack("menus." + name + ".items." + c);
                    ItemStack item = ItemBuilder.parse(plugin.getMenus().getConfig().getItemStack("menus." + name + ".items." + c).clone(),
                            new String[]{"{KITSOLO}", plugin.getLang().get("menus.kitsperks.kitsolo.nameItem"), plugin.getLang().get("menus.kitsperks.kitsolo.loreItem")},
                            new String[]{"{KITTEAM}", plugin.getLang().get("menus.kitsperks.kitteam.nameItem"), plugin.getLang().get("menus.kitsperks.kitteam.loreItem")},
                            new String[]{"{KITRANKED}", plugin.getLang().get("menus.kitsperks.kitranked.nameItem"), plugin.getLang().get("menus.kitsperks.kitranked.loreItem")},
                            new String[]{"{PERKSOLO}", plugin.getLang().get("menus.kitsperks.perksolo.nameItem"), plugin.getLang().get("menus.kitsperks.perksolo.loreItem")},
                            new String[]{"{PERKTEAM}", plugin.getLang().get("menus.kitsperks.perkteam.nameItem"), plugin.getLang().get("menus.kitsperks.perkteam.loreItem")},
                            new String[]{"{PERKRANKED}", plugin.getLang().get("menus.kitsperks.perkranked.nameItem"), plugin.getLang().get("menus.kitsperks.perkranked.loreItem")},
                            new String[]{"{KITCLOSE}", plugin.getLang().get("menus.kitsperks.close.nameItem"), plugin.getLang().get("menus.kitsperks.close.loreItem")});
                    contents.put(slot, item);
                    config.put(slot, litem);
                }
                this.contents = contents;
                this.config = config;
            }
        }
    }

}