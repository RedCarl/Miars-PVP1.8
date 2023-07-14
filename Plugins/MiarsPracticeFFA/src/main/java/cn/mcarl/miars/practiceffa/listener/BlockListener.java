package cn.mcarl.miars.practiceffa.listener;

import cn.mcarl.miars.practiceffa.conf.PluginConfig;
import cn.mcarl.miars.practiceffa.utils.FFAUtil;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockListener implements Listener {
    
    @EventHandler
    public void BlockPlaceEvent(BlockPlaceEvent e) {

        if (FFAUtil.isItemRange(e.getBlock().getLocation(),PluginConfig.FFA_SITE.LOCATION.getNotNull(),(PluginConfig.FFA_SITE.BUILD_RADIUS.getNotNull()))){
            if (e.getPlayer().getGameMode() != GameMode.CREATIVE) {
                e.setCancelled(true);
            }
        }

    }
    
    @EventHandler
    public void BlockBreakEvent(BlockBreakEvent e) {
        if (FFAUtil.isItemRange(e.getBlock().getLocation(), PluginConfig.FFA_SITE.LOCATION.getNotNull(), (PluginConfig.FFA_SITE.BUILD_RADIUS.getNotNull()))){
            if (e.getPlayer().getGameMode() != GameMode.CREATIVE) {
                e.setCancelled(true);
            }
        }
    }
}
