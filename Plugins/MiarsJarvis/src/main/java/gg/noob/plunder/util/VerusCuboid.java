// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.util;

import org.bukkit.World;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.server.v1_8_R3.MathHelper;
import org.bukkit.util.Vector;
import org.bukkit.Location;
import net.minecraft.server.v1_8_R3.BlockPosition;

public class VerusCuboid
{
    private double z1;
    private double z2;
    private double y1;
    private double x1;
    private double x2;
    private double y2;
    
    public VerusCuboid(final BlockPosition blockPosition) {
        this(blockPosition.getX(), blockPosition.getY(), blockPosition.getZ());
    }
    
    public VerusCuboid(final Pair<Long, Location> packetLocation) {
        this(packetLocation.getY().getX(), packetLocation.getY().getY(), packetLocation.getY().getZ());
    }
    
    public VerusCuboid(final double n, final double n2, final double n3) {
        this(n, n, n2, n2, n3, n3);
    }
    
    public VerusCuboid(final Vector vec, final double width, final double height) {
        this(vec.getX(), vec.getY(), vec.getZ(), vec.getX(), vec.getY(), vec.getZ());
        this.expand(width / 2.0, 0.0, width / 2.0);
        this.y2 += height;
    }
    
    public VerusCuboid(final double x1, final double x2, final double y1, final double y2, final double z1, final double z2) {
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
        this.z1 = z1;
        this.z2 = z2;
    }
    
    public VerusCuboid(final Pair<Long, Location> playerLocation, final Pair<Long, Location> playerLocation2) {
        this(Math.min(playerLocation.getY().getX(), playerLocation2.getY().getX()), Math.max(playerLocation.getY().getX(), playerLocation2.getY().getX()), Math.min(playerLocation.getY().getY(), playerLocation2.getY().getY()), Math.max(playerLocation.getY().getY(), playerLocation2.getY().getY()), Math.min(playerLocation.getY().getZ(), playerLocation2.getY().getZ()), Math.max(playerLocation.getY().getZ(), playerLocation2.getY().getZ()));
    }
    
    public VerusCuboid shrink(final double n, final double n2, final double n3) {
        this.x1 += n;
        this.x2 -= n;
        this.y1 += n2;
        this.y2 -= n2;
        this.z1 += n3;
        this.z2 -= n3;
        return this;
    }
    
    public double cX() {
        return (this.x1 + this.x2) * 0.5;
    }
    
    public double cZ() {
        return (this.z1 + this.z2) * 0.5;
    }
    
    public VerusCuboid copy() {
        return new VerusCuboid(this.x1, this.x2, this.y1, this.y2, this.z1, this.z2);
    }
    
    public boolean contains(final double n, final double n2, final double n3) {
        return this.x1 <= n && this.x2 >= n && this.y1 <= n2 && this.y2 >= n2 && this.z1 <= n3 && this.z2 >= n3;
    }
    
    public double distanceXZ(final Location playerLocation) {
        return this.distanceXZ(playerLocation.getX(), playerLocation.getZ());
    }
    
    public double distanceXZ(final double n, final double n2) {
        if (this.containsXZ(n, n2)) {
            return 0.0;
        }
        return MathHelper.sqrt(Math.min(Math.pow(n - this.x1, 2.0), Math.pow(n - this.x2, 2.0)) + Math.min(Math.pow(n2 - this.z1, 2.0), Math.pow(n2 - this.z2, 2.0)));
    }
    
    public boolean overlaps(final VerusCuboid cuboid) {
        return this.x1 <= cuboid.x2 && cuboid.x1 <= this.x1 && this.z1 <= cuboid.z2 && cuboid.z1 <= this.z1;
    }
    
    public double getX2() {
        return this.x2;
    }
    
    public void setX2(final double x2) {
        this.x2 = x2;
    }
    
    public VerusCuboid move(final double n, final double n2, final double n3) {
        this.x1 += n;
        this.x2 += n;
        this.y1 += n2;
        this.y2 += n2;
        this.z1 += n3;
        this.z2 += n3;
        return this;
    }
    
