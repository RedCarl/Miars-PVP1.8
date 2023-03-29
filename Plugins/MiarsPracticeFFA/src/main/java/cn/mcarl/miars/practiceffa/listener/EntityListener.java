package cn.mcarl.miars.practiceffa.listener;

import cn.mcarl.miars.practiceffa.conf.PluginConfig;
import cn.mcarl.miars.practiceffa.manager.CombatManager;
import cn.mcarl.miars.practiceffa.utils.FFAUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityListener implements Listener {

    @EventHandler
    public void EntityDamageByEntityEvent(EntityDamageByEntityEvent e){
        if (e.getDamager() instanceof Player attacker && e.getEntity() instanceof Player attacked){

            // 判断两人是否有人在安全区
            if (FFAUtil.isRange(attacked,PluginConfig.FFA_SITE.LOCATION.get(),PluginConfig.FFA_SITE.RADIUS.get())){
                e.setCancelled(true);
            }else if (FFAUtil.isRange(attacker,PluginConfig.FFA_SITE.LOCATION.get(),PluginConfig.FFA_SITE.RADIUS.get())){
                e.setCancelled(true);
            }else {
                // 进入战斗模式
                CombatManager.getInstance().start(attacked,attacker.getUniqueId(),15);
                CombatManager.getInstance().start(attacker,attacked.getUniqueId(),15);
            }
        }
    }

    @EventHandler
    public void EntityDamageEvent(EntityDamageEvent event) {
        if(event.getEntity() instanceof Player player) {
            // 判断攻击者是否在安全区内
            if (FFAUtil.isRange(player, PluginConfig.FFA_SITE.LOCATION.get(),PluginConfig.FFA_SITE.RADIUS.get())){
                event.setCancelled(true);
            }
        }
    }

}
