// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.checks.movement.speed;

import java.util.Collection;
import java.util.HashSet;
import java.util.Arrays;
import gg.noob.plunder.data.PlayerData;
import gg.noob.plunder.util.MathUtils;
import gg.noob.plunder.Plunder;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.entity.Player;
import java.util.Set;
import org.bukkit.Location;
import gg.noob.plunder.checks.Check;

public class SpeedD extends Check
{
    private Location lastLocation;
    private Location lastLastLocation;
    private static final Set<Integer> DIRECTIONS;
    private double threshold;
    
    public SpeedD() {
        super("Speed (D)");
        this.lastLocation = null;
        this.lastLastLocation = null;
        this.threshold = 0.0;
    }
    
    @Override
    public void handleReceivedPacket(final Player player, final Packet packet) {
        if (packet instanceof PacketPlayInFlying) {
            final PacketPlayInFlying packetPlayInFlying = (PacketPlayInFlying)packet;
            final PlayerData data = Plunder.getInstance().getDataManager().getPlayerData(player);
            final Location to = new Location(player.getWorld(), packetPlayInFlying.a(), packetPlayInFlying.b(), packetPlayInFlying.c(), packetPlayInFlying.d(), packetPlayInFlying.e());
            final Location from = (this.lastLocation == null) ? to : this.lastLocation;
            if (data == null) {
                return;
            }
            if (packetPlayInFlying.g()) {
                to.setX(from.getX());
                to.setY(from.getY());
                to.setZ(from.getZ());
            }
            if (packetPlayInFlying.h()) {
                to.setYaw(from.getYaw());
                to.setPitch(from.getPitch());
            }
            if (System.currentTimeMillis() - data.getLastTeleport() < 1000L) {
                return;
            }
            if (this.lastLocation != null && this.lastLastLocation != null) {
                final float moveAngle = MathUtils.getMoveAngle(this.lastLastLocation, this.lastLocation);
                final double deltaX = to.getX() - from.getX();
                final double deltaZ = to.getZ() - from.getZ();
                final double deltaXZ = Math.hypot(deltaX, deltaZ);
                final double yaw = MathUtils.wrapAngleTo180_float(to.getYaw() - from.getYaw());
                if (yaw > 0.001 && yaw <= 360.0 && deltaXZ > 0.01 && !player.isOnGround()) {
                    SpeedD.DIRECTIONS.forEach(direction -> {
                        double change = Math.abs((float) direction - moveAngle);
                        if (change < (double)1.0E-4f) {
                            double d = this.threshold;
                            this.threshold = d + 1.0;
                            if (d > 4.0) {
                                this.logCheat("Change: " + change);
                                this.back();
                            }
                        } else {
                            this.threshold -= Math.min(this.threshold, 0.02f);
                        }
                    });
                }
            }
            this.lastLastLocation = this.lastLocation;
            this.lastLocation = to;
        }
    }
    
    static {
        DIRECTIONS = new HashSet<Integer>(Arrays.asList(45, 90, 135, 180));
    }
}
