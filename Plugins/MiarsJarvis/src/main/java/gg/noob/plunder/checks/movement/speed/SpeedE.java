// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.checks.movement.speed;

import org.bukkit.inventory.PlayerInventory;
import org.bukkit.World;
import gg.noob.plunder.data.PlayerData;
import org.bukkit.enchantments.Enchantment;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Map;
import org.bukkit.util.Vector;
import gg.noob.plunder.Plunder;
import org.bukkit.potion.PotionEffectType;
import com.google.common.util.concurrent.AtomicDouble;
import gg.noob.plunder.util.MathUtils;
import gg.noob.plunder.util.PlayerUtils;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import gg.noob.plunder.checks.Check;

public class SpeedE extends Check
{
    private double buffer;
    private int lastFastAirTick;
    private int lastAirTick;
    private int lastGroundTick;
    private int lastBypassTick;
    private int bypassFallbackTicks;
    private int lastBlockAboveTick;
    private int lastSpeed;
    
    public SpeedE() {
        super("Speed (E)");
        this.buffer = 0.0;
        this.lastFastAirTick = 0;
        this.lastAirTick = 0;
        this.lastGroundTick = 0;
        this.lastBypassTick = -50;
        this.bypassFallbackTicks = 0;
        this.lastBlockAboveTick = -20;
        this.lastSpeed = 0;
    }
    
