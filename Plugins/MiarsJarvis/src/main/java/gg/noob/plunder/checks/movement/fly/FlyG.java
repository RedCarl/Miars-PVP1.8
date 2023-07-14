// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.checks.movement.fly;

import gg.noob.plunder.data.PlayerData;
import gg.noob.plunder.util.MathUtils;
import net.minecraft.server.v1_8_R3.MathHelper;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import gg.noob.plunder.checks.Check;

public class FlyG extends Check
{
    private int lastBypass;
    private int ticks;
    private final Vector lastMovement;
    private static final float VALUE = 0.017453292f;
    
    public FlyG() {
        super("Fly (G)");
        this.lastBypass = 0;
        this.ticks = 0;
        this.lastMovement = new Vector();
    }
    
    @Override
    public void handleLocationUpdate(final Player player, final Location location2, final Location location, final PacketPlayInFlying packetPlayInFlying) {
        final PlayerData data = this.getPlayerData();
        if (!data.isFallFlying() || player.getAllowFlight() || packetPlayInFlying.f() || data.isTakingVelocity() || data.isServerGround() || data.getTicks() - this.lastBypass <= 20) {
            this.ticks = 0;
        }
        else {
            if (this.ticks++ > 9) {
                final float pitch = location2.getPitch();
                final float yaw = location2.getYaw();
                final float mPitch = pitch * 0.017453292f;
                final double deltaY = location2.getY() - location.getY();
                final double lastMoveY = this.lastMovement.getY();
                final double lastMoveLen = this.lastMovement.length();
                final Vector rotateVector = getVectorForRotation(pitch, yaw);
                final double vectorLen = rotateVector.length();
                final double d = Math.sqrt(Math.pow(rotateVector.getX(), 2.0) + Math.pow(rotateVector.getZ(), 2.0));
                final float f = (float)(Math.pow(MathHelper.cos(mPitch), 2.0) * Math.min(1.0, vectorLen / 0.4));
                double d2 = deltaY / 0.9900000095367432;
                final double d3 = (lastMoveY - 0.08) * 0.9800000190734863;
                if (mPitch < 0.0f) {
                    d2 -= lastMoveLen * -MathHelper.sin(mPitch) * 0.04 * 3.2;
                }
                if (lastMoveY < 0.0 && d > 0.0) {
                    d2 -= lastMoveY * -0.1 * f;
                }
                d2 -= -0.08 + f * 0.06;
                final double lastMoveLow = MathUtils.lowestAbs(lastMoveY - d2, deltaY - d3);
                final double lowest = MathUtils.lowestAbs((d2 - lastMoveY) / d2, (deltaY - d3) / d3);
                if (Math.abs(lastMoveLow) > 0.025 && Math.abs(lowest) > 0.075) {
                    final int totalTicks = data.getTicks();
                    if (totalTicks - this.lastBypass >= 20) {
                        if (data.getStairsTicks() <= 0.0 && data.getNearSlabTicks() <= 0 && data.getLiquidTicks() <= 0 && !data.isBouncedOnSlime() && data.getNearClimbTicks() <= 0 && data.getOnWebTicks() <= 0 && data.getOnIceTicks() <= 0 && data.getFenceTicks() <= 0) {
                            this.logCheat(String.format("%.2f %.2f", deltaY, lastMoveLow));
                            this.back();
                        }
                        else {
                            this.lastBypass = totalTicks;
                            this.ticks = 0;
                        }
                    }
                }
            }
            this.lastMovement.setX(location2.getX() - location.getX());
            this.lastMovement.setY(location2.getY() - location.getY());
            this.lastMovement.setZ(location2.getZ() - location.getZ());
        }
    }
    
    private static Vector getVectorForRotation(final float f, final float f1) {
        final float location2 = MathHelper.cos(-f1 * 0.017453292f - 3.1415927f);
        final float var3 = MathHelper.sin(-f1 * 0.017453292f - 3.1415927f);
        final float var4 = -MathHelper.cos(-f * 0.017453292f);
        final float pitch = MathHelper.sin(-f * 0.017453292f);
        return new Vector(var3 * var4, pitch, location2 * var4);
    }
}
