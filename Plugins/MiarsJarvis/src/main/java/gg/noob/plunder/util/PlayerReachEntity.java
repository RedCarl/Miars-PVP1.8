// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.util;

public class PlayerReachEntity
{
    public AABB.Vec3D serverPos;
    public ReachInterpolationData oldPacketLocation;
    public ReachInterpolationData newPacketLocation;
    public int lastTransactionHung;
    
    public PlayerReachEntity(final double x, final double y, final double z) {
        this.serverPos = new AABB.Vec3D(x, y, z);
        this.newPacketLocation = new ReachInterpolationData(GetBoundingBox.getBoundingBoxFromPosAndSize(x, y, z, 0.6, 1.8), this.serverPos.getX(), this.serverPos.getY(), this.serverPos.getZ());
        this.lastTransactionHung = 1;
    }
    
    public void onFirstTransaction(final double x, final double y, final double z) {
        this.oldPacketLocation = this.newPacketLocation;
        this.newPacketLocation = new ReachInterpolationData(this.oldPacketLocation.getPossibleLocationCombined(), x, y, z);
    }
    
    public void onSecondTransaction() {
        this.oldPacketLocation = null;
    }
    
    public void onMovement() {
        this.newPacketLocation.tickMovement(this.oldPacketLocation == null);
        if (this.oldPacketLocation != null) {
            this.oldPacketLocation.tickMovement(true);
            this.newPacketLocation.updatePossibleStartingLocation(this.oldPacketLocation.getPossibleLocationCombined());
        }
    }
    
    public AABB getPossibleCollisionBoxes() {
        if (this.oldPacketLocation == null) {
            return this.newPacketLocation.getPossibleLocationCombined();
        }
        return ReachInterpolationData.combineCollisionBox(this.oldPacketLocation.getPossibleLocationCombined(), this.newPacketLocation.getPossibleLocationCombined());
    }
}
