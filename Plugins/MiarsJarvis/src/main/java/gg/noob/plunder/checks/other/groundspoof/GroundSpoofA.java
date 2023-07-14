//
// Decompiled by Procyon v0.5.36
//

package gg.noob.plunder.checks.other.groundspoof;

import gg.noob.plunder.checks.Check;
import gg.noob.plunder.data.PlayerData;
import gg.noob.plunder.util.BlockUtils;
import gg.noob.plunder.util.Pair;
import gg.noob.plunder.util.PlayerUtils;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import net.minecraft.server.v1_8_R3.PacketPlayOutBlockChange;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class GroundSpoofA extends Check
{
    private static final double divisor = 0.015625;
    private int airBuffer;
    private int groundBuffer;
    private boolean lastGround;
    private boolean lastLastGround;
    private boolean lastLastLastGround;

    public GroundSpoofA() {
        super("Ground Spoof (A)");
        this.airBuffer = 0;
        this.groundBuffer = 0;
        this.lastGround = false;
        this.lastLastGround = false;
        this.lastLastLastGround = false;
        this.setViolationsRemoveOneTime(1000L);
    }

    @Override
    public void handleReceivedPacket(final Player player, final Packet packet) {
        if (packet instanceof PacketPlayInFlying) {
            final PacketPlayInFlying packetPlayInFlying = (PacketPlayInFlying)packet;
            final PlayerData data = this.getPlayerData();
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
            if (data.isTeleporting(10) || data.moveTicks < 2 || player.getAllowFlight() || System.currentTimeMillis() - data.getLastBypass() < 1000L || data.getAboveSlimeTicks() > 0 || data.getStairsTicks() > 0.0 || data.getHalfBlockTicks() > 0 || PlayerUtils.hasInvalidJumpBoost(player) || data.getNearSlabTicks() > 0 || player.getGameMode() == GameMode.CREATIVE || data.getNearClimbTicks() > 0) {
                if (this.groundBuffer > 0) {
                    --this.groundBuffer;
                }
                if (this.airBuffer > 0) {
                    --this.airBuffer;
                }
                return;
            }
            if (packetPlayInFlying.f()) {
                final boolean isAir = (!this.lastGround && !this.lastLastGround && !this.lastLastLastGround) || (data.getGroundTicks() < data.getMaxPingTicks() && data.getClientGroundTicks() == (this.lastGround ? 1 : 0) + (this.lastLastGround ? 1 : 0) + (this.lastLastLastGround ? 1 : 0));
                if (isAir && !data.isServerGround() && !data.isBlockBelow() && !data.isBlockNear()) {
                    if (data.getTicks() - data.getLastBlockPlacedTicks() < 10L) {
                        if (data.getTicks() - data.getLastBlockPlacedCanceledTicks() < 10L) {
                            this.back();
                        }
                        return;
                    }
                    if (System.currentTimeMillis() - data.getLastBlockBreak() < 500L) {
                        if (System.currentTimeMillis() - data.getLastBlockBreakCanceled() < 500L) {
                            this.back();
                        }
                        return;
                    }
                    if (++this.groundBuffer > 1.0) {
                        this.logCheat("Client Ground Ticks: " + data.getClientGroundTicks() + " Air Ticks: " + data.getAirTicks() + " Ground Ticks: " + data.getGroundTicks() + " Ping Ticks: " + data.getMaxPingTicks() + " Added: " + (this.lastGround ? 1 : 0) + (this.lastLastGround ? 1 : 0) + (this.lastLastLastGround ? 1 : 0));
                        this.back();
                    }
                    for (final Pair<Block, net.minecraft.server.v1_8_R3.Block> pair : BlockUtils.getNearbyBlocksBukkitAndNMS(to, 1)) {
                        final Block block = pair.getX();
                        ((CraftPlayer)player).getHandle().playerConnection.sendPacket((Packet)new PacketPlayOutBlockChange((World)((CraftWorld)block.getWorld()).getHandle(), BlockUtils.toPosition(block.getLocation())));
                    }
                }
                else {
                    this.groundBuffer -= (int)0.1;
                }
            }
            final boolean dground = Math.abs(to.getY()) % 0.015625 < 1.0E-4 && data.isNearGround();
            if (!packetPlayInFlying.f()) {
                if (data.getOnWebTicks() <= 0 && !player.isInsideVehicle() && (data.isServerGround() || data.isBlockBelow()) && dground && data.getHalfBlockTicks() <= 0 && !data.isTeleporting(10)) {
                    if ((this.airBuffer += 10) > 30) {
                        this.logCheat("Type: Air");
                        this.back();
                    }
                }
                else if (this.airBuffer > 0) {
                    this.airBuffer -= 4;
                }
            }
        }
    }
}
