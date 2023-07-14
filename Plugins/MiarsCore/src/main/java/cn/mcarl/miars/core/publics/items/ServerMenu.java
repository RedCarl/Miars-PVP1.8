package cn.mcarl.miars.core.publics.items;

import cn.mcarl.miars.core.ui.ServerMenuGUI;
import cn.mcarl.miars.storage.utils.ItemBuilder;
import cn.mcarl.miars.storage.utils.easyitem.AbstractItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class ServerMenu extends AbstractItem {
    public ServerMenu(){
        register();
    }

    @Override
    public void init() {

        id = "server_menu";
        item = new ItemBuilder(Material.BOOK)
                .setName("&c游戏选择 &7(右键打开)")
                .toItemStack();
    }

    @Override
    public void onInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        ServerMenuGUI.open(player,"游戏选择");
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
