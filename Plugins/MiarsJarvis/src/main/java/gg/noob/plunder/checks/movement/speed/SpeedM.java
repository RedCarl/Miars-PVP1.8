// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.checks.movement.speed;

import gg.noob.plunder.data.PlayerData;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import gg.noob.plunder.checks.Check;

public class SpeedM extends Check
{
    private double buffer;
    private int clientAirTicks;
    private double lastDeltaXZ;
    
    public SpeedM() {
        super("Speed (M)");
        this.buffer = 0.0;
        this.clientAirTicks = 0;
        this.lastDeltaXZ = 0.0;
    }
    
    @Override
    public void handleLocationUpdate(final Player player, final Location to, final Location from, final PacketPlayInFlying packetPlayInFlying) {
        final PlayerData data = this.getPlayerData();
        if (data.getSolidBlockBehindTicks() <= 0 || player.getAllowFlight() || data.isTeleporting() || System.currentTimeMillis() - data.getLastBypass() < 1000L || data.isTakingVelocity() || player.getVehicle() != null) {
            this.buffer = 0.0;
            return;
        }
        final int serverAirTicks = data.getAirTicks();
        final double deltaXZ = Math.hypot(to.getX() - from.getX(), to.getZ() - from.getZ());
        final float prediction = (float)this.lastDeltaXZ * 0.91f + 0.026f;
        final float diff = (float)deltaXZ - prediction;
        if (this.clientAirTicks > 1 && serverAirTicks > 0 && diff > 0.001) {
            final double buffer = this.buffer;
            this.buffer = buffer + 1.0;
            if (buffer > 1.0) {
                this.logCheat("Prediction: " + prediction + " DeltaXZ: " + deltaXZ + "LastDeltaXZ: " + this.lastDeltaXZ + " Diff: " + diff);
                this.back();
            }
        }
        else {
            this.buffer = Math.max(0.0, this.buffer - 0.05);
        }
        if (packetPlayInFlying.f()) {
            this.clientAirTicks = 0;
        }
        else {
            ++this.clientAirTicks;
        }
        this.lastDeltaXZ = deltaXZ;
    }
}
