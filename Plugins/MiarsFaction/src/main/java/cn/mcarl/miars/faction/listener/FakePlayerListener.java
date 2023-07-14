package cn.mcarl.miars.faction.listener;

import cn.mcarl.miars.faction.conf.PluginConfig;
import cn.mcarl.miars.faction.manager.FakePlayerManager;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class FakePlayerListener implements Listener {

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {
        Entity entity = e.getEntity();
        World world = entity.getWorld();

        if (world.getName().equals(PluginConfig.PROTECTED_REGION.WORLD_NAME+"_spawn")) {
            return;
        }

        FakePlayerManager.getInstance().killFakePlayer(entity);
    }

    @EventHandler
    public void PlayerJoinEvent(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        FakePlayerManager.getInstance().getFakePlayer(player);
    }

    @EventHandler
    public void PlayerQuitEvent(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        FakePlayerManager.getInstance().setFakePlayer(player);
    }

    @EventHandler
    public void PlayerKickEvent(PlayerKickEvent e) {
        Player player = e.getPlayer();
        FakePlayerManager.getInstance().setFakePlayer(player);
    }

}
