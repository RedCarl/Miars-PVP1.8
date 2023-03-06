package cn.mcarl.miars.skypvp.listener;

import cc.carm.lib.easyplugin.utils.ColorParser;
import cn.mcarl.miars.core.utils.ToolUtils;
import cn.mcarl.miars.skypvp.enums.LuckBlockType;
import cn.mcarl.miars.skypvp.manager.ScoreBoardManager;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    @EventHandler
    public void PlayerInteractAtEntityEvent(PlayerInteractAtEntityEvent e){
        if (e.getRightClicked() instanceof ArmorStand stand){
            if (stand.getCustomName().contains("miars_lucky")){
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void PlayerMoveEvent(PlayerMoveEvent e){
        Player player = e.getPlayer();
        for (Entity entity:player.getWorld().getNearbyEntities(player.getLocation(),2,2,2)) {
            if (entity instanceof ArmorStand stand){
                if (stand.getCustomName().contains("miars_lucky")){
                    String name = stand.getCustomName().substring(stand.getCustomName().indexOf(".")+1);
                    player.sendMessage(ColorParser.parse("&7你开启了一个 "+ LuckBlockType.valueOf(name).getName()+" &7幸运方块！"));
                    stand.remove();
                }
            }
        }
    }
    @EventHandler
    public void PlayerJoinEvent(PlayerJoinEvent e){
        Player player = e.getPlayer();

        ScoreBoardManager.getInstance().joinPlayer(player);
    }

    @EventHandler
    public void PlayerQuitEvent(PlayerQuitEvent e){
        Player player = e.getPlayer();
        ScoreBoardManager.getInstance().removePlayer(player);
    }

    @EventHandler
    public void EntityDamageEvent(EntityDamageEvent e){
        if (e.getEntity() instanceof Player player){
            if (e.getCause() == EntityDamageEvent.DamageCause.FALL){
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void EntityDamageByEntityEvent(EntityDamageByEntityEvent e){
        if (e.getEntity() instanceof Player entity){ // 被攻击者
            if (e.getDamager() instanceof Player player){// 攻击者

            }
        }
    }
}
