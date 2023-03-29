package cn.mcarl.miars.practice.manager;

import cn.mcarl.miars.practice.MiarsPractice;
import cn.mcarl.miars.practice.conf.PluginConfig;
import cn.mcarl.miars.storage.entity.practice.ArenaState;
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
                for (QueueInfo q: PracticeQueueDataStorage.getInstance().getQueueInfos(FKitType.valueOf(PluginConfig.PRACTICE_SITE.MODE.get()),QueueType.UNRANKED)) {
                    ArenaState state = PracticeArenaStateDataStorage.getInstance().isNullArena();
                    if (state!=null){
                        List<String> players = new ArrayList<>();
                        for (String s:q.getPlayers()) {
                            players.add(s);
                            if (players.size()==2){
                                ArenaManager.getInstance().allotArena(players.get(0),players.get(1),state,QueueType.UNRANKED);
                                players.clear();
                                break;
                            }
                        }
                    }
                }
            }
        }.runTaskTimerAsynchronously(MiarsPractice.getInstance(),20,20);
    }
}
