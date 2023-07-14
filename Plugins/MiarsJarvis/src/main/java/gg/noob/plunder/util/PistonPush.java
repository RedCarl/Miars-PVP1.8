// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.util;

import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;
import org.bukkit.World;

public class PistonPush
{
    private World world;
    private Vector position;
    private BlockFace direction;
    private long timestamp;
    
    public PistonPush(final World world, final Vector position, final BlockFace direction, final long timestamp) {
        this.world = world;
        this.position = position;
        this.direction = direction;
        this.timestamp = timestamp;
    }
    
    public World getWorld() {
        return this.world;
    }
    
    public Vector getPosition() {
        return this.position;
    }
    
    public BlockFace getDirection() {
        return this.direction;
    }
    
    public long getTimestamp() {
        return this.timestamp;
    }
}
