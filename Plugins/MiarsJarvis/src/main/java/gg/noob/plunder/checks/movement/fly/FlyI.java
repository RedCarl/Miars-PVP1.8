// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.checks.movement.fly;

import net.minecraft.server.v1_8_R3.PacketPlayInBlockPlace;
import net.minecraft.server.v1_8_R3.Packet;
import gg.noob.plunder.data.PlayerData;
import gg.noob.plunder.util.MathUtils;
import gg.noob.plunder.util.PlayerUtils;
import org.bukkit.potion.PotionEffectType;
import gg.noob.plunder.util.BlockUtils;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import gg.noob.plunder.checks.Check;

public class FlyI extends Check
{
    private int lastTPTick;
    private Double lastYDiff;
    private int lastBypassTick;
    private long lastBlockPlace;
    
    public FlyI() {
        super("Fly (I)");
        this.lastYDiff = null;
        this.lastBlockPlace = System.currentTimeMillis();
    }
    
    @Override
    public void handleLocationUpdate(final Player player, final Location location2, final Location location, final PacketPlayInFlying packetPlayInFlying) {
        final PlayerData data = this.getPlayerData();
        if (data.isTeleporting(3) || data.getLiquidTicks() > 0 || player.getMaximumNoDamageTicks() < 5) {
            this.lastYDiff = null;
            return;
        }
        if (!BlockUtils.isOnGround(location) || !packetPlayInFlying.f()) {
            final double d1 = location2.getY() - location.getY();
            final double d2 = d1 - 0.41999998688697815;
            if (this.lastYDiff != null && data.getTicks() - 40 > this.lastBypassTick && System.currentTimeMillis() - this.lastBlockPlace > 500L && !data.isFallFlying() && !data.isBouncedOnSlime() && !player.getAllowFlight() && !player.isDead() && !player.isInsideVehicle() && data.getTicks() - data.getLastVelocityTicks() > (2 + data.getMaxPingTicks()) * 2 && location2.getY() < location.getY() && Math.abs(d1 + 0.9800000190734863) > 1.0E-11 && Math.abs(d1 + 0.09800000190735147) > 1.0E-11 && Math.abs(d1 - 0.0030162615090425504) > 1.0E-9 && Math.abs(d1 / 0.9800000190734863 + 0.08) > 1.0E-11 && Math.abs(d2) > 9.999999960041972E-13 && Math.abs(d2 - PlayerUtils.getPotionEffectLevel(player, PotionEffectType.JUMP) * 0.1) > 1.0000000116860974E-7 && Math.abs(d1 + 0.15233518685055714) > 1.0E-11 && Math.abs(d1 + 0.07242780368044421) > 1.0E-11 && Math.max(location2.getY(), location.getY()) < 255.0) {
                final boolean b = location.getX() != location2.getX() && location.getZ() != location2.getZ();
                double d3 = (this.lastYDiff - 0.08) * 0.9800000190734863;
                if ((packetPlayInFlying.f() && d1 < 0.0 && d3 < d1 && MathUtils.onGround(location2.getY())) || (Math.pow(location.getX() - location2.getX(), 2.0) + Math.pow(location.getZ() - location2.getZ(), 2.0) < 0.0025 && player.hasPotionEffect(PotionEffectType.JUMP))) {
                    d3 = d1;
                }
                else if (!player.hasPotionEffect(PotionEffectType.JUMP) && Math.abs(d3) < 0.005) {
                    d3 = 0.0;
                }
                final double d4 = Math.abs(d3 - d1);
                final double d5 = (d3 - d1) / d3;
                final int i = data.getTicks();
                if (d4 > 2.0 && Math.abs(d5) > 300.0) {
                    if (i - this.lastTPTick > 20 || i - this.lastTPTick > 1) {}
                    this.logCheat(String.format("%s %s %.3f, %.3f%s", data.getLastTeleportTicks(), BlockUtils.isOnGround(location), d4, d5 * 100.0, "%"));
                    this.back();
                    this.lastTPTick = i;
                }
                if (data.isTeleporting(3) && !BlockUtils.isOnGround(location) && d4 > 1.0E-7) {
                    if (data.getStairsTicks() <= 0.0 && data.getNearSlabTicks() <= 0 && data.getLiquidTicks() <= 0 && !data.isBouncedOnSlime() && data.getNearClimbTicks() <= 0 && data.getOnWebTicks() <= 0 && data.getOnIceTicks() <= 0 && !BlockUtils.isNearFence(player) && data.getSolidBlockBehindTicks() <= 0) {
                        this.logCheat(String.format("D: %s D2: %s P: %s V: %s", d4, d1, location2.getY() % 1.0, b));
                    }
                    else {
                        this.lastBypassTick = i;
                    }
                }
            }
        }
        if (!packetPlayInFlying.f()) {
            this.lastYDiff = location2.getY() - location.getY();
        }
        else {
            this.lastYDiff = null;
        }
    }
    
    @Override
    public void handleReceivedPacket(final Player player, final Packet packet) {
        if (packet instanceof PacketPlayInBlockPlace && ((PacketPlayInBlockPlace)packet).getFace() != 255) {
            this.lastBlockPlace = System.currentTimeMillis();
        }
    }
}
