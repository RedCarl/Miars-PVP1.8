package cn.mcarl.miars.practice.manager;

import cn.mcarl.miars.practice.MiarsPractice;
import cn.mcarl.miars.practice.conf.PluginConfig;
import cn.mcarl.miars.storage.entity.practice.QueueInfo;
import cn.mcarl.miars.storage.enums.FKitType;
import cn.mcarl.miars.storage.enums.QueueType;
import cn.mcarl.miars.storage.storage.data.PracticeQueueDataStorage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
                System.out.println("TICK:");
                for (QueueInfo q: PracticeQueueDataStorage.getInstance().getQueueInfos(FKitType.valueOf(PluginConfig.PRACTICE_SITE.MODE.get()))) {
                    System.out.println(q);
                    if (q.getQueueType().equals(QueueType.UNRANKED)){
                        Integer arenaId = ArenaManager.getInstance().isNullArena();
                        if (arenaId!=null){
                            List<Player> players = new ArrayList<>();
                            for (UUID u:q.getPlayers()) {
                                if (players.size()<2){
                                    players.add(Bukkit.getPlayer(u));
                                }else {
                                    // 匹配成功
                                    ArenaManager.getInstance().allotArena(players.get(0),players.get(1),arenaId);
                                    break;
                                }
                            }
                            System.out.println(players);
                        }
                        System.out.println(arenaId);

                    }
                }
            }
        }.runTaskTimerAsynchronously(MiarsPractice.getInstance(),20,20);
    }
}
