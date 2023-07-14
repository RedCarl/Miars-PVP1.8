package cn.mcarl.miars.core.utils;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class Cube {
    private World world;
    private Location min;
    private Location max;

    public Cube(Location min,Location max){
       this.world = min.getWorld();
       this.min = min;
       this.max = max;
    }

    public boolean isPlayerInCube(Player player){
        Location p = player.getLocation();
        Location min = this.min;
        Location max = this.max;
        if (p.getBlockX()<=max.getBlockX() && p.getBlockY()<=max.getBlockY() && p.getBlockZ()<=max.getBlockZ()){
            if (p.getBlockX()>=min.getBlockX() && p.getBlockY()>=min.getBlockY() && p.getBlockZ()>=min.getBlockZ()){
                return true;
            }
        }
        return false;
    }
}
