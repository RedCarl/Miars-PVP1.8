package io.github.Leonardo0013YT.UltraSkyWars.superclass;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public abstract class UltraInventory {

    public String title;
    public Map<Integer, ItemStack> config = new HashMap<>();
    public Map<Integer, ItemStack> contents = new HashMap<>();
    public int rows = 6;
    public String name;

    public UltraInventory(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Map<Integer, ItemStack> getConfig() {
        return config;
    }

    public void setConfig(Map<Integer, ItemStack> config) {
        this.config = config;
    }

    public Map<Integer, ItemStack> getContents() {
        return contents;
    }

    public void setContents(Map<Integer, ItemStack> contents) {
        this.contents = contents;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void save() {
        UltraSkyWars plugin = UltraSkyWars.get();
        plugin.getMenus().set("menus." + name + ".rows", rows);
        plugin.getMenus().set("menus." + name + ".items", null);
        for (int i : config.keySet()) {
            plugin.getMenus().set("menus." + name + ".items." + i, config.get(i));
        }
        plugin.getMenus().save();
        reload();
    }

    public abstract void reload();

}