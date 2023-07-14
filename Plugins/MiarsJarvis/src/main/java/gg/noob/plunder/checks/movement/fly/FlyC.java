// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.checks.movement.fly;

import gg.noob.plunder.data.PlayerData;
import org.bukkit.Location;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.entity.Player;
import gg.noob.plunder.checks.Check;

public class FlyC extends Check
{
    private int count;
    
    public FlyC() {
        super("Fly (C)");
        this.count = 0;
    }
    
    @Override
    public void handleReceivedPacket(final Player player, final Packet packet) {
        if (packet instanceof PacketPlayInFlying) {
            final PacketPlayInFlying packetPlayInFlying = (PacketPlayInFlying)packet;
            final PlayerData data = this.getPlayerData();
            if (data.getAboveSlimeTicks() <= 0) {
                return;
            }
            final Location to = new Location(player.getWorld(), packetPlayInFlying.a(), packetPlayInFlying.b(), packetPlayInFlying.c(), packetPlayInFlying.d(), packetPlayInFlying.e());
            final Location from = player.getLocation();
            if (!data.isTeleporting() && !player.getAllowFlight() && !player.isInsideVehicle() && data.getLiquidTicks() <= 0 && !player.isOnGround() && data.getAboveSlimeTicks() <= 0) {
                final double offsetH = Math.hypot(to.getX() - from.getX(), to.getZ() - from.getZ());
                final double offsetY = to.getY() - from.getY();
                if (offsetH > 0.0 && offsetY == 0.0) {
                    if (++this.count >= 10) {
                        this.logCheat(player, "OffsetH: " + offsetH + " OffsetY: " + offsetY);
                        this.back();
                    }
                }
                else {
                    this.count = 0;
                }
            }
            else {
                this.count = 0;
            }
        }
    }
}
