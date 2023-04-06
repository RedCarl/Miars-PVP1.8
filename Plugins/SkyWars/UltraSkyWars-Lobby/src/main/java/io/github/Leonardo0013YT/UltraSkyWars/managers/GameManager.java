package io.github.Leonardo0013YT.UltraSkyWars.managers;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.data.SWPlayer;
import io.github.Leonardo0013YT.UltraSkyWars.enums.GameType;
import io.github.Leonardo0013YT.UltraSkyWars.game.GameData;
import io.github.Leonardo0013YT.UltraSkyWars.objects.CustomJoin;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GameManager {

    private HashSet<String> modes = new HashSet<>();
    private Map<String, GameData> gameData = new HashMap<>();
    private Map<String, Integer> gamesID = new HashMap<>();
    private Map<String, CustomJoin> joins = new HashMap<>();
    private Map<Player, Integer> playerGame = new HashMap<>();
    private Map<Integer, String> gamesUpdating = new HashMap<>();
    private ArrayList<String> worlds = new ArrayList<>();
    private long lastUpdatePlayers = 0L;
    private Map<String, Integer> players = new HashMap<>();
    private UltraSkyWars plugin;

    public GameManager(UltraSkyWars plugin) {
        this.plugin = plugin;
    }

    public void reload() {
        modes.add("SOLO");
        modes.add("TEAM");
        modes.add("RANKED");
        modes.add("ALL");
        modes.add("TNT_MADNESS");
        modes.add("LUCKY");
        modes.add("RUSH");
        if (plugin.getJoin().isSet("joins")) {
            ConfigurationSection j = plugin.getJoin().getConfig().getConfigurationSection("joins");
            for (String s : j.getKeys(false)) {
                String name = plugin.getJoin().get(null, "joins." + s + ".name");
                joins.put(name, new CustomJoin(plugin, "joins." + s));
            }
        }
    }

    public HashSet<String> getModes() {
        return modes;
    }

    public void updateGame(String server, String map, String color, String state, String type, int players, int max) {
        if (!gameData.containsKey(server)) {
            gameData.put(server, new GameData(server, map, color, state, type.toLowerCase(), players, max));
        } else {
            GameData gd = gameData.get(server);
            gd.setState(state);
            gd.setPlayers(players);
            gd.setMax(max);
        }
        if (plugin.getIjm().isSignsInjection()) {
            plugin.getIjm().getSigns().getSim().update(type.toLowerCase(), server, map, state, players, max);
        }
    }

    public void removeGameServer(String server) {
        if (gameData.containsKey(server)) {
            gameData.get(server).setState("EMPTY");
        }
        if (plugin.getIjm().isSignsInjection()) {
            for (GameData gd : gameData.values()) {
                if (gd == null) continue;
                plugin.getIjm().getSigns().getSim().update(gd.getType().toLowerCase(), gd.getServer(), gd.getMap(), gd.getState(), gd.getPlayers(), gd.getMax());
            }
        }
    }

    public void removeGameMap(String map) {
        gameData.remove(map);
    }

    public GameData getGameRandomFavorites(Player p, String type) {
        SWPlayer sw = plugin.getDb().getSWPlayer(p);
        ArrayList<GameData> games = getGamesByType(type.toLowerCase()).stream().filter(d -> d.getState().equals("WAITING") || d.getState().equals("STARTING")).filter(d -> d.getPlayers() < d.getMax()).filter(game -> sw.getFavorites().contains(game.getMap())).collect(Collectors.toCollection(ArrayList::new));
        Collections.shuffle(games);
        int alt = 0;
        GameData g = null;
        for (GameData game : games) {
            if (g == null || alt <= game.getPlayers()) {
                g = game;
                alt = game.getPlayers();
            }
        }
        return g;
    }

    public boolean addRandomGame(Player p, String type) {
        List<GameData> now = new ArrayList<>(gameData.values());
        Collections.shuffle(now);
        int alt = 0;
        GameData g = null;
        Stream<GameData> filter = now.stream().filter(d -> d.getState().equals("WAITING") || d.getState().equals("STARTING")).filter(d -> d.getPlayers() < d.getMax());
        List<GameData> fixed;
        if (type.equals("ALL")) {
            fixed = filter.collect(Collectors.toList());
        } else {
            fixed = filter.filter(d -> d.getType().equalsIgnoreCase(type)).collect(Collectors.toList());
        }
        for (GameData game : fixed) {
            if (g == null || alt < game.getPlayers()) {
                g = game;
                alt = game.getPlayers();
            }
        }
        if (g != null) {
            if (!g.getServer().equals("")) {
                if (plugin.getIjm().isParty() && plugin.getIjm().getParty().getPam().isLeader(p)) {
                    plugin.getIjm().getParty().getPam().sendPartyServer(p.getUniqueId(), g.getServer());
                } else {
                    plugin.sendToServer(p, g.getServer());
                }
            }
            return true;
        }
        return false;
    }

    public GameData getDataByMap(String map) {
        for (GameData gd : gameData.values()) {
            if (gd.getMap().equals(map)) {
                return gd;
            }
        }
        return null;
    }

    public Map<String, GameData> getGameData() {
        return gameData;
    }

    public ArrayList<String> getWorlds() {
        return worlds;
    }

    public boolean isPlayerInGame(Player p) {
        return playerGame.containsKey(p);
    }

    public GameData getGameByPlayers(GameType type, int amount, List<GameData> ready) {
        List<GameData> games = getGamesByType(type.name());
        for (GameData game : games) {
            if (game.getPlayers() == amount && !ready.contains(game)) {
                return game;
            }
        }
        return null;
    }

    public int getGameSize(String type) {
        if (lastUpdatePlayers + plugin.getCm().getUpdatePlayersPlaceholder() < System.currentTimeMillis()) {
            updatePlayersPlaceholder();
        }
        return players.getOrDefault(type, 0);
    }

    public void updatePlayersPlaceholder() {
        for (GameType t : GameType.values()) {
            String type = t.name().toLowerCase();
            int count = 0;
            for (GameData g : getGamesByType(type)) {
                if (g == null) continue;
                count += g.getPlayers();
            }
            players.put(type, count);
        }
        for (String t : joins.keySet()) {
            CustomJoin cj = joins.get(t);
            int count = cj.getGameSize();
            players.put(t, count);
        }
        lastUpdatePlayers = System.currentTimeMillis();
    }

    public ArrayList<GameData> getGamesByType(String type) {
        ArrayList<GameData> games = new ArrayList<>();
        for (GameData gd : gameData.values()) {
            if (gd.getType().equals(type.toLowerCase())) {
                games.add(gd);
            }
        }
        return games;
    }

    public void addGameUpdating(int id) {
        if (!gamesUpdating.containsKey(id)) {
            gamesUpdating.put(id, "");
        }
    }

    public void removeGameUpdating(int id) {
        gamesUpdating.remove(id);
    }

    public Map<String, CustomJoin> getJoins() {
        return joins;
    }

    public int getGameID(String map) {
        return gamesID.get(map);
    }

    public Map<Player, Integer> getPlayerGame() {
        return playerGame;
    }
}