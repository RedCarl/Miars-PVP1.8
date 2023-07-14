// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.checks.movement.nofall;

import gg.noob.plunder.data.PlayerData;
import gg.noob.plunder.util.PlayerUtils;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.entity.Player;
import gg.noob.plunder.checks.Check;

public class NoFallD extends Check
{
    private int buffer;
    
    public NoFallD() {
        super("No Fall (D)");
        this.buffer = 0;
    }
    
    @Override
    public void handleReceivedPacket(final Player player, final Packet packet) {
        if (packet instanceof PacketPlayInFlying) {
            final PacketPlayInFlying packetPlayInFlying = (PacketPlayInFlying)packet;
            final PlayerData data = this.getPlayerData();
            if (player.getAllowFlight() || data.isTeleporting() || PlayerUtils.checkMovement(player) || data.isDoingBlockUpdate() || data.isPlacingBlocks()) {
                return;
            }
            final boolean mathGround = packetPlayInFlying.b() % 0.015625 == 0.0;
            if (data.getAirTicks() == 0) {
                this.buffer = 0;
            }
            if (mathGround && packetPlayInFlying.f() && data.getAirTicks() > 10 && this.buffer++ > 0) {
                this.logCheat();
            }
        }
    }
}
