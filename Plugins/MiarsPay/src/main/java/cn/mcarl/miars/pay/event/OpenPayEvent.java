package cn.mcarl.miars.pay.event;

import cn.mcarl.miars.pay.MiarsPay;
import cn.mcarl.miars.pay.enums.PaywayType;
import org.jetbrains.annotations.NotNull;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class OpenPayEvent extends GlazedPayEvent
{
    private static final HandlerList handlerList;
    private final Player player;
    private final String order;
    private final PaywayType paywayType;
    private final Double money;
    
    public OpenPayEvent(final MiarsPay plugin, final Player player, final String order, final PaywayType paywayType, final Double money) {
        super(plugin);
        this.player = player;
        this.order = order;
        this.paywayType = paywayType;
        this.money = money;
    }
    
    public Player getPlayer() {
        return this.player;
    }
    
    public String getOrder() {
        return this.order;
    }
    
    public PaywayType getPaywayType() {
        return this.paywayType;
    }
    
    public Double getMoney() {
        return this.money;
    }
    
    @NotNull
    public HandlerList getHandlers() {
        return OpenPayEvent.handlerList;
    }
    
    public static HandlerList getHandlerList() {
        return OpenPayEvent.handlerList;
    }
    
    static {
        handlerList = new HandlerList();
    }
}
