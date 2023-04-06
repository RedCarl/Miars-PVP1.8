package io.github.Leonardo0013YT.UltraSkyWars.modules.soulwell.listeners;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.modules.soulwell.InjectionSoulWell;
import io.github.Leonardo0013YT.UltraSkyWars.modules.soulwell.soulwell.SoulWell;
import io.github.Leonardo0013YT.UltraSkyWars.modules.soulwell.soulwell.SoulWellSession;
import io.github.Leonardo0013YT.UltraSkyWars.superclass.UltraInventory;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerListener implements Listener {

    private UltraSkyWars plugin;
    private InjectionSoulWell is;

    public PlayerListener(UltraSkyWars plugin, InjectionSoulWell is) {
        this.plugin = plugin;
        this.is = is;
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();
        if (plugin.getCm().isSoulwellEnabled()) {
            if (plugin.getIjm().getSoulwell().getSwm().isSession(p)) {
                SoulWellSession sws = plugin.getIjm().getSoulwell().getSwm().getSession(p);
                if (sws.isRolling()) {
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> p.openInventory(sws.getInv()), 1);
                }
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            Block b = e.getClickedBlock();
            if (plugin.getCm().isSoulwellEnabled()) {
                if (is.getSwm().getSoulWells().containsKey(b.getLocation())) {
                    if (!plugin.getGm().isPlayerInGame(p) && !(is.getSwm().isSession(p) && is.getSwm().getSession(p).isConfirm())) {
                        if (plugin.getLvl().isEmpty()) {
                            p.sendMessage(plugin.getLang().get(p, "messages.noRewards"));
                            e.setCancelled(true);
                            return;
                        }
                        SoulWell sw = is.getSwm().getSoulWells().get(b.getLocation());
                        e.setCancelled(true);
                        is.getSwm().addSession(p, sw);
                        UltraInventory inventory = plugin.getUim().getMenus("soulwellmenu");
                        plugin.getUim().openContentInventory(p, inventory);
                    }
                }
            }
        }
    }

}