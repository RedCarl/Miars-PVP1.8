// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.checks.movement.fly;

import gg.noob.plunder.data.PlayerData;
import gg.noob.plunder.Plunder;
import gg.noob.plunder.util.PlayerUtils;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import gg.noob.plunder.checks.Check;

public class FlyJ extends Check
{
    private double threshold;
    private double lastDeltaY;
    
    public FlyJ() {
        super("Fly (J)");
        this.threshold = 0.0;
        this.lastDeltaY = 0.0;
    }
    
    @Override
    public void handleLocationUpdate(final Player player, final Location to, final Location from, final PacketPlayInFlying packetPlayInFlying) {
        final PlayerData data = this.getPlayerData();
        final double deltaY = to.getY() - from.getY();
        if (packetPlayInFlying.f() || data.isPlacingBlocks() || data.isTeleporting() || PlayerUtils.checkMovement(player) || player.getAllowFlight() || data.isBouncedOnSlime() || player.isInsideVehicle() || data.getLiquidTicks() > 0 || data.getOnWebTicks() > 0 || data.getNearClimbTicks() > 0 || data.isTakingVelocity() || !Plunder.getInstance().getLastGround().containsKey(player.getUniqueId()) || this.checkConditions(data)) {
            this.threshold = 0.0;
            this.lastDeltaY = deltaY;
            return;
        }
        final double prediction = (this.lastDeltaY - 0.08) * 0.9800000190734863;
        double difference = Math.abs(deltaY - prediction);
        final double deltaX = Math.abs(to.getX() - from.getX());
        final double deltaZ = Math.abs(to.getZ() - from.getZ());
        final double deltaXZ = Math.hypot(deltaX, deltaZ);
        if (System.currentTimeMillis() - data.getLastBlockPlacedTicks() < 500L && ((deltaY >= 0.40400001406669617 && deltaY <= 0.4050000011920929) || (this.lastDeltaY >= 0.40400001406669617 && this.lastDeltaY <= 0.4050000011920929))) {
            difference = 0.0;
        }
        if (deltaXZ <= 0.005 && deltaY < 0.0 && deltaY > -0.08) {
            this.threshold -= Math.min(this.threshold, 1.0);
        }
        if (!data.isOnGround() && data.getLastGroundLoc() == null) {
            if (difference > 0.005) {
                final double threshold = this.threshold;
                this.threshold = threshold + 1.0;
                if (threshold > 3.0) {
                    this.logCheat();
                    this.back();
                }
            }
            else {
                this.threshold -= Math.min(this.threshold, 0.0010000000474974513);
            }
        }
        this.lastDeltaY = deltaY;
    }
    
    private boolean checkConditions(final PlayerData data) {
        return data.getLiquidTicks() > 0 || data.getTicks() < 60;
    }
}
