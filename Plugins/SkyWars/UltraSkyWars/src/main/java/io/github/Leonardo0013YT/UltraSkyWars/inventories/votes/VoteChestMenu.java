package io.github.Leonardo0013YT.UltraSkyWars.inventories.votes;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.chests.ChestType;
import io.github.Leonardo0013YT.UltraSkyWars.superclass.Game;
import io.github.Leonardo0013YT.UltraSkyWars.superclass.UltraInventory;
import io.github.Leonardo0013YT.UltraSkyWars.utils.ItemBuilder;
import io.github.Leonardo0013YT.UltraSkyWars.utils.NBTEditor;
import io.github.Leonardo0013YT.UltraSkyWars.vote.Vote;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;

public class VoteChestMenu extends UltraInventory {

    public VoteChestMenu(UltraSkyWars plugin, String name) {
        super(name);
        this.title = plugin.getChestType().get("lang." + name + ".title");
        reload();
        plugin.getUim().getActions().put(title, (b) -> {
            InventoryClickEvent e = b.getInventoryClickEvent();
            Player p = b.getPlayer();
            if (plugin.getCm().isSetupLobby(p)) return;
            Game g = plugin.getGm().getGameByPlayer(p);
            e.setCancelled(true);
            if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR)) {
                return;
            }
            ItemStack item = e.getCurrentItem();
            if (!item.hasItemMeta()) {
                return;
            }
            if (!item.getItemMeta().hasDisplayName()) {
                return;
            }
            if (g == null) return;
            Vote vote = g.getVote();
            ItemMeta im = item.getItemMeta();
            String display = im.getDisplayName();
            String type = NBTEditor.getString(item, "CHEST", "VOTE", "TYPE");
            if (type == null) return;
            String lower = type.toLowerCase();
            if (display.equals(plugin.getChestType().get(p, "lang.chest." + lower + ".nameItem"))) {
                if (!p.hasPermission("ultraskywars.votes.chest.*") && !p.hasPermission("ultraskywars.votes.chest." + lower)) {
                    p.sendMessage(plugin.getLang().get(p, "messages.noPermission"));
                    return;
                }
                Vote.VotePlayer vp = vote.getVotePlayer(p);
                if (vp.getChestType() != null) {
                    if (vp.getChestType().equals(type)) {
                        p.sendMessage(plugin.getLang().get(p, "messages.alreadyVoted").replaceAll("<type>", plugin.getChestType().get(p, "votes.chest." + lower)));
                        return;
                    }
                }
                vp.setChestType(type);
                g.sendGameMessage(plugin.getLang().get(p, "messages.voted").replaceAll("<player>", p.getName()).replaceAll("<category>", plugin.getChestType().get(p, "votes.chest.name")).replaceAll("<type>", plugin.getChestType().get(p, "votes.chest." + lower)).replaceAll("<votes>", String.valueOf(vote.getVotes(type))));
                String[][] replacements = new String[plugin.getCtm().getChests().size()][3];
                int i = 0;
                for (ChestType ct : plugin.getCtm().getChests().values()) {
                    replacements[i][0] = "<" + ct.getKey() + "Chest>";
                    replacements[i][1] = String.valueOf(vote.getVotes(ct.getKey().toUpperCase()));
                    replacements[i][2] = ct.getKey().toUpperCase();
                    i++;
                }
                plugin.getUim().openChestInventory(p, plugin.getUim().getMenus("chest"), replacements);
            }
        });
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
                    ItemStack item = ItemBuilder.parse(plugin.getMenus().getConfig().getItemStack("menus." + name + ".items." + c).clone());
                    contents.put(slot, item);
                    config.put(slot, litem);
                }
                for (ChestType chest : plugin.getCtm().getChests().values()) {
                    int slot = chest.getVoteSlot();
                    ItemStack litem = chest.getVoteItem();
                    ItemStack item = ItemBuilder.parse(chest.getVoteItem(),
                            new String[]{chest.getEdit(), plugin.getChestType().get("lang.chest." + chest.getKey() + ".nameItem"), plugin.getChestType().get("lang.chest." + chest.getKey() + ".loreItem")});
                    contents.put(slot, item);
                    config.put(slot, litem);
                }
                this.contents = contents;
                this.config = config;
            }
        }
    }

}