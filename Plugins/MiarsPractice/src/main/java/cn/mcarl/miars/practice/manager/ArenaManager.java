package cn.mcarl.miars.practice.manager;

import cn.mcarl.miars.practice.conf.PluginConfig;
import cn.mcarl.miars.storage.MiarsStorage;
import cn.mcarl.miars.storage.entity.Arena;
import cn.mcarl.miars.storage.enums.FKitType;
import cn.mcarl.miars.storage.storage.data.ArenaDataStorage;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ArenaManager {

    private static final ArenaManager instance = new ArenaManager();

    public static ArenaManager getInstance() {
        return instance;
    }

    List<Arena> data = new ArrayList<>();

    public void init(){
        List<Arena> list = ArenaDataStorage.getInstance().getArenaData(FKitType.valueOf(PluginConfig.PRACTICE_SITE.MODE.get()));
        for (Arena o:list){
            o.setState(0);
            data.add(o);
        }
    }

    /**
     * 房间分配,如果返回 NULL 就是没有房间
     */
    public Arena allotArena(Player a,Player b){
        for (Arena arena:data){
            if (arena.getState()==0){
                arena.setState(1);
                arena.setA(a);
                arena.setB(b);
                arena.setStartTime(System.currentTimeMillis());

                // 更新Redis房间信息
                MiarsStorage.getRedisStorage().setList(PluginConfig.PRACTICE_SITE.MODE.get(), data);

                return arena;
            }
        }
        return null;
    }



}
