package cn.mcarl.miars.skypvp.utils;

import cn.mcarl.miars.storage.utils.ItemBuilder;
import cn.mcarl.miars.core.utils.MiarsUtil;
import cn.mcarl.miars.skypvp.MiarsSkyPVP;
import cn.mcarl.miars.skypvp.enums.LuckBlockType;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.scheduler.BukkitRunnable;

public class RotatingHead {

    private static final RotatingHead instance = new RotatingHead();
    public static RotatingHead getInstance() {
        return instance;
    }
    public ArmorStand spawnOrb(Location location, LuckBlockType luckBlockType) {

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

        new BukkitRunnable() {
            int i = 5;
            boolean a = true;
            @Override
            public void run() {
                if (stand.isDead()){
                    cancel();
                }

                Location l = stand.getLocation();


                if (i==0){
                    a=false;
                }else if (i==5){
                    a=true;
                }

                if (a){
                    l.add(0,0.17,0);
                    i--;
                }else{
                    l.add(0,-0.17,0);
                    i++;
                }

                stand.setHeadPose(stand.getHeadPose().add(0, 0.17, 0));
                stand.teleport(l);
            }

        }.runTaskTimer(MiarsSkyPVP.getInstance(), 0, 2);

        return stand;
    }
}
