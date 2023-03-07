package cn.mcarl.miars.skypvp.utils;

import cn.mcarl.miars.skypvp.conf.PluginConfig;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PlayerUtils {
    public static boolean isProtectedRegion(Player player){
        Location p = player.getLocation();
        Location min = PluginConfig.PROTECTED_REGION.MIN.get();
        Location max = PluginConfig.PROTECTED_REGION.MAX.get();
        if (p.getBlockX()<=max.getBlockX() && p.getBlockY()<=max.getBlockY() && p.getBlockZ()<=max.getBlockZ()){

            if (p.getBlockX()>=min.getBlockX() && p.getBlockY()>=min.getBlockY() && p.getBlockZ()>=min.getBlockZ()){
                return true;
            }
        }

        return false;
    }
}
