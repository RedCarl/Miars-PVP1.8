// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.util;

public class GetBoundingBox
{
    public static AABB getBoundingBoxFromPosAndSize(final double centerX, final double minY, final double centerZ, final double width, final double height) {
        final double minX = centerX - width / 2.0;
        final double maxX = centerX + width / 2.0;
        final double maxY = minY + height;
        final double minZ = centerZ - width / 2.0;
        final double maxZ = centerZ + width / 2.0;
        return new AABB(new AABB.Vec3D(minX, minY, minZ), new AABB.Vec3D(maxX, maxY, maxZ));
    }
}
