package cn.mcarl.miars.faction.manager;

import cn.mcarl.miars.faction.entity.ChunkData;
import cn.mcarl.miars.faction.entity.FactionsEntity;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: carl0
 * @DATE: 2022/7/22 21:51
 */
public class FactionsTopManager {

    private final Map<String,Long> worth_Spawners = new HashMap<>();
    private final Map<String,Long> worth_Items = new HashMap<>();
    private final Map<String,Long> worth_Blocks = new HashMap<>();

    private static final FactionsTopManager instance = new FactionsTopManager();
    public static FactionsTopManager getInstance() {
        return instance;
    }
    public FactionsTopManager() {
        getAllWorth();
    }

    private void getAllWorth() {

    }

    public long getSpawners(String name){
        if (worth_Spawners.containsKey(name)){
            return worth_Spawners.get(name);
        }
        return 0;
    }

    public long getItems(String name,short id){
        if (worth_Items.containsKey(name+"-"+id)){
            return worth_Items.get(name+"-"+id);
        }
        return 0;
    }

    public long getBlocks(String name,short id){
        if (worth_Blocks.containsKey(name+"-"+id)){
            return worth_Blocks.get(name+"-"+id);
        }
        return 0;
    }
    
    
    public void countFactionsChunkValue(Block block, FactionsEntity factionsEntity){
        //派系区块价值计算 (TPS)
        if (FactionsTopManager.getInstance().getBlocks(block.getType().name(),new ItemStack(block.getType()).getDurability())!=0){
            for (ChunkData c:factionsEntity.getChunks()) {
                if (c.getX() == block.getChunk().getX()){
                    if (c.getZ() == block.getChunk().getZ()){
                        factionsEntity.getChunks().remove(c);
                        factionsEntity.getChunks().add(ChunkData.toChunkData(block.getChunk(),true));
                        FactionsManager.getInstance().saveFactions(factionsEntity);
                    }
                }
            }
        }
    }

    public long countFactionsValue(FactionsEntity factionsEntity){
        long value = 0;
        for (ChunkData c:factionsEntity.getChunks()) {
            value+=c.getValue();
        }
        return value;
    }
}
