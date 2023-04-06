package io.github.Leonardo0013YT.UltraSkyWars.utils;

import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class InstantFirework {

    public InstantFirework(FireworkEffect effect, Location loc) {
        Firework f = loc.getWorld().spawn(loc, Firework.class);
        FireworkMeta fm = f.getFireworkMeta();
        fm.addEffect(effect);
        f.setFireworkMeta(fm);
        try {
            Object eF = f.getClass().getMethod("getHandle").invoke(f);
            Field fl = eF.getClass().getDeclaredField("expectedLifespan");
            fl.setAccessible(true);
            fl.set(eF, 1);
        } catch (NoSuchMethodError | NoSuchMethodException | IllegalAccessException | InvocationTargetException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

}