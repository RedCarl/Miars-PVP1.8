package cn.mcarl.miars.core.publics.items;

import cn.mcarl.miars.core.utils.ItemBuilder;
import cn.mcarl.miars.core.utils.easyitem.AbstractItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class GoldHead extends AbstractItem {
    public GoldHead(){
        register();
    }

    @Override
    public void init() {

        id = "gold_head";
        item = new ItemBuilder(Material.GOLDEN_APPLE)
                .setName("&6&l金头")
                .toItemStack();
    }

    @Override
    public void onItemConsume(PlayerItemConsumeEvent e, Player p, ItemStack i) {
        i.setType(Material.AIR);
        p.removePotionEffect(PotionEffectType.REGENERATION);
        p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,10*20,1));
    }
}
