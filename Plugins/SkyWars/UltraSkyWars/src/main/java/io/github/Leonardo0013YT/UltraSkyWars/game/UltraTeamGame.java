package io.github.Leonardo0013YT.UltraSkyWars.game;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.api.events.USWGameStartEvent;
import io.github.Leonardo0013YT.UltraSkyWars.api.events.glass.USWGlassCreateEvent;
import io.github.Leonardo0013YT.UltraSkyWars.api.events.glass.USWGlassDeleteEvent;
import io.github.Leonardo0013YT.UltraSkyWars.calls.CallBackAPI;
import io.github.Leonardo0013YT.UltraSkyWars.cosmetics.Balloon;
import io.github.Leonardo0013YT.UltraSkyWars.cosmetics.Glass;
import io.github.Leonardo0013YT.UltraSkyWars.cosmetics.kits.Kit;
import io.github.Leonardo0013YT.UltraSkyWars.data.SWPlayer;
import io.github.Leonardo0013YT.UltraSkyWars.enums.CustomSound;
import io.github.Leonardo0013YT.UltraSkyWars.enums.StatType;
import io.github.Leonardo0013YT.UltraSkyWars.enums.State;
import io.github.Leonardo0013YT.UltraSkyWars.managers.GameManager;
import io.github.Leonardo0013YT.UltraSkyWars.superclass.Game;
import io.github.Leonardo0013YT.UltraSkyWars.team.Team;
import io.github.Leonardo0013YT.UltraSkyWars.utils.CenterMessage;
import io.github.Leonardo0013YT.UltraSkyWars.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class UltraTeamGame extends Game {

    public UltraTeamGame(UltraSkyWars plugin, GameManager gm, String path, String name, int id, CallBackAPI<String> correctly) {
        super(gm, path, name, id, correctly);
        this.gameType = plugin.getArenas().getOrDefault(path + ".gameType", "TEAM").toUpperCase();
        this.pregame = plugin.getCm().getPregame();
    }

    @Override
    public void resetTime() {
        UltraSkyWars plugin = UltraSkyWars.get();
        this.pregame = plugin.getCm().getPregame();
    }

    public void addPlayer(Player p) {
        UltraSkyWars plugin = UltraSkyWars.get();
        p.setGameMode(GameMode.ADVENTURE);
        cached.add(p);
        players.add(p);
        if (players.size() >= max && starting > 10) {
            starting = 10;
            sendGameMessage(plugin.getLang().get("messages.gameFull"));
        }
        vote.addVotePlayer(p);
        gamePlayer.put(p.getUniqueId(), new GamePlayer(p, this, false));
        gamePlayerNames.put(p.getName(), p.getUniqueId());
        Utils.setCleanPlayer(p);
        sendGameMessage(plugin.getLang().get(p, "messages.join").replace("<suffix>", plugin.getAdm().getPlayerSuffix(p)).replace("<prefix>", plugin.getAdm().getPlayerPrefix(p)).replaceAll("<player>", p.getName()).replaceAll("<players>", String.valueOf(players.size())).replaceAll("<max>", String.valueOf(getMax())));
        p.teleport(lobby);
        plugin.getGm().updateGame(name, color, state.name(), gameType, players.size(), max);
        plugin.getGem().updateInventories(gameType, "none");
        sendGameSound(CustomSound.JOIN_PLAYER);
        checkStart();
        Utils.updateSB(this, true);
        Utils.setCleanPlayer(p);
    }

    public void updateBar() {
        UltraSkyWars plugin = UltraSkyWars.get();
        for (Player p : players) {
            SWPlayer sw = plugin.getDb().getSWPlayer(p);
            if (sw == null) {
                continue;
            }
            if (sw.getTeamKit() != 999999) {
                Kit k = plugin.getKm().getKits().get(sw.getTeamKit());
                if (k != null) {
                    plugin.getVc().getReflection().sendActionBar(plugin.getLang().get(p, "action.selected").replaceAll("<kit>", k.getName()), p);
                }
            } else {
                plugin.getVc().getReflection().sendActionBar(plugin.getLang().get(p, "action.noSelected"), p);
            }
        }
    }

    public void updateStarting() {
        Utils.updateSB(this, false);
        UltraSkyWars plugin = UltraSkyWars.get();
        if (starting == 30 || starting == 15 || starting == 10 || (starting <= 5 && starting > 0)) {
            sendGameTitle(plugin.getLang().get("titles.starting.title").replaceAll("<time>", String.valueOf(starting)), plugin.getLang().get("titles.starting.subtitle").replaceAll("<time>", String.valueOf(starting)), 0, 40, 0);
            sendGameMessage(plugin.getLang().get("messages.starting").replaceAll("<time>", String.valueOf(starting)).replaceAll("<s>", (starting > 1) ? "s" : ""));
            sendGameSound(CustomSound.STARTING);
        }
        if (starting == 29 || starting == 14 || starting == 9 || starting == 0) {
            sendGameTitle("", "", 0, 1, 0);
        }
        if (starting == 2) {
            for (Player on : cached) {
                on.closeInventory();
                on.getInventory().remove(plugin.getIm().getTeam());
            }
        }
        if (starting == 1) {
            for (Player on : cached) {
                if (getTeamPlayer(on) == null) {
                    joinRandomTeam(on);
                }
            }
            for (Team team : teams.values()) {
                int amount = 0;
                for (Player p : team.getMembers()) {
                    Location sp = team.getSpawn().clone().add(0, amount * 10, 0);
                    SWPlayer sw = plugin.getDb().getSWPlayer(p);
                    Glass g = plugin.getCos().getGlass(sw.getGlass());
                    if (g != null) {
                        if (plugin.getCm().isTeamOneCage()) {
                            if (!team.isCreated()) {
                                USWGlassCreateEvent e = new USWGlassCreateEvent(p, g, team.getSpawn(), true);
                                Bukkit.getPluginManager().callEvent(e);
                                if (!e.isCancelled()) {
                                    g.createCage(team.getSpawn(), true, (b) -> {
                                    });
                                    team.setCreated(true);
                                }
                            }
                        } else {
                            USWGlassCreateEvent e = new USWGlassCreateEvent(p, g, team.getSpawn(), false);
                            Bukkit.getPluginManager().callEvent(e);
                            if (!e.isCancelled()) {
                                g.createCage(sp, false, (b) -> {
                                });
                                team.setCenter(p, sp);
                            }
                        }
                    }
                    Balloon b = plugin.getCos().getBalloon(sw.getBalloon());
                    if (b != null) {
                        if (!balloons.containsKey(team.getId())) {
                            balloons.put(team.getId(), b.spawn(p, team.getBalloon(), team.getFence()));
                        }
                    }
                    amount++;
                }
            }
        }
        if (starting == 0) {
            updateVisible();
            for (Team team : teams.values()) {
                for (Player t : team.getMembers()) {
                    if (plugin.getCm().isTeamOneCage()) {
                        t.teleport(team.getSpawn().clone().add(0, 0.5, 0));
                    } else {
                        if (team.getCenter(t) != null) {
                            t.teleport(team.getCenter(t).clone().add(0, 0.5, 0));
                        }
                    }
                }
                team.execute();
            }
            setState(State.PREGAME);
        }
        starting--;
    }

    public void updatePreGame() {
        Utils.updateSB(this, false);
        UltraSkyWars plugin = UltraSkyWars.get();
        if (pregame == 30 || pregame == 15 || pregame == 10 || (pregame <= 5 && pregame > 0)) {
            sendGameTitle(plugin.getLang().get("titles.pregame.title").replaceAll("<time>", String.valueOf(pregame)), plugin.getLang().get("titles.pregame.subtitle").replaceAll("<time>", String.valueOf(pregame)), 0, 40, 0);
            sendGameMessage(plugin.getLang().get("messages.pregame").replaceAll("<time>", String.valueOf(pregame)).replaceAll("<s>", (pregame > 1) ? "s" : ""));
            sendGameSound(CustomSound.PREGAME);
        }
        if (pregame == 29 || pregame == 14 || pregame == 9 || pregame == 0) {
            sendGameTitle("", "", 0, 1, 0);
        }
        if (pregame == 5) {
            plugin.getWc().clearLobby(lobby);
        }
        if (pregame == 0) {
            for (Team team : teams.values()) {
                for (Player p : team.getMembers()) {
                    p.setGameMode(GameMode.SURVIVAL);
                    noDamaged.add(p);
                    p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 1000));
                    p.getInventory().clear();
                    p.getInventory().setArmorContents(null);
                    addEnderPearl(p);
                    SWPlayer sw = plugin.getDb().getSWPlayer(p);
                    if (sw != null) {
                        sw.addStat(StatType.PLAYED, gameType, 1);
                        Glass g = plugin.getCos().getGlass(sw.getGlass());
                        if (g != null) {
                            if (plugin.getCm().isTeamOneCage()) {
                                Bukkit.getPluginManager().callEvent(new USWGlassDeleteEvent(p, g, team.getSpawn(), true));
                                g.deleteCage(team.getSpawn(), true, (b) -> {
                                });
                            } else {
                                Bukkit.getPluginManager().callEvent(new USWGlassDeleteEvent(p, g, team.getSpawn(), false));
                                g.deleteCage(team.getCenter(p), false, (b) -> {
                                });
                            }
                        }
                        GamePlayer gop = gamePlayer.get(p.getUniqueId());
                        if (gop != null && !gop.hasChallenge("NOOB")) {
                            if (sw.getTeamKit() != 999999) {
                                Kit k = plugin.getKm().getKits().get(sw.getTeamKit());
                                if (k != null) {
                                    if (k.getModes().contains(gameType) && k.getLevels().get(sw.getTeamKitLevel()) != null) {
                                        k.getLevels().get(sw.getTeamKitLevel()).giveKitLevel(p);
                                    }
                                }
                            }
                        }
                    }
                    p.closeInventory();
                }
            }
            for (String s : plugin.getLang().get("messages.start").split("\\n")) {
                getCached().forEach(p -> p.sendMessage(CenterMessage.getCenteredMessage(s)));
            }
            started = System.currentTimeMillis();
            vote.executeVotes();
            USWGameStartEvent e = new USWGameStartEvent(this, cached, teams.values(), vote);
            Bukkit.getServer().getPluginManager().callEvent(e);
            setState(State.GAME);
            checkWin();
        }
        pregame--;
    }

    public void removePlayerTeam(Player p, Team team) {
        UltraSkyWars plugin = UltraSkyWars.get();
        if (balloons.containsKey(team.getId())) {
            int b = balloons.get(team.getId());
            Balloon bl = plugin.getCos().getBalloon(b);
            bl.remove(p);
            balloons.remove(team.getId());
        }
        team.removeMember(p);
        SWPlayer sw = plugin.getDb().getSWPlayer(p);
        if (sw == null) return;
        if (isState(State.WAITING) || isState(State.STARTING)) {
            Glass g = plugin.getCos().getGlass(sw.getGlass());
            if (plugin.getCm().isTeamOneCage()) {
                if (g != null) {
                    g.deleteCage(team.getSpawn(), true, (b) -> {
                    });
                }
            } else {
                if (g != null) {
                    g.deleteCage(team.getCenter(p), false, (b) -> {
                    });
                }
            }
        }
    }

    public Team getTeamByID(int id) {
        return teams.get(id);
    }

}