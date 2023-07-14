// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.util;

import java.util.HashMap;
import java.util.Iterator;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import java.util.Map;
import org.bukkit.event.Listener;

public class TraceManager implements Listener
{
    private static Map<Player, LocationTrace> playerTraces;
    
    public static void initiateTrace(final Player player) {
        TraceManager.playerTraces.put(player, new LocationTrace());
    }
    
    public static void removeTrace(final Player player) {
        TraceManager.playerTraces.remove(player);
    }
    
    public static void clearTrace(final Player player) {
        final LocationTrace trace = TraceManager.playerTraces.get(player);
        if (trace != null) {
            trace.clear();
        }
    }
    
    public static void addLocation(final Player player, final Location location) {
        final LocationTrace trace = TraceManager.playerTraces.get(player);
        if (trace == null) {
            TraceManager.playerTraces.put(player, new LocationTrace());
        }
        TraceManager.playerTraces.get(player).addLocation(location, ((CraftEntity)player).getHandle().getBoundingBox());
    }
    
    public static Iterator<LocationTraceElement> getTrace(final Player player, final int ping) {
        return TraceManager.playerTraces.get(player).trace(player, ping);
    }
    
    public static Iterator<LocationTraceElement> getTraceAll(final Player player) {
        return TraceManager.playerTraces.get(player).trace(player);
    }
    
    static {
        TraceManager.playerTraces = new HashMap<Player, LocationTrace>();
    }
}
