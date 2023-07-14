// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.util;

import java.util.ArrayDeque;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import java.util.Iterator;
import net.minecraft.server.v1_8_R3.AxisAlignedBB;
import org.bukkit.Location;
import java.util.concurrent.ConcurrentLinkedDeque;

public class LocationTrace
{
    private int size;
    private ConcurrentLinkedDeque<LocationTraceElement> locationTrace;
    
    public LocationTrace() {
        this(60);
    }
    
    public LocationTrace(final int size) {
        this.size = size;
        this.locationTrace = new ConcurrentLinkedDeque<LocationTraceElement>();
    }
    
    public void addLocation(final Location location, final AxisAlignedBB axisAlignedBB) {
        final LocationTraceElement locationTraceElement = new LocationTraceElement(location, axisAlignedBB);
        if (this.locationTrace.peek() != null && !this.locationTrace.peek().getLocation().getWorld().equals(locationTraceElement.getLocation().getWorld())) {
            this.locationTrace.clear();
        }
        if (this.locationTrace.size() == this.size) {
            this.locationTrace.pop();
        }
        this.locationTrace.add(locationTraceElement);
        this.removeOld();
    }
    
    private void removeOld() {
        if (this.locationTrace.size() == 0) {
            return;
        }
        final Iterator<LocationTraceElement> iterator = this.locationTrace.iterator();
        while (iterator.hasNext()) {
            final LocationTraceElement element = iterator.next();
            if (System.currentTimeMillis() - element.getTime() < 1000L) {
                continue;
            }
            iterator.remove();
        }
    }
    
    public void clear() {
        this.locationTrace.clear();
    }
    
    public Iterator<LocationTraceElement> trace(final Player player) {
        this.removeOld();
        if (this.locationTrace.size() == 0) {
            this.locationTrace.add(new LocationTraceElement(player.getLocation(), ((CraftPlayer)player).getHandle().getBoundingBox()));
        }
        return this.locationTrace.iterator();
    }
    
    public Iterator<LocationTraceElement> trace(final Player player, final int ping) {
        this.removeOld();
        final ArrayDeque<LocationTraceElement> customTrace = new ArrayDeque<LocationTraceElement>();
        for (final LocationTraceElement element : this.locationTrace) {
            if (System.currentTimeMillis() - element.getTime() < ping + ((ping > 200) ? 500 : 300)) {
                if (System.currentTimeMillis() - element.getTime() <= ping - ((ping > 200) ? 500 : 300)) {
                    continue;
                }
                customTrace.add(element);
            }
        }
        if (customTrace.size() == 0) {
            customTrace.add(new LocationTraceElement(player.getLocation(), ((CraftPlayer)player).getHandle().getBoundingBox()));
        }
        return customTrace.descendingIterator();
    }
}
