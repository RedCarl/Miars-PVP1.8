// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.checks.other.scaffold;

import gg.noob.plunder.data.PlayerData;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.entity.Player;
import gg.noob.plunder.checks.Check;

public class ScaffoldH extends Check
{
    private double buffer;
    
    public ScaffoldH() {
        super("Scaffold (H)");
        this.buffer = 0.0;
        this.setMaxViolations(5);
    }
    
    @Override
    public void handleReceivedPacket(final Player player, final Packet packet) {
        final PlayerData data = this.getPlayerData();
        if (packet instanceof PacketPlayInFlying) {
            final PacketPlayInFlying packetPlayInFlying = (PacketPlayInFlying)packet;
            if (!packetPlayInFlying.g() || !packetPlayInFlying.h()) {
                return;
            }
            final double deltaXZ = data.getDeltaXZ();
            final double accelXZ = Math.abs(deltaXZ - data.getLastDeltaXZ());
            if (deltaXZ > 0.15 && accelXZ < 1.0E-5 && (data.getDeltaYaw() > 1.0f || data.getDeltaPitch() > 1.0f) && data.getGroundTicks() < 18 && data.isServerGround() && data.getKey().contains("S") && data.isPlacingBlocks()) {
                final double buffer = this.buffer + 1.0;
                this.buffer = buffer;
                if (buffer > 2.0) {
                    this.logCheat("deltaXZ=%s accelXZ=%s", deltaXZ, accelXZ);
                }
            }
            else if (this.buffer > 0.0) {
                this.buffer -= 0.025;
            }
        }
    }
}
