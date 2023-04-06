package io.github.Leonardo0013YT.UltraSkyWars.listeners;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.enums.State;
import io.github.Leonardo0013YT.UltraSkyWars.game.GamePlayer;
import io.github.Leonardo0013YT.UltraSkyWars.superclass.Game;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.ArrayList;
import java.util.List;

public class TeleportFixThree implements Listener {

    private Server server;
    private UltraSkyWars plugin;

    public TeleportFixThree(UltraSkyWars plugin) {
        this.plugin = plugin;
        this.server = plugin.getServer();
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerTeleport(PlayerTeleportEvent e) {
        Player p = e.getPlayer();
        Game game = plugin.getGm().getGameByPlayer(p);
        if (game == null) {
            return;
        }
        if (game.isState(State.PREGAME) || game.isState(State.GAME) || game.isState(State.FINISH) || game.isState(State.RESTARTING))
            return;
        if (game.getSpectators().contains(p)) {
            for (Player on : Bukkit.getOnlinePlayers()) {
                GamePlayer gp = game.getGamePlayer().get(on.getUniqueId());
                if (gp != null) {
                    if (gp.isDead()) {
                        on.hidePlayer(p);
                        p.hidePlayer(on);
                    } else {
                        on.hidePlayer(p);
                        p.showPlayer(on);
                    }
                }
            }
            return;
        }
        int TELEPORT_FIX_DELAY = 30;
        server.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            List<Player> nearby = getPlayersWithin(p, 100);
            updateEntities(p, nearby, false);
            server.getScheduler().scheduleSyncDelayedTask(plugin, () -> updateEntities(p, nearby, true), 1);
        }, TELEPORT_FIX_DELAY);
    }

    private void updateEntities(Player p, List<Player> players, boolean visible) {
        Game game = plugin.getGm().getGameByPlayer(p);
        if (game == null) {
            return;
        }
        for (Player player : players) {
            if (game.getSpectators().contains(player)) {
                player.hidePlayer(p);
                continue;
            }
            if (visible) {
                p.showPlayer(player);
                player.showPlayer(p);
            } else {
                p.hidePlayer(player);
                player.hidePlayer(p);
            }
        }
    }

    private List<Player> getPlayersWithin(Player player, int distance) {
        List<Player> res = new ArrayList<>();
        int d2 = distance * distance;
        for (Player p : server.getOnlinePlayers()) {
            if (p != player && p.getWorld() == player.getWorld() && p.getLocation().distanceSquared(player.getLocation()) <= d2) {
                res.add(p);
            }
        }
        return res;
    }
}