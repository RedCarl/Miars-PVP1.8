package cn.mcarl.miars.megawalls.game.listener;

import cn.mcarl.miars.core.utils.ToolUtils;
import cn.mcarl.miars.megawalls.conf.PluginConfig;
import cn.mcarl.miars.megawalls.game.entitiy.GamePlayer;
import cn.mcarl.miars.megawalls.game.item.BackLobby;
import cn.mcarl.miars.megawalls.game.item.ClassesHead;
import cn.mcarl.miars.megawalls.game.item.ClassesSelect;
import cn.mcarl.miars.megawalls.game.manager.GameManager;
import cn.mcarl.miars.megawalls.game.manager.GamePlayerManager;
import cn.mcarl.miars.megawalls.manager.ScoreBoardManager;
import cn.mcarl.miars.storage.storage.data.MPlayerDataStorage;
import cn.mcarl.miars.storage.storage.data.MRankDataStorage;
import net.citizensnpcs.api.event.NPCDamageByEntityEvent;
import net.citizensnpcs.api.event.NPCKnockbackEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.util.Vector;

public class GamePlayerListener implements Listener {
    @EventHandler
    public void PlayerJoinEvent(PlayerJoinEvent e){
        Player player = e.getPlayer();
        ToolUtils.playerInitialize(player);

        // 判断房间的状态
        switch (GameManager.getInstance().getGameInfo().getGameState()){
            case WAIT, READY -> {
                // 初始化玩家身份
                GamePlayer gamePlayer = GamePlayerManager.getInstance().getGamePlayer(player);
                gamePlayer.sendMessageAll(
                        MRankDataStorage.getInstance().getMRank(
                                MPlayerDataStorage.getInstance().getMPlayer(player).getRank()
                        ).getPrefix()+player.getName()+" &7加入了本次对局。 (&a"+Bukkit.getOnlinePlayers().size()+"&7/&c"+ (PluginConfig.TEAM_LIMIT.get()*4) +"&7)"
                );

                ScoreBoardManager.getInstance().joinPlayer(player);
                player.teleport(GameManager.getInstance().getGameInfo().getLobbySpawn());

                player.getInventory().clear();

                new ClassesSelect().give(player,0);
                new ClassesHead().give(player,1);
                new BackLobby().give(player,8);
            }
            case CONDUCT -> {
                player.kickPlayer("您无法中途进入房间。");
            }
            case END -> {
                player.kickPlayer("游戏已经结束。");
            }
        }


    }
    @EventHandler
    public void PlayerQuitEvent(PlayerQuitEvent e){
        Player player = e.getPlayer();

        // 清除玩家身份
        GamePlayerManager.getInstance().removeGamePlayer(player);
        ScoreBoardManager.getInstance().removePlayer(player);
    }


    @EventHandler
    public void EntityDamageByEntityEvent(EntityDamageByEntityEvent e){
        // 判断房间的状态
        switch (GameManager.getInstance().getGameInfo().getGameState()){
            case WAIT, READY, END -> {
                // 未开始或者结束时，禁止攻击玩家
                e.setCancelled(true);
            }
            case START,CONDUCT -> {
                if (e.getEntity() instanceof Player a){
                    if (e.getDamager() instanceof Player b){
                        // 关闭队伍伤害
                        e.setCancelled(GameManager.getInstance().isTeam(a,b));
                    }
                }
            }
        }
    }

    @EventHandler
    public void FoodLevelChangeEvent(FoodLevelChangeEvent e){
        // 判断房间的状态
        switch (GameManager.getInstance().getGameInfo().getGameState()){
            case WAIT, READY, END -> {
                // 如果游戏结束了或未开始，禁止饥饿
                e.setFoodLevel(20);
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void PlayerMoveEvent(PlayerMoveEvent e){
        Player player = e.getPlayer();
        Location location = player.getLocation();

        // 判断房间的状态
        switch (GameManager.getInstance().getGameInfo().getGameState()){
            case WAIT, READY -> {
                // 防止玩家掉入虚空
                if (location.getBlockY() <= 0){
                    player.teleport(GameManager.getInstance().getGameInfo().getLobbySpawn());
                }
            }
        }
    }

    @EventHandler
    public void PlayerRespawnEvent(PlayerRespawnEvent e){
        Player player = e.getPlayer();
        GamePlayer gamePlayer = GamePlayerManager.getInstance().getGamePlayer(player);

        // 判断房间的状态
        switch (GameManager.getInstance().getGameInfo().getGameState()){
            case WAIT, READY -> {
                // 如果游戏还未开始，就复活到大厅出生点
                e.setRespawnLocation(GameManager.getInstance().getGameInfo().getLobbySpawn());
            }
            default -> {
                // 重生时复活到自己阵营的复活点
                e.setRespawnLocation(GameManager.getInstance().getGamePlayerTeam(gamePlayer).getRespawn());
            }
        }

    }

    @EventHandler
    public void PlayerDeathEvent(PlayerDeathEvent e){
        Player player = e.getEntity();

        // 判断房间的状态
        switch (GameManager.getInstance().getGameInfo().getGameState()){
            case WAIT, READY -> {
                // 如果游戏没有开始，死亡不凋落
                e.setKeepInventory(true);
            }
            default -> {
                // 游戏过程中，被击杀会掉落背包物品
                e.setKeepInventory(false);
            }
        }

        player.spigot().respawn();
        e.setDeathMessage(null);
    }

    @EventHandler(
            priority = EventPriority.HIGHEST
    )
    public void EntityRegainHealthEvent(EntityRegainHealthEvent e) {
        if (e.getEntityType() == EntityType.WITHER) {
            e.setCancelled(true);
            e.setAmount(0.0D);
        }
    }

    @EventHandler
    public void EntityChangeBlockEvent(EntityChangeBlockEvent e){
        if (e.getEntity().getType() == EntityType.WITHER_SKULL){
            e.setCancelled(true);
        }
    }

    @EventHandler(
            priority = EventPriority.HIGHEST
    )
    public void onBlockIgnite(BlockIgniteEvent e) {
        e.setCancelled(true);
    }

}
