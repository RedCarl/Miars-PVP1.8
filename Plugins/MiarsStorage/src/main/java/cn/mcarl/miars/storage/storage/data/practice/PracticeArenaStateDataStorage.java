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


    public void init(String key){
        List<Arena> list = PracticeArenaDataStorage.getInstance().getArenaData(FKitType.valueOf(key));
        for (Arena o:list){
            arenaState.add(new ArenaState(
                    0,
                    o.getId(),
                    0,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    0L,
                    0L,
                    null,
                    null,
                    null
            ));
        }


        // 更新Redis房间信息
        setArenaStateRedisList(arenaState,key);
    }

    public ArenaState getArenaStateById(Integer id){
        AtomicReference<ArenaState> data = new AtomicReference<>(new ArenaState());

        arenaState.forEach(arenaState -> {
            if (arenaState.getArenaId().equals(id)){
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



    public Integer isNullArena(){
        for (ArenaState state:arenaState){
            if (state.getState()==0){

                return state.getArenaId();
            }
        }
        return null;
    }

    /**
     * 房间释放
     */
    public void releaseArena(Integer id,String key){
        getArenaStateById(id).setState(0);
        getArenaStateById(id).setPlayerA(null);
        getArenaStateById(id).setAFInventory(null);
        getArenaStateById(id).setPlayerB(null);
        getArenaStateById(id).setBFInventory(null);
        getArenaStateById(id).setStartTime(null);
        getArenaStateById(id).setEndTime(null);
        getArenaStateById(id).setWin(null);
        getArenaStateById(id).setFKitType(null);

        // 更新Redis房间信息
        setArenaStateRedisList(arenaState,key);
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
    public void allotArena(String a, String b, Integer id,String key,QueueType queueType){
        ArenaState state = PracticeArenaStateDataStorage.getInstance().getArenaStateById(id);
        state.setState(1);
        state.setPlayerA(a);
        state.setPlayerB(b);
        state.setStartTime(System.currentTimeMillis());
        state.setQueueType(queueType);
        state.setFKitType(FKitType.valueOf(key));

        // 更新Redis房间信息
        PracticeArenaStateDataStorage.getInstance().setArenaStateRedisList(arenaState,key);

        PracticeQueueDataStorage.getInstance().removeQueue(a);
        PracticeQueueDataStorage.getInstance().removeQueue(b);
    }


    public List<ArenaState> getArenaStateByQueueAndFig(FKitType fKitType, QueueType queueType){
        List<ArenaState> list = new ArrayList<>();

        for (ArenaState a:arenaState){
            if (a.getFKitType().equals(fKitType) && a.getQueueType().equals(queueType) && a.getState()==2){
                list.add(a);
            }
        }

        return list;
    }
}
