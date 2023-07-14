package cn.mcarl.miars.core.manager;

import cn.mcarl.miars.core.MiarsCore;
import cn.mcarl.miars.core.impl.lunarclient.LunarClientAPI;
import cn.mcarl.miars.core.impl.lunarclient.cooldown.LunarClientAPICooldown;
import cn.mcarl.miars.storage.utils.ToolUtils;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class EnderPearlManager {
    private static final EnderPearlManager instance = new EnderPearlManager();

    public static EnderPearlManager getInstance() {
        return instance;
    }

    HashMap<UUID,Long> hashMap = new HashMap<>();

    public boolean isCancelled(Player player){
        if (hashMap.containsKey(player.getUniqueId())){
            if (!((System.currentTimeMillis()-hashMap.get(player.getUniqueId()))>=12000)){
                player.playSound(player.getLocation(), Sound.VILLAGER_NO,1,1);
                return true;
            }
        }
        return false;
    }

    public boolean putPearl(Player player){
        if (hashMap.containsKey(player.getUniqueId())){
            if (!((System.currentTimeMillis()-hashMap.get(player.getUniqueId()))>12000)){
                player.playSound(player.getLocation(), Sound.VILLAGER_NO,1,1);
                return true;
            }
        }else {
            hashMap.put(player.getUniqueId(),System.currentTimeMillis());
            LunarClientAPICooldown.sendCooldown(player, "EnderPearl");
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (hashMap.containsKey(player.getUniqueId())){
                        if (!((System.currentTimeMillis()-hashMap.get(player.getUniqueId()))>12000)){
                            int s = (int) ((12000-(System.currentTimeMillis()-hashMap.get(player.getUniqueId())))/1000);

                            player.setLevel(s);
                        }else {
                            hashMap.remove(player.getUniqueId());
                            cancel();
                        }

                    }
                }
            }.runTaskTimerAsynchronously(MiarsCore.getInstance(),1L,1L);
            ToolUtils.reduceXpBar(player,12*20);
        }
        return false;
    }


}
