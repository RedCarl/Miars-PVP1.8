// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.checks.combat.aim;

import gg.noob.plunder.data.PlayerData;
import java.util.Collection;
import gg.noob.plunder.util.MathUtils;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import java.util.ArrayList;
import java.util.List;
import gg.noob.plunder.checks.Check;

public class AimE extends Check
{
    private double lastSTD;
    private double lastDeltaYaw;
    private final List<Double> deltaYawList;
    
    public AimE() {
        super("Aim (E)");
        this.deltaYawList = new ArrayList<Double>();
        this.setMaxViolations(5);
    }
    
    @Override
    public void handleRotationUpdate(final Player player, final Location to, final Location from, final PacketPlayInFlying packetPlayInFlying, final boolean lastGround) {
        final PlayerData data = this.getPlayerData();
        if (System.currentTimeMillis() - data.getLastAttackTime() < 500L && !data.isTeleporting(3)) {
            final double yaw = MathUtils.wrapAngleTo180_float(to.getYaw());
            if (yaw > 150.0) {
                this.deltaYawList.add(yaw);
                if (this.deltaYawList.size() >= 25) {
                    final double std = MathUtils.getStandardDeviation(this.deltaYawList);
                    if (std < 0.03 || (Math.abs(std - this.lastSTD) < 0.001 && yaw > 155.0)) {
                        this.logCheat("Standard Deviation: " + std + " List: " + this.deltaYawList.size());
                    }
                    this.lastSTD = std;
                    this.deltaYawList.clear();
                }
            }
            this.lastDeltaYaw = yaw;
        }
    }
}
