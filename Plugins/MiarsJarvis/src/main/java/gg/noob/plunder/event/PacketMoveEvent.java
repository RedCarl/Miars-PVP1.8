// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.event;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Event;

public class PacketMoveEvent extends Event
{
    private static final HandlerList handlerList;
    private Player player;
    
    public PacketMoveEvent(final Player player) {
        this.player = player;
    }
    
    public HandlerList getHandlers() {
        return PacketMoveEvent.handlerList;
    }
    
    public Player getPlayer() {
        return this.player;
    }
    
    public static HandlerList getHandlerList() {
        return PacketMoveEvent.handlerList;
    }
    
    static {
        handlerList = new HandlerList();
    }
}
