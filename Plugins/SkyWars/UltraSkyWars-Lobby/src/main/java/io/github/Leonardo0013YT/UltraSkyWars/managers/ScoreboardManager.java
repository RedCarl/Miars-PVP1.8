package io.github.Leonardo0013YT.UltraSkyWars.managers;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.scoreboard.Netherboard;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ScoreboardManager {

    UltraSkyWars plugin;
    private Netherboard board = Netherboard.instance();

    public ScoreboardManager(UltraSkyWars plugin) {
        this.plugin = plugin;
    }

    public void update(Player p) {
        if (p == null || !p.isOnline()) return;
        if (plugin.getCm().isDisableAllScoreboards()) return;
        if (!plugin.getGm().isPlayerInGame(p) && plugin.getCm().isLobbyScoreboard()) {
            if (!board.hasBoard(p)) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    board.createBoard(p, plugin.getLang().get(p, "scoreboards.main-title"));
                    board.getBoard(p).setName(plugin.getLang().get(p, "scoreboards.main-title"));
                    board.getBoard(p).setAll(plugin.getLang().get(p, "scoreboards.main").split("\\n"));
                });
            } else {
                board.getBoard(p).setName(plugin.getLang().get(p, "scoreboards.main-title"));
                board.getBoard(p).setAll(plugin.getLang().get(p, "scoreboards.main").split("\\n"));
            }
        }
    }

    public void remove(Player p) {
        if (board.hasBoard(p)) {
            board.removeBoard(p);
        }
    }

}