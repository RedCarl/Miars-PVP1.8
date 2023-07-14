// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.checks.combat.noswing;

import gg.noob.plunder.data.PlayerData;
import net.minecraft.server.v1_8_R3.PacketPlayInBlockDig;
import net.minecraft.server.v1_8_R3.PacketPlayInArmAnimation;
import gg.noob.plunder.Plunder;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.entity.Player;
import gg.noob.plunder.checks.Check;

public class NoSwingB extends Check
{
    private int invalid;
    
    public NoSwingB() {
        super("No Swing (B)");
        this.invalid = 0;
    }
    
    @Override
    public void handleReceivedPacket(final Player player, final Packet packet) {
        final PlayerData data = Plunder.getInstance().getDataManager().getPlayerData(player);
        if (data == null) {
            return;
        }
        if (packet instanceof PacketPlayInArmAnimation) {
            this.invalid = 0;
        }
        else if (packet instanceof PacketPlayInBlockDig) {
            final PacketPlayInBlockDig packetPlayInBlockDig = (PacketPlayInBlockDig)packet;
            if ((packetPlayInBlockDig.c() == PacketPlayInBlockDig.EnumPlayerDigType.START_DESTROY_BLOCK || packetPlayInBlockDig.c() == PacketPlayInBlockDig.EnumPlayerDigType.STOP_DESTROY_BLOCK) && System.currentTimeMillis() - data.getLastClientTransaction() < 500L && this.invalid++ > 5) {
                this.logCheat(player);
            }
        }
    }
}
