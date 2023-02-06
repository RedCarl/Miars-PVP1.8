package cn.mcarl.miars.pay.event;

import cn.mcarl.miars.pay.MiarsPay;
import org.bukkit.event.Event;

public abstract class GlazedPayEvent extends Event
{
    private final MiarsPay plugin;
    
    public GlazedPayEvent(final MiarsPay plugin) {
        this.plugin = plugin;
    }
    
    public GlazedPayEvent(final MiarsPay plugin, final boolean isAsync) {
        super(isAsync);
        this.plugin = plugin;
    }
    
    public MiarsPay getPlugin() {
        return this.plugin;
    }
}
