package cn.mcarl.miars.practiceffa.manager;

import cn.mcarl.miars.practiceffa.entity.GamePlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GamePlayerManager {
    private static final GamePlayerManager instance = new GamePlayerManager();
    public static GamePlayerManager getInstance() {
        return instance;
    }

    Map<UUID, GamePlayer> map = new HashMap<>();

    public GamePlayer get(Player player){
        if (map.containsKey(player.getUniqueId())){
            return map.get(player.getUniqueId());
        }

        GamePlayer gamePlayer = new GamePlayer(player);
        map.put(player.getUniqueId(),gamePlayer);
        return map.get(player.getUniqueId());
    }

    public void remove(Player player){
        map.remove(player.getUniqueId());
    }

    public void init(Player player){
        remove(player);
        get(player);
    }
}
