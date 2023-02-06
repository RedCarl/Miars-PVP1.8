package cn.mcarl.miars.pay.event;

import cn.mcarl.miars.pay.MiarsPay;
import org.jetbrains.annotations.NotNull;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class ClosePayEvent extends GlazedPayEvent
{
    private static final HandlerList handlerList;
    private final Player player;
    
    public ClosePayEvent(final MiarsPay plugin, final Player player) {
        super(plugin);
        this.player = player;
    }
    
    public Player getPlayer() {
        return this.player;
    }
    
    @NotNull
    public HandlerList getHandlers() {
        return ClosePayEvent.handlerList;
    }
    
    public static HandlerList getHandlerList() {
        return ClosePayEvent.handlerList;
    }
    
    static {
        handlerList = new HandlerList();
    }
}
