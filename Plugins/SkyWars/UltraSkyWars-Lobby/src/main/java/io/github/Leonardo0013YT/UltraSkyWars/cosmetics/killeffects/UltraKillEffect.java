package io.github.Leonardo0013YT.UltraSkyWars.cosmetics.killeffects;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.data.SWPlayer;
import io.github.Leonardo0013YT.UltraSkyWars.superclass.Cosmetic;
import io.github.Leonardo0013YT.UltraSkyWars.utils.ItemBuilder;
import io.github.Leonardo0013YT.UltraSkyWars.utils.NBTEditor;
import io.github.Leonardo0013YT.UltraSkyWars.utils.Utils;
import io.github.Leonardo0013YT.UltraSkyWars.xseries.XMaterial;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class UltraKillEffect extends Cosmetic {

    private String type;
    private ItemStack icon;

    public UltraKillEffect(UltraSkyWars plugin, String s) {
        super(plugin.getKilleffect(), s, "killeffect");
        this.type = plugin.getKilleffect().get(s + ".type");
        this.icon = Utils.getIcon(plugin.getKilleffect(), s);
        plugin.getCos().setLastPage("KillEffect", page);
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
                    if (!sw.getKilleffects().contains(id)) {
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
                        if (isBuy && !sw.getKilleffects().contains(id)) {
                            lore.set(i, plugin.getLang().get(p, "menus.killeffectsselector.price").replaceAll("<price>", String.valueOf(price)));
                        } else if (!isBuy && !sw.getKilleffects().contains(id)) {
                            if (needPermToBuy && p.hasPermission(permission)) {
                                lore.set(i, plugin.getLang().get(p, "menus.killeffectsselector.price").replaceAll("<price>", String.valueOf(price)));
                            } else {
                                lore.set(i, plugin.getLang().get(p, "menus.killeffectsselector.noBuyable"));
                            }
                        } else if (sw.getKilleffects().contains(id) || !needPermToBuy) {
                            lore.set(i, plugin.getLang().get(p, "menus.killeffectsselector.buyed"));
                        }
                    } else {
                        lore.set(i, plugin.getLang().get(p, "menus.killeffectsselector.buyed"));
                    }
                    break;
                case "<status>":
                    if (!p.hasPermission(autoGivePermission)) {
                        if (sw.getKilleffects().contains(id)) {
                            lore.set(i, plugin.getLang().get(p, "menus.killeffectsselector.hasBuy"));
                        } else if (isBuy) {
                            if (plugin.getAdm().getCoins(p) > price) {
                                lore.set(i, plugin.getLang().get(p, "menus.killeffectsselector.buy"));
                            } else {
                                lore.set(i, plugin.getLang().get(p, "menus.killeffectsselector.noMoney"));
                            }
                        } else if (needPermToBuy) {
                            if (plugin.getAdm().getCoins(p) > price) {
                                lore.set(i, plugin.getLang().get(p, "menus.killeffectsselector.buy"));
                            } else {
                                lore.set(i, plugin.getLang().get(p, "menus.killeffectsselector.noMoney"));
                            }
                        } else {
                            lore.set(i, plugin.getLang().get(p, "menus.killeffectsselector.noPermission"));
                        }
                    } else {
                        lore.set(i, plugin.getLang().get(p, "menus.killeffectsselector.hasBuy"));
                    }
                    break;
            }
        }
        iconM.setLore(lore);
        icon.setItemMeta(iconM);
        return NBTEditor.set(icon, id, "ULTRASKYWARS", "KILLEFFECT");
    }

    public String getType() {
        return type;
    }

}