package cn.mcarl.miars.storage.storage.data.practice;
import cn.mcarl.miars.storage.MiarsStorage;
import cn.mcarl.miars.storage.entity.ffa.FPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: carl0
 * @DATE: 2022/11/30 23:10
 */
public class FPlayerDataStorage {
    private static final FPlayerDataStorage instance = new FPlayerDataStorage();
    public static FPlayerDataStorage getInstance() {
        return instance;
    }

    private final Map<String, FPlayer> dataMap = new HashMap<>();

    /**
     * 存储玩家数据
     */
    public void putFPlayer(FPlayer fPlayer){
        try {

            MiarsStorage.getMySQLStorage().replaceFPlayerData(fPlayer);

            FPlayer data = MiarsStorage.getMySQLStorage().queryFPlayerDataByUUID(String.valueOf(fPlayer.getUuid()));
            data.setName(Bukkit.getPlayer(fPlayer.getUuid()).getName());
            dataMap.put(
                    String.valueOf(fPlayer.getUuid()),
                    data
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取玩家数据
     */
    public FPlayer getFPlayer(Player player){
        if (dataMap.containsKey(player.getUniqueId().toString())){
            return dataMap.get(player.getUniqueId().toString());
        }

        FPlayer data = MiarsStorage.getMySQLStorage().queryFPlayerDataByUUID(player.getUniqueId().toString());

        //如果没有数据，就初始化玩家数据
        if (data==null){
            try {
                // 初始化玩家数据
                putFPlayer(new FPlayer(player.getUniqueId(),player.getName(),0L,0L,0L,null,new Date(System.currentTimeMillis())));
                data = MiarsStorage.getMySQLStorage().queryFPlayerDataByUUID(player.getUniqueId().toString());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        data.setName(player.getName());
        dataMap.put(player.getUniqueId().toString(),data);
        return dataMap.get(player.getUniqueId().toString());
    }

    /**
     * 移除玩家数据
     */
    public void clearUserCacheData(Player player){
        dataMap.remove(player.getUniqueId().toString());
    }



}
