package io.github.Leonardo0013YT.UltraSkyWars.inventories.selectors;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.superclass.UltraInventory;
import io.github.Leonardo0013YT.UltraSkyWars.utils.ItemBuilder;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class PartingSelectorMenu extends UltraInventory {

    private HashMap<String, Integer> slots = new HashMap<>();
    private ArrayList<Integer> extra = new ArrayList<>();

    public PartingSelectorMenu(UltraSkyWars plugin, String name) {
        super(name);
        this.title = plugin.getLang().get("menus.partingselector.title");
        reload();
    }

    public ArrayList<Integer> getExtra() {
        return extra;
    }

    public HashMap<String, Integer> getSlots() {
        return slots;
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
                    AtomicReference<String> selected = new AtomicReference<>("NONE");
                    ItemStack item = ItemBuilder.parse(plugin.getMenus().getConfig().getItemStack("menus." + name + ".items." + c).clone(), selected::set,
                            new String[]{"{SELECTED}", plugin.getLang().get("menus.partingselector.parting.nameItem"), plugin.getLang().get("menus.partingselector.parting.loreItem")},
                            new String[]{"{LAST}", plugin.getLang().get("menus.last.nameItem"), plugin.getLang().get("menus.last.loreItem")},
                            new String[]{"{NEXT}", plugin.getLang().get("menus.next.nameItem"), plugin.getLang().get("menus.next.loreItem")},
                            new String[]{"{CLOSE}", plugin.getLang().get("menus.partingselector.close.nameItem"), plugin.getLang().get("menus.partingselector.close.loreItem")},
                            new String[]{"{DESELECT}", plugin.getLang().get("menus.partingselector.deselect.nameItem"), plugin.getLang().get("menus.partingselector.deselect.loreItem")});
                    contents.put(slot, item);
                    if (selected.get().equals("NONE")) {
                        extra.add(slot);
                    } else {
                        slots.put(selected.get(), slot);
                    }
                    config.put(slot, litem);
                }
                this.contents = contents;
                this.config = config;
            }
        }
    }

    public int getSlot(String name) {
        return slots.getOrDefault(name, -1);
    }

}