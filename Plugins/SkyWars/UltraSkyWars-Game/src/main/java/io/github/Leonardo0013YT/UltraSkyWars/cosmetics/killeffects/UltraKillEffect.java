package io.github.Leonardo0013YT.UltraSkyWars.cosmetics.killeffects;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.superclass.Cosmetic;
import io.github.Leonardo0013YT.UltraSkyWars.utils.Utils;
import org.bukkit.inventory.ItemStack;

public class UltraKillEffect extends Cosmetic {

    private String type;
    private ItemStack icon;

    public UltraKillEffect(UltraSkyWars plugin, String s) {
        super(plugin.getKilleffect(), s, "killeffect");
        this.type = plugin.getKilleffect().get(s + ".type");
        this.icon = Utils.getIcon(plugin.getKilleffect(), s);
        plugin.getCos().setLastPage("KillEffect", page);
    }

    public String getType() {
        return type;
    }

}