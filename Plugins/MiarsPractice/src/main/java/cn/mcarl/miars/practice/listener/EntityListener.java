package cn.mcarl.miars.practice.listener;

import cn.mcarl.miars.practice.manager.ArenaManager;
import cn.mcarl.miars.storage.entity.practice.ArenaState;
import cn.mcarl.miars.storage.storage.data.practice.PracticeArenaStateDataStorage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityListener implements Listener {

    @EventHandler
    public void EntityDamageEvent(EntityDamageEvent e) {
        if(e.getEntity() instanceof Player player) {
            // 判断战斗是否已经结束
            ArenaState state = PracticeArenaStateDataStorage.getInstance().getArenaStateByPlayer(player);
            if (state != null && state.getState()!=2){
                e.setCancelled(true);
            }
        }
    }

}
