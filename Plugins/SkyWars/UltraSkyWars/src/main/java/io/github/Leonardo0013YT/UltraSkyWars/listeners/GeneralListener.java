package io.github.Leonardo0013YT.UltraSkyWars.listeners;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class GeneralListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onQuit(PlayerQuitEvent e) {
        remove(e.getPlayer());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onKick(PlayerKickEvent e) {
        remove(e.getPlayer());
    }

    private void remove(Player p) {
        UltraSkyWars plugin = UltraSkyWars.get();
        plugin.getCos().remove(p);
        plugin.getLvl().remove(p);
        plugin.getSb().remove(p);
        plugin.getUim().removeInventory(p);
        if (plugin.getIjm().isSoulWellInjection()) {
            plugin.getIjm().getSoulwell().getSwm().removeSession(p);
        }
        plugin.getGm().removePlayerAllGame(p);
        plugin.getDb().savePlayer(p);
    }

}