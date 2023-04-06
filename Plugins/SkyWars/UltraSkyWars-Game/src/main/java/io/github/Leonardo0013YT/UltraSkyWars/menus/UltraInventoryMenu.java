package io.github.Leonardo0013YT.UltraSkyWars.menus;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.cosmetics.kits.Kit;
import io.github.Leonardo0013YT.UltraSkyWars.cosmetics.kits.KitLevel;
import io.github.Leonardo0013YT.UltraSkyWars.data.SWPlayer;
import io.github.Leonardo0013YT.UltraSkyWars.enums.OrderType;
import io.github.Leonardo0013YT.UltraSkyWars.game.GamePlayer;
import io.github.Leonardo0013YT.UltraSkyWars.inventories.KitsPerksMenu;
import io.github.Leonardo0013YT.UltraSkyWars.inventories.SpectateOptionsMenu;
import io.github.Leonardo0013YT.UltraSkyWars.inventories.SpectatePlayersMenu;
import io.github.Leonardo0013YT.UltraSkyWars.inventories.actions.InventoryAction;
import io.github.Leonardo0013YT.UltraSkyWars.inventories.selectors.GameSelectorMenu;
import io.github.Leonardo0013YT.UltraSkyWars.inventories.votes.*;
import io.github.Leonardo0013YT.UltraSkyWars.superclass.Game;
import io.github.Leonardo0013YT.UltraSkyWars.superclass.UltraInventory;
import io.github.Leonardo0013YT.UltraSkyWars.utils.ItemBuilder;
import io.github.Leonardo0013YT.UltraSkyWars.utils.NBTEditor;
import io.github.Leonardo0013YT.UltraSkyWars.utils.Utils;
import io.github.Leonardo0013YT.UltraSkyWars.xseries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class UltraInventoryMenu {

    private HashMap<String, UltraInventory> menus = new HashMap<>();
    private HashMap<UUID, Integer> pages = new HashMap<>();
    private HashMap<String, Consumer<InventoryAction>> actions = new HashMap<>();
    private UltraSkyWars plugin;

    public UltraInventoryMenu(UltraSkyWars plugin) {
        this.plugin = plugin;
    }

    public void loadMenus() {
        menus.clear();
        menus.put("kitsperks", new KitsPerksMenu(plugin, "kitsperks"));
        menus.put("votes", new VoteMainMenu(plugin, "votes"));
        menus.put("chest", new VoteChestMenu(plugin, "chest"));
        menus.put("final", new VoteFinalMenu(plugin, "final"));
        menus.put("health", new VoteHealthMenu(plugin, "health"));
        menus.put("projectile", new VoteProjectileMenu(plugin, "projectile"));
        menus.put("time", new VoteTimeMenu(plugin, "time"));
        menus.put("options", new SpectateOptionsMenu(plugin, "options"));
        menus.put("players", new SpectatePlayersMenu(plugin, "players"));
        menus.put("gamesoloselector", new GameSelectorMenu(plugin, "gamesoloselector", "solo"));
        menus.put("gameteamselector", new GameSelectorMenu(plugin, "gameteamselector", "team"));
        menus.put("gamerankedselector", new GameSelectorMenu(plugin, "gamerankedselector", "ranked"));
        menus.put("gameallselector", new GameSelectorMenu(plugin, "gameallselector", "all"));
    }

    public UltraInventory getMenus(String t) {
        return menus.get(t);
    }

    public HashMap<String, Consumer<InventoryAction>> getActions() {
        return actions;
    }

    public void openInventory(Player p, UltraInventory i) {
        Inventory inv = Bukkit.createInventory(null, i.getRows() * 9, i.getTitle());
        for (Map.Entry<Integer, ItemStack> entry : i.getConfig().entrySet()) {
            Integer s = entry.getKey();
            ItemStack it = entry.getValue();
            inv.setItem(s, it);
        }
        p.openInventory(inv);
    }

    public Inventory openContentInventory(Player p, UltraInventory i) {
        Inventory inv = Bukkit.createInventory(null, i.getRows() * 9, i.getTitle());
        for (Map.Entry<Integer, ItemStack> entry : i.getContents().entrySet()) {
            Integer s = entry.getKey();
            ItemStack it = entry.getValue();
            inv.setItem(s, ItemBuilder.parseVariables(p, it));
        }
        p.openInventory(inv);
        return inv;
    }

    public void openPlayersInventory(Player p, UltraInventory i, Game game, OrderType type, String[]... t) {
        Inventory inv = Bukkit.createInventory(null, i.getRows() * 9, i.getTitle());
        if (type.equals(OrderType.ALPHABETICALLY)) {
            List<String> pls = new ArrayList<>();
            game.getPlayers().forEach(player -> pls.add(player.getName()));
            for (String l : Utils.orderABC(pls)) {
                GamePlayer gp = game.getGamePlayerByName(l);
                if (gp.getP().equals(p) || gp.isDead()) continue;
                ItemStack skull = ItemBuilder.skull(XMaterial.PLAYER_HEAD, 1, "§e" + l, plugin.getLang().get(p, "menus.players.players.loreItem").replaceAll("<health>", Utils.formatDouble(gp.getP().getHealth())).replaceAll("<food>", Utils.formatDouble(gp.getP().getFoodLevel())).replaceAll("<kills>", String.valueOf(gp.getKills())), l);
                inv.addItem(skull);
            }
        } else if (type.equals(OrderType.KILLS)) {
            List<Integer> pls = new ArrayList<>();
            for (GamePlayer gp : game.getGamePlayer().values()) {
                if (gp.isDead()) continue;
                pls.add(gp.getKills());
            }
            List<GamePlayer> ready = new ArrayList<>();
            for (int l : Utils.orderDESC(pls)) {
                GamePlayer gp = game.getGamePlayerByKills(l, ready);
                ready.add(gp);
                checkEquals(p, inv, gp);
            }
        } else {
            for (GamePlayer gp : game.getGamePlayer().values()) {
                if (gp.isDead()) continue;
                checkEquals(p, inv, gp);
            }
        }
        for (Map.Entry<Integer, ItemStack> entry : i.getContents().entrySet()) {
            Integer s = entry.getKey();
            ItemStack it = entry.getValue();
            inv.setItem(s, ItemBuilder.parseVariables(p, it, t));
        }
        p.openInventory(inv);
    }

    private void checkEquals(Player p, Inventory inv, GamePlayer gp) {
        if (gp.getP().equals(p)) return;
        ItemStack skull = ItemBuilder.skull(XMaterial.PLAYER_HEAD, 1, "§e" + gp.getP().getName(), plugin.getLang().get(p, "menus.players.players.loreItem").replaceAll("<health>", Utils.formatDouble(gp.getP().getHealth())).replaceAll("<food>", Utils.formatDouble(gp.getP().getFoodLevel())).replaceAll("<kills>", String.valueOf(gp.getKills())), gp.getP().getName());
        inv.addItem(skull);
    }

    public void openInventory(Player p, UltraInventory i, String[]... t) {
        Inventory inv = Bukkit.createInventory(null, i.getRows() * 9, i.getTitle());
        for (Map.Entry<Integer, ItemStack> entry : i.getContents().entrySet()) {
            Integer s = entry.getKey();
            ItemStack it = entry.getValue().clone();
            inv.setItem(s, ItemBuilder.parseVariables(p, it, t));
        }
        p.openInventory(inv);
    }

    public void openChestInventory(Player p, UltraInventory i, String[]... t) {
        Inventory inv = Bukkit.createInventory(null, i.getRows() * 9, i.getTitle());
        for (Map.Entry<Integer, ItemStack> entry : i.getContents().entrySet()) {
            Integer s = entry.getKey();
            ItemStack it = entry.getValue().clone();
            AtomicReference<String> selected = new AtomicReference<>("");
            ItemStack now = ItemBuilder.parseChestVariables(p, it, selected::set, t);
            inv.setItem(s, NBTEditor.set(now, selected.get(), "CHEST", "VOTE", "TYPE"));
        }
        p.openInventory(inv);
    }

    public void setInventory(String inv, Inventory close) {
        if (menus.containsKey(inv)) {
            Map<Integer, ItemStack> items = new HashMap<>();
            for (int i = 0; i < close.getSize(); i++) {
                ItemStack it = close.getItem(i);
                if (it == null || it.getType().equals(Material.AIR)) {
                    continue;
                }
                items.put(i, it);
            }
            menus.get(inv).setConfig(items);
            menus.get(inv).save();
        }
    }

    public void createKitSelectorMenu(Player p, String type, boolean game) {
        int page = pages.get(p.getUniqueId());
        int size = plugin.getCm().getRInvKits() * 9;
        Inventory inv = Bukkit.createInventory(null, size, plugin.getLang().get(p, "menus.kitselector.title"));
        SWPlayer sw = plugin.getDb().getSWPlayer(p);
        ItemStack deselect = ItemBuilder.item(plugin.getCm().getDeselect(), plugin.getLang().get(p, "menus.kitselector.deselect.nameItem"), plugin.getLang().get(p, "menus.kitselector.deselect.loreItem"));
        ItemStack back = ItemBuilder.item(plugin.getCm().getBack(), plugin.getLang().get(p, "menus.back.nameItem"), plugin.getLang().get(p, "menus.back.loreItem"));
        ItemStack close = ItemBuilder.item(XMaterial.BARRIER, plugin.getLang().get(p, "menus.close.nameItem"), plugin.getLang().get(p, "menus.close.loreItem"));
        ItemStack next = ItemBuilder.item(plugin.getCm().getNext(), 1, plugin.getLang().get(p, "menus.next.nameItem"), plugin.getLang().get(p, "menus.next.loreItem"));
        ItemStack last = ItemBuilder.item(plugin.getCm().getLast(), 1, plugin.getLang().get(p, "menus.last.nameItem"), plugin.getLang().get(p, "menus.last.loreItem"));
        int kitSelected = sw.getSoloKit();
        int kitSelectedLevel = sw.getSoloKitLevel();
        if (type.equals("TEAM")) {
            kitSelected = sw.getTeamKit();
            kitSelectedLevel = sw.getTeamKitLevel();
        } else if (type.equals("RANKED")) {
            kitSelected = sw.getRankedKit();
            kitSelectedLevel = sw.getRankedKitLevel();
        }
        for (Kit k : plugin.getKm().getKits().values()) {
            if (!k.getModes().contains(type)) {
                continue;
            }
            if (k.getId() == kitSelected) {
                KitLevel kl = k.getLevels().get(kitSelectedLevel);
                if (kl == null) {
                    inv.setItem(k.getSlot(), NBTEditor.set(k.getLevels().get(1).getIcon(p), type, "KIT", "SELECTOR", "TYPE"));
                    continue;
                }
                ItemStack i = kl.getIcon(p);
                ItemStack kit = ItemBuilder.nameLore(i.clone(), plugin.getLang().get(p, "menus.kitselector.kit.nameItem"), plugin.getLang().get(p, "menus.kitselector.kit.loreItem"));
                inv.setItem(size - 4, NBTEditor.set(kit, type, "KIT", "SELECTOR", "TYPE"));
                if (k.getPage() != page) continue;
                inv.setItem(k.getSlot(), NBTEditor.set(i, type, "KIT", "SELECTOR", "TYPE"));
            } else {
                if (k.getPage() != page) continue;
                inv.setItem(k.getSlot(), NBTEditor.set(k.getLevels().get(1).getIcon(p), type, "KIT", "SELECTOR", "TYPE"));
            }
        }
        if (kitSelected != 999999) {
            inv.setItem(size - 6, NBTEditor.set(deselect, type, "KIT", "SELECTOR", "TYPE"));
        }
        if (page > 1) {
            inv.setItem(size - 9, NBTEditor.set(last, type, "KIT", "SELECTOR", "TYPE"));
        }
        if (page < plugin.getKm().getLastPage()) {
            inv.setItem(size - 1, NBTEditor.set(next, type, "KIT", "SELECTOR", "TYPE"));
        }
        if (game) {
            inv.setItem(size - 5, NBTEditor.set(close, type, "KIT", "SELECTOR", "TYPE"));
        } else {
            inv.setItem(size - 5, NBTEditor.set(back, type, "KIT", "SELECTOR", "TYPE"));
        }
        p.openInventory(inv);
    }

    public void createKitLevelSelectorMenu(Player p, Kit k, String type) {
        Inventory inv = Bukkit.createInventory(null, 54, plugin.getLang().get(p, "menus.kitlevels.title"));
        ItemStack close = ItemBuilder.item(plugin.getCm().getBack(), plugin.getLang().get(p, "menus.back.nameItem"), plugin.getLang().get(p, "menus.back.loreItem"));
        for (KitLevel kl : k.getLevels().values()) {
            ItemStack i = kl.getIcon(p);
            inv.setItem(kl.getSlot(), NBTEditor.set(i, type, "KIT", "SELECTOR", "TYPE"));
        }
        inv.setItem(49, NBTEditor.set(close, type, "KIT", "SELECTOR", "TYPE"));
        p.openInventory(inv);
    }

    public HashMap<UUID, Integer> getPages() {
        return pages;
    }

    public HashMap<String, UltraInventory> getMenus() {
        return menus;
    }

    public void addPage(Player p) {
        pages.putIfAbsent(p.getUniqueId(), 1);
        pages.put(p.getUniqueId(), pages.get(p.getUniqueId()) + 1);
    }

    public void removePage(Player p) {
        pages.put(p.getUniqueId(), pages.get(p.getUniqueId()) - 1);
    }

}