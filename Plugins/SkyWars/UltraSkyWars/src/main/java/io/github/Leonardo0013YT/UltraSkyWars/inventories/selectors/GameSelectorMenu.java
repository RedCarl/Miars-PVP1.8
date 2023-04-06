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

public class GameSelectorMenu extends UltraInventory {

    private HashMap<String, Integer> slots = new HashMap<>();
    private ArrayList<Integer> extra = new ArrayList<>();
    private ArrayList<Integer> gameSlots = new ArrayList<>();
    private String type;

    public GameSelectorMenu(UltraSkyWars plugin, String name, String type) {
        super(name);
        this.type = type;
        this.title = plugin.getLang().get("menus.selector.title").replaceAll("<type>", plugin.getLang().get("selector." + type));
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
                            new String[]{"{RANDOM}", plugin.getLang().get("menus.selector.random.nameItem"), plugin.getLang().get("menus.selector.random.loreItem").replaceAll("<type>", plugin.getLang().get("selector." + type))},
                            new String[]{"{FAVORITES}", plugin.getLang().get("menus.selector.favorites.nameItem"), plugin.getLang().get("menus.selector.favorites.loreItem").replaceAll("<type>", plugin.getLang().get("selector." + type))},
                            new String[]{"{NEXT}", plugin.getLang().get("menus.next.nameItem"), plugin.getLang().get("menus.next.loreItem")},
                            new String[]{"{CLOSE}", plugin.getLang().get("menus.close.nameItem"), plugin.getLang().get("menus.close.loreItem")},
                            new String[]{"{LAST}", plugin.getLang().get("menus.last.nameItem"), plugin.getLang().get("menus.last.loreItem")},
                            new String[]{"{GAMESLOT}", "", ""});
                    if (selected.get().equals("NONE")) {
                        extra.add(slot);
                    } else if (selected.get().equals("{GAMESLOT}")) {
                        gameSlots.add(slot);
                    } else {
                        slots.put(selected.get(), slot);
                    }
                    contents.put(slot, item);
                    config.put(slot, litem);
                }
                this.contents = contents;
                this.config = config;
            }
        }
    }

    public ArrayList<Integer> getGameSlots() {
        return gameSlots;
    }

    public int getSlot(String name) {
        return slots.getOrDefault(name, -1);
    }

}