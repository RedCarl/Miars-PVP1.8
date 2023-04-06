package io.github.Leonardo0013YT.UltraSkyWars.managers;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.api.events.USWGameJoinEvent;
import io.github.Leonardo0013YT.UltraSkyWars.api.events.USWGameQuitEvent;
import io.github.Leonardo0013YT.UltraSkyWars.data.SWPlayer;
import io.github.Leonardo0013YT.UltraSkyWars.enums.GameType;
import io.github.Leonardo0013YT.UltraSkyWars.enums.State;
import io.github.Leonardo0013YT.UltraSkyWars.events.*;
import io.github.Leonardo0013YT.UltraSkyWars.game.GameData;
import io.github.Leonardo0013YT.UltraSkyWars.game.UltraGame;
import io.github.Leonardo0013YT.UltraSkyWars.game.UltraRankedGame;
import io.github.Leonardo0013YT.UltraSkyWars.game.UltraTeamGame;
import io.github.Leonardo0013YT.UltraSkyWars.objects.CustomJoin;
import io.github.Leonardo0013YT.UltraSkyWars.superclass.Game;
import io.github.Leonardo0013YT.UltraSkyWars.superclass.GameEvent;
import io.github.Leonardo0013YT.UltraSkyWars.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GameManager {

    private HashSet<String> modes = new HashSet<>();
    private Map<String, GameData> gameData = new HashMap<>();
    private Map<Integer, Game> games = new HashMap<>();
    private Map<String, Integer> gamesID = new HashMap<>();
    private Map<String, CustomJoin> joins = new HashMap<>();
    private Map<UUID, Integer> playerGame = new HashMap<>();
    private Map<String, GameEvent> events = new HashMap<>();
    private Map<Integer, String> gamesUpdating = new HashMap<>();
    private ArrayList<String> worlds = new ArrayList<>();
    private long lastUpdatePlayers = 0L;
    private Map<String, Integer> players = new HashMap<>();
    private Game bungee;
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
        events.put("none", new NoneEvent(plugin, 0));
        events.put("refill", new RefillEvent(plugin, 0));
        events.put("dragon", new DragonEvent(plugin, 0));
        events.put("tnt", new TNTEvent(plugin, 0));
        events.put("wither", new WitherEvent(plugin, 0));
        events.put("zombies", new ZombieEvent(plugin, 0));
        events.put("border", new BorderEvent(plugin, 0));
        if (plugin.getArenas().isSet("arenas")) {
            ConfigurationSection conf = plugin.getArenas().getConfig().getConfigurationSection("arenas");
            for (String c : conf.getKeys(false)) {
                int id = games.size();
                boolean ranked = plugin.getArenas().getBoolean("arenas." + c + ".ranked");
                int teamSize = plugin.getArenas().getInt("arenas." + c + ".teamSize");
                Game game;
                AtomicBoolean load = new AtomicBoolean(false);
                if (ranked) {
                    game = new UltraRankedGame(plugin, this, "arenas." + c, c, id, (b) -> setAtomic(b, c, load));
                } else {
                    if (teamSize > 1) {
                        game = new UltraTeamGame(plugin, this, "arenas." + c, c, id, (b) -> setAtomic(b, c, load));
                    } else {
                        game = new UltraGame(plugin, this, "arenas." + c, c, id, (b) -> setAtomic(b, c, load));
                    }
                }
                game.setState(State.WAITING);
                if (!load.get()) {
                    plugin.sendLogMessage("§cArena " + c + " no loaded correctly!");
                    continue;
                }
                games.put(id, game);
                gamesID.put(game.getName(), id);
                worlds.add(game.getName());
            }
        }
        if (plugin.getJoin().isSet("joins")) {
            ConfigurationSection j = plugin.getJoin().getConfig().getConfigurationSection("joins");
            for (String s : j.getKeys(false)) {
                String name = plugin.getJoin().get(null, "joins." + s + ".name");
                joins.put(name, new CustomJoin(plugin, "joins." + s));
            }
        }
    }

    public void setAtomic(String b, String c, AtomicBoolean load) {
        if (b.equals("NO_ISLANDS")) {
            plugin.sendLogMessage("Arena error §c" + c + " §eno islands.");
        }
        if (b.equals("NO_CENTERS")) {
            plugin.sendLogMessage("Arena error §c" + c + " §eno center chests.");
        }
        if (b.equals("NO_LOBBY")) {
            plugin.sendLogMessage("Arena error §c" + c + " §eno lobby set.");
        }
        if (b.equals("NO_SPECTATOR")) {
            plugin.sendLogMessage("Arena error §c" + c + " §eno spectator set.");
        }
        if (b.equals("DONE")) {
            load.set(true);
        }
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
    }

    public void removeGameServer(String server) {
        if (gameData.containsKey(server)) {
            gameData.get(server).setState("EMPTY");
        }
    }

    public void removeGameMap(String map) {
        gameData.remove(map);
    }

    public Game getBungee() {
        if (bungee == null) {
            if (games.size() > 0) {
                bungee = this.games.get(0);
            }
        }
        return bungee;
    }

    public synchronized void addPlayerGame(Player p, int id) {
        addPlayerGame(p, id, false);
    }

    public synchronized void addPlayerGame(Player p, int id, boolean ignoreParty) {
        Game game = games.get(id);
        if (checkPlayerGame(p, game)) return;
        if (!ignoreParty && plugin.getIjm().isParty() && plugin.getIjm().getParty().getPam().isLeader(p)) {
            boolean noRanked = game.getGameType().equals("RANKED") && plugin.getCm().isRankedJoin() && !plugin.getCm().isRankedJoinParties();
            if (!noRanked) {
                for (UUID uuid : plugin.getIjm().getParty().getPam().getPartyByPlayer(p.getUniqueId()).getMembers().keySet()) {
                    Player on = Bukkit.getPlayer(uuid);
                    if (on == null || !on.isOnline()) continue;
                    addPartyPlayer(on, p, game);
                }
            } else {
                p.sendMessage(plugin.getLang().get("messages.noGameRankedWithParty"));
            }
            return;
        }
        USWGameJoinEvent e = new USWGameJoinEvent(p, game);
        Bukkit.getServer().getPluginManager().callEvent(e);
        if (e.isCancelled()) {
            return;
        }
        if (plugin.getCm().isRankedJoin()){
            int l = plugin.getCm().getRankedLevels();
            boolean has = plugin.getLvl().getLevel(p).getLevel() >= l;
            if (game.getGameType().equals("RANKED") && !has) {
                p.sendMessage(plugin.getLang().get("messages.noLevelToRanked").replace("<level>", String.valueOf(l)));
                return;
            }
        }
        playerGame.put(p.getUniqueId(), id);
        game.addPlayer(p);
    }

    public void addPartyPlayer(Player on, Player leader, Game game) {
        if (checkPlayerGame(on, game)) return;
        if (on.equals(leader)) {
            on.sendMessage(plugin.getLang().get(on, "parties.join"));
        } else {
            on.sendMessage(plugin.getLang().get(on, "parties.joinGame"));
        }
        removePlayerAllGame(on);
        addPlayerGame(on, game.getId(), true);
    }

    private boolean checkPlayerGame(Player on, Game game) {
        if (game.getCached().contains(on)){
            return false;
        }
        if (game.isState(State.FINISH) || game.isState(State.RESTARTING) || game.isState(State.GAME) || game.isState(State.PREGAME) || (game.isState(State.STARTING) && game.getStarting() < 3)) {
            on.sendMessage(plugin.getLang().get(on, "messages.alreadyStart"));
            plugin.sendToServer(on, plugin.getCm().getLobbyServer());
            return true;
        }
        if (game.getCached().size() >= game.getMax()) {
            on.sendMessage(plugin.getLang().get(on, "messages.fullGame"));
            plugin.sendToServer(on, plugin.getCm().getLobbyServer());
            return true;
        }
        return false;
    }

    public void removePlayerGame(Player p, int id) {
        Game game = games.get(id);
        USWGameQuitEvent event = new USWGameQuitEvent(p, game);
        Bukkit.getServer().getPluginManager().callEvent(event);
        game.removePlayer(p);
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

    public synchronized void removePlayerAllGame(Player p) {
        if (!playerGame.containsKey(p.getUniqueId())) return;
        int id = playerGame.get(p.getUniqueId());
        removePlayerGame(p, id);
        plugin.getLvl().checkUpgrade(p);
        playerGame.remove(p.getUniqueId());
        Utils.updateSB(p);
        if (plugin.getCm().isBungeeModeEnabled() && plugin.getCm().isBungeeModeGame()) {
            plugin.sendToServer(p, plugin.getCm().getLobbyServer());
        }
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

    public Map<Integer, Game> getGames() {
        return games;
    }

    public Game getGameByPlayer(Player p) {
        return games.get(playerGame.get(p.getUniqueId()));
    }

    public boolean isPlayerInGame(Player p) {
        return playerGame.containsKey(p.getUniqueId());
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

    public Game getGameByName(String name) {
        for (Game g : games.values()) {
            if (g.getName().equals(name)) {
                return g;
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

    public void update() {
        for (int i : new ArrayList<>(gamesUpdating.keySet())) {
            if (!games.containsKey(i)) continue;
            Game game = games.get(i);
            game.update();
        }
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

    public GameEvent getEvent(String type) {
        return events.get(type);
    }

    public int getGameID(String map) {
        return gamesID.get(map);
    }

    public HashSet<String> getModes() {
        return modes;
    }

    public Map<UUID, Integer> getPlayerGame() {
        return playerGame;
    }
}