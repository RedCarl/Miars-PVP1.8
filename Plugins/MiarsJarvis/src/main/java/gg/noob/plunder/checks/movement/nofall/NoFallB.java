// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.checks.movement.nofall;

import gg.noob.plunder.data.PlayerData;
import org.bukkit.GameMode;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import gg.noob.plunder.checks.Check;

public class NoFallB extends Check
{
    private static final double divisor = 0.015625;
    private int airBuffer;
    private int groundBuffer;
    
    public NoFallB() {
        super("NoFall (B)");
        this.airBuffer = 0;
        this.groundBuffer = 0;
        this.setViolationsRemoveOneTime(1000L);
    }
    
    @Override
    public void handleLocationUpdate(final Player player, final Location to, final Location from, final PacketPlayInFlying packetPlayInFlying) {
        final PlayerData data = this.getPlayerData();
        if (data == null) {
            return;
        }
        if (System.currentTimeMillis() - data.getLastMove() < 100L || player.getAllowFlight() || data.isBouncedOnSlime() || player.getGameMode() == GameMode.CREATIVE || data.getNearClimbTicks() > 0 || data.isTeleporting()) {
            if (this.groundBuffer > 0) {
                --this.groundBuffer;
            }
            if (this.airBuffer > 0) {
                --this.airBuffer;
            }
            return;
        }
        if (packetPlayInFlying.f() && !player.isOnGround() && !player.isInsideVehicle() && !data.isPlacingBlocks() && data.getUnderBlockTicks() <= 0 && data.getSolidBlockBehindTicks() <= 0) {
            this.groundBuffer += 2;
            if (this.groundBuffer > 14) {
                this.groundBuffer = 14;
                this.logCheat();
            }
        }
        else if (this.groundBuffer > 0) {
            --this.groundBuffer;
        }
        final boolean dground = Math.abs(to.getY()) % 0.015625 < 1.0E-4;
        if (!packetPlayInFlying.f() && data.getOnWebTicks() <= 0 && !player.isInsideVehicle() && (player.isOnGround() || data.getUnderBlockTicks() <= 0) && dground && data.getNearSlabTicks() <= 0 && data.getStairsTicks() <= 0.0 && data.getNearClimbTicks() <= 0 && System.currentTimeMillis() - data.getLastTeleport() > 1000L) {
            if ((this.airBuffer += 10) > 30) {
                this.logCheat();
                this.back();
            }
        }
        else if (this.airBuffer > 0) {
            this.airBuffer -= 4;
        }
    }
}
