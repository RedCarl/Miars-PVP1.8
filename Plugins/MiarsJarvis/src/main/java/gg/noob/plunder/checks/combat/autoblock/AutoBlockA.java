// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.checks.combat.autoblock;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftHumanEntity;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import gg.noob.plunder.checks.Check;

public class AutoBlockA extends Check
{
    private long lastBlockHit;
    
    public AutoBlockA() {
        super("AutoBlock (A)");
        this.lastBlockHit = -1L;
        this.setMaxViolations(11);
    }
    
    @Override
    public void handleLocationUpdate(final Player player, final Location to, final Location from, final PacketPlayInFlying packetPlayInFlying) {
        if (player.isBlocking()) {
            if (this.lastBlockHit == -1L) {
                this.lastBlockHit = System.currentTimeMillis();
            }
        }
        else {
            this.lastBlockHit = -1L;
        }
    }
    
    @Override
    public boolean handleReceivedPacketCanceled(final Player player, final Packet packet) {
        if (packet instanceof PacketPlayInUseEntity) {
            final PacketPlayInUseEntity packetPlayInUseEntity = (PacketPlayInUseEntity)packet;
            if (packetPlayInUseEntity.a() != PacketPlayInUseEntity.EnumEntityUseAction.ATTACK) {
                return false;
            }
            if (this.lastBlockHit != -1L && player.isBlocking()) {
                final long time = this.lastBlockHit;
                if (System.currentTimeMillis() - time > 1000L) {
                    ((CraftHumanEntity)player).getHandle().g = null;
                    this.logCheat(player);
                    return true;
                }
            }
        }
        return false;
    }
}
