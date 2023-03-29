package cn.mcarl.miars.practice.manager;

import cn.mcarl.miars.practice.conf.PluginConfig;
import cn.mcarl.miars.storage.entity.practice.Arena;
import cn.mcarl.miars.storage.entity.practice.ArenaState;
import cn.mcarl.miars.storage.enums.practice.FKitType;
import cn.mcarl.miars.storage.storage.data.practice.PracticeArenaStateDataStorage;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerEvent;

import java.util.*;

public class BuildUHCManager {
    private static final BuildUHCManager instance = new BuildUHCManager();

    public static BuildUHCManager getInstance() {
        return instance;
    }

    private Map<Integer,List<Block>> map = new HashMap<>();

    /**
     * 判断该位置的方块能不能破坏
     * @param block
     * @return
     */
    public boolean isBlockBreak(Arena arena,ArenaState state,Block block){

        if (state.getState() != ArenaState.State.GAME){
            return false;
        }

        if (map.get(arena.getId())==null){
            return false;
        }

        return map.get(arena.getId()).contains(block);
    }

    /**
     * 判断房间是否可以放置方块
     * @return
     */
    public boolean isBlockPlace(ArenaState state){
        if (state.getState() != ArenaState.State.GAME){
            return false;
        }

        return FKitType.valueOf(PluginConfig.PRACTICE_SITE.MODE.get()) == FKitType.BUILD_UHC;
    }

    /**
     * 放置方块，并存储方块数据
     * @param arena
     * @param block
     */
    public boolean placeBlock(Arena arena,ArenaState state, Block block){
        if (arena==null || state==null){
            if (FKitType.valueOf(PluginConfig.PRACTICE_SITE.MODE.get())!=FKitType.BUILD_UHC){
                return true;
            }
        }else {
            if (!isBlockPlace(state)){
                return true;
            }
        }

        List<Block> blocks = new ArrayList<>();
        if (this.map.get(arena.getId())!=null){
            blocks = this.map.get(arena.getId());
        }

        blocks.add(block);

        this.map.put(arena.getId(),blocks);

        return false;
    }

    /**
     * 破坏方块，并移除方块数据
     * @param arena
     * @param block
     */
    public boolean breakBlock(Arena arena,ArenaState state, Block block){
        if (!isBlockBreak(arena,state,block)){
            return true;
        }

        List<Block> blocks = this.map.get(arena.getId());

        blocks.remove(block);

        this.map.put(arena.getId(),blocks);

        return false;
    }

    /**
     * 清理方块
     * @param arena
     */
    public void clearBlock(Arena arena){
        List<Block> blocks = new ArrayList<>();

        if (this.map.get(arena.getId())!=null){
            blocks = this.map.get(arena.getId());
        }

        for (Block block:blocks) {
            block.setType(Material.AIR);
        }

        if (arena.getCenter()!=null){
            for (Entity entity:arena.getCenter().getWorld().getNearbyEntities(arena.getCenter(),32,256,32)) {
                if (entity instanceof Arrow){
                    entity.remove();
                }
            }
        }
    }
}
