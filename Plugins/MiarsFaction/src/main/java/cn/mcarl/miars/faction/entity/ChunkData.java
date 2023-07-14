package cn.mcarl.miars.faction.entity;

import cn.mcarl.miars.faction.manager.FactionsTopManager;
import lombok.*;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

/**
 * @Author: carl0
 * @DATE: 2022/6/24 15:02
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChunkData {
    private int x, z;
    private long value;

    public Chunk toBukkit(World world) {
        return world.getChunkAt(x, z);
    }

    public static ChunkData toChunkData(Chunk chunk, boolean isCount) {
        if (isCount) {
            return new ChunkData(chunk.getX(), chunk.getZ(), getChunkValue(chunk));
        }
        return new ChunkData(chunk.getX(), chunk.getZ(), 0);
    }

    public static String toString(ChunkData chunkData) {
        return chunkData.getX() + "," + chunkData.getZ();
    }

    public static ChunkData StringToChunkData(String chunk) {
        String x = chunk.substring(0, chunk.indexOf(","));
        String z = chunk.substring(x.length() + 1);
        return new ChunkData(Integer.parseInt(x), Integer.parseInt(z), 0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChunkData chunkData = (ChunkData) o;
        return x == chunkData.x && z == chunkData.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, z);
    }

    public static long getChunkValue(Chunk chunk) {
        long value = 0;

        int x = chunk.getX() << 4;
        int z = chunk.getZ() << 4;

        World world = chunk.getWorld();

        for (int xx = x; xx < x + 16; xx++) {
            for (int zz = z; zz < z + 16; zz++) {
                for (int yy = 0; yy < 256; yy++) {
                    Block block = world.getBlockAt(xx, yy, zz);
                    if (block.getType().equals(Material.CHEST)) {
                        Chest chest = (Chest) block.getState();
                        for (ItemStack i : chest.getBlockInventory()) {
                            if (i == null) {
                                continue;
                            }
                            long v = FactionsTopManager.getInstance().getItems(i.getType().name(), i.getDurability());
                            value += v;
                        }
                    }
                    long v = FactionsTopManager.getInstance().getBlocks(block.getType().name(), new ItemStack(block.getType()).getDurability());
                    value += v;
                }
            }
        }

        for (Entity e : chunk.getEntities()) {
            long v = FactionsTopManager.getInstance().getSpawners(e.getType().name());
            value += v;
        }
        return value;
    }
}
