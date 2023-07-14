package cn.mcarl.miars.megawalls.game.listener;

import cn.mcarl.miars.core.utils.MiarsUtils;
import cn.mcarl.miars.megawalls.MiarsMegaWalls;
import cn.mcarl.miars.megawalls.classes.Classes;
import cn.mcarl.miars.megawalls.classes.ClassesManager;
import cn.mcarl.miars.megawalls.conf.PluginConfig;
import cn.mcarl.miars.megawalls.game.entitiy.GamePlayer;
import cn.mcarl.miars.megawalls.game.entitiy.GameTeam;
import cn.mcarl.miars.megawalls.game.entitiy.enums.GameState;
import cn.mcarl.miars.megawalls.game.item.BackLobby;
import cn.mcarl.miars.megawalls.game.item.ClassesHead;
import cn.mcarl.miars.megawalls.game.item.ClassesSelect;
import cn.mcarl.miars.megawalls.game.manager.GameManager;
import cn.mcarl.miars.megawalls.game.manager.GamePlayerManager;
import cn.mcarl.miars.megawalls.game.manager.ScoreBoardManager;
import cn.mcarl.miars.megawalls.utils.PlayerUtils;
import cn.mcarl.miars.storage.storage.data.MPlayerDataStorage;
import cn.mcarl.miars.storage.storage.data.MRankDataStorage;
import cn.mcarl.miars.storage.utils.ToolUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Wither;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;

public class GamePlayerListener implements Listener {

    @EventHandler
    public void PlayerJoinEvent(PlayerJoinEvent e){
        Player player = e.getPlayer();
        ToolUtils.playerInitialize(player);

        GamePlayer gamePlayer = GamePlayerManager.getInstance().getGamePlayer(player);
        gamePlayer.getPlayerStats().update();
        // 判断房间的状态
        switch (GameManager.getInstance().getGameInfo().getGameState()){
            case WAIT, READY -> {
                // 初始化玩家身份
                gamePlayer.sendMessageAll(
                        MRankDataStorage.getInstance().getMRank(
                                MPlayerDataStorage.getInstance().getMPlayer(player).getRank()
                        ).getPrefix()+player.getName()+" &7加入了本次对局。 (&a"+Bukkit.getOnlinePlayers().size()+"&7/&c"+ (PluginConfig.TEAM_LIMIT.get()*4) +"&7)"
                );

                ScoreBoardManager.getInstance().joinPlayer(player);
                player.teleport(GameManager.getInstance().getGameInfo().getLobbySpawn());

                player.setGameMode(GameMode.ADVENTURE);
                player.getInventory().clear();
                player.getInventory().setArmorContents(null);
                player.getActivePotionEffects().clear();
                player.setMaxHealth(20.0D);
                player.setHealth(20.0D);
                player.setFoodLevel(20);
                player.setFireTicks(0);
                player.setLevel(0);
                player.setExp(0.0F);

                new ClassesSelect().give(player,0);
                new ClassesHead().give(player,1);
                new BackLobby().give(player,8);

                if (gamePlayer.getPlayerStats().getClasses()==null|| "".equals(gamePlayer.getPlayerStats().getClasses())){
                    gamePlayer.getPlayerStats().setClasses("Cow");
                }
                System.out.println(gamePlayer.getPlayerStats().getClasses());
                Classes classes = ClassesManager.getClassesByName(gamePlayer.getPlayerStats().getClasses());

                Bukkit.getScheduler().runTaskLaterAsynchronously(MiarsMegaWalls.getInstance(), () -> {
                    PlayerUtils.skinChange(player, classes.getDefaultSkin().getValue(), classes.getDefaultSkin().getSignature());
                }, 1L);
            }
            case START,CONDUCT -> {
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
                if (e.getDamager() instanceof Projectile projectile) {
                    // 防止凋零误伤队友
                    if (projectile.getShooter() instanceof Wither wither){
                        if (e.getEntity() instanceof Player player) {
                            GamePlayer gamePlayer = GamePlayerManager.getInstance().getGamePlayer(player);
                            GameTeam gameTeam = GameManager.getInstance().getGamePlayerTeam(gamePlayer);

                            if (gameTeam.getTeamWither().getCustomName().equals(wither.getCustomName())){
                                e.setCancelled(true);
                            }
                        }
                    }
                }

            }
        }
    }

    @EventHandler
    public void FoodLevelChangeEvent(FoodLevelChangeEvent e){
        // 判断房间的状态
        switch (GameManager.getInstance().getGameInfo().getGameState()){
            case WAIT, READY, START , END -> {
                // 如果游戏结束了或未开始或者墙没倒，禁止饥饿
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
        GameTeam gameTeam = GameManager.getInstance().getGamePlayerTeam(gamePlayer);
        // 判断房间的状态
        switch (GameManager.getInstance().getGameInfo().getGameState()){
            case WAIT, READY -> {
                // 如果游戏还未开始，就复活到大厅出生点
                e.setRespawnLocation(GameManager.getInstance().getGameInfo().getLobbySpawn());
            }
            default -> {
                // 重生时复活到自己阵营的复活点
                if (gameTeam.isWitherDead()){
                    e.setRespawnLocation(player.getLocation());
                }else {
                    e.setRespawnLocation(GameManager.getInstance().getGamePlayerTeam(gamePlayer).getSpawn());
                }
            }
        }

    }

    @EventHandler
    public void PlayerDeathEvent(PlayerDeathEvent e){
        Player player = e.getEntity();
        GamePlayer gamePlayer = GamePlayerManager.getInstance().getGamePlayer(player);
        GameTeam gameTeam = GameManager.getInstance().getGamePlayerTeam(gamePlayer);

        // 判断房间的状态
        switch (GameManager.getInstance().getGameInfo().getGameState()){
            case WAIT, READY -> {
                // 如果游戏没有开始，死亡不凋落
                e.setKeepInventory(true);
            }
            default -> {
                // 游戏过程中，被击杀会掉落背包物品
                e.setKeepInventory(false);
                if (gameTeam.isWitherDead()){
                    player.setGameMode(GameMode.SPECTATOR);


                    // 检测其他队伍是否已经被淘汰！
                    int a = 0;
                    GameTeam winTeam = new GameTeam();
                    for (GameTeam gt:GameManager.getInstance().getGameInfo().getGameTeams().values()) {
                        if (gt.getSurvivePlayers().size()!=0){
                            winTeam=gt;
                            a++;
                        }
                    }
                    if (a<=1){
                        GameManager.getInstance().getGameInfo().setWinTeam(winTeam);
                        GameManager.getInstance().getGameInfo().setGameState(GameState.END);
                    }
                }
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

    @EventHandler
    public void BlockBreakEvent(BlockBreakEvent e){
        Player player = e.getPlayer();
        // 判断房间的状态
        switch (GameManager.getInstance().getGameInfo().getGameState()){
            case WAIT, READY, START -> {
                if (!player.hasPermission("miars.admin")){
                    e.setCancelled(true);
                }
            }
            default -> {
            }
        }
    }

    @EventHandler
    public void BlockPlaceEvent(BlockPlaceEvent e){
        Player player = e.getPlayer();
        // 判断房间的状态
        switch (GameManager.getInstance().getGameInfo().getGameState()){
            case WAIT, READY, START -> {
                if (!player.hasPermission("miars.admin")){
                    e.setCancelled(true);
                }
            }
            default -> {
            }
        }
    }

    @EventHandler(
            priority = EventPriority.HIGHEST
    )
    public void onEntityExplode(EntityExplodeEvent e) {
        e.setCancelled(true);
    }

}
