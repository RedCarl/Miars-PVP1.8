package cn.mcarl.miars.storage.storage.data.practice;

import cn.mcarl.miars.storage.MiarsStorage;
import cn.mcarl.miars.storage.entity.practice.ArenaState;
import cn.mcarl.miars.storage.entity.practice.enums.practice.FKitType;
import cn.mcarl.miars.storage.entity.practice.enums.practice.QueueType;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson2.JSONException;
import org.bukkit.entity.Player;

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

    List<ArenaState> arenaState = new ArrayList<>();



    public void init(String key,List<ArenaState> data){
        this.arenaState = data;
        // 更新Redis房间信息
        setArenaStateRedisList(arenaState,key);
    }

    public ArenaState getArenaStateById(ArenaState state){
        AtomicReference<ArenaState> data = new AtomicReference<>(new ArenaState());

        arenaState.forEach(arenaState -> {
            if (arenaState.getArenaId().equals(state.getArenaId()) && arenaState.getWorld().equals(state.getWorld())){
                data.set(arenaState);
            }
        });
        return data.get();
    }
    public ArenaState getArenaStateByPlayer(String player){
        for (ArenaState a:arenaState) {
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
        setArenaStateRedisList(arenaState,key);
    }

    public ArenaState isNullArena(){

        List<ArenaState> list = new ArrayList<>();

        for (ArenaState state:this.arenaState){
            if (state.getState() == ArenaState.State.IDLE){
                list.add(state);
            }
        }

        if (list.size()!=0){
            return list.get((int) (Math.random()* list.size()));
        }

        return null;
    }

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

    public void setArenaStateRedisList(List<ArenaState> list,String key){
        if (list.size()==0){
            MiarsStorage.getRedisStorage().delJedis(key);
        }
        MiarsStorage.getRedisStorage().setJedis(key,JSONArray.toJSON(list).toString());
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
        setArenaStateRedisList(arenaState,key);
    }



    // FFA
    public Integer getArenaStateByQueueAndFig(FKitType fKitType, QueueType queueType){
        int i = 0;
        for (ArenaState a:getArenaStateRedisList(fKitType.name())) {
            if (a.getQueueType()==queueType && a.getState() != ArenaState.State.IDLE){
                i+=2;
            }
        }
        return i;
    }

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
