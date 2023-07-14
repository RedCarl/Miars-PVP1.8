// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.checks.combat.killaura;

import gg.noob.plunder.data.PlayerData;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;
import net.minecraft.server.v1_8_R3.PacketPlayInArmAnimation;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.entity.Player;
import gg.noob.plunder.checks.Check;

public class KillAuraA extends Check
{
    private int arm;
    private int useEntity;
    private int buffer;
    private int validAmount;
    
    public KillAuraA() {
        super("KillAura (A)");
        this.setMaxViolations(5);
    }
    
    @Override
    public void handleReceivedPacket(final Player player, final Packet packet) {
        final PlayerData data = this.getPlayerData();
        if (packet instanceof PacketPlayInArmAnimation) {
            if (data.deltaXZ > 0.21 && data.target != null && Math.abs(data.target.getVelocity().getY() + 0.078) > 0.001) {
                ++this.validAmount;
            }
            if (++this.arm >= 14) {
                final float ratio = this.useEntity / (float)this.arm;
                if (ratio > 0.99f) {
                    if (this.validAmount > 6 && ++this.buffer > 4) {
                        this.logCheat("Ratio: " + ratio * 100.0f + " Buffer: " + this.buffer);
                    }
                }
                else {
                    this.buffer = 0;
                }
                final int arm = 0;
                this.useEntity = arm;
                this.validAmount = arm;
                this.arm = arm;
            }
        }
        else if (packet instanceof PacketPlayInUseEntity) {
            ++this.useEntity;
        }
    }
}
