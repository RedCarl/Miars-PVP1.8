package io.github.Leonardo0013YT.UltraSkyWars.modules.pwt.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class PWTListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        for (Player on : Bukkit.getOnlinePlayers()) {
            if (p.getWorld().getName().equals(on.getWorld().getName())) continue;
            on.hidePlayer(p);
            p.hidePlayer(on);
        }
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent e) {
        Location from = e.getFrom();
        Location to = e.getTo();
        if (to == null || from == null || to.getWorld() == null || from.getWorld() == null) return;
        if (to.getWorld().getName().equals(from.getWorld().getName())) {
            return;
        }
        Player p = e.getPlayer();
        World wTo = to.getWorld();
        World wFrom = from.getWorld();
        wFrom.getPlayers().forEach(on -> on.hidePlayer(p));
        wFrom.getPlayers().forEach(p::hidePlayer);
        wTo.getPlayers().forEach(pl -> pl.showPlayer(p));
        wTo.getPlayers().forEach(p::showPlayer);
    }

}