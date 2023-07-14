// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.checks.movement.fly;

import gg.noob.plunder.data.PlayerData;
import org.bukkit.GameMode;
import gg.noob.plunder.util.PlayerUtils;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import gg.noob.plunder.checks.Check;

public class FlyO extends Check
{
    private int buffer;
    private double lDeltaY;
    
    public FlyO() {
        super("Fly (O)");
        this.buffer = 0;
        this.lDeltaY = 0.0;
    }
    
    @Override
    public void handleLocationUpdate(final Player player, final Location to, final Location from, final PacketPlayInFlying packetPlayInFlying, final boolean lastGround) {
        final PlayerData data = this.getPlayerData();
        final double deltaY = to.getY() - from.getY();
        if (data.isTakingVelocity(20) || player.getAllowFlight() || data.isPlacingBlocks() || PlayerUtils.checkMovement(player) || data.isDoingBlockUpdate() || data.isTeleporting() || player.getGameMode() == GameMode.CREATIVE || data.getUnderBlockTicks() > 0) {
            return;
        }
        if (Math.abs(deltaY + this.lDeltaY) < 0.05 && data.getHalfBlockTicks() < 4 && data.getAboveSlimeTicks() < 5 && Math.abs(deltaY) > 0.2) {
            this.buffer += 15;
            if (this.buffer > 20) {
                this.logCheat("Delta Y: " + deltaY + " Last Delta Y: " + this.lDeltaY);
                this.back();
                this.buffer = 20;
            }
        }
        else if (this.buffer > 0) {
            --this.buffer;
        }
        this.lDeltaY = deltaY;
    }
}
