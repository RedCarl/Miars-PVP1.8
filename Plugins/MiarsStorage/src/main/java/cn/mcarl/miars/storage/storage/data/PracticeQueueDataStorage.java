package cn.mcarl.miars.storage.storage.data;

import cn.mcarl.miars.storage.MiarsStorage;
import cn.mcarl.miars.storage.entity.practice.QueueInfo;
import cn.mcarl.miars.storage.enums.FKitType;
import cn.mcarl.miars.storage.enums.QueueType;
import cn.mcarl.miars.storage.utils.ToolUtils;
import com.alibaba.fastjson.JSONArray;
import org.bukkit.entity.Player;

import java.util.*;

public class PracticeQueueDataStorage {

    private final String REDIS_KEY = "PRACTICE_QUEUE";
    private List<QueueInfo> queueInfos;
    private final Map<UUID,Long> queueTime = new HashMap<>();

    private static final PracticeQueueDataStorage instance = new PracticeQueueDataStorage();

    public PracticeQueueDataStorage(){
        try {
            queueInfos=new ArrayList<>(getQueueInfoRedisList());
        }catch (Exception error){
            MiarsStorage.getInstance().log(error.getMessage());
        }
    }

    public static PracticeQueueDataStorage getInstance() {
        return instance;
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
                    setQueueInfoRedisList(queueInfos); // 数据同步至Redis
                    return true;
                }else {
                    return false;
                }
            }
        }
        queueInfos.add(new QueueInfo(fKitType,queueType, Collections.singletonList(player.getUniqueId())));
        queueTime.put(player.getUniqueId(),System.currentTimeMillis()); // 匹配计时
        setQueueInfoRedisList(queueInfos); // 数据同步至Redis

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
                setQueueInfoRedisList(queueInfos); // 数据同步至Redis
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

    /**
     * 获取某个模式中正在匹配的队列
     * @param fKitType
     * @return
     */
    public List<QueueInfo> getQueueInfos(FKitType fKitType){
        List<QueueInfo> list = new ArrayList<>();

        for (QueueInfo q:queueInfos) {
            if (q.getFKitType().equals(fKitType)){
                list.add(q);
            }
        }

        System.out.println(queueInfos);

        return list;
    }

    public void clear(){
        setQueueInfoRedisList(new ArrayList<>());
    }
    
    public List<QueueInfo> getQueueInfoRedisList(){
        List<QueueInfo> list =JSONArray.parseArray(
                MiarsStorage.getRedisStorage().getJedis(REDIS_KEY)).toJavaList(QueueInfo.class);
        return Objects.requireNonNullElseGet(list, ArrayList::new);
    }
    
    public void setQueueInfoRedisList(List<QueueInfo> list){
        MiarsStorage.getRedisStorage().setJedis(REDIS_KEY,JSONArray.toJSON(list).toString());
    }

}
