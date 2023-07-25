package cn.mcarl.miars.practice.item;

import cn.mcarl.miars.practice.manager.PlayerInventoryManager;
import cn.mcarl.miars.storage.entity.ffa.FKit;
import cn.mcarl.miars.storage.entity.practice.ArenaState;
import cn.mcarl.miars.storage.storage.data.practice.PracticeArenaStateDataStorage;
import cn.mcarl.miars.storage.utils.ItemBuilder;
import cn.mcarl.miars.storage.utils.easyitem.AbstractItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class KitBook extends AbstractItem {

    private FKit fKit;

    public KitBook(FKit fKit){
        this.fKit = fKit;
        register();
    }

    @Override
    public void init() {

        id = "kit_book";
        item = new ItemBuilder(Material.BOOK)
                        .setName("&7"+fKit.getName()+" &c[Kit]")
                        .toItemStack();
    }

    @Override
    public void onInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        ArenaState state = PracticeArenaStateDataStorage.getInstance().getArenaStateByPlayer(player.getName());

        if (state!=null){
            PlayerInventoryManager.getInstance().autoEquip(player, fKit.getInventory());
        }
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
