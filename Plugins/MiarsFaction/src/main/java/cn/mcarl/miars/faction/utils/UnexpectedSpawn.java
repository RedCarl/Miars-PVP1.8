
package cn.mcarl.miars.faction.utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.Arrays;
import java.util.Random;

public class UnexpectedSpawn {

    private static UnexpectedSpawn unexpectedSpawn = new UnexpectedSpawn();

    public static UnexpectedSpawn getInstance(){
        return unexpectedSpawn;
    }

    Material[] b = {
            Material.AIR,
            Material.WATER,
            Material.STATIONARY_WATER,
            Material.STATIONARY_LAVA,
            Material.LAVA,
            Material.BEDROCK
    };

    public Location randomLocation(World w) {
        int a = 0;
        do {

            Random r = new Random();

            int x = -1000 + r.nextInt(1000 - -1000 + 1);

            int z = -1000 + r.nextInt(1000 - -1000 + 1);

            int y = w.getHighestBlockYAt( x , z );

            Block block = w.getHighestBlockAt( x , z );

            if (Arrays.asList(b).contains(block.getType())){
                a++;
            }else {
                return new Location(w, x, y, z);
            }

        }while (a <= 128);

        return null;
    }
}
