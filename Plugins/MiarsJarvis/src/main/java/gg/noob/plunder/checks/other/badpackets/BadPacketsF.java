// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.checks.other.badpackets;

import gg.noob.plunder.data.PlayerData;
import net.minecraft.server.v1_8_R3.PacketPlayInBlockDig;
import net.minecraft.server.v1_8_R3.EnumDirection;
import net.minecraft.server.v1_8_R3.World;
import net.minecraft.server.v1_8_R3.PacketPlayOutBlockChange;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import net.minecraft.server.v1_8_R3.PacketPlayInBlockPlace;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.entity.Player;
import gg.noob.plunder.checks.Check;

public class BadPacketsF extends Check
{
    private static boolean crasher;
    private static double health;
    
    public BadPacketsF() {
        super("Bad Packets (F)");
    }
    
    @Override
    public boolean handleReceivedPacketCanceled(final Player player, final Packet packet) {
        boolean cancel = false;
        final PlayerData data = this.getPlayerData();
        if (packet instanceof PacketPlayInFlying) {
            if (data.backTicks > 0 && System.currentTimeMillis() - data.backTime > 50L) {
                data.executeBack();
                final PlayerData playerData = data;
                --playerData.backTicks;
                data.backTime = System.currentTimeMillis();
                cancel = true;
            }
            if (data.fallBackTicks > 0 && System.currentTimeMillis() - data.fallBackTime > 50L) {
                data.executeFallBack();
                final PlayerData playerData2 = data;
                --playerData2.fallBackTicks;
                data.fallBackTime = System.currentTimeMillis();
                cancel = true;
            }
        }
        else if (packet instanceof PacketPlayInUseEntity) {
            if (data.getBackTicks() > 0) {
                cancel = true;
            }
        }
        else if (packet instanceof PacketPlayInBlockPlace) {
            if (data.getBackTicks() > 0) {
                cancel = true;
                final PacketPlayInBlockPlace packetPlayInBlockPlace = (PacketPlayInBlockPlace)packet;
                ((CraftPlayer)player).getHandle().playerConnection.sendPacket((Packet)new PacketPlayOutBlockChange((World)((CraftWorld)player.getWorld()).getHandle(), packetPlayInBlockPlace.a()));
                ((CraftPlayer)player).getHandle().playerConnection.sendPacket((Packet)new PacketPlayOutBlockChange((World)((CraftWorld)player.getWorld()).getHandle(), packetPlayInBlockPlace.a().shift(EnumDirection.fromType1(packetPlayInBlockPlace.getFace()))));
            }
        }
        else if (packet instanceof PacketPlayInBlockDig && data.getBackTicks() > 0) {
            cancel = true;
            final PacketPlayInBlockDig packetPlayInBlockDig = (PacketPlayInBlockDig)packet;
            ((CraftPlayer)player).getHandle().playerConnection.sendPacket((Packet)new PacketPlayOutBlockChange((World)((CraftWorld)player.getWorld()).getHandle(), packetPlayInBlockDig.a()));
        }
        return cancel;
    }
    
    static {
        BadPacketsF.crasher = false;
        BadPacketsF.health = 1.0;
    }
}
