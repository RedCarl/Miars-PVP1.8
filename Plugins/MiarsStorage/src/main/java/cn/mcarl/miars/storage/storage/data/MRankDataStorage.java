package cn.mcarl.miars.storage.storage.data;

import cn.mcarl.miars.storage.MiarsStorage;
import cn.mcarl.miars.storage.entity.MRank;
import cn.mcarl.miars.storage.entity.MRank;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MRankDataStorage {
    private static final MRankDataStorage instance = new MRankDataStorage();
    public static MRankDataStorage getInstance() {
        return instance;
    }
    public MRankDataStorage(){
        for (MRank m:MiarsStorage.getMySQLStorage().queryRankDataList()){
            dataMap.put(m.getName(),m);
        }
        tick();
    }

    private final Map<String, MRank> dataMap = new HashMap<>();

    /**
     * 存入rank数据
     */
    public void putMRank(MRank mRank){
        try {

            MiarsStorage.getMySQLStorage().replaceMRankData(mRank);

            List<MRank> data = MiarsStorage.getMySQLStorage().queryRankDataList();

            for (MRank m:data){
                dataMap.put(m.getName(),m);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取rank数据
     */
    public MRank getMRank(String rank){

        if (dataMap.containsKey(rank)){
            return dataMap.get(rank);
        }

        MRank data = MiarsStorage.getMySQLStorage().queryMRankDataByName(rank);

        // 如果没有数据，就初始化玩家数据
        if (data==null){
            return null;
        }

        dataMap.put(rank,data);

        return dataMap.get(rank);

    }

    /**
     * 获取所有rank数据
     * @return
     */
    public Map<String,MRank> getMRankList(){
        return dataMap;
    }

    /**
     * 清理rank缓存
     */
    public void clearUserCacheData(Player player){
        dataMap.remove(player.getUniqueId().toString());
    }

    public void tick(){
        new BukkitRunnable() {
            @Override
            public void run() {
                for (MRank m:MiarsStorage.getMySQLStorage().queryRankDataList()){
                    dataMap.put(m.getName(),m);
                }
            }
        }.runTaskTimerAsynchronously(MiarsStorage.getInstance(),20,20);
    }
}