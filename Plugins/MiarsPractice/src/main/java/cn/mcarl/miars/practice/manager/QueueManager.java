package cn.mcarl.miars.practice.manager;

import cn.mcarl.miars.practice.MiarsPractice;
import cn.mcarl.miars.practice.conf.PluginConfig;
import cn.mcarl.miars.storage.entity.practice.ArenaState;
import cn.mcarl.miars.storage.entity.practice.Duel;
import cn.mcarl.miars.storage.entity.practice.QueueInfo;
import cn.mcarl.miars.storage.enums.practice.FKitType;
import cn.mcarl.miars.storage.enums.practice.QueueType;
import cn.mcarl.miars.storage.storage.data.practice.PracticeArenaStateDataStorage;
import cn.mcarl.miars.storage.storage.data.practice.PracticeQueueDataStorage;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: carl0
 * @DATE: 2023/1/30 20:12
 */
public class QueueManager {
    private static final QueueManager instance = new QueueManager();

    public static QueueManager getInstance() {
        return instance;
    }

    public void init(){
        tick();
    }

    private void tick(){
        new BukkitRunnable() {
            @Override
            public void run() {
                unRankTick();
                duelTick();
            }
        }.runTaskTimerAsynchronously(MiarsPractice.getInstance(),20,20);
    }

    public void unRankTick(){
        for (QueueInfo q: PracticeQueueDataStorage.getInstance().getQueueInfos(FKitType.valueOf(PluginConfig.PRACTICE_SITE.MODE.get()),QueueType.UNRANKED)) {
            ArenaState state = PracticeArenaStateDataStorage.getInstance().isNullArena();
            if (state!=null){
                List<String> players = new ArrayList<>();
                for (String s:q.getPlayers()) {
                    players.add(s);
                    if (players.size()==2){
                        ArenaManager.getInstance().allotArena(players.get(0),players.get(1),state,QueueType.UNRANKED);

                        PracticeQueueDataStorage.getInstance().removeQueue(players.get(0));
                        PracticeQueueDataStorage.getInstance().removeQueue(players.get(1));

                        players.clear();
                        break;
                    }
                }
            }
        }
    }

    public void duelTick(){
        for (Duel d: PracticeQueueDataStorage.getInstance().getDuels(FKitType.valueOf(PluginConfig.PRACTICE_SITE.MODE.get()))) {
            ArenaState state = PracticeArenaStateDataStorage.getInstance().isNullArena();
            if (state!=null){
                ArenaManager.getInstance().allotArena(d.getA(),d.getB(),state,QueueType.UNRANKED);

                PracticeQueueDataStorage.getInstance().removeDuel(
                        new Duel(
                                FKitType.valueOf(PluginConfig.PRACTICE_SITE.MODE.get()),
                                d.getA(),
                                d.getB(),
                                0L,
                                1
                        )
                );

                break;
            }
        }
    }
}
