package cn.mcarl.miars.megawalls.game.item;

import cn.mcarl.miars.core.MiarsCore;
import cn.mcarl.miars.storage.utils.ItemBuilder;
import cn.mcarl.miars.core.utils.easyitem.AbstractItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class BackLobby extends AbstractItem {

    public BackLobby(){
        register();
    }

    @Override
    public void init() {

        id = "back_lobby";
        item = new ItemBuilder(Material.BED)
                .setName("&c返回大厅")
                .setLore("&7回到超级战墙大厅。")
                .toItemStack();
    }

    @Override
    public void onInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        MiarsCore.getBungeeApi().connect(player,"megawalls");
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
