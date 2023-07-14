// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.checks.movement.speed;

import gg.noob.plunder.data.PlayerData;
import gg.noob.plunder.util.MathUtils;
import org.bukkit.GameMode;
import gg.noob.plunder.util.PlayerUtils;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import gg.noob.plunder.checks.Check;

public class SpeedN extends Check
{
    private int verbose;
    
    public SpeedN() {
        super("Speed (N)");
        this.verbose = 0;
    }
    
    @Override
    public void handleLocationUpdate(final Player player, final Location to, final Location from, final PacketPlayInFlying packetPlayInFlying, final boolean lastGround) {
        final PlayerData data = this.getPlayerData();
        final double deltaXZ = Math.hypot(to.getX() - from.getX(), to.getZ() - from.getZ());
        if (data.isTeleporting() || data.isTakingVelocity(25) || data.getLiquidTicks() > 0 || player.getAllowFlight() || PlayerUtils.hasInvalidJumpBoost(player) || player.getGameMode() == GameMode.CREATIVE || data.getStairsTicks() > 0.0 || data.getNearSlabTicks() > 0 || player.getAllowFlight() || data.isBouncedOnSlime() || player.isInsideVehicle() || data.getLiquidTicks() > 0 || data.getOnWebTicks() > 0 || data.getFlyingTicks() > 0) {
            --this.verbose;
            if (this.verbose < 0) {
                this.verbose = 0;
            }
            return;
        }
        double baseSpeed = PlayerUtils.getBaseSpeed(data) + (packetPlayInFlying.f() ? ((data.getGroundTicks() > 10) ? 0.02 : 0.03) : 0.05999999865889549);
        baseSpeed += ((data.getOnIceTicks() > 0) ? (0.4 + Math.min(60, 60 - data.getOnIceTicks()) * 0.015) : 0.0);
        baseSpeed += ((data.getUnderBlockTicks() > 0) ? (0.35 + (20 - data.getUnderBlockTicks()) * 0.005) : 0.0);
        baseSpeed += ((data.getHalfBlockTicks() > 0) ? (0.2 + (20 - data.getHalfBlockTicks()) * 0.005) : 0.0);
        baseSpeed += ((data.getAboveSlimeTicks() > 0) ? 0.1 : 0.0);
        if (data.isPlacingBlocks()) {
            baseSpeed += 0.2;
        }
        if (PlayerUtils.getBaseSpeed(data) < 0.2) {
            return;
        }
        if (deltaXZ > baseSpeed) {
            this.verbose += ((deltaXZ - baseSpeed > 0.44999998807907104) ? 4 : 1);
            if (this.verbose > 25 || deltaXZ - baseSpeed > 0.44999998807907104) {
                this.logCheat("Delta XZ: " + MathUtils.round(deltaXZ, 3) + " Base Speed: " + MathUtils.round(baseSpeed, 3));
                this.back();
            }
        }
        else {
            --this.verbose;
            if (this.verbose < 0) {
                this.verbose = 0;
            }
        }
    }
}
