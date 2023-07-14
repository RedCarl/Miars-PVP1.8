// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.checks.combat.killaura;

import net.minecraft.server.v1_8_R3.Entity;
import gg.noob.plunder.data.PlayerData;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;
import org.bukkit.entity.EntityType;
import org.bukkit.Location;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.entity.Player;
import gg.noob.plunder.checks.Check;

public class KillAuraK extends Check
{
    private int buffer;
    private boolean attack;
    private double lastDeltaX;
    private double lastDeltaZ;
    
    public KillAuraK() {
        super("KillAura (K)");
        this.buffer = 0;
        this.attack = false;
        this.lastDeltaX = 0.0;
        this.lastDeltaZ = 0.0;
        this.setMaxViolations(5);
    }
    
    @Override
    public void handleReceivedPacket(final Player player, final Packet packet) {
        if (packet instanceof PacketPlayInFlying) {
            final PacketPlayInFlying packetPlayInFlying = (PacketPlayInFlying)packet;
            final PlayerData data = this.getPlayerData();
            final Location to = new Location(player.getWorld(), packetPlayInFlying.a(), packetPlayInFlying.b(), packetPlayInFlying.c(), packetPlayInFlying.d(), packetPlayInFlying.e());
            final Location from = new Location(player.getWorld(), data.lastPosX, data.lastPosY, data.lastPosZ, data.lastYaw, data.lastPitch);
            if (!packetPlayInFlying.g() || packetPlayInFlying.b() == -999.0) {
                to.setX(from.getX());
                to.setY(from.getY());
                to.setZ(from.getZ());
            }
            if (!packetPlayInFlying.h()) {
                to.setYaw(from.getYaw());
                to.setPitch(from.getPitch());
            }
            final double deltaX = to.getX() - from.getX();
            final double deltaZ = to.getZ() - from.getZ();
            if (this.attack && data.isSprint()) {
                double px = this.lastDeltaX;
                double pz = this.lastDeltaZ;
                px *= 0.6;
                pz *= 0.6;
                final double pxz = px * px + pz * pz;
                final double noxz = Math.pow(Math.hypot(this.lastDeltaX, this.lastDeltaZ), 2.0);
                final double deltaXZ = Math.pow(Math.hypot(deltaX, deltaZ), 2.0);
                final double deltaYes = Math.abs(deltaXZ - pxz);
                final double deltaNo = Math.abs(deltaXZ - noxz);
                if (deltaYes > 0.049 && deltaNo < 1.0E-4 && data.target != null && data.target.getType().equals((Object)EntityType.PLAYER)) {
                    if (++this.buffer > 5) {
                        this.logCheat(String.format("Delta Yes: %.3f Delta No: %.3f Delta XZ: %.2f No XZ: %.2f", deltaYes, deltaNo, data.deltaXZ, noxz));
                    }
                }
                else if (this.buffer > 0) {
                    this.buffer -= (int)0.1;
                }
            }
            this.attack = false;
            this.lastDeltaX = deltaX;
            this.lastDeltaZ = deltaZ;
        }
        else if (packet instanceof PacketPlayInUseEntity) {
            final PacketPlayInUseEntity packetPlayInUseEntity = (PacketPlayInUseEntity)packet;
            final Entity entity = packetPlayInUseEntity.a((World)((CraftWorld)player.getWorld()).getHandle());
            if (packetPlayInUseEntity.a() == PacketPlayInUseEntity.EnumEntityUseAction.ATTACK && entity instanceof EntityPlayer) {
                this.attack = true;
            }
        }
    }
}
