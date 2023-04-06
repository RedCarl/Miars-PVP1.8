package io.github.Leonardo0013YT.UltraSkyWars.vote;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.enums.FinalType;
import io.github.Leonardo0013YT.UltraSkyWars.enums.HealthType;
import io.github.Leonardo0013YT.UltraSkyWars.enums.ProjectileType;
import io.github.Leonardo0013YT.UltraSkyWars.enums.TimeType;
import io.github.Leonardo0013YT.UltraSkyWars.game.GamePlayer;
import io.github.Leonardo0013YT.UltraSkyWars.superclass.Game;
import io.github.Leonardo0013YT.UltraSkyWars.superclass.GameEvent;
import io.github.Leonardo0013YT.UltraSkyWars.utils.CenterMessage;
import lombok.Getter;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.*;

public class Vote {

    private UltraSkyWars plugin;
    private Game game;
    private HashMap<Player, VotePlayer> votes = new HashMap<>();
    private String chestType = null;
    private FinalType finalType = null;
    private HealthType healthType = null;
    private ProjectileType projectileType = null;
    private TimeType timeType = null;
    private boolean noVoted = true;
    private int chest, finish, health, projectile, time;

    public Vote(UltraSkyWars plugin, Game game) {
        this.plugin = plugin;
        this.game = game;
    }

    public void addVotePlayer(Player p) {
        votes.put(p, new VotePlayer());
    }

    public void removeVotePlayer(Player p) {
        votes.remove(p);
    }

    public VotePlayer getVotePlayer(Player p) {
        return votes.get(p);
    }

    public void executeVotes() {
        selectVotes();
        World w = game.getLobby().getWorld();
        if (projectileType != null) {
            game.setProjectileType(projectileType);
        }
        if (chestType != null) {
            game.setChestType(chestType);
            game.getTeams().values().forEach(t -> t.getChest().fill(game, false));
            game.getCenter().fill(game, false);
        }
        if (finalType != null) {
            game.setFinalType(finalType);
            ArrayList<GameEvent> events = new ArrayList<>(game.getEvents());
            for (int i = 0; i < events.size(); i++) {
                GameEvent e = events.get(i);
                if (e.getType().equals("final")) {
                    GameEvent ev = plugin.getGm().getEvent(finalType.name().toLowerCase()).clone();
                    ev.setTime(e.getTime());
                    events.set(i, ev);
                }
            }
            game.setEvents(events);
        }
        if (healthType != null) {
            game.setHealthType(healthType);
            for (Player p : game.getCached()) {
                GamePlayer gp = game.getGamePlayer().get(p.getUniqueId());
                if (gp != null && gp.hasChallenge("HEARTS")) {
                    p.setMaxHealth(10);
                } else {
                    p.setMaxHealth(healthType.getAmount());
                }
                p.setHealth(p.getMaxHealth());
            }
        } else {
            for (Player p : game.getCached()) {
                GamePlayer gp = game.getGamePlayer().get(p.getUniqueId());
                if (gp != null && gp.hasChallenge("HEARTS")) {
                    p.setMaxHealth(10);
                }
                p.setHealth(p.getMaxHealth());
            }
        }
        if (timeType != null) {
            game.setTimeType(timeType);
            w.setTime(timeType.getTime());
        }
        List<String> msg = new ArrayList<>();
        for (String s : plugin.getLang().get("messages.votes").split("\\n")) {
            if (s.equals("<votes>")) {
                if (noVoted) {
                    msg.add(CenterMessage.getCenteredMessage(plugin.getLang().get("messages.voteType.noType")));
                } else {
                    if (chest > 0) {
                        msg.add(CenterMessage.getCenteredMessage(plugin.getLang().get("messages.voteType.chest").replaceAll("<chest>", plugin.getChestType().get("votes.chest." + chestType.toLowerCase())).replaceAll("<vchest>", String.valueOf(chest))));
                    }
                    if (finish > 0) {
                        msg.add(CenterMessage.getCenteredMessage(plugin.getLang().get("messages.voteType.final").replaceAll("<final>", plugin.getLang().get("votes.final." + finalType.name().toLowerCase())).replaceAll("<vfinal>", String.valueOf(finish))));
                    }
                    if (health > 0) {
                        msg.add(CenterMessage.getCenteredMessage(plugin.getLang().get("messages.voteType.health").replaceAll("<health>", plugin.getLang().get("votes.health." + healthType.name().toLowerCase())).replaceAll("<vhealth>", String.valueOf(health))));
                    }
                    if (projectile > 0) {
                        msg.add(CenterMessage.getCenteredMessage(plugin.getLang().get("messages.voteType.projectile").replaceAll("<proyectile>", plugin.getLang().get("votes.projectile." + projectileType.name().toLowerCase())).replaceAll("<vproyectile>", String.valueOf(projectile))));
                    }
                    if (time > 0) {
                        msg.add(CenterMessage.getCenteredMessage(plugin.getLang().get("messages.voteType.time").replaceAll("<time>", plugin.getLang().get("votes.time." + timeType.name().toLowerCase())).replaceAll("<vtime>", String.valueOf(time))));
                    }
                }
            } else {
                msg.add(CenterMessage.getCenteredMessage(s));
            }
        }
        game.getPlayers().forEach(p -> msg.forEach(p::sendMessage));
    }

