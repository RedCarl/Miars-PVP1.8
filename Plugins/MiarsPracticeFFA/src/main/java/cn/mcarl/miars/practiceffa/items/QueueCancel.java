package cn.mcarl.miars.practiceffa.items;

import cn.mcarl.miars.storage.utils.ItemBuilder;
import cn.mcarl.miars.core.utils.easyitem.AbstractItem;
import cn.mcarl.miars.practiceffa.entity.GamePlayer;
import cn.mcarl.miars.storage.storage.data.practice.PracticeQueueDataStorage;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class QueueCancel extends AbstractItem {
    public QueueCancel(){
        register();
    }

    @Override
    public void init() {

        id = "queue_cancel";
        item = new ItemBuilder(Material.INK_SACK)
                .setName("&cCancel &7(Right Click)")
                .setData((short) 1)
                .toItemStack();
    }


    @Override
    public void onInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK){
            PracticeQueueDataStorage.getInstance().removeQueue(player.getName());
            GamePlayer.get(player).initData();
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
