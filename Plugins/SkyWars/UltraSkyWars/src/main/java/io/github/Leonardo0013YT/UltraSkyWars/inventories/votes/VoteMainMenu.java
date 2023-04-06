package io.github.Leonardo0013YT.UltraSkyWars.inventories.votes;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.chests.ChestType;
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

public class VoteMainMenu extends UltraInventory {

    public VoteMainMenu(UltraSkyWars plugin, String name) {
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
            if (display.equals(plugin.getLang().get(p, "menus.votes.chest.nameItem"))) {
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
            if (display.equals(plugin.getLang().get(p, "menus.votes.final.nameItem"))) {
                plugin.getUim().openInventory(p, plugin.getUim().getMenus("final"),
                        new String[]{"<dragonfinal>", String.valueOf(vote.getVotes("DRAGON"))},
                        new String[]{"<borderfinal>", String.valueOf(vote.getVotes("BORDER"))},
                        new String[]{"<tntfinal>", String.valueOf(vote.getVotes("TNT"))},
                        new String[]{"<zombiesfinal>", String.valueOf(vote.getVotes("ZOMBIES"))},
                        new String[]{"<witherfinal>", String.valueOf(vote.getVotes("WITHER"))},
                        new String[]{"<nonefinal>", String.valueOf(vote.getVotes("NONE"))});
            }
            if (display.equals(plugin.getLang().get(p, "menus.votes.health.nameItem"))) {
                plugin.getUim().openInventory(p, plugin.getUim().getMenus("health"),
                        new String[]{"<health5>", String.valueOf(vote.getVotes("HEALTH5"))},
                        new String[]{"<health10>", String.valueOf(vote.getVotes("HEALTH10"))},
                        new String[]{"<health20>", String.valueOf(vote.getVotes("HEALTH20"))},
                        new String[]{"<healthuhc>", String.valueOf(vote.getVotes("UHC"))});
            }
            if (display.equals(plugin.getLang().get(p, "menus.votes.projectile.nameItem"))) {
                plugin.getUim().openInventory(p, plugin.getUim().getMenus("projectile"),
                        new String[]{"<noproj>", String.valueOf(vote.getVotes("NOPROJ"))},
                        new String[]{"<yesproj>", String.valueOf(vote.getVotes("YESPROJ"))},
                        new String[]{"<exproj>", String.valueOf(vote.getVotes("EXPROJ"))},
                        new String[]{"<desproj>", String.valueOf(vote.getVotes("DESPROJ"))},
                        new String[]{"<teleproj>", String.valueOf(vote.getVotes("TELEPROJ"))});
            }
            if (display.equals(plugin.getLang().get(p, "menus.votes.time.nameItem"))) {
                plugin.getUim().openInventory(p, plugin.getUim().getMenus("time"),
                        new String[]{"<dawntime>", String.valueOf(vote.getVotes("DAWN"))},
                        new String[]{"<daytime>", String.valueOf(vote.getVotes("DAY"))},
                        new String[]{"<afternoontime>", String.valueOf(vote.getVotes("AFTERNOON"))},
                        new String[]{"<nighttime>", String.valueOf(vote.getVotes("NIGHT"))});
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
                            new String[]{"{MAINCHEST}", plugin.getLang().get(null, "menus.votes.chest.nameItem"), plugin.getLang().get(null, "menus.votes.chest.loreItem")},
                            new String[]{"{MAINFINAL}", plugin.getLang().get(null, "menus.votes.final.nameItem"), plugin.getLang().get(null, "menus.votes.final.loreItem")},
                            new String[]{"{MAINHEALTH}", plugin.getLang().get(null, "menus.votes.health.nameItem"), plugin.getLang().get(null, "menus.votes.health.loreItem")},
                            new String[]{"{MAINPROJECTILE}", plugin.getLang().get(null, "menus.votes.projectile.nameItem"), plugin.getLang().get(null, "menus.votes.projectile.loreItem")},
                            new String[]{"{MAINTIME}", plugin.getLang().get(null, "menus.votes.time.nameItem"), plugin.getLang().get(null, "menus.votes.time.loreItem")});
                    contents.put(slot, item);
                    config.put(slot, litem);
                }
                this.contents = contents;
                this.config = config;
            }
        }
    }

}