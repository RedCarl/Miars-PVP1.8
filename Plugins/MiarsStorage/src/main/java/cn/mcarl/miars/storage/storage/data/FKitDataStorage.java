package cn.mcarl.miars.storage.storage.data;

import cn.mcarl.miars.storage.MiarsStorage;
import cn.mcarl.miars.storage.entity.ffa.FKit;
import cn.mcarl.miars.storage.enums.FKitType;
import org.bukkit.entity.Player;

import java.util.List;

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
    public List<FKit> getFKitData(Player p, FKitType fKitType){
        try {
            return MiarsStorage.getMySQLStorage().queryFKitDataList(p.getUniqueId(),fKitType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
