package cn.mcarl.miars.skypvp.utils;

import cn.mcarl.miars.core.utils.ItemBuilder;
import cn.mcarl.miars.core.utils.MiarsUtil;
import cn.mcarl.miars.skypvp.MiarsSkyPVP;
import cn.mcarl.miars.skypvp.enums.LuckBlockType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;

import java.util.HashMap;
import java.util.Map;

public class RotatingHead {

    private static final RotatingHead instance = new RotatingHead();
    public static RotatingHead getInstance() {
        return instance;
    }
    private final Map<String,ArmorStand> standMap = new HashMap<>();
    public void spawnOrb(String name, Location location, LuckBlockType luckBlockType) {

        // Gets world the player is in.
        World world = location.getWorld();

        // Variables
        Location loc;
        ArmorStand stand;

        // Sets the location
        loc = location;

        // Creates the armor stand and sets it's visibility and gravity to false.
        stand = (ArmorStand) world.spawnEntity(loc, EntityType.ARMOR_STAND);
        stand.setVisible(false);
        stand.setGravity(false);
        stand.setCustomName("miars_lucky"+"."+luckBlockType.name());

        // Sets the helmet of the armor stand to a player head.
        stand.getEquipment().setHelmet(
                new ItemBuilder(
                        MiarsUtil.createSkull(luckBlockType.getValue())
                )
                        .toItemStack()
        );

        standMap.put(name,stand);

        new BukkitRunnable() {
            int i = 5;
            boolean a = true;
            @Override
            public void run() {
                if (stand.isDead()){
                    MiarsSkyPVP.getInstance().log("dead!");
                    cancel();
                }

                Location l = stand.getLocation();


                if (i==0){
                    a=false;
                }else if (i==5){
                    a=true;
                }

                if (a){
                    l.add(0,0.20,0);
                    i--;
                }else{
                    l.add(0,-0.20,0);
                    i++;
                }

                stand.setHeadPose(stand.getHeadPose().add(0, 0.17, 0));
                stand.teleport(l);
            }

        }.runTaskTimer(MiarsSkyPVP.getInstance(), 0, 2);

    }
}
