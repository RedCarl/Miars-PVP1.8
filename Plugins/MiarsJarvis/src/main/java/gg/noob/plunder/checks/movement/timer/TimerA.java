// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.checks.movement.timer;

import net.minecraft.server.v1_8_R3.PacketPlayOutPosition;
import gg.noob.plunder.data.PlayerData;
import net.minecraft.server.v1_8_R3.PacketPlayInKeepAlive;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.entity.Player;
import org.bukkit.Location;
import gg.noob.plunder.checks.Check;

public class TimerA extends Check
{
    private int sentFlying;
    private long currentFlying;
    private long balance;
    private double buffer;
    private long lastDelayedMove;
    private Location lastValidLoc;
    
    public TimerA() {
        super("Timer (A)");
        this.sentFlying = 0;
        this.currentFlying = -1L;
        this.balance = -64L;
        this.buffer = 0.0;
        this.lastDelayedMove = System.currentTimeMillis();
        this.setMaxViolations(11);
        this.setLagCheck(false);
    }
    
    @Override
    public void handleReceivedPacket(final Player player, final Packet packet) {
        final PlayerData data = this.getPlayerData();
        if (packet instanceof PacketPlayInFlying) {
            final PacketPlayInFlying packetPlayInFlying = (PacketPlayInFlying)packet;
            final Location to = new Location(player.getWorld(), packetPlayInFlying.a(), packetPlayInFlying.b(), packetPlayInFlying.c(), packetPlayInFlying.d(), packetPlayInFlying.e());
            final Location from = new Location(player.getWorld(), data.lastPosX, data.lastPosY, data.lastPosZ, data.lastYaw, data.lastPitch);
            if (!packetPlayInFlying.g() || packetPlayInFlying.b() == -999.0) {
                to.setX(from.getX());
                to.setY(from.getY());
                to.setZ(from.getZ());
            }
            if (!packetPlayInFlying.h()) {
                to.setYaw(from.getYaw());
                to.setPitch(from.getPitch());
            }
            if (data.isTeleporting(5) || data.getFlyingTicks() > 0) {
                this.lastValidLoc = null;
                return;
            }
            ++this.sentFlying;
            if (this.currentFlying == -1L) {
                this.currentFlying = System.currentTimeMillis();
                return;
            }
            final long lastFlying = this.currentFlying;
            this.currentFlying = System.currentTimeMillis();
            final long distance = this.currentFlying - lastFlying;
            this.balance += 50L - distance;
            if (distance > 40L) {
                if (this.lastValidLoc != null) {
                    final double locDistance = to.distance(this.lastValidLoc);
                    if (locDistance > 4.0) {
                        this.logCheat("Balance: " + this.balance + " Distance: " + locDistance);
                        this.back();
                    }
                }
                this.lastValidLoc = to;
            }
            if (this.balance > 8L) {
                this.buffer += ((distance > 40L) ? 1.0 : 2.5);
                if (this.buffer > 10.0 && this.sentFlying > 100 && this.buffer > 16.0) {
                    if (System.currentTimeMillis() - this.lastDelayedMove > 1000L && System.currentTimeMillis() - data.getLastBypass() > 1000L) {
                        this.logCheat("Balance: " + this.balance);
                        this.back();
                    }
                    this.buffer = 12.0;
                }
                this.balance = 0L;
            }
            else {
                this.buffer = Math.max(0.0, this.buffer - (this.isStranger() ? 0.01 : 0.1));
                if (this.balance < -128L) {
                    this.balance = -128L;
                }
            }
            if (this.currentFlying - lastFlying > 120L) {
                this.lastDelayedMove = System.currentTimeMillis();
            }
        }
        else if (packet instanceof PacketPlayInKeepAlive) {
            --this.balance;
        }
    }
    
    @Override
    public void handleSentPacket(final Player player, final Packet packet) {
        if (packet instanceof PacketPlayOutPosition) {
            this.balance -= 50L;
        }
    }
}
