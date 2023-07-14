// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.util;

import net.minecraft.server.v1_8_R3.AxisAlignedBB;
import org.bukkit.Location;

public class LocationTraceElement
{
    private Location location;
    private long time;
    private AxisAlignedBB boundingbox;
    
    public LocationTraceElement(final Location location, final AxisAlignedBB boundingbox) {
        this.location = location;
        this.time = System.currentTimeMillis();
        this.boundingbox = new AxisAlignedBB(boundingbox.a, boundingbox.b, boundingbox.c, boundingbox.d, boundingbox.e, boundingbox.f);
    }
    
    public double lengthX() {
        return this.boundingbox.d - this.boundingbox.a;
    }
    
    public double lengthY() {
        return this.boundingbox.e - this.boundingbox.b;
    }
    
    public double lengthZ() {
        return this.boundingbox.f - this.boundingbox.c;
    }
    
    public Location getLocation() {
        return this.location;
    }
    
    public long getTime() {
        return this.time;
    }
    
    public AxisAlignedBB getBoundingbox() {
        return this.boundingbox;
    }
}
