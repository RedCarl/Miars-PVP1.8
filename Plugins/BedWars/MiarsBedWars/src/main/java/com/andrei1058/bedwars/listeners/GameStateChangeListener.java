package com.andrei1058.bedwars.listeners;

import com.andrei1058.bedwars.API;
import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.arena.GameState;
import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.events.gameplay.GameStateChangeEvent;
import com.andrei1058.bedwars.arena.Arena;
import com.andrei1058.bedwars.manager.ScoreBoardManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class GameStateChangeListener implements Listener {

    @EventHandler
    public void GameStateChangeEvent(GameStateChangeEvent e){
        IArena arena = e.getArena();
        for (Player player:Bukkit.getOnlinePlayers()) {
            API.setPlayerTag(arena, player);
        }

        if (e.getNewState().equals(GameState.restarting)){
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (Bukkit.getOnlinePlayers().size()==0){
                        Bukkit.shutdown();
                    }
                }
            }.runTaskTimer(BedWars.plugin,0,20);
        }
    }

    @EventHandler
    public void PlayerJoinEvent(PlayerJoinEvent e){
        Player player = e.getPlayer();
        IArena arena = Arena.getArenaByPlayer(player);
        API.setPlayerTag(arena, player);


        // Give scoreboard
        ScoreBoardManager.getInstance().joinPlayer(player);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        final Player player = e.getPlayer();
        ScoreBoardManager.getInstance().removePlayer(player);
    }
}
