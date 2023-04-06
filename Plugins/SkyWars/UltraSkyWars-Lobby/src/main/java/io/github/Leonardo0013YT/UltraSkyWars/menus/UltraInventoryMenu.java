package io.github.Leonardo0013YT.UltraSkyWars.menus;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.cosmetics.*;
import io.github.Leonardo0013YT.UltraSkyWars.cosmetics.killeffects.UltraKillEffect;
import io.github.Leonardo0013YT.UltraSkyWars.cosmetics.kits.Kit;
import io.github.Leonardo0013YT.UltraSkyWars.cosmetics.kits.KitLevel;
import io.github.Leonardo0013YT.UltraSkyWars.cosmetics.taunts.Taunt;
import io.github.Leonardo0013YT.UltraSkyWars.cosmetics.windances.UltraWinDance;
import io.github.Leonardo0013YT.UltraSkyWars.cosmetics.wineffects.UltraWinEffect;
import io.github.Leonardo0013YT.UltraSkyWars.data.SWPlayer;
import io.github.Leonardo0013YT.UltraSkyWars.inventories.KitsPerksMenu;
import io.github.Leonardo0013YT.UltraSkyWars.inventories.LobbyShopMenu;
import io.github.Leonardo0013YT.UltraSkyWars.inventories.SoulWellMenuRolling;
import io.github.Leonardo0013YT.UltraSkyWars.inventories.selectors.*;
import io.github.Leonardo0013YT.UltraSkyWars.superclass.UltraInventory;
import io.github.Leonardo0013YT.UltraSkyWars.utils.ItemBuilder;
import io.github.Leonardo0013YT.UltraSkyWars.utils.NBTEditor;
import io.github.Leonardo0013YT.UltraSkyWars.xseries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UltraInventoryMenu {

    private HashMap<String, UltraInventory> menus = new HashMap<>();
    private HashMap<UUID, Integer> pages = new HashMap<>();
    private UltraSkyWars plugin;

    public UltraInventoryMenu(UltraSkyWars plugin) {
        this.plugin = plugin;
        loadMenus();
    }

    public void loadMenus() {
        menus.clear();
        menus.put("kitsperks", new KitsPerksMenu(plugin, "kitsperks"));
        menus.put("lobby", new LobbyShopMenu(plugin, "lobby"));
        menus.put("soulwellmenu", new SoulWellMenuRolling(plugin, "soulwellmenu"));
        menus.put("balloonsselector", new BalloonSelectorMenu(plugin, "balloonsselector"));
        menus.put("glassselector", new GlassSelectorMenu(plugin, "glassselector"));
        menus.put("tauntsselector", new TauntsSelectorMenu(plugin, "tauntsselector"));
        menus.put("trailsselector", new TrailsSelectorMenu(plugin, "trailsselector"));
        menus.put("partingselector", new PartingSelectorMenu(plugin, "partingselector"));
        menus.put("killsoundsselector", new KillSoundsSelectorMenu(plugin, "killsoundsselector"));
        menus.put("killeffectsselector", new KillEffectsSelectorMenu(plugin, "killeffectsselector"));
        menus.put("wineffectsselector", new WinEffectsSelectorMenu(plugin, "wineffectsselector"));
        menus.put("windancesselector", new WinDancesSelectorMenu(plugin, "windancesselector"));
        menus.put("gamesoloselector", new GameSelectorMenu(plugin, "gamesoloselector", "solo"));
        menus.put("gameteamselector", new GameSelectorMenu(plugin, "gameteamselector", "team"));
        menus.put("gamerankedselector", new GameSelectorMenu(plugin, "gamerankedselector", "ranked"));
        menus.put("gameallselector", new GameSelectorMenu(plugin, "gameallselector", "all"));
    }

    public UltraInventory getMenus(String t) {
        return menus.get(t);
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

    public void openInventory(Player p, UltraInventory i, String[]... t) {
        Inventory inv = Bukkit.createInventory(null, i.getRows() * 9, i.getTitle());
        for (Map.Entry<Integer, ItemStack> entry : i.getContents().entrySet()) {
            Integer s = entry.getKey();
            ItemStack it = entry.getValue().clone();
            inv.setItem(s, ItemBuilder.parseVariables(p, it, t));
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

    public void createPartingSelectorMenu(Player p) {
        int page = pages.get(p.getUniqueId());
        int size = plugin.getCm().getRInvParting() * 9;
        PartingSelectorMenu selector = (PartingSelectorMenu) getMenus("partingselector");
        Inventory inv = Bukkit.createInventory(null, size, plugin.getLang().get(p, "menus.partingselector.title"));
        for (int s : selector.getExtra()) {
            inv.setItem(s, selector.getContents().get(s));
        }
        SWPlayer sw = plugin.getDb().getSWPlayer(p);
        for (Parting k : plugin.getCos().getPartings().values()) {
            if (k.getId() == sw.getParting()) {
                ItemStack i = k.getIcon(p);
                ItemStack kit = ItemBuilder.nameLore(i.clone(), plugin.getLang().get(p, "menus.partingselector.parting.nameItem"), plugin.getLang().get(p, "menus.partingselector.parting.loreItem"));
                int s = selector.getSlot("{SELECTED}");
                if (s > -1 && s < 54) {
                    inv.setItem(s, kit);
                }
                if (k.getPage() != page) continue;
                inv.setItem(k.getSlot(), i);
            } else {
                if (k.getPage() != page) continue;
                inv.setItem(k.getSlot(), k.getIcon(p));
            }
        }
        if (sw.getParting() != 999999) {
            int s = selector.getSlot("{DESELECT}");
            if (s > -1 && s < 54) {
                inv.setItem(s, selector.getContents().get(s));
            }
        }
        if (page > 1) {
            int s = selector.getSlot("{LAST}");
            if (s > -1 && s < 54) {
                inv.setItem(s, selector.getContents().get(s));
            }
        }
        if (page < plugin.getCos().getLastPage("Parting")) {
            int s = selector.getSlot("{NEXT}");
            if (s > -1 && s < 54) {
                inv.setItem(s, selector.getContents().get(s));
            }
        }
        int s = selector.getSlot("{CLOSE}");
        if (s > -1 && s < 54) {
            inv.setItem(s, selector.getContents().get(s));
        }
        p.openInventory(inv);
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
            inv.setItem(size - 5, close);
        } else {
            inv.setItem(size - 5, NBTEditor.set(back, type, "KIT", "SELECTOR", "TYPE"));
        }
        p.openInventory(inv);
    }

    public void createTrailsSelectorMenu(Player p) {
        int page = pages.get(p.getUniqueId());
        int size = plugin.getCm().getRInvTrails() * 9;
        TrailsSelectorMenu selector = (TrailsSelectorMenu) getMenus("trailsselector");
        Inventory inv = Bukkit.createInventory(null, size, plugin.getLang().get(p, "menus.trailsselector.title"));
        for (int s : selector.getExtra()) {
            inv.setItem(s, selector.getContents().get(s));
        }
        SWPlayer sw = plugin.getDb().getSWPlayer(p);
        for (Trail k : plugin.getCos().getTrails().values()) {
            if (k.getId() == sw.getTrail()) {
                ItemStack i = k.getIcon(p);
                ItemStack kit = ItemBuilder.nameLore(i.clone(), plugin.getLang().get(p, "menus.trailsselector.trail.nameItem"), plugin.getLang().get(p, "menus.trailsselector.trail.loreItem"));
                int s = selector.getSlot("{SELECTED}");
                if (s > -1 && s < 54) {
                    inv.setItem(s, kit);
                }
                if (k.getPage() != page) continue;
                inv.setItem(k.getSlot(), i);
            } else {
                if (k.getPage() != page) continue;
                inv.setItem(k.getSlot(), k.getIcon(p));
            }
        }
        if (sw.getTrail() != 999999) {
            int s = selector.getSlot("{DESELECT}");
            if (s > -1 && s < 54) {
                inv.setItem(s, selector.getContents().get(s));
            }
        }
        if (page > 1) {
            int s = selector.getSlot("{LAST}");
            if (s > -1 && s < 54) {
                inv.setItem(s, selector.getContents().get(s));
            }
        }
        if (page < plugin.getCos().getLastPage("Trail")) {
            int s = selector.getSlot("{NEXT}");
            if (s > -1 && s < 54) {
                inv.setItem(s, selector.getContents().get(s));
            }
        }
        int s = selector.getSlot("{CLOSE}");
        if (s > -1 && s < 54) {
            inv.setItem(s, selector.getContents().get(s));
        }
        p.openInventory(inv);
    }

    public void createTauntsSelectorMenu(Player p) {
        int page = pages.get(p.getUniqueId());
        int size = plugin.getCm().getRInvTaunts() * 9;
        TauntsSelectorMenu selector = (TauntsSelectorMenu) getMenus("tauntsselector");
        Inventory inv = Bukkit.createInventory(null, size, plugin.getLang().get(p, "menus.tauntsselector.title"));
        for (int s : selector.getExtra()) {
            inv.setItem(s, selector.getContents().get(s));
        }
        SWPlayer sw = plugin.getDb().getSWPlayer(p);
        for (Taunt k : plugin.getCos().getTaunts().values()) {
            if (k.getId() == sw.getTaunt()) {
                ItemStack i = k.getIcon(p);
                ItemStack kit = ItemBuilder.nameLore(i.clone(), plugin.getLang().get(p, "menus.tauntsselector.taunt.nameItem"), plugin.getLang().get(p, "menus.tauntsselector.taunt.loreItem"));
                int s = selector.getSlot("{SELECTED}");
                if (s > -1 && s < 54) {
                    inv.setItem(s, kit);
                }
                if (k.getPage() != page) continue;
                inv.setItem(k.getSlot(), i);
            } else {
                if (k.getPage() != page) continue;
                inv.setItem(k.getSlot(), k.getIcon(p));
            }
        }
        if (sw.getTaunt() != 999999) {
            int s = selector.getSlot("{DESELECT}");
            if (s > -1 && s < 54) {
                inv.setItem(s, selector.getContents().get(s));
            }
        }
        if (page > 1) {
            int s = selector.getSlot("{LAST}");
            if (s > -1 && s < 54) {
                inv.setItem(s, selector.getContents().get(s));
            }
        }
        if (page < plugin.getCos().getLastPage("Taunt")) {
            int s = selector.getSlot("{NEXT}");
            if (s > -1 && s < 54) {
                inv.setItem(s, selector.getContents().get(s));
            }
        }
        int s = selector.getSlot("{CLOSE}");
        if (s > -1 && s < 54) {
            inv.setItem(s, selector.getContents().get(s));
        }
        p.openInventory(inv);
    }

    public void createGlassSelectorMenu(Player p) {
        int page = pages.get(p.getUniqueId());
        int size = plugin.getCm().getRInvGlasses() * 9;
        GlassSelectorMenu selector = (GlassSelectorMenu) getMenus("glassselector");
        Inventory inv = Bukkit.createInventory(null, size, plugin.getLang().get(p, "menus.glassselector.title"));
        for (int s : selector.getExtra()) {
            inv.setItem(s, selector.getContents().get(s));
        }
        SWPlayer sw = plugin.getDb().getSWPlayer(p);
        for (Glass k : plugin.getCos().getGlasses().values()) {
            if (k.getId() == sw.getGlass()) {
                ItemStack i = k.getIcon(p);
                ItemStack kit = ItemBuilder.nameLore(i.clone(), plugin.getLang().get(p, "menus.glassselector.glass.nameItem"), plugin.getLang().get(p, "menus.glassselector.glass.loreItem"));
                int s = selector.getSlot("{SELECTED}");
                if (s > -1 && s < 54) {
                    inv.setItem(s, kit);
                }
                if (k.getPage() != page) continue;
                inv.setItem(k.getSlot(), i);
            } else {
                if (k.getPage() != page) continue;
                inv.setItem(k.getSlot(), k.getIcon(p));
            }
        }
        if (sw.getGlass() != 999999) {
            int s = selector.getSlot("{DESELECT}");
            if (s > -1 && s < 54) {
                inv.setItem(s, selector.getContents().get(s));
            }
        }
        if (page > 1) {
            int s = selector.getSlot("{LAST}");
            if (s > -1 && s < 54) {
                inv.setItem(s, selector.getContents().get(s));
            }
        }
        if (page < plugin.getCos().getLastPage("Glass")) {
            int s = selector.getSlot("{NEXT}");
            if (s > -1 && s < 54) {
                inv.setItem(s, selector.getContents().get(s));
            }
        }
        int s = selector.getSlot("{CLOSE}");
        if (s > -1 && s < 54) {
            inv.setItem(s, selector.getContents().get(s));
        }
        p.openInventory(inv);
    }

    public void createBalloonSelectorMenu(Player p) {
        int page = pages.get(p.getUniqueId());
        int size = plugin.getCm().getRInvBalloons() * 9;
        BalloonSelectorMenu selector = (BalloonSelectorMenu) getMenus("balloonsselector");
        Inventory inv = Bukkit.createInventory(null, size, plugin.getLang().get(p, "menus.balloonsselector.title"));
        for (int s : selector.getExtra()) {
            inv.setItem(s, selector.getContents().get(s));
        }
        SWPlayer sw = plugin.getDb().getSWPlayer(p);
        for (Balloon k : plugin.getCos().getBalloons().values()) {
            if (k.getId() == sw.getBalloon()) {
                ItemStack i = k.getIcon(p);
                ItemStack kit = ItemBuilder.createSkull(plugin.getLang().get(p, "menus.balloonsselector.balloon.nameItem"), plugin.getLang().get(p, "menus.balloonsselector.balloon.loreItem"), k.getUrl());
                int s = selector.getSlot("{SELECTED}");
                if (s > -1 && s < 54) {
                    inv.setItem(s, kit);
                }
                if (k.getPage() != page) continue;
                inv.setItem(k.getSlot(), i);
            } else {
                if (k.getPage() != page) continue;
                inv.setItem(k.getSlot(), k.getIcon(p));
            }
        }
        if (sw.getBalloon() != 999999) {
            int s = selector.getSlot("{DESELECT}");
            if (s > -1 && s < 54) {
                inv.setItem(s, selector.getContents().get(s));
            }
        }
        if (page > 1) {
            int s = selector.getSlot("{LAST}");
            if (s > -1 && s < 54) {
                inv.setItem(s, selector.getContents().get(s));
            }
        }
        if (page < plugin.getCos().getLastPage("Balloon")) {
            int s = selector.getSlot("{NEXT}");
            if (s > -1 && s < 54) {
                inv.setItem(s, selector.getContents().get(s));
            }
        }
        int s = selector.getSlot("{CLOSE}");
        if (s > -1 && s < 54) {
            inv.setItem(s, selector.getContents().get(s));
        }
        p.openInventory(inv);
    }

    public void createKillSoundSelectorMenu(Player p) {
        int page = pages.get(p.getUniqueId());
        int size = plugin.getCm().getRInvKillSounds() * 9;
        KillSoundsSelectorMenu selector = (KillSoundsSelectorMenu) getMenus("killsoundsselector");
        Inventory inv = Bukkit.createInventory(null, size, plugin.getLang().get(p, "menus.killsoundsselector.title"));
        for (int s : selector.getExtra()) {
            inv.setItem(s, selector.getContents().get(s));
        }
        SWPlayer sw = plugin.getDb().getSWPlayer(p);
        for (KillSound k : plugin.getCos().getKillSounds().values()) {
            if (k.getId() == sw.getKillSound()) {
                ItemStack i = k.getIcon(p);
                ItemStack kit = ItemBuilder.nameLore(i.clone(), plugin.getLang().get(p, "menus.killsoundsselector.killsound.nameItem"), plugin.getLang().get(p, "menus.killsoundsselector.killsound.loreItem"));
                int s = selector.getSlot("{SELECTED}");
                if (s > -1 && s < 54) {
                    inv.setItem(s, kit);
                }
                if (k.getPage() != page) continue;
                inv.setItem(k.getSlot(), i);
            } else {
                if (k.getPage() != page) continue;
                inv.setItem(k.getSlot(), k.getIcon(p));
            }
        }
        if (sw.getKillSound() != 999999) {
            int s = selector.getSlot("{DESELECT}");
            if (s > -1 && s < 54) {
                inv.setItem(s, selector.getContents().get(s));
            }
        }
        if (page > 1) {
            int s = selector.getSlot("{LAST}");
            if (s > -1 && s < 54) {
                inv.setItem(s, selector.getContents().get(s));
            }
        }
        if (page < plugin.getCos().getLastPage("KillSound")) {
            int s = selector.getSlot("{NEXT}");
            if (s > -1 && s < 54) {
                inv.setItem(s, selector.getContents().get(s));
            }
        }
        int s = selector.getSlot("{CLOSE}");
        if (s > -1 && s < 54) {
            inv.setItem(s, selector.getContents().get(s));
        }
        p.openInventory(inv);
    }

    public void createKillEffectSelectorMenu(Player p) {
        int page = pages.get(p.getUniqueId());
        int size = plugin.getCm().getRInvKillEffects() * 9;
        KillEffectsSelectorMenu selector = (KillEffectsSelectorMenu) getMenus("killeffectsselector");
        Inventory inv = Bukkit.createInventory(null, size, plugin.getLang().get(p, "menus.killeffectsselector.title"));
        for (int s : selector.getExtra()) {
            inv.setItem(s, selector.getContents().get(s));
        }
        SWPlayer sw = plugin.getDb().getSWPlayer(p);
        for (UltraKillEffect k : plugin.getCos().getKillEffect().values()) {
            if (k.getId() == sw.getKillEffect()) {
                ItemStack i = k.getIcon(p);
                ItemStack kit = ItemBuilder.nameLore(i.clone(), plugin.getLang().get(p, "menus.killeffectsselector.killeffect.nameItem"), plugin.getLang().get(p, "menus.killeffectsselector.killeffect.loreItem"));
                int s = selector.getSlot("{SELECTED}");
                if (s > -1 && s < 54) {
                    inv.setItem(s, kit);
                }
                if (k.getPage() != page) continue;
                inv.setItem(k.getSlot(), i);
            } else {
                if (k.getPage() != page) continue;
                inv.setItem(k.getSlot(), k.getIcon(p));
            }
        }
        if (sw.getKillEffect() != 999999) {
            int s = selector.getSlot("{DESELECT}");
            if (s > -1 && s < 54) {
                inv.setItem(s, selector.getContents().get(s));
            }
        }
        if (page > 1) {
            int s = selector.getSlot("{LAST}");
            if (s > -1 && s < 54) {
                inv.setItem(s, selector.getContents().get(s));
            }
        }
        if (page < plugin.getCos().getLastPage("KillEffect")) {
            int s = selector.getSlot("{NEXT}");
            if (s > -1 && s < 54) {
                inv.setItem(s, selector.getContents().get(s));
            }
        }
        int s = selector.getSlot("{CLOSE}");
        if (s > -1 && s < 54) {
            inv.setItem(s, selector.getContents().get(s));
        }
        p.openInventory(inv);
    }

    public void createWinEffectSelectorMenu(Player p) {
        int page = pages.get(p.getUniqueId());
        int size = plugin.getCm().getRInvWinEffects() * 9;
        WinEffectsSelectorMenu selector = (WinEffectsSelectorMenu) getMenus("wineffectsselector");
        Inventory inv = Bukkit.createInventory(null, size, plugin.getLang().get(p, "menus.wineffectsselector.title"));
        for (int s : selector.getExtra()) {
            inv.setItem(s, selector.getContents().get(s));
        }
        SWPlayer sw = plugin.getDb().getSWPlayer(p);
        for (UltraWinEffect k : plugin.getCos().getWinEffects().values()) {
            if (k.getId() == sw.getWinEffect()) {
                ItemStack i = k.getIcon(p);
                ItemStack kit = ItemBuilder.nameLore(i.clone(), plugin.getLang().get(p, "menus.wineffectsselector.wineffect.nameItem"), plugin.getLang().get(p, "menus.wineffectsselector.wineffect.loreItem"));
                int s = selector.getSlot("{SELECTED}");
                if (s > -1 && s < 54) {
                    inv.setItem(s, kit);
                }
                if (k.getPage() != page) continue;
                inv.setItem(k.getSlot(), i);
            } else {
                if (k.getPage() != page) continue;
                inv.setItem(k.getSlot(), k.getIcon(p));
            }
        }
        if (sw.getWinEffect() != 999999) {
            int s = selector.getSlot("{DESELECT}");
            if (s > -1 && s < 54) {
                inv.setItem(s, selector.getContents().get(s));
            }
        }
        if (page > 1) {
            int s = selector.getSlot("{LAST}");
            if (s > -1 && s < 54) {
                inv.setItem(s, selector.getContents().get(s));
            }
        }
        if (page < plugin.getCos().getLastPage("KillSound")) {
            int s = selector.getSlot("{NEXT}");
            if (s > -1 && s < 54) {
                inv.setItem(s, selector.getContents().get(s));
            }
        }
        int s = selector.getSlot("{CLOSE}");
        if (s > -1 && s < 54) {
            inv.setItem(s, selector.getContents().get(s));
        }
        p.openInventory(inv);
    }

    public void createWinDanceSelectorMenu(Player p) {
        int page = pages.get(p.getUniqueId());
        int size = plugin.getCm().getRInvWinDances() * 9;
        WinDancesSelectorMenu selector = (WinDancesSelectorMenu) getMenus("windancesselector");
        Inventory inv = Bukkit.createInventory(null, size, plugin.getLang().get(p, "menus.windancesselector.title"));
        for (int s : selector.getExtra()) {
            inv.setItem(s, selector.getContents().get(s));
        }
        SWPlayer sw = plugin.getDb().getSWPlayer(p);
        for (UltraWinDance k : plugin.getCos().getWinDance().values()) {
            if (k.getId() == sw.getWinDance()) {
                ItemStack i = k.getIcon(p);
                ItemStack kit = ItemBuilder.nameLore(i.clone(), plugin.getLang().get(p, "menus.windancesselector.windance.nameItem"), plugin.getLang().get(p, "menus.windancesselector.windance.loreItem"));
                int s = selector.getSlot("{SELECTED}");
                if (s > -1 && s < 54) {
                    inv.setItem(s, kit);
                }
                if (k.getPage() != page) continue;
                inv.setItem(k.getSlot(), i);
            } else {
                if (k.getPage() != page) continue;
                inv.setItem(k.getSlot(), k.getIcon(p));
            }
        }
        if (sw.getWinDance() != 999999) {
            int s = selector.getSlot("{DESELECT}");
            if (s > -1 && s < 54) {
                inv.setItem(s, selector.getContents().get(s));
            }
        }
        if (page > 1) {
            int s = selector.getSlot("{LAST}");
            if (s > -1 && s < 54) {
                inv.setItem(s, selector.getContents().get(s));
            }
        }
        if (page < plugin.getCos().getLastPage("WinDance")) {
            int s = selector.getSlot("{NEXT}");
            if (s > -1 && s < 54) {
                inv.setItem(s, selector.getContents().get(s));
            }
        }
        int s = selector.getSlot("{CLOSE}");
        if (s > -1 && s < 54) {
            inv.setItem(s, selector.getContents().get(s));
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