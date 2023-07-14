// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.checks.other.scaffold;

import gg.noob.plunder.data.PlayerData;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.entity.Player;
import gg.noob.plunder.checks.Check;

public class ScaffoldI extends Check
{
    private double buffer;
    
    public ScaffoldI() {
        super("Scaffold (I)");
        this.buffer = 0.0;
        this.setMaxViolations(5);
    }
    
    @Override
    public void handleReceivedPacket(final Player player, final Packet packet) {
        final PlayerData data = this.getPlayerData();
        if (packet instanceof PacketPlayInFlying) {
            final PacketPlayInFlying packetPlayInFlying = (PacketPlayInFlying)packet;
            if (!packetPlayInFlying.g()) {
                return;
            }
            final double deltaXZ = data.getDeltaXZ();
            final double lDeltaXZ = data.getLastDeltaXZ();
            final double accelXZ = Math.abs(deltaXZ - lDeltaXZ);
            if (deltaXZ < 0.15 && deltaXZ > 0.1 && lDeltaXZ > 0.15 && accelXZ < 0.1 && accelXZ > 0.099 && data.getGroundTicks() < 18 && data.isServerGround() && data.isPlacingBlocks()) {
                final double buffer = this.buffer + 1.0;
                this.buffer = buffer;
                if (buffer > 2.0) {
                    this.logCheat("DeltaXZ=%s lDeltaXZ=%s accel=%s", deltaXZ, lDeltaXZ, accelXZ);
                }
            }
            else if (this.buffer > 0.0) {
                this.buffer -= 0.1;
            }
        }
    }
}
