// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.checks.other.badpackets;

import net.minecraft.server.v1_8_R3.PacketPlayInTransaction;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.entity.Player;
import gg.noob.plunder.checks.Check;

public class BadPacketsC extends Check
{
    private long lastTransactionReceived;
    private long lastFlying;
    private int buffer;
    
    public BadPacketsC() {
        super("Bad Packets (C)");
        this.lastTransactionReceived = -1L;
        this.lastFlying = -1L;
        this.buffer = 0;
        this.setMaxViolations(30);
        this.setViolationsRemoveOneTime(1000L);
    }
    
    @Override
    public boolean handleReceivedPacketCanceled(final Player player, final Packet packet) {
        if (packet instanceof PacketPlayInUseEntity) {
            if (System.currentTimeMillis() - this.lastTransactionReceived < 100L && System.currentTimeMillis() - this.lastFlying > 100L) {
                if (++this.buffer > 3) {
                    this.logCheat("Last Transaction: " + (System.currentTimeMillis() - this.lastTransactionReceived) + " Last Flying: " + (System.currentTimeMillis() - this.lastFlying));
                }
                return true;
            }
            if (this.buffer > 0) {
                --this.buffer;
            }
        }
        if (packet instanceof PacketPlayInFlying) {
            this.lastFlying = System.currentTimeMillis();
        }
        else if (packet instanceof PacketPlayInTransaction) {
            this.lastTransactionReceived = System.currentTimeMillis();
        }
        return false;
    }
}
