package cn.mcarl.miars.practice.listener;

import cn.mcarl.miars.practice.entity.GamePlayer;
import cn.mcarl.miars.practice.manager.ArenaManager;
import cn.mcarl.miars.storage.entity.practice.ArenaState;
import cn.mcarl.miars.storage.storage.data.practice.PracticeArenaStateDataStorage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class SumoListener implements Listener {

    @EventHandler
    public void FoodLevelChangeEvent(FoodLevelChangeEvent e){
        if (e.getEntity() instanceof Player player){
            e.setFoodLevel(20);
        }

    }

    @EventHandler
    public void PlayerMoveEvent(PlayerMoveEvent e){
        Player player = e.getPlayer();
        ArenaState state = PracticeArenaStateDataStorage.getInstance().getArenaStateByPlayer(player.getName());

        if (state!=null){
            switch (state.getState()){
                case IDLE,READY -> {
                    if (e.getFrom().getZ() != e.getTo().getZ() && e.getFrom().getX() != e.getTo().getX()) {
                        e.setTo(e.getFrom());
                    }
                }
                case GAME -> {
                    if(e.getPlayer().getLocation().add(0, -1, 0).getBlock().getType().equals(Material.STATIONARY_WATER) || e.getPlayer().getLocation().add(0, -1, 0).getBlock().getType().equals(Material.WATER)){
                        ArenaManager.getInstance().endGame(
                                GamePlayer.get(Bukkit.getPlayerExact(
                                        state.getPlayerA().equals(player.getName())?state.getPlayerB():state.getPlayerA()
                                )),
                                GamePlayer.get(player));
                    }else if(e.getPlayer().getLocation().add(0, 0, 0).getBlock().getType().equals(Material.STATIONARY_WATER) || e.getPlayer().getLocation().add(0, 0, 0).getBlock().getType().equals(Material.WATER)){
                        ArenaManager.getInstance().endGame(
                                GamePlayer.get(Bukkit.getPlayerExact(
                                        state.getPlayerA().equals(player.getName())?state.getPlayerB():state.getPlayerA()
                                )),
                                GamePlayer.get(player));
                    }else if(e.getPlayer().getLocation().add(0, 1, 0).getBlock().getType().equals(Material.STATIONARY_WATER) || e.getPlayer().getLocation().add(0, 1, 0).getBlock().getType().equals(Material.WATER)){
                        ArenaManager.getInstance().endGame(
                                GamePlayer.get(Bukkit.getPlayerExact(
                                        state.getPlayerA().equals(player.getName())?state.getPlayerB():state.getPlayerA()
                                )),
                                GamePlayer.get(player));
                    }else if(e.getPlayer().getLocation().add(0, -2, 0).getBlock().getType().equals(Material.STATIONARY_WATER) || e.getPlayer().getLocation().add(0, -2, 0).getBlock().getType().equals(Material.WATER)){
                        ArenaManager.getInstance().endGame(
                                GamePlayer.get(Bukkit.getPlayerExact(
                                        state.getPlayerA().equals(player.getName())?state.getPlayerB():state.getPlayerA()
                                )),
                                GamePlayer.get(player));
                    }
                }
            }
        }
    }

    @EventHandler
    public void EntityDamageByEntityEvent(EntityDamageByEntityEvent e){
        e.setDamage(0);
    }
}
