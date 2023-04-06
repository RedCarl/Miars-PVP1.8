package io.github.Leonardo0013YT.UltraSkyWars.api.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class RedisMessageEvent extends Event {

    private static final HandlerList HANDLERS_LIST = new HandlerList();
    private String channel;
    private String message;

    public RedisMessageEvent(String channel, String message) {
        this.channel = channel;
        this.message = message;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

    public String getChannel() {
        return channel;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

}