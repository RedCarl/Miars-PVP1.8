// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.checks.other.scaffold;

import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import net.minecraft.server.v1_8_R3.PacketPlayInBlockPlace;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.entity.Player;
import gg.noob.plunder.checks.Check;

public class ScaffoldE extends Check
{
    private long lastFlying;
    private double average;
    
    public ScaffoldE() {
        super("Scaffold (E)");
        this.lastFlying = System.currentTimeMillis();
        this.average = 20.0;
        this.setMaxViolations(3);
        this.setViolationsRemoveOneTime(1000L);
    }
    
    @Override
    public void handleReceivedPacket(final Player player, final Packet packet) {
        if (packet instanceof PacketPlayInBlockPlace) {
            if (!player.getItemInHand().getType().isBlock()) {
                return;
            }
            final double diff = (double)(System.currentTimeMillis() - this.lastFlying);
            this.average = (this.average * 9.0 + diff) / 10.0;
            if (this.average < 10.0) {
                this.logCheat("Average: " + this.average);
                this.average = 20.0;
            }
        }
        else if (packet instanceof PacketPlayInFlying) {
            this.lastFlying = System.currentTimeMillis();
        }
    }
}
