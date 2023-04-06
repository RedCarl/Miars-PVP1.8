package io.github.Leonardo0013YT.UltraSkyWars.managers;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.cosmetics.kits.KitLevel;
import io.github.Leonardo0013YT.UltraSkyWars.data.SWPlayer;
import io.github.Leonardo0013YT.UltraSkyWars.interfaces.Purchasable;
import io.github.Leonardo0013YT.UltraSkyWars.utils.Utils;
import org.bukkit.entity.Player;

public class ShopManager {

    private UltraSkyWars plugin;

    public ShopManager(UltraSkyWars plugin) {
        this.plugin = plugin;
    }

    public void buy(Player p, Purchasable purchasable, String name) {
        if (!purchasable.isBuy()) {
            p.sendMessage(plugin.getLang().get(p, "messages.noBuy"));
            return;
        }
        if (plugin.getAdm().getCoins(p) < purchasable.getPrice()) {
            p.sendMessage(plugin.getLang().get(p, "messages.noCoins"));
            return;
        }
        SWPlayer sw = plugin.getDb().getSWPlayer(p);
        plugin.getAdm().removeCoins(p, purchasable.getPrice());
        if (purchasable instanceof KitLevel) {
            KitLevel k = (KitLevel) purchasable;
            if (plugin.getCm().isKitLevelsOrder() && !isLastLevel(sw, k)) {
                p.sendMessage(plugin.getLang().get(p, "messages.buyLastLevel"));
                return;
            }
            sw.addKitLevel(k.getKit().getId(), k.getLevel());
        }
        Utils.updateSB(p);
        p.sendMessage(plugin.getLang().get(p, "messages.bought").replaceAll("<name>", name));
    }

    public boolean isLastLevel(SWPlayer sw, KitLevel kl) {
        if (kl.getLevel() == 1) {
            return true;
        }
        return sw.hasKitLevel(kl.getKit().getId(), kl.getLevel() - 1);
    }

}