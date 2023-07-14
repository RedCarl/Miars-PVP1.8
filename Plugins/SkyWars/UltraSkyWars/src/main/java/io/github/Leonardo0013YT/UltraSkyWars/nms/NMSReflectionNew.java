//package io.github.Leonardo0013YT.UltraSkyWars.nms;
//
//import cn.mcarl.miars.core.utils.MiarsUtils;
//import io.github.Leonardo0013YT.UltraSkyWars.enums.DamageCauses;
//import io.github.Leonardo0013YT.UltraSkyWars.enums.nms.NametagVersion;
//import io.github.Leonardo0013YT.UltraSkyWars.superclass.NMSReflection;
//import net.md_5.bungee.api.ChatMessageType;
//import net.md_5.bungee.api.chat.BaseComponent;
//import net.md_5.bungee.api.chat.ComponentBuilder;
//import net.minecraft.server.v1_8_R3.EntityEnderDragon;
//import net.minecraft.server.v1_8_R3.EntityPlayer;
//import net.minecraft.server.v1_8_R3.Packet;
//import org.bukkit.Bukkit;
//import org.bukkit.entity.Entity;
//import org.bukkit.entity.LivingEntity;
//import org.bukkit.entity.Player;
//
//import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.Method;
//import java.util.Arrays;
//import java.util.Collection;
//
//public class NMSReflectionNew extends NMSReflection {
//
//    static NametagVersion nametagVersion;
//    private static String version;
//    private Method a, position, sendTitle, getHandle, getHandleEntity;
//    private DamageCauses causes;
//    private boolean isNewAction;
//
//    public NMSReflectionNew() {
//        try {
//            version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
//            sendTitle = getOBClass("CraftPlayer").getMethod("sendTitle", String.class, String.class, int.class, int.class, int.class);
//            getHandle = getOBClass("CraftPlayer").getMethod("getHandle");
//            getHandleEntity = getOBClass("CraftEntity").getMethod("getHandle");
//            nametagVersion = NametagVersion.valueOf(version);
//            a = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class);
//            position = getNMSClass("Entity").getMethod("setPositionRotation", double.class, double.class, double.class, float.class, float.class);
//            causes = DamageCauses.valueOf(version);
//            isNewAction = version.equals("v1_12_R1") || version.equals("v1_13_R2") || version.equals("v1_14_R1") || version.equals("v1_15_R1") || version.equals("v1_16_R1") || version.equals("v1_16_R2") || version.equals("v1_16_R3") || version.equals("v1_17_R1");
//            if (!isNewAction) {
//                a = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void sendPacket(Player player, Object packet) {
//        try {
//            EntityPlayer cp = (EntityPlayer) getHandle.invoke(player);
//            cp.b.sendPacket((Packet<?>) packet);
//        } catch (IllegalAccessException | InvocationTargetException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void setCollidesWithEntities(Player p, boolean bol) {
//        p.setCollidable(bol);
//    }
//
//    public DamageCauses getCauses() {
//        return causes;
//    }
//
//    public void freezeMob(LivingEntity mob) {
//    }
//
//    public void sendActionBar(String msg, Player... players) {
//        sendActionBar(msg, Arrays.asList(players));
//    }
//
//    public void sendActionBar(String msg, Collection<Player> players) {
//        for (Player p : players) {
//            if (p == null || !p.isOnline()) continue;
//            MiarsUtils.sendActionText(p,msg);
//        }
//    }
//
//    public void moveDragon(Entity ent, double x, double y, double z, float yaw, float pitch) {
//        if (ent == null) return;
//        try {
//            ((EntityEnderDragon)getHandleEntity.invoke(ent)).setPositionRotation(x, y, z, yaw, pitch);
//        } catch (Exception ignored) {
//        }
//    }
//
//    public void sendTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut, Player... players) {
//        sendTitle(title, subtitle, fadeIn, stay, fadeOut, Arrays.asList(players));
//    }
//
//    public void sendTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut, Collection<Player> players) {
//        for (Player p : players) {
//            if (p == null || !p.isOnline()) continue;
//            try {
//                sendTitle.invoke(p, title, subtitle, fadeIn, stay, fadeOut);
//            } catch (IllegalAccessException | InvocationTargetException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//}