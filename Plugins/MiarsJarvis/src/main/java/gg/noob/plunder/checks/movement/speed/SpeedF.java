// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.checks.movement.speed;

import gg.noob.plunder.data.PlayerData;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import gg.noob.plunder.checks.Check;

public class SpeedF extends Check
{
    private int threshold;
    private double lastDiff;
    
    public SpeedF() {
        super("Speed (F)");
        this.threshold = 0;
    }
    
    @Override
    public void handleLocationUpdate(final Player player, final Location playerLocation2, final Location playerLocation, final PacketPlayInFlying packetPlayInFlying) {
        final PlayerData data = this.getPlayerData();
        if (data.getNearClimbTicks() > 0 && playerLocation2.getY() > playerLocation.getY() && !player.getAllowFlight() && !data.isTakingVelocity()) {
            final double d = playerLocation2.getY() - playerLocation.getY();
            if (d > ((data.getClientGroundTicks2() > 0) ? 0.42 : 0.118)) {
                final int n = this.threshold++;
                if (n > 1 && d >= this.lastDiff * 0.95) {
                    this.threshold = 0;
                    this.logCheat("D: %s GT: %s", d, data.getClientGroundTicks2());
                    this.back();
                }
            }
            else {
                this.threshold = 0;
            }
            this.lastDiff = d;
        }
    }
}
