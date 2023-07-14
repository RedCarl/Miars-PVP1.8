// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.checks.movement.inventory;

import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import java.util.Collection;
import gg.noob.plunder.util.MathUtils;
import net.minecraft.server.v1_8_R3.PacketPlayInWindowClick;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.entity.Player;
import java.util.ArrayList;
import java.util.List;
import gg.noob.plunder.checks.Check;

public class InventoryC extends Check
{
    private int shiftClickTicks;
    private Long lastClickWindow;
    private List<Long> delays;
    private double lastStd;
    private double threshold;
    
    public InventoryC() {
        super("Inventory (C)");
        this.shiftClickTicks = 0;
        this.lastClickWindow = -1L;
        this.delays = new ArrayList<Long>();
        this.lastStd = 0.0;
        this.threshold = 0.0;
        this.setMaxViolations(10);
        this.setViolationsRemoveOneTime(1000L);
    }
    
    @Override
    public void handleReceivedPacket(final Player player, final Packet packet) {
        if (packet instanceof PacketPlayInWindowClick) {
            final PacketPlayInWindowClick packetPlayInWindowClick = (PacketPlayInWindowClick)packet;
            if (packetPlayInWindowClick.f() == 1 || (packetPlayInWindowClick.f() == 4 && packetPlayInWindowClick.c() == 1)) {
                ++this.shiftClickTicks;
            }
            if (packetPlayInWindowClick.f() == 5 || this.shiftClickTicks > 1) {
                return;
            }
            if ((packetPlayInWindowClick.f() == 0 && packetPlayInWindowClick.c() == 0) || (packetPlayInWindowClick.f() == 0 && packetPlayInWindowClick.c() == 1) || packetPlayInWindowClick.f() == 6 || packetPlayInWindowClick.f() == 1 || (packetPlayInWindowClick.f() == 4 && packetPlayInWindowClick.c() == 1)) {
                final long time = System.currentTimeMillis();
                if (this.lastClickWindow != null) {
                    final long change = System.currentTimeMillis() - (time - this.lastClickWindow);
                    this.delays.add(change);
                    if (this.delays.size() == 5) {
                        final double std = MathUtils.getStandardDeviation(this.delays);
                        final double changeStd = Math.abs(std - this.lastStd);
                        if (changeStd < 0.7) {
                            final double threshold = this.threshold + 1.0;
                            this.threshold = threshold;
                            if (threshold > 2.0) {
                                this.logCheat();
                                player.closeInventory();
                            }
                        }
                        else {
                            this.threshold -= Math.min(this.threshold, 0.5);
                        }
                        this.delays.clear();
                        this.lastStd = std;
                    }
                }
                this.lastClickWindow = time;
            }
        }
        else if (packet instanceof PacketPlayInFlying) {
            this.shiftClickTicks = 0;
        }
    }
}
