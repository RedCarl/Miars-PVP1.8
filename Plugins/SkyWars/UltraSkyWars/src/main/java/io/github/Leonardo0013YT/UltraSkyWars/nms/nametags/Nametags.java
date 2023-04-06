package io.github.Leonardo0013YT.UltraSkyWars.nms.nametags;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.enums.nms.NametagVersion;
import io.github.Leonardo0013YT.UltraSkyWars.nms.NMSReflectionOld;
import io.github.Leonardo0013YT.UltraSkyWars.superclass.NMSReflection;
import io.github.Leonardo0013YT.UltraSkyWars.xseries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

public class Nametags {

    static Constructor<?> PacketPlayOutScoreboardTeam, ChatComponentText;

    static {
        try {
            PacketPlayOutScoreboardTeam = NMSReflection.getNMSClass("PacketPlayOutScoreboardTeam").getConstructor();
            ChatComponentText = NMSReflection.getNMSClass("ChatComponentText").getConstructor(String.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    Object packet;

    public Nametags(String teamName, String displayName, String prefix) {
        try {
            NametagVersion version = NMSReflectionOld.getNametagVersion();
            packet = PacketPlayOutScoreboardTeam.newInstance();
            Object displayObject = ChatComponentText.newInstance(displayName);
            Object prefixObject = ChatComponentText.newInstance(prefix);
            this.setField(version.getH(), 0);
            this.setField(version.getB(), XMaterial.isNewVersion() ? displayObject : displayName);
            this.setField(version.getA(), teamName);
            this.setField(version.getC(), XMaterial.isNewVersion() ? prefixObject : prefix);
            this.setField(version.getE(), "always");
            this.setField(version.getI(), 1);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public void deleteTeam(String teamName) {
        for (Player pl : Bukkit.getOnlinePlayers()) {
            remove(teamName, pl);
        }
    }

    public void deleteTeam(Player p, String teamName) {
        remove(teamName, p);
    }

    private void remove(String teamName, Player p) {
        try {
            Object packet = PacketPlayOutScoreboardTeam.newInstance();
            NametagVersion version = NMSReflectionOld.getNametagVersion();
            Field f = packet.getClass().getDeclaredField(version.getA());
            f.setAccessible(true);
            f.set(packet, teamName);
            f.setAccessible(false);
            Field f2 = packet.getClass().getDeclaredField(version.getH());
            f2.setAccessible(true);
            f2.set(packet, 1);
            f2.setAccessible(false);
            UltraSkyWars.get().getVc().getReflection().sendPacket(p, packet);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void addPlayer(Player pl) {
        try {
            this.add(pl);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void sendToPlayer(Player pl) {
        UltraSkyWars.get().getVc().getReflection().sendPacket(pl, packet);
    }

    public void setField(String field, Object value) {
        try {
            Field f = this.packet.getClass().getDeclaredField(field);
            f.setAccessible(true);
            f.set(this.packet, value);
            f.setAccessible(false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public void add(Player pl) throws NoSuchFieldException, IllegalAccessException {
        NametagVersion version = NMSReflectionOld.getNametagVersion();
        Field f = this.packet.getClass().getDeclaredField(version.getG());
        f.setAccessible(true);
        ((Collection<String>) f.get(this.packet)).add(pl.getName());
    }


}