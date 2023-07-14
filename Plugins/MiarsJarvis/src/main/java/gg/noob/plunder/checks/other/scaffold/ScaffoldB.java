// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.checks.other.scaffold;

import gg.noob.plunder.data.PlayerData;
import net.minecraft.server.v1_8_R3.PacketPlayInHeldItemSlot;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.entity.Player;
import gg.noob.plunder.checks.Check;

public class ScaffoldB extends Check
{
    private int lastSlot;
    
    public ScaffoldB() {
        super("Scaffold (B)");
        this.setMaxViolations(3);
    }
    
    @Override
    public void handleReceivedPacket(final Player player, final Packet packet) {
        final PlayerData data = this.getPlayerData();
        if (packet instanceof PacketPlayInHeldItemSlot) {
            final PacketPlayInHeldItemSlot packetPlayInHeldItemSlot = (PacketPlayInHeldItemSlot)packet;
            if (packetPlayInHeldItemSlot.a() == this.lastSlot && data.getTicks() > 200) {
                this.logCheat(player);
            }
            this.lastSlot = packetPlayInHeldItemSlot.a();
        }
    }
}
