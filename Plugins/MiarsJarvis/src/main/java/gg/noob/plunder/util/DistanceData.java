// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.util;

public class DistanceData
{
    private final double x;
    private final double extra;
    private final double vertical;
    private final double z;
    private final double horizontal;
    private final double previousTickExtra;
    private final double reach;
    private final double angle;
    private final VerusCuboid hitbox;
    
    public DistanceData(final VerusCuboid hitbox, final double x, final double z, final double horizontal, final double angle, final double extra, final double vertical, final double reach, final double previousTickExtra) {
        this.hitbox = hitbox;
        this.x = x;
        this.z = z;
        this.horizontal = horizontal;
        this.angle = angle;
        this.extra = extra;
        this.vertical = vertical;
        this.reach = reach;
        this.previousTickExtra = previousTickExtra;
    }
    
    public VerusCuboid getHitbox() {
        return this.hitbox;
    }
    
    public double getPreviousTickExtra() {
        return this.previousTickExtra;
    }
    
    public double getX() {
        return this.x;
    }
    
    public double getAngle() {
        return this.angle;
    }
    
    public double getZ() {
        return this.z;
    }
    
    public double getHorizontal() {
        return this.horizontal;
    }
    
    public double getExtra() {
        return this.extra;
    }
    
    public double getReach() {
        return this.reach;
    }
    
    public double getVertical() {
        return this.vertical;
    }
}
