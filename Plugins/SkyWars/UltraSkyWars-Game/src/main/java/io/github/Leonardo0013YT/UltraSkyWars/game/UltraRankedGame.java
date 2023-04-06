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
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class UltraRankedGame extends Game {

    public UltraRankedGame(UltraSkyWars plugin, GameManager gm, String path, String name, int id, CallBackAPI<String> correctly) {
        super(plugin, gm, path, name, id, correctly);
        this.gameType = plugin.getArenas().getOrDefault(path + ".gameType", "RANKED").toUpperCase();
        this.pregame = (plugin.getCm().isPreLobbySystem() ? plugin.getCm().getPregame() : 0);
        if (!plugin.getCm().isPreLobbySystem()) {
            Glass g = plugin.getCos().getGlass(0);
            if (g != null) {
                for (Team team : teams.values()) {
                    g.createCage(team.getSpawn(), false, (b) -> {
                    });
                }
            }
        }
    }

    @Override
    public void resetTime() {
        this.pregame = (plugin.getCm().isPreLobbySystem() ? plugin.getCm().getPregame() : 0);
        if (!plugin.getCm().isPreLobbySystem()) {
            Glass g = plugin.getCos().getGlass(0);
            if (g != null) {
                for (Team team : teams.values()) {
                    g.createCage(team.getSpawn(), false, (b) -> {
                    });
                }
            }
        }
    }

    public void addPlayer(Player p) {
        if (plugin.getCm().isPreLobbySystem()) {
            p.teleport(lobby);
        } else {
            if (getTeamPlayer(p) == null) {
                joinRandomTeam(p);
            }
            Team team = getTeamPlayer(p);
            for (Player on : team.getMembers()) {
                SWPlayer sw = plugin.getDb().getSWPlayer(on);
                if (sw != null) {
                    Glass g2 = plugin.getCos().getGlass(0);
                    if (g2 != null) {
                        g2.deleteCage(team.getSpawn(), false, (b) -> {
                        });
                    }
                    Glass g = plugin.getCos().getGlass(sw.getGlass());
                    if (g != null) {
                        USWGlassCreateEvent e = new USWGlassCreateEvent(p, g, team.getSpawn(), false);
                        Bukkit.getPluginManager().callEvent(e);
                        if (!e.isCancelled()) {
                            g.createCage(team.getSpawn(), false, (b) -> on.teleport(team.getSpawn().clone().add(0, 0.5, 0)));
                            team.setCreated(true);
                        }
                        on.teleport(team.getSpawn().clone().add(0, 0.5, 0));
                    }
                    Balloon b = plugin.getCos().getBalloon(sw.getBalloon());
                    if (b != null) {
                        balloons.put(team.getId(), b.spawn(p, team.getBalloon(), team.getFence()));
                    }
                } else {
                    Glass g = plugin.getCos().getGlass(0);
                    if (g != null) {
                        USWGlassCreateEvent e = new USWGlassCreateEvent(p, g, team.getSpawn(), false);
                        Bukkit.getPluginManager().callEvent(e);
                        if (!e.isCancelled()) {
                            g.createCage(team.getSpawn(), false, (b) -> on.teleport(team.getSpawn().clone().add(0, 0.5, 0)));
                            team.setCreated(true);
                        } else {
                            on.teleport(team.getSpawn().clone().add(0, 0.5, 0));
                        }
                    }
                }
            }
        }
    }

    public void removePlayer(Player p) {
        removePlayerAllTeam(p);
        vote.removeVotePlayer(p);
        if (gamePlayer.containsKey(p.getUniqueId())) {
            GamePlayer gp = gamePlayer.get(p.getUniqueId());
            SWPlayer sw = plugin.getDb().getSWPlayer(p);
            plugin.getAdm().addCoins(p, gp.getCoins());
            sw.addSouls(gp.getSouls());
            sw.addXp(gp.getXP());
            gp.reset();
            gamePlayer.remove(p.getUniqueId());
        }
        cached.remove(p);
        players.remove(p);
        spectators.remove(p);
        noDamaged.remove(p);
        if (isState(State.WAITING) || isState(State.STARTING)) {
            sendGameSound(CustomSound.QUIT_PLAYER);
            sendGameMessage(plugin.getLang().get(p, "messages.quit").replace("<suffix>", plugin.getAdm().getPlayerSuffix(p)).replace("<prefix>", plugin.getAdm().getPlayerPrefix(p)).replaceAll("<player>", p.getName()).replaceAll("<players>", String.valueOf(players.size())).replaceAll("<max>", String.valueOf(getMax())));
        }
        plugin.getGm().updateGame(plugin.getCm().getGameServer(), name, color, state.name(), gameType, players.size(), max);
        plugin.getBm().sendUpdateGame(this);
        plugin.getGem().updateInventories(gameType, "none");
        if (plugin.getMainLobby() != null) {
            p.teleport(plugin.getMainLobby());
        }
        plugin.getTgm().removeTag(p);
        checkCancel();
        checkWin();
        Utils.updateSB(this);
    }

    public void updateBar() {
        for (Player p : players) {
            SWPlayer sw = plugin.getDb().getSWPlayer(p);
            if (sw == null) {
                continue;
            }
            if (sw.getRankedKit() != 999999) {
                Kit k = plugin.getKm().getKits().get(sw.getRankedKit());
                if (k != null) {
                    plugin.getVc().getNMS().sendActionBar(p, plugin.getLang().get(p, "action.selected").replaceAll("<kit>", k.getName()));
                }
            } else {
                plugin.getVc().getNMS().sendActionBar(p, plugin.getLang().get(p, "action.noSelected"));
            }
        }
    }

    public void updateStarting() {
        Utils.updateSB(this);
        if (starting == 30 || starting == 15 || starting == 10 || starting == 5 || starting == 4 || starting == 3 || starting == 2 || starting == 1) {
            sendGameTitle(plugin.getLang().get("titles.starting.title").replaceAll("<time>", String.valueOf(starting)), plugin.getLang().get("titles.starting.subtitle").replaceAll("<time>", String.valueOf(starting)), 0, 40, 0);
            sendGameMessage(plugin.getLang().get("messages.starting").replaceAll("<time>", String.valueOf(starting)).replaceAll("<s>", (starting > 1) ? "s" : ""));
            sendGameSound(CustomSound.STARTING);
        }
        if (starting == 29 || starting == 14 || starting == 9 || starting == 0) {
            sendGameTitle("", "", 0, 1, 0);
        }
        if (starting == 1) {
            if (plugin.getCm().isPreLobbySystem()) {
                for (Player on : cached) {
                    if (getTeamPlayer(on) == null) {
                        joinRandomTeam(on);
                    }
                }
                for (Team team : teams.values()) {
                    for (Player p : team.getMembers()) {
                        SWPlayer sw = plugin.getDb().getSWPlayer(p);
                        Glass g = plugin.getCos().getGlass(sw.getGlass());
                        if (g != null) {
                            USWGlassCreateEvent e = new USWGlassCreateEvent(p, g, team.getSpawn(), false);
                            Bukkit.getPluginManager().callEvent(e);
                            if (!e.isCancelled()) {
                                g.createCage(team.getSpawn(), false, (b) -> {
                                });
                            }
                        }
                        Balloon b = plugin.getCos().getBalloon(sw.getBalloon());
                        if (b != null) {
                            balloons.put(team.getId(), b.spawn(p, team.getBalloon(), team.getFence()));
                        }
                    }
                }
            }
        }
        if (starting == 0) {
            updateVisible();
            for (Team team : teams.values()) {
                if (plugin.getCm().isPreLobbySystem()) {
                    for (Player t : team.getMembers()) {
                        t.teleport(team.getSpawn().clone().add(0, 0.5, 0));
                    }
                }
                team.execute();
            }
            setState(State.PREGAME);
        }
        starting--;
    }

    public void updatePreGame() {
        Utils.updateSB(this);
        if (pregame == 30 || pregame == 15 || pregame == 10 || pregame == 5 || pregame == 4 || pregame == 3 || pregame == 2 || pregame == 1) {
            sendGameTitle(plugin.getLang().get("titles.pregame.title").replaceAll("<time>", String.valueOf(pregame)), plugin.getLang().get("titles.pregame.subtitle").replaceAll("<time>", String.valueOf(pregame)), 0, 40, 0);
            sendGameMessage(plugin.getLang().get("messages.pregame").replaceAll("<time>", String.valueOf(pregame)).replaceAll("<s>", (pregame > 1) ? "s" : ""));
            sendGameSound(CustomSound.PREGAME);
        }
        if (pregame == 29 || pregame == 14 || pregame == 9 || pregame == 0) {
            sendGameTitle("", "", 0, 1, 0);
        }
        if (pregame == 5) {
            if (plugin.getCm().isPreLobbySystem()) {
                plugin.getWc().clearLobby(lobby);
            }
        }
        if (pregame == 0) {
            for (Team team : teams.values()) {
                if (team.getMembers().isEmpty()) {
                    Glass g = plugin.getCos().getGlass(0);
                    if (g != null) {
                        g.deleteCage(team.getSpawn(), false, (b) -> { });
                    }
                    continue;
                }
                for (Player p : team.getMembers()) {
                    p.setGameMode(GameMode.SURVIVAL);
                    noDamaged.add(p);
                    p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 1000));
                    p.getInventory().clear();
                    p.getInventory().setArmorContents(null);
                    SWPlayer sw = plugin.getDb().getSWPlayer(p);
                    if (sw != null) {
                        sw.addStat(StatType.PLAYED, gameType, 1);
                        Glass g = plugin.getCos().getGlass(sw.getGlass());
                        if (g != null) {
                            Bukkit.getPluginManager().callEvent(new USWGlassDeleteEvent(p, g, team.getSpawn(), false));
                            g.deleteCage(team.getSpawn(), false, (b) -> {
                            });
                        }
                        GamePlayer gop = gamePlayer.get(p.getUniqueId());
                        if (gop != null && !gop.hasChallenge("NOOB")) {
                            if (sw.getRankedKit() != 999999) {
                                Kit k = plugin.getKm().getKits().get(sw.getRankedKit());
                                if (k != null) {
                                    if (k.getModes().contains(gameType) && k.getLevels().get(sw.getRankedKitLevel()) != null) {
                                        k.getLevels().get(sw.getRankedKitLevel()).giveKitLevel(p);
                                    }
                                }
                            }
                        }
                        p.closeInventory();
                    }
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
        if (balloons.containsKey(team.getId())) {
            int b = balloons.get(team.getId());
            Balloon bl = plugin.getCos().getBalloon(b);
            bl.remove(p);
            balloons.remove(team.getId());
        }
        team.removeMember(p);
        SWPlayer sw = plugin.getDb().getSWPlayer(p);
        if (sw == null) return;
        Glass g = plugin.getCos().getGlass(sw.getGlass());
        if (g != null) {
            g.deleteCage(team.getSpawn(), false, (b) -> {
            });
        }
        Glass g1 = plugin.getCos().getGlass(0);
        if (g1 != null) {
            g1.createCage(team.getSpawn(), false, (b) -> { });
        }
    }

}