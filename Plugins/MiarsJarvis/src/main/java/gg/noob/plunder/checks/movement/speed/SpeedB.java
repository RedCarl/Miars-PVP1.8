// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.checks.movement.speed;

import gg.noob.plunder.data.PlayerData;
import gg.noob.plunder.util.PlayerUtils;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.entity.Player;
import gg.noob.plunder.checks.Check;

public class SpeedB extends Check
{
    private int count;
    
    public SpeedB() {
        super("Speed (B)");
        this.count = 0;
    }
    
    @Override
    public void handleReceivedPacket(final Player player, final Packet packet) {
        if (packet instanceof PacketPlayInFlying) {
            final PacketPlayInFlying packetPlayInFlying = (PacketPlayInFlying)packet;
            final PlayerData data = this.getPlayerData();
            if (data.isTakingVelocity() || data.isTeleporting() || data.getPistonTicks() > 0.0 || player.getAllowFlight() || PlayerUtils.hasInvalidJumpBoost(player) || player.getVehicle() != null || data.getNearClimbTicks() > 0 || data.getLiquidTicks() > 0 || data.getUnderBlockTicks() > 0) {
                return;
            }
            final double deltaXZ = data.getDeltaXZ();
            final int iceTicks = data.getOnIceTicks();
            final int collidedVTicks = data.getUnderBlockTicks();
            double limit = PlayerUtils.getBaseSpeed(data.getPlayer(), 0.34f);
            if (iceTicks < 40 || data.isBouncedOnSlime()) {
                limit += 0.34;
            }
            if (collidedVTicks < 40) {
                limit += 0.91;
            }
            final boolean invalid = deltaXZ > limit && data.getAirTicks() > 2;
            if (invalid) {
                ++this.count;
                if (this.count > 10) {
                    this.logCheat(player, "DeltaXZ: " + deltaXZ);
                    this.back();
                    this.count -= 2;
                    if (this.count < 0) {
                        this.count = 0;
                    }
                }
            }
            else {
                this.count -= 2;
                if (this.count < 0) {
                    this.count = 0;
                }
            }
        }
    }
}
