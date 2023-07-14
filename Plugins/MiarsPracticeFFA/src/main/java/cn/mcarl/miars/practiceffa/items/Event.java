package cn.mcarl.miars.practiceffa.items;

import cn.mcarl.miars.storage.utils.ItemBuilder;
import cn.mcarl.miars.storage.utils.easyitem.AbstractItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class Event extends AbstractItem {
    public Event(){
        register();
    }

    @Override
    public void init() {

        id = "event";
        item = new ItemBuilder(Material.EMERALD)
                .setName("&aEvent &7(Right Click)")
                .toItemStack();
    }


    @Override
    public void onInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK){
//            SelectFKitTypeGUI.open(player);
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
