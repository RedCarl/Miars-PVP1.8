package io.github.Leonardo0013YT.UltraSkyWars.inventories;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.superclass.UltraInventory;
import io.github.Leonardo0013YT.UltraSkyWars.utils.ItemBuilder;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class SoulWellMenuRolling extends UltraInventory {

    public SoulWellMenuRolling(UltraSkyWars plugin, String name) {
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
                            new String[]{"{SOULCONFIRM}", plugin.getLang().get("menus.soulwellmenu.confirm.nameItem"), plugin.getLang().get("menus.soulwellmenu.confirm.loreItem")},
                            new String[]{"{SOULSETTINGS}", plugin.getLang().get("menus.soulwellmenu.settings.nameItem"), plugin.getLang().get("menus.soulwellmenu.settings.loreItem")},
                            new String[]{"{SOULCANCEL}", plugin.getLang().get("menus.soulwellmenu.deny.nameItem"), plugin.getLang().get("menus.soulwellmenu.deny.loreItem")});
                    contents.put(slot, item);
                    config.put(slot, litem);
                }
                this.contents = contents;
                this.config = config;
            }
        }
    }

}