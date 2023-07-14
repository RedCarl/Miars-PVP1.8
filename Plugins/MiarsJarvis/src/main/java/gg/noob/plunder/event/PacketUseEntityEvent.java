// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.event;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

public class PacketUseEntityEvent extends Event implements Cancellable
{
    private static final HandlerList handlerList;
    private PacketPlayInUseEntity.EnumEntityUseAction Action;
    private boolean cancelled;
    private Player Attacker;
    private Entity Attacked;
    private int entityId;
    
    public PacketUseEntityEvent(final PacketPlayInUseEntity.EnumEntityUseAction Action, final Player Attacker, final Entity Attacked, final int entityId) {
        this.Action = Action;
        this.Attacker = Attacker;
        this.Attacked = Attacked;
        this.entityId = entityId;
    }
    
    public HandlerList getHandlers() {
        return PacketUseEntityEvent.handlerList;
    }
    
    public void setCancelled(final boolean cancelled) {
        this.cancelled = cancelled;
    }
    
    public boolean isCancelled() {
        return this.cancelled;
    }
    
    public PacketPlayInUseEntity.EnumEntityUseAction getAction() {
        return this.Action;
    }
    
    public Player getAttacker() {
        return this.Attacker;
    }
    
    public Entity getAttacked() {
        return this.Attacked;
    }
    
    public int getEntityId() {
        return this.entityId;
    }
    
    public static HandlerList getHandlerList() {
        return PacketUseEntityEvent.handlerList;
    }
    
    static {
        handlerList = new HandlerList();
    }
}