    public void selectVotes() {
        Map<String, Integer> ct = new HashMap<>(), ft = new HashMap<>(), ht = new HashMap<>(), pt = new HashMap<>(), tt = new HashMap<>();
        for (VotePlayer vp : votes.values()) {
            if (vp.isNoVoted()) continue;
            if (vp.getFinalType() != null) {
                ft.put(vp.getFinalType().name(), ft.getOrDefault(vp.getFinalType().name(), 0) + 1);
            }
            if (vp.getChestType() != null) {
                ct.put(vp.getChestType(), ct.getOrDefault(vp.getChestType(), 0) + 1);
            }
            if (vp.getProjectileType() != null) {
                pt.put(vp.getProjectileType().name(), pt.getOrDefault(vp.getProjectileType().name(), 0) + 1);
            }
            if (vp.getHealthType() != null) {
                ht.put(vp.getHealthType().name(), ht.getOrDefault(vp.getHealthType().name(), 0) + 1);
            }
            if (vp.getTimeType() != null) {
                tt.put(vp.getTimeType().name(), tt.getOrDefault(vp.getTimeType().name(), 0) + 1);
            }
            if (noVoted) {
                noVoted = false;
            }
        }
        chestType = getHighVotes(ct, plugin.getCtm().getDefaultChest());
        finalType = FinalType.valueOf(getHighVotes(ft, "BORDER"));
        healthType = HealthType.valueOf(getHighVotes(ht, "HEALTH10"));
        projectileType = ProjectileType.valueOf(getHighVotes(pt, "YESPROJ"));
        timeType = TimeType.valueOf(getHighVotes(tt, "DAY"));
        chest = ct.getOrDefault(chestType, 0);
        finish = ft.getOrDefault(finalType.name(), 0);
        health = ht.getOrDefault(healthType.name(), 0);
        projectile = pt.getOrDefault(projectileType.name(), 0);
        time = tt.getOrDefault(timeType.name(), 0);
    }

    public <K, V extends Comparable<V>> K getHighVotes(Map<K, V> map, K def) {
        Optional<Map.Entry<K, V>> maxEntry = map.entrySet().stream().max(Map.Entry.comparingByValue());
        return maxEntry.map(Map.Entry::getKey).orElse(def);
    }

    public void reset() {
        votes.clear();
        chestType = null;
        finalType = null;
        healthType = null;
        projectileType = null;
        timeType = null;
        chest = 0;
        finish = 0;
        health = 0;
        projectile = 0;
        time = 0;
        noVoted = true;
    }

    public int getVotes(String name) {
        int c = 0;
        for (VotePlayer vp : votes.values()) {
            if (vp.isNoVoted()) continue;
            if (vp.getChestType() != null) {
                if (vp.getChestType().equals(name)) {
                    c++;
                }
            }
            if (vp.getFinalType() != null) {
                if (vp.getFinalType().name().equals(name)) {
                    c++;
                }
            }
            if (vp.getHealthType() != null) {
                if (vp.getHealthType().name().equals(name)) {
                    c++;
                }
            }
            if (vp.getProjectileType() != null) {
                if (vp.getProjectileType().name().equals(name)) {
                    c++;
                }
            }
            if (vp.getTimeType() != null) {
                if (vp.getTimeType().name().equals(name)) {
                    c++;
                }
            }
        }
        return c;
    }

    @Getter
    public static class VotePlayer {

        private String chestType = null;
        private FinalType finalType = null;
        private HealthType healthType = null;
        private ProjectileType projectileType = null;
        private TimeType timeType = null;
        private boolean noVoted = true;

        public void setChestType(String chestType) {
            this.chestType = chestType;
            this.noVoted = false;
        }

        public void setFinalType(FinalType finalType) {
            this.finalType = finalType;
            this.noVoted = false;
        }

        public void setHealthType(HealthType healthType) {
            this.healthType = healthType;
            this.noVoted = false;
        }

        public void setProjectileType(ProjectileType projectileType) {
            this.projectileType = projectileType;
            this.noVoted = false;
        }

        public void setTimeType(TimeType timeType) {
            this.timeType = timeType;
            this.noVoted = false;
        }

    }
}