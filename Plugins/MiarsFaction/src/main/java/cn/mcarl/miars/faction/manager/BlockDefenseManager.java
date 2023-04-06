package cn.mcarl.miars.faction.manager;

import cn.mcarl.miars.faction.entity.BlockDefenseEntity;
import cn.mcarl.miars.faction.entity.FLocation;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.HashMap;
import java.util.List;

/**
 * @Author: carl0
 * @DATE: 2022/7/14 13:46
 */
public class BlockDefenseManager {

    private final HashMap<FLocation, BlockDefenseEntity> values = new HashMap<>();
    private static final BlockDefenseManager instance = new BlockDefenseManager();

    public static BlockDefenseManager getInstance() {
        return instance;
    }

    public BlockDefenseEntity getBlockValue(Block block) {
        if (getBlockDefaultValue(block.getType()) == 0) {
            return null;
        }

        if (!values.containsKey(FLocation.toFLocation(block.getLocation()))) {
            values.put(FLocation.toFLocation(block.getLocation()), new BlockDefenseEntity(System.currentTimeMillis(),getBlockDefaultValue(block.getType())));
        }
        BlockDefenseEntity blockDefenseEntity = values.get(FLocation.toFLocation(block.getLocation()));
        double max = getBlockDefaultValue(block.getType());
        double left = max - blockDefenseEntity.getHealthy();
        long now = System.currentTimeMillis();
        double health = blockDefenseEntity.getHealthy() + (left * (((now - (double) blockDefenseEntity.getLastAttack()) / 1000) / 3600));
        values.get(FLocation.toFLocation(block.getLocation())).setHealthy(health);
        return values.get(FLocation.toFLocation(block.getLocation()));
    }

    /**
     * 摧毁方块耐久，如果低于等于 0 方块消失
     * 返回 true 说明方块还在，返回 false 说明方块消失
     *
     * @param block 方块
     * @param value 受到伤害
     * @return 是否
     */
    public Block reduceBlockValue(Location source,Block block, double value) {
        if (source != null && source.getBlock().isLiquid()) {
            value = value * 0.35; //液体减伤
        }
        if (makeBlowable(block)){
            if (getBlockValue(block)!=null && (getBlockValue(block).getHealthy() - value) <= 0) {
                values.remove(FLocation.toFLocation(block.getLocation()));
                block.setType(Material.AIR);
                return null;
            }
            values.get(FLocation.toFLocation(block.getLocation())).setHealthy(getBlockValue(block).getHealthy() - value);
            values.get(FLocation.toFLocation(block.getLocation())).setLastAttack(System.currentTimeMillis());
            return null;
        }
        return block;
    }

    public double getBlockDefaultValue(Material material) {
        switch (material) {
            case OBSIDIAN:
                return 5.0;
        }
        return 0;
    }

    public List<Block> onBoom(Location source, List<Block> blocks, int damage, int dmgRadius) {
        int radius = (int) Math.ceil(dmgRadius);

        blocks.removeIf((block) -> !makeBlowable(block));

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    Location loc = new Location(source.getWorld(), x + source.getX(), y + source.getY(), z + source.getZ());
                    if (source.distance(loc) <= dmgRadius) {
                        Block block = reduceBlockValue(source,loc.getBlock(), damage);
                        if (block != null) blocks.add(block);

                    }
                }
            }
        }
        return blocks;
    }
    private boolean makeBlowable(Block block){
        return getBlockDefaultValue(block.getType()) != 0;
    }

}
