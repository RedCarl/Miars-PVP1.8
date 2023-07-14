// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.checks.combat.killaura;

import gg.noob.plunder.data.PlayerData;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.entity.Player;
import gg.noob.plunder.checks.Check;

public class KillAuraL extends Check
{
    private double threshold;
    
    public KillAuraL() {
        super("KillAura (L)");
        this.threshold = 0.0;
        this.setMaxViolations(5);
    }
    
    @Override
    public void handleReceivedPacket(final Player player, final Packet packet) {
        final PlayerData data = this.getPlayerData();
        if (packet instanceof PacketPlayInUseEntity && ((PacketPlayInUseEntity)packet).a() == PacketPlayInUseEntity.EnumEntityUseAction.ATTACK) {
            final double deltaPitch = data.getDeltaPitch();
            final double mouseY = data.getExpiermentalDeltaY();
            if (mouseY > 10000.0 && deltaPitch < 4.0 && deltaPitch > 0.2) {
                final double threshold = this.threshold;
                this.threshold = threshold + 1.0;
                if (threshold > 5.0) {
                    this.logCheat("Mouse Y: " + mouseY + " Delta Pitch: " + deltaPitch);
                    this.threshold = 5.0;
                }
            }
            else {
                this.threshold -= Math.min(this.threshold, 0.6000000238418579);
            }
        }
    }
}
