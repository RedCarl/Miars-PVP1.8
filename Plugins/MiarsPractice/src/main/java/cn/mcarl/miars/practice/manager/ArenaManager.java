package cn.mcarl.miars.practice.manager;

import cn.mcarl.miars.core.manager.ServerManager;
import cn.mcarl.miars.practice.conf.PluginConfig;
import cn.mcarl.miars.storage.entity.practice.Arena;
import cn.mcarl.miars.storage.enums.FKitType;
import cn.mcarl.miars.storage.enums.QueueType;
import cn.mcarl.miars.storage.storage.data.practice.PracticeArenaDataStorage;
import cn.mcarl.miars.storage.storage.data.practice.PracticeArenaStateDataStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class ArenaManager {

    private static final ArenaManager instance = new ArenaManager();

    public static ArenaManager getInstance() {
        return instance;
    }

    List<Arena> arenaData = new ArrayList<>();

    public void init(){
        List<Arena> list = PracticeArenaDataStorage.getInstance().getArenaData(FKitType.valueOf(PluginConfig.PRACTICE_SITE.MODE.get()));
        arenaData.addAll(list);
        PracticeArenaStateDataStorage.getInstance().init(PluginConfig.PRACTICE_SITE.MODE.get());
    }

    /**
     * 房间分配,如果返回 NULL 就是没有房间
     */
    public Arena allotArena(String a, String b, Integer id, QueueType queueType){
        PracticeArenaStateDataStorage.getInstance().allotArena(a,b,id,PluginConfig.PRACTICE_SITE.MODE.get(),queueType);

        ServerManager.getInstance().sendPlayerToServer(a,cn.mcarl.miars.core.conf.PluginConfig.SERVER_INFO.NAME.get());
        ServerManager.getInstance().sendPlayerToServer(b,cn.mcarl.miars.core.conf.PluginConfig.SERVER_INFO.NAME.get());

        return getArenaById(id);
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

    /**
     * 房间释放
     */
    public void releaseArena(Integer id){
        PracticeArenaStateDataStorage.getInstance().releaseArena(id,PluginConfig.PRACTICE_SITE.MODE.get());
    }
    /**
     * 游戏结束
     */
    public void endGame(Integer id){
        PracticeArenaStateDataStorage.getInstance().getArenaStateById(id).setState(3);
    }
    /**
     * 游戏开始
     */
    public void startGame(Integer id){
        PracticeArenaStateDataStorage.getInstance().getArenaStateById(id).setState(2);
    }

    public void clear(){
        PracticeArenaStateDataStorage.getInstance().setArenaStateRedisList(new ArrayList<>(),PluginConfig.PRACTICE_SITE.MODE.get());
    }
}
