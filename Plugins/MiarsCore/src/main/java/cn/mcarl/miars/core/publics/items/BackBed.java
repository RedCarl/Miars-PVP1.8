package cn.mcarl.miars.core.publics.items;

import cn.mcarl.miars.core.MiarsCore;
import cn.mcarl.miars.core.utils.ItemBuilder;
import cn.mcarl.miars.core.utils.easyitem.AbstractItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class BackBed extends AbstractItem {
    public BackBed(){
        register();
    }

    @Override
    public void init() {

        id = "back_bed";
        item = new ItemBuilder(Material.BED)
                .setName("&c回到大厅 &7(右键使用)")
                .toItemStack();
    }


    @Override
    public void onInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK){
            MiarsCore.getBungeeApi().connect(player,"lobby-a");
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
