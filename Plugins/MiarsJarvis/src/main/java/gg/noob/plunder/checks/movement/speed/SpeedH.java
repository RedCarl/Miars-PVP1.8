// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.checks.movement.speed;

import gg.noob.plunder.data.PlayerData;
import gg.noob.plunder.util.MathUtils;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import gg.noob.plunder.checks.Check;

public class SpeedH extends Check
{
    private double lastDeltaX;
    private double lastDeltaZ;
    private Vector velocity;
    private boolean checked;
    private double buffer;
    
    public SpeedH() {
        super("Speed (H)");
        this.lastDeltaX = 0.0;
        this.lastDeltaZ = 0.0;
        this.velocity = null;
        this.checked = false;
        this.buffer = 0.0;
        this.setMaxViolations(6);
        this.setViolationsRemoveOneTime(1000L);
    }
    
    @Override
    public void handleTransaction(final Player player, final Vector velocity) {
        this.velocity = velocity;
    }
    
    @Override
    public void handleLocationUpdate(final Player player, final Location to, final Location from, final PacketPlayInFlying packetPlayInFlying) {
        final PlayerData data = this.getPlayerData();
        final double deltaX = to.getX() - from.getX();
        final double deltaY = to.getY() - from.getY();
        final double deltaZ = to.getZ() - from.getZ();
        if (data.isTeleporting(10) || System.currentTimeMillis() - data.getLastBypass() < 1000L || player.getVehicle() != null || player.getAllowFlight() || data.getTicks() - data.getLastNoDamageVelocityTicks() < 10 || data.getSolidBlockBehindTicks() > 0 || data.getUnderBlockTicks() > 0 || player.getMaximumNoDamageTicks() < 5) {
            this.lastDeltaX = deltaX;
            this.lastDeltaZ = deltaZ;
            return;
        }
        final boolean xChange = MathUtils.isOpposite(deltaX, this.lastDeltaX) && Math.abs(deltaX - this.lastDeltaX) > (this.checked ? 0.3 : 0.23);
        final boolean zChange = MathUtils.isOpposite(deltaZ, this.lastDeltaZ) && Math.abs(deltaZ - this.lastDeltaZ) > (this.checked ? 0.3 : 0.23);
        this.checked = false;
        if ((MathUtils.isOpposite(deltaX, this.lastDeltaX) || MathUtils.isOpposite(deltaZ, this.lastDeltaZ)) && this.velocity != null) {
            this.velocity = null;
            this.checked = true;
        }
        else if (data.getAirTicks() > 0 && (xChange || zChange) && Math.abs(deltaY) < 0.41999998688697815) {
            final double buffer = this.buffer + 1.0;
            this.buffer = buffer;
            if (buffer > 1.0) {
                this.logCheat("DeltaX: " + (float)deltaX + " Last DeltaX: " + (float)this.lastDeltaX + " DeltaZ: " + (float)deltaZ + " Last DeltaZ: " + (float)this.lastDeltaZ);
                this.back();
                this.buffer = 1.0;
            }
        }
        else {
            this.buffer -= Math.min(this.buffer, 0.001);
        }
        this.lastDeltaX = deltaX;
        this.lastDeltaZ = deltaZ;
    }
}
