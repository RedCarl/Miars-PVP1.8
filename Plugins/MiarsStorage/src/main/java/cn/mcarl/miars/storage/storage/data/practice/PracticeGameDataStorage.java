package cn.mcarl.miars.storage.storage.data.practice;

import cn.mcarl.miars.storage.MiarsStorage;
import cn.mcarl.miars.storage.entity.practice.ArenaState;
import cn.mcarl.miars.storage.enums.practice.FKitType;
import cn.mcarl.miars.storage.enums.practice.QueueType;

import java.util.List;

/**
 * @Author: carl0
 * @DATE: 2023/2/3 17:39
 */
public class PracticeGameDataStorage {
    private static final PracticeGameDataStorage instance = new PracticeGameDataStorage();
    public static PracticeGameDataStorage getInstance() {
        return instance;
    }

    /**
     * 保存房间至数据库
     */
    public void putArenaData(ArenaState data){
        try {

            MiarsStorage.getMySQLStorage().replacePracticeGameData(data);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<ArenaState> getArenaData(String name,FKitType fKitType){
        try {
            return MiarsStorage.getMySQLStorage().queryPracticeGameDataList(name,fKitType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ArenaState getArenaDataByEndTime(Long endTime){
        try {
            return MiarsStorage.getMySQLStorage().queryPracticeGameDataByEndTime(endTime);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ArenaState getArenaDataById(Integer Id){
        try {
            return MiarsStorage.getMySQLStorage().queryPracticeGameDataById(Id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<ArenaState> getArenaDataByWin(String name){
        try {
            return MiarsStorage.getMySQLStorage().queryPracticeGameDataByWin(name);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<ArenaState> getArenaDataByDayWin(String name, FKitType fKitType, QueueType queueType, Long time){
        try {
            return MiarsStorage.getMySQLStorage().queryPracticeGameDataByDayWin(name,fKitType,queueType, time);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
