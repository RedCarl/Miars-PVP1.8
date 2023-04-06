package cn.mcarl.miars.faction.manager;

import cn.mcarl.miars.faction.MiarsFaction;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @Author: carl0
 * @DATE: 2022/6/27 13:43
 */
public class TeleportManager {
    private static TeleportManager instance = new TeleportManager();

    public static TeleportManager getInstance(){
        return instance;
    }


    Map<UUID,Long> time = new HashMap<>();
    public void teleport(Player player, Location location) {
        if (player == null) {
            return;
        }
        if (time.containsKey(player.getUniqueId())){
            time.put(player.getUniqueId(),System.currentTimeMillis());
            return;
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!time.containsKey(player.getUniqueId())){
                    time.put(player.getUniqueId(),System.currentTimeMillis());
                }

                if (time.get(player.getUniqueId()) == 0 ){
                    time.remove(player.getUniqueId());
                    cancel();
                    return;
                }

                if ((System.currentTimeMillis() - time.get(player.getUniqueId()))/1000>=5){
                    player.teleport(location);
                    time.remove(player.getUniqueId());
                    cancel();
                    return;
                }
                //player.sendActionBar(new TranslateMessage("factions.teleport.time",(5-(System.currentTimeMillis() - time.get(player.getUuid()))/1000)));
            }
        }.runTaskTimer(MiarsFaction.getInstance(),20,20);
    }

    public void cancelTeleport(Player player){
        if (time.containsKey(player.getUniqueId()) && time.get(player.getUniqueId())!=0){
            time.put(player.getUniqueId(),0L);
            //player.sendActionBar(new TranslateMessage("factions.teleport.cancel"));
        }
    }


}
