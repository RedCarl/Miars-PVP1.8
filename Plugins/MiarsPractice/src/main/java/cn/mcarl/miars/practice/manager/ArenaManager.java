package cn.mcarl.miars.practice.manager;

import cn.mcarl.miars.core.manager.ServerManager;
import cn.mcarl.miars.practice.conf.PluginConfig;
import cn.mcarl.miars.storage.MiarsStorage;
import cn.mcarl.miars.storage.entity.practice.Arena;
import cn.mcarl.miars.storage.entity.practice.ArenaState;
import cn.mcarl.miars.storage.enums.FKitType;
import cn.mcarl.miars.storage.storage.data.ArenaDataStorage;
import cn.mcarl.miars.storage.storage.data.PracticeQueueDataStorage;
import com.alibaba.fastjson.JSONArray;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class ArenaManager {

    private static final ArenaManager instance = new ArenaManager();

    public static ArenaManager getInstance() {
        return instance;
    }

    List<Arena> arenaData = new ArrayList<>();
    List<ArenaState> arenaState = new ArrayList<>();

    public void init(){
        List<Arena> list = ArenaDataStorage.getInstance().getArenaData(FKitType.valueOf(PluginConfig.PRACTICE_SITE.MODE.get()));
        for (Arena o:list){
            arenaData.add(o);
            arenaState.add(new ArenaState(
                    o.getId(),
                    0,
                    null,
                    null,
                    null,
                    null,
                    0L,
                    0L,
                    null
            ));
        }


        // 更新Redis房间信息
        setArenaStateRedisList(arenaState);
    }

    /**
     * 房间分配,如果返回 NULL 就是没有房间
     */
    public Arena allotArena(String a, String b, Integer id){
        ArenaState state = getArenaStateById(id);
        state.setState(1);
        state.setA(a);
        state.setB(b);
        state.setStartTime(System.currentTimeMillis());

        // 更新Redis房间信息
        setArenaStateRedisList(arenaState);

        PracticeQueueDataStorage.getInstance().removeQueue(a);
        PracticeQueueDataStorage.getInstance().removeQueue(b);

        ServerManager.getInstance().sendPlayerToServer(a,cn.mcarl.miars.core.conf.PluginConfig.SERVER_INFO.NAME.get());
        ServerManager.getInstance().sendPlayerToServer(b,cn.mcarl.miars.core.conf.PluginConfig.SERVER_INFO.NAME.get());

        return getArenaById(id);
    }

    public Integer isNullArena(){
        for (ArenaState state:arenaState){
            if (state.getState()==0){

                return state.getId();
            }
        }
        return null;
    }

    public Arena getArenaById(Integer id){
        AtomicReference<Arena> data = new AtomicReference<>(new Arena());

        arenaData.forEach(arena -> {
            if (arena.getId().equals(id)){
                data.set(arena);
            }
        });
        return data.get();
    }
    public ArenaState getArenaStateById(Integer id){
        AtomicReference<ArenaState> data = new AtomicReference<>(new ArenaState());

        arenaState.forEach(arenaState -> {
            if (arenaState.getId().equals(id)){
                data.set(arenaState);
            }
        });
        return data.get();
    }
    public ArenaState getArenaStateByPlayer(Player player){
        for (ArenaState a:arenaState) {
            if (a.getA().equals(player.getName()) || a.getB().equals(player.getName())){
                return a;
            }
        }
        return null;
    }

    /**
     * 房间释放
     */
    public void releaseArena(Integer id){
        getArenaStateById(id).setState(0);
        getArenaStateById(id).setA(null);
        getArenaStateById(id).setB(null);
        getArenaStateById(id).setEndTime(null);
        getArenaStateById(id).setStartTime(null);

        // 更新Redis房间信息
        setArenaStateRedisList(arenaState);
    }

    public void clear(){
        setArenaStateRedisList(new ArrayList<>());
    }





    public List<ArenaState> getArenaStateRedisList(){
        return JSONArray.parseArray(
                MiarsStorage.getRedisStorage().getJedis(PluginConfig.PRACTICE_SITE.MODE.get())).toJavaList(ArenaState.class);
    }

    public void setArenaStateRedisList(List<ArenaState> list){
        if (list.size()==0){
            MiarsStorage.getRedisStorage().delJedis(PluginConfig.PRACTICE_SITE.MODE.get());
        }
        MiarsStorage.getRedisStorage().setJedis(PluginConfig.PRACTICE_SITE.MODE.get(),JSONArray.toJSON(list).toString());
    }
}
