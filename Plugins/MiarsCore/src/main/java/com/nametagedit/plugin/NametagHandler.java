package com.nametagedit.plugin;

import cn.mcarl.miars.core.MiarsCore;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

@Getter
@Setter
public class NametagHandler implements Listener {
    private NametagManager nametagManager;
    private MiarsCore plugin;

    public NametagHandler(MiarsCore plugin,NametagManager nametagManager) {
        this.plugin = plugin;
        this.nametagManager = nametagManager;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }


    /**
     * Cleans up any nametag data on the server to prevent memory leaks
     */
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        nametagManager.reset(event.getPlayer().getName());
    }

    /**
     * Applies tags to a player
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        nametagManager.sendTeams(player);
    }
}