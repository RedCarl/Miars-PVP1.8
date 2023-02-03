package cn.mcarl.miars.practice.manager;

import cn.mcarl.miars.core.manager.ServerManager;
import cn.mcarl.miars.practice.MiarsPractice;
import cn.mcarl.miars.practice.conf.PluginConfig;
import cn.mcarl.miars.storage.MiarsStorage;
import cn.mcarl.miars.storage.entity.practice.Arena;
import cn.mcarl.miars.storage.entity.practice.ArenaState;
import cn.mcarl.miars.storage.enums.FKitType;
import cn.mcarl.miars.storage.storage.data.practice.PracticeArenaDataStorage;
import cn.mcarl.miars.storage.storage.data.practice.PracticeQueueDataStorage;
import com.alibaba.fastjson.JSONArray;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

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
        List<Arena> list = PracticeArenaDataStorage.getInstance().getArenaData(FKitType.valueOf(PluginConfig.PRACTICE_SITE.MODE.get()));
        for (Arena o:list){
            arenaData.add(o);
            arenaState.add(new ArenaState(
                    0,
                    o.getId(),
                    0,
                    null,
                    null,
                    null,
                    null,
                    0L,
                    0L,
                    null,
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
        state.setPlayerA(a);
        state.setPlayerB(b);
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

                return state.getArenaId();
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

    /**
     * 房间释放
     */
    public void releaseArena(Integer id){
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
        setArenaStateRedisList(arenaState);
    }

    /**
     * 游戏结束
     */
    public void endGame(Integer id){
        getArenaStateById(id).setState(3);
    }
    /**
     * 游戏开始
     */
    public void startGame(Integer id){
        getArenaStateById(id).setState(2);
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
