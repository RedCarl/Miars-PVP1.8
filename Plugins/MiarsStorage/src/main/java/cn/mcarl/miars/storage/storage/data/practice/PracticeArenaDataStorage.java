package cn.mcarl.miars.storage.storage.data.practice;

import cn.mcarl.miars.storage.MiarsStorage;
import cn.mcarl.miars.storage.entity.practice.Arena;
import cn.mcarl.miars.storage.enums.practice.FKitType;

import java.util.List;

/**
 * @Author: carl0
 * @DATE: 2023/1/5 23:32
 */
public class PracticeArenaDataStorage {
    private static final PracticeArenaDataStorage instance = new PracticeArenaDataStorage();
    public static PracticeArenaDataStorage getInstance() {
        return instance;
    }

    /**
     * 保存房间至数据库
     */
    public void putArenaData(Arena data){
        try {

            Arena arena = MiarsStorage.getMySQLStorage().queryFPlayerDataByName(data.getName());

            if (arena!=null){
                data.setId(arena.getId());
            }

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
