package cn.mcarl.miars.practiceffa.manager;

import cn.mcarl.miars.practiceffa.MiarsPracticeFFA;
import cn.mcarl.miars.storage.entity.FCombatInfo;
import cn.mcarl.miars.practiceffa.utils.FFAUtil;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: carl0
 * @DATE: 2023/1/4 12:49
 */
public class CombatManager {

    private static final CombatManager instance = new CombatManager();
    public static CombatManager getInstance() {
        return instance;
    }

    private final Map<String, FCombatInfo> data = new HashMap<>();

    public void init() {
    }

    /**
     * 开始战斗
     * @param uuid 玩家ID
     * @param second 持续多少秒
     */
    public void start(Player opponent,String uuid,int second){
        data.put(uuid,new FCombatInfo(opponent.getUniqueId(),System.currentTimeMillis()+(second* 1000L)));
    }

    /**
     * 获取战斗时刻的剩余时间
     *
     * @param p 玩家
     * @return 秒
     */
    public int getLastSecond(Player p){
        if (isCombat(p)){
            return (int) Math.abs((data.get(p.getUniqueId().toString()).getDate()-System.currentTimeMillis())/1000);
        }
        return 0;
    }

    /**
     * 获取战斗数据
     *
     * @param p 玩家
     * @return 秒
     */
    public FCombatInfo getCombatInfo(Player p){
        return data.get(p.getUniqueId().toString());
    }

    /**
     * 是否正在战斗状态
     *
     * @param p 玩家
     * @return 是否
     */
    public boolean isCombat(Player p){
        if (data.get(p.getUniqueId().toString()) != null){
            if (data.get(p.getUniqueId().toString()).getDate() >= System.currentTimeMillis()){
                return true;
            }else {
                clear(p);
            }
        }
        return false;
    }

    /**
     * 清除玩家数据
     * @param p 玩家ID
     */
    public void clear(Player p){
        data.remove(p.getUniqueId().toString());
        new BukkitRunnable() {
            @Override
            public void run() {
                FFAUtil.removeVirtualBorder(p);
            }
        }.runTaskLaterAsynchronously(MiarsPracticeFFA.getInstance(),10);
    }
}
