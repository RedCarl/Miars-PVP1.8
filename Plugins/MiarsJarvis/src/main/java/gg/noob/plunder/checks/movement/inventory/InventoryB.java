// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.checks.movement.inventory;

import gg.noob.plunder.data.PlayerData;
import gg.noob.plunder.util.MathUtils;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.entity.Player;
import gg.noob.plunder.checks.Check;

public class InventoryB extends Check
{
    private double threshold;
    private double invTicks;
    
    public InventoryB() {
        super("Inventory (B)");
        this.threshold = 0.0;
        this.invTicks = 0.0;
        this.setViolationsRemoveOneTime(1500L);
    }
    
    @Override
    public void handleReceivedPacket(final Player player, final Packet packet) {
        final PlayerData data = this.getPlayerData();
        if (packet instanceof PacketPlayInFlying) {
            if (data.isTeleporting() || data.isTakingVelocity() || data.getOnIceTicks() > 0 || data.isBouncedOnSlime() || data.getPistonTicks() > 0.0) {
                final double n = 0.0;
                this.invTicks = n;
                this.threshold = n;
                return;
            }
            final double max = (this.invTicks <= 30.0) ? MathUtils.getBaseSpeed(player, data) : 0.01;
            if (data.isInInventory()) {
                ++this.invTicks;
                if (this.invTicks > 12.0) {
                    if (data.getDeltaXZ() > max) {
                        final double threshold = this.threshold + 1.0;
                        this.threshold = threshold;
                        if (threshold > 15.0) {
                            this.logCheat();
                            player.closeInventory();
                        }
                    }
                    else {
                        this.threshold -= Math.min(this.threshold, 0.1);
                    }
                }
                else {
                    this.threshold -= Math.min(this.threshold, 0.1);
                }
            }
            else {
                final double n2 = 0.0;
                this.invTicks = n2;
                this.threshold = n2;
            }
        }
    }
}
