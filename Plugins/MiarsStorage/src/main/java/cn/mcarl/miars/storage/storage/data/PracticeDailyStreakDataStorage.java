package cn.mcarl.miars.storage.storage.data;

import cn.mcarl.miars.storage.MiarsStorage;
import cn.mcarl.miars.storage.entity.practice.DailyStreak;
import cn.mcarl.miars.storage.entity.practice.QueueInfo;
import cn.mcarl.miars.storage.enums.FKitType;
import cn.mcarl.miars.storage.enums.QueueType;
import com.alibaba.fastjson.JSONArray;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PracticeDailyStreakDataStorage {

    private static final PracticeDailyStreakDataStorage instance = new PracticeDailyStreakDataStorage();

    public static PracticeDailyStreakDataStorage getInstance() {
        return instance;
    }

    private final String REDIS_KEY = "PRACTICE_DAILY_STREAKS";

    public PracticeDailyStreakDataStorage(){
        dailyStreaks=getDailyStreaksRedisList();
        if (dailyStreaks==null){
            dailyStreaks=new ArrayList<>();
        }
    }
    List<DailyStreak> dailyStreaks;


    // 玩家连胜败
    public void putDailyStreakData(Player player, QueueType queueType, FKitType fKitType,Boolean isWin){

        if (isWin){

            for (DailyStreak d:dailyStreaks){
                if (d.getUuid() == player.getUniqueId() && queueType.equals(d.getQueueType()) && fKitType.equals(d.getFKitType())){
                    d.setStreak(d.getStreak()+1);

                    setDailyStreaksRedisList(dailyStreaks);
                    return;
                }
            }

            dailyStreaks.add(new DailyStreak(player.getUniqueId(),1,queueType,fKitType));

        }else {

            for (DailyStreak d:dailyStreaks){
                if (d.getUuid() == player.getUniqueId() && queueType.equals(d.getQueueType()) && fKitType.equals(d.getFKitType())){
                    dailyStreaks.remove(d);

                    setDailyStreaksRedisList(dailyStreaks);
                    return;
                }
            }

        }

        setDailyStreaksRedisList(dailyStreaks);

    }

    // 获取连胜Top3
    public List<DailyStreak> getDailyStreaksTop(QueueType queueType, FKitType fKitType){
        return null;
    }

    // 获得玩家的连胜次数
    public DailyStreak getDailyStreaksByPlayer(Player player, QueueType queueType, FKitType fKitType){
        return null;
    }

    // 清除Redis数据
    public void clear(){
        setDailyStreaksRedisList(new ArrayList<>());
    }


    public List<DailyStreak> getDailyStreaksRedisList(){
        return JSONArray.parseArray(
                MiarsStorage.getRedisStorage().getJedis(REDIS_KEY)).toJavaList(DailyStreak.class);
    }

    public void setDailyStreaksRedisList(List<DailyStreak> list){
        if (list.size()==0){
            MiarsStorage.getRedisStorage().delJedis(REDIS_KEY);
        }
        MiarsStorage.getRedisStorage().setJedis(REDIS_KEY,JSONArray.toJSON(list).toString());
    }
}
