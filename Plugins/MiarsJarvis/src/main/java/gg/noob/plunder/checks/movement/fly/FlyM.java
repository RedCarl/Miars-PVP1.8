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

public class FlyM extends Check
{
    public FlyM() {
        super("Fly (M)");
    }
    
    @Override
    public void handleLocationUpdate(final Player player, final Location to, final Location from, final PacketPlayInFlying packetPlayInFlying, final boolean lastGround) {
        final PlayerData data = this.getPlayerData();
        final double deltaY = to.getY() - from.getY();
        final double jumpHeight = PlayerUtils.getJumpHeight(player);
        if (data.isJumped() && data.getAboveSlimeTicks() < 4 && !PlayerUtils.checkMovement(player) && !data.isDoingBlockUpdate() && lastGround && data.getUnderBlockTicks() < 4 && !data.isPlacingBlocks() && data.getHalfBlockTicks() < 4 && !data.isTakingVelocity(4) && !player.getAllowFlight() && !data.isBouncedOnSlime() && !player.isInsideVehicle() && data.getLiquidTicks() <= 0 && data.getOnWebTicks() <= 0 && data.getStairsTicks() <= 0.0 && data.getNearSlabTicks() <= 0 && data.getNearClimbTicks() <= 0 && Math.abs(deltaY - jumpHeight) > 0.009999999776482582) {
            this.logCheat("Delta Y: " + deltaY + " Jump Height: " + jumpHeight);
            this.back();
        }
    }
}
