package cn.mcarl.miars.megawalls.game.item;

import cc.carm.lib.easyplugin.utils.ColorParser;
import cn.mcarl.miars.core.MiarsCore;
import cn.mcarl.miars.core.utils.ItemBuilder;
import cn.mcarl.miars.core.utils.MiarsUtil;
import cn.mcarl.miars.core.utils.easyitem.AbstractItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class ClassesHead extends AbstractItem {

    public ClassesHead(){
        register();
    }

    @Override
    public void init() {

        id = "classes_head";
        item = new ItemBuilder(MiarsUtil.createSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDg5ODVmODI0MDVjY2FjOGY1YWUxYjlmNjAzMTQyNTYzYzBlNTFmNGI4NGE4ZTM5NThkNTNiOTkzZWNiMThkMyJ9fX0="))
                .setName("&c皮肤")
                .setLore("&7更换你职业的皮肤。")
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

    @Override
    public void onDropItem(PlayerDropItemEvent e) {
        e.setCancelled(true);
    }
}
