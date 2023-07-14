// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.checks.combat.autoclicker;

import gg.noob.plunder.data.PlayerData;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import net.minecraft.server.v1_8_R3.PacketPlayInArmAnimation;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.entity.Player;
import gg.noob.lib.util.LinkedList;
import java.util.Deque;
import gg.noob.plunder.checks.Check;

public class AutoClickerD extends Check
{
    private Deque<Integer> recentData;
    private int movements;
    
    public AutoClickerD() {
        super("AutoClicker (D)");
        this.recentData = (Deque<Integer>)new LinkedList();
        this.setMaxViolations(3);
    }
    
    @Override
    public void handleReceivedPacket(final Player player, final Packet packet) {
        final PlayerData data = this.getPlayerData();
        if (packet instanceof PacketPlayInArmAnimation) {
            if (this.movements < 10 && !data.isDiggingBlocks() && !data.isPlacingBlocks()) {
                this.recentData.add(this.movements);
                if (this.recentData.size() == 500) {
                    final int outliers = Math.toIntExact(this.recentData.stream().mapToInt(i -> i).filter(i -> i > 3).count());
                    if (outliers < 5) {
                        this.logCheat("Outliers: " + outliers);
                    }
                    this.recentData.clear();
                }
            }
            this.movements = 0;
        }
        else if (packet instanceof PacketPlayInFlying) {
            ++this.movements;
        }
    }
}
