// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.util;

public class ReachInterpolationData
{
    public final AABB targetLocation;
    public AABB startingLocation;
    public int interpolationStepsLowBound;
    public int interpolationStepsHighBound;
    
    public ReachInterpolationData(final AABB startingLocation, final double x, final double y, final double z) {
        this.interpolationStepsLowBound = 0;
        this.interpolationStepsHighBound = 0;
        this.startingLocation = startingLocation;
        this.targetLocation = GetBoundingBox.getBoundingBoxFromPosAndSize(x, y, z, 0.6, 1.8);
    }
    
    public AABB getPossibleLocationCombined() {
        final double stepMinX = (this.targetLocation.min.getX() - this.startingLocation.min.getX()) / 3.0;
        final double stepMaxX = (this.targetLocation.max.getX() - this.startingLocation.max.getX()) / 3.0;
        final double stepMinY = (this.targetLocation.min.getY() - this.startingLocation.min.getY()) / 3.0;
        final double stepMaxY = (this.targetLocation.max.getY() - this.startingLocation.max.getY()) / 3.0;
        final double stepMinZ = (this.targetLocation.min.getZ() - this.startingLocation.min.getZ()) / 3.0;
        final double stepMaxZ = (this.targetLocation.max.getZ() - this.startingLocation.max.getZ()) / 3.0;
        AABB minimumInterpLocation = new AABB(new AABB.Vec3D(this.startingLocation.min.getX() + this.interpolationStepsLowBound * stepMinX, this.startingLocation.getMin().getY() + this.interpolationStepsLowBound * stepMinY, this.startingLocation.min.getZ() + this.interpolationStepsLowBound * stepMinZ), new AABB.Vec3D(this.startingLocation.max.getX() + this.interpolationStepsLowBound * stepMaxX, this.startingLocation.max.getY() + this.interpolationStepsLowBound * stepMaxY, this.startingLocation.max.getZ() + this.interpolationStepsLowBound * stepMaxZ));
        for (int step = this.interpolationStepsLowBound + 1; step <= this.interpolationStepsHighBound; ++step) {
            minimumInterpLocation = combineCollisionBox(minimumInterpLocation, new AABB(new AABB.Vec3D(this.startingLocation.min.getX() + step * stepMinX, this.startingLocation.min.getY() + step * stepMinY, this.startingLocation.min.getZ() + step * stepMinZ), new AABB.Vec3D(this.startingLocation.max.getX() + step * stepMaxX, this.startingLocation.max.getY() + step * stepMaxY, this.startingLocation.max.getZ() + step * stepMaxZ)));
        }
        return minimumInterpLocation;
    }
    
    public static AABB combineCollisionBox(final AABB one, final AABB two) {
        final double minX = Math.min(one.min.getX(), two.min.getX());
        final double maxX = Math.max(one.max.getX(), two.max.getX());
        final double minY = Math.min(one.min.getY(), two.min.getY());
        final double maxY = Math.max(one.max.getY(), two.max.getY());
        final double minZ = Math.min(one.min.getZ(), two.min.getZ());
        final double maxZ = Math.max(one.max.getZ(), two.max.getZ());
        return new AABB(new AABB.Vec3D(minX, minY, minZ), new AABB.Vec3D(maxX, maxY, maxZ));
    }
    
    public void updatePossibleStartingLocation(final AABB possibleLocationCombined) {
        this.startingLocation = combineCollisionBox(this.startingLocation, possibleLocationCombined);
    }
    
    public void tickMovement(final boolean incrementLowBound) {
        if (incrementLowBound) {
            this.interpolationStepsLowBound = Math.min(this.interpolationStepsLowBound + 1, 3);
        }
        this.interpolationStepsHighBound = Math.min(this.interpolationStepsHighBound + 1, 3);
    }
}
