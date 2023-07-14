// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.checks.combat.killaura;

import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.entity.Player;
import gg.noob.plunder.checks.Check;

public class KillAuraE extends Check
{
    private int ticks;
    private int invalidTicks;
    private int lastTicks;
    private int totalTicks;
    
    public KillAuraE() {
        super("KillAura (E)");
        this.ticks = 0;
        this.invalidTicks = 0;
        this.lastTicks = 0;
        this.totalTicks = 0;
        this.setMaxViolations(5);
    }
    
    @Override
    public void handleReceivedPacket(final Player player, final Packet packet) {
        if (packet instanceof PacketPlayInUseEntity) {
            if (this.ticks <= 8) {
                if (this.lastTicks == this.ticks) {
                    ++this.invalidTicks;
                }
                if (++this.totalTicks >= 25) {
                    if (this.invalidTicks > 22) {
                        this.logCheat();
                    }
                    this.totalTicks = 0;
                    this.invalidTicks = 0;
                }
                this.lastTicks = this.ticks;
            }
        }
        else if (packet instanceof PacketPlayInFlying) {
            ++this.ticks;
        }
    }
}
