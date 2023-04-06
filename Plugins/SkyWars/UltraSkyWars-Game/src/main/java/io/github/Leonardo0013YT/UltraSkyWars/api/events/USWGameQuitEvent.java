package io.github.Leonardo0013YT.UltraSkyWars.api.events;

import io.github.Leonardo0013YT.UltraSkyWars.superclass.Game;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class USWGameQuitEvent extends Event {

    private static final HandlerList HANDLERS_LIST = new HandlerList();
    private Player player;
    private Game game;

    public USWGameQuitEvent(Player player, Game game) {
        this.player = player;
        this.game = game;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

    public Game getGame() {
        return game;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }
}