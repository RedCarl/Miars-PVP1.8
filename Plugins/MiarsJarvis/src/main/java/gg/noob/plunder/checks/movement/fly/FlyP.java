// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.checks.movement.fly;

import gg.noob.plunder.data.PlayerData;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.entity.Player;
import gg.noob.plunder.checks.Check;

public class FlyP extends Check
{
    private int buffer;
    private double lastY;
    
    public FlyP() {
        super("Fly (P)");
        this.buffer = 0;
        this.lastY = 0.0;
    }
    
    @Override
    public void handleReceivedPacket(final Player player, final Packet packet) {
        if (packet instanceof PacketPlayInFlying) {
            final PacketPlayInFlying packetPlayInFlying = (PacketPlayInFlying)packet;
            final PlayerData data = this.getPlayerData();
            final double deltaY = packetPlayInFlying.b() - this.lastY;
            if (player.getAllowFlight() || data.isBouncedOnSlime() || player.isInsideVehicle() || data.getLiquidTicks() > 0 || data.getOnWebTicks() > 0 || data.getNearClimbTicks() > 0 || data.isBlockBelow() || (packetPlayInFlying.f() && data.getGroundTicks() < 3) || player.getAllowFlight() || data.isTakingVelocity(1) || data.isTeleporting() || deltaY != 0.0 || data.getLiquidTicks() > 0 || data.getNearClimbTicks() > 16) {
                this.buffer = 0;
            }
            if (++this.buffer > 20) {
                this.disconnect("Flying in the air");
            }
            this.lastY = packetPlayInFlying.b();
        }
    }
}
