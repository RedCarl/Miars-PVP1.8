// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.checks.movement.speed;

import gg.noob.plunder.data.PlayerData;
import gg.noob.plunder.util.BlockUtils;
import gg.noob.plunder.util.PlayerUtils;
import gg.noob.plunder.util.MathUtils;
import org.bukkit.Location;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import gg.noob.plunder.Plunder;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.entity.Player;
import gg.noob.plunder.checks.Check;

public class SpeedG extends Check
{
    private int threshold;
    private Double lastAngle;
    
    public SpeedG() {
        super("Speed (G)");
        this.threshold = 0;
        this.lastAngle = null;
    }
    
    @Override
    public void handleReceivedPacket(final Player player, final Packet packet) {
        final PlayerData data = Plunder.getInstance().getDataManager().getPlayerData(player);
        if (data == null) {
            return;
        }
        if (packet instanceof PacketPlayInFlying) {
            final PacketPlayInFlying packetPlayInFlying = (PacketPlayInFlying)packet;
            final Location p0 = player.getLocation();
            final Location p2 = new Location(player.getWorld(), packetPlayInFlying.a(), packetPlayInFlying.b(), packetPlayInFlying.c(), packetPlayInFlying.d(), packetPlayInFlying.e());
            if (data.isSprint() && packetPlayInFlying.f() && player.isOnGround()) {
                final double angle = Math.toDegrees(-Math.atan2(p2.getX() - p0.getX(), p2.getZ() - p0.getZ()));
                final double angleDiff = Math.min(MathUtils.getDistanceBetweenAngles360(angle, p2.getYaw()), MathUtils.getDistanceBetweenAngles360(angle, p0.getYaw()));
                if (this.lastAngle != null) {
                    final double lastAngleDiff = MathUtils.getDistanceBetweenAngles360(this.lastAngle, angleDiff);
                    if (angleDiff > 47.5) {
                        if (lastAngleDiff < 5.0 && this.threshold++ > 5) {
                            if (!PlayerUtils.hasInvalidJumpBoost(player) && data.getNearSlabTicks() <= 0 && data.getNearClimbTicks() <= 0 && data.getUnderBlockTicks() <= 0 && data.getLiquidTicks() <= 0 && data.getStairsTicks() <= 0.0 && data.getSolidBlockBehindTicks() <= 0 && data.getOnIceTicks() <= 0 && !BlockUtils.isNearFence(player.getLocation()) && data.getPistonTicks() <= 0.0 && !data.isBouncedOnSlime() && data.getOnWebTicks() <= 0) {
                                this.logCheat(player, String.format("D: %s", angleDiff));
                                this.back();
                            }
                            else {
                                this.threshold = -20;
                            }
                            this.threshold = 0;
                        }
                    }
                    else {
                        this.threshold = 0;
                    }
                }
                this.lastAngle = angleDiff;
            }
            else {
                this.lastAngle = null;
                this.threshold = 0;
            }
        }
    }
}
