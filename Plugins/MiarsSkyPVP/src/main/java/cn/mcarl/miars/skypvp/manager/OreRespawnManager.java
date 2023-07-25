package cn.mcarl.miars.skypvp.manager;

import cn.mcarl.miars.skypvp.MiarsSkyPVP;
import cn.mcarl.miars.skypvp.entity.OreBlock;
import org.bukkit.Material;
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
                    if (b.getMaterial()== Material.IRON_ORE){
                        if (System.currentTimeMillis() - data.get(b) >= 1000*60*3){
                            b.getLocation().getBlock().setType(b.getMaterial());
                        }
                    }
                    if (b.getMaterial()== Material.GOLD_ORE){
                        if (System.currentTimeMillis() - data.get(b) >= 1000*60*5){
                            b.getLocation().getBlock().setType(b.getMaterial());
                        }
                    }
                    if (b.getMaterial()== Material.QUARTZ_ORE){
                        if (System.currentTimeMillis() - data.get(b) >= 1000*60*5){
                            b.getLocation().getBlock().setType(b.getMaterial());
                        }
                    }
                    if (b.getMaterial()== Material.DIAMOND_ORE){
                        if (System.currentTimeMillis() - data.get(b) >= 1000*60*10){
                            b.getLocation().getBlock().setType(b.getMaterial());
                        }
                    }
                    if (b.getMaterial()== Material.EMERALD_ORE){
                        if (System.currentTimeMillis() - data.get(b) >= 1000*60*15){
                            b.getLocation().getBlock().setType(b.getMaterial());
                        }
                    }
                }
            }
        }.runTaskTimer(MiarsSkyPVP.getInstance(),0,20);
    }

    public void put(OreBlock block){
        data.put(block,System.currentTimeMillis());
    }

    public void clear(){
        for (OreBlock oreBlock:data.keySet()) {
            oreBlock.getLocation().getBlock().setType(oreBlock.getMaterial());
        }
        data.clear();
    }
}
