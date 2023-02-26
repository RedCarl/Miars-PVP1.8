package cn.mcarl.miars.megawalls.game.listener;

import cn.mcarl.miars.megawalls.conf.PluginConfig;
import cn.mcarl.miars.megawalls.game.entitiy.GamePlayer;
import cn.mcarl.miars.megawalls.game.item.ClassesSelect;
import cn.mcarl.miars.megawalls.game.manager.GameManager;
import cn.mcarl.miars.megawalls.game.manager.GamePlayerManager;
import cn.mcarl.miars.megawalls.manager.ScoreBoardManager;
import cn.mcarl.miars.storage.storage.data.MPlayerDataStorage;
import cn.mcarl.miars.storage.storage.data.MRankDataStorage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class GamePlayerListener implements Listener {

    @EventHandler
    public void PlayerJoinEvent(PlayerJoinEvent e){
        Player player = e.getPlayer();

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

                new ClassesSelect().give(player,0);
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
}
