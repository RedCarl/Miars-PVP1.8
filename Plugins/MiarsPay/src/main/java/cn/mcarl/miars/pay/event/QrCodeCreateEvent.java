package cn.mcarl.miars.pay.event;

import cn.mcarl.miars.pay.MiarsPay;
import cn.mcarl.miars.pay.enums.PaywayType;
import org.jetbrains.annotations.NotNull;
import java.awt.image.BufferedImage;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class QrCodeCreateEvent extends GlazedPayEvent
{
    private static final HandlerList handlerList;
    private final Player player;
    private final PaywayType paywayType;
    private final Double money;
    private BufferedImage qrCode;
    
    public QrCodeCreateEvent(final MiarsPay plugin, final Player player, final PaywayType paywayType, final Double money, final BufferedImage qrCode) {
        super(plugin, true);
        this.player = player;
        this.paywayType = paywayType;
        this.money = money;
        this.qrCode = qrCode;
    }
    
    public Player getPlayer() {
        return this.player;
    }
    
    public PaywayType getPaywayType() {
        return this.paywayType;
    }
    
    public Double getMoney() {
        return this.money;
    }
    
    public BufferedImage getQrCode() {
        return this.qrCode;
    }
    
    public void setQrCode(final BufferedImage qrCode) {
        this.qrCode = qrCode;
    }
    
    @NotNull
    public HandlerList getHandlers() {
        return QrCodeCreateEvent.handlerList;
    }
    
    public static HandlerList getHandlerList() {
        return QrCodeCreateEvent.handlerList;
    }
    
    static {
        handlerList = new HandlerList();
    }
}
