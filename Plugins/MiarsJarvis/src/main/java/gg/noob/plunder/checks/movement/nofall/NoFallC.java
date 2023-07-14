// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.checks.movement.nofall;

import gg.noob.plunder.data.PlayerData;
import gg.noob.plunder.util.PlayerUtils;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.entity.Player;
import gg.noob.plunder.checks.Check;

public class NoFallC extends Check
{
    private int threshold;
    
    public NoFallC() {
        super("No Fall (C)");
        this.threshold = 0;
    }
    
    @Override
    public void handleReceivedPacket(final Player player, final Packet packet) {
        if (packet instanceof PacketPlayInFlying) {
            final PacketPlayInFlying packetPlayInFlying = (PacketPlayInFlying)packet;
            final PlayerData data = this.getPlayerData();
            if (data.isTeleporting(3) || data.isBouncedOnSlime() || player.isInsideVehicle() || data.isPlacingBlocks() || PlayerUtils.checkMovement(player) || data.isDoingBlockUpdate() || data.getHalfBlockTicks() > 0 || data.getStairsTicks() > 0.0 || data.getNearSlabTicks() > 0 || data.getOnWebTicks() > 0 || (data.isTakingVelocity(10) && data.getTicks() - data.getLastFallDamageTicks() < 20) || data.getLiquidTicks() > 0) {
                return;
            }
            final double deltaY = data.getDeltaY();
            final boolean isGround = data.getLastGroundLoc() != null;
            if (packetPlayInFlying.b() % 0.015625 != 0.0 && packetPlayInFlying.b() % 0.015625 > 0.009) {
                if (isGround && (deltaY < 0.0 || deltaY >= 0.0) && !data.isServerGround()) {
                    if (this.threshold++ > 3) {
                        this.logCheat();
                        this.back();
                    }
                }
                else {
                    this.threshold -= (int)Math.min(this.threshold, 1.0E-5);
                }
            }
        }
    }
}
