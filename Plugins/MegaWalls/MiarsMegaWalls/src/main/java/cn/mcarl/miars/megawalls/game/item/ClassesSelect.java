package cn.mcarl.miars.megawalls.game.item;

import cc.carm.lib.easyplugin.utils.ColorParser;
import cn.mcarl.miars.core.utils.ItemBuilder;
import cn.mcarl.miars.core.utils.easyitem.AbstractItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class ClassesSelect extends AbstractItem {

    public ClassesSelect(){
        register();
    }

    @Override
    public void init() {

        id = "classes_select";
        item = new ItemBuilder(Material.IRON_SWORD)
                .setName("&c职业选择")
                .setLore("&7选择你的职业。")
                .toItemStack();
    }

    @Override
    public void onInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        player.sendMessage(ColorParser.parse("&7敬请期待...."));
    }

    @Override
    public void onInventoryClick(InventoryClickEvent e) {
        e.setCancelled(true);
    }
}
