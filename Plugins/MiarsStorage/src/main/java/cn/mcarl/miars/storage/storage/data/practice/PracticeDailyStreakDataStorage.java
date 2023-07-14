package cn.mcarl.miars.storage.storage.data.practice;

import cn.mcarl.miars.storage.MiarsStorage;
import cn.mcarl.miars.storage.entity.practice.DailyStreak;
import cn.mcarl.miars.storage.entity.practice.enums.practice.FKitType;
import cn.mcarl.miars.storage.entity.practice.enums.practice.QueueType;
import cn.mcarl.miars.storage.utils.CustomSort;
import com.alibaba.fastjson.JSONArray;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PracticeDailyStreakDataStorage {

    private static final PracticeDailyStreakDataStorage instance = new PracticeDailyStreakDataStorage();

    public static PracticeDailyStreakDataStorage getInstance() {
        return instance;
    }

    private final String REDIS_KEY = "PRACTICE_DAILY_STREAKS";
    List<DailyStreak> dailyStreaks = new ArrayList<>();

    // 玩家连胜败
    public void putDailyStreakData(Player player, QueueType queueType, FKitType fKitType,Boolean isWin){

        dailyStreaks = getDailyStreaksRedisList();

        for (DailyStreak d:dailyStreaks){
            if (d.getName().equals(player.getName()) && queueType.equals(d.getQueueType()) && fKitType.equals(d.getFKitType())){
                if (isWin){
                    d.setStreak(d.getStreak()+1);
                }else {
                    d.setStreak(0);
                }
                setDailyStreaksRedisList(dailyStreaks);
                return;
            }
        }

        if (isWin){
            dailyStreaks.add(new DailyStreak(player.getName(),player.getUniqueId(),1,queueType,fKitType));
        }

        setDailyStreaksRedisList(dailyStreaks);

    }

    // 控制查询速度
    Long time = 0L;

    // 获取连胜Top
    public List<DailyStreak> getDailyStreaksTop(QueueType queueType, FKitType fKitType){

        // 控制速度
        if ((System.currentTimeMillis()-time)/1000 > 3){
            dailyStreaks = getDailyStreaksRedisList();
        }

        List<DailyStreak> list = new ArrayList<>();
        for (DailyStreak d:dailyStreaks) {
            if (d.getFKitType().equals(fKitType) && d.getQueueType().equals(queueType)){
                list.add(d);
            }
        }
        CustomSort.sort(list,"streak",false);
        return list;
    }

    // 获得玩家的连胜次数
    public DailyStreak getDailyStreaksByPlayer(Player player, QueueType queueType, FKitType fKitType,List<DailyStreak> list){
        for (DailyStreak d:list) {
            if (d.getName().equals(player.getName()) && d.getFKitType().equals(fKitType) && d.getQueueType().equals(queueType)){
                return d;
            }
        }
        return null;
    }

    // 清除Redis数据
    public void clear(){
        setDailyStreaksRedisList(new ArrayList<>());
    }


    /**
     * 请不要频繁调用，会影响性能
     * @return
     */
    public List<DailyStreak> getDailyStreaksRedisList(){

        String json = MiarsStorage.getRedisStorage().getJedis(REDIS_KEY);
        if (json==null || json.equals("[]")){
            return new ArrayList<>();
        }

        return JSONArray.parseArray(json).toJavaList(DailyStreak.class);
    }

    /**
     * 请不要频繁调用，会影响性能
     * @return
     */
    public void setDailyStreaksRedisList(List<DailyStreak> list){
        if (list.size()==0){
            MiarsStorage.getRedisStorage().delJedis(REDIS_KEY);
        }
        MiarsStorage.getRedisStorage().setJedis(REDIS_KEY,JSONArray.toJSON(list).toString());
    }
}