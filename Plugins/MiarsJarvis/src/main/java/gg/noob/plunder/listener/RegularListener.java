package gg.noob.plunder.listener;

import gg.noob.lib.tab.client.ClientVersionUtil;
import gg.noob.plunder.Plunder;
import gg.noob.plunder.checks.Check;
import gg.noob.plunder.data.PlayerData;
import gg.noob.plunder.util.ByteBufReader;
import gg.noob.plunder.util.PacketHandler;
import gg.noob.plunder.util.TraceManager;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelPipeline;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.util.Vector;

import java.util.AbstractMap;

public class RegularListener
        implements Listener {
    @EventHandler
    public void onPlayerVelocity(PlayerVelocityEvent e) {
        Plunder.getInstance().getLastVelocity().put(e.getPlayer().getUniqueId(), new AbstractMap.SimpleEntry<Long, Vector>(System.currentTimeMillis(), e.getVelocity()));
    }

    @EventHandler(priority=EventPriority.MONITOR)
    public void onPlayerMove(PlayerMoveEvent e) {
        if (e.getTo().getX() == e.getFrom().getX() && e.getTo().getY() == e.getFrom().getY() && e.getTo().getZ() == e.getFrom().getZ()) {
            return;
        }
        Player player = e.getPlayer();
        PlayerData data = Plunder.getInstance().getDataManager().getPlayerData(player);
        if (data == null) {
            return;
        }
        if (player.isOnGround() && data.isServerGround() && !data.isBacking() && data.getBackTicks() <= 0 && System.currentTimeMillis() - data.getLastBypass() > 500L) {
            Plunder.getInstance().getLastGround().put(player.getUniqueId(), new AbstractMap.SimpleEntry<Long, Location>(System.currentTimeMillis(), player.getLocation().clone()));
            data.setLastGround(player.getLocation().clone());
            data.setLastGroundTime(System.currentTimeMillis());
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        PlayerData data = Plunder.getInstance().getDataManager().getPlayerData(player);
        if (data == null) {
            return;
        }
        boolean cancel = false;
        for (Check check : data.getChecks()) {
            if (!check.handleEvent(player, event)) {
                continue;
            }
            cancel = true;
        }
        if (cancel) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        PlayerData data = Plunder.getInstance().getDataManager().getPlayerData(player);
        if (data == null) {
            return;
        }
        boolean cancel = false;
        for (Check check : data.getChecks()) {
            if (!check.handleEvent(player, event)) {
                continue;
            }
            cancel = true;
        }
        if (cancel) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent e) {
        if (Plunder.getInstance().getLastGround().containsKey(e.getPlayer().getUniqueId()) && e.getTo().distance(Plunder.getInstance().getLastGround().get(e.getPlayer().getUniqueId()).getValue()) > 2.0) {
            Plunder.getInstance().getLastGround().remove(e.getPlayer().getUniqueId());
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e) {
        Plunder.getInstance().getLastGround().remove(e.getPlayer().getUniqueId());
    }

    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        CraftPlayer craftplayer = (CraftPlayer)player;
        Channel channel = craftplayer.getHandle().playerConnection.networkManager.channel;
        if (ClientVersionUtil.getProtocolVersion(player) > 47) {
            player.kickPlayer(ChatColor.RED + "You version is not allowed to join this server!");
            return;
        }
        ChannelPipeline pipeline = channel.pipeline();
        if (pipeline == null) {
            return;
        }
        String handlerName = "plunder_packet_handler";
        channel.eventLoop().submit(() -> {
            pipeline.addBefore("packet_handler", handlerName, new PacketHandler(player));
            if (pipeline.get("decompress") != null) {
                pipeline.addAfter("decompress", "cf_decompress", new ByteBufReader(craftplayer));
            } else {
                pipeline.addAfter("splitter", "cf_decompress", new ByteBufReader(craftplayer));
            }
            pipeline.addBefore("packet_handler", "invalid_packet_handler", new ByteBufReader(craftplayer));
        });
        TraceManager.initiateTrace(player);
    }

//    @EventHandler
//    public void onPlayerReport(ReportEvent e) {
//        Report report;
//        ManagerManager managerManager = Plunder.getInstance().getManagerManager();
//        Manager manager = managerManager.getManagerByUUID((report = e.getReport()).getTargetUuid());
//        if (manager == null) {
//            return;
//        }
//        if (manager.getTrust() > 100) {
//            manager.setTrust(manager.getTrust() - 100);
//        } else {
//            manager.setTrust(0);
//        }
//    }

    @EventHandler(priority=EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        TraceManager.removeTrace(player);
        Plunder.getInstance().getLastGround().remove(player.getUniqueId());
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Plunder.getInstance().getLastGround().remove(event.getEntity().getUniqueId());
    }

    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onPlayerDamage(EntityDamageEvent event) {
        if (event.getEntityType() == EntityType.PLAYER) {
            TraceManager.addLocation((Player)event.getEntity(), event.getEntity().getLocation());
            PlayerData data = Plunder.getInstance().getDataManager().getPlayerData((Player)event.getEntity());
            if (data == null) {
                return;
            }
            if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
                data.setLastFallDamageTicks(data.getTicks());
            }
            data.setLastDamageTime(System.currentTimeMillis());
        }
    }
}

