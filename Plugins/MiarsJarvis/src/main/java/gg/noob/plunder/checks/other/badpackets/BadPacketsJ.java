// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.checks.other.badpackets;

import gg.noob.plunder.data.PlayerData;
import net.minecraft.server.v1_8_R3.PacketPlayInSteerVehicle;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.entity.Player;
import gg.noob.plunder.checks.Check;

public class BadPacketsJ extends Check
{
    private int ticks;
    private double buffer;
    
    public BadPacketsJ() {
        super("Bad Packets (J)");
        this.ticks = 0;
        this.buffer = 0.0;
        this.setMaxViolations(11);
    }
    
    @Override
    public boolean handleReceivedPacketCanceled(final Player player, final Packet packet) {
        final PlayerData data = this.getPlayerData();
        boolean cancel = false;
        if (packet instanceof PacketPlayInFlying) {
            final PacketPlayInFlying packetPlayInFlying = (PacketPlayInFlying)packet;
            if (data.getTicks() < 20 || data.isTeleporting(3)) {
                this.ticks = 0;
                this.buffer = 0.0;
                return false;
            }
            if (packetPlayInFlying.g()) {
                if (this.ticks > 20) {
                    final double buffer = this.buffer + 1.0;
                    this.buffer = buffer;
                    if (buffer > 1.0) {
                        this.logCheat("Ticks: " + this.ticks);
                        this.buffer = 1.0;
                    }
                    cancel = true;
                }
                this.ticks = 0;
                if (this.buffer > 0.0) {
                    this.buffer -= 0.01;
                }
            }
            else {
                ++this.ticks;
            }
        }
        else if (packet instanceof PacketPlayInSteerVehicle) {
            this.ticks = 0;
            this.buffer = 0.0;
        }
        return cancel;
    }
}
