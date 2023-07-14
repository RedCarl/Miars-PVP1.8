// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.checks.movement.fly;

import gg.noob.plunder.data.PlayerData;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import gg.noob.plunder.checks.Check;

public class FlyD extends Check
{
    public FlyD() {
        super("Fly (D)");
        this.setMaxViolations(6);
    }
    
    @Override
    public void handleLocationUpdate(final Player player, final Location to, final Location from, final PacketPlayInFlying packetPlayInFlying) {
        final PlayerData data = this.getPlayerData();
        final double deltaXZ = Math.hypot(to.getX() - from.getX(), to.getZ() - from.getZ());
        final double deltaY = Math.abs(to.getY() - from.getY());
        if ((deltaXZ > 4.0 || deltaY > 4.0) && !data.isTeleporting(3) && data.getInVehicleTicks() <= 0 && data.getFlyingTicks() <= 0 && System.currentTimeMillis() - data.getLastBypass() > 1000L) {
            this.logCheat("Delta XZ: " + deltaXZ + " Delta Y: " + deltaY);
            this.back();
        }
    }
}
