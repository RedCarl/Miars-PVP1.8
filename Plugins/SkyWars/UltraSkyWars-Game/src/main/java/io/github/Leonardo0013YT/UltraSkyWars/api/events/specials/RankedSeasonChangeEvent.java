package io.github.Leonardo0013YT.UltraSkyWars.api.events.specials;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class RankedSeasonChangeEvent extends Event {

    private static final HandlerList HANDLERS_LIST = new HandlerList();

    private int newSeason;

    public RankedSeasonChangeEvent(int newSeason) {
        this.newSeason = newSeason;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

    public int getNewSeason() {
        return newSeason;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

}