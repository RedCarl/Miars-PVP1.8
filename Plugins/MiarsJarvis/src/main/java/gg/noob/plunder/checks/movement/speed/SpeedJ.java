// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.checks.movement.speed;

import gg.noob.plunder.data.PlayerData;
import gg.noob.plunder.Plunder;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import gg.noob.plunder.checks.Check;

public class SpeedJ extends Check
{
    private int bypassTicks;
    private double buffer;
    private boolean lastClientGround;
    
    public SpeedJ() {
        super("Speed (J)");
        this.bypassTicks = 0;
        this.buffer = 0.0;
        this.lastClientGround = false;
    }
    
    @Override
    public void handleLocationUpdate(final Player player, final Location to, final Location from, final PacketPlayInFlying packetPlayInFlying) {
        final PlayerData data = this.getPlayerData();
        if (data == null) {
            return;
        }
        this.bypassTicks = Math.max(0, this.bypassTicks - 1);
        if (this.bypassTicks > 0 || System.currentTimeMillis() - data.getLastTeleport() < 1000L || data.isBouncedOnSlime() || data.getLiquidTicks() > 0 || data.getUnderBlockTicks() > 0 || Plunder.getInstance().getLastVelocity().containsKey(player.getUniqueId()) || player.getAllowFlight()) {
            this.bypassTicks = 20;
            return;
        }
        final float deltaY = (float)(to.getY() - from.getY());
        final boolean mathGround = to.getY() % 0.015625 == 0.0;
        if (!packetPlayInFlying.f() && !mathGround && this.lastClientGround) {
            if (deltaY > 0.0f && deltaY < 0.404444f) {
                final double buffer = this.buffer;
                this.buffer = buffer + 1.0;
                if (buffer > 1.0) {
                    this.logCheat("DeltaY: " + deltaY);
                    this.back();
                }
            }
            else {
                this.buffer = Math.max(0.0, this.buffer - 0.1);
            }
        }
        this.lastClientGround = packetPlayInFlying.f();
    }
}
