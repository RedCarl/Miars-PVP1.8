package cn.mcarl.miars.megawalls.game.entitiy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameWall {
    private Location minCorner;
    private Location maxCorner;
    private boolean collapse;
    private World world;
    private final List<Block> blocks = new ArrayList<>();
    public GameWall(Location pos1, Location pos2) {
        if (pos1 != null && pos2 != null && pos1.getWorld().getName().equals(pos2.getWorld().getName())) {
            this.world = pos1.getWorld();
            this.setMinMax(pos1, pos2);
            this.loadBlocks();
        }

    }
    private void setMinMax(Location pos1, Location pos2) {
        this.minCorner = this.getMinimumCorner(pos1, pos2);
        this.maxCorner = this.getMaximumCorner(pos1, pos2);
    }

    private Location getMinimumCorner(Location pos1, Location pos2) {
        return new Location(this.world, Math.min(pos1.getBlockX(), pos2.getBlockX()), Math.min(pos1.getBlockY(), pos2.getBlockY()), Math.min(pos1.getBlockZ(), pos2.getBlockZ()));
    }

    private Location getMaximumCorner(Location pos1, Location pos2) {
        return new Location(this.world, Math.max(pos1.getBlockX(), pos2.getBlockX()), Math.max(pos1.getBlockY(), pos2.getBlockY()), Math.max(pos1.getBlockZ(), pos2.getBlockZ()));
    }

    public List<Block> getBlocks() {
        return this.blocks;
    }

    private void loadBlocks() {

        for (int y = this.minCorner.getBlockY(); y <= this.maxCorner.getBlockY(); y++) {
            for (int x = this.minCorner.getBlockX(); x <= this.maxCorner.getBlockX() ; x++) {
                Location loc = new Location(this.world, x, y, minCorner.getBlockZ());
                if (loc.getBlock().getType() != Material.AIR) {
                    this.blocks.add(loc.getBlock());
                }
            }

            for (int z = this.minCorner.getBlockZ(); z <= this.maxCorner.getBlockZ() ; z++) {
                Location loc = new Location(this.world, minCorner.getBlockX(), y, z);
                if (loc.getBlock().getType() != Material.AIR) {
                    this.blocks.add(loc.getBlock());
                }
            }

            for (int x = this.maxCorner.getBlockX(); x >= this.minCorner.getBlockX() ; x--) {
                Location loc = new Location(this.world, x, y, maxCorner.getBlockZ());
                if (loc.getBlock().getType() != Material.AIR) {
                    this.blocks.add(loc.getBlock());
                }
            }

            for (int z = this.maxCorner.getBlockZ(); z >= this.minCorner.getBlockZ() ; z--) {
                Location loc = new Location(this.world, maxCorner.getBlockX(), y, z);
                if (loc.getBlock().getType() != Material.AIR) {
                    this.blocks.add(loc.getBlock());
                }
            }
        }

    }

    public boolean isInWall(Location location) {
        if (!location.getWorld().equals(this.world)) {
            return false;
        } else {
            return location.getBlockX() >= this.minCorner.getBlockX() && location.getBlockX() <= this.maxCorner.getBlockX() && location.getBlockY() >= this.minCorner.getBlockY() && location.getBlockY() <= this.maxCorner.getBlockY() && location.getBlockZ() >= this.minCorner.getBlockZ() && location.getBlockZ() <= this.maxCorner.getBlockZ();
        }
    }

    public void collapse() {
        if (!this.collapse) {
            this.collapse = true;

            for (Block block : this.blocks) {
                this.world.getBlockAt(block.getLocation()).setType(Material.AIR);
            }
        }
    }
}
