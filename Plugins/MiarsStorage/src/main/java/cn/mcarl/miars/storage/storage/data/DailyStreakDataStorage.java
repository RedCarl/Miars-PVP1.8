package cn.mcarl.miars.storage.storage.data;

import cn.mcarl.miars.storage.MiarsStorage;
import cn.mcarl.miars.storage.entity.practice.DailyStreak;
import cn.mcarl.miars.storage.enums.FKitType;
import cn.mcarl.miars.storage.enums.QueueType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class DailyStreakDataStorage {

    private static final DailyStreakDataStorage instance = new DailyStreakDataStorage();

    public static DailyStreakDataStorage getInstance() {
        return instance;
    }

    List<DailyStreak> dailyStreaks = new ArrayList<>();


    // 玩家连胜败
    public void putDailyStreakData(Player player, QueueType queueType, FKitType fKitType,Boolean isWin){

        if (isWin){

            for (DailyStreak d:dailyStreaks){
                if (d.getUuid() == player.getUniqueId() && queueType.equals(d.getQueueType()) && fKitType.equals(d.getFKitType())){
                    d.setStreak(d.getStreak()+1);

                    MiarsStorage.getRedisStorage().setList("DAILY_STREAKS",dailyStreaks);
                    return;
                }
            }

            dailyStreaks.add(new DailyStreak(player.getUniqueId(),1,queueType,fKitType));

        }else {

            for (DailyStreak d:dailyStreaks){
                if (d.getUuid() == player.getUniqueId() && queueType.equals(d.getQueueType()) && fKitType.equals(d.getFKitType())){
                    dailyStreaks.remove(d);

                    MiarsStorage.getRedisStorage().setList("DAILY_STREAKS",dailyStreaks);
                    return;
                }
            }

        }

        MiarsStorage.getRedisStorage().setList("DAILY_STREAKS",dailyStreaks);

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
        MiarsStorage.getRedisStorage().setList("DAILY_STREAKS",null);
    }
}
