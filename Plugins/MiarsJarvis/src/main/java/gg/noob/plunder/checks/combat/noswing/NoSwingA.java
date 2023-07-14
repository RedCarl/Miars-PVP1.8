// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.checks.combat.noswing;

import gg.noob.plunder.data.PlayerData;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;
import net.minecraft.server.v1_8_R3.PacketPlayInArmAnimation;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import gg.noob.plunder.Plunder;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.entity.Player;
import gg.noob.plunder.checks.Check;

public class NoSwingA extends Check
{
    private boolean received;
    
    public NoSwingA() {
        super("No Swing (A)");
        this.received = false;
    }
    
    @Override
    public void handleReceivedPacket(final Player player, final Packet packet) {
        final PlayerData data = Plunder.getInstance().getDataManager().getPlayerData(player);
        if (data == null) {
            return;
        }
        if (packet instanceof PacketPlayInFlying) {
            this.received = false;
        }
        else if (packet instanceof PacketPlayInArmAnimation) {
            this.received = true;
        }
        else if (packet instanceof PacketPlayInUseEntity && ((PacketPlayInUseEntity)packet).a() == PacketPlayInUseEntity.EnumEntityUseAction.ATTACK) {
            if (!this.received) {
                this.logCheat(player);
            }
            this.received = false;
        }
    }
}
