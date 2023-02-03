package cn.mcarl.bungee.auth.listener;

import cn.mcarl.bungee.MiarsBungee;
import cn.mcarl.bungee.auth.MiarsAuthBungee;
import cn.mcarl.bungee.auth.utils.GeneralUtil;
import net.md_5.bungee.api.event.*;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import static net.md_5.bungee.event.EventPriority.HIGHEST;
import static net.md_5.bungee.event.EventPriority.LOW;

public class PlayerListener implements Listener {
  MiarsAuthBungee plugin;
  public PlayerListener(MiarsAuthBungee plugin) {
    this.plugin = plugin;
  }

  @EventHandler(priority = HIGHEST)
  public void onPostLogin(PostLoginEvent event) {
  }

  @EventHandler
  public void onDisconnect(PlayerDisconnectEvent event) {
  }

  @EventHandler(priority = HIGHEST)
  public void onPreLogin(PreLoginEvent event) {
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onProfileRequest(LoginEvent event) {

  }

  @EventHandler(priority = HIGHEST)
  public void chooseServer(ServerConnectEvent event) {
  }

  @EventHandler(priority = LOW)
  public void onKick(ServerKickEvent event) {
  }
}
