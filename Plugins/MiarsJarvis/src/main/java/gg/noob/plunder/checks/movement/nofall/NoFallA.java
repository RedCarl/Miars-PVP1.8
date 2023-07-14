// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.checks.movement.nofall;

import gg.noob.plunder.data.PlayerData;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import gg.noob.plunder.checks.Check;

public class NoFallA extends Check
{
    private float buffer;
    private long lastStartBack;
    private static double divisor;
    private double lastDeltaY;
    
    public NoFallA() {
        super("NoFall (A)");
        this.buffer = 0.0f;
        this.lastStartBack = -1L;
        this.lastDeltaY = 0.0;
        this.setViolationsRemoveOneTime(1000L);
    }
    
    @Override
    public void handleLocationUpdate(final Player player, final Location to, final Location from, final PacketPlayInFlying packetPlayInFlying) {
        final PlayerData data = this.getPlayerData();
        final double deltaY = to.getY() - from.getY();
        final double deltaXZ = Math.hypot(to.getX() - from.getX(), to.getZ() - from.getZ());
        if ((deltaXZ == 0.0 && deltaY == 0.0) || data.getLiquidTicks() > 0 || player.getAllowFlight() || data.getNearSlabTicks() > 0 || data.getStairsTicks() > 0.0 || data.getAboveSlimeTicks() > 0 || data.getTicks() - data.getLastBypass() < 500L || data.isTeleporting(1)) {
            if (this.buffer > 0.0f) {
                this.buffer -= 0.5f;
            }
            return;
        }
        final boolean onGround = packetPlayInFlying.f();
        boolean flag;
        if (onGround) {
            flag = (Math.abs(deltaY) > 0.0051 && !data.isBouncedOnSlime() && data.getUnderBlockTicks() <= 0 && !player.isOnGround() && ((deltaY >= 0.0 && (Math.abs(to.getY()) % NoFallA.divisor != 0.0 || Math.abs(deltaY) % NoFallA.divisor != 0.0)) || deltaY <= this.lastDeltaY));
        }
        else {
            flag = (deltaY == 0.0 && this.lastDeltaY == 0.0 && data.getNearClimbTicks() <= 0 && !data.isBouncedOnSlime());
        }
        if (flag) {
            final float buffer = this.buffer + 1.0f;
            this.buffer = buffer;
            if (buffer > 1.0f) {
                this.logCheat(String.format("Ground: %s Delta Y: %.4f Last Delta Y: %.4f", onGround, deltaY, this.lastDeltaY));
                this.back();
            }
        }
        else if (this.buffer > 0.0f) {
            this.buffer -= 0.25f;
        }
        this.lastDeltaY = deltaY;
    }
    
    static {
        NoFallA.divisor = 0.015625;
    }
}
