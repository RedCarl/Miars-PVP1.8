package cn.mcarl.bungee.listener;

import cn.mcarl.bungee.MiarsBungee;
import cc.carm.lib.easyplugin.utils.ColorParser;
import cn.mcarl.bungee.util.bungee.ServerHelper;
import net.md_5.bungee.api.AbstractReconnectHandler;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerKickEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PlayerListener implements Listener {
  MiarsBungee plugin;

  public PlayerListener(MiarsBungee plugin) {
    this.plugin = plugin;
  }

  @EventHandler
  public void onServerKickEvent(ServerKickEvent ev) {
    ServerInfo kickedFrom;
    if (ev.getPlayer().getServer() != null) {
      kickedFrom = ev.getPlayer().getServer().getInfo();
    } else if (this.plugin.getProxy().getReconnectHandler() != null) {
      kickedFrom = this.plugin.getProxy().getReconnectHandler().getServer(ev.getPlayer());
    } else {
      kickedFrom = AbstractReconnectHandler.getForcedHost(ev.getPlayer().getPendingConnection());
      if (kickedFrom == null) {
        kickedFrom = ProxyServer.getInstance().getServerInfo(ev.getPlayer().getPendingConnection().getListener().getDefaultServer());
      }
    }
    ServerInfo kickTo = this.plugin.getProxy().getServerInfo("practice-ffa");
    if (kickedFrom != null && kickedFrom.equals(kickTo)) {
      return;
    }
    ev.setCancelled(true);
    ev.setCancelServer(kickTo);
    ev.getPlayer().sendMessage(ColorParser.parse("&cDue to the restart and maintenance of your server, you have been transferred to the lobby."));
  }
}
