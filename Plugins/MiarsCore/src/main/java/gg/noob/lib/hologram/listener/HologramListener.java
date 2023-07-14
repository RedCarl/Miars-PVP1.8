package gg.noob.lib.hologram.listener;

import cn.mcarl.miars.core.MiarsCore;
import gg.noob.lib.hologram.Hologram;
import gg.noob.lib.hologram.HologramManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HologramListener implements Listener {

    private Map<UUID, Long> lastJoin = new HashMap<>();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        HologramManager hologramManager = MiarsCore.getInstance().getHologramManager();
        Player player = e.getPlayer();

        lastJoin.put(player.getUniqueId(), System.currentTimeMillis());

        Bukkit.getScheduler().runTaskLater(MiarsCore.getInstance(), () -> {
            for (Hologram hologram : hologramManager.getHolograms()) {
                hologram.show(player);
            }
        }, 1L);
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent e) {
        HologramManager hologramManager = MiarsCore.getInstance().getHologramManager();
        Player player = e.getPlayer();
        Location to = e.getTo();
        Location from = e.getFrom();

        Bukkit.getScheduler().runTaskLater(MiarsCore.getInstance(), () -> {
            if (System.currentTimeMillis() - lastJoin.get(player.getUniqueId()) < 1000) {
                return;
            }

            for (Hologram hologram : hologramManager.getHolograms()) {
                if (hologram.isInDisplayRange(to) && !hologram.isInDisplayRange(from)) {
                    hologram.destroy(player);
                    hologram.show(player);
                } else if (!hologram.isInDisplayRange(to) && hologram.isInDisplayRange(from)) {
                    hologram.destroy(player);
                }
            }
        }, 1L);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        HologramManager hologramManager = MiarsCore.getInstance().getHologramManager();
        Player player = e.getPlayer();
        Location to = e.getTo();
        Location from = e.getFrom();

        for (Hologram hologram : hologramManager.getHolograms()) {
            if (hologram.isInDisplayRange(to) && !hologram.isInDisplayRange(from)) {
                hologram.destroy(player);
                hologram.show(player);
            } else if (!hologram.isInDisplayRange(to) && hologram.isInDisplayRange(from)) {
                hologram.destroy(player);
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        HologramManager hologramManager = MiarsCore.getInstance().getHologramManager();
        Player player = e.getPlayer();

        for (Hologram hologram : hologramManager.getHolograms()) {
            hologram.remove(player.getPlayer());
        }
    }

}
