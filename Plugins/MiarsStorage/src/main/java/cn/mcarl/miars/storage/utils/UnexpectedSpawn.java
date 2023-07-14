
package cn.mcarl.miars.storage.utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

import java.util.HashSet;
import java.util.concurrent.ThreadLocalRandom;

public class UnexpectedSpawn {

    private static HashSet<Material> getBlacklistedMaterials() {
        HashSet<Material> a = new HashSet<>();
        a.add(Material.AIR);
        a.add(Material.WATER);
        a.add(Material.LAVA);
        a.add(Material.BEDROCK);
        return a;
    }

    private static int AddFailRange(int previous, int rangetoadd) {
        int result = 0;
        int valtype = Integer.signum(previous);

        if (valtype == 0 || valtype == 1){
            result = previous + rangetoadd;
        }
        else {
            result = previous - rangetoadd;
        }
        return result;
    }

    public static Location getRandomSpawnLocation(World world) {
        int range = (int) (world.getWorldBorder().getSize()/2 - 1000);

        int tryCount = 0;

        int xmin = -range;
        int xmax = range;
        int zmin = -range;
        int zmax = range;
        int retryonfail = range+1000;

        boolean isSpawnBlacklistInverted = false;

        while (true) {

            if(tryCount == 5000) {
                xmin = AddFailRange(xmin ,retryonfail);
                xmax = AddFailRange(xmax ,retryonfail);
                zmin = AddFailRange(zmin ,retryonfail);
                zmax = AddFailRange(zmax ,retryonfail);
            }
            else if (tryCount >= 10000) {
                Location location = world.getSpawnLocation();
                return location.add(0.5d, 1d, 0.5d);
            }

            int x = xmin + ThreadLocalRandom.current().nextInt((xmax - xmin) + 1);
            int z = zmin + ThreadLocalRandom.current().nextInt((zmax - zmin) + 1);
            int y = world.getHighestBlockYAt(x, z);

            tryCount++;

            Location location = new Location(world, x, y, z);

            // Special case for server version < 1.15.2 (?)
            // Related: https://www.spigotmc.org/threads/gethighestblockat-returns-air.434090/
            if (location.getBlock().getType() == Material.AIR) {
                location = location.subtract(0, 1, 0);
            }

            if(!isSpawnBlacklistInverted) {
                if (getBlacklistedMaterials().contains(location.getBlock().getType())) {
                    continue;
                }
            }
            else {
                if (!getBlacklistedMaterials().contains(location.getBlock().getType())) {
                    continue;
                }
            }

            return location.add(0.5d, 1d, 0.5d);
        }
    }
}
