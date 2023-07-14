// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.util;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

public class LineBlock
{
    private LineBlock parent;
    private int x;
    private int y;
    private int z;
    
    public LineBlock(final Block block, final LineBlock parent) {
        this(block.getX(), block.getY(), block.getZ(), parent);
    }
    
    public LineBlock(final int x, final int y, final int z, final LineBlock parent) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.parent = parent;
    }
    
    public LineBlock add(final int x, final int y, final int z) {
        return new LineBlock(this.x + x, this.y + y, this.z + z, this);
    }
    
    public boolean equals(final Block block) {
        return this.getX() == block.getX() && this.getY() == block.getY() && this.getZ() == block.getZ();
    }
    
    public boolean equals(final LineBlock block) {
        return this.getX() == block.getX() && this.getY() == block.getY() && this.getZ() == block.getZ();
    }
    
    public Material getBlock(final World world) {
        return BlockUtils.getBlockType(world, this.x, this.y, this.z);
    }
    
    public LineBlock getParent() {
        return this.parent;
    }
    
    public int getX() {
        return this.x;
    }
    
    public int getY() {
        return this.y;
    }
    
    public int getZ() {
        return this.z;
    }
}
