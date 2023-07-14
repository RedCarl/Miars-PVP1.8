// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.checks.combat.killaura;

import gg.noob.plunder.data.PlayerData;
import net.minecraft.server.v1_8_R3.PacketPlayInArmAnimation;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.entity.Player;
import gg.noob.plunder.checks.Check;

public class KillAuraF extends Check
{
    private double threshold;
    private double swings;
    private double attacks;
    
    public KillAuraF() {
        super("KillAura (F)");
        this.threshold = 0.0;
        this.setMaxViolations(5);
    }
    
    @Override
    public void handleReceivedPacket(final Player player, final Packet packet) {
        final PlayerData data = this.getPlayerData();
        if (packet instanceof PacketPlayInUseEntity) {
            if (((PacketPlayInUseEntity)packet).a() == PacketPlayInUseEntity.EnumEntityUseAction.ATTACK) {
                final double yawDiff = Math.abs(data.getLastMoveLoc().getYaw() - data.getLastLastMoveLoc().getYaw());
                if (yawDiff > 3.5 && yawDiff < 120.0 && data.getDeltaXZ() > 0.1) {
                    ++this.attacks;
                }
            }
        }
        else if (packet instanceof PacketPlayInArmAnimation) {
            if (this.swings > 100.0) {
                final double n = 0.0;
                this.attacks = n;
                this.swings = n;
            }
            ++this.swings;
            double ratio = this.attacks / this.swings * 100.0;
            if (ratio < 50.0) {
                ratio = 50.0;
            }
            if (ratio > 75.0 && this.attacks > 5.0 && this.swings > 5.0) {
                final double threshold = this.threshold + 1.0;
                this.threshold = threshold;
                if (threshold > 4.0) {
                    this.logCheat("Ratio: " + ratio);
                }
            }
            else {
                --this.threshold;
            }
        }
    }
}
