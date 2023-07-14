package cn.mcarl.miars.core.publics.items;

import cn.mcarl.miars.core.ui.RanksGUI;
import cn.mcarl.miars.storage.utils.ItemBuilder;
import cn.mcarl.miars.storage.utils.easyitem.AbstractItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class Ranks extends AbstractItem {
    public Ranks(boolean prefix,boolean ascFlag){
        this.prefix = prefix;
        this.ascFlag = ascFlag;

        register();
    }

    boolean prefix;
    boolean ascFlag;

    @Override
    public void init() {

        id = "ranks";
        item = new ItemBuilder(Material.NAME_TAG)
                        .setName("&bName Tag &7(Right Click)")
                        .toItemStack();
    }

    @Override
    public void onInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        RanksGUI.open(player,this.prefix,this.ascFlag);
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
