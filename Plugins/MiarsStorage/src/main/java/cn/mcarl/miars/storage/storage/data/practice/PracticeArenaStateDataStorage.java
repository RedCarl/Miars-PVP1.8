package cn.mcarl.miars.storage.storage.data.practice;

import cn.mcarl.miars.storage.MiarsStorage;
import cn.mcarl.miars.storage.entity.practice.Arena;
import cn.mcarl.miars.storage.entity.practice.ArenaState;
import cn.mcarl.miars.storage.enums.practice.FKitType;
import cn.mcarl.miars.storage.enums.practice.QueueType;
import com.alibaba.fastjson.JSONArray;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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
    public ArenaState getArenaStateByPlayer(Player player){
        for (ArenaState a:arenaState) {
            if ((a.getPlayerA()!=null && a.getPlayerA().equals(player.getName())) || (a.getPlayerB()!=null && a.getPlayerB().equals(player.getName()))){
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
        return JSONArray.parseArray(
                MiarsStorage.getRedisStorage().getJedis(key)).toJavaList(ArenaState.class);
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
        data.setStartTime(System.currentTimeMillis());
        data.setQueueType(queueType);
        data.setFKitType(FKitType.valueOf(key));

        // 更新Redis房间信息
        PracticeArenaStateDataStorage.getInstance().setArenaStateRedisList(arenaState,key);

        PracticeQueueDataStorage.getInstance().removeQueue(a);
        PracticeQueueDataStorage.getInstance().removeQueue(b);
    }


    public List<ArenaState> getArenaStateByQueueAndFig(FKitType fKitType, QueueType queueType){
        List<ArenaState> list = new ArrayList<>();

        for (ArenaState a:arenaState){
            if (a.getFKitType().equals(fKitType) && a.getQueueType().equals(queueType) && a.getState() == ArenaState.State.GAME){
                list.add(a);
            }
        }

        return list;
    }

    public Integer getGamePlayersByQueueType(QueueType type){
        int i = 0;
        for (ArenaState a:arenaState) {
            if (a.getQueueType()==type && a.getState() != ArenaState.State.IDLE){
                i+=2;
            }
        }

        return i;
    }
}
