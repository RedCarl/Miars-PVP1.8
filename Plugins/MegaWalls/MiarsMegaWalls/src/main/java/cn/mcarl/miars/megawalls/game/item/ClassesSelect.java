package cn.mcarl.miars.megawalls.game.item;

import cn.mcarl.miars.storage.utils.ItemBuilder;
import cn.mcarl.miars.core.utils.easyitem.AbstractItem;
import cn.mcarl.miars.megawalls.game.ui.ClassesSelectGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class ClassesSelect extends AbstractItem {

    public ClassesSelect(){
        register();
    }

    @Override
    public void init() {

        id = "classes_select";
        item = new ItemBuilder(Material.IRON_SWORD)
                .setName("&a职业选择")
                .setLore("&7选择你的职业。")
                .toItemStack();
    }

    @Override
    public void onInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        ClassesSelectGUI.open(player);
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
