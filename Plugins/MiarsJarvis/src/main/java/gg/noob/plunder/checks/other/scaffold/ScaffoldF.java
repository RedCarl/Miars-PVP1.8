// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.checks.other.scaffold;

import gg.noob.plunder.data.PlayerData;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import net.minecraft.server.v1_8_R3.PacketPlayInEntityAction;
import net.minecraft.server.v1_8_R3.PacketPlayInBlockPlace;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.entity.Player;
import gg.noob.plunder.checks.Check;

public class ScaffoldF extends Check
{
    private long lastFlying;
    private long lastPlace;
    private double average;
    
    public ScaffoldF() {
        super("Scaffold (F)");
        this.average = 20.0;
        this.setMaxViolations(3);
    }
    
    @Override
    public void handleReceivedPacket(final Player player, final Packet packet) {
        final PlayerData data = this.getPlayerData();
        if (packet instanceof PacketPlayInBlockPlace) {
            final PacketPlayInBlockPlace packetPlayInBlockPlace = (PacketPlayInBlockPlace)packet;
            if (packetPlayInBlockPlace.a() == null || packetPlayInBlockPlace.a().getY() == -1) {
                return;
            }
            if (!player.getItemInHand().getType().isBlock()) {
                return;
            }
            this.lastPlace = System.currentTimeMillis();
        }
        else if (packet instanceof PacketPlayInEntityAction) {
            final PacketPlayInEntityAction packetPlayInEntityAction = (PacketPlayInEntityAction)packet;
            if (packetPlayInEntityAction.b() == PacketPlayInEntityAction.EnumPlayerAction.START_SNEAKING && System.currentTimeMillis() - this.lastPlace < 300L) {
                final double diff = (double)(System.currentTimeMillis() - this.lastFlying);
                this.average = (this.average * 4.0 + diff) / 5.0;
                if (this.average < 10.0) {
                    this.logCheat("Average: " + this.average);
                    this.average = 20.0;
                }
            }
        }
        else if (packet instanceof PacketPlayInFlying) {
            this.lastFlying = System.currentTimeMillis();
        }
    }
}
