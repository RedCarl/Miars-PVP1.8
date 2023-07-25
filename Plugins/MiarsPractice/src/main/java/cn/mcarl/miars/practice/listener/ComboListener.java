package cn.mcarl.miars.practice.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class ComboListener implements Listener {

    @EventHandler
    public void FoodLevelChangeEvent(FoodLevelChangeEvent e){
        if (e.getEntity() instanceof Player player){
            e.setFoodLevel(20);
        }

    }

}
