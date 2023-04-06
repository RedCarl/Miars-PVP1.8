package io.github.Leonardo0013YT.UltraSkyWars.cosmetics.windances;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.interfaces.Purchasable;
import io.github.Leonardo0013YT.UltraSkyWars.superclass.Cosmetic;
import io.github.Leonardo0013YT.UltraSkyWars.utils.Utils;
import org.bukkit.inventory.ItemStack;

public class UltraWinDance extends Cosmetic implements Purchasable {

    private String type;
    private ItemStack icon;

    public UltraWinDance(UltraSkyWars plugin, String s) {
        super(plugin.getWindance(), s, "windances");
        this.type = plugin.getWindance().get(s + ".type");
        this.icon = Utils.getIcon(plugin.getWindance(), s);
        plugin.getCos().setLastPage("WinDance", page);
    }

    public String getType() {
        return type;
    }

}