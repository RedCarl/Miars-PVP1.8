package cn.mcarl.miars.storage.storage.data;

import cn.mcarl.miars.storage.MiarsStorage;
import cn.mcarl.miars.storage.entity.MPlayer;
import cn.mcarl.miars.storage.entity.MRank;
import org.bukkit.entity.Player;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: carl0
 * @DATE: 2022/11/30 23:10
 */
public class MPlayerDataStorage {
    private static final MPlayerDataStorage instance = new MPlayerDataStorage();
    public static MPlayerDataStorage getInstance() {
        return instance;
    }

    private final Map<String, MPlayer> dataMap = new HashMap<>();

    /**
     * 存储玩家数据
     */
    public void putMPlayer(MPlayer mPlayer){
        try {

            MiarsStorage.getMySQLStorage().replaceMPlayer(mPlayer);

            MPlayer data = MiarsStorage.getMySQLStorage().queryMPlayerByUUID(mPlayer.getUuid());
            dataMap.put(
                    mPlayer.getUuid(),
                    data
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取玩家数据
     */
    public MPlayer getMPlayer(Player player){
        if (dataMap.containsKey(player.getUniqueId().toString())){
            return dataMap.get(player.getUniqueId().toString());
        }
        MPlayer data = MiarsStorage.getMySQLStorage().queryMPlayerByUUID(player.getUniqueId().toString());

        //如果没有数据，就初始化玩家数据
        if (data==null){
            try {
                putMPlayer(new MPlayer(player.getUniqueId().toString(),"default", List.of(new String[]{"default"}),null,new Date(System.currentTimeMillis())));
                data = MiarsStorage.getMySQLStorage().queryMPlayerByUUID(player.getUniqueId().toString());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        dataMap.put(player.getUniqueId().toString(),data);
        return dataMap.get(player.getUniqueId().toString());
    }

    /**
     * 移除玩家数据
     */
    public void checkMPlayer(Player player){
        MPlayer mPlayer = getMPlayer(player);
        // 检查头衔是否拥有
        for (MRank mRank:MRankDataStorage.getInstance().getMRankList().values()){
            if (player.hasPermission(mRank.getPermissions())){
                if (!mPlayer.getRanks().contains(mRank.getName())){
                    mPlayer.getRanks().add(mRank.getName());
                }
            }
        }
        putMPlayer(mPlayer);
    }
    /**
     *
     * 移除玩家数据
     */
    public void clearUserCacheData(Player player){
        dataMap.remove(player.getUniqueId().toString());
    }

    public void reload(){
        dataMap.clear();
    }

}
