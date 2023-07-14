// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.checks.movement.inventory;

import gg.noob.plunder.data.PlayerData;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import gg.noob.plunder.util.MathUtils;
import net.minecraft.server.v1_8_R3.PacketPlayInWindowClick;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.entity.Player;
import gg.noob.plunder.checks.Check;

public class InventoryA extends Check
{
    private int ticks;
    
    public InventoryA() {
        super("Inventory (A)");
        this.ticks = 0;
        this.setMaxViolations(10);
        this.setViolationsRemoveOneTime(1000L);
    }
    
    @Override
    public void handleReceivedPacket(final Player player, final Packet packet) {
        final PlayerData data = this.getPlayerData();
        if (packet instanceof PacketPlayInWindowClick) {
            if (data.getPistonTicks() > 0.0 || data.getOnIceTicks() > 0 || data.isTeleporting() || data.isTakingVelocity() || player.getAllowFlight()) {
                this.ticks = 0;
                return;
            }
            final double deltaXZ = data.getDeltaXZ();
            final double baseSpeed = MathUtils.getBaseSpeed(player, data);
            if (this.ticks > 7 && deltaXZ > baseSpeed) {
                this.logCheat("Delta XZ: " + deltaXZ + " Base Speed: " + baseSpeed + " Ticks: " + this.ticks);
                player.closeInventory();
            }
        }
        else if (packet instanceof PacketPlayInFlying) {
            if (data.isInInventory()) {
                ++this.ticks;
            }
            else {
                this.ticks -= Math.min(this.ticks, 2);
            }
        }
    }
}
