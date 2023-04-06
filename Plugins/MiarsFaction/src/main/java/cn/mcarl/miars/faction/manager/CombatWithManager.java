package cn.mcarl.miars.faction.manager;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @Author: carl0
 * @DATE: 2022/7/13 23:15
 */
public class CombatWithManager {

    private static final CombatWithManager instance = new CombatWithManager();

    public static CombatWithManager getInstance() {
        return instance;
    }

    private final Map<UUID,Long> combatTime = new HashMap<>();

    public boolean isInCombatWith(Player player){
        if (combatTime.containsKey(player.getUniqueId())){
            long time = (System.currentTimeMillis() - combatTime.get(player.getUniqueId()))/1000;
            return time <= 15;
        }
        return false;
    }

    public void setPlayerCombatWith(Player player){
        combatTime.put(player.getUniqueId(),System.currentTimeMillis());
    }

}
