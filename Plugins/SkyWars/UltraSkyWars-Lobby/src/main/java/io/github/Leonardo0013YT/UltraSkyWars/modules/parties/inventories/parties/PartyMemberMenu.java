package io.github.Leonardo0013YT.UltraSkyWars.modules.parties.inventories.parties;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.modules.parties.InjectionParty;
import io.github.Leonardo0013YT.UltraSkyWars.superclass.UltraInventory;
import io.github.Leonardo0013YT.UltraSkyWars.utils.ItemBuilder;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class PartyMemberMenu extends UltraInventory {

    private final InjectionParty ip;
    private HashMap<String, Integer> slots = new HashMap<>();
    private ArrayList<Integer> extra = new ArrayList<>();
    private ArrayList<Integer> gameSlots = new ArrayList<>();

    public PartyMemberMenu(InjectionParty ip, String name) {
        super(name);
        this.ip = ip;
        this.title = ip.getParties().get("menus.members.title");
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
                    AtomicReference<String> selected = new AtomicReference<>("NONE");
                    ItemStack item = ItemBuilder.parse(plugin.getMenus().getConfig().getItemStack("menus." + name + ".items." + c).clone(), selected::set,
                            new String[]{"{PMEMBERCLOSE}", ip.getParties().get("menus.members.close.nameItem"), ip.getParties().get("menus.members.close.loreItem")},
                            new String[]{"{PMEMBERDELETE}", ip.getParties().get("menus.members.delete.nameItem"), ip.getParties().get("menus.members.delete.loreItem")},
                            new String[]{"{MEMBERSLOT}", "", ""});
                    if (selected.get().equals("NONE")) {
                        extra.add(slot);
                    } else if (selected.get().equals("{MEMBERSLOT}")) {
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

    public ArrayList<Integer> getExtra() {
        return extra;
    }

    public ArrayList<Integer> getGameSlots() {
        return gameSlots;
    }

    public int getSlot(String name) {
        return slots.getOrDefault(name, -1);
    }

}