    public List<BlockPosition> getPositions() {
        final int n = (int)Math.floor(this.x1);
        final int n2 = (int)Math.ceil(this.x2);
        final int n3 = (int)Math.floor(this.y1);
        final int n4 = (int)Math.ceil(this.y2);
        final int n5 = (int)Math.floor(this.z1);
        final int n6 = (int)Math.ceil(this.z2);
        final ArrayList<BlockPosition> list = new ArrayList<BlockPosition>();
        list.add(new BlockPosition(n, n3, n5));
        for (int i = n; i < n2; ++i) {
            for (int j = n3; j < n4; ++j) {
                for (int k = n5; k < n6; ++k) {
                    list.add(new BlockPosition(i, j, k));
                }
            }
        }
        return list;
    }
    
    public double getZ2() {
        return this.z2;
    }
    
    public void setZ2(final double z2) {
        this.z2 = z2;
    }
    
    public VerusCuboid expand(final double n, final double n2, final double n3) {
        this.x1 -= n;
        this.x2 += n;
        this.y1 -= n2;
        this.y2 += n2;
        this.z1 -= n3;
        this.z2 += n3;
        return this;
    }
    
    public double getY2() {
        return this.y2;
    }
    
    public void setY2(final double y2) {
        this.y2 = y2;
    }
    
    public VerusCuboid fixY() {
        this.y1 = Math.max(0.0, this.y1);
        this.y2 = Math.min(256.0, this.y2);
        return this;
    }
    
    public double cY() {
        return (this.y1 + this.y2) * 0.5;
    }
    
    public boolean containsXZ(final double n, final double n2) {
        return this.x1 <= n && this.x2 >= n && this.z1 <= n2 && this.z2 >= n2;
    }
    
    public double getX1() {
        return this.x1;
    }
    
    public void setX1(final double x1) {
        this.x1 = x1;
    }
    
    public boolean contains(final Pair<Long, Location> playerLocation) {
        return this.contains(playerLocation.getY().getX(), playerLocation.getY().getY(), playerLocation.getY().getZ());
    }
    
    public VerusCuboid combine(final VerusCuboid cuboid) {
        return new VerusCuboid(Math.min(this.x1, cuboid.x1), Math.max(this.x2, cuboid.x2), Math.min(this.y1, cuboid.y1), Math.max(this.y2, cuboid.y2), Math.min(this.z1, cuboid.z1), Math.max(this.z2, cuboid.z2));
    }
    
    public boolean containsBlock(final World world, final Pair<Long, Location> packetLocation) {
        final int n = (int)Math.floor(packetLocation.getY().getX());
        final int n2 = (int)Math.floor(packetLocation.getY().getY());
        final int n3 = (int)Math.floor(packetLocation.getY().getZ());
        final int n4 = (int)Math.floor(this.x1);
        final int n5 = (int)Math.ceil(this.x2);
        final int max = Math.max((int)Math.floor(this.y1), 0);
        final int min = Math.min((int)Math.ceil(this.y2), world.getMaxHeight());
        final int n6 = (int)Math.floor(this.z1);
        final int n7 = (int)Math.ceil(this.z2);
        return n4 <= n && n5 > n && max <= n2 && min > n2 && n6 <= n3 && n7 > n3;
    }
    
    public double getZ1() {
        return this.z1;
    }
    
    public void setZ1(final double z1) {
        this.z1 = z1;
    }
    
    public boolean containsXZ(final Pair<Long, Location> playerLocation) {
        return this.containsXZ(playerLocation.getY().getX(), playerLocation.getY().getZ());
    }
    
    public VerusCuboid add(final VerusCuboid cuboid) {
        this.x1 += cuboid.x1;
        this.x2 += cuboid.x2;
        this.y1 += cuboid.y1;
        this.y2 += cuboid.y2;
        this.z1 += cuboid.z1;
        this.z2 += cuboid.z2;
        return this;
    }
    
    public double getY1() {
        return this.y1;
    }
    
    public void setY1(final double y1) {
        this.y1 = y1;
    }
    
    public boolean phase(final VerusCuboid cuboid) {
        return (this.x1 <= cuboid.x1 && this.x2 >= cuboid.x2) || (this.z1 <= cuboid.z1 && this.z2 >= cuboid.z2);
    }
}
