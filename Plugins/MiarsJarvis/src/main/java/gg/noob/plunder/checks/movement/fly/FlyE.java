// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.checks.movement.fly;

import gg.noob.plunder.data.PlayerData;
import gg.noob.plunder.util.MathUtils;
import gg.noob.plunder.util.PlayerUtils;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.GameMode;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import gg.noob.plunder.checks.Check;

public class FlyE extends Check
{
    private Double lastY;
    private boolean jumping;
    private int jumped;
    private int lastBypassTick;
    
    public FlyE() {
        super("Fly (E)");
        this.lastY = null;
        this.jumping = false;
        this.lastBypassTick = -10;
    }
    
    @Override
    public void handleLocationUpdate(final Player player, final Location to, final Location from, final PacketPlayInFlying packetPlayInFlying) {
        final PlayerData data = this.getPlayerData();
        if (!packetPlayInFlying.f() && !data.isTakingVelocity() && data.getTicks() - data.getLastTeleportTicks() > data.getPingTicks() && data.getTicks() - data.getLastBlockPlacedTicks() < 10L && data.getLiquidTicks() <= 0 && System.currentTimeMillis() - data.getLastTeleport() > 1000L && !player.getAllowFlight() && player.getGameMode() == GameMode.SURVIVAL && data.getTicks() - 40 > this.lastBypassTick && !data.isFallFlying() && !player.isInsideVehicle()) {
            if (this.lastY != null) {
                if (this.jumping && to.getY() < this.lastY) {
                    if (this.jumped++ > 1 && PlayerUtils.getPotionEffectLevel(player, PotionEffectType.JUMP) == 0) {
                        final int totalTicks = data.getTicks();
                        if (data.getStairsTicks() <= 0.0 && data.getNearSlabTicks() <= 0 && data.getLiquidTicks() <= 0 && !data.isBouncedOnSlime() && data.getNearClimbTicks() <= 0 && data.getOnWebTicks() <= 0 && data.getOnIceTicks() <= 0 && data.getFenceTicks() <= 0) {
                            if (!data.isPlacingBlocks()) {
                                this.logCheat();
                            }
                            this.back();
                        }
                        else {
                            this.jumped = 0;
                            this.lastBypassTick = totalTicks;
                        }
                    }
                    this.jumping = false;
                }
                else if (to.getY() > this.lastY) {
                    this.jumping = true;
                }
            }
        }
        else if (MathUtils.onGround(to.getY()) || (to.getY() - 0.41999998688697815) % 1.0 > 1.0E-15) {
            this.jumped = 0;
            this.jumping = false;
        }
        if (packetPlayInFlying.g()) {
            this.lastY = to.getY();
        }
    }
}