    @Override
    public void handleLocationUpdate(final Player player, final Location playerLocation2, final Location playerLocation, final PacketPlayInFlying packetPlayInFlying) {
        final PlayerData data = this.getPlayerData();
        if ((playerLocation2.getX() != playerLocation.getX() || playerLocation2.getZ() != playerLocation.getZ()) && !data.isTeleporting() && !player.isInsideVehicle() && !PlayerUtils.hasInvalidJumpBoost(player) && !data.isFallFlying()) {
            if (data.getFlyingTicks() > 0) {
                this.bypassFallbackTicks = 20;
                return;
            }
            final double d = MathUtils.hypot(playerLocation2.getX() - playerLocation.getX(), playerLocation2.getZ() - playerLocation.getZ());
            final AtomicDouble doubleWrapper = new AtomicDouble(0.0);
            int n = Math.max(PlayerUtils.getPotionEffectLevel(player, PotionEffectType.SPEED), 0);
            if (this.lastSpeed > n) {
                n = this.lastSpeed - 1;
            }
            if (packetPlayInFlying.f()) {
                if (this.bypassFallbackTicks > 0) {
                    this.bypassFallbackTicks -= 10;
                }
                this.lastGroundTick = data.getTicks();
                final double d2 = Math.toDegrees(-Math.atan2(playerLocation2.getX() - playerLocation.getX(), playerLocation2.getZ() - playerLocation.getZ()));
                final double d3 = MathUtils.getDistanceBetweenAngles360(d2, playerLocation2.getYaw());
                final boolean bl = d3 < 5.0;
                final double d4 = bl ? 0.281 : 0.2865;
                doubleWrapper.addAndGet(n * 0.0573);
                doubleWrapper.addAndGet(d4);
                if (player.getWalkSpeed() > 0.2) {
                    doubleWrapper.set(doubleWrapper.get() * player.getWalkSpeed() / 0.2);
                }
                if (this.lastAirTick >= this.lastGroundTick - 10) {
                    doubleWrapper.addAndGet((this.lastGroundTick - this.lastAirTick) * 0.125);
                }
            }
            else {
                if (this.bypassFallbackTicks > 0) {
                    doubleWrapper.addAndGet(0.1);
                    --this.bypassFallbackTicks;
                }
                this.lastAirTick = data.getTicks();
                boolean bl2 = false;
                if (d > 0.36 && this.lastFastAirTick < this.lastGroundTick) {
                    this.lastFastAirTick = data.getTicks();
                    doubleWrapper.addAndGet(0.6125);
                    bl2 = true;
                }
                else {
                    doubleWrapper.addAndGet(0.36);
                }
                if (data.isFallFlying()) {
                    this.bypassFallbackTicks = 100;
                    doubleWrapper.set(doubleWrapper.get() * 5.0);
                }
                double d5 = n;
                if (this.lastAirTick - this.lastGroundTick < 1 + (n - 1) / 2) {
                    d5 += n * n * n * 0.0018;
                }
                double d6;
                if (bl2) {
                    d6 = 0.0375;
                }
                else {
                    d6 = 0.0175;
                }
                doubleWrapper.addAndGet(d5 * d6);
                if (player.getWalkSpeed() > 0.2) {
                    doubleWrapper.addAndGet(doubleWrapper.get() * (player.getWalkSpeed() - 0.2) * 2.0);
                }
            }
            this.lastSpeed = n;
            if (data.isTakingVelocity()) {
                return;
            }
            final boolean bl3 = data.hasLag();
            final Vector velocity = Plunder.getInstance().getLastVelocity().containsKey(player.getUniqueId()) ? Plunder.getInstance().getLastVelocity().get(player.getUniqueId()).getValue() : null;
            if (velocity != null) {
                doubleWrapper.addAndGet(Math.sqrt(Math.hypot(velocity.getX(), velocity.getZ())));
            }
            if (d > doubleWrapper.get()) {
                final World world = player.getWorld();
                final Location playerLocation3 = player.getLocation();
                final int n2 = data.getTicks();
                final AtomicInteger n3 = new AtomicInteger(this.lastFastAirTick);
                final PlayerInventory playerInventory = player.getInventory();
                if (playerInventory.getBoots() != null && playerInventory.getBoots().containsEnchantment(Enchantment.DEPTH_STRIDER) && data.getLiquidTicks() > 0) {
                    return;
                }
                final boolean bl4 = n2 - 20 < this.lastBlockAboveTick;
                boolean bl5;
                if (!(bl5 = bl4) && data.getUnderBlockTicks() > 0) {
                    bl5 = true;
                    this.lastBlockAboveTick = n2;
                }
                final int n4 = (n2 - 60 < this.lastBypassTick) ? 1 : 0;
                int n5;
                if ((n5 = n4) == 0 && (data.getOnIceTicks() > 0 || (data.getNearSlabTicks() > 0 && data.getStairsTicks() > 0.0))) {
                    n5 = 1;
                    this.lastBypassTick = n2;
                }
                final int n6 = bl3 ? 2 : n3.getAndSet(1);
                if (bl5) {
                    doubleWrapper.addAndGet(0.3 * n3.get());
                }
                if (n5 != 0) {
                    double d7;
                    if (packetPlayInFlying.f()) {
                        d7 = 0.25;
                    }
                    else {
                        d7 = 0.3;
                    }
                    doubleWrapper.addAndGet(d7 * n3.get());
                    this.bypassFallbackTicks = 60;
                }
                if (d > doubleWrapper.get()) {
                    double d8;
                    if (bl3) {
                        d8 = (d - doubleWrapper.get()) * 0.5 + 0.1;
                    }
                    else {
                        d8 = d - doubleWrapper.get();
                    }
                    double d9 = d8 + 0.3;
                    if (System.currentTimeMillis() - data.getLastTeleport() < 1000L) {
                        d9 = 0.15;
                    }
                    this.buffer += d9;
                    if (this.buffer > 5.0) {
                        double d10;
                        if (velocity == null) {
                            d10 = 0.0;
                        }
                        else {
                            d10 = Math.sqrt(Math.hypot(velocity.getX(), velocity.getZ()));
                        }
                        this.logCheat(String.format("DST: %.3f LM %.3f G: %s L: %s V: %s FA: %s", d, doubleWrapper.get(), packetPlayInFlying.f(), bl3, d10, n2 - n3.get()));
                        this.back();
                    }
                }
                else {
                    this.buffer = Math.min(0.0, this.buffer - 0.02);
                }
            }
            else {
                this.buffer = Math.min(0.0, this.buffer - 0.02);
            }
        }
    }
}
