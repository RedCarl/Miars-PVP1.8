// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.checks.combat.aim;

import gg.noob.plunder.data.PlayerData;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import gg.noob.plunder.checks.Check;

public class AimG extends Check
{
    private float suspiciousYaw;
    
    public AimG() {
        super("Aim (G)");
    }
    
    @Override
    public void handleRotationUpdate(final Player player, final Location to, final Location from, final PacketPlayInFlying packetPlayInFlying) {
        final PlayerData data = this.getPlayerData();
        if (System.currentTimeMillis() - data.getLastAttackTime() < 500L) {
            final float yawChange = Math.abs(to.getYaw() - from.getYaw());
            if (yawChange > 1.0f && Math.round(yawChange) == yawChange && yawChange % 1.5f != 0.0f) {
                if (yawChange == this.suspiciousYaw) {
                    this.logCheat("Yaw Change: " + yawChange);
                }
                this.suspiciousYaw = (float)Math.round(yawChange);
            }
            else {
                this.suspiciousYaw = 0.0f;
            }
        }
    }
}
