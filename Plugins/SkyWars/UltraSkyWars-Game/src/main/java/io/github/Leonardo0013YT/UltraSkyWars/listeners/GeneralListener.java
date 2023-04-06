package io.github.Leonardo0013YT.UltraSkyWars.listeners;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class GeneralListener implements Listener {

    private UltraSkyWars plugin;

    public GeneralListener(UltraSkyWars plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        if (plugin.getCm().isBungeeModeEnabled() && plugin.getCm().isBungeeModeGame()) {
            e.setQuitMessage(null);
        }
        plugin.getLvl().remove(p);
        plugin.getGm().removePlayerAllGame(p);
        plugin.getDb().savePlayer(p);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onKick(PlayerKickEvent e) {
        Player p = e.getPlayer();
        plugin.getSb().remove(p);
        plugin.getLvl().remove(p);
        plugin.getGm().removePlayerAllGame(p);
        plugin.getDb().savePlayer(p);
    }

}