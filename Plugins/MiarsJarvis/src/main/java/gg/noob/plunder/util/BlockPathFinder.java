// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.util;

import org.bukkit.World;
import org.bukkit.Material;
import org.bukkit.Location;
import com.google.common.collect.Sets;
import java.util.Iterator;
import java.util.Set;
import org.bukkit.block.Block;

public class BlockPathFinder
{
    private static boolean isValidBlock(final LineBlock check, final Block end, final Set<LineBlock> banned) {
        for (final LineBlock block : banned) {
            if (block.equals(check)) {
                return false;
            }
        }
        final WrappedMaterial material = WrappedMaterial.get(check.getBlock(end.getWorld()));
        return material.getY() < 1.0 || material.getX() < 1.0 || material.getZ() < 1.0 || material.name().contains("FENCE") || !material.isSolid() || check.equals(end);
    }
    
    private static Set<LineBlock> line(final Block start, final Block end) {
        final Set<LineBlock> confirmed = Sets.newHashSet();
        final Set<LineBlock> banned = Sets.newHashSet();
        LineBlock current = new LineBlock(start, null);
        int iterations = 0;
        while (current != null && iterations++ != 60) {
            confirmed.add(current);
            if (current.equals(end)) {
                break;
            }
            if (current.getX() != end.getX()) {
                if (current.getX() < end.getX()) {
                    current = current.add(1, 0, 0);
                    if (isValidBlock(current, end, banned)) {
                        continue;
                    }
                    banned.add(current);
                    current = current.getParent();
                }
                else {
                    current = current.add(-1, 0, 0);
                    if (isValidBlock(current, end, banned)) {
                        continue;
                    }
                    banned.add(current);
                    current = current.getParent();
                }
            }
            if (current.getY() != end.getY()) {
                if (current.getY() < end.getY()) {
                    current = current.add(0, 1, 0);
                    if (isValidBlock(current, end, banned)) {
                        continue;
                    }
                    banned.add(current);
                    current = current.getParent();
                }
                else {
                    current = current.add(0, -1, 0);
                    if (isValidBlock(current, end, banned)) {
                        continue;
                    }
                    banned.add(current);
                    current = current.getParent();
                }
            }
            if (current.getZ() != end.getZ()) {
                if (current.getZ() < end.getZ()) {
                    current = current.add(0, 0, 1);
                    if (isValidBlock(current, end, banned)) {
                        continue;
                    }
                    banned.add(current);
                    current = current.getParent();
                }
                else {
                    current = current.add(0, 0, -1);
                    if (isValidBlock(current, end, banned)) {
                        continue;
                    }
                    banned.add(current);
                    current = current.getParent();
                }
            }
            banned.add(current);
            confirmed.remove(current);
            current = current.getParent();
        }
        if (iterations == 60) {
            confirmed.add(new LineBlock(end, null));
        }
        return confirmed;
    }
    
    public static Set<Material> line(final Location start, final Location end) {
        return toBlocks(start.getWorld(), line(start.getBlock(), end.getBlock()));
    }
    
    private static Set<Material> toBlocks(final World world, final Set<LineBlock> lineBlocks) {
        final Set<Material> blocks = Sets.newHashSet();
        for (final LineBlock lineBlock : lineBlocks) {
            blocks.add(lineBlock.getBlock(world));
        }
        return blocks;
    }
}
