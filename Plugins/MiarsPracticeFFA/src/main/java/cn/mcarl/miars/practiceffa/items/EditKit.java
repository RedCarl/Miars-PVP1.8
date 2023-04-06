package cn.mcarl.miars.practiceffa.items;

import cn.mcarl.miars.storage.utils.ItemBuilder;
import cn.mcarl.miars.core.utils.easyitem.AbstractItem;
import cn.mcarl.miars.practiceffa.ui.SelectFKitTypeGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class EditKit extends AbstractItem {
    public EditKit(){
        register();
    }

    @Override
    public void init() {

        id = "edit_kit";
        item = new ItemBuilder(Material.BOOK)
                .setName("&6Edit Kit &7(Right Click)")
                .toItemStack();
    }


    @Override
    public void onInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK){
            SelectFKitTypeGUI.open(player);
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
