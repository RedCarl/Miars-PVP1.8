package io.github.Leonardo0013YT.UltraSkyWars.listeners;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.superclass.Game;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class MotdListener implements Listener {

    private UltraSkyWars plugin;

    public MotdListener(UltraSkyWars plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMotd(ServerListPingEvent e) {
        Game g = plugin.getGm().getBungee();
        if (g == null) return;
        e.setMotd(plugin.getLang().get("motds." + g.getState().name()).replace("<max>", String.valueOf(g.getMax())).replace("<players>", String.valueOf(g.getPlayers().size())).replace("<map>", g.getName()));
    }

}