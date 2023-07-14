// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.checks.combat.aim;

import gg.noob.plunder.data.PlayerData;
import gg.noob.plunder.util.MathUtils;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.entity.Player;
import gg.noob.plunder.checks.Check;

public class AimH extends Check
{
    private double threshold;
    private double lastDeltaYaw;
    
    public AimH() {
        super("Aim (H)");
        this.setMaxViolations(3);
    }
    
    @Override
    public void handleReceivedPacket(final Player player, final Packet packet) {
        final PlayerData data = this.getPlayerData();
        if (packet instanceof PacketPlayInUseEntity) {
            if (data.isTeleporting(3)) {
                return;
            }
            final double yaw = MathUtils.wrapAngleTo180_float(data.getDeltaYaw());
            final double difference = Math.abs(yaw - this.lastDeltaYaw);
            if (difference == 0.0 && yaw > 2.0 && this.lastDeltaYaw > 2.0) {
                final double threshold = this.threshold;
                this.threshold = threshold + 1.0;
                if (threshold > 6.0) {
                    this.logCheat("Yaw: " + yaw + " Last Delta Yaw: " + this.lastDeltaYaw);
                }
            }
            else {
                this.threshold -= Math.min(this.threshold, 0.25);
            }
            this.lastDeltaYaw = yaw;
        }
    }
}
