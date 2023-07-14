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

public class AimC extends Check
{
    private List<Double> deltaPitchList;
    
    public AimC() {
        super("Aim (C)");
        this.deltaPitchList = new ArrayList<Double>();
    }
    
    @Override
    public void handleRotationUpdate(final Player player, final Location to, final Location from, final PacketPlayInFlying packetPlayInFlying) {
        final PlayerData data = this.getPlayerData();
        if (data.isTeleporting(3)) {
            return;
        }
        final double deltaPitch = Math.abs(to.getPitch() - from.getPitch());
        if (deltaPitch > 0.8) {
            this.deltaPitchList.add(deltaPitch);
            if (this.deltaPitchList.size() > 125) {
                final double std = MathUtils.getStandardDeviation(this.deltaPitchList);
                if (std < 0.9) {
                    this.logCheat("STD: " + std);
                }
                this.deltaPitchList.clear();
            }
        }
    }
}
