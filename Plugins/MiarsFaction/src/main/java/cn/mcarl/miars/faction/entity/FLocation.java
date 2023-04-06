package cn.mcarl.miars.faction.entity;

import lombok.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.Objects;


/**
 * @Author: carl0
 * @DATE: 2022/7/14 14:12
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class FLocation {
    private String world;
    private int x, y, z;

    public Location toLocation(FLocation fLocation) {
        return new Location(Bukkit.getWorld(fLocation.world), fLocation.getX(), fLocation.getY(), fLocation.getZ());
    }

    public static FLocation toFLocation(Location location) {
        return new FLocation(location.getWorld().getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }
}
