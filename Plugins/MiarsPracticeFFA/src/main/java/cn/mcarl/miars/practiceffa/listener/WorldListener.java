package cn.mcarl.miars.practiceffa.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

/**
 * @Author: carl0
 * @DATE: 2023/1/3 23:58
 */
public class WorldListener implements Listener {

    @EventHandler
    public void WeatherChangeEvent(WeatherChangeEvent e){
        e.setCancelled(true);
    }
}
