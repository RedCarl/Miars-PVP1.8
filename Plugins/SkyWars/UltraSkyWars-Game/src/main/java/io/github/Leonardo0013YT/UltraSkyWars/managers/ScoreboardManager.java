package io.github.Leonardo0013YT.UltraSkyWars.managers;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.enums.State;
import io.github.Leonardo0013YT.UltraSkyWars.game.GamePlayer;
import io.github.Leonardo0013YT.UltraSkyWars.scoreboard.Netherboard;
import io.github.Leonardo0013YT.UltraSkyWars.superclass.Game;
import io.github.Leonardo0013YT.UltraSkyWars.superclass.GameEvent;
import io.github.Leonardo0013YT.UltraSkyWars.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ScoreboardManager {

    UltraSkyWars plugin;
    private Netherboard board = Netherboard.instance();

    public ScoreboardManager(UltraSkyWars plugin) {
        this.plugin = plugin;
    }

    public void update(Player p, GameEvent ge) {
        if (p == null || !p.isOnline()) return;
        if (plugin.getCm().isDisableAllScoreboards()) return;
        if (!plugin.getGm().isPlayerInGame(p)) return;
        Game game = plugin.getGm().getGameByPlayer(p);
        if (!board.hasBoard(p)) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                board.createBoard(p, replaceTitle(plugin.getLang().get(p, "scoreboards.lobby-title"), game, plugin));
                board.getBoard(p).setName(replaceTitle(plugin.getLang().get(p, "scoreboards.lobby-title"), game, plugin));
                board.getBoard(p).setAll(lobby(plugin.getLang().get(p, "scoreboards.lobby"), game, plugin).split("\\n"));
            });
            return;
        }
        if (game.isState(State.WAITING)) {
            board.getBoard(p).setName(replaceTitle(plugin.getLang().get(p, "scoreboards.lobby-title"), game, plugin));
            board.getBoard(p).setAll(lobby(plugin.getLang().get(p, "scoreboards.lobby"), game, plugin).split("\\n"));
        } else if (game.isState(State.STARTING)) {
            board.getBoard(p).setName(replaceTitle(plugin.getLang().get(p, "scoreboards.starting-title"), game, plugin));
            board.getBoard(p).setAll(starting(plugin.getLang().get(p, "scoreboards.starting"), game, plugin).split("\\n"));
        } else if (game.isState(State.PREGAME)) {
            board.getBoard(p).setName(replaceTitle(plugin.getLang().get(p, "scoreboards.pregame-title"), game, plugin));
            board.getBoard(p).setAll(pregame(plugin.getLang().get(p, "scoreboards.pregame"), game, plugin).split("\\n"));
        } else if (game.isState(State.GAME) || game.isState(State.FINISH)) {
            if (game.getGameType().equals("SOLO")) {
                board.getBoard(p).setName(replaceTitle(plugin.getLang().get(p, "scoreboards.game-title"), game, plugin));
                board.getBoard(p).setAll(game(p, plugin.getLang().get(p, "scoreboards.game"), game, ge, plugin).split("\\n"));
            } else if (game.getGameType().equals("TEAM")) {
                board.getBoard(p).setName(replaceTitle(plugin.getLang().get(p, "scoreboards.team-title"), game, plugin));
                board.getBoard(p).setAll(game(p, plugin.getLang().get(p, "scoreboards.team"), game, ge, plugin).split("\\n"));
            } else {
                board.getBoard(p).setName(replaceTitle(plugin.getLang().get(p, "scoreboards.ranked-title"), game, plugin));
                board.getBoard(p).setAll(game(p, plugin.getLang().get(p, "scoreboards.ranked"), game, ge, plugin).split("\\n"));
            }
        }
    }

    private String replaceTitle(String c, Game game, UltraSkyWars plugin) {
        return c.replaceAll("<mode>", plugin.getLang().get("modes." + game.getGameType().toLowerCase()));
    }

    private String lobby(String c, Game game, UltraSkyWars plugin) {
        return c.replaceAll("<mode>", plugin.getLang().get("modes." + game.getGameType().toLowerCase()))
                .replaceAll("<date>", Utils.getDate())
                .replaceAll("<players>", String.valueOf(game.getPlayers().size()))
                .replaceAll("<max>", String.valueOf(game.getMax()))
                .replaceAll("<map>", game.getName());
    }

    private String starting(String c, Game game, UltraSkyWars plugin) {
        return c.replaceAll("<mode>", plugin.getLang().get("modes." + game.getGameType().toLowerCase()))
                .replaceAll("<date>", Utils.getDate())
                .replaceAll("<players>", String.valueOf(game.getPlayers().size()))
                .replaceAll("<max>", String.valueOf(game.getMax()))
                .replaceAll("<map>", game.getName())
                .replaceAll("<time>", String.valueOf(game.getStarting()))
                .replaceAll("<s>", (game.getStarting() > 1) ? "s" : "");
    }

    private String pregame(String c, Game game, UltraSkyWars plugin) {
        return c.replaceAll("<mode>", plugin.getLang().get("modes." + game.getGameType().toLowerCase()))
                .replaceAll("<date>", Utils.getDate())
                .replaceAll("<players>", String.valueOf(game.getPlayers().size()))
                .replaceAll("<max>", String.valueOf(game.getMax()))
                .replaceAll("<map>", game.getName())
                .replaceAll("<time>", String.valueOf(game.getPregame()))
                .replaceAll("<s>", (game.getPregame() > 1) ? "s" : "");
    }

    private String game(Player p, String c, Game game, GameEvent ge, UltraSkyWars plugin) {
        GamePlayer gp = game.getGamePlayer().get(p.getUniqueId());
        String name;
        int time;
        if (ge == null) {
            name = plugin.getLang().get(p, "events.noMore");
            time = 0;
        } else {
            name = plugin.getLang().get(p, "events." + ge.getName());
            time = ge.getTime();
        }
        int elo = (plugin.getDb().getSWPlayer(p) == null) ? 0 : plugin.getDb().getSWPlayer(p).getElo();
        return c.replaceAll("<mode>", plugin.getLang().get(p, "modes." + game.getGameType().toLowerCase()))
                .replaceAll("<eventTime>", Utils.convertTime(time))
                .replaceAll("<kills>", String.valueOf((gp == null) ? 0 : gp.getKills()))
                .replaceAll("<teamkills>", String.valueOf(game.getTeamPlayer(p) == null ? 0 : game.getTeamPlayer(p).getKills()))
                .replaceAll("<event>", name)
                .replaceAll("<elo>", String.valueOf(elo))
                .replaceAll("<date>", Utils.getDate())
                .replaceAll("<players>", String.valueOf(game.getPlayers().size()))
                .replaceAll("<map>", game.getName());
    }

    public void remove(Player p) {
        if (board.hasBoard(p)) {
            board.removeBoard(p);
        }
    }

}