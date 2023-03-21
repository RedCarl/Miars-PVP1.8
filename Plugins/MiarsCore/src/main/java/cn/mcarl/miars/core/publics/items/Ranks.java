package cn.mcarl.miars.core.publics.items;

import cn.mcarl.miars.core.ui.RanksGUI;
import cn.mcarl.miars.core.ui.ServerMenuGUI;
import cn.mcarl.miars.core.utils.ItemBuilder;
import cn.mcarl.miars.core.utils.easyitem.AbstractItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;

public class Ranks extends AbstractItem {
    public Ranks(){
        register();
    }

    @Override
    public void init() {

        id = "ranks";
        item = new ItemBuilder(Material.NAME_TAG)
                        .setName("&c头衔管理 &7(右键打开)")
                        .toItemStack();
    }

    @Override
    public void onInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        RanksGUI.open(player);
    }

    @Override
    public void onInventoryClick(InventoryClickEvent e) {
        e.setCancelled(true);
    }

    @Override
    public void onDropItem(PlayerDropItemEvent e) {
        e.setCancelled(true);
    }

}
