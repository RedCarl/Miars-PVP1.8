package cn.mcarl.miars.skypvp.manager;

import cn.mcarl.miars.skypvp.MiarsSkyPVP;
import cn.mcarl.miars.skypvp.entitiy.OreBlock;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class OreRespawnManager {
    private static final OreRespawnManager instance = new OreRespawnManager();
    public static OreRespawnManager getInstance() {
        return instance;
    }

    private final Map<OreBlock, Long> data = new HashMap<>();

    public void init(){
        tick();
    }

    public void tick(){
        new BukkitRunnable() {
            @Override
            public void run() {
                for (OreBlock b:data.keySet()) {
                    if (System.currentTimeMillis() - data.get(b) >= 10000){
                        b.getLocation().getBlock().setType(b.getMaterial());
                    }
                }
            }
        }.runTaskTimer(MiarsSkyPVP.getInstance(),0,20);
    }

    public void put(OreBlock block){
        data.put(block,System.currentTimeMillis());
    }

}
