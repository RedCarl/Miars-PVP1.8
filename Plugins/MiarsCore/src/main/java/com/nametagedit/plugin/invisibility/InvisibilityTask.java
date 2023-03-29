package com.nametagedit.plugin.invisibility;

import cn.mcarl.miars.core.MiarsCore;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class InvisibilityTask extends BukkitRunnable {
    @Override
    public void run(){
        List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
        if(players.isEmpty()){
            return;
        }

        players.forEach(player ->{
            if(player.hasPotionEffect(PotionEffectType.INVISIBILITY)){
                MiarsCore.getApi().hideNametag(player);
            }else{
                MiarsCore.getApi().showNametag(player);
            }
        });
    }

}