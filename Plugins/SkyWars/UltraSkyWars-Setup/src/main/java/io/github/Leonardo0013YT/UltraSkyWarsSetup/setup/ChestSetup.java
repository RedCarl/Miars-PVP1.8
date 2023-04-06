package io.github.Leonardo0013YT.UltraSkyWarsSetup.setup;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ChestSetup {

    private UltraSkyWars plugin;
    private String type, filter = "NONE";
    private ChestLoteSetup actual;
    private ArrayList<ItemSetup> items = new ArrayList<>();
    private ArrayList<ItemStack> temporarily = new ArrayList<>();

    public ChestSetup(UltraSkyWars plugin, String type) {
        this.plugin = plugin;
        this.type = type;
    }

    public ChestLoteSetup getActual() {
        return actual;
    }

    public void setActual(ChestLoteSetup actual) {
        this.actual = actual;
    }

    public List<ItemSetup> getItems() {
        if (filter.equals("REFILL")) {
            return items.stream().filter(ItemSetup::isRefill).collect(Collectors.toList());
        }
        if (filter.equals("SWORD")) {
            return items.stream().filter(e -> e.getItem().getType().name().endsWith("SWORD")).collect(Collectors.toList());
        }
        if (filter.equals("HELMET")) {
            return items.stream().filter(e -> e.getItem().getType().name().endsWith("HELMET")).collect(Collectors.toList());
        }
        if (filter.equals("BOOTS")) {
            return items.stream().filter(e -> e.getItem().getType().name().endsWith("BOOTS")).collect(Collectors.toList());
        }
        if (filter.equals("LEGGINGS")) {
            return items.stream().filter(e -> e.getItem().getType().name().endsWith("LEGGINGS")).collect(Collectors.toList());
        }
        if (filter.equals("CHESTPLATE")) {
            return items.stream().filter(e -> e.getItem().getType().name().endsWith("CHESTPLATE")).collect(Collectors.toList());
        }
        if (filter.equals("CENTER")) {
            return items.stream().filter(ItemSetup::isCenter).collect(Collectors.toList());
        }
        return items;
    }

    public String getType() {
        return type;
    }

    public ArrayList<ItemStack> getTemporarily() {
        return temporarily;
    }

    public void setTemporarily(ArrayList<ItemStack> temporarily) {
        this.temporarily = temporarily;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public void saveChestLote(Inventory inv) {
        Collection<ItemStack> items = new ArrayList<>();
        for (int i = 0; i < 45; i++) {
            if (inv.getItem(i) == null || inv.getItem(i).getType().equals(Material.AIR)) {
                continue;
            }
            items.add(inv.getItem(i));
        }
        items.forEach(i -> addItem(new ItemSetup(i, actual.isCenter(), actual.isRefill(), actual.getChance(), actual.getModes())));
    }

    public void saveChest(Player p) {
        plugin.getChests().set("chests." + type.toLowerCase(), null);
        for (ItemSetup is : items) {
            String path = "chests." + type.toLowerCase() + "." + is.toString();
            plugin.getChests().set(path + ".item", is.getItem());
            plugin.getChests().set(path + ".chance", is.getChance());
            plugin.getChests().set(path + ".center", is.isCenter());
            plugin.getChests().set(path + ".refill", is.isRefill());
            plugin.getChests().set(path + ".modes", is.getModes());
        }
        plugin.getChests().save();
        plugin.getCtm().reload();
        p.sendMessage(plugin.getLang().get(p, "setup.chests.save"));
    }

    public void addItem(ItemSetup item) {
        items.add(item);
    }

    public void removeItem(ItemStack item) {
        items.removeIf(i -> i.getDisplay().getItemMeta().getDisplayName().equals(item.getItemMeta().getDisplayName()));
    }

}