// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.checks.combat.aim;

import java.util.Collection;
import gg.noob.plunder.util.MathUtils;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import java.util.ArrayList;
import java.util.List;
import gg.noob.plunder.checks.Check;

public class AimJ extends Check
{
    private double threshold;
    private double lastSTD;
    private List<Double> deltaYawList;
    
    public AimJ() {
        super("Aim (J)");
        this.deltaYawList = new ArrayList<Double>();
        this.setMaxViolations(3);
    }
    
    @Override
    public void handleRotationUpdate(final Player player, final Location to, final Location from, final PacketPlayInFlying packetPlayInFlying) {
        final double yaw = MathUtils.wrapAngleTo180_float(to.getYaw() - from.getYaw());
        if (yaw > 1.0) {
            this.deltaYawList.add(yaw);
            if (this.deltaYawList.size() >= 100) {
                final double std = MathUtils.getStandardDeviation(this.deltaYawList);
                final double distance = Math.abs(std - this.lastSTD);
                if (distance < 0.01) {
                    final double threshold = this.threshold + 1.0;
                    this.threshold = threshold;
                    if (threshold > 6.0) {
                        this.logCheat("STD Distance: " + distance);
                    }
                }
                else {
                    this.threshold -= Math.min(this.threshold, 0.25);
                }
                this.lastSTD = std;
                this.deltaYawList.clear();
            }
        }
    }
}
