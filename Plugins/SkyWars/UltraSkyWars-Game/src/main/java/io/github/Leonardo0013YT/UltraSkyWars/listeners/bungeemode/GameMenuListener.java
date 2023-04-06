package io.github.Leonardo0013YT.UltraSkyWars.listeners.bungeemode;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.cosmetics.kits.Kit;
import io.github.Leonardo0013YT.UltraSkyWars.cosmetics.kits.KitLevel;
import io.github.Leonardo0013YT.UltraSkyWars.data.SWPlayer;
import io.github.Leonardo0013YT.UltraSkyWars.enums.*;
import io.github.Leonardo0013YT.UltraSkyWars.game.GameData;
import io.github.Leonardo0013YT.UltraSkyWars.game.GamePlayer;
import io.github.Leonardo0013YT.UltraSkyWars.game.UltraGameChest;
import io.github.Leonardo0013YT.UltraSkyWars.game.UltraTeamGame;
import io.github.Leonardo0013YT.UltraSkyWars.superclass.Game;
import io.github.Leonardo0013YT.UltraSkyWars.superclass.GameEvent;
import io.github.Leonardo0013YT.UltraSkyWars.superclass.UltraInventory;
import io.github.Leonardo0013YT.UltraSkyWars.team.Team;
import io.github.Leonardo0013YT.UltraSkyWars.utils.NBTEditor;
import io.github.Leonardo0013YT.UltraSkyWars.utils.Utils;
import io.github.Leonardo0013YT.UltraSkyWars.vote.Vote;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class GameMenuListener implements Listener {

    private UltraSkyWars plugin;
    private HashMap<UUID, Location> chests = new HashMap<>();

    public GameMenuListener(UltraSkyWars plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && e.getClickedBlock().getType().name().endsWith("CHEST")) {
            UltraSkyWars plugin = UltraSkyWars.get();
            if (!plugin.getGm().isPlayerInGame(p)) {
                return;
            }
            Game game = plugin.getGm().getGameByPlayer(p);
            GameEvent ge = game.getNowEvent();
            if (ge != null) {
                if (game.isState(State.GAME) && ge.getName().equals("refill")) {
                    chests.put(p.getUniqueId(), e.getClickedBlock().getLocation());
                }
            }
        }
    }

    @EventHandler
    public void onMenu(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        Game g = plugin.getGm().getGameByPlayer(p);
        if (!e.getSlotType().equals(InventoryType.SlotType.OUTSIDE)) {
            List<ItemStack> items = new ArrayList<>();
            items.add(e.getCurrentItem());
            items.add(e.getCursor());
            items.add((e.getClick() == org.bukkit.event.inventory.ClickType.NUMBER_KEY) ? e.getWhoClicked().getInventory().getItem(e.getHotbarButton()) : e.getCurrentItem());
            for (ItemStack item : items) {
                if (plugin.getIm().getItems().contains(item)) {
                    e.setCancelled(true);
                }
            }
        }
        if (e.getView().getTitle().equals(plugin.getLang().get(p, "menus.perks.title"))) {
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
            ItemMeta im = item.getItemMeta();
            String display = im.getDisplayName();
            if (display.equals(plugin.getLang().get(p, "menus.back.nameItem"))) {
                plugin.getUim().openContentInventory(p, plugin.getUim().getMenus("kitsperks"));
            }
        }
        if (e.getView().getTitle().equals(plugin.getLang().get(p, "menus.teamselector.title"))) {
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
            Game game = plugin.getGm().getGameByPlayer(p);
            if (game == null) {
                return;
            }
            UltraTeamGame utg = (UltraTeamGame) game;
            int id = NBTEditor.getInt(item, "TEAM", "ID");
            Team team = utg.getTeamByID(id);
            if (team == null) {
                return;
            }
            if (team.getTeamSize() >= game.getTeamSize()) {
                p.sendMessage(plugin.getLang().get(p, "messages.teamFull"));
                return;
            }
            p.closeInventory();
            game.removePlayerAllTeam(p);
            game.addPlayerTeam(p, team);
            plugin.getGem().getViews().entrySet().stream().filter(c -> c.getValue().equals("teams")).filter(c -> game.getPlayers().contains(p)).forEach(u -> {
                Player v = Bukkit.getPlayer(u.getKey());
                plugin.getGem().updateTeamSelector(game, v.getOpenInventory().getTopInventory());
            });
            p.sendMessage(plugin.getLang().get(p, "messages.joinTeam").replaceAll("<id>", String.valueOf((id + 1))));
        }
        if (e.getView().getTitle().equals(plugin.getLang().get(p, "menus.selector.title").replaceAll("<type>", plugin.getLang().get(p, "selector.all"))) || e.getView().getTitle().equals(plugin.getLang().get(p, "menus.selector.title").replaceAll("<type>", plugin.getLang().get(p, "selector.normal"))) || e.getView().getTitle().equals(plugin.getLang().get(p, "menus.selector.title").replaceAll("<type>", plugin.getLang().get(p, "selector.team"))) || e.getView().getTitle().equals(plugin.getLang().get(p, "menus.selector.title").replaceAll("<type>", plugin.getLang().get(p, "selector.ranked")))) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR)) {
                return;
            }
            ItemStack item = e.getCurrentItem();
            if (!item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) {
                return;
            }
            ItemMeta im = item.getItemMeta();
            String display = im.getDisplayName();
            if (display.equals(plugin.getLang().get(p, "menus.close.nameItem"))) {
                if (e.getClick().equals(ClickType.RIGHT)) {
                    p.sendMessage(plugin.getLang().get(p, "messages.closeWithClick"));
                    return;
                }
                p.closeInventory();
                return;
            }
            String gameType = "SOLO";
            if (e.getView().getTitle().equals(plugin.getLang().get(p, "menus.selector.title").replaceAll("<type>", plugin.getLang().get(p, "selector.team")))) {
                gameType = "TEAM";
            } else if (e.getView().getTitle().equals(plugin.getLang().get(p, "menus.selector.title").replaceAll("<type>", plugin.getLang().get(p, "selector.ranked")))) {
                gameType = "RANKED";
            } else if (e.getView().getTitle().equals(plugin.getLang().get(p, "menus.selector.title").replaceAll("<type>", plugin.getLang().get(p, "selector.all")))) {
                gameType = "ALL";
            }
            if (display.equals(plugin.getLang().get(p, "menus.next.nameItem"))) {
                plugin.getUim().addPage(p);
                createMenuGames(p, gameType, plugin);
                return;
            }
            if (display.equals(plugin.getLang().get(p, "menus.last.nameItem"))) {
                plugin.getUim().removePage(p);
                createMenuGames(p, gameType, plugin);
                return;
            }
            if (display.equals(plugin.getLang().get(p, "menus.selector.random.nameItem"))) {
                boolean added = plugin.getGm().addRandomGame(p, gameType);
                if (!added) {
                    p.sendMessage(plugin.getLang().get(p, "messages.noRandom"));
                }
                return;
            }
            if (display.equals(plugin.getLang().get(p, "menus.selector.favorites.nameItem"))) {
                if (!p.hasPermission("ultraskywars.selector.favorites")) {
                    p.sendMessage(plugin.getLang().get(p, "messages.noPermission"));
                    return;
                }
                GameData game = plugin.getGm().getGameRandomFavorites(p, gameType);
                if (game != null) {
                    if (!game.getServer().equals("")) {
                        if (plugin.getIjm().isParty() && plugin.getIjm().getParty().getPam().isLeader(p)) {
                            plugin.getIjm().getParty().getPam().sendPartyServer(p.getUniqueId(), game.getServer());
                        } else {
                            plugin.sendToServer(p, game.getServer());
                        }
                    }
                } else {
                    p.sendMessage(plugin.getLang().get(p, "messages.noRandom"));
                }
                return;
            }
            String name = NBTEditor.getString(item, "ITEM_SW_JOIN_SERVER");
            GameData game = plugin.getGm().getGameData().get(name);
            if (e.getClick().equals(ClickType.RIGHT)) {
                if (!p.hasPermission("ultraskywars.selector.favorites")) {
                    p.sendMessage(plugin.getLang().get(p, "messages.noPermission"));
                    return;
                }
                SWPlayer sw = plugin.getDb().getSWPlayer(p);
                if (sw.getFavorites().contains(name)) {
                    sw.getFavorites().remove(name);
                    p.sendMessage(plugin.getLang().get(p, "messages.favoriteRemove").replaceAll("<map>", name));
                } else {
                    sw.getFavorites().add(name);
                    p.sendMessage(plugin.getLang().get(p, "messages.favoriteAdd").replaceAll("<map>", name));
                }
                createMenuGames(p, gameType, plugin);
            } else {
                if (!game.getServer().equals("")) {
                    if ((game.getState().equals("WAITING") || game.getState().equals("STARTING") || p.hasPermission("ultraskywars.mod")) && game.getPlayers() < game.getMax()) {
                        if (plugin.getIjm().isParty() && plugin.getIjm().getParty().getPam().isLeader(p)) {
                            plugin.getIjm().getParty().getPam().sendPartyServer(p.getUniqueId(), game.getServer());
                        } else {
                            plugin.sendToServer(p, game.getServer());
                        }
                    } else {
                        p.sendMessage(plugin.getLang().get(p, "messages.alreadyStart"));
                    }
                }
                p.closeInventory();
            }
        }
        if (e.getView().getTitle().equals(plugin.getLang().get(p, "menus.kitsperks.title"))) {
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
            ItemMeta im = item.getItemMeta();
            String display = im.getDisplayName();
            if (display.equals(plugin.getLang().get(p, "menus.kitsperks.kitsolo.nameItem"))) {
                plugin.getUim().getPages().put(p.getUniqueId(), 1);
                plugin.getUim().createKitSelectorMenu(p, "SOLO", true);
            }
            if (display.equals(plugin.getLang().get(p, "menus.kitsperks.kitteam.nameItem"))) {
                plugin.getUim().getPages().put(p.getUniqueId(), 1);
                plugin.getUim().createKitSelectorMenu(p, "TEAM", true);
            }
            if (display.equals(plugin.getLang().get(p, "menus.kitsperks.kitranked.nameItem"))) {
                plugin.getUim().getPages().put(p.getUniqueId(), 1);
                plugin.getUim().createKitSelectorMenu(p, "RANKED", true);
            }
            if (display.equals(plugin.getLang().get(p, "menus.kitsperks.perksolo.nameItem"))) {
                if (!plugin.getIjm().isPerksInjection()) {
                    p.sendMessage(plugin.getLang().get(p, "injections.perks"));
                    return;
                }
                plugin.getGem().createPerksMenu(p, "SOLO");
            }
            if (display.equals(plugin.getLang().get(p, "menus.kitsperks.perkteam.nameItem"))) {
                if (!plugin.getIjm().isPerksInjection()) {
                    p.sendMessage(plugin.getLang().get(p, "injections.perks"));
                    return;
                }
                plugin.getGem().createPerksMenu(p, "TEAM");
            }
            if (display.equals(plugin.getLang().get(p, "menus.kitsperks.perkranked.nameItem"))) {
                if (!plugin.getIjm().isPerksInjection()) {
                    p.sendMessage(plugin.getLang().get(p, "injections.perks"));
                    return;
                }
                plugin.getGem().createPerksMenu(p, "RANKED");
            }
            if (display.equals(plugin.getLang().get("menus.kitsperks.close.nameItem"))) {
                if (e.getClick().equals(ClickType.RIGHT)) {
                    p.sendMessage(plugin.getLang().get(p, "messages.closeWithClick"));
                    return;
                }
                p.closeInventory();
            }
        }
        if (e.getView().getTitle().equals(plugin.getLang().get(p, "menus.kitlevels.title"))) {
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
            ItemMeta im = item.getItemMeta();
            String display = im.getDisplayName();
            if (display.equals(plugin.getLang().get(p, "menus.close.nameItem"))) {
                if (e.getClick().equals(ClickType.RIGHT)) {
                    p.sendMessage(plugin.getLang().get(p, "messages.closeWithClick"));
                    return;
                }
                p.closeInventory();
                return;
            }
            String type = NBTEditor.getString(item, "KIT", "SELECTOR", "TYPE");
            if (type == null) return;
            if (display.equals(plugin.getLang().get(p, "menus.back.nameItem"))) {
                plugin.getUim().createKitSelectorMenu(p, type, true);
                return;
            }
            SWPlayer sw = plugin.getDb().getSWPlayer(p);
            Kit k = plugin.getKm().getKitByItem(p, item);
            if (k == null) {
                return;
            }
            KitLevel kl = plugin.getKm().getKitLevelByItem(k, p, item);
            if (kl == null) {
                return;
            }
            if (p.hasPermission(kl.getAutoGivePermission())) {
                if (type.equals("SOLO")) {
                    sw.setSoloKit(k.getId());
                    sw.setSoloKitLevel(kl.getLevel());
                } else if (type.equals("TEAM")) {
                    sw.setTeamKit(k.getId());
                    sw.setTeamKitLevel(kl.getLevel());
                } else {
                    sw.setRankedKit(k.getId());
                    sw.setRankedKitLevel(kl.getLevel());
                }
                p.sendMessage(plugin.getLang().get(p, "messages.select").replaceAll("<kit>", k.getName()));
                plugin.getUim().createKitSelectorMenu(p, type, true);
                return;
            }
            if (!sw.hasKitLevel(k.getId(), kl.getLevel())) {
                if (kl.needPermToBuy() && !p.hasPermission(k.getPermission())) {
                    p.sendMessage(plugin.getLang().get(p, "messages.noPermit"));
                } else {
                    plugin.getShm().buy(p, kl, k.getName());
                }
            } else {
                if (type.equals("SOLO")) {
                    sw.setSoloKit(k.getId());
                    sw.setSoloKitLevel(kl.getLevel());
                } else if (type.equals("TEAM")) {
                    sw.setTeamKit(k.getId());
                    sw.setTeamKitLevel(kl.getLevel());
                } else {
                    sw.setRankedKit(k.getId());
                    sw.setRankedKitLevel(kl.getLevel());
                }
                p.sendMessage(plugin.getLang().get(p, "messages.select").replaceAll("<kit>", k.getName()));
            }
            plugin.getUim().createKitSelectorMenu(p, type, true);
        }
        if (e.getView().getTitle().equals(plugin.getLang().get(p, "menus.preview.title"))) {
            e.setCancelled(true);
        }
        if (e.getView().getTitle().equals(plugin.getLang().get(p, "menus.kitselector.title"))) {
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
            ItemMeta im = item.getItemMeta();
            String display = im.getDisplayName();
            SWPlayer sw = plugin.getDb().getSWPlayer(p);
            String type = NBTEditor.getString(item, "KIT", "SELECTOR", "TYPE");
            if (type == null) return;
            if (display.equals(plugin.getLang().get(p, "menus.back.nameItem"))) {
                plugin.getUim().openContentInventory(p, plugin.getUim().getMenus("kitsperks"));
                return;
            }
            if (display.equals(plugin.getLang().get(p, "menus.close.nameItem"))) {
                if (e.getClick().equals(ClickType.RIGHT)) {
                    p.sendMessage(plugin.getLang().get(p, "messages.closeWithClick"));
                    return;
                }
                p.closeInventory();
                return;
            }
            if (display.equals(plugin.getLang().get(p, "menus.next.nameItem"))) {
                plugin.getUim().addPage(p);
                plugin.getUim().createKitSelectorMenu(p, type, true);
                return;
            }
            if (display.equals(plugin.getLang().get(p, "menus.last.nameItem"))) {
                plugin.getUim().removePage(p);
                plugin.getUim().createKitSelectorMenu(p, type, true);
                return;
            }
            if (display.equals(plugin.getLang().get(p, "menus.kitselector.kit.nameItem"))) {
                int kitSelected = sw.getSoloKit();
                int kitSelectedLevel = sw.getSoloKitLevel();
                if (type.equals("TEAM")) {
                    kitSelected = sw.getTeamKit();
                    kitSelectedLevel = sw.getTeamKitLevel();
                } else if (type.equals("RANKED")) {
                    kitSelected = sw.getRankedKit();
                    kitSelectedLevel = sw.getRankedKitLevel();
                }
                Kit kit = plugin.getKm().getKits().get(kitSelected);
                KitLevel kl = kit.getLevels().get(kitSelectedLevel);
                plugin.getGem().createKitsMenu(p, kl.getInv(), kl.getArmors());
                return;
            }
            if (display.equals(plugin.getLang().get(p, "menus.kitselector.deselect.nameItem"))) {
                if (type.equals("SOLO")) {
                    if (sw.getSoloKit() == 999999) {
                        p.sendMessage(plugin.getLang().get(p, "messages.noSelect"));
                        return;
                    }
                    sw.setSoloKit(999999);
                    sw.setSoloKitLevel(1);
                } else if (type.equals("TEAM")) {
                    if (sw.getTeamKit() == 999999) {
                        p.sendMessage(plugin.getLang().get(p, "messages.noSelect"));
                        return;
                    }
                    sw.setTeamKit(999999);
                    sw.setTeamKitLevel(1);
                } else {
                    if (sw.getRankedKit() == 999999) {
                        p.sendMessage(plugin.getLang().get(p, "messages.noSelect"));
                        return;
                    }
                    sw.setRankedKit(999999);
                    sw.setRankedKitLevel(1);
                }
                p.sendMessage(plugin.getLang().get(p, "messages.deselect"));
                plugin.getUim().createKitSelectorMenu(p, type, true);
                return;
            }
            Kit k = plugin.getKm().getKitByItem(p, item);
            if (k == null) {
                return;
            }
            if (plugin.getCm().isDisableKitLevels()) {
                KitLevel kl = k.getFirstLevel().getValue();
                if (kl == null) {
                    return;
                }
                if (p.hasPermission(kl.getAutoGivePermission())) {
                    if (type.equals("SOLO")) {
                        sw.setSoloKit(k.getId());
                        sw.setSoloKitLevel(kl.getLevel());
                    } else if (type.equals("TEAM")) {
                        sw.setTeamKit(k.getId());
                        sw.setTeamKitLevel(kl.getLevel());
                    } else {
                        sw.setRankedKit(k.getId());
                        sw.setRankedKitLevel(kl.getLevel());
                    }
                    p.sendMessage(plugin.getLang().get(p, "messages.select").replaceAll("<kit>", k.getName()));
                    plugin.getUim().createKitSelectorMenu(p, type, true);
                    return;
                }
                if (!sw.hasKitLevel(k.getId(), kl.getLevel())) {
                    if (kl.needPermToBuy() && !p.hasPermission(k.getPermission())) {
                        p.sendMessage(plugin.getLang().get(p, "messages.noPermit"));
                    } else {
                        plugin.getShm().buy(p, kl, k.getName());
                    }
                } else {
                    if (type.equals("SOLO")) {
                        sw.setSoloKit(k.getId());
                        sw.setSoloKitLevel(kl.getLevel());
                    } else if (type.equals("TEAM")) {
                        sw.setTeamKit(k.getId());
                        sw.setTeamKitLevel(kl.getLevel());
                    } else {
                        sw.setRankedKit(k.getId());
                        sw.setRankedKitLevel(kl.getLevel());
                    }
                    p.sendMessage(plugin.getLang().get(p, "messages.select").replaceAll("<kit>", k.getName()));
                }
            } else {
                plugin.getUim().createKitLevelSelectorMenu(p, k, type);
            }
        }
        if (g == null) {
            return;
        }
        UltraInventory sp = plugin.getUim().getMenus("players");
        UltraInventory so = plugin.getUim().getMenus("options");
        UltraInventory v = plugin.getUim().getMenus("votes");
        UltraInventory ch = plugin.getUim().getMenus("chest");
        UltraInventory fi = plugin.getUim().getMenus("final");
        UltraInventory he = plugin.getUim().getMenus("health");
        UltraInventory pr = plugin.getUim().getMenus("projectile");
        UltraInventory ti = plugin.getUim().getMenus("time");
        if (e.getView().getTitle().equals(so.getTitle())) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR)) {
                return;
            }
            if (!so.getContents().containsKey(e.getSlot())) {
                return;
            }
            ItemStack item = so.getContents().get(e.getSlot());
            if (!item.hasItemMeta()) {
                return;
            }
            if (!item.getItemMeta().hasDisplayName()) {
                return;
            }
            Game game = plugin.getGm().getGameByPlayer(p);
            SWPlayer sw = plugin.getDb().getSWPlayer(p);
            ItemMeta im = item.getItemMeta();
            String display = im.getDisplayName();
            if (display.equals(plugin.getLang().get(p, "menus.options.speedI.nameItem"))) {
                sw.setSpeed(0.2F);
                game.updateSpectatorOptions(p);
                p.sendMessage(plugin.getLang().get(p, "messages.setSpeed").replaceAll("<speed>", "I"));
                p.closeInventory();
            }
            if (display.equals(plugin.getLang().get(p, "menus.options.speedII.nameItem"))) {
                sw.setSpeed(0.4F);
                game.updateSpectatorOptions(p);
                p.sendMessage(plugin.getLang().get(p, "messages.setSpeed").replaceAll("<speed>", "II"));
                p.closeInventory();
            }
            if (display.equals(plugin.getLang().get(p, "menus.options.speedIII.nameItem"))) {
                sw.setSpeed(0.6F);
                game.updateSpectatorOptions(p);
                p.sendMessage(plugin.getLang().get(p, "messages.setSpeed").replaceAll("<speed>", "III"));
                p.closeInventory();
            }
            if (display.equals(plugin.getLang().get(p, "menus.options.speedIV.nameItem"))) {
                sw.setSpeed(0.8F);
                game.updateSpectatorOptions(p);
                p.sendMessage(plugin.getLang().get(p, "messages.setSpeed").replaceAll("<speed>", "IV"));
                p.closeInventory();
            }
            if (display.equals(plugin.getLang().get(p, "menus.options.speedV.nameItem"))) {
                sw.setSpeed(1.0F);
                game.updateSpectatorOptions(p);
                p.sendMessage(plugin.getLang().get(p, "messages.setSpeed").replaceAll("<speed>", "V"));
                p.closeInventory();
            }
            if (display.equals(plugin.getLang().get(p, "menus.options.nightvision.nameItem"))) {
                if (!sw.isNightVision()) {
                    sw.setNightVision(true);
                    p.sendMessage(plugin.getLang().get(p, "messages.setNightVision").replaceAll("<state>", plugin.getLang().get(p, "activated")));
                } else {
                    sw.setNightVision(false);
                    p.sendMessage(plugin.getLang().get(p, "messages.setNightVision").replaceAll("<state>", plugin.getLang().get(p, "deactivated")));
                }
                game.updateSpectatorOptions(p);
            }
            if (display.equals(plugin.getLang().get(p, "menus.options.spects.nameItem"))) {
                if (!sw.isSpectatorsView()) {
                    sw.setSpectatorsView(true);
                    p.sendMessage(plugin.getLang().get(p, "messages.setSpectator").replaceAll("<state>", plugin.getLang().get(p, "activated")));
                } else {
                    sw.setSpectatorsView(false);
                    p.sendMessage(plugin.getLang().get(p, "messages.setSpectator").replaceAll("<state>", plugin.getLang().get(p, "deactivated")));
                }
                game.updateSpectatorOptions(p);
            }
            if (display.equals(plugin.getLang().get(p, "menus.options.first.nameItem"))) {
                if (!sw.isFirstPerson()) {
                    sw.setFirstPerson(true);
                    p.sendMessage(plugin.getLang().get(p, "messages.setFirstPerson").replaceAll("<state>", plugin.getLang().get(p, "activated")));
                } else {
                    sw.setFirstPerson(false);
                    p.sendMessage(plugin.getLang().get(p, "messages.setFirstPerson").replaceAll("<state>", plugin.getLang().get(p, "deactivated")));
                }
                game.updateSpectatorOptions(p);
            }
        }
        if (e.getView().getTitle().equals(sp.getTitle())) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR)) {
                return;
            }
            ItemStack it = e.getCurrentItem();
            if (!it.hasItemMeta()) {
                return;
            }
            if (!it.getItemMeta().hasDisplayName()) {
                return;
            }
            ItemMeta im = it.getItemMeta();
            String display = im.getDisplayName();
            if (sp.getContents().containsKey(e.getSlot())) {
                ItemStack item = sp.getContents().get(e.getSlot());
                if (!item.hasItemMeta()) {
                    return;
                }
                if (!item.getItemMeta().hasDisplayName()) {
                    return;
                }
                ItemMeta im2 = item.getItemMeta();
                String display2 = im2.getDisplayName();
                if (display2.equals(plugin.getLang().get(p, "menus.players.close.nameItem"))) {
                    if (e.getClick().equals(ClickType.RIGHT)) {
                        p.sendMessage(plugin.getLang().get(p, "messages.closeWithClick"));
                        return;
                    }
                    p.closeInventory();
                    return;
                }
                if (display2.equals(plugin.getLang().get(p, "menus.players.order.nameItem"))) {
                    GamePlayer gp = g.getGamePlayer().get(p.getUniqueId());
                    if (gp.getOrderType().equals(OrderType.NONE) || gp.getOrderType().equals(OrderType.PLAYERS)) {
                        gp.setOrderType(OrderType.ALPHABETICALLY);
                    } else if (gp.getOrderType().equals(OrderType.ALPHABETICALLY)) {
                        gp.setOrderType(OrderType.KILLS);
                    } else {
                        gp.setOrderType(OrderType.NONE);
                    }
                    plugin.getUim().openPlayersInventory(p, plugin.getUim().getMenus("players"), g, gp.getOrderType(), new String[]{"<order>", plugin.getLang().get(p, "order." + gp.getOrderType().name())});
                    return;
                }
                return;
            }
            String name = display.replaceFirst("§e", "");
            Player on = Bukkit.getPlayer(name);
            if (on == null) {
                return;
            }
            p.teleport(on);
            p.closeInventory();
            p.sendMessage(plugin.getLang().get(p, "messages.teleported").replaceAll("<player>", on.getName()));
        }
        if (e.getView().getTitle().equals(pr.getTitle())) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR)) {
                return;
            }
            if (!pr.getContents().containsKey(e.getSlot())) {
                return;
            }
            ItemStack item = pr.getContents().get(e.getSlot());
            if (!item.hasItemMeta()) {
                return;
            }
            if (!item.getItemMeta().hasDisplayName()) {
                return;
            }
            Vote vote = g.getVote();
            ItemMeta im = item.getItemMeta();
            String display = im.getDisplayName();
            if (display.equals(plugin.getLang().get(p, "menus.projectile.noProj.nameItem"))) {
                if (!p.hasPermission("ultraskywars.votes.projectile.*") && !p.hasPermission("ultraskywars.votes.projectile.noproj")) {
                    p.sendMessage(plugin.getLang().get(p, "messages.noPermission"));
                    return;
                }
                Vote.VotePlayer vp = vote.getVotePlayer(p);
                if (vp.getProjectileType() != null) {
                    if (vp.getProjectileType().equals(ProjectileType.NOPROJ)) {
                        p.sendMessage(plugin.getLang().get(p, "messages.alreadyVoted").replaceAll("<type>", plugin.getLang().get(p, "votes.projectile.noproj")));
                        return;
                    }
                }
                vp.setProjectileType(ProjectileType.NOPROJ);
                g.sendGameMessage(plugin.getLang().get(p, "messages.voted").replaceAll("<player>", p.getName()).replaceAll("<category>", plugin.getLang().get(p, "votes.projectile.name")).replaceAll("<type>", plugin.getLang().get(p, "votes.projectile.noproj")).replaceAll("<votes>", String.valueOf(vote.getVotes("NOPROJ"))));
                plugin.getUim().openInventory(p, plugin.getUim().getMenus("projectile"),
                        new String[]{"<noproj>", String.valueOf(vote.getVotes("NOPROJ"))},
                        new String[]{"<yesproj>", String.valueOf(vote.getVotes("YESPROJ"))},
                        new String[]{"<exproj>", String.valueOf(vote.getVotes("EXPROJ"))},
                        new String[]{"<desproj>", String.valueOf(vote.getVotes("DESPROJ"))},
                        new String[]{"<teleproj>", String.valueOf(vote.getVotes("TELEPROJ"))});
            }
            if (display.equals(plugin.getLang().get(p, "menus.projectile.siProj.nameItem"))) {
                if (!p.hasPermission("ultraskywars.votes.projectile.*") && !p.hasPermission("ultraskywars.votes.projectile.siproj")) {
                    p.sendMessage(plugin.getLang().get(p, "messages.noPermission"));
                    return;
                }
                Vote.VotePlayer vp = vote.getVotePlayer(p);
                if (vp.getProjectileType() != null) {
                    if (vp.getProjectileType().equals(ProjectileType.YESPROJ)) {
                        p.sendMessage(plugin.getLang().get(p, "messages.alreadyVoted").replaceAll("<type>", plugin.getLang().get(p, "votes.projectile.yesproj")));
                        return;
                    }
                }
                vp.setProjectileType(ProjectileType.YESPROJ);
                g.sendGameMessage(plugin.getLang().get(p, "messages.voted").replaceAll("<player>", p.getName()).replaceAll("<category>", plugin.getLang().get(p, "votes.projectile.name")).replaceAll("<type>", plugin.getLang().get(p, "votes.projectile.yesproj")).replaceAll("<votes>", String.valueOf(vote.getVotes("YESPROJ"))));
                plugin.getUim().openInventory(p, plugin.getUim().getMenus("projectile"),
                        new String[]{"<noproj>", String.valueOf(vote.getVotes("NOPROJ"))},
                        new String[]{"<yesproj>", String.valueOf(vote.getVotes("YESPROJ"))},
                        new String[]{"<exproj>", String.valueOf(vote.getVotes("EXPROJ"))},
                        new String[]{"<desproj>", String.valueOf(vote.getVotes("DESPROJ"))},
                        new String[]{"<teleproj>", String.valueOf(vote.getVotes("TELEPROJ"))});
            }
            if (display.equals(plugin.getLang().get(p, "menus.projectile.exProj.nameItem"))) {
                if (!p.hasPermission("ultraskywars.votes.projectile.*") && !p.hasPermission("ultraskywars.votes.projectile.exproj")) {
                    p.sendMessage(plugin.getLang().get(p, "messages.noPermission"));
                    return;
                }
                Vote.VotePlayer vp = vote.getVotePlayer(p);
                if (vp.getProjectileType() != null) {
                    if (vp.getProjectileType().equals(ProjectileType.EXPROJ)) {
                        p.sendMessage(plugin.getLang().get(p, "messages.alreadyVoted").replaceAll("<type>", plugin.getLang().get(p, "votes.projectile.exproj")));
                        return;
                    }
                }
                vp.setProjectileType(ProjectileType.EXPROJ);
                g.sendGameMessage(plugin.getLang().get(p, "messages.voted").replaceAll("<player>", p.getName()).replaceAll("<category>", plugin.getLang().get(p, "votes.projectile.name")).replaceAll("<type>", plugin.getLang().get(p, "votes.projectile.exproj")).replaceAll("<votes>", String.valueOf(vote.getVotes("EXPROJ"))));
                plugin.getUim().openInventory(p, plugin.getUim().getMenus("projectile"),
                        new String[]{"<noproj>", String.valueOf(vote.getVotes("NOPROJ"))},
                        new String[]{"<yesproj>", String.valueOf(vote.getVotes("YESPROJ"))},
                        new String[]{"<exproj>", String.valueOf(vote.getVotes("EXPROJ"))},
                        new String[]{"<desproj>", String.valueOf(vote.getVotes("DESPROJ"))},
                        new String[]{"<teleproj>", String.valueOf(vote.getVotes("TELEPROJ"))});
            }
            if (display.equals(plugin.getLang().get(p, "menus.projectile.desProj.nameItem"))) {
                if (!p.hasPermission("ultraskywars.votes.projectile.*") && !p.hasPermission("ultraskywars.votes.projectile.desproj")) {
                    p.sendMessage(plugin.getLang().get(p, "messages.noPermission"));
                    return;
                }
                Vote.VotePlayer vp = vote.getVotePlayer(p);
                if (vp.getProjectileType() != null) {
                    if (vp.getProjectileType().equals(ProjectileType.DESPROJ)) {
                        p.sendMessage(plugin.getLang().get(p, "messages.alreadyVoted").replaceAll("<type>", plugin.getLang().get(p, "votes.projectile.desproj")));
                        return;
                    }
                }
                vp.setProjectileType(ProjectileType.DESPROJ);
                g.sendGameMessage(plugin.getLang().get(p, "messages.voted").replaceAll("<player>", p.getName()).replaceAll("<category>", plugin.getLang().get(p, "votes.projectile.name")).replaceAll("<type>", plugin.getLang().get(p, "votes.projectile.desproj")).replaceAll("<votes>", String.valueOf(vote.getVotes("DESPROJ"))));
                plugin.getUim().openInventory(p, plugin.getUim().getMenus("projectile"),
                        new String[]{"<noproj>", String.valueOf(vote.getVotes("NOPROJ"))},
                        new String[]{"<yesproj>", String.valueOf(vote.getVotes("YESPROJ"))},
                        new String[]{"<exproj>", String.valueOf(vote.getVotes("EXPROJ"))},
                        new String[]{"<desproj>", String.valueOf(vote.getVotes("DESPROJ"))},
                        new String[]{"<teleproj>", String.valueOf(vote.getVotes("TELEPROJ"))});
            }
            if (display.equals(plugin.getLang().get(p, "menus.projectile.teleProj.nameItem"))) {
                if (!p.hasPermission("ultraskywars.votes.projectile.*") && !p.hasPermission("ultraskywars.votes.projectile.teleproj")) {
                    p.sendMessage(plugin.getLang().get(p, "messages.noPermission"));
                    return;
                }
                Vote.VotePlayer vp = vote.getVotePlayer(p);
                if (vp.getProjectileType() != null) {
                    if (vp.getProjectileType().equals(ProjectileType.TELEPROJ)) {
                        p.sendMessage(plugin.getLang().get(p, "messages.alreadyVoted").replaceAll("<type>", plugin.getLang().get(p, "votes.projectile.teleproj")));
                        return;
                    }
                }
                vp.setProjectileType(ProjectileType.TELEPROJ);
                g.sendGameMessage(plugin.getLang().get(p, "messages.voted").replaceAll("<player>", p.getName()).replaceAll("<category>", plugin.getLang().get(p, "votes.projectile.name")).replaceAll("<type>", plugin.getLang().get(p, "votes.projectile.teleproj")).replaceAll("<votes>", String.valueOf(vote.getVotes("TELEPROJ"))));
                plugin.getUim().openInventory(p, plugin.getUim().getMenus("projectile"),
                        new String[]{"<noproj>", String.valueOf(vote.getVotes("NOPROJ"))},
                        new String[]{"<yesproj>", String.valueOf(vote.getVotes("YESPROJ"))},
                        new String[]{"<exproj>", String.valueOf(vote.getVotes("EXPROJ"))},
                        new String[]{"<desproj>", String.valueOf(vote.getVotes("DESPROJ"))},
                        new String[]{"<teleproj>", String.valueOf(vote.getVotes("TELEPROJ"))});
            }
        }
        if (e.getView().getTitle().equals(ti.getTitle())) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR)) {
                return;
            }
            if (!ti.getContents().containsKey(e.getSlot())) {
                return;
            }
            ItemStack item = ti.getContents().get(e.getSlot());
            if (!item.hasItemMeta()) {
                return;
            }
            if (!item.getItemMeta().hasDisplayName()) {
                return;
            }
            Vote vote = g.getVote();
            ItemMeta im = item.getItemMeta();
            String display = im.getDisplayName();
            if (display.equals(plugin.getLang().get(p, "menus.time.dawn.nameItem"))) {
                if (!p.hasPermission("ultraskywars.votes.time.*") && !p.hasPermission("ultraskywars.votes.time.dawn")) {
                    p.sendMessage(plugin.getLang().get(p, "messages.noPermission"));
                    return;
                }
                Vote.VotePlayer vp = vote.getVotePlayer(p);
                if (vp.getTimeType() != null) {
                    if (vp.getTimeType().equals(TimeType.DAWN)) {
                        p.sendMessage(plugin.getLang().get(p, "messages.alreadyVoted").replaceAll("<type>", plugin.getLang().get(p, "votes.time.dawn")));
                        return;
                    }
                }
                vp.setTimeType(TimeType.DAWN);
                g.sendGameMessage(plugin.getLang().get(p, "messages.voted").replaceAll("<player>", p.getName()).replaceAll("<category>", plugin.getLang().get(p, "votes.time.name")).replaceAll("<type>", plugin.getLang().get(p, "votes.time.dawn")).replaceAll("<votes>", String.valueOf(vote.getVotes("DAWN"))));
                plugin.getUim().openInventory(p, plugin.getUim().getMenus("time"),
                        new String[]{"<dawntime>", String.valueOf(vote.getVotes("DAWN"))},
                        new String[]{"<daytime>", String.valueOf(vote.getVotes("DAY"))},
                        new String[]{"<afternoontime>", String.valueOf(vote.getVotes("AFTERNOON"))},
                        new String[]{"<nighttime>", String.valueOf(vote.getVotes("NIGHT"))});
            }
            if (display.equals(plugin.getLang().get(p, "menus.time.day.nameItem"))) {
                if (!p.hasPermission("ultraskywars.votes.time.*") && !p.hasPermission("ultraskywars.votes.time.day")) {
                    p.sendMessage(plugin.getLang().get(p, "messages.noPermission"));
                    return;
                }
                Vote.VotePlayer vp = vote.getVotePlayer(p);
                if (vp.getTimeType() != null) {
                    if (vp.getTimeType().equals(TimeType.DAY)) {
                        p.sendMessage(plugin.getLang().get(p, "messages.alreadyVoted").replaceAll("<type>", plugin.getLang().get(p, "votes.time.day")));
                        return;
                    }
                }
                vp.setTimeType(TimeType.DAY);
                g.sendGameMessage(plugin.getLang().get(p, "messages.voted").replaceAll("<player>", p.getName()).replaceAll("<category>", plugin.getLang().get(p, "votes.time.name")).replaceAll("<type>", plugin.getLang().get(p, "votes.time.day")).replaceAll("<votes>", String.valueOf(vote.getVotes("DAY"))));
                plugin.getUim().openInventory(p, plugin.getUim().getMenus("time"),
                        new String[]{"<dawntime>", String.valueOf(vote.getVotes("DAWN"))},
                        new String[]{"<daytime>", String.valueOf(vote.getVotes("DAY"))},
                        new String[]{"<afternoontime>", String.valueOf(vote.getVotes("AFTERNOON"))},
                        new String[]{"<nighttime>", String.valueOf(vote.getVotes("NIGHT"))});
            }
            if (display.equals(plugin.getLang().get(p, "menus.time.afternoon.nameItem"))) {
                if (!p.hasPermission("ultraskywars.votes.time.*") && !p.hasPermission("ultraskywars.votes.time.afternoon")) {
                    p.sendMessage(plugin.getLang().get(p, "messages.noPermission"));
                    return;
                }
                Vote.VotePlayer vp = vote.getVotePlayer(p);
                if (vp.getTimeType() != null) {
                    if (vp.getTimeType().equals(TimeType.AFTERNOON)) {
                        p.sendMessage(plugin.getLang().get(p, "messages.alreadyVoted").replaceAll("<type>", plugin.getLang().get(p, "votes.time.afternoon")));
                        return;
                    }
                }
                vp.setTimeType(TimeType.AFTERNOON);
                g.sendGameMessage(plugin.getLang().get(p, "messages.voted").replaceAll("<player>", p.getName()).replaceAll("<category>", plugin.getLang().get(p, "votes.time.name")).replaceAll("<type>", plugin.getLang().get(p, "votes.time.afternoon")).replaceAll("<votes>", String.valueOf(vote.getVotes("AFTERNOON"))));
                plugin.getUim().openInventory(p, plugin.getUim().getMenus("time"),
                        new String[]{"<dawntime>", String.valueOf(vote.getVotes("DAWN"))},
                        new String[]{"<daytime>", String.valueOf(vote.getVotes("DAY"))},
                        new String[]{"<afternoontime>", String.valueOf(vote.getVotes("AFTERNOON"))},
                        new String[]{"<nighttime>", String.valueOf(vote.getVotes("NIGHT"))});
            }
            if (display.equals(plugin.getLang().get(p, "menus.time.night.nameItem"))) {
                if (!p.hasPermission("ultraskywars.votes.time.*") && !p.hasPermission("ultraskywars.votes.time.night")) {
                    p.sendMessage(plugin.getLang().get(p, "messages.noPermission"));
                    return;
                }
                Vote.VotePlayer vp = vote.getVotePlayer(p);
                if (vp.getTimeType() != null) {
                    if (vp.getTimeType().equals(TimeType.NIGHT)) {
                        p.sendMessage(plugin.getLang().get(p, "messages.alreadyVoted").replaceAll("<type>", plugin.getLang().get(p, "votes.time.night")));
                        return;
                    }
                }
                vp.setTimeType(TimeType.NIGHT);
                g.sendGameMessage(plugin.getLang().get(p, "messages.voted").replaceAll("<player>", p.getName()).replaceAll("<category>", plugin.getLang().get(p, "votes.time.name")).replaceAll("<type>", plugin.getLang().get(p, "votes.time.night")).replaceAll("<votes>", String.valueOf(vote.getVotes("NIGHT"))));
                plugin.getUim().openInventory(p, plugin.getUim().getMenus("time"),
                        new String[]{"<dawntime>", String.valueOf(vote.getVotes("DAWN"))},
                        new String[]{"<daytime>", String.valueOf(vote.getVotes("DAY"))},
                        new String[]{"<afternoontime>", String.valueOf(vote.getVotes("AFTERNOON"))},
                        new String[]{"<nighttime>", String.valueOf(vote.getVotes("NIGHT"))});
            }
        }
        if (e.getView().getTitle().equals(he.getTitle())) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR)) {
                return;
            }
            if (!he.getContents().containsKey(e.getSlot())) {
                return;
            }
            ItemStack item = he.getContents().get(e.getSlot());
            if (!item.hasItemMeta()) {
                return;
            }
            if (!item.getItemMeta().hasDisplayName()) {
                return;
            }
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
        }
        if (e.getView().getTitle().equals(fi.getTitle())) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR)) {
                return;
            }
            if (!fi.getContents().containsKey(e.getSlot())) {
                return;
            }
            ItemStack item = fi.getContents().get(e.getSlot());
            if (!item.hasItemMeta()) {
                return;
            }
            if (!item.getItemMeta().hasDisplayName()) {
                return;
            }
            Vote vote = g.getVote();
            ItemMeta im = item.getItemMeta();
            String display = im.getDisplayName();
            if (display.equals(plugin.getLang().get(p, "menus.final.dragon.nameItem"))) {
                if (!p.hasPermission("ultraskywars.votes.final.*") && !p.hasPermission("ultraskywars.votes.final.dragon")) {
                    p.sendMessage(plugin.getLang().get(p, "messages.noPermission"));
                    return;
                }
                Vote.VotePlayer vp = vote.getVotePlayer(p);
                if (vp.getFinalType() != null) {
                    if (vp.getFinalType().equals(FinalType.DRAGON)) {
                        p.sendMessage(plugin.getLang().get(p, "messages.alreadyVoted").replaceAll("<type>", plugin.getLang().get(p, "votes.final.dragon")));
                        return;
                    }
                }
                vp.setFinalType(FinalType.DRAGON);
                g.sendGameMessage(plugin.getLang().get(p, "messages.voted").replaceAll("<player>", p.getName()).replaceAll("<category>", plugin.getLang().get(p, "votes.final.name")).replaceAll("<type>", plugin.getLang().get(p, "votes.final.dragon")).replaceAll("<votes>", String.valueOf(vote.getVotes("DRAGON"))));
                plugin.getUim().openInventory(p, plugin.getUim().getMenus("final"),
                        new String[]{"<dragonfinal>", String.valueOf(vote.getVotes("DRAGON"))},
                        new String[]{"<borderfinal>", String.valueOf(vote.getVotes("BORDER"))},
                        new String[]{"<tntfinal>", String.valueOf(vote.getVotes("TNT"))},
                        new String[]{"<zombiesfinal>", String.valueOf(vote.getVotes("ZOMBIES"))},
                        new String[]{"<witherfinal>", String.valueOf(vote.getVotes("WITHER"))},
                        new String[]{"<nonefinal>", String.valueOf(vote.getVotes("NONE"))});
            }
            if (display.equals(plugin.getLang().get(p, "menus.final.tnt.nameItem"))) {
                if (!p.hasPermission("ultraskywars.votes.final.*") && !p.hasPermission("ultraskywars.votes.final.tnt")) {
                    p.sendMessage(plugin.getLang().get(p, "messages.noPermission"));
                    return;
                }
                Vote.VotePlayer vp = vote.getVotePlayer(p);
                if (vp.getFinalType() != null) {
                    if (vp.getFinalType().equals(FinalType.TNT)) {
                        p.sendMessage(plugin.getLang().get(p, "messages.alreadyVoted").replaceAll("<type>", plugin.getLang().get(p, "votes.final.tnt")));
                        return;
                    }
                }
                vp.setFinalType(FinalType.TNT);
                g.sendGameMessage(plugin.getLang().get(p, "messages.voted").replaceAll("<player>", p.getName()).replaceAll("<category>", plugin.getLang().get(p, "votes.final.name")).replaceAll("<type>", plugin.getLang().get(p, "votes.final.tnt")).replaceAll("<votes>", String.valueOf(vote.getVotes("TNT"))));
                plugin.getUim().openInventory(p, plugin.getUim().getMenus("final"),
                        new String[]{"<dragonfinal>", String.valueOf(vote.getVotes("DRAGON"))},
                        new String[]{"<borderfinal>", String.valueOf(vote.getVotes("BORDER"))},
                        new String[]{"<tntfinal>", String.valueOf(vote.getVotes("TNT"))},
                        new String[]{"<zombiesfinal>", String.valueOf(vote.getVotes("ZOMBIES"))},
                        new String[]{"<witherfinal>", String.valueOf(vote.getVotes("WITHER"))},
                        new String[]{"<nonefinal>", String.valueOf(vote.getVotes("NONE"))});
            }
            if (display.equals(plugin.getLang().get(p, "menus.final.wither.nameItem"))) {
                if (!p.hasPermission("ultraskywars.votes.final.*") && !p.hasPermission("ultraskywars.votes.final.wither")) {
                    p.sendMessage(plugin.getLang().get(p, "messages.noPermission"));
                    return;
                }
                Vote.VotePlayer vp = vote.getVotePlayer(p);
                if (vp.getFinalType() != null) {
                    if (vp.getFinalType().equals(FinalType.WITHER)) {
                        p.sendMessage(plugin.getLang().get(p, "messages.alreadyVoted").replaceAll("<type>", plugin.getLang().get(p, "votes.final.wither")));
                        return;
                    }
                }
                vp.setFinalType(FinalType.WITHER);
                g.sendGameMessage(plugin.getLang().get(p, "messages.voted").replaceAll("<player>", p.getName()).replaceAll("<category>", plugin.getLang().get(p, "votes.final.name")).replaceAll("<type>", plugin.getLang().get(p, "votes.final.wither")).replaceAll("<votes>", String.valueOf(vote.getVotes("WITHER"))));
                plugin.getUim().openInventory(p, plugin.getUim().getMenus("final"),
                        new String[]{"<dragonfinal>", String.valueOf(vote.getVotes("DRAGON"))},
                        new String[]{"<borderfinal>", String.valueOf(vote.getVotes("BORDER"))},
                        new String[]{"<tntfinal>", String.valueOf(vote.getVotes("TNT"))},
                        new String[]{"<zombiesfinal>", String.valueOf(vote.getVotes("ZOMBIES"))},
                        new String[]{"<witherfinal>", String.valueOf(vote.getVotes("WITHER"))},
                        new String[]{"<nonefinal>", String.valueOf(vote.getVotes("NONE"))});
            }
            if (display.equals(plugin.getLang().get(p, "menus.final.zombie.nameItem"))) {
                if (!p.hasPermission("ultraskywars.votes.final.*") && !p.hasPermission("ultraskywars.votes.final.zombie")) {
                    p.sendMessage(plugin.getLang().get(p, "messages.noPermission"));
                    return;
                }
                Vote.VotePlayer vp = vote.getVotePlayer(p);
                if (vp.getFinalType() != null) {
                    if (vp.getFinalType().equals(FinalType.ZOMBIES)) {
                        p.sendMessage(plugin.getLang().get(p, "messages.alreadyVoted").replaceAll("<type>", plugin.getLang().get(p, "votes.final.zombies")));
                        return;
                    }
                }
                vp.setFinalType(FinalType.ZOMBIES);
                g.sendGameMessage(plugin.getLang().get(p, "messages.voted").replaceAll("<player>", p.getName()).replaceAll("<category>", plugin.getLang().get(p, "votes.final.name")).replaceAll("<type>", plugin.getLang().get(p, "votes.final.zombies")).replaceAll("<votes>", String.valueOf(vote.getVotes("ZOMBIES"))));
                plugin.getUim().openInventory(p, plugin.getUim().getMenus("final"),
                        new String[]{"<dragonfinal>", String.valueOf(vote.getVotes("DRAGON"))},
                        new String[]{"<borderfinal>", String.valueOf(vote.getVotes("BORDER"))},
                        new String[]{"<tntfinal>", String.valueOf(vote.getVotes("TNT"))},
                        new String[]{"<zombiesfinal>", String.valueOf(vote.getVotes("ZOMBIES"))},
                        new String[]{"<witherfinal>", String.valueOf(vote.getVotes("WITHER"))},
                        new String[]{"<nonefinal>", String.valueOf(vote.getVotes("NONE"))});
            }
            if (display.equals(plugin.getLang().get(p, "menus.final.none.nameItem"))) {
                if (!p.hasPermission("ultraskywars.votes.final.*") && !p.hasPermission("ultraskywars.votes.final.none")) {
                    p.sendMessage(plugin.getLang().get(p, "messages.noPermission"));
                    return;
                }
                Vote.VotePlayer vp = vote.getVotePlayer(p);
                if (vp.getFinalType() != null) {
                    if (vp.getFinalType().equals(FinalType.NONE)) {
                        p.sendMessage(plugin.getLang().get(p, "messages.alreadyVoted").replaceAll("<type>", plugin.getLang().get(p, "votes.final.none")));
                        return;
                    }
                }
                vp.setFinalType(FinalType.NONE);
                g.sendGameMessage(plugin.getLang().get(p, "messages.voted").replaceAll("<player>", p.getName()).replaceAll("<category>", plugin.getLang().get(p, "votes.final.name")).replaceAll("<type>", plugin.getLang().get(p, "votes.final.none")).replaceAll("<votes>", String.valueOf(vote.getVotes("NONE"))));
                plugin.getUim().openInventory(p, plugin.getUim().getMenus("final"),
                        new String[]{"<dragonfinal>", String.valueOf(vote.getVotes("DRAGON"))},
                        new String[]{"<borderfinal>", String.valueOf(vote.getVotes("BORDER"))},
                        new String[]{"<tntfinal>", String.valueOf(vote.getVotes("TNT"))},
                        new String[]{"<zombiesfinal>", String.valueOf(vote.getVotes("ZOMBIES"))},
                        new String[]{"<witherfinal>", String.valueOf(vote.getVotes("WITHER"))},
                        new String[]{"<nonefinal>", String.valueOf(vote.getVotes("NONE"))});
            }
            if (display.equals(plugin.getLang().get(p, "menus.final.border.nameItem"))) {
                if (!p.hasPermission("ultraskywars.votes.final.*") && !p.hasPermission("ultraskywars.votes.final.border")) {
                    p.sendMessage(plugin.getLang().get(p, "messages.noPermission"));
                    return;
                }
                Vote.VotePlayer vp = vote.getVotePlayer(p);
                if (vp.getFinalType() != null) {
                    if (vp.getFinalType().equals(FinalType.BORDER)) {
                        p.sendMessage(plugin.getLang().get(p, "messages.alreadyVoted").replaceAll("<type>", plugin.getLang().get(p, "votes.final.border")));
                        return;
                    }
                }
                vp.setFinalType(FinalType.BORDER);
                g.sendGameMessage(plugin.getLang().get(p, "messages.voted").replaceAll("<player>", p.getName()).replaceAll("<category>", plugin.getLang().get(p, "votes.final.name")).replaceAll("<type>", plugin.getLang().get(p, "votes.final.border")).replaceAll("<votes>", String.valueOf(vote.getVotes("BORDER"))));
                plugin.getUim().openInventory(p, plugin.getUim().getMenus("final"),
                        new String[]{"<dragonfinal>", String.valueOf(vote.getVotes("DRAGON"))},
                        new String[]{"<borderfinal>", String.valueOf(vote.getVotes("BORDER"))},
                        new String[]{"<tntfinal>", String.valueOf(vote.getVotes("TNT"))},
                        new String[]{"<zombiesfinal>", String.valueOf(vote.getVotes("ZOMBIES"))},
                        new String[]{"<witherfinal>", String.valueOf(vote.getVotes("WITHER"))},
                        new String[]{"<nonefinal>", String.valueOf(vote.getVotes("NONE"))});
            }
        }
        if (e.getView().getTitle().equals(ch.getTitle())) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR)) {
                return;
            }
            if (!ch.getContents().containsKey(e.getSlot())) {
                return;
            }
            ItemStack item = ch.getContents().get(e.getSlot());
            if (!item.hasItemMeta()) {
                return;
            }
            if (!item.getItemMeta().hasDisplayName()) {
                return;
            }
            Vote vote = g.getVote();
            ItemMeta im = item.getItemMeta();
            String display = im.getDisplayName();
            if (display.equals(plugin.getChestType().get(p, "lang.chest.basic.nameItem"))) {
                if (!p.hasPermission("ultraskywars.votes.chest.*") && !p.hasPermission("ultraskywars.votes.chest.basic")) {
                    p.sendMessage(plugin.getLang().get(p, "messages.noPermission"));
                    return;
                }
                Vote.VotePlayer vp = vote.getVotePlayer(p);
                if (vp.getChestType() != null) {
                    if (vp.getChestType().equals("BASIC")) {
                        p.sendMessage(plugin.getLang().get(p, "messages.alreadyVoted").replaceAll("<type>", plugin.getChestType().get(p, "votes.chest.basic")));
                        return;
                    }
                }
                vp.setChestType("BASIC");
                g.sendGameMessage(plugin.getLang().get(p, "messages.voted").replaceAll("<player>", p.getName()).replaceAll("<category>", plugin.getChestType().get(p, "votes.chest.name")).replaceAll("<type>", plugin.getChestType().get(p, "votes.chest.basic")).replaceAll("<votes>", String.valueOf(vote.getVotes("BASIC"))));
                plugin.getUim().openInventory(p, plugin.getUim().getMenus("chest"),
                        new String[]{"<basicChest>", String.valueOf(vote.getVotes("BASIC"))},
                        new String[]{"<normalChest>", String.valueOf(vote.getVotes("NORMAL"))},
                        new String[]{"<opChest>", String.valueOf(vote.getVotes("OP"))});
            }
            if (display.equals(plugin.getChestType().get(p, "lang.chest.normal.nameItem"))) {
                if (!p.hasPermission("ultraskywars.votes.chest.*") && !p.hasPermission("ultraskywars.votes.chest.normal")) {
                    p.sendMessage(plugin.getLang().get(p, "messages.noPermission"));
                    return;
                }
                Vote.VotePlayer vp = vote.getVotePlayer(p);
                if (vp.getChestType() != null) {
                    if (vp.getChestType().equals("NORMAL")) {
                        p.sendMessage(plugin.getLang().get(p, "messages.alreadyVoted").replaceAll("<type>", plugin.getChestType().get(p, "votes.chest.normal")));
                        return;
                    }
                }
                vp.setChestType("NORMAL");
                g.sendGameMessage(plugin.getLang().get(p, "messages.voted").replaceAll("<player>", p.getName()).replaceAll("<category>", plugin.getChestType().get(p, "votes.chest.name")).replaceAll("<type>", plugin.getChestType().get(p, "votes.chest.normal")).replaceAll("<votes>", String.valueOf(vote.getVotes("NORMAL"))));
                plugin.getUim().openInventory(p, plugin.getUim().getMenus("chest"),
                        new String[]{"<basicChest>", String.valueOf(vote.getVotes("BASIC"))},
                        new String[]{"<normalChest>", String.valueOf(vote.getVotes("NORMAL"))},
                        new String[]{"<opChest>", String.valueOf(vote.getVotes("OP"))});
            }
            if (display.equals(plugin.getChestType().get(p, "lang.chest.op.nameItem"))) {
                if (!p.hasPermission("ultraskywars.votes.chest.*") && !p.hasPermission("ultraskywars.votes.chest.op")) {
                    p.sendMessage(plugin.getLang().get(p, "messages.noPermission"));
                    return;
                }
                Vote.VotePlayer vp = vote.getVotePlayer(p);
                if (vp.getChestType() != null) {
                    if (vp.getChestType().equals("OP")) {
                        p.sendMessage(plugin.getLang().get(p, "messages.alreadyVoted").replaceAll("<type>", plugin.getChestType().get(p, "votes.chest.op")));
                        return;
                    }
                }
                vp.setChestType("OP");
                g.sendGameMessage(plugin.getLang().get(p, "messages.voted").replaceAll("<player>", p.getName()).replaceAll("<category>", plugin.getChestType().get(p, "votes.chest.name")).replaceAll("<type>", plugin.getChestType().get(p, "votes.chest.op")).replaceAll("<votes>", String.valueOf(vote.getVotes("OP"))));
                plugin.getUim().openInventory(p, plugin.getUim().getMenus("chest"),
                        new String[]{"<basicChest>", String.valueOf(vote.getVotes("BASIC"))},
                        new String[]{"<normalChest>", String.valueOf(vote.getVotes("NORMAL"))},
                        new String[]{"<opChest>", String.valueOf(vote.getVotes("OP"))});
            }
        }
        if (e.getView().getTitle().equals(v.getTitle())) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR)) {
                return;
            }
            if (!v.getContents().containsKey(e.getSlot())) {
                return;
            }
            ItemStack item = v.getContents().get(e.getSlot());
            if (!item.hasItemMeta()) {
                return;
            }
            if (!item.getItemMeta().hasDisplayName()) {
                return;
            }
            Vote vote = g.getVote();
            ItemMeta im = item.getItemMeta();
            String display = im.getDisplayName();
            if (display.equals(plugin.getLang().get(p, "menus.votes.chest.nameItem"))) {
                plugin.getUim().openInventory(p, plugin.getUim().getMenus("chest"),
                        new String[]{"<basicChest>", String.valueOf(vote.getVotes("BASIC"))},
                        new String[]{"<normalChest>", String.valueOf(vote.getVotes("NORMAL"))},
                        new String[]{"<opChest>", String.valueOf(vote.getVotes("OP"))});
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
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();
        UltraSkyWars plugin = UltraSkyWars.get();
        if (plugin.getCm().isChestHolograms()) {
            if (chests.containsKey(p.getUniqueId())) {
                if (plugin.getGm().isPlayerInGame(p)) {
                    Game game = plugin.getGm().getGameByPlayer(p);
                    if (e.getInventory().getType().equals(InventoryType.CHEST)) {
                        UltraGameChest ugc = game.getChestByLocation(chests.get(p.getUniqueId()));
                        if (ugc != null) {
                            GameEvent ge = game.getNowEvent();
                            ugc.spawn(Utils.isEmpty(ugc.getInv()));
                            if (ge != null) {
                                String text = plugin.getLang().get("timer").replace("<time>", Utils.convertTime(ge.getTime()));
                                ugc.updateTimer(text);
                            }
                        }
                    }
                    chests.remove(p.getUniqueId());
                }
            }
        }
        if (e.getView().getTitle().equals(plugin.getLang().get("menus.teamselector.title"))) {
            plugin.getGem().getViews().remove(p.getUniqueId());
        }
    }

    private void createMenuGames(Player p, String gameType, UltraSkyWars plugin) {
        plugin.getGem().createSelectorMenu(p, "none", gameType.toLowerCase());
    }
}