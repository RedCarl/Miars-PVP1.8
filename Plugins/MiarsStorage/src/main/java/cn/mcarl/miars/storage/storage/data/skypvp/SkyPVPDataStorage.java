package cn.mcarl.miars.storage.storage.data.skypvp;

import cn.mcarl.miars.storage.MiarsStorage;
import cn.mcarl.miars.storage.entity.skypvp.SPlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SkyPVPDataStorage {
    private static final SkyPVPDataStorage instance = new SkyPVPDataStorage();
    public static SkyPVPDataStorage getInstance() {
        return instance;
    }

    private final Map<UUID, SPlayer> dataMap = new HashMap<>();

    public void putSPlayer(SPlayer sPlayer){
        try {

            MiarsStorage.getMySQLStorage().replaceSkyPvp(sPlayer);

            SPlayer data = MiarsStorage.getMySQLStorage().querySkyPvp(sPlayer.getUuid());
            dataMap.put(
                    sPlayer.getUuid(),
                    data
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public SPlayer getSPlayer(Player player){
        if (dataMap.containsKey(player.getUniqueId())){
            return dataMap.get(player.getUniqueId());
        }
        SPlayer data = MiarsStorage.getMySQLStorage().querySkyPvp(player.getUniqueId());

        //如果没有数据，就初始化玩家数据
        if (data==null){
            try {
                putSPlayer(
                        new SPlayer(
                                player.getUniqueId(),
                                player.getName(),
                                0L,
                                0L,
                                0L,
                                0L
                        )
                );
                data = MiarsStorage.getMySQLStorage().querySkyPvp(player.getUniqueId());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        dataMap.put(player.getUniqueId(),data);
        return dataMap.get(player.getUniqueId());
    }

    public boolean checkSPlayer(Player player){
        return MiarsStorage.getMySQLStorage().querySkyPvp(player.getUniqueId())!=null;
    }

    public void clearUserCacheData(Player player){
        dataMap.remove(player.getUniqueId());
    }

    public void reload(){
        dataMap.clear();
    }
}
