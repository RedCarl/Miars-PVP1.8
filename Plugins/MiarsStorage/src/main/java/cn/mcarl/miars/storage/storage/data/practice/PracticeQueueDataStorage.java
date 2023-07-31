package cn.mcarl.miars.storage.storage.data.practice;

import cn.mcarl.miars.storage.MiarsStorage;
import cn.mcarl.miars.storage.entity.ffa.FPlayer;
import cn.mcarl.miars.storage.entity.practice.Duel;
import cn.mcarl.miars.storage.entity.practice.QueueInfo;
import cn.mcarl.miars.storage.entity.practice.enums.practice.FKitType;
import cn.mcarl.miars.storage.entity.practice.enums.practice.QueueType;
import com.alibaba.fastjson.JSONArray;

import java.util.*;

public class PracticeQueueDataStorage {
    private static final PracticeQueueDataStorage instance = new PracticeQueueDataStorage();

    public static PracticeQueueDataStorage getInstance() {
        return instance;
    }
    private final String REDIS_KEY = "PRACTICE_QUEUE";

    public PracticeQueueDataStorage(){
        init();
    }

    private List<QueueInfo> queueInfos;
    private final Map<String,Long> queueTime = new HashMap<>();

    public void init(){
        queueInfos=getQueueInfoRedisList();
        if (queueInfos==null){
            queueInfos=new ArrayList<>();
        }
    }

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
     * 决斗申请
     */
    public boolean addDuel(Duel duel){
        List<Duel> duels = new ArrayList<>(getDuelRedisList());

        for (Duel d:duels) {
            if (d.getA().equals(duel.getA()) && d.getB().equals(duel.getB()) && d.getType().equals(duel.getType())){
                return false;
            }
        }

        duels.add(duel);
        setDuelRedisList(duels);
        return true;
    }

    public boolean acceptDuel(Duel duel){
        List<Duel> duels = new ArrayList<>(getDuelRedisList());

        for (Duel i:duels) {
            if (duel.getA().equals(i.getA()) && duel.getB().equals(i.getB())){
                // 接受的时候判断时间是否超时
                if ((System.currentTimeMillis() - i.getTime())/1000/60 >= 5){
                    return false;
                }
                i.setState(1);
                setDuelRedisList(duels);
                return true;
            }
        }

        return false;
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

    public boolean removeDuel(Duel duel){
        List<Duel> duels = new ArrayList<>(getDuelRedisList());

        for (Duel d:getDuelRedisList()){
            if (d.isEqual(duel)){
                duels.remove(d);
            }
        }

        setDuelRedisList(duels); // 数据同步至Redis
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
        if (queueTime.get(fPlayer.getName())==null){
            return 0;
        }
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

    public List<Duel> getDuels(FKitType fKitType){
        List<Duel> list = new ArrayList<>();
        for (Duel d:getDuelRedisList()) {
            if (d.getType().equals(fKitType) && d.getState()==1){
                list.add(d);
            }
        }
        return list;
    }

    public void clear(){
        setQueueInfoRedisList(new ArrayList<>());
        setDuelRedisList(new ArrayList<>());
    }
    
    public List<QueueInfo> getQueueInfoRedisList(){

        String json = MiarsStorage.getRedisStorage().getJedis(REDIS_KEY);

        if (json==null || "[]".equals(json)){
            return new ArrayList<>();
        }

        return JSONArray.parseArray(json).toJavaList(QueueInfo.class);
    }

    public void setQueueInfoRedisList(List<QueueInfo> list){
        if (list.size()==0){
            MiarsStorage.getRedisStorage().delJedis(REDIS_KEY);
        }
        MiarsStorage.getRedisStorage().setJedis(REDIS_KEY,JSONArray.toJSON(list).toString());
    }

    public List<Duel> getDuelRedisList(){

        String json = MiarsStorage.getRedisStorage().getJedis(REDIS_KEY+"_DUEL");

        if (json==null || "[]".equals(json)){
            return new ArrayList<>();
        }

        return JSONArray.parseArray(json).toJavaList(Duel.class);
    }
    public void setDuelRedisList(List<Duel> list){
        if (list.size()==0){
            MiarsStorage.getRedisStorage().delJedis(REDIS_KEY+"_DUEL");
        }
        MiarsStorage.getRedisStorage().setJedis(REDIS_KEY+"_DUEL",JSONArray.toJSON(list).toString());
    }

}
