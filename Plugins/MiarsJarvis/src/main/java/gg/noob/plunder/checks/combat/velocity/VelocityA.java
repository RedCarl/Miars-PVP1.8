// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.checks.combat.velocity;

import gg.noob.plunder.util.PlayerUtils;
import org.bukkit.Location;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import net.minecraft.server.v1_8_R3.Packet;
import gg.noob.plunder.data.PlayerData;
import org.bukkit.util.Vector;
import org.bukkit.entity.Player;
import gg.noob.plunder.checks.Check;

public class VelocityA extends Check
{
    private double vY;
    private double buffer;
    
    public VelocityA() {
        super("Velocity (A)");
        this.setMaxViolations(6);
    }
    
    @Override
    public void handleTransaction(final Player player, final Vector velocity) {
        final PlayerData data = this.getPlayerData();
        if (velocity.getY() > 0.1 && data.getTicks() - data.getLastBlockPlacedTicks() > 5L) {
            this.vY = velocity.getY();
        }
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
            final double deltaY = to.getY() - from.getY();
            if (this.vY > 0.0 && !data.isTeleporting(7) && !PlayerUtils.hasInvalidJumpBoost(player) && !player.getAllowFlight() && data.getOnWebTicks() <= 0 && data.getLiquidTicks() <= 0 && data.getNearClimbTicks() <= 0 && System.currentTimeMillis() - data.getLastBypass() > 1000L && data.getUnderBlockTicks() <= 0) {
                final double pct = deltaY / this.vY * 100.0;
                if ((pct < 99.999 || pct > 400.0) && data.getUnderBlockTicks() <= 0 && !data.isPlacingBlocks()) {
                    final double buffer = this.buffer + 1.0;
                    this.buffer = buffer;
                    if (buffer > 10.0) {
                        this.logCheat("Percentage: " + pct + " Buffer: " + this.buffer);
                        this.buffer = 5.0;
                    }
                }
                else {
                    this.buffer -= ((this.buffer > 0.0) ? 0.25 : 0.0);
                }
                this.vY -= 0.08;
                this.vY *= 0.98;
                if (this.vY < 0.005 || !data.isTakingVelocity(7)) {
                    this.vY = 0.0;
                }
            }
        }
    }
}
