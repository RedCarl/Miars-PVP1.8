package cn.mcarl.miars.core.listener;

import cn.mcarl.miars.core.manager.EnderPearlManager;
import org.bukkit.Material;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class EnderPearlListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void ProjectileLaunchEvent(ProjectileLaunchEvent e){
        if (e.getEntity() instanceof EnderPearl enderPearl){
            if (enderPearl.getShooter() instanceof Player player){
                e.setCancelled(EnderPearlManager.getInstance().putPearl(player));
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (!e.hasItem() || e.getItem().getType() != Material.ENDER_PEARL || !e.getAction().name().contains("RIGHT_")) {
            return;
        }

        Player player = e.getPlayer();
        e.setCancelled(EnderPearlManager.getInstance().isCancelled(player));
        player.updateInventory();
    }

}
