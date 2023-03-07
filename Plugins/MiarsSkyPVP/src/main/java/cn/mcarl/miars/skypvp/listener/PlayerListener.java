package cn.mcarl.miars.skypvp.listener;

import cc.carm.lib.easyplugin.utils.ColorParser;
import cn.mcarl.miars.skypvp.enums.LuckBlockType;
import cn.mcarl.miars.skypvp.manager.CombatManager;
import cn.mcarl.miars.skypvp.manager.ScoreBoardManager;
import cn.mcarl.miars.skypvp.utils.PlayerUtils;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.projectiles.ProjectileSource;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerListener implements Listener {

    @EventHandler
    public void PlayerInteractAtEntityEvent(PlayerInteractAtEntityEvent e){
        if (e.getRightClicked() instanceof ArmorStand stand){
            if (stand.getCustomName().contains("miars_lucky")){
                e.setCancelled(true);
            }
        }
    }

    Map<UUID,Boolean> protectedRegion = new HashMap<>();

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

        // 是否在安全区
        if (protectedRegion.containsKey(player.getUniqueId())){
            if (PlayerUtils.isProtectedRegion(player)){
                if (!protectedRegion.get(player.getUniqueId())){
                    player.sendMessage(ColorParser.parse("&a您进入了安全区,这里您将绝对安全。"));
                    protectedRegion.put(player.getUniqueId(),true);
                }
            }else {
                if (protectedRegion.get(player.getUniqueId())){
                    player.sendMessage(ColorParser.parse("&c您走出了安全区,随时会有危险。"));
                    protectedRegion.put(player.getUniqueId(),false);
                }
            }
        }else {
            protectedRegion.put(player.getUniqueId(),PlayerUtils.isProtectedRegion(player));
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

    @EventHandler(priority = EventPriority.MONITOR)
    public void EntityDamageByEntityEvent(EntityDamageByEntityEvent e){
        if (e.getEntity() instanceof Player entity){ // 被攻击者
            if (e.getDamager() instanceof Player player){// 攻击者
                if (PlayerUtils.isProtectedRegion(player) || PlayerUtils.isProtectedRegion(entity)){
                    e.setCancelled(true);
                    e.setDamage(0);
                }else {
                    CombatManager.getInstance().start(entity,player.getUniqueId().toString(),30);
                }
            }
            if (e.getDamager() instanceof Arrow arrow){// 攻击者
                ProjectileSource shooter = arrow.getShooter();
                if(shooter instanceof Player player) {
                    if (PlayerUtils.isProtectedRegion(player) || PlayerUtils.isProtectedRegion(entity)){
                        e.setCancelled(true);
                        e.setDamage(0);
                    }else {
                        CombatManager.getInstance().start(entity,player.getUniqueId().toString(),30);
                    }
                }
            }
        }
    }
}
