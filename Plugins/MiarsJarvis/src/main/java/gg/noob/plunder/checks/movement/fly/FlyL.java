// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.checks.movement.fly;

import java.io.IOException;
import net.minecraft.server.v1_8_R3.PacketDataSerializer;
import io.netty.buffer.Unpooled;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityVelocity;
import net.minecraft.server.v1_8_R3.Packet;
import gg.noob.plunder.data.PlayerData;
import gg.noob.plunder.util.PlayerUtils;
import org.bukkit.GameMode;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import gg.noob.plunder.checks.Check;

public class FlyL extends Check
{
    private double vertical;
    private double limit;
    private double velocityY;
    private double slimeY;
    private double lDeltaY;
    private boolean pistonBelow;
    
    public FlyL() {
        super("Fly (L)");
    }
    
    @Override
    public void handleLocationUpdate(final Player player, final Location to, final Location from, final PacketPlayInFlying packetPlayInFlying) {
        final PlayerData data = this.getPlayerData();
        final double deltaY = to.getY() - from.getY();
        if (data.getLiquidTicks() > 0 || data.isTeleporting() || player.getAllowFlight() || player.getGameMode() == GameMode.CREATIVE || data.getNearClimbTicks() > 0 || player.getAllowFlight() || data.isBouncedOnSlime() || System.currentTimeMillis() - data.getLastBypass() < 1000L || PlayerUtils.hasInvalidJumpBoost(player) || player.isInsideVehicle() || data.isPlacingBlocks() || data.getLiquidTicks() > 0 || data.getOnWebTicks() > 0) {
            this.vertical = 0.0;
            this.limit = Double.MAX_VALUE;
            return;
        }
        if (data.isServerGround() || packetPlayInFlying.f()) {
            this.vertical = 0.0;
            this.pistonBelow = (data.getPistonTicks() > 0.0 && data.getAboveSlimeTicks() > 0);
            this.limit = PlayerUtils.getTotalHeight(PlayerUtils.getJumpHeight(player));
            if (!data.isTakingVelocity(3)) {
                this.velocityY = 0.0;
            }
            if (data.getAboveSlimeTicks() > 16 && packetPlayInFlying.f()) {
                this.slimeY = PlayerUtils.getTotalHeight((float)Math.abs(this.lDeltaY));
            }
            else if (data.getAboveSlimeTicks() > 14) {
                this.slimeY = 0.0;
            }
        }
        else {
            this.vertical += deltaY;
            final double limit = (this.limit + this.slimeY + this.velocityY) * 1.6;
            if (this.vertical > limit && !this.pistonBelow && player.getMaximumNoDamageTicks() > 5) {
                this.logCheat("Vertical: " + this.vertical + " Limit: " + limit);
                this.back();
            }
        }
        this.lDeltaY = deltaY;
    }
    
    @Override
    public void handleSentPacket(final Player player, final Packet packet) {
        if (packet instanceof PacketPlayOutEntityVelocity) {
            final PacketPlayOutEntityVelocity packetPlayOutEntityVelocity = (PacketPlayOutEntityVelocity)packet;
            final PacketDataSerializer serializer = new PacketDataSerializer(Unpooled.buffer(0));
            try {
                packet.b(serializer);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            final int id = serializer.e();
            if (id != player.getEntityId()) {
                return;
            }
            final double x = serializer.readShort() / 8000.0;
            final double y = serializer.readShort() / 8000.0;
            final double z = serializer.readShort() / 8000.0;
            this.velocityY = PlayerUtils.getTotalHeight((float)y);
        }
    }
}
