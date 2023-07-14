// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.util;

import net.minecraft.server.v1_8_R3.MathHelper;
import org.bukkit.util.Vector;

public class ReachUtils
{
    public static Vector calculateIntercept(final AABB self, final Vector origin, final Vector end) {
        Vector minX = getIntermediateWithXValue(origin, end, self.min.getX());
        Vector maxX = getIntermediateWithXValue(origin, end, self.max.getX());
        Vector minY = getIntermediateWithYValue(origin, end, self.min.getY());
        Vector maxY = getIntermediateWithYValue(origin, end, self.max.getY());
        Vector minZ = getIntermediateWithZValue(origin, end, self.min.getZ());
        Vector maxZ = getIntermediateWithZValue(origin, end, self.max.getZ());
        if (!isVecInYZ(self, minX)) {
            minX = null;
        }
        if (!isVecInYZ(self, maxX)) {
            maxX = null;
        }
        if (!isVecInXZ(self, minY)) {
            minY = null;
        }
        if (!isVecInXZ(self, maxY)) {
            maxY = null;
        }
        if (!isVecInXY(self, minZ)) {
            minZ = null;
        }
        if (!isVecInXY(self, maxZ)) {
            maxZ = null;
        }
        Vector best = null;
        if (minX != null) {
            best = minX;
        }
        if (maxX != null && (best == null || origin.distanceSquared(maxX) < origin.distanceSquared(best))) {
            best = maxX;
        }
        if (minY != null && (best == null || origin.distanceSquared(minY) < origin.distanceSquared(best))) {
            best = minY;
        }
        if (maxY != null && (best == null || origin.distanceSquared(maxY) < origin.distanceSquared(best))) {
            best = maxY;
        }
        if (minZ != null && (best == null || origin.distanceSquared(minZ) < origin.distanceSquared(best))) {
            best = minZ;
        }
        if (maxZ != null && (best == null || origin.distanceSquared(maxZ) < origin.distanceSquared(best))) {
            best = maxZ;
        }
        return best;
    }
    
    public static Vector getIntermediateWithXValue(final Vector self, final Vector other, final double x) {
        final double d0 = other.getX() - self.getX();
        final double d2 = other.getY() - self.getY();
        final double d3 = other.getZ() - self.getZ();
        if (d0 * d0 < 1.0000000116860974E-7) {
            return null;
        }
        final double d4 = (x - self.getX()) / d0;
        return (d4 >= 0.0 && d4 <= 1.0) ? new Vector(self.getX() + d0 * d4, self.getY() + d2 * d4, self.getZ() + d3 * d4) : null;
    }
    
    public static Vector getIntermediateWithYValue(final Vector self, final Vector other, final double y) {
        final double d0 = other.getX() - self.getX();
        final double d2 = other.getY() - self.getY();
        final double d3 = other.getZ() - self.getZ();
        if (d2 * d2 < 1.0000000116860974E-7) {
            return null;
        }
        final double d4 = (y - self.getY()) / d2;
        return (d4 >= 0.0 && d4 <= 1.0) ? new Vector(self.getX() + d0 * d4, self.getY() + d2 * d4, self.getZ() + d3 * d4) : null;
    }
    
    public static Vector getIntermediateWithZValue(final Vector self, final Vector other, final double z) {
        final double d0 = other.getX() - self.getX();
        final double d2 = other.getY() - self.getY();
        final double d3 = other.getZ() - self.getZ();
        if (d3 * d3 < 1.0000000116860974E-7) {
            return null;
        }
        final double d4 = (z - self.getZ()) / d3;
        return (d4 >= 0.0 && d4 <= 1.0) ? new Vector(self.getX() + d0 * d4, self.getY() + d2 * d4, self.getZ() + d3 * d4) : null;
    }
    
    private static boolean isVecInYZ(final AABB self, final Vector vec) {
        return vec != null && vec.getY() >= self.min.getY() && vec.getY() <= self.max.getY() && vec.getZ() >= self.min.getZ() && vec.getZ() <= self.max.getZ();
    }
    
    private static boolean isVecInXZ(final AABB self, final Vector vec) {
        return vec != null && vec.getX() >= self.min.getX() && vec.getX() <= self.max.getX() && vec.getZ() >= self.min.getZ() && vec.getZ() <= self.max.getZ();
    }
    
    private static boolean isVecInXY(final AABB self, final Vector vec) {
        return vec != null && vec.getX() >= self.min.getX() && vec.getX() <= self.max.getX() && vec.getY() >= self.min.getY() && vec.getY() <= self.max.getY();
    }
    
    public static Vector getLook(final float xRot, final float yRot) {
        final float f = MathHelper.cos(-xRot * 0.017453292f - 3.1415927f);
        final float f2 = MathHelper.sin(-xRot * 0.017453292f - 3.1415927f);
        final float f3 = -MathHelper.cos(-yRot * 0.017453292f);
        final float f4 = MathHelper.sin(-yRot * 0.017453292f);
        return new Vector(f2 * f3, f4, f * f3);
    }
    
    public static boolean isVecInside(final AABB self, final Vector vec) {
        return vec.getX() > self.min.getX() && vec.getX() < self.max.getX() && vec.getY() > self.min.getY() && vec.getY() < self.max.getY() && vec.getZ() > self.min.getZ() && vec.getZ() < self.max.getZ();
    }
}
