package cn.mcarl.miars.storage.storage.data.practice;

import cn.mcarl.miars.storage.MiarsStorage;
import cn.mcarl.miars.storage.entity.ffa.FPlayer;
import cn.mcarl.miars.storage.entity.practice.QueueInfo;
import cn.mcarl.miars.storage.enums.practice.FKitType;
import cn.mcarl.miars.storage.enums.practice.QueueType;
import com.alibaba.fastjson.JSONArray;
import lombok.Data;
import org.bukkit.entity.Player;

import java.util.*;

public class PracticeQueueDataStorage {
    private static final PracticeQueueDataStorage instance = new PracticeQueueDataStorage();

    public static PracticeQueueDataStorage getInstance() {
        return instance;
    }
    private final String REDIS_KEY = "PRACTICE_QUEUE";

    public PracticeQueueDataStorage(){
        queueInfos=getQueueInfoRedisList();
        if (queueInfos==null){
            queueInfos=new ArrayList<>();
        }
    }

    private List<QueueInfo> queueInfos;
    private final Map<String,Long> queueTime = new HashMap<>();

    /**
     * 将玩家添加进入队列
     * @param fKitType 模式类型
     * @param queueType 队列类型
     * @param fPlayer 玩家
     * @return 是否
     */
    public boolean addQueue(FKitType fKitType, QueueType queueType, FPlayer fPlayer){

        for (QueueInfo q:queueInfos){
            if (q.getFKitType().equals(fKitType) && q.getQueueType().equals(queueType) && !q.getPlayers().contains(fPlayer.getName())){
                if (!q.getPlayers().contains(fPlayer.getName())){

                    List<String> list = new ArrayList<>(q.getPlayers());
                    list.add(fPlayer.getName());
                    q.setPlayers(list);

                    queueTime.put(fPlayer.getName(),System.currentTimeMillis()); // 匹配计时
                    setQueueInfoRedisList(queueInfos); // 数据同步至Redis
                    return true;
                }else {
                    return false;
                }
            }
        }
        queueInfos.add(new QueueInfo(fKitType,queueType, Collections.singletonList(fPlayer.getName())));
        queueTime.put(fPlayer.getName(),System.currentTimeMillis()); // 匹配计时
        setQueueInfoRedisList(queueInfos); // 数据同步至Redis

        return true;
    }

    /**
     * 决斗
     * @param a
     * @param b
     */
    public boolean addDuel(FKitType fKitType,Player a,Player b){
        List<Duel> duels = new ArrayList<>(getDuelRedisList());

        for (Duel duel:duels) {
            if (
                    duel.getA().equals(a.getName()) ||
                            duel.getA().equals(b.getName()) ||
                            duel.getB().equals(a.getName()) ||
                            duel.getB().equals(b.getName())
            ){
                return false;
            }
        }

        duels.add(new Duel(
                fKitType,
                a.getName(),
                b.getName()
        ));
        MiarsStorage.getRedisStorage().setJedis(REDIS_KEY+"_DUEL",JSONArray.toJSON(duels).toString());
        return true;
    }
    @Data
    static
    class Duel{
        public Duel(FKitType fKitType, String a, String b) {
            this.fKitType = fKitType;
            this.a = a;
            this.b = b;
        }

        private FKitType fKitType;
        private String a;
        private String b;
    }

    /**
     * 将玩家移出队列
     * @param name 玩家
     * @return 是否
     */
    public boolean removeQueue(String name){
        for (QueueInfo q:queueInfos){
            if (q.getPlayers().contains(name)){

                List<String> list = new ArrayList<>(q.getPlayers());
                list.remove(name);
                q.setPlayers(list);

                queueTime.remove(name); // 匹配计时移移除
                setQueueInfoRedisList(queueInfos); // 数据同步至Redis
            }
        }
        return false;
    }

    /**
     * 查询玩家是否在队列中
     * @param fPlayer 玩家
     * @return 是否
     */
    public boolean isQueue(FPlayer fPlayer){
        return queueTime.containsKey(fPlayer.getName());
    }

    /**
     * 获取玩家当前的队列信息
     * @param fPlayer 玩家
     * @return 是否
     */
    public QueueInfo getQueue(FPlayer fPlayer){

        for (QueueInfo q:queueInfos){
            if (q.getPlayers().contains(fPlayer.getName())){
                return q;
            }
        }

        return null;
    }

    /**
     * 获取玩家队列的时间
     *
     * @param fPlayer 玩家
     * @return 是否
     */
    public long getQueueTime(FPlayer fPlayer){
        return (System.currentTimeMillis()  - queueTime.get(fPlayer.getName()))/1000;
    }

    /**
     * 获取某个模式中正在匹配的队列
     * @param fKitType
     * @return
     */
    public List<QueueInfo> getQueueInfos(FKitType fKitType,QueueType queueType){
        List<QueueInfo> list = new ArrayList<>();
        for (QueueInfo q:getQueueInfoRedisList()) {
            if (q.getFKitType().equals(fKitType) && q.getQueueType().equals(queueType)){
                list.add(q);
            }
        }

        return list;
    }

    public void clear(){
        setQueueInfoRedisList(new ArrayList<>());
    }
    
    public List<QueueInfo> getQueueInfoRedisList(){

        String json = MiarsStorage.getRedisStorage().getJedis(REDIS_KEY);

        if (json==null || "[]".equals(json)){
            return new ArrayList<>();
        }

        return JSONArray.parseArray(json).toJavaList(QueueInfo.class);
    }


    public List<Duel> getDuelRedisList(){

        String json = MiarsStorage.getRedisStorage().getJedis(REDIS_KEY+"_DUEL");

        if (json==null || "[]".equals(json)){
            return new ArrayList<>();
        }

        return JSONArray.parseArray(json).toJavaList(Duel.class);
    }

    public void setQueueInfoRedisList(List<QueueInfo> list){
        if (list.size()==0){
            MiarsStorage.getRedisStorage().delJedis(REDIS_KEY);
        }
        MiarsStorage.getRedisStorage().setJedis(REDIS_KEY,JSONArray.toJSON(list).toString());
    }

}
