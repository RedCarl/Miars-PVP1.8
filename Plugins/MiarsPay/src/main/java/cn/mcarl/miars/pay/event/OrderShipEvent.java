package cn.mcarl.miars.pay.event;

import cn.mcarl.miars.pay.MiarsPay;
import org.jetbrains.annotations.NotNull;
import com.google.gson.JsonObject;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Cancellable;

public class OrderShipEvent extends GlazedPayEvent implements Cancellable
{
    private static final HandlerList handlerList;
    private JsonObject orderInfo;
    private boolean cancelled;
    
    public OrderShipEvent(final MiarsPay plugin, final JsonObject orderInfo) {
        super(plugin);
        this.orderInfo = orderInfo;
    }
    
    public JsonObject getOrderInfo() {
        return this.orderInfo;
    }
    
    public void setOrderInfo(final JsonObject orderInfo) {
        this.orderInfo = orderInfo;
    }
    
    public boolean isCancelled() {
        return this.cancelled;
    }
    
    public void setCancelled(final boolean cancelled) {
        this.cancelled = cancelled;
    }
    
    @NotNull
    public HandlerList getHandlers() {
        return OrderShipEvent.handlerList;
    }
    
    public static HandlerList getHandlerList() {
        return OrderShipEvent.handlerList;
    }
    
    static {
        handlerList = new HandlerList();
    }
}
