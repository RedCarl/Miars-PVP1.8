// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.checks.combat.killaura;

import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.entity.Player;
import gg.noob.plunder.checks.Check;

public class KillAuraC extends Check
{
    private int buffer;
    private int legalHits;
    private int illegalHits;
    private int movements;
    private int lastMovements;
    
    public KillAuraC() {
        super("KillAura (C)");
        this.buffer = 0;
        this.legalHits = 0;
        this.illegalHits = 0;
        this.movements = 0;
        this.lastMovements = 0;
        this.setMaxViolations(5);
    }
    
    @Override
    public void handleReceivedPacket(final Player player, final Packet packet) {
        if (packet instanceof PacketPlayInUseEntity) {
            if (this.movements < 10) {
                if (this.movements == this.lastMovements) {
                    ++this.illegalHits;
                }
                else {
                    ++this.legalHits;
                }
                if (this.legalHits + this.illegalHits == 30) {
                    if (this.illegalHits > 24) {
                        this.buffer += 12;
                        if (this.buffer > 20) {
                            this.logCheat("Illegal Hits Ratio: " + this.illegalHits / 30.0 + " Movements: " + this.movements);
                            this.buffer = 20;
                        }
                    }
                    else if (this.buffer > 0) {
                        this.buffer -= 4;
                    }
                    this.illegalHits = 0;
                    this.legalHits = 0;
                }
            }
            this.lastMovements = this.movements;
            this.movements = 0;
        }
        else if (packet instanceof PacketPlayInFlying) {
            ++this.movements;
        }
    }
}
