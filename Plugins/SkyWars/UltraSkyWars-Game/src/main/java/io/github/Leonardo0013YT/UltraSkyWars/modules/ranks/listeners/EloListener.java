package io.github.Leonardo0013YT.UltraSkyWars.modules.ranks.listeners;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.api.events.data.USWPlayerLoadEvent;
import io.github.Leonardo0013YT.UltraSkyWars.modules.ranks.InjectionEloRank;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class EloListener implements Listener {

    private InjectionEloRank eloInjection;
    private UltraSkyWars plugin;

    public EloListener(UltraSkyWars plugin, InjectionEloRank eloInjection) {
        this.plugin = plugin;
        this.eloInjection = eloInjection;
    }

    @EventHandler
    public void onMenu(InventoryClickEvent e) {
        if (e.getView().getTitle().equals(eloInjection.getRankeds().get("menus.title"))) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void loadPlayer(USWPlayerLoadEvent e) {
        Player p = e.getPlayer();
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            eloInjection.getErm().checkUpgrateOrDegrate(p);
        });
    }

}