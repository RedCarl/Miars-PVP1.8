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

public class FlyN extends Check
{
    private double lDeltaY;
    
    public FlyN() {
        super("Fly (N)");
        this.lDeltaY = 0.0;
    }
    
    @Override
    public void handleLocationUpdate(final Player player, final Location to, final Location from, final PacketPlayInFlying packetPlayInFlying, final boolean lastGround) {
        final PlayerData data = this.getPlayerData();
        final boolean toGround = packetPlayInFlying.f() && data.isBlockBelow();
        final boolean fromGround = lastGround && data.isLastBlockBelow();
        final double deltaY = to.getY() - from.getY();
        double max = PlayerUtils.getJumpHeight(player);
        if (toGround) {
            if (!fromGround) {
                if (this.lDeltaY > 0.0 && data.getFenceTicks() > 16 && data.getUnderBlockTicks() > 18) {
                    max = 0.0;
                }
            }
            else if (data.getNearSlabTicks() > 0 || data.getStairsTicks() > 0.0) {
                max = 0.5;
            }
            else if (data.getHalfBlockTicks() > 0) {
                max = 0.5625;
            }
        }
        if (deltaY > max && !PlayerUtils.checkMovement(player) && !data.isDoingBlockUpdate() && !data.isTeleporting(5) && !player.getAllowFlight() && !data.isBouncedOnSlime() && !player.isInsideVehicle() && data.getLiquidTicks() <= 0 && !PlayerUtils.hasInvalidJumpBoost(player) && data.getOnWebTicks() <= 0 && data.getStairsTicks() <= 0.0 && player.getMaximumNoDamageTicks() >= 5 && data.getNearSlabTicks() <= 0 && data.getHalfBlockTicks() <= 0 && data.getNearClimbTicks() <= 0) {
            this.logCheat("Delta Y: " + deltaY + " Max: " + max);
            this.back();
        }
        this.lDeltaY = deltaY;
    }
}
