// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.checks.movement.inventory;

import gg.noob.plunder.data.PlayerData;
import org.bukkit.plugin.Plugin;
import gg.noob.plunder.Plunder;
import org.bukkit.Bukkit;
import net.minecraft.server.v1_8_R3.PacketPlayInWindowClick;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.entity.Player;
import gg.noob.plunder.checks.Check;

public class InventoryD extends Check
{
    public InventoryD() {
        super("Inventory (D)");
        this.setMaxViolations(10);
        this.setViolationsRemoveOneTime(1000L);
    }
    
    @Override
    public boolean handleReceivedPacketCanceled(final Player player, final Packet packet) {
        if (packet instanceof PacketPlayInWindowClick) {
            final PlayerData data = this.getPlayerData();
            final PacketPlayInWindowClick packetPlayInWindowClick = (PacketPlayInWindowClick)packet;
            if (packetPlayInWindowClick.a() == 0 && !data.isInInventory()) {
                this.logCheat();
                player.closeInventory();
                Bukkit.getScheduler().runTaskLater((Plugin)Plunder.getInstance(), player::updateInventory, 1L);
                return true;
            }
        }
        return false;
    }
}
