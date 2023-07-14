// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.checks.movement.fly;

import gg.noob.plunder.data.PlayerData;
import org.bukkit.GameMode;
import gg.noob.plunder.util.PlayerUtils;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import gg.noob.plunder.checks.Check;

public class FlyH extends Check
{
    private double lDeltaY;
    private double slimeY;
    
    public FlyH() {
        super("Fly (H)");
        this.lDeltaY = 0.0;
        this.slimeY = 0.0;
    }
    
    @Override
    public void handleLocationUpdate(final Player player, final Location to, final Location from, final PacketPlayInFlying packetPlayInFlying, final boolean lastGround) {
        final PlayerData data = this.getPlayerData();
        final double deltaXZ = Math.hypot(to.getX() - from.getX(), to.getZ() - from.getZ());
        final double deltaY = to.getY() - from.getY();
        double max = Math.max((packetPlayInFlying.f() && data.blockBelow) ? 0.6001 : 0.5001, (data.isTakingVelocity(20) ? Math.max(data.getLastVelocity().getY(), PlayerUtils.getJumpHeight(player)) : PlayerUtils.getJumpHeight(player)) + 0.001);
        if (data.getHalfBlockTicks() > 0) {
            max = Math.max(0.5625, max);
        }
        if (data.getAboveSlimeTicks() > 0 && packetPlayInFlying.f()) {
            this.slimeY = PlayerUtils.getTotalHeight((float)Math.abs(this.lDeltaY));
            max = Math.max(max, this.slimeY);
        }
        else if (data.getAboveSlimeTicks() > 0 && data.getAirTicks() > 2) {
            this.slimeY -= 0.07999999821186066;
            this.slimeY *= 0.9800000190734863;
            max = Math.max(max, this.slimeY);
        }
        else if (data.getAboveSlimeTicks() <= 0 && this.slimeY != 0.0) {
            this.slimeY = 0.0;
        }
        if (deltaY > max && !data.isRoseBush() && !data.isTakingVelocity(2) && data.getAboveSlimeTicks() <= 0 && !data.isTeleporting(5) && player.getMaximumNoDamageTicks() >= 5 && data.getLiquidTicks() <= 0 && !player.getAllowFlight() && player.getGameMode() != GameMode.CREATIVE && data.getNearClimbTicks() <= 18 && !player.getAllowFlight() && !data.isBouncedOnSlime() && !player.isInsideVehicle() && data.getLiquidTicks() <= 0 && data.getOnWebTicks() <= 0 && !player.getAllowFlight()) {
            this.logCheat("Delta Y: " + deltaXZ + " Max: " + max);
            this.back();
        }
        this.lDeltaY = deltaY;
    }
}
