package io.github.Leonardo0013YT.UltraSkyWars.cosmetics;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.data.SWPlayer;
import io.github.Leonardo0013YT.UltraSkyWars.superclass.Cosmetic;
import io.github.Leonardo0013YT.UltraSkyWars.utils.ItemBuilder;
import io.github.Leonardo0013YT.UltraSkyWars.utils.NBTEditor;
import io.github.Leonardo0013YT.UltraSkyWars.utils.Utils;
import io.github.Leonardo0013YT.UltraSkyWars.xseries.XMaterial;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class Parting extends Cosmetic {

    private ArrayList<String> lines;
    private ItemStack icon;

    public Parting(UltraSkyWars plugin, String s) {
        super(plugin.getParting(), s, "parting");
        this.lines = (ArrayList<String>) plugin.getParting().getList(s + ".message");
        this.icon = Utils.getIcon(plugin.getParting(), s);
        plugin.getCos().setLastPage("Parting", page);
    }

    public ItemStack getIcon(Player p) {
        if (!icon.hasItemMeta()) {
            return icon;
        }
        UltraSkyWars plugin = UltraSkyWars.get();
        SWPlayer sw = plugin.getDb().getSWPlayer(p);
        ItemStack icon = this.icon.clone();
        if (!p.hasPermission(autoGivePermission)) {
            if (price > 0) {
                if (plugin.getCm().isRedPanelInLocked()) {
                    if (!sw.getPartings().contains(id)) {
                        icon = ItemBuilder.item(XMaterial.matchDefinedXMaterial(plugin.getCm().getRedPanelMaterial().name(), plugin.getCm().getRedPanelData()).orElse(XMaterial.RED_STAINED_GLASS_PANE), 1, icon.getItemMeta().getDisplayName(), icon.getItemMeta().getLore());
                    }
                }
            }
        }
        ItemMeta iconM = icon.getItemMeta();
        List<String> lore = icon.getItemMeta().getLore();
        for (int i = 0; i < lore.size(); i++) {
            String s = lore.get(i);
            switch (s) {
                case "<price>":
                    if (!p.hasPermission(autoGivePermission)) {
                        if (isBuy && !sw.getPartings().contains(id)) {
                            lore.set(i, plugin.getLang().get(p, "menus.partingselector.price").replaceAll("<price>", String.valueOf(price)));
                        } else if (!isBuy && !sw.getPartings().contains(id)) {
                            if (needPermToBuy && p.hasPermission(permission)) {
                                lore.set(i, plugin.getLang().get(p, "menus.partingselector.price").replaceAll("<price>", String.valueOf(price)));
                            } else {
                                lore.set(i, plugin.getLang().get(p, "menus.partingselector.noBuyable"));
                            }
                        } else if (sw.getPartings().contains(id) || !needPermToBuy) {
                            lore.set(i, plugin.getLang().get(p, "menus.partingselector.buyed"));
                        }
                    } else {
                        lore.set(i, plugin.getLang().get(p, "menus.partingselector.buyed"));
                    }
                    break;
                case "<status>":
                    if (!p.hasPermission(autoGivePermission)) {
                        if (sw.getPartings().contains(id)) {
                            lore.set(i, plugin.getLang().get(p, "menus.partingselector.hasBuy"));
                        } else if (isBuy) {
                            if (plugin.getAdm().getCoins(p) > price) {
                                lore.set(i, plugin.getLang().get(p, "menus.partingselector.buy"));
                            } else {
                                lore.set(i, plugin.getLang().get(p, "menus.partingselector.noMoney"));
                            }
                        } else if (needPermToBuy) {
                            if (plugin.getAdm().getCoins(p) > price) {
                                lore.set(i, plugin.getLang().get(p, "menus.partingselector.buy"));
                            } else {
                                lore.set(i, plugin.getLang().get(p, "menus.partingselector.noMoney"));
                            }
                        } else {
                            lore.set(i, plugin.getLang().get(p, "menus.partingselector.noPermission"));
                        }
                    } else {
                        lore.set(i, plugin.getLang().get(p, "menus.partingselector.hasBuy"));
                    }
                    break;
            }
        }
        iconM.setLore(lore);
        icon.setItemMeta(iconM);
        return NBTEditor.set(icon, id, "ULTRASKYWARS", "PARTING");
    }

    public void execute(Player d) {
        Location l = d.getLocation().add(0, 1, 0);
        UltraSkyWars.get().getAdm().createHologram(l, lines);
        new BukkitRunnable() {
            @Override
            public void run() {
                UltraSkyWars.get().getAdm().deleteHologram(l);
            }
        }.runTaskLater(UltraSkyWars.get(), 20 * 5);
    }

}