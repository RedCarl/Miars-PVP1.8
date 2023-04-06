package io.github.Leonardo0013YT.UltraSkyWars.scoreboard;

import io.github.Leonardo0013YT.UltraSkyWars.scoreboard.bukkit.BPlayerBoard;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

import java.util.HashMap;
import java.util.Map;

public class Netherboard {

    private static Netherboard instance;

    private final Map<Player, BPlayerBoard> boards = new HashMap<>();

    private Netherboard() {
    }

    public static Netherboard instance() {
        if (instance == null)
            instance = new Netherboard();
        return instance;
    }

    public BPlayerBoard createBoard(Player player, String name) {
        return createBoard(player, null, name);
    }

    public BPlayerBoard createBoard(Player player, Scoreboard scoreboard, String name) {
        deleteBoard(player);
        BPlayerBoard board = new BPlayerBoard(player, scoreboard, name);
        boards.put(player, board);
        return board;
    }

    public void deleteBoard(Player player) {
        if (boards.containsKey(player))
            boards.get(player).delete();
    }

    public void removeBoard(Player player) {
        boards.remove(player);
    }

    public boolean hasBoard(Player player) {
        return boards.containsKey(player);
    }

    public BPlayerBoard getBoard(Player player) {
        return boards.get(player);
    }

    public Map<Player, BPlayerBoard> getBoards() {
        return new HashMap<>(boards);
    }

}
