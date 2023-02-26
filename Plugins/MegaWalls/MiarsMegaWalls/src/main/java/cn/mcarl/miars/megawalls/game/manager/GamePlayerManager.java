package cn.mcarl.miars.megawalls.game.manager;

import cn.mcarl.miars.megawalls.game.entitiy.GamePlayer;
import org.bukkit.entity.Player;

import java.util.*;

public class GamePlayerManager {
    private static final GamePlayerManager instance = new GamePlayerManager();
    public static GamePlayerManager getInstance() {
        return instance;
    }

    Map<UUID, GamePlayer> maps = new HashMap<>();


    /**
     * 获取游戏玩家信息
     * @param player
     * @return
     */
    public GamePlayer getGamePlayer(Player player){
        if (maps.containsKey(player.getUniqueId())){
            return maps.get(player.getUniqueId());
        }

        // 查询玩家是否在线
        if (!player.isOnline()){
            return null;
        }

        maps.put(player.getUniqueId(),new GamePlayer(
                player.getUniqueId(),
                player.getName(),
                null
        ));

        return maps.get(player.getUniqueId());
    }

    /**
     * 清除玩家
     * @param player
     */
    public void removeGamePlayer(Player player){
        maps.remove(player.getUniqueId());
    }
}
