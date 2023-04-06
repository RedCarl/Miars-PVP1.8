package com.andrei1058.bedwars.utils;

import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.arena.Arena;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Random;

public class Step {
    static boolean foot;

    public static void run(Player p) {
        if (!(new Random().nextInt(100)>=50)){
            return;
        }

        IArena a = Arena.getArenaByPlayer(p);
        if (a!=null && a.getShowTime().containsKey(p)){
            Location l = p.getLocation(); //Get the player's location
            l.setY(Math.floor(l.getY())); //Make sure the location's y is an integer

            if (!l.clone().subtract(0, 1, 0).getBlock().isEmpty())
            {
                double x = Math.cos(Math.toRadians(p.getLocation().getYaw())) * 0.25d;
                double y = Math.sin(Math.toRadians(p.getLocation().getYaw())) * 0.25d;

                if (foot) {
                    l.add(x, 0.025D, y);
                } else {
                    l.subtract(x, -0.025D, y);
                }

                ParticleEffect.FOOTSTEP.display(0, 0, 0, 2, 2, l, 100);
            }
        }
        foot = !foot;
    }
}