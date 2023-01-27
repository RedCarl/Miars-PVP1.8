package cn.mcarl.miars.storage.storage.data;

import cn.mcarl.miars.storage.MiarsStorage;
import cn.mcarl.miars.storage.entity.practice.QueueInfo;
import cn.mcarl.miars.storage.enums.FKitType;
import cn.mcarl.miars.storage.enums.QueueType;
import cn.mcarl.miars.storage.utils.ToolUtils;
import org.bukkit.entity.Player;

import java.util.*;

public class QueueDataStorage {

    private static final QueueDataStorage instance = new QueueDataStorage();

    public static QueueDataStorage getInstance() {
        return instance;
    }

    private final List<QueueInfo> queueInfos = new ArrayList<>();

    private final Map<UUID,Long> queueTime = new HashMap<>();

    public void init(){
        MiarsStorage.getRedisStorage().setList("QUEUE",queueInfos);
    }

    /**
     * 将玩家添加进入队列
     * @param fKitType 模式类型
     * @param queueType 队列类型
     * @param player 玩家
     * @return 是否
     */
    public boolean addQueue(FKitType fKitType, QueueType queueType, Player player){

        for (QueueInfo q:queueInfos){
            if (q.getFKitType().equals(fKitType) && q.getQueueType().equals(queueType) && !q.getPlayers().contains(player.getUniqueId())){
                if (!q.getPlayers().contains(player.getUniqueId())){
                    q.getPlayers().add(player.getUniqueId());
                    queueTime.put(player.getUniqueId(),System.currentTimeMillis()); // 匹配计时
                    MiarsStorage.getRedisStorage().setList("QUEUE",queueInfos); // 数据同步至Redis
                    return true;
                }else {
                    return false;
                }
            }
        }
        queueInfos.add(new QueueInfo(fKitType,queueType, Collections.singletonList(player.getUniqueId())));
        queueTime.put(player.getUniqueId(),System.currentTimeMillis()); // 匹配计时
        MiarsStorage.getRedisStorage().setList("QUEUE",queueInfos); // 数据同步至Redis

        return true;
    }

    /**
     * 将玩家移出队列
     * @param player 玩家
     * @return 是否
     */
    public boolean removeQueue(Player player){
        for (QueueInfo q:queueInfos){
            if (q.getPlayers().contains(player.getUniqueId())){

                List<UUID> list = new ArrayList<>(q.getPlayers());
                list.remove(player.getUniqueId());
                q.setPlayers(list);
                queueTime.remove(player.getUniqueId()); // 匹配计时移移除
                MiarsStorage.getRedisStorage().setList("QUEUE",queueInfos); // 数据同步至Redis

//                Iterator<UUID> iterator = q.getPlayers().iterator();
//                while (iterator.hasNext()){
//                    UUID uuid = iterator.next();
//                    if (uuid==player.getUniqueId()){
//                        iterator.remove();
//                        queueTime.remove(player.getUniqueId()); // 匹配计时移移除
//                        MiarsStorage.getRedisStorage().setList("QUEUE",queueInfos); // 数据同步至Redis
//                    }
//                }
            }
        }
        return false;
    }

    /**
     * 查询玩家是否在队列中
     * @param player 玩家
     * @return 是否
     */
    public boolean isQueue(Player player){

        for (QueueInfo q:queueInfos){
            if (q.getPlayers().contains(player.getUniqueId())){
                return true;
            }
        }

        return false;

    }

    /**
     * 获取玩家当前的队列信息
     * @param player 玩家
     * @return 是否
     */
    public QueueInfo getQueue(Player player){

        for (QueueInfo q:queueInfos){
            if (q.getPlayers().contains(player.getUniqueId())){
                return q;
            }
        }

        return null;
    }

    /**
     * 获取玩家队列的时间
     * @param player 玩家
     * @return 是否
     */
    public String getQueueTime(Player player){
        return ToolUtils.getDate((System.currentTimeMillis()  - queueTime.get(player.getUniqueId()))/1000);
    }

    public void clear(){
        MiarsStorage.getRedisStorage().setList("QUEUE",null);
    }

}
