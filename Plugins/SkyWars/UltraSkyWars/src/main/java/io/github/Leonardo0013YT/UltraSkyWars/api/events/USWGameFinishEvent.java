package io.github.Leonardo0013YT.UltraSkyWars.api.events;

import io.github.Leonardo0013YT.UltraSkyWars.superclass.Game;
import io.github.Leonardo0013YT.UltraSkyWars.team.Team;
import io.github.Leonardo0013YT.UltraSkyWars.vote.Vote;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.Collection;

public class USWGameFinishEvent extends Event {

    private static final HandlerList HANDLERS_LIST = new HandlerList();
    private Game game;
    private Collection<Player> players;
    private Collection<Team> teams;
    private Vote vote;
    private boolean isCancelled = false;

    public USWGameFinishEvent(Game game, Collection<Player> players, Collection<Team> teams, Vote vote) {
        this.game = game;
        this.players = players;
        this.teams = teams;
        this.vote = vote;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

    public Collection<Player> getPlayers() {
        return players;
    }

    public Collection<Team> getTeams() {
        return teams;
    }

    public Game getGame() {
        return game;
    }

    public Vote getVote() {
        return vote;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

}