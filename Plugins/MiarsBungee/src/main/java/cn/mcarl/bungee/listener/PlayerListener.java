package cn.mcarl.bungee.listener;

import cn.mcarl.bungee.MiarsBungee;
import cc.carm.lib.easyplugin.utils.ColorParser;
import cn.mcarl.bungee.util.bungee.ServerHelper;
import net.md_5.bungee.api.AbstractReconnectHandler;
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
    var player = ev.getPlayer();
    var kickedFrom = originOfKick(player).getName();
    var kickTo = ServerHelper.getLobbyServer();

    if (kickedFrom != null && kickedFrom.equals(kickTo)) {
      return;
    }

    ev.setCancelled(true);
    ev.setCancelServer(plugin.getProxy().getServers().get(kickTo));

    player.sendMessage(ColorParser.parse("&7因为您所在服务器重启维护，已经将您移动至大厅。"));
  }

  private ServerInfo originOfKick(ProxiedPlayer player) {
    if (player.getServer() != null) {
      return player.getServer().getInfo();
    } else if (plugin.getProxy().getReconnectHandler() != null) {
      return plugin.getProxy().getReconnectHandler().getServer(player);
    } else {
      final var pendingConnection = player.getPendingConnection();
      final var forcedHost = AbstractReconnectHandler.getForcedHost(pendingConnection);

      if (forcedHost != null) {
        return forcedHost;
      } else {
        final var defaultServer = pendingConnection.getListener().getServerPriority().get(0);
        return plugin.getProxy().getServerInfo(defaultServer);
      }
    }
  }
}
