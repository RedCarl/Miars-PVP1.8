// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.checks.movement.speed;

import gg.noob.plunder.data.PlayerData;
import gg.noob.plunder.util.PlayerUtils;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import gg.noob.plunder.checks.Check;

public class SpeedI extends Check
{
    private double buffer;
    private double lastDeltaXZ;
    private double lastDeltaY;
    
    public SpeedI() {
        super("Speed (I)");
        this.buffer = 0.0;
        this.lastDeltaXZ = 0.0;
        this.lastDeltaY = 0.0;
        this.setViolationsRemoveOneTime(1000L);
    }
    
    @Override
    public void handleLocationUpdate(final Player player, final Location to, final Location from, final PacketPlayInFlying packetPlayInFlying) {
        final PlayerData data = this.getPlayerData();
        if (data.getUnderBlockTicks() > 0 || data.isBouncedOnSlime() || data.isTeleporting(3) || player.getVehicle() != null || data.isPlacingBlocks() || PlayerUtils.hasInvalidJumpBoost(player) || data.isTakingVelocity()) {
            this.buffer = 0.0;
            return;
        }
        final double deltaX = to.getX() - from.getX();
        final double deltaY = to.getY() - from.getY();
        final double deltaZ = to.getZ() - from.getZ();
        final float deltaXZ = (float)Math.hypot(deltaX, deltaZ);
        if (deltaXZ > 0.15 && this.lastDeltaXZ > 0.15 && ((deltaY > 0.0 && this.lastDeltaY < 0.0) || (deltaY < 0.0 && this.lastDeltaY > 0.0))) {
            final double buffer = this.buffer;
            this.buffer = buffer + 1.0;
            if (buffer > 3.0) {
                this.logCheat("DeltaY: " + deltaY + " Last DeltaY: " + this.lastDeltaY);
                this.back();
                this.buffer = 4.0;
            }
        }
        else {
            this.buffer = Math.max(0.0, this.buffer - 0.2);
        }
        this.lastDeltaXZ = deltaXZ;
        this.lastDeltaY = deltaY;
    }
}
