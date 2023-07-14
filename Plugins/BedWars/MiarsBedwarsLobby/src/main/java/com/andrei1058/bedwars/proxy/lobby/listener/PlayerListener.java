package com.andrei1058.bedwars.proxy.lobby.listener;

import cc.carm.lib.easyplugin.utils.ColorParser;
import cn.mcarl.miars.core.publics.items.Ranks;
import cn.mcarl.miars.core.publics.items.ServerMenu;
import cn.mcarl.miars.storage.entity.MPlayer;
import cn.mcarl.miars.storage.entity.MRank;
import cn.mcarl.miars.storage.storage.data.MPlayerDataStorage;
import cn.mcarl.miars.storage.storage.data.MRankDataStorage;
import com.andrei1058.bedwars.proxy.BedWarsProxy;
import com.andrei1058.bedwars.proxy.lobby.manager.ScoreBoardManager;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.*;

public class PlayerListener implements Listener {

    Location spawn = new Location(Bukkit.getWorld("world"),0.5,142,0.5,-90,0);
    @EventHandler
    public void PlayerJoinEvent(PlayerJoinEvent e){
        Player player = e.getPlayer();

        new ServerMenu().give(player,0);
        new Ranks(true,false).give(player,1);

        player.teleport(spawn);
        ScoreBoardManager.getInstance().joinPlayer(player);
    }

    @EventHandler
    public void PlayerQuitEvent(PlayerQuitEvent e){
        Player player = e.getPlayer();
        ScoreBoardManager.getInstance().removePlayer(player);
    }

    @EventHandler
    public void FoodLevelChangeEvent(FoodLevelChangeEvent e){
        if (e.getEntity() instanceof Player p){
            p.setFoodLevel(20);
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void PlayerDropItemEvent(PlayerDropItemEvent event) {
        // 禁止玩家丢弃物品
        event.setCancelled(true);
    }
    @EventHandler
    public void PlayerMoveEvent(PlayerMoveEvent e){
        Player player = e.getPlayer();
        Location location = player.getLocation();

        if (location.getBlockY() <= 0){
            player.teleport(spawn);
        }
    }

    @EventHandler
    public void AsyncPlayerChatEvent(AsyncPlayerChatEvent e){
        MPlayer mPlayer = MPlayerDataStorage.getInstance().getMPlayer(e.getPlayer());
        MRank mRank = MRankDataStorage.getInstance().getMRank(mPlayer.getRank());
        e.setFormat(ColorParser.parse(PlaceholderAPI.setPlaceholders(e.getPlayer(), "&7["+BedWarsProxy.getLevelManager().getLevel(e.getPlayer()))+"&7]" +mRank.getPrefix()+mRank.getNameColor()+"%1$s&f: %2$s"));
    }

    @EventHandler
    public void EntityDamageEvent(EntityDamageEvent e){
        if (e.getEntity() instanceof Player player){
            if (e.getCause() == EntityDamageEvent.DamageCause.FALL){
                e.setCancelled(true);
            }
        }
    }


}
