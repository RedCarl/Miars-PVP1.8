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

public class FlyK extends Check
{
    private double lastDeltaY;
    private long lastPos;
    private float buffer;
    private static double mult;
    
    public FlyK() {
        super("Fly (K)");
        this.lastDeltaY = 0.0;
    }
    
    @Override
    public void handleLocationUpdate(final Player player, final Location to, final Location from, final PacketPlayInFlying packetPlayInFlying, final boolean lastGround) {
        final PlayerData data = this.getPlayerData();
        final double deltaY = to.getY() - from.getY();
        final long timeStamp = System.currentTimeMillis();
        final double lDeltaY = lastGround ? 0.0 : this.lastDeltaY;
        final boolean onGround = packetPlayInFlying.f();
        double predicted = onGround ? lDeltaY : ((lDeltaY - 0.08) * FlyK.mult);
        if (lastGround && !onGround && deltaY > 0.0) {
            predicted = PlayerUtils.getJumpHeight(player);
        }
        if (Math.abs(predicted) < 0.005) {
            predicted = 0.0;
        }
        if (timeStamp - this.lastPos > 60L) {
            final double toCheck = (predicted - 0.08) * FlyK.mult;
            if (Math.abs(deltaY - toCheck) < Math.abs(deltaY - predicted)) {
                predicted = toCheck;
            }
        }
        final double deltaPredict = Math.abs(deltaY - predicted);
        if (data.getFenceTicks() <= 0 && !data.isTeleporting(3) && Plunder.getInstance().getLastGround().containsKey(player.getUniqueId()) && !player.getAllowFlight() && !data.isBouncedOnSlime() && !player.isInsideVehicle() && data.getLiquidTicks() <= 0 && data.getOnWebTicks() <= 0 && data.getStairsTicks() <= 0.0 && data.getNearSlabTicks() < 0 && data.getNearClimbTicks() <= 0 && !data.isPlacingBlocks() && !data.isTakingVelocity() && (data.getHalfBlockTicks() <= 0 || data.getAirTicks() > 4 || data.getGroundTicks() > 7) && data.getUnderBlockTicks() <= 0 && deltaPredict > 0.016) {
            final float buffer = this.buffer + 1.0f;
            this.buffer = buffer;
            if (buffer > 5.0f) {
                this.logCheat("Predicted: " + predicted);
                this.back();
                if (this.buffer > 6.0f) {
                    this.buffer = 6.0f;
                }
            }
        }
        else {
            this.buffer -= ((this.buffer > 0.0f) ? 0.25f : 0.0f);
        }
        this.lastPos = timeStamp;
        this.lastDeltaY = deltaY;
    }
    
    static {
        FlyK.mult = 0.9800000190734863;
    }
}
