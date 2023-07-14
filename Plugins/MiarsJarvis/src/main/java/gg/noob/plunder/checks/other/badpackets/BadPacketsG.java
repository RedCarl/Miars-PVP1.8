// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.checks.other.badpackets;

import gg.noob.plunder.data.PlayerData;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import net.minecraft.server.v1_8_R3.PacketPlayInEntityAction;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.entity.Player;
import gg.noob.plunder.checks.Check;

public class BadPacketsG extends Check
{
    private boolean sent;
    private int count;
    
    public BadPacketsG() {
        super("Bad Packets (G)");
        this.sent = false;
        this.count = 0;
        this.setMaxViolations(11);
        this.setViolationsRemoveOneTime(1000L);
    }
    
    @Override
    public void handleReceivedPacket(final Player player, final Packet packet) {
        final PlayerData data = this.getPlayerData();
        if (packet instanceof PacketPlayInEntityAction) {
            final PacketPlayInEntityAction.EnumPlayerAction playerAction = ((PacketPlayInEntityAction)packet).b();
            if (playerAction == PacketPlayInEntityAction.EnumPlayerAction.START_SPRINTING || playerAction == PacketPlayInEntityAction.EnumPlayerAction.STOP_SPRINTING) {
                if (this.sent) {
                    ++this.count;
                    if (this.count > 3) {
                        this.logCheat(player);
                        this.sent = false;
                        this.count = 0;
                    }
                }
                else {
                    this.sent = true;
                }
            }
            else if (playerAction == PacketPlayInEntityAction.EnumPlayerAction.OPEN_INVENTORY || playerAction == PacketPlayInEntityAction.EnumPlayerAction.RIDING_JUMP) {
                data.fallFlying = true;
            }
        }
        else if (packet instanceof PacketPlayInFlying) {
            this.sent = false;
        }
    }
}
