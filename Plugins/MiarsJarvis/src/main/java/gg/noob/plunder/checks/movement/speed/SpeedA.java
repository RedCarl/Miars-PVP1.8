// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.checks.movement.speed;

import gg.noob.plunder.data.PlayerData;
import gg.noob.plunder.util.MathUtils;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import gg.noob.plunder.checks.Check;

public class SpeedA extends Check
{
    private double threshold;
    private double lastDeltaXZ;
    private long lastBack;
    private long lastStartBack;
    
    public SpeedA() {
        super("Speed (A)");
        this.threshold = 0.0;
        this.lastDeltaXZ = 0.0;
        this.lastBack = -1L;
        this.lastStartBack = -1L;
    }
    
    @Override
    public void handleLocationUpdate(final Player player, final Location to, final Location from, final PacketPlayInFlying packetPlayInFlying, final boolean lastGround) {
        final PlayerData data = this.getPlayerData();
        final double deltaXZ = Math.hypot(to.getX() - from.getX(), to.getZ() - from.getZ());
        if (player.isInsideVehicle() || data.isTeleporting() || data.isBouncedOnSlime() || player.getAllowFlight() || player.getWalkSpeed() > 0.2f || data.isTakingVelocity()) {
            this.lastDeltaXZ = deltaXZ;
            return;
        }
        double prediction = this.lastDeltaXZ * data.getFriction();
        final double deltaY = to.getY() - from.getY();
        if (!packetPlayInFlying.f() && lastGround && deltaY > 0.0) {
            prediction += 0.20000000298023224;
        }
        if (data.hasHitSlowdown()) {
            prediction += 0.010099999606609344;
        }
        if (data.getPistonTicks() > 0.0) {
            prediction += 0.5;
        }
        prediction += MathUtils.movingFlyingV3(data, false, to, from, packetPlayInFlying, lastGround, deltaXZ, 0.0, false);
        final double totalSpeed = deltaXZ - prediction;
        if (packetPlayInFlying.f() || lastGround) {
            if (totalSpeed > 0.5 && deltaXZ > 0.2) {
                final double threshold = this.threshold + 1.0;
                this.threshold = threshold;
                if (threshold > 1.0) {
                    this.logCheat("Total Speed: " + totalSpeed + " Delta Y: " + deltaY + " Ground: " + packetPlayInFlying.f() + " Last Ground: " + lastGround);
                    this.back();
                }
            }
            else {
                this.threshold -= Math.min(0.0, 1.0E-4);
            }
        }
        this.lastDeltaXZ = deltaXZ;
    }
}
