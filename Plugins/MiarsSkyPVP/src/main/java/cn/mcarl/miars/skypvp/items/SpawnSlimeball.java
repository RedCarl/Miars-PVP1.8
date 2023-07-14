package cn.mcarl.miars.skypvp.items;

import cn.mcarl.miars.storage.utils.ItemBuilder;
import cn.mcarl.miars.storage.utils.easyitem.AbstractItem;
import cn.mcarl.miars.skypvp.manager.SpawnManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class SpawnSlimeball extends AbstractItem {

    public SpawnSlimeball(){
        register();
    }

    @Override
    public void init() {

        id = "spawn_compass";
        item = new ItemBuilder(Material.SLIME_BALL)
                .setName("&a返回出生点&7(右键使用)")
                .setLore("&7您需要在不动，保持 &c5 &7秒。")
                .setLore("&7冷却: &c5 &7分钟。")
                .toItemStack();
    }

    @Override
    public void onInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (e.getAction().equals(Action.RIGHT_CLICK_AIR)||e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
            SpawnManager.getInstance().put(player);
        }
    }

    @Override
    public void onPlayerDeath(PlayerDeathEvent e, Player p, ItemStack i) {
        p.getInventory().remove(i);
        e.getDrops().remove(i);
    }
}
