// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.checks.combat.killaura;

import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.entity.Player;
import gg.noob.plunder.checks.Check;

public class KillAuraJ extends Check
{
    private boolean interact;
    
    public KillAuraJ() {
        super("KillAura (J)");
        this.interact = false;
        this.setMaxViolations(5);
    }
    
    @Override
    public void handleReceivedPacket(final Player player, final Packet packet) {
        if (packet instanceof PacketPlayInFlying) {
            this.interact = false;
        }
        else if (packet instanceof PacketPlayInUseEntity) {
            final PacketPlayInUseEntity packetPlayInUseEntity = (PacketPlayInUseEntity)packet;
            if ((packetPlayInUseEntity.a() == PacketPlayInUseEntity.EnumEntityUseAction.INTERACT || packetPlayInUseEntity.a() == PacketPlayInUseEntity.EnumEntityUseAction.INTERACT_AT) && player.getItemInHand() != null && player.getItemInHand().getType().name().contains("SWORD")) {
                this.interact = true;
            }
            if (packetPlayInUseEntity.a() == PacketPlayInUseEntity.EnumEntityUseAction.ATTACK && this.interact) {
                this.logCheat("Interacting while attacking");
            }
        }
    }
}
