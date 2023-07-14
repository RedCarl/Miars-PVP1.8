// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.data.listener;

import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import gg.noob.plunder.data.DataManager;
import gg.noob.plunder.Plunder;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.Listener;

public class DataGeneralListener implements Listener
{
    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent e) {
        final DataManager dataManager = Plunder.getInstance().getDataManager();
        final Player player = e.getPlayer();
        dataManager.addPlayerData(player);
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(final PlayerQuitEvent e) {
        final DataManager dataManager = Plunder.getInstance().getDataManager();
        final Player player = e.getPlayer();
        dataManager.removePlayerData(player);
    }
}
