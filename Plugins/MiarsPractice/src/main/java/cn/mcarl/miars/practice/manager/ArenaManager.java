package cn.mcarl.miars.practice.manager;

import cn.mcarl.miars.practice.conf.PluginConfig;
import cn.mcarl.miars.storage.MiarsStorage;
import cn.mcarl.miars.storage.entity.practice.Arena;
import cn.mcarl.miars.storage.entity.practice.ArenaState;
import cn.mcarl.miars.storage.enums.FKitType;
import cn.mcarl.miars.storage.storage.data.ArenaDataStorage;
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
               0L,
               0L
            ));
        }


        // 更新Redis房间信息
        MiarsStorage.getRedisStorage().setList(PluginConfig.PRACTICE_SITE.MODE.get(), arenaState);
    }

    /**
     * 房间分配,如果返回 NULL 就是没有房间
     */
    public Arena allotArena(Player a,Player b){
        for (ArenaState state:arenaState){
            if (state.getState()==0){
                state.setState(1);
                state.setA(a);
                state.setB(b);
                state.setStartTime(System.currentTimeMillis());

                // 更新Redis房间信息
                MiarsStorage.getRedisStorage().setList(PluginConfig.PRACTICE_SITE.MODE.get(), arenaState);

                return getArenaById(state.getId());
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

    /**
     * 房间释放
     */
    public void releaseArena(Integer id){
        getArenaStateById(id).setState(0);

        // 更新Redis房间信息
        MiarsStorage.getRedisStorage().setList(PluginConfig.PRACTICE_SITE.MODE.get(), arenaState);
    }

    public void clear(){
        MiarsStorage.getRedisStorage().setList(PluginConfig.PRACTICE_SITE.MODE.get(), null);
    }



}
