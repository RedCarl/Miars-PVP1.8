package cn.mcarl.miars.faction.listener;

import cn.mcarl.miars.faction.MiarsFaction;
import cn.mcarl.miars.faction.conf.PluginConfig;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class SpawnListener implements Listener {
    @EventHandler
    public void BlockPlaceEvent(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        try {
            if (player.getWorld().getName().equals(PluginConfig.PROTECTED_REGION.SPAWN.get().getWorld().getName())){
                if (e.getPlayer().getGameMode() != GameMode.CREATIVE) {
                    e.setCancelled(true);
                }
            }
        }catch (NullPointerException exception){
            MiarsFaction.getInstance().log("主城设置配置出错，请查看配置文件。");
        }

    }

    @EventHandler
    public void BlockBreakEvent(BlockBreakEvent e) {
        Player player = e.getPlayer();

        try {
            if (player.getWorld().getName().equals(PluginConfig.PROTECTED_REGION.SPAWN.get().getWorld().getName())){
                if (e.getPlayer().getGameMode() != GameMode.CREATIVE) {
                    e.setCancelled(true);
                }
            }
        }catch (NullPointerException exception){
            MiarsFaction.getInstance().log("主城设置配置出错，请查看配置文件。");
        }

    }
}
