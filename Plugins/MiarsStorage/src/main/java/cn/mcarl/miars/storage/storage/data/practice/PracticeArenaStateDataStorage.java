package cn.mcarl.miars.storage.storage.data.practice;

import cn.mcarl.miars.storage.MiarsStorage;
import cn.mcarl.miars.storage.entity.practice.ArenaState;
import cn.mcarl.miars.storage.entity.practice.ArenaState;
import cn.mcarl.miars.storage.entity.practice.enums.practice.FKitType;
import cn.mcarl.miars.storage.entity.practice.enums.practice.QueueType;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @Author: carl0
 * @DATE: 2023/2/5 12:02
 */
public class PracticeArenaStateDataStorage {

    private static final PracticeArenaStateDataStorage instance = new PracticeArenaStateDataStorage();
    public static PracticeArenaStateDataStorage getInstance() {
        return instance;
    }

    List<ArenaState> arenaStates = new ArrayList<>();



    public void init(String key,List<ArenaState> data){
        this.arenaStates = data;
        // 更新Redis房间信息
        setArenaStateRedisList(arenaStates,key);
    }

    public ArenaState getArenaStateById(ArenaState state){
        AtomicReference<ArenaState> data = new AtomicReference<>(new ArenaState());

        arenaStates.forEach(arenaStates -> {
            if (arenaStates.getArenaId().equals(state.getArenaId()) && arenaStates.getWorld().equals(state.getWorld())){
                data.set(arenaStates);
            }
        });
        return data.get();
    }
    
    public ArenaState getArenaStateByPlayer(String player){
        for (ArenaState a:arenaStates) {
            if (
                    (a.getPlayerA()!=null && a.getPlayerA().equals(player)) ||
                    (a.getPlayerB()!=null && a.getPlayerB().equals(player))
            ){
                return a;
            }
        }
        return null;
    }

    public void updateRedis(String key){
        setArenaStateRedisList(arenaStates,key);
    }

    public ArenaState isNullArena(){

        List<ArenaState> list = new ArrayList<>();

        for (ArenaState state:this.arenaStates){
            if (state.getState() == ArenaState.State.IDLE){
                list.add(state);
            }
        }

        if (list.size()!=0){
            return list.get((int) (Math.random()* list.size()));
        }

        return null;
    }

    /**
     * 获取房间状态,注意获取频率
     * @param key
     * @return
     */
    public List<ArenaState> getArenaStateRedisList(String key){
        List<ArenaState> list = new ArrayList<>();
        try {
            list = JSONArray.parseArray(
                    MiarsStorage.getRedisStorage().getJedis(key)).toJavaList(ArenaState.class);
        }catch (JSONException e){
            MiarsStorage.getInstance().log(e.getMessage()+" / "+key);
        }

        return list;
    }

    /**
     * 设置房间状态
     * @param list
     * @param key
     */
    public void setArenaStateRedisList(List<ArenaState> list,String key){
        if (list.size()==0){
            MiarsStorage.getRedisStorage().delJedis(key);
            return;
        }
        list.forEach(ArenaState::formatRedis);
        MiarsStorage.getRedisStorage().setJedis(key, JSONArray.toJSONString(list));
    }

    /**
     * 房间分配,如果返回 NULL 就是没有房间
     */
    public void allotArena(String a, String b, ArenaState state,String key,QueueType queueType){
        ArenaState data = PracticeArenaStateDataStorage.getInstance().getArenaStateById(state);
        data.setPlayerA(a);
        data.setPlayerB(b);
        data.setQueueType(queueType);
        data.setFKitType(FKitType.valueOf(key));

        // 更新Redis房间信息
        setArenaStateRedisList(arenaStates,key);
    }


    /**
     * 获取正在游玩的人数By匹配类型+游戏模式
     * @param fKitType
     * @param queueType
     * @return
     */
    public Integer getArenaStateByQueueAndFig(FKitType fKitType, QueueType queueType){
        int i = 0;
        for (ArenaState a:getArenaStateRedisList(fKitType.name())) {
            if (a.getQueueType()==queueType && a.getState() != ArenaState.State.IDLE){
                i+=2;
            }
        }
        return i;
    }

    /**
     * 获取匹配中的玩家数量By匹配类型(普通 排位)
     * @param type
     * @return
     */
    public Integer getGamePlayersByQueueType(QueueType type){
        int i = 0;
        for (FKitType t:FKitType.values()) {
            for (ArenaState a:getArenaStateRedisList(t.name())) {
                if (a.getQueueType()==type && a.getState() != ArenaState.State.IDLE){
                    i+=2;
                }
            }
        }
        return i;
    }
}
