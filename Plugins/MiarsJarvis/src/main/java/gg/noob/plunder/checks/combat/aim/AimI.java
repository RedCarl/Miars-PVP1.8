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

public class AimI extends Check
{
    private List<Double> deltaYawList;
    
    public AimI() {
        super("Aim (I)");
        this.deltaYawList = new ArrayList<Double>();
        this.setMaxViolations(3);
    }
    
    @Override
    public void handleRotationUpdate(final Player player, final Location to, final Location from, final PacketPlayInFlying packetPlayInFlying) {
        final PlayerData data = this.getPlayerData();
        if (data.isTeleporting(3)) {
            return;
        }
        final double yaw = MathUtils.wrapAngleTo180_float(to.getYaw() - from.getYaw());
        if (yaw > 1.0) {
            this.deltaYawList.add(yaw);
            if (this.deltaYawList.size() >= 100) {
                final double std = MathUtils.getStandardDeviation(this.deltaYawList);
                if (std < 0.1) {
                    this.logCheat("STD: " + std);
                }
                this.deltaYawList.clear();
            }
        }
    }
}
