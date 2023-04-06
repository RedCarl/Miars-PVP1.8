package io.github.Leonardo0013YT.UltraSkyWars.listeners.lobby;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.cosmetics.*;
import io.github.Leonardo0013YT.UltraSkyWars.cosmetics.killeffects.UltraKillEffect;
import io.github.Leonardo0013YT.UltraSkyWars.cosmetics.kits.Kit;
import io.github.Leonardo0013YT.UltraSkyWars.cosmetics.kits.KitLevel;
import io.github.Leonardo0013YT.UltraSkyWars.cosmetics.taunts.Taunt;
import io.github.Leonardo0013YT.UltraSkyWars.cosmetics.windances.UltraWinDance;
import io.github.Leonardo0013YT.UltraSkyWars.cosmetics.wineffects.UltraWinEffect;
import io.github.Leonardo0013YT.UltraSkyWars.data.SWPlayer;
import io.github.Leonardo0013YT.UltraSkyWars.enums.CustomSound;
import io.github.Leonardo0013YT.UltraSkyWars.game.GameData;
import io.github.Leonardo0013YT.UltraSkyWars.objects.PerkLevel;
import io.github.Leonardo0013YT.UltraSkyWars.objects.PrestigeIcon;
import io.github.Leonardo0013YT.UltraSkyWars.objects.PreviewCosmetic;
import io.github.Leonardo0013YT.UltraSkyWars.superclass.Perk;
import io.github.Leonardo0013YT.UltraSkyWars.utils.NBTEditor;
import io.github.Leonardo0013YT.UltraSkyWars.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class LobbyMenuListener implements Listener {

    private UltraSkyWars plugin;

    public LobbyMenuListener(UltraSkyWars plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMenu(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (!e.getSlotType().equals(InventoryType.SlotType.OUTSIDE) && (e.getClickedInventory().getType().equals(InventoryType.PLAYER) || e.getClickedInventory().getType().equals(InventoryType.CRAFTING))) {
            if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR)) {
                if (e.getClick().equals(ClickType.NUMBER_KEY)) {
                    e.setCancelled(true);
                }
                return;
            }
            ItemStack item = e.getCurrentItem();
            if (plugin.getIm().getLobby().equals(item)) {
                e.setCancelled(true);
            }
        }
        if (e.getView().getTitle().equals(plugin.getLang().get(p, "menus.prestige.title"))) {
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
            SWPlayer sw = plugin.getDb().getSWPlayer(p);
            if (item.getItemMeta().getDisplayName().equals(plugin.getLang().get(p, "menus.back.nameItem"))) {
                plugin.getGem().createLevelMenu(p);
                return;
            }
            if (item.getItemMeta().getDisplayName().equals(plugin.getLang().get(p, "menus.close.nameItem"))) {
                p.closeInventory();
                return;
            }
            String id = NBTEditor.getString(item, "PRESTIGE_ICON_ID");
            boolean has = NBTEditor.getBoolean(item, "PRESTIGE_ICON_HAS");
            if (!has) {
                p.sendMessage(plugin.getLang().get("messages.levels.prestigeNoHas"));
                CustomSound.PRESTIGE_NO_HAS.reproduce(p);
                return;
            }
            if (sw.getPrestigeIcon().equals(id)) {
                p.sendMessage(plugin.getLang().get("messages.levels.alreadySelected"));
                CustomSound.PRESTIGE_ALREADY_SELECTED.reproduce(p);
            } else {
                PrestigeIcon pi = plugin.getLvl().getPrestige().get(id);
                p.sendMessage(plugin.getLang().get("messages.levels.selectPrestige").replace("<name>", pi.getName()));
                sw.setPrestigeIcon(id);
                plugin.getGem().createPrestigeIcons(p);
                CustomSound.PRESTIGE_SELECT.reproduce(p);
            }
        }
        if (e.getView().getTitle().equals(plugin.getLang().get(p, "menus.levels.title"))) {
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
            if (display.equals(plugin.getLang().get(p, "menus.levels.prestige.nameItem"))) {
                plugin.getGem().createPrestigeIcons(p);
            }
            if (display.equals(plugin.getLang().get(p, "menus.levels.hide.nameItem"))) {
                sw.setShowLevel(false);
                p.sendMessage(plugin.getLang().get("messages.showPrefix").replaceAll("<status>", Utils.parseBoolean(sw.isShowLevel())));
                plugin.getGem().createLevelMenu(p);
            }
            if (display.equals(plugin.getLang().get(p, "menus.levels.show.nameItem"))) {
                sw.setShowLevel(true);
                p.sendMessage(plugin.getLang().get("messages.showPrefix").replaceAll("<status>", Utils.parseBoolean(sw.isShowLevel())));
                plugin.getGem().createLevelMenu(p);
            }
            if (display.equals(plugin.getLang().get(p, "menus.close.nameItem"))) {
                if (e.getClick().equals(ClickType.RIGHT)) {
                    p.sendMessage(plugin.getLang().get(p, "messages.closeWithClick"));
                    return;
                }
                p.closeInventory();
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
                return;
            }
            String[] data = NBTEditor.getString(e.getCurrentItem(), "PERK_DATA").split(":");
            int id = Integer.parseInt(data[0]);
            boolean locked = Boolean.parseBoolean(data[1]);
            int level = Integer.parseInt(data[2]);
            boolean maxed = Boolean.parseBoolean(data[3]);
            SWPlayer sw = plugin.getDb().getSWPlayer(p);
            if (e.isRightClick()) {
                if (locked) {
                    p.sendMessage(plugin.getLang().get("messages.perks.perkLocked"));
                    CustomSound.PERK_LOCKED.reproduce(p);
                    return;
                }
                if (sw.getPerksEnabled().contains(id)) {
                    sw.getPerksEnabled().remove(id);
                    CustomSound.PERK_OFF.reproduce(p);
                } else {
                    sw.getPerksEnabled().add(id);
                    CustomSound.PERK_ON.reproduce(p);
                }
                p.sendMessage(plugin.getLang().get("messages.perks.toggle").replace("<state>", (!sw.getPerksEnabled().contains(id) ? plugin.getLang().get("messages.perks.onState") : plugin.getLang().get("messages.perks.offState"))));
            } else {
                if (maxed) {
                    p.sendMessage(plugin.getLang().get("messages.perks.maxedPerk"));
                    CustomSound.PERK_MAXED.reproduce(p);
                    return;
                }
                Perk perk = plugin.getIjm().getPerks().getPem().getPerkByID(id);
                PerkLevel next = perk.getLevels().get(level + 1);
                if (plugin.getAdm().getCoins(p) < next.getPrice()) {
                    p.sendMessage(plugin.getLang().get("messages.perks.noCoins"));
                    CustomSound.PERK_NO_COINS.reproduce(p);
                    return;
                }
                sw.getPerksData().put(id, level + 1);
                plugin.getAdm().removeCoins(p, next.getPrice());
                p.sendMessage(plugin.getLang().get("messages.perks.buyed").replace("<level>", String.valueOf(level + 1)));
                CustomSound.PERK_BUY.reproduce(p);
            }
            p.closeInventory();
        }
        if (e.getView().getTitle().equals(plugin.getLang().get(p, "menus.partingselector.title"))) {
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
            if (display.equals(plugin.getLang().get(p, "menus.next.nameItem"))) {
                plugin.getUim().addPage(p);
                plugin.getUim().createPartingSelectorMenu(p);
                return;
            }
            if (display.equals(plugin.getLang().get(p, "menus.last.nameItem"))) {
                plugin.getUim().removePage(p);
                plugin.getUim().createPartingSelectorMenu(p);
                return;
            }
            if (display.equals(plugin.getLang().get(p, "menus.partingselector.kit.nameItem"))) {
                return;
            }
            if (display.equals(plugin.getLang().get(p, "menus.partingselector.deselect.nameItem"))) {
                if (sw.getParting() == 999999) {
                    p.sendMessage(plugin.getLang().get(p, "messages.noSelect"));
                    return;
                }
                sw.setParting(999999);
                p.sendMessage(plugin.getLang().get(p, "messages.deselectParting"));
                plugin.getUim().createPartingSelectorMenu(p);
                return;
            }
            if (display.equals(plugin.getLang().get(p, "menus.partingselector.close.nameItem"))) {
                if (e.getClick().equals(ClickType.RIGHT)) {
                    p.sendMessage(plugin.getLang().get(p, "messages.closeWithClick"));
                    return;
                }
                p.closeInventory();
                return;
            }
            Parting k = plugin.getCos().getPartingByItem(item);
            if (k == null) {
                return;
            }
            if (p.hasPermission(k.getAutoGivePermission())) {
                sw.setParting(k.getId());
                p.sendMessage(plugin.getLang().get(p, "messages.selectParting").replaceAll("<parting>", k.getName()));
                plugin.getUim().createPartingSelectorMenu(p);
                return;
            }
            if (!sw.getPartings().contains(k.getId())) {
                if (k.needPermToBuy() && !p.hasPermission(k.getPermission())) {
                    p.sendMessage(plugin.getLang().get(p, "messages.noPermit"));
                } else {
                    plugin.getShm().buy(p, k, k.getName());
                }
            } else {
                sw.setParting(k.getId());
                p.sendMessage(plugin.getLang().get(p, "messages.selectParting").replaceAll("<parting>", k.getName()));
            }
            plugin.getUim().createPartingSelectorMenu(p);
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
        if (e.getView().getTitle().equals(plugin.getLang().get(p, "menus.lobby.title"))) {
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
            if (display.equals(plugin.getLang().get(p, "menus.lobby.soulwell.nameItem"))) {
                if (!plugin.getIjm().isSoulWellInjection()) {
                    p.sendMessage(plugin.getLang().get(p, "injections.soulwell"));
                    return;
                }
                plugin.getIjm().getSoulwell().getWel().createSoulWellMenu(p);
            }
            if (display.equals(plugin.getLang().get(p, "menus.lobby.levels.nameItem"))) {
                plugin.getGem().createLevelMenu(p);
            }
            if (display.equals(plugin.getLang().get(p, "menus.lobby.perksKits.nameItem"))) {
                plugin.getUim().openContentInventory(p, plugin.getUim().getMenus("kitsperks"));
            }
            if (display.equals(plugin.getLang().get(p, "menus.lobby.trails.nameItem"))) {
                plugin.getUim().getPages().put(p.getUniqueId(), 1);
                plugin.getUim().createTrailsSelectorMenu(p);
            }
            if (display.equals(plugin.getLang().get(p, "menus.lobby.taunts.nameItem"))) {
                plugin.getUim().getPages().put(p.getUniqueId(), 1);
                plugin.getUim().createTauntsSelectorMenu(p);
            }
            if (display.equals(plugin.getLang().get(p, "menus.lobby.parting.nameItem"))) {
                plugin.getUim().getPages().put(p.getUniqueId(), 1);
                plugin.getUim().createPartingSelectorMenu(p);
            }
            if (display.equals(plugin.getLang().get(p, "menus.lobby.balloons.nameItem"))) {
                plugin.getUim().getPages().put(p.getUniqueId(), 1);
                plugin.getUim().createBalloonSelectorMenu(p);
            }
            if (display.equals(plugin.getLang().get(p, "menus.lobby.glass.nameItem"))) {
                plugin.getUim().getPages().put(p.getUniqueId(), 1);
                plugin.getUim().createGlassSelectorMenu(p);
            }
            if (display.equals(plugin.getLang().get(p, "menus.lobby.wineffects.nameItem"))) {
                plugin.getUim().getPages().put(p.getUniqueId(), 1);
                plugin.getUim().createWinEffectSelectorMenu(p);
            }
            if (display.equals(plugin.getLang().get(p, "menus.lobby.killeffects.nameItem"))) {
                plugin.getUim().getPages().put(p.getUniqueId(), 1);
                plugin.getUim().createKillEffectSelectorMenu(p);
            }
            if (display.equals(plugin.getLang().get(p, "menus.lobby.windances.nameItem"))) {
                plugin.getUim().getPages().put(p.getUniqueId(), 1);
                plugin.getUim().createWinDanceSelectorMenu(p);
            }
            if (display.equals(plugin.getLang().get(p, "menus.lobby.killsound.nameItem"))) {
                plugin.getUim().getPages().put(p.getUniqueId(), 1);
                plugin.getUim().createKillSoundSelectorMenu(p);
            }
        }
        if (e.getView().getTitle().equals(plugin.getLang().get(p, "menus.trailsselector.title"))) {
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
            if (display.equals(plugin.getLang().get(p, "menus.next.nameItem"))) {
                plugin.getUim().addPage(p);
                plugin.getUim().createTrailsSelectorMenu(p);
                return;
            }
            if (display.equals(plugin.getLang().get(p, "menus.last.nameItem"))) {
                plugin.getUim().removePage(p);
                plugin.getUim().createTrailsSelectorMenu(p);
                return;
            }
            SWPlayer sw = plugin.getDb().getSWPlayer(p);
            if (display.equals(plugin.getLang().get(p, "menus.trailsselector.kit.nameItem"))) {
                return;
            }
            if (display.equals(plugin.getLang().get(p, "menus.trailsselector.deselect.nameItem"))) {
                if (sw.getTrail() == 999999) {
                    p.sendMessage(plugin.getLang().get(p, "messages.noSelect"));
                    return;
                }
                sw.setTrail(999999);
                p.sendMessage(plugin.getLang().get(p, "messages.deselect"));
                plugin.getUim().createTrailsSelectorMenu(p);
                return;
            }
            if (display.equals(plugin.getLang().get(p, "menus.trailsselector.close.nameItem"))) {
                if (e.getClick().equals(ClickType.RIGHT)) {
                    p.sendMessage(plugin.getLang().get(p, "messages.closeWithClick"));
                    return;
                }
                p.closeInventory();
                return;
            }
            Trail k = plugin.getCos().getTrailByItem(item);
            if (k == null) {
                return;
            }
            if (p.hasPermission(k.getAutoGivePermission())) {
                sw.setTrail(k.getId());
                p.sendMessage(plugin.getLang().get(p, "messages.selectTrail").replaceAll("<trail>", k.getName()));
                plugin.getUim().createTrailsSelectorMenu(p);
                return;
            }
            if (!sw.getTrails().contains(k.getId())) {
                if (k.needPermToBuy() && !p.hasPermission(k.getPermission())) {
                    p.sendMessage(plugin.getLang().get(p, "messages.noPermit"));
                } else {
                    plugin.getShm().buy(p, k, k.getName());
                }
            } else {
                sw.setTrail(k.getId());
                p.sendMessage(plugin.getLang().get(p, "messages.selectTrail").replaceAll("<trail>", k.getName()));
            }
            plugin.getUim().createTrailsSelectorMenu(p);
        }
        if (e.getView().getTitle().equals(plugin.getLang().get(p, "menus.tauntsselector.title"))) {
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
            if (display.equals(plugin.getLang().get(p, "menus.next.nameItem"))) {
                plugin.getUim().addPage(p);
                plugin.getUim().createTauntsSelectorMenu(p);
                return;
            }
            if (display.equals(plugin.getLang().get(p, "menus.last.nameItem"))) {
                plugin.getUim().removePage(p);
                plugin.getUim().createTauntsSelectorMenu(p);
                return;
            }
            if (display.equals(plugin.getLang().get(p, "menus.tauntsselector.kit.nameItem"))) {
                return;
            }
            if (display.equals(plugin.getLang().get(p, "menus.tauntsselector.deselect.nameItem"))) {
                if (sw.getTaunt() == 999999) {
                    p.sendMessage(plugin.getLang().get(p, "messages.noSelect"));
                    return;
                }
                sw.setTaunt(999999);
                p.sendMessage(plugin.getLang().get(p, "messages.deselect"));
                plugin.getUim().createTauntsSelectorMenu(p);
                return;
            }
            if (display.equals(plugin.getLang().get(p, "menus.tauntsselector.close.nameItem"))) {
                if (e.getClick().equals(ClickType.RIGHT)) {
                    p.sendMessage(plugin.getLang().get(p, "messages.closeWithClick"));
                    return;
                }
                p.closeInventory();
                return;
            }
            Taunt k = plugin.getCos().getTauntByItem(item);
            if (k == null) {
                return;
            }
            if (e.getClick().equals(ClickType.RIGHT)) {
                k.executePreview(p, plugin);
                return;
            }
            if (p.hasPermission(k.getAutoGivePermission())) {
                sw.setTaunt(k.getId());
                p.sendMessage(plugin.getLang().get(p, "messages.selectTaunt").replaceAll("<taunt>", k.getName()));
                plugin.getUim().createTauntsSelectorMenu(p);
                return;
            }
            if (!sw.getTaunts().contains(k.getId())) {
                if (k.needPermToBuy() && !p.hasPermission(k.getPermission())) {
                    p.sendMessage(plugin.getLang().get(p, "messages.noPermit"));
                } else {
                    plugin.getShm().buy(p, k, k.getName());
                }
            } else {
                sw.setTaunt(k.getId());
                p.sendMessage(plugin.getLang().get(p, "messages.selectTaunt").replaceAll("<taunt>", k.getName()));
            }
            plugin.getUim().createTauntsSelectorMenu(p);
        }
        if (e.getView().getTitle().equals(plugin.getLang().get(p, "menus.glassselector.title"))) {
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
            if (display.equals(plugin.getLang().get(p, "menus.next.nameItem"))) {
                plugin.getUim().addPage(p);
                plugin.getUim().createGlassSelectorMenu(p);
                return;
            }
            if (display.equals(plugin.getLang().get(p, "menus.last.nameItem"))) {
                plugin.getUim().removePage(p);
                plugin.getUim().createGlassSelectorMenu(p);
                return;
            }
            if (display.equals(plugin.getLang().get(p, "menus.glassselector.kit.nameItem"))) {
                return;
            }
            if (display.equals(plugin.getLang().get(p, "menus.glassselector.deselect.nameItem"))) {
                if (sw.getGlass() == 999999) {
                    p.sendMessage(plugin.getLang().get(p, "messages.noSelect"));
                    return;
                }
                sw.setGlass(999999);
                p.sendMessage(plugin.getLang().get(p, "messages.deselect"));
                plugin.getUim().createGlassSelectorMenu(p);
                return;
            }
            if (display.equals(plugin.getLang().get(p, "menus.glassselector.close.nameItem"))) {
                if (e.getClick().equals(ClickType.RIGHT)) {
                    p.sendMessage(plugin.getLang().get(p, "messages.closeWithClick"));
                    return;
                }
                p.closeInventory();
                return;
            }
            Glass k = plugin.getCos().getGlassByItem(item);
            if (k == null) {
                return;
            }
            if (e.getClick().equals(ClickType.RIGHT)) {
                if (plugin.getCos().getPreviewCosmetic("glass") == null) {
                    p.sendMessage(plugin.getLang().get("setup.noGlassPreview"));
                    return;
                }
                if (plugin.getCos().getPlayerPreview().containsKey(p.getUniqueId())) {
                    p.sendMessage(plugin.getLang().get("setup.alreadyPreview"));
                    return;
                }
                p.closeInventory();
                PreviewCosmetic pc = plugin.getCos().getPreviewCosmetic("glass");
                plugin.getCos().getPlayerPreview().put(p.getUniqueId(), "glass");
                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    pc.addPreview(p);
                    pc.execute(p, k.getId());
                }, 1L);
                return;
            }
            if (p.hasPermission(k.getAutoGivePermission())) {
                sw.setGlass(k.getId());
                p.sendMessage(plugin.getLang().get(p, "messages.selectGlass").replaceAll("<glass>", k.getName()));
                plugin.getUim().createGlassSelectorMenu(p);
                return;
            }
            if (!sw.getGlasses().contains(k.getId())) {
                if (k.needPermToBuy() && !p.hasPermission(k.getPermission())) {
                    p.sendMessage(plugin.getLang().get(p, "messages.noPermit"));
                } else {
                    plugin.getShm().buy(p, k, k.getName());
                }
            } else {
                sw.setGlass(k.getId());
                p.sendMessage(plugin.getLang().get(p, "messages.selectGlass").replaceAll("<glass>", k.getName()));
            }
            plugin.getUim().createGlassSelectorMenu(p);
        }
        if (e.getView().getTitle().equals(plugin.getLang().get(p, "menus.balloonsselector.title"))) {
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
            if (display.equals(plugin.getLang().get(p, "menus.next.nameItem"))) {
                plugin.getUim().addPage(p);
                plugin.getUim().createBalloonSelectorMenu(p);
                return;
            }
            if (display.equals(plugin.getLang().get(p, "menus.last.nameItem"))) {
                plugin.getUim().removePage(p);
                plugin.getUim().createBalloonSelectorMenu(p);
                return;
            }
            if (display.equals(plugin.getLang().get(p, "menus.balloonsselector.kit.nameItem"))) {
                return;
            }
            if (display.equals(plugin.getLang().get(p, "menus.balloonsselector.deselect.nameItem"))) {
                if (sw.getBalloon() == 999999) {
                    p.sendMessage(plugin.getLang().get(p, "messages.noSelect"));
                    return;
                }
                sw.setBalloon(999999);
                p.sendMessage(plugin.getLang().get(p, "messages.deselect"));
                plugin.getUim().createBalloonSelectorMenu(p);
                return;
            }
            if (display.equals(plugin.getLang().get(p, "menus.balloonsselector.close.nameItem"))) {
                if (e.getClick().equals(ClickType.RIGHT)) {
                    p.sendMessage(plugin.getLang().get(p, "messages.closeWithClick"));
                    return;
                }
                p.closeInventory();
                return;
            }
            Balloon k = plugin.getCos().getBalloonByItem(item);
            if (k == null) {
                return;
            }
            if (e.getClick().equals(ClickType.RIGHT)) {
                if (plugin.getCos().getPreviewCosmetic("balloon") == null) {
                    p.sendMessage(plugin.getLang().get("setup.noBalloonPreview"));
                    return;
                }
                if (plugin.getCos().getPlayerPreview().containsKey(p.getUniqueId())) {
                    p.sendMessage(plugin.getLang().get("setup.alreadyPreview"));
                    return;
                }
                p.closeInventory();
                PreviewCosmetic pc = plugin.getCos().getPreviewCosmetic("balloon");
                plugin.getCos().getPlayerPreview().put(p.getUniqueId(), "balloon");
                pc.addPreview(p);
                pc.execute(p, k.getId());
                return;
            }
            if (p.hasPermission(k.getAutoGivePermission())) {
                sw.setBalloon(k.getId());
                p.sendMessage(plugin.getLang().get(p, "messages.selectBalloon").replaceAll("<balloon>", k.getName()));
                plugin.getUim().createBalloonSelectorMenu(p);
                return;
            }
            if (!sw.getBalloons().contains(k.getId())) {
                if (k.needPermToBuy() && !p.hasPermission(k.getPermission())) {
                    p.sendMessage(plugin.getLang().get(p, "messages.noPermit"));
                } else {
                    plugin.getShm().buy(p, k, k.getName());
                }
            } else {
                sw.setBalloon(k.getId());
                p.sendMessage(plugin.getLang().get(p, "messages.selectBalloon").replaceAll("<balloon>", k.getName()));
            }
            plugin.getUim().createBalloonSelectorMenu(p);
        }
        if (e.getView().getTitle().equals(plugin.getLang().get(p, "menus.killsoundsselector.title"))) {
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
            if (display.equals(plugin.getLang().get(p, "menus.next.nameItem"))) {
                plugin.getUim().addPage(p);
                plugin.getUim().createKillSoundSelectorMenu(p);
                return;
            }
            if (display.equals(plugin.getLang().get(p, "menus.last.nameItem"))) {
                plugin.getUim().removePage(p);
                plugin.getUim().createKillSoundSelectorMenu(p);
                return;
            }
            if (display.equals(plugin.getLang().get(p, "menus.killsoundsselector.kit.nameItem"))) {
                return;
            }
            if (display.equals(plugin.getLang().get(p, "menus.killsoundsselector.deselect.nameItem"))) {
                if (sw.getKillSound() == 999999) {
                    p.sendMessage(plugin.getLang().get(p, "messages.noSelect"));
                    return;
                }
                sw.setKillSound(999999);
                p.sendMessage(plugin.getLang().get(p, "messages.deselect"));
                plugin.getUim().createKillSoundSelectorMenu(p);
                return;
            }
            if (display.equals(plugin.getLang().get(p, "menus.killsoundsselector.close.nameItem"))) {
                if (e.getClick().equals(ClickType.RIGHT)) {
                    p.sendMessage(plugin.getLang().get(p, "messages.closeWithClick"));
                    return;
                }
                p.closeInventory();
                return;
            }
            KillSound k = plugin.getCos().getKillSoundByItem(item);
            if (k == null) {
                return;
            }
            if (e.getClick().equals(ClickType.RIGHT)) {
                p.playSound(p.getLocation(), k.getSound(), k.getVol1(), k.getVol2());
                return;
            }
            if (p.hasPermission(k.getAutoGivePermission())) {
                sw.setKillSound(k.getId());
                p.sendMessage(plugin.getLang().get(p, "messages.selectKillSound").replaceAll("<killsound>", k.getName()));
                plugin.getUim().createKillSoundSelectorMenu(p);
                return;
            }
            if (!sw.getKillsounds().contains(k.getId())) {
                if (k.needPermToBuy() && !p.hasPermission(k.getPermission())) {
                    p.sendMessage(plugin.getLang().get(p, "messages.noPermit"));
                } else {
                    plugin.getShm().buy(p, k, k.getName());
                }
            } else {
                sw.setKillSound(k.getId());
                p.sendMessage(plugin.getLang().get(p, "messages.selectKillSound").replaceAll("<killsound>", k.getName()));
            }
            plugin.getUim().createKillSoundSelectorMenu(p);
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
                plugin.getUim().createKitSelectorMenu(p, "SOLO", false);
            }
            if (display.equals(plugin.getLang().get(p, "menus.kitsperks.kitteam.nameItem"))) {
                plugin.getUim().getPages().put(p.getUniqueId(), 1);
                plugin.getUim().createKitSelectorMenu(p, "TEAM", false);
            }
            if (display.equals(plugin.getLang().get(p, "menus.kitsperks.kitranked.nameItem"))) {
                plugin.getUim().getPages().put(p.getUniqueId(), 1);
                plugin.getUim().createKitSelectorMenu(p, "RANKED", false);
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
        if (e.getView().getTitle().equals(plugin.getLang().get(p, "menus.wineffectsselector.title"))) {
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
            if (display.equals(plugin.getLang().get(p, "menus.next.nameItem"))) {
                plugin.getUim().addPage(p);
                plugin.getUim().createWinEffectSelectorMenu(p);
                return;
            }
            if (display.equals(plugin.getLang().get(p, "menus.last.nameItem"))) {
                plugin.getUim().removePage(p);
                plugin.getUim().createWinEffectSelectorMenu(p);
                return;
            }
            if (display.equals(plugin.getLang().get(p, "menus.wineffectsselector.kit.nameItem"))) {
                return;
            }
            if (display.equals(plugin.getLang().get(p, "menus.wineffectsselector.deselect.nameItem"))) {
                if (sw.getWinEffect() == 999999) {
                    p.sendMessage(plugin.getLang().get(p, "messages.noSelect"));
                    return;
                }
                sw.setWinEffect(999999);
                p.sendMessage(plugin.getLang().get(p, "messages.deselect"));
                plugin.getUim().createWinEffectSelectorMenu(p);
                return;
            }
            if (display.equals(plugin.getLang().get(p, "menus.wineffectsselector.close.nameItem"))) {
                if (e.getClick().equals(ClickType.RIGHT)) {
                    p.sendMessage(plugin.getLang().get(p, "messages.closeWithClick"));
                    return;
                }
                p.closeInventory();
                return;
            }
            UltraWinEffect k = plugin.getCos().getWinEffectByItem(item);
            if (k == null) {
                return;
            }
            if (p.hasPermission(k.getAutoGivePermission())) {
                sw.setWinEffect(k.getId());
                p.sendMessage(plugin.getLang().get(p, "messages.selectWinEffect").replaceAll("<wineffect>", k.getName()));
                plugin.getUim().createWinEffectSelectorMenu(p);
                return;
            }
            if (!sw.getWineffects().contains(k.getId())) {
                if (k.needPermToBuy() && !p.hasPermission(k.getPermission())) {
                    p.sendMessage(plugin.getLang().get(p, "messages.noPermit"));
                } else {
                    plugin.getShm().buy(p, k, k.getName());
                }
            } else {
                sw.setWinEffect(k.getId());
                p.sendMessage(plugin.getLang().get(p, "messages.selectWinEffect").replaceAll("<wineffect>", k.getName()));
            }
            plugin.getUim().createWinEffectSelectorMenu(p);
        }
        if (e.getView().getTitle().equals(plugin.getLang().get(p, "menus.killeffectsselector.title"))) {
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
            if (display.equals(plugin.getLang().get(p, "menus.next.nameItem"))) {
                plugin.getUim().addPage(p);
                plugin.getUim().createKillEffectSelectorMenu(p);
                return;
            }
            if (display.equals(plugin.getLang().get(p, "menus.last.nameItem"))) {
                plugin.getUim().removePage(p);
                plugin.getUim().createKillEffectSelectorMenu(p);
                return;
            }
            if (display.equals(plugin.getLang().get(p, "menus.killeffectsselector.kit.nameItem"))) {
                return;
            }
            if (display.equals(plugin.getLang().get(p, "menus.killeffectsselector.deselect.nameItem"))) {
                if (sw.getKillEffect() == 999999) {
                    p.sendMessage(plugin.getLang().get(p, "messages.noSelect"));
                    return;
                }
                sw.setKillEffect(999999);
                p.sendMessage(plugin.getLang().get(p, "messages.deselect"));
                plugin.getUim().createKillEffectSelectorMenu(p);
                return;
            }
            if (display.equals(plugin.getLang().get(p, "menus.killeffectsselector.close.nameItem"))) {
                if (e.getClick().equals(ClickType.RIGHT)) {
                    p.sendMessage(plugin.getLang().get(p, "messages.closeWithClick"));
                    return;
                }
                p.closeInventory();
                return;
            }
            UltraKillEffect k = plugin.getCos().getKillEffectByItem(item);
            if (k == null) {
                return;
            }
            if (p.hasPermission(k.getAutoGivePermission())) {
                sw.setKillEffect(k.getId());
                p.sendMessage(plugin.getLang().get(p, "messages.selectKillEffect").replaceAll("<killeffect>", k.getName()));
                plugin.getUim().createKillEffectSelectorMenu(p);
                return;
            }
            if (!sw.getKilleffects().contains(k.getId())) {
                if (k.needPermToBuy() && !p.hasPermission(k.getPermission())) {
                    p.sendMessage(plugin.getLang().get(p, "messages.noPermit"));
                } else {
                    plugin.getShm().buy(p, k, k.getName());
                }
            } else {
                sw.setKillEffect(k.getId());
                p.sendMessage(plugin.getLang().get(p, "messages.selectKillEffect").replaceAll("<killeffect>", k.getName()));
            }
            plugin.getUim().createKillEffectSelectorMenu(p);
        }
        if (e.getView().getTitle().equals(plugin.getLang().get(p, "menus.windancesselector.title"))) {
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
            if (display.equals(plugin.getLang().get(p, "menus.next.nameItem"))) {
                plugin.getUim().addPage(p);
                plugin.getUim().createWinDanceSelectorMenu(p);
                return;
            }
            if (display.equals(plugin.getLang().get(p, "menus.last.nameItem"))) {
                plugin.getUim().removePage(p);
                plugin.getUim().createWinDanceSelectorMenu(p);
                return;
            }
            if (display.equals(plugin.getLang().get(p, "menus.windancesselector.kit.nameItem"))) {
                return;
            }
            if (display.equals(plugin.getLang().get(p, "menus.windancesselector.deselect.nameItem"))) {
                if (sw.getWinDance() == 999999) {
                    p.sendMessage(plugin.getLang().get(p, "messages.noSelect"));
                    return;
                }
                sw.setWinDance(999999);
                p.sendMessage(plugin.getLang().get(p, "messages.deselect"));
                plugin.getUim().createWinDanceSelectorMenu(p);
                return;
            }
            if (display.equals(plugin.getLang().get(p, "menus.windancesselector.close.nameItem"))) {
                if (e.getClick().equals(ClickType.RIGHT)) {
                    p.sendMessage(plugin.getLang().get(p, "messages.closeWithClick"));
                    return;
                }
                p.closeInventory();
                return;
            }
            UltraWinDance k = plugin.getCos().getWinDanceByItem(item);
            if (k == null) {
                return;
            }
            if (p.hasPermission(k.getAutoGivePermission())) {
                sw.setWinDance(k.getId());
                p.sendMessage(plugin.getLang().get(p, "messages.selectWinDance").replaceAll("<windance>", k.getName()));
                plugin.getUim().createWinDanceSelectorMenu(p);
                return;
            }
            if (!sw.getWindances().contains(k.getId())) {
                if (k.needPermToBuy() && !p.hasPermission(k.getPermission())) {
                    p.sendMessage(plugin.getLang().get(p, "messages.noPermit"));
                } else {
                    plugin.getShm().buy(p, k, k.getName());
                }
            } else {
                sw.setWinDance(k.getId());
                p.sendMessage(plugin.getLang().get(p, "messages.selectWinDance").replaceAll("<windance>", k.getName()));
            }
            plugin.getUim().createWinDanceSelectorMenu(p);
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
            String type = NBTEditor.getString(item, "KIT", "SELECTOR", "TYPE");
            if (type == null) return;
            if (display.equals(plugin.getLang().get(p, "menus.back.nameItem"))) {
                plugin.getUim().createKitSelectorMenu(p, type, false);
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
                plugin.getUim().createKitSelectorMenu(p, type, false);
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
            plugin.getUim().createKitSelectorMenu(p, type, false);
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
            if (display.equals(plugin.getLang().get(p, "menus.next.nameItem"))) {
                plugin.getUim().addPage(p);
                plugin.getUim().createKitSelectorMenu(p, type, false);
                return;
            }
            if (display.equals(plugin.getLang().get(p, "menus.last.nameItem"))) {
                plugin.getUim().removePage(p);
                plugin.getUim().createKitSelectorMenu(p, type, false);
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
                plugin.getUim().createKitSelectorMenu(p, type, false);
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
                    plugin.getUim().createKitSelectorMenu(p, type, false);
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
                plugin.getUim().createKitSelectorMenu(p, type, false);
            } else {
                plugin.getUim().createKitLevelSelectorMenu(p, k, type);
            }
        }
    }

    private void createMenuGames(Player p, String gameType, UltraSkyWars plugin) {
        plugin.getGem().createSelectorMenu(p, "none", gameType.toLowerCase());
    }

}