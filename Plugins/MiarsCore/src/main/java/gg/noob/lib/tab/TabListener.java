package gg.noob.lib.tab;

import gg.noob.lib.tab.client.ClientVersionUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;

public class TabListener implements Listener {

    private final TabHandler handler;

    /**
     * Constructor to make a new {@link TabUpdater}
     *
     * @param handler the handler to register it to
     */
    public TabListener(TabHandler handler) {
        this.handler = handler;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (ClientVersionUtil.getProtocolVersion(player)!=5){
            return;
        }
        this.handler.getAdapter().addFakePlayers(player);
        handler.getUpdater().sendUpdate(player);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (ClientVersionUtil.getProtocolVersion(player)!=5){
            return;
        }
        this.handler.removeAdapter(player);
        this.handler.getUpdater().lastUpdate.remove(player.getUniqueId());
    }

    @EventHandler
    public void onDisable(PluginDisableEvent event) {
        this.handler.getUpdater().cancelExecutors();
    }

}
