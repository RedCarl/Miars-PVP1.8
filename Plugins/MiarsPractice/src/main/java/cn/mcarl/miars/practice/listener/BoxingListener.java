package cn.mcarl.miars.practice.listener;

import cn.mcarl.miars.practice.entity.GamePlayer;
import cn.mcarl.miars.practice.manager.ArenaManager;
import cn.mcarl.miars.practice.manager.BoxingManager;
import cn.mcarl.miars.storage.entity.practice.ArenaState;
import cn.mcarl.miars.storage.storage.data.practice.PracticeArenaStateDataStorage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class BoxingListener implements Listener {

    @EventHandler
    public void PlayerJoinEvent(PlayerJoinEvent e){
        Player player = e.getPlayer();
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,Integer.MAX_VALUE,1));

        BoxingManager.getInstance().clearBoxingData(player.getUniqueId());
        BoxingManager.getInstance().clearComboData(player.getUniqueId());
    }

    @EventHandler
    public void FoodLevelChangeEvent(FoodLevelChangeEvent e){
        if (e.getEntity() instanceof Player player){
            e.setFoodLevel(20);
        }

    }


    @EventHandler
    public void EntityDamageByEntityEvent(EntityDamageByEntityEvent e){
        if (e.getEntity() instanceof Player player){
            if (e.getDamager() instanceof Player damager){
                e.setDamage(0);
                if (e.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK){
                    ArenaState state = PracticeArenaStateDataStorage.getInstance().getArenaStateByPlayer(damager.getName());

                    if (state.getState() == ArenaState.State.GAME){

                        BoxingManager.getInstance().addBoxingData(damager.getUniqueId());
                        BoxingManager.getInstance().addComboData(damager.getUniqueId());
                        BoxingManager.getInstance().clearComboData(player.getUniqueId());

                        if (BoxingManager.getInstance().getBoxingData(damager.getUniqueId())>=100){
                            ArenaManager.getInstance().endGame(
                                    new GamePlayer(
                                            damager.getUniqueId(),
                                            damager.getName(),
                                            damager.getHealth(),
                                            damager.getFoodLevel(),
                                            damager.getActivePotionEffects()
                                    ),
                                    new GamePlayer(
                                            player.getUniqueId(),
                                            player.getName(),
                                            player.getHealth(),
                                            player.getFoodLevel(),
                                            player.getActivePotionEffects()
                                    )
                            );
                        }
                    }
                }
            }
        }
    }

}
