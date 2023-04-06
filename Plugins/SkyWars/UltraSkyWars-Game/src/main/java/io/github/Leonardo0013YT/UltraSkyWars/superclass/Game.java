package io.github.Leonardo0013YT.UltraSkyWars.superclass;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.api.events.USWGameFinishEvent;
import io.github.Leonardo0013YT.UltraSkyWars.api.events.USWGameWinEvent;
import io.github.Leonardo0013YT.UltraSkyWars.calls.CallBackAPI;
import io.github.Leonardo0013YT.UltraSkyWars.data.SWPlayer;
import io.github.Leonardo0013YT.UltraSkyWars.enums.*;
import io.github.Leonardo0013YT.UltraSkyWars.game.GameChest;
import io.github.Leonardo0013YT.UltraSkyWars.game.GamePlayer;
import io.github.Leonardo0013YT.UltraSkyWars.game.GameWin;
import io.github.Leonardo0013YT.UltraSkyWars.game.UltraGameChest;
import io.github.Leonardo0013YT.UltraSkyWars.interfaces.KillEffect;
import io.github.Leonardo0013YT.UltraSkyWars.interfaces.WinDance;
import io.github.Leonardo0013YT.UltraSkyWars.interfaces.WinEffect;
import io.github.Leonardo0013YT.UltraSkyWars.managers.GameManager;
import io.github.Leonardo0013YT.UltraSkyWars.team.Team;
import io.github.Leonardo0013YT.UltraSkyWars.utils.CenterMessage;
import io.github.Leonardo0013YT.UltraSkyWars.utils.Utils;
import io.github.Leonardo0013YT.UltraSkyWars.vote.Vote;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public abstract class Game {

    public String gameType;
    public Collection<Player> players = new ArrayList<>(), spectators = new ArrayList<>(), cached = new ArrayList<>(), noDamaged = new ArrayList<>();
    public Map<Integer, Team> teams = new HashMap<>();
    public Map<UUID, Integer> playerTeam = new HashMap<>();
    public ArrayList<GameEvent> events = new ArrayList<>();
    public Collection<WinDance> winDances = new ArrayList<>();
    public Collection<WinEffect> winEffects = new ArrayList<>();
    public Collection<KillEffect> killEffects = new ArrayList<>();
    public Map<UUID, Long> enderpearls = new HashMap<>();
    public Map<UUID, GamePlayer> gamePlayer = new HashMap<>();
    public Map<Integer, Integer> balloons = new HashMap<>();
    public Map<Location, Integer> chests = new HashMap<>();
    public GameChest center;
    public int id, min, teamSize, max, starting, pregame, borderX, borderZ, event = 0, borderStart, borderEnd;
    public long started = 0;
    public State state;
    public UltraSkyWars plugin;
    public Location lobby, spectator;
    public String name, color;
    public Vote vote;
    public String chestType = "NORMAL";
    public FinalType finalType = FinalType.NONE;
    public HealthType healthType = HealthType.HEALTH10;
    public ProjectileType projectileType = ProjectileType.YESPROJ;
    public TimeType timeType = TimeType.DAY;
    public boolean inFinish = false, inReset = false, votes;

    public Game(UltraSkyWars plugin, GameManager gm, String path, String name, int id, CallBackAPI<String> correctly) {
        plugin.getWc().resetWorld(name);
        this.plugin = plugin;
        this.lobby = Utils.getStringLocation(plugin.getArenas().get(path + ".lobby"));
        this.spectator = Utils.getStringLocation(plugin.getArenas().get(path + ".spectator"));
        if (lobby == null || lobby.getWorld() == null) {
            correctly.done("NO_LOBBY");
            return;
        }
        if (spectator == null || spectator.getWorld() == null) {
            correctly.done("NO_SPECTATOR");
            return;
        }
        this.name = name;
        this.id = id;
        this.starting = plugin.getCm().getStarting();
        this.min = plugin.getArenas().getInt(path + ".min");
        this.votes = plugin.getArenas().getBooleanOrDefault(path + ".votes", true);
        this.color = plugin.getArenas().getOrDefault(path + ".color", "none");
        this.borderX = plugin.getArenas().getIntOrDefault(path + ".borderX", 0);
        this.borderZ = plugin.getArenas().getIntOrDefault(path + ".borderZ", 0);
        this.borderStart = plugin.getArenas().getIntOrDefault(path + ".borderStart", 300);
        this.borderEnd = plugin.getArenas().getIntOrDefault(path + ".borderEnd", 20);
        this.teamSize = Math.max(plugin.getArenas().getInt(path + ".teamSize"), 1);
        if (plugin.getArenas().isSet(path + ".islands")) {
            ConfigurationSection conf = plugin.getArenas().getConfig().getConfigurationSection(path + ".islands");
            for (String c : conf.getKeys(false)) {
                int tid = teams.size();
                Team t = new Team(plugin, this, path + ".islands." + c, tid);
                teams.put(tid, t);
                t.getChest().getInvs().keySet().forEach(e -> {
                    e.getBlock().setMetadata("TEAM_ID_CHEST", new FixedMetadataValue(plugin, id));
                    chests.put(e, tid);
                });
            }
        } else {
            correctly.done("NO_ISLANDS");
            return;
        }
        List<Location> cen = new ArrayList<>();
        if (plugin.getArenas().isSet(path + ".center")) {
            for (String c : plugin.getArenas().getList(path + ".center")) {
                cen.add(Utils.getStringLocation(c));
            }
        } else {
            correctly.done("NO_CENTERS");
            return;
        }
        List<String> eve = plugin.getArenas().getListOrDefault(path + ".events", new ArrayList<>());
        int s = 0;
        for (String e : eve) {
            String[] ev = e.split(":");
            String type = ev[0];
            s += Integer.parseInt(ev[1]);
            if (type.equals("REFILL")) {
                GameEvent rev = gm.getEvent("refill").clone();
                rev.setTime(s);
                events.add(rev);
            }
            if (type.equals("FINAL")) {
                GameEvent rev = gm.getEvent("none").clone();
                rev.setTime(s);
                events.add(rev);
            }
        }
        this.max = teams.size() * teamSize;
        this.center = new GameChest(true, cen);
        center.getInvs().keySet().forEach(l -> {
            l.getBlock().setMetadata("TEAM_ID_CHEST", new FixedMetadataValue(plugin, id));
            chests.put(l, -1);
        });
        this.vote = new Vote(plugin, this);
        this.lobby.getWorld().getEntities().stream().filter(e -> !e.getType().equals(EntityType.PLAYER)).forEach(Entity::remove);
        correctly.done("DONE");
    }

    public void debug() {
        if ((isState(State.GAME) || isState(State.FINISH) || isState(State.RESTARTING)) && players.isEmpty()) {
            setState(State.RESTARTING);
            if (!inReset) {
                inReset = true;
                reset(false);
                UltraSkyWars plugin = UltraSkyWars.get();
                plugin.getGm().removeGameUpdating(id);
            }
        }
    }

    public void preJoinGame(Player p) {
        p.setGameMode(GameMode.ADVENTURE);
        cached.add(p);
        players.add(p);
        vote.addVotePlayer(p);
        if (players.size() >= max && starting > 10) {
            starting = 10;
            sendGameMessage(plugin.getLang().get("messages.gameFull"));
        }
        gamePlayer.put(p.getUniqueId(), new GamePlayer(p, this, false));
        Utils.setCleanPlayer(p);
        sendGameSound(CustomSound.JOIN_PLAYER);
        sendGameMessage(plugin.getLang().get(p, "messages.join").replace("<suffix>", plugin.getAdm().getPlayerSuffix(p)).replace("<prefix>", plugin.getAdm().getPlayerPrefix(p)).replaceAll("<player>", p.getName()).replaceAll("<players>", String.valueOf(players.size())).replaceAll("<max>", String.valueOf(getMax())));
        if (plugin.getCm().isPreLobbySystem()) {
            p.teleport(lobby);
        } else {
            if (getTeamPlayer(p) == null) {
                joinRandomTeam(p);
            }
            Team team = getTeamPlayer(p);
            p.teleport(team.getSpawn().getBlock().getLocation().clone().add(0.5, 0.5, 0.5));
        }
        plugin.getGm().updateGame(plugin.getCm().getGameServer(), name, color, state.name(), gameType, players.size(), max);
        plugin.getBm().sendUpdateGame(this);
        plugin.getGem().updateInventories(gameType, "none");
        checkStart();
        plugin.getGm().getPlayerGame().put(p.getUniqueId(), id);
        Utils.updateSB(this);
    }

    public void reset(boolean add) {
        if (add){
            if (plugin.getCm().isEnabledGameRestart()){
                plugin.getBm().setGames(plugin.getBm().getGames() + 1);
                if (plugin.getBm().getGames() >= plugin.getCm().getGamesToRestart()) {
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, Bukkit::shutdown, 20L);
                    return;
                }
            }
        }
        World s = spectator.getWorld();
        events.forEach(e -> e.stop(this));
        players.clear();
        playerTeam.clear();
        spectators.clear();
        cached.clear();
        noDamaged.clear();
        enderpearls.clear();
        gamePlayer.clear();
        chests.clear();
        event = 0;
        vote.reset();
        events.forEach(GameEvent::reset);
        starting = plugin.getCm().getStarting();
        plugin.getWc().deleteWorld(s, true, 0, (w) -> new BukkitRunnable() {
            @Override
            public void run() {
                inFinish = false;
                updateWorld(w);
                lobby.getWorld().setTime(500);
                lobby.getWorld().getEntities().stream().filter(e -> !e.getType().equals(EntityType.PLAYER)).forEach(Entity::remove);
                teams.values().forEach(t -> {
                    t.reset();
                    t.getChest().getInvs().keySet().forEach(e -> {
                        e.getBlock().setMetadata("TEAM_ID_CHEST", new FixedMetadataValue(plugin, id));
                        chests.put(e, t.getId());
                    });
                });
                List<Location> chests = new ArrayList<>(center.getInvs().keySet());
                center = new GameChest(true, chests);
                center.getInvs().keySet().forEach(l -> {
                    l.getBlock().setMetadata("TEAM_ID_CHEST", new FixedMetadataValue(plugin, id));
                    Game.this.chests.put(l, -1);
                });
                setState(State.WAITING);
                inReset = false;
                resetTime();
            }
        }.runTaskLater(plugin, 20L));
    }

    public UltraGameChest getChestByLocation(Location loc) {
        if (chests.containsKey(loc)) {
            int id = chests.get(loc);
            if (id == -1) {
                return center.getInvs().get(loc);
            } else {
                return teams.get(id).getChest().getInvs().get(loc);
            }
        }
        return null;
    }

    public int isEnderPearlDisabled(){
        return (started + (plugin.getCm().getEnderPearlSeconds() * 1000L) > System.currentTimeMillis()) ? 2 : 0;
    }

    public Map<Location, Integer> getChests() {
        return chests;
    }

    public void updateWorld(World w) {
        lobby.setWorld(w);
        spectator.setWorld(w);
        for (Location l : center.getChests()) {
            l.setWorld(w);
        }
        for (Location l : center.getInvs().keySet()) {
            l.setWorld(w);
        }
        for (Team t : teams.values()) {
            t.updateWorld(w);
        }
    }

    public void remove(Player p) {
        removePlayerAllTeam(p);
        players.remove(p);
        noDamaged.remove(p);
        enderpearls.remove(p.getUniqueId());
        if (!spectators.contains(p)) {
            spectators.add(p);
        }
    }

    public void forceStart(Player p) {
        setState(State.STARTING);
        starting = 5;
        sendGameMessage(plugin.getLang().get("messages.forceStart").replaceAll("<player>", p.getName()));
        plugin.getGm().addGameUpdating(id);
    }

    public void setSpect(Player p) {
        if (gamePlayer.containsKey(p.getUniqueId())) {
            gamePlayer.get(p.getUniqueId()).setDead(true);
        } else {
            GamePlayer gp = new GamePlayer(p, this, true);
            gamePlayer.put(p.getUniqueId(), gp);
            gp.setDead(true);
        }
        Utils.setCleanPlayer(p);
        p.setAllowFlight(true);
        p.setFlying(true);
        p.teleport(spectator);
        p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 999999, 999999));
        p.getInventory().clear();
        p.getInventory().setItem(0, plugin.getIm().getSpectate());
        p.getInventory().setItem(4, plugin.getIm().getOptions());
        p.getInventory().setItem(7, plugin.getIm().getPlay());
        p.getInventory().setItem(8, plugin.getIm().getLeave());
        spectators.forEach(s -> s.hidePlayer(p));
        players.forEach(pl -> pl.hidePlayer(p));
        updateSpectatorOptions(p);
        plugin.getGm().updateGame(plugin.getCm().getGameServer(), name, color, state.name(), gameType, players.size(), max);
        plugin.getBm().sendUpdateGame(this);
        plugin.getGem().updateInventories(gameType, "none");
        checkWin();
    }

    public void checkStart() {
        if (isState(State.WAITING)) {
            if (min <= players.size()) {
                updateVisible();
                setState(State.STARTING);
                sendGameMessage(plugin.getLang().get("messages.minPlayers"));
                plugin.getGm().addGameUpdating(id);
            }
        }
    }

    public void checkCancel() {
        if (isState(State.STARTING)) {
            if (min > players.size()) {
                cancel();
                plugin.getGm().removeGameUpdating(id);
            }
        }
    }

    public void update() {
        if (isState(State.WAITING) || isState(State.STARTING) || isState(State.PREGAME)) {
            updateBar();
        }
        if (isState(State.STARTING)) {
            updateStarting();
        }
        if (isState(State.PREGAME)) {
            updatePreGame();
        }
        if (isState(State.GAME)) {
            updateGame();
        }
        if (isState(State.FINISH)) {
            updateFinish();
        }
        if (isState(State.RESTARTING)) {
            if (!inReset) {
                inReset = true;
                reset(true);
                plugin.getGm().removeGameUpdating(id);
            }
        }
    }

    public void updateGame() {
        Utils.updateSB(this);
        GameEvent e = getNowEvent();
        if (e == null) return;
        e.update();
        int s = e.getTime();
        if (s == 0) {
            event++;
            if (plugin.getCm().isChestHolograms()) {
                teams.values().forEach(t -> t.getChest().getInvs().values().stream().filter(UltraGameChest::isSpawned).forEach(UltraGameChest::hide));
                center.getInvs().values().stream().filter(UltraGameChest::isSpawned).forEach(UltraGameChest::hide);
            }
            e.start(this);
            plugin.getVc().getReflection().sendTitle(plugin.getLang().get("titles." + e.getName() + ".title"), plugin.getLang().get("titles." + e.getName() + ".subtitle"), 0, 30, 0, getPlayers());
        } else {
            if (e.getName().equals("refill")) {
                if (plugin.getCm().isChestHolograms()) {
                    String text = plugin.getLang().get("timer").replace("<time>", Utils.convertTime(e.getTime()));
                    teams.values().forEach(t -> t.getChest().getInvs().values().stream().filter(UltraGameChest::isSpawned).forEach(c -> c.updateTimer(text)));
                    center.getInvs().values().stream().filter(UltraGameChest::isSpawned).forEach(c -> c.updateTimer(text));
                }
            }
        }
    }

    public void cancel() {
        this.starting = plugin.getCm().getStarting();
        this.pregame = plugin.getCm().getPregame();
        setState(State.WAITING);
        sendGameMessage(plugin.getLang().get("messages.cancelStart"));
        sendGameTitle(plugin.getLang().get("titles.cancel.title"), plugin.getLang().get("titles.cancel.subtitle"), 0, 40, 0);
        sendGameSound(CustomSound.CANCELSTART);
        plugin.getGm().updateGame(plugin.getCm().getGameServer(), name, color, state.name(), gameType, players.size(), max);
        plugin.getBm().sendUpdateGame(this);
    }

    public void checkWin() {
        if (!isState(State.GAME)) return;
        if (getTeamAlive() <= 1) {
            setState(State.FINISH);
        }
    }

    public int getMin() {
        return min;
    }

    public void addWinDance(WinDance wd) {
        winDances.add(wd);
    }

    public void addWinEffects(WinEffect wd) {
        winEffects.add(wd);
    }

    public void addKillEffects(KillEffect wd) {
        killEffects.add(wd);
    }

    public void sendGameMessage(String msg) {
        for (Player p : cached) {
            p.sendMessage(msg);
        }
    }

    public void sendGameTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        plugin.getVc().getReflection().sendTitle(title, subtitle, fadeIn, stay, fadeOut, cached);
    }

    public void sendGameSound(CustomSound sound) {
        for (Player p : cached) {
            sound.reproduce(p);
        }
    }

    public void addPlayerTeam(Player p, Team team) {
        team.addMember(p);
        playerTeam.put(p.getUniqueId(), team.getId());
    }

    public GameEvent getNowEvent() {
        if (events.isEmpty() || event >= events.size()) {
            return null;
        }
        return events.get(event);
    }

    public void joinRandomTeam(Player p) {
        for (Team team : teams.values()) {
            if (team.getTeamSize() < teamSize) {
                addPlayerTeam(p, team);
                break;
            }
        }
    }

    public void removePlayerAllTeam(Player p) {
        if (!playerTeam.containsKey(p.getUniqueId())) {
            return;
        }
        int id = playerTeam.get(p.getUniqueId());
        removePlayerTeam(p, teams.get(id));
        playerTeam.remove(p.getUniqueId());
    }

    public GamePlayer getGamePlayerByName(String name) {
        for (GamePlayer gp : gamePlayer.values()) {
            if (gp.getP().getName().equals(name)) {
                return gp;
            }
        }
        return null;
    }

    public GamePlayer getGamePlayerByKills(int kills, List<GamePlayer> except) {
        for (GamePlayer gp : gamePlayer.values()) {
            if (gp.getKills() == kills && !except.contains(gp)) {
                return gp;
            }
        }
        return null;
    }

    public int getPregame() {
        return pregame;
    }

    public int getStarting() {
        return starting;
    }

    public int getMax() {
        return max;
    }

    public int getId() {
        return id;
    }

    public int getTeamSize() {
        return teamSize;
    }

    public boolean isState(State state) {
        return this.state.equals(state);
    }

    public Location getLobby() {
        return lobby;
    }

    public Location getSpectator() {
        return spectator;
    }

    public String getName() {
        return name;
    }

    public Team getLastTeam() {
        for (Team team : teams.values()) {
            if (team.getTeamSize() > 0) {
                return team;
            }
        }
        return null;
    }

    public int getTeamAlive() {
        int c = 0;
        for (Team team : teams.values()) {
            if (team.getTeamSize() > 0) {
                c++;
            }
        }
        return c;
    }

    public Team getTeamPlayer(Player p) {
        if (!playerTeam.containsKey(p.getUniqueId())) {
            return null;
        }
        return teams.get(playerTeam.get(p.getUniqueId()));
    }

    public GameChest getCenter() {
        return center;
    }

    public Collection<Player> getPlayers() {
        return players;
    }

    public Collection<Player> getSpectators() {
        return spectators;
    }

    public Map<Integer, Team> getTeams() {
        return teams;
    }

    public Collection<Player> getCached() {
        return cached;
    }

    public Collection<Player> getNoDamaged() {
        return noDamaged;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
        plugin.getGm().updateGame(plugin.getCm().getGameServer(), name, color, state.name(), gameType, players.size(), max);
        plugin.getBm().sendUpdateGame(this);
        plugin.getGem().updateInventories(gameType, "none");
    }

    public String getChestType() {
        return chestType;
    }

    public void setChestType(String chestType) {
        this.chestType = chestType;
    }

    public FinalType getFinalType() {
        return finalType;
    }

    public void setFinalType(FinalType finalType) {
        this.finalType = finalType;
    }

    public HealthType getHealthType() {
        return healthType;
    }

    public void setHealthType(HealthType healthType) {
        this.healthType = healthType;
    }

    public ProjectileType getProjectileType() {
        return projectileType;
    }

    public void setProjectileType(ProjectileType projectileType) {
        this.projectileType = projectileType;
    }

    public TimeType getTimeType() {
        return timeType;
    }

    public void setTimeType(TimeType timeType) {
        this.timeType = timeType;
    }

    public String getGameType() {
        return gameType;
    }

    public Map<UUID, GamePlayer> getGamePlayer() {
        return gamePlayer;
    }

    public Vote getVote() {
        return vote;
    }

    public ArrayList<GameEvent> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<GameEvent> events) {
        this.events = events;
    }

    public boolean isVotes() {
        return votes;
    }

    public String getColor() {
        if (color.equals("none")) {
            return "";
        }
        return color;
    }

    public int getBorderX() {
        return borderX;
    }

    public int getBorderZ() {
        return borderZ;
    }

    public void updateSpectatorOptions(Player p) {
        SWPlayer sw = plugin.getDb().getSWPlayer(p);
        if (sw == null) return;
        p.setAllowFlight(true);
        p.setFlying(true);
        p.setFlySpeed(sw.getSpeed());
        if (sw.isNightVision()) {
            p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 999999, 999999));
        } else {
            p.removePotionEffect(PotionEffectType.NIGHT_VISION);
        }
        if (sw.isSpectatorsView()) {
            for (Player s : spectators) {
                p.showPlayer(s);
            }
        } else {
            for (Player s : spectators) {
                p.hidePlayer(s);
            }
        }
    }

    public void updateVisible() {
        for (Player to : players) {
            for (Player from : players) {
                to.showPlayer(from);
                from.showPlayer(to);
            }
        }
    }

    public void updateFinish() {
        if (!inFinish) {
            USWGameFinishEvent event = new USWGameFinishEvent(this, cached, teams.values(), vote);
            Bukkit.getServer().getPluginManager().callEvent(event);
            inFinish = true;
            int al = getTeamAlive();
            for (GameEvent e : events) {
                e.stop(this);
            }
            if (al <= 1) {
                Team team = getLastTeam();
                if (team != null) {
                    GameWin win = new GameWin(this);
                    win.setTeamWin(team);
                    USWGameWinEvent wie = new USWGameWinEvent(this, team);
                    Bukkit.getServer().getPluginManager().callEvent(wie);
                    List<String> top = win.getTop();
                    for (Player p : team.getMembers()) {
                        SWPlayer sp = plugin.getDb().getSWPlayer(p);
                        if (sp == null) continue;
                        sp.addStat(StatType.WINS, gameType, 1);
                        GamePlayer gp = gamePlayer.get(p.getUniqueId());
                        if (gp != null){
                            gp.addCoins(plugin.getCm().getCoinsWin());
                            gp.addXP(plugin.getCm().getXpWin());
                            gp.addSouls(plugin.getCm().getSoulsWin());
                        }
                        plugin.getCos().executeWinDance(this, p, sp.getWinDance());
                        plugin.getCos().executeWinEffect(this, p, sp.getWinEffect());
                        if (plugin.getCm().isWCMDEnabled()) {
                            plugin.getCm().getWinCommands().forEach(c -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), c.replaceAll("<player>", p.getName())));
                        }
                    }
                    plugin.getVc().getReflection().sendTitle(plugin.getLang().get("titles.win.title"), plugin.getLang().get("titles.win." + gameType.toLowerCase()), 0, 40, 0, team.getMembers());
                    for (String s : plugin.getLang().get("messages.win").split("\\n")) {
                        getCached().forEach(p -> p.sendMessage(CenterMessage.getCenteredMessage(s.replaceAll("<winner>", win.getWinner()).replaceAll("<top1>", top.get(0).split(":")[0]).replaceAll("<number1>", top.get(0).split(":")[1]).replaceAll("<top2>", top.get(1).split(":")[0]).replaceAll("<number2>", top.get(1).split(":")[1]).replaceAll("<top3>", top.get(2).split(":")[0]).replaceAll("<number3>", top.get(2).split(":")[1]))));
                    }
                }
            }
            for (Player on : cached) {
                GamePlayer pt = gamePlayer.get(on.getUniqueId());
                Utils.sendRewards(plugin, on, pt);
            }
            new BukkitRunnable() {
                @Override
                public void run() {
                    winEffects.forEach(WinEffect::stop);
                    winDances.forEach(WinDance::stop);
                    killEffects.forEach(KillEffect::stop);
                    Collection<Player> c = new ArrayList<>(Bukkit.getOnlinePlayers());
                    c.forEach(p -> {
                        if (plugin.getCm().isOnFinish()) {
                            boolean added = plugin.getGm().addRandomGame(p, getGameType());
                            if (!added) {
                                plugin.sendToServer(p, plugin.getCm().getLobbyServer());
                            }
                        } else {
                            plugin.getGm().removePlayerAllGame(p);
                        }
                    });
                    setState(State.RESTARTING);
                }
            }.runTaskLater(plugin, 20L * plugin.getCm().getWinTime());
        }
    }

    public long getStarted() {
        return started;
    }

    public int getBorderStart() {
        return borderStart;
    }

    public int getBorderEnd() {
        return borderEnd;
    }

    public abstract void updateBar();

    public abstract void updateStarting();

    public abstract void updatePreGame();

    public abstract void resetTime();

    public abstract void addPlayer(Player p);

    public abstract void removePlayer(Player p);

    public abstract void removePlayerTeam(Player p, Team team);

}