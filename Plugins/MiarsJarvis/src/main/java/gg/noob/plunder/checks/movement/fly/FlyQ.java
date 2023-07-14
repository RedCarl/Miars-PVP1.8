// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.checks.movement.fly;

import gg.noob.plunder.data.PlayerData;
import gg.noob.plunder.util.PlayerUtils;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import gg.noob.plunder.checks.Check;

public class FlyQ extends Check
{
    private double slimeY;
    private double lastDeltaY;
    
    public FlyQ() {
        super("Fly (Q)");
        this.slimeY = 0.0;
        this.lastDeltaY = 0.0;
    }
    
    @Override
    public boolean handleLocationUpdateCanceled(final Player player, final Location to, final Location from, final PacketPlayInFlying packetPlayInFlying, final boolean lastGround) {
        final PlayerData data = this.getPlayerData();
        boolean cancel = false;
        final double deltaXZ = Math.hypot(to.getX() - from.getX(), to.getZ() - from.getZ());
        final double deltaY = to.getY() - from.getY();
        final double jumpHeight = PlayerUtils.getJumpHeight(player);
        double max = Math.max((packetPlayInFlying.f() && data.isServerGround()) ? 0.6001 : 0.5001, (data.isTakingVelocity(20) ? Math.max(data.getLastVelocity().getY(), jumpHeight) : jumpHeight) + 0.001);
        if (data.getHalfBlockTicks() > 0) {
            max = Math.max(0.5625, max);
        }
        if (data.getAboveSlimeTicks() > 0 && packetPlayInFlying.f() && data.isNearGround()) {
            this.slimeY = PlayerUtils.getTotalHeight((float)Math.abs(this.lastDeltaY));
            max = Math.max(max, this.slimeY);
        }
        else if (data.getAboveSlimeTicks() > 0 && data.airTicks > 2) {
            this.slimeY -= 0.07999999821186066;
            this.slimeY *= 0.9800000190734863;
            max = Math.max(max, this.slimeY);
        }
        else if (data.getAboveSlimeTicks() <= 0 && this.slimeY != 0.0) {
            this.slimeY = 0.0;
        }
        if (deltaY > max && player.getMaximumNoDamageTicks() > 5 && !data.isRoseBush() && !data.isTakingVelocity(2) && !data.isPlacingBlocks() && !player.getAllowFlight() && !data.isBouncedOnSlime() && !player.isInsideVehicle()) {
            if (!data.isTeleporting(3)) {
                this.logCheat("Delta Y: " + deltaY + " Max: " + max);
            }
            this.back();
            cancel = true;
        }
        this.lastDeltaY = deltaY;
        return cancel;
    }
}
