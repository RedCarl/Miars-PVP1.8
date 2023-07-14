package cn.mcarl.miars.core.listener;

import cn.mcarl.miars.core.conf.PluginConfig;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.event.world.WorldLoadEvent;

public class WorldListener implements Listener {


    @EventHandler
    public void WorldLoadEvent(WorldLoadEvent e){
        World world = e.getWorld();
    }
    @EventHandler
    public void WeatherChangeEvent(WeatherChangeEvent e){
        e.setCancelled(PluginConfig.SITE.DISABLED_WEATHER_CHANGE.get());
    }

    @EventHandler
    public void BlockExplodeEvent(BlockExplodeEvent e){
        e.setCancelled(PluginConfig.SITE.DISABLED_EXPLODE_DESTROY.get());
    }

    @EventHandler
    public void EntityExplodeEvent(EntityExplodeEvent e){
        e.setCancelled(PluginConfig.SITE.DISABLED_EXPLODE_DESTROY.get());
    }

    @EventHandler
    public void LeavesDecayEvent(LeavesDecayEvent e){
        e.setCancelled(PluginConfig.SITE.DISABLED_EXPLODE_DESTROY.get());
    }

}
