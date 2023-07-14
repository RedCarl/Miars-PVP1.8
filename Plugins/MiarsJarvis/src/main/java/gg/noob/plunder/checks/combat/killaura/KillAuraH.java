// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.checks.combat.killaura;

import gg.noob.plunder.data.PlayerData;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.entity.Player;
import gg.noob.plunder.checks.Check;

public class KillAuraH extends Check
{
    private double lastYawDiff;
    private double lastDeltaXZ;
    private double threshold;
    
    public KillAuraH() {
        super("KillAura (H)");
        this.setMaxViolations(5);
    }
    
    @Override
    public void handleReceivedPacket(final Player player, final Packet packet) {
        final PlayerData data = this.getPlayerData();
        if (packet instanceof PacketPlayInUseEntity) {
            if (((PacketPlayInUseEntity)packet).a() == PacketPlayInUseEntity.EnumEntityUseAction.ATTACK) {
                final double yaw = data.getLastMoveLoc().getYaw() - data.getLastLastMoveLoc().getYaw();
                final double lastYaw = Math.abs(yaw - this.lastYawDiff);
                final double deltaXZ = data.getDeltaXZ();
                final double difference = Math.abs(deltaXZ - this.lastDeltaXZ);
                if (difference > 0.0 && difference < 0.001 && lastYaw > 7.0) {
                    final double threshold = this.threshold + 1.0;
                    this.threshold = threshold;
                    if (threshold > 2.0) {
                        this.logCheat("Difference: " + difference + " Last Yaw: " + lastYaw);
                        this.threshold = 0.0;
                    }
                }
                else {
                    this.threshold -= Math.min(this.threshold, 0.25);
                }
                this.lastDeltaXZ = deltaXZ;
                this.lastYawDiff = yaw;
            }
        }
        else if (packet instanceof PacketPlayInFlying && System.currentTimeMillis() - data.getLastAttackTime() > 100L) {
            this.threshold = 0.0;
        }
    }
}
