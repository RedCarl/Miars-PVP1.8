package io.github.Leonardo0013YT.UltraSkyWars.chests;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Getter
public class ChestType {

    private final SWChest chest;
    private final String key, edit, vote, editLore, voteLore, editName, voteName;
    private final int setupSlot, voteSlot;
    private final ItemStack voteItem, setupItem;
    private final boolean refillChange, armorAllTeams;

    public ChestType(UltraSkyWars plugin, String key) {
        this.key = key;
        this.edit = plugin.getChestType().get("types." + key + ".edit");
        this.setupSlot = plugin.getChestType().getInt("types." + key + ".slots.setup");
        this.voteSlot = plugin.getChestType().getInt("types." + key + ".slots.votes");
        this.vote = plugin.getChestType().get("votes.chest." + key);
        this.editName = plugin.getChestType().get("lang.chests." + key + ".nameItem");
        this.editLore = listString(plugin.getChestType().getList("lang.chests." + key + ".loreItem"));
        this.voteName = plugin.getChestType().get("lang.chest." + key + ".nameItem");
        this.voteLore = listString(plugin.getChestType().getList("lang.chest." + key + ".loreItem"));
        this.voteItem = plugin.getChestType().getConfig().getItemStack("menus.chest.items." + voteSlot);
        this.setupItem = plugin.getChestType().getConfig().getItemStack("menus.chests.items." + setupSlot);
        this.refillChange = plugin.getChestType().getBooleanOrDefault("types." + key + ".refillChange", false);
        this.armorAllTeams = plugin.getChestType().getBooleanOrDefault("types." + key + ".armorAllTeams", true);
        this.chest = new SWChest(plugin, "chests." + key);
    }

    public String listString(List<String> list) {
        return String.join("\\n", list);
    }

}