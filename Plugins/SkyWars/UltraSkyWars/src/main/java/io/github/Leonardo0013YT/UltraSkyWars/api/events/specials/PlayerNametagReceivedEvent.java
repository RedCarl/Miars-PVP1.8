package io.github.Leonardo0013YT.UltraSkyWars.api.events.specials;

import io.github.Leonardo0013YT.UltraSkyWars.nms.nametags.Nametags;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerNametagReceivedEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS_LIST = new HandlerList();
    private Player player;
    private Nametags green;
    private Nametags red;
    private boolean isCancelled = false;

    public PlayerNametagReceivedEvent(Player player, Nametags green, Nametags red) {
        this.player = player;
        this.green = green;
        this.red = red;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

    public Player getPlayer() {
        return player;
    }

    public Nametags getGreen() {
        return green;
    }

    public Nametags getRed() {
        return red;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean isCancelled) {
        this.isCancelled = isCancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

}