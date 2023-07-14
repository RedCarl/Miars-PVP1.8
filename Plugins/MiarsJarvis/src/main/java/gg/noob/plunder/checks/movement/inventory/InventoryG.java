// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.checks.movement.inventory;

import gg.noob.plunder.data.PlayerData;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;
import net.minecraft.server.v1_8_R3.PacketPlayInWindowClick;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.entity.Player;
import gg.noob.plunder.checks.Check;

public class InventoryG extends Check
{
    private boolean clickWindow;
    
    public InventoryG() {
        super("Inventory (G)");
        this.clickWindow = false;
        this.setMaxViolations(6);
    }
    
    @Override
    public boolean handleReceivedPacketCanceled(final Player player, final Packet packet) {
        boolean cancel = false;
        if (packet instanceof PacketPlayInWindowClick) {
            this.clickWindow = true;
        }
        else if (packet instanceof PacketPlayInUseEntity) {
            final PlayerData data = this.getPlayerData();
            final PacketPlayInUseEntity packetPlayInUseEntity = (PacketPlayInUseEntity)packet;
            if (packetPlayInUseEntity.a() == PacketPlayInUseEntity.EnumEntityUseAction.ATTACK && this.clickWindow) {
                this.logCheat();
                player.closeInventory();
                cancel = true;
            }
        }
        else if (packet instanceof PacketPlayInFlying) {
            this.clickWindow = false;
        }
        return cancel;
    }
}
