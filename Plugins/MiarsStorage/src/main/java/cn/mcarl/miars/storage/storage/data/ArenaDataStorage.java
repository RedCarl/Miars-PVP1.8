package cn.mcarl.miars.storage.storage.data;

import cn.mcarl.miars.storage.MiarsStorage;
import cn.mcarl.miars.storage.entity.Arena;
import cn.mcarl.miars.storage.enums.FKitType;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * @Author: carl0
 * @DATE: 2023/1/5 23:32
 */
public class ArenaDataStorage {
    private static final ArenaDataStorage instance = new ArenaDataStorage();
    public static ArenaDataStorage getInstance() {
        return instance;
    }

    /**
     * 保存房间至数据库
     */
    public void putArenaData(Arena data){
        try {
            MiarsStorage.getMySQLStorage().replaceArenaData(data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取该模式的所有房间
     */
    public List<Arena> getArenaData(FKitType mode){
        try {
            return MiarsStorage.getMySQLStorage().queryArenaDataList(mode);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
