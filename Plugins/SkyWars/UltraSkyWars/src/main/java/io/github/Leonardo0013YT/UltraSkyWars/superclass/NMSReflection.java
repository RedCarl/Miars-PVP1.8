package io.github.Leonardo0013YT.UltraSkyWars.superclass;

import io.github.Leonardo0013YT.UltraSkyWars.enums.DamageCauses;
import io.github.Leonardo0013YT.UltraSkyWars.enums.nms.NametagVersion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.Collection;

public abstract class NMSReflection {

    private final static String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

    public static NametagVersion getNametagVersion() {
        return NametagVersion.valueOf(version);
    }

    public static Class<?> getNMSClass(String name) {
        try {
            return Class.forName("net.minecraft.server." + version + "." + name);
        } catch (Exception var3) {
            return null;
        }
    }

    public static Class<?> getOBClass(String name) {
        try {
            return Class.forName("org.bukkit.craftbukkit." + version + "." + name);
        } catch (Exception var3) {
            return null;
        }
    }

    public abstract void sendPacket(Player player, Object object);

    public abstract void setCollidesWithEntities(Player p, boolean bol);

    public abstract DamageCauses getCauses();

    public abstract void freezeMob(LivingEntity mob);

    public abstract void sendActionBar(String msg, Player... players);

    public abstract void sendActionBar(String msg, Collection<Player> players);

    public abstract void moveDragon(Entity ent, double x, double y, double z, float yaw, float pitch);

    public abstract void sendTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut, Player... players);

    public abstract void sendTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut, Collection<Player> players);

}