package io.github.Leonardo0013YT.UltraSkyWars.api.events.specials;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class RedisPartyMessageEvent extends Event {

    private static final HandlerList HANDLERS_LIST = new HandlerList();
    private String message;

    public RedisPartyMessageEvent(String message) {
        this.message = message;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

}