package cn.mcarl.miars.practiceffa.items;

import cn.mcarl.miars.storage.utils.ItemBuilder;
import cn.mcarl.miars.storage.utils.easyitem.AbstractItem;
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
        item = new ItemBuilder(Material.COMPASS)
                .setName("&bGame Menu &7(Right Click)")
                .setUnbreakable()
                .toItemStack();
    }


    @Override
    public void onInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK){
            SoloMenuGUI.open(player);
        }
        e.setCancelled(true);
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
