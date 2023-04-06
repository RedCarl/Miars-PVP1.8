package io.github.Leonardo0013YT.UltraSkyWars.inventories.votes;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.enums.HealthType;
import io.github.Leonardo0013YT.UltraSkyWars.superclass.Game;
import io.github.Leonardo0013YT.UltraSkyWars.superclass.UltraInventory;
import io.github.Leonardo0013YT.UltraSkyWars.utils.ItemBuilder;
import io.github.Leonardo0013YT.UltraSkyWars.vote.Vote;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;

public class VoteHealthMenu extends UltraInventory {

    public VoteHealthMenu(UltraSkyWars plugin, String name) {
        super(name);
        this.title = plugin.getLang().get(null, "menus." + name + ".title");
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
            if (display.equals(plugin.getLang().get(p, "menus.health.health5.nameItem"))) {
                if (!p.hasPermission("ultraskywars.votes.health.*") && !p.hasPermission("ultraskywars.votes.health.health5")) {
                    p.sendMessage(plugin.getLang().get(p, "messages.noPermission"));
                    return;
                }
                Vote.VotePlayer vp = vote.getVotePlayer(p);
                if (vp.getHealthType() != null) {
                    if (vp.getHealthType().equals(HealthType.HEALTH5)) {
                        p.sendMessage(plugin.getLang().get(p, "messages.alreadyVoted").replaceAll("<type>", plugin.getLang().get(p, "votes.health.health5")));
                        return;
                    }
                }
                vp.setHealthType(HealthType.HEALTH5);
                g.sendGameMessage(plugin.getLang().get(p, "messages.voted").replaceAll("<player>", p.getName()).replaceAll("<category>", plugin.getLang().get(p, "votes.health.name")).replaceAll("<type>", plugin.getLang().get(p, "votes.health.health5")).replaceAll("<votes>", String.valueOf(vote.getVotes("HEALTH5"))));
                plugin.getUim().openInventory(p, plugin.getUim().getMenus("health"),
                        new String[]{"<health5>", String.valueOf(vote.getVotes("HEALTH5"))},
                        new String[]{"<health10>", String.valueOf(vote.getVotes("HEALTH10"))},
                        new String[]{"<health20>", String.valueOf(vote.getVotes("HEALTH20"))},
                        new String[]{"<healthuhc>", String.valueOf(vote.getVotes("UHC"))});
            }
            if (display.equals(plugin.getLang().get(p, "menus.health.health10.nameItem"))) {
                if (!p.hasPermission("ultraskywars.votes.health.*") && !p.hasPermission("ultraskywars.votes.health.health10")) {
                    p.sendMessage(plugin.getLang().get(p, "messages.noPermission"));
                    return;
                }
                Vote.VotePlayer vp = vote.getVotePlayer(p);
                if (vp.getHealthType() != null) {
                    if (vp.getHealthType().equals(HealthType.HEALTH10)) {
                        p.sendMessage(plugin.getLang().get(p, "messages.alreadyVoted").replaceAll("<type>", plugin.getLang().get(p, "votes.health.health10")));
                        return;
                    }
                }
                vp.setHealthType(HealthType.HEALTH10);
                g.sendGameMessage(plugin.getLang().get(p, "messages.voted").replaceAll("<player>", p.getName()).replaceAll("<category>", plugin.getLang().get(p, "votes.health.name")).replaceAll("<type>", plugin.getLang().get(p, "votes.health.health10")).replaceAll("<votes>", String.valueOf(vote.getVotes("HEALTH10"))));
                plugin.getUim().openInventory(p, plugin.getUim().getMenus("health"),
                        new String[]{"<health5>", String.valueOf(vote.getVotes("HEALTH5"))},
                        new String[]{"<health10>", String.valueOf(vote.getVotes("HEALTH10"))},
                        new String[]{"<health20>", String.valueOf(vote.getVotes("HEALTH20"))},
                        new String[]{"<healthuhc>", String.valueOf(vote.getVotes("UHC"))});
            }
            if (display.equals(plugin.getLang().get(p, "menus.health.health20.nameItem"))) {
                if (!p.hasPermission("ultraskywars.votes.health.*") && !p.hasPermission("ultraskywars.votes.health.health20")) {
                    p.sendMessage(plugin.getLang().get(p, "messages.noPermission"));
                    return;
                }
                Vote.VotePlayer vp = vote.getVotePlayer(p);
                if (vp.getHealthType() != null) {
                    if (vp.getHealthType().equals(HealthType.HEALTH20)) {
                        p.sendMessage(plugin.getLang().get(p, "messages.alreadyVoted").replaceAll("<type>", plugin.getLang().get(p, "votes.health.health20")));
                        return;
                    }
                }
                vp.setHealthType(HealthType.HEALTH20);
                g.sendGameMessage(plugin.getLang().get(p, "messages.voted").replaceAll("<player>", p.getName()).replaceAll("<category>", plugin.getLang().get(p, "votes.health.name")).replaceAll("<type>", plugin.getLang().get(p, "votes.health.health20")).replaceAll("<votes>", String.valueOf(vote.getVotes("HEALTH20"))));
                plugin.getUim().openInventory(p, plugin.getUim().getMenus("health"),
                        new String[]{"<health5>", String.valueOf(vote.getVotes("HEALTH5"))},
                        new String[]{"<health10>", String.valueOf(vote.getVotes("HEALTH10"))},
                        new String[]{"<health20>", String.valueOf(vote.getVotes("HEALTH20"))},
                        new String[]{"<healthuhc>", String.valueOf(vote.getVotes("UHC"))});
            }
            if (display.equals(plugin.getLang().get(p, "menus.health.uhc.nameItem"))) {
                if (!p.hasPermission("ultraskywars.votes.health.*") && !p.hasPermission("ultraskywars.votes.health.uhc")) {
                    p.sendMessage(plugin.getLang().get(p, "messages.noPermission"));
                    return;
                }
                Vote.VotePlayer vp = vote.getVotePlayer(p);
                if (vp.getHealthType() != null) {
                    if (vp.getHealthType().equals(HealthType.UHC)) {
                        p.sendMessage(plugin.getLang().get(p, "messages.alreadyVoted").replaceAll("<type>", plugin.getLang().get(p, "votes.health.uhc")));
                        return;
                    }
                }
                vp.setHealthType(HealthType.UHC);
                g.sendGameMessage(plugin.getLang().get(p, "messages.voted").replaceAll("<player>", p.getName()).replaceAll("<category>", plugin.getLang().get(p, "votes.health.name")).replaceAll("<type>", plugin.getLang().get(p, "votes.health.uhc")).replaceAll("<votes>", String.valueOf(vote.getVotes("UHC"))));
                plugin.getUim().openInventory(p, plugin.getUim().getMenus("health"),
                        new String[]{"<health5>", String.valueOf(vote.getVotes("HEALTH5"))},
                        new String[]{"<health10>", String.valueOf(vote.getVotes("HEALTH10"))},
                        new String[]{"<health20>", String.valueOf(vote.getVotes("HEALTH20"))},
                        new String[]{"<healthuhc>", String.valueOf(vote.getVotes("UHC"))});
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
                    ItemStack item = ItemBuilder.parse(plugin.getMenus().getConfig().getItemStack("menus." + name + ".items." + c).clone(),
                            new String[]{"{HEALTH5}", plugin.getLang().get(null, "menus.health.health5.nameItem"), plugin.getLang().get(null, "menus.health.health5.loreItem")},
                            new String[]{"{HEALTH10}", plugin.getLang().get(null, "menus.health.health10.nameItem"), plugin.getLang().get(null, "menus.health.health10.loreItem")},
                            new String[]{"{HEALTH20}", plugin.getLang().get(null, "menus.health.health20.nameItem"), plugin.getLang().get(null, "menus.health.health20.loreItem")},
                            new String[]{"{UHC}", plugin.getLang().get(null, "menus.health.uhc.nameItem"), plugin.getLang().get(null, "menus.health.uhc.loreItem")});
                    contents.put(slot, item);
                    config.put(slot, litem);
                }
                this.contents = contents;
                this.config = config;
            }
        }
    }

}