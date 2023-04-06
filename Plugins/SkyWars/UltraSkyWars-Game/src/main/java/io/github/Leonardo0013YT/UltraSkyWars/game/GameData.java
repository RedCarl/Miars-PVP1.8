package io.github.Leonardo0013YT.UltraSkyWars.game;

import io.github.Leonardo0013YT.UltraSkyWars.enums.State;
import lombok.Getter;
import lombok.Setter;

public class GameData {

    @Getter
    @Setter
    private String map, server, state, type;
    @Getter
    @Setter
    private int players, max;
    @Setter
    private String color;

    public GameData(String server, String map, String color, String state, String type, int players, int max) {
        this.map = map;
        this.server = server;
        this.color = color;
        this.state = state;
        this.players = players;
        this.max = max;
        this.type = type.toLowerCase();
    }

    public GameData(String map, String color, String state, String type, int players, int max) {
        this("", map, color, state, type, players, max);
    }

    public boolean isState(State state) {
        return State.valueOf(this.state.toUpperCase()).equals(state);
    }

    public String getColor() {
        if (color.equals("none")) {
            return "";
        }
        return color;
    }

}