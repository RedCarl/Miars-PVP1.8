// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.checks.movement.fly;

import gg.noob.plunder.data.PlayerData;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import gg.noob.plunder.checks.Check;

public class FlyA extends Check
{
    private double lastYDiff;
    
    public FlyA() {
        super("Fly (A)");
        this.lastYDiff = 0.0;
    }
    
    @Override
    public void handleLocationUpdate(final Player player, final Location to, final Location from, final PacketPlayInFlying packetPlayInFlying, final boolean lastGround) {
        final PlayerData data = this.getPlayerData();
        final double y = from.getY() - to.getY();
        if (lastGround && from.getY() % 0.5 * 16.0 % 1.0 != 0.0 && from.getY() % 0.5 * 64.0 != 1.0 && from.getY() % 0.5 != 0.0 && to.getY() % 1.0 != 0.41999998688697815 && to.getY() % 1.0 != 0.0 && Math.abs(from.getY() % 0.5 - 0.015555072702202466) > 1.0E-12 && from.getY() % 1.0 != 0.09375 && !data.isTeleporting(3) && !data.isTakingVelocity() && !player.isInsideVehicle() && !player.getAllowFlight() && data.getNearClimbTicks() <= 0 && data.getAboveSlimeTicks() <= 0 && !data.isBouncedOnSlime() && !data.isPlacingBlocks() && !packetPlayInFlying.f() && y < 0.078 && this.lastYDiff > 0.08) {
            this.logCheat(String.format("L: %.3f N: %.3f", this.lastYDiff, y));
        }
        this.lastYDiff = y;
    }
}
