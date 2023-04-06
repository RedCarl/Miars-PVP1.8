package io.github.Leonardo0013YT.UltraSkyWars.modules.parties.inventories;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.modules.parties.InjectionParty;
import io.github.Leonardo0013YT.UltraSkyWars.superclass.UltraInventory;
import io.github.Leonardo0013YT.UltraSkyWars.utils.ItemBuilder;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class PartyMainMenu extends UltraInventory {

    private final InjectionParty ip;

    public PartyMainMenu(InjectionParty ip, String name) {
        super(name);
        this.ip = ip;
        this.title = ip.getParties().get("menus.main.title");
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
                            new String[]{"{PARTYCLOSE}", ip.getParties().get("menus.main.close.nameItem"), ip.getParties().get("menus.main.close.loreItem")},
                            new String[]{"{PARTYCREATE}", ip.getParties().get("menus.main.create.nameItem"), ip.getParties().get("menus.main.create.loreItem")},
                            new String[]{"{PARTYJOIN}", ip.getParties().get("menus.main.join.nameItem"), ip.getParties().get("menus.main.join.loreItem")});
                    contents.put(slot, item);
                    config.put(slot, litem);
                }
                this.contents = contents;
                this.config = config;
            }
        }
    }

}