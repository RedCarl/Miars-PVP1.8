package io.github.Leonardo0013YT.UltraSkyWars.cosmetics.wineffects;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.superclass.Cosmetic;
import io.github.Leonardo0013YT.UltraSkyWars.utils.Utils;
import org.bukkit.inventory.ItemStack;

public class UltraWinEffect extends Cosmetic {

    private String type;
    private ItemStack icon;

    public UltraWinEffect(UltraSkyWars plugin, String s) {
        super(plugin.getWineffect(), s, "wineffect");
        this.type = plugin.getWineffect().get(s + ".type");
        this.icon = Utils.getIcon(plugin.getWineffect(), s);
        plugin.getCos().setLastPage("WinEffect", page);
    }

    public String getType() {
        return type;
    }

}