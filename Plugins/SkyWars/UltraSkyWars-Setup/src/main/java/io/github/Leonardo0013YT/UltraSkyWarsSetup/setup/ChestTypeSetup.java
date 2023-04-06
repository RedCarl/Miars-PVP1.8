package io.github.Leonardo0013YT.UltraSkyWarsSetup.setup;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.utils.ItemBuilder;
import org.bukkit.Material;

public class ChestTypeSetup {

    private String name, key, edit;
    private int slotSetup, slotVotes;
    private boolean refillChange, armorAllTeams;

    public ChestTypeSetup(String name) {
        this.name = name;
        this.key = name.toLowerCase();
        this.edit = "{CHEST" + name.toUpperCase() + "}";
        this.slotSetup = 12;
        this.slotVotes = 12;
        this.refillChange = false;
    }

    public void save() {
        UltraSkyWars plugin = UltraSkyWars.get();
        plugin.getChestType().set("types." + key + ".key", key);
        plugin.getChestType().set("types." + key + ".edit", edit);
        plugin.getChestType().set("types." + key + ".slots.setup", slotSetup);
        plugin.getChestType().set("types." + key + ".slots.votes", slotVotes);
        plugin.getChestType().set("types." + key + ".refillChange", refillChange);
        plugin.getChestType().set("types." + key + ".armorAllTeams", armorAllTeams);
        plugin.getChestType().set("votes.chest." + key, name);
        plugin.getChestType().set("lang.chest." + key + ".nameItem", "&7" + name + " Chest");
        plugin.getChestType().set("lang.chest." + key + ".loreItem", "&7The chests will contain\n&7very simple things.\n&7\n&eVotes: &b<" + key + "Chest>\n&7\n&eClick to vote!");
        plugin.getChestType().set("lang.chests." + key + ".nameItem", "&7" + name + " Chest");
        plugin.getChestType().set("lang.chests." + key + ".loreItem", "&7Click to open menu to\n&7setup Basic Chest items.\n&7\n&eClick to open!");
        plugin.getChestType().set("menus.chests.items." + slotSetup, ItemBuilder.item(Material.CHAINMAIL_CHESTPLATE, 1, edit, ""));
        plugin.getChestType().set("menus.chest.items." + slotVotes, ItemBuilder.item(Material.CHAINMAIL_CHESTPLATE, 1, edit, ""));
        plugin.getChestType().save();
        plugin.getCtm().reload();
    }

    public void setArmorAllTeams(boolean armorAllTeams) {
        this.armorAllTeams = armorAllTeams;
    }

    public boolean isArmorAllTeams() {
        return armorAllTeams;
    }

    public void setRefillChange(boolean refillChange) {
        this.refillChange = refillChange;
    }

    public boolean isRefillChange() {
        return refillChange;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getEdit() {
        return edit;
    }

    public void setEdit(String edit) {
        this.edit = edit;
    }

    public int getSlotSetup() {
        return slotSetup;
    }

    public void setSlotSetup(int slotSetup) {
        this.slotSetup = slotSetup;
    }

    public int getSlotVotes() {
        return slotVotes;
    }

    public void setSlotVotes(int slotVotes) {
        this.slotVotes = slotVotes;
    }
}