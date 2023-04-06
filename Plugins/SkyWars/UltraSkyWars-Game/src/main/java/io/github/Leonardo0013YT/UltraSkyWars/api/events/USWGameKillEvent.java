package io.github.Leonardo0013YT.UltraSkyWars.api.events;

import io.github.Leonardo0013YT.UltraSkyWars.superclass.Game;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class USWGameKillEvent extends Event {

    private static final HandlerList HANDLERS_LIST = new HandlerList();
    private Player player;
    private Player death;
    private Game game;

    public USWGameKillEvent(Player player, Player death, Game game) {
        this.player = player;
        this.death = death;
        this.game = game;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

    public Game getGame() {
        return game;
    }

    public Player getDeath() {
        return death;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }
}