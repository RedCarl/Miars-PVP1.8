package cn.mcarl.miars.storage.storage.data.practice;

import cn.mcarl.miars.storage.MiarsStorage;
import cn.mcarl.miars.storage.entity.ffa.FKit;
import cn.mcarl.miars.storage.enums.practice.FKitType;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

/**
 * @Author: carl0
 * @DATE: 2023/1/5 23:32
 */
public class FKitDataStorage {
    private static final FKitDataStorage instance = new FKitDataStorage();
    public static FKitDataStorage getInstance() {
        return instance;
    }


    /**
     * 存储套件数据
     */
    public void putFKitData(FKit fKit){
        try {
            MiarsStorage.getMySQLStorage().replaceFKitData(fKit);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取套件数据
     */
    public List<FKit> getFKitData(UUID uuid, FKitType fKitType){
        try {
            return MiarsStorage.getMySQLStorage().queryFKitDataList(uuid,fKitType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取套件数据
     */
    public FKit getFKitDataById(Integer id){
        try {
            return MiarsStorage.getMySQLStorage().queryFKitDataById(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
