package io.github.Leonardo0013YT.UltraSkyWars.api.events.glass;

import io.github.Leonardo0013YT.UltraSkyWars.cosmetics.Glass;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class USWGlassDeleteEvent extends Event {

    private static final HandlerList HANDLERS_LIST = new HandlerList();
    private Player player;
    private Glass glass;
    private Location location;
    private boolean team;

    public USWGlassDeleteEvent(Player player, Glass glass, Location location, boolean team) {
        this.player = player;
        this.glass = glass;
        this.location = location;
        this.team = team;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

    public Player getPlayer() {
        return player;
    }

    public Glass getGlass() {
        return glass;
    }

    public Location getLocation() {
        return location;
    }

    public boolean isTeam() {
        return team;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

}