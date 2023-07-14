// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.util;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public class VelocityData
{
    public final Vector vector;
    public final int entityID;
    public final int transaction;
    public double offset;
    public Location to;
    public Location from;
    
    public VelocityData(final int entityID, final int transaction, final Vector vector, final Location to, final Location from) {
        this.offset = 2.147483647E9;
        this.entityID = entityID;
        this.vector = vector;
        this.transaction = transaction;
        this.to = to;
        this.from = from;
    }
    
    public VelocityData(final int entityID, final int transaction, final Vector vector, final double offset, final Location to, final Location from) {
        this.offset = 2.147483647E9;
        this.entityID = entityID;
        this.vector = vector;
        this.transaction = transaction;
        this.offset = offset;
        this.to = to;
        this.from = from;
    }
}
