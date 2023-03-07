package cn.mcarl.miars.skypvp.listener;

import cn.mcarl.miars.skypvp.entitiy.OreBlock;
import cn.mcarl.miars.skypvp.manager.OreRespawnManager;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public class BlockListener implements Listener {

    @EventHandler
    public void BlockPlaceEvent(BlockPlaceEvent e) {
        if (e.getPlayer().getGameMode() != GameMode.CREATIVE) {
            e.setCancelled(true);
        }

    }
    
    @EventHandler
    public void BlockBreakEvent(BlockBreakEvent e) {
        Block block = e.getBlock();
        if (e.getPlayer().getGameMode() != GameMode.CREATIVE) {
            if (!block.getType().name().contains("_ORE")){
                e.setCancelled(true);
            }else {
                OreRespawnManager.getInstance().put(new OreBlock(
                        block.getType(),
                        block.getLocation()
                ));

                // 掉落物品
                switch (block.getType()){
                    case IRON_ORE -> {
                        block.setType(Material.AIR);
                        block.getLocation().getWorld().dropItem(block.getLocation(),new ItemStack(Material.IRON_INGOT));
                    }
                    case GOLD_ORE -> {
                        block.setType(Material.AIR);
                        block.getLocation().getWorld().dropItem(block.getLocation(),new ItemStack(Material.GOLD_INGOT));
                    }
                }
            }
        }
    }
}
