// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.manager.listener;

import gg.noob.plunder.Plunder;
import gg.noob.plunder.checks.Check;
import gg.noob.plunder.data.PlayerData;
import gg.noob.plunder.manager.Manager;
import gg.noob.plunder.manager.ManagerManager;
import gg.noob.plunder.util.MongoManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class ManagerGeneralListener implements Listener
{
    @EventHandler
    public void onPlayerLogin(final AsyncPlayerPreLoginEvent e) {
        final MongoManager mongoManager = Plunder.getInstance().getMongoManager();
        final ManagerManager managerManager = Plunder.getInstance().getManagerManager();
        final UUID uuid = e.getUniqueId();
        final String name = e.getName();
        new BukkitRunnable() {
            @Override
            public void run() {
                Manager manager = mongoManager.getManager(uuid);
                if (manager == null) {
                    manager = new Manager(uuid, name);
                }
                managerManager.getManagers().add(manager);
                manager.save();
            }
        }.runTaskAsynchronously(Plunder.getInstance());
    }
    
    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent e) {
        final ManagerManager managerManager = Plunder.getInstance().getManagerManager();
        final Player player = e.getPlayer();
        final Manager manager = managerManager.getManagerByUUID(player.getUniqueId());
        if (manager == null) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    final Manager manager = managerManager.getManagerByUUID(player.getUniqueId());
                    if (manager == null) {
                        player.kickPlayer(ChatColor.RED + "Your manager hasn't loaded yet, try again later!");
                    }
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (manager == null) {
                                return;
                            }
                            if (manager.getTrust() == 0) {
                                manager.setTrust(250);
                            }
                            if (manager.getTrust() == 0) {
                                manager.setTrust(100);
                            }
                        }
                    }.runTaskLater(Plunder.getInstance(), 10L);
                }
            }.runTaskLater(Plunder.getInstance(), 10L);
        }
    }
    
    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent e) {
        final ManagerManager managerManager = Plunder.getInstance().getManagerManager();
        final Player player = e.getPlayer();
        final Manager manager = managerManager.getManagerByUUID(player.getUniqueId());
        if (manager == null) {
            return;
        }
        final PlayerData data = Plunder.getInstance().getDataManager().getPlayerData(player);
        if (data != null) {
            int size = 0;
            for (final Check check : data.getChecks()) {
                if (check.getVl() > 0) {
                    ++size;
                }
            }
            if (size > 0) {
                int addTrust = 0;
                if (data.getTicks() > 12000 && data.getLastAttacked() != null) {
                    if (size < 10) {
                        addTrust += 50;
                    }
                    else if (size < 30) {
                        addTrust += 25;
                    }
                    else {
                        addTrust -= 50;
                    }
                    manager.setTrust(manager.getTrust() + addTrust);
                    if (manager.getTrust() < 0) {
                        manager.setTrust(0);
                    }
                    manager.save();
                }
            }
        }
        managerManager.getManagers().remove(manager);
    }
}
