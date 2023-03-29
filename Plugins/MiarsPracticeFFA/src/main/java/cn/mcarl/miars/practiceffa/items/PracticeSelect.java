package cn.mcarl.miars.practiceffa.items;

import cn.mcarl.miars.core.utils.ItemBuilder;
import cn.mcarl.miars.core.utils.easyitem.AbstractItem;
import cn.mcarl.miars.practiceffa.ui.SoloMenuGUI;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;

public class PracticeSelect extends AbstractItem {
    public PracticeSelect(){
        register();
    }

    @Override
    public void init() {

        id = "practice_select";
        item = new ItemBuilder(Material.DIAMOND_SWORD)
                .setName("&6SoloMenu &7(Right Click)")
                .addEnchant(Enchantment.ARROW_DAMAGE,1,true)
                .addFlag(ItemFlag.HIDE_ENCHANTS)
                .addFlag(ItemFlag.HIDE_UNBREAKABLE)
                .setUnbreakable()
                .toItemStack();
    }


    @Override
    public void onInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK){
            SoloMenuGUI.open(player);
        }
    }

    @Override
    public void onDropItem(PlayerDropItemEvent e) {
        e.setCancelled(true);
    }

    @Override
    public void onInventoryClick(InventoryClickEvent e) {
        e.setCancelled(true);
    }
}
