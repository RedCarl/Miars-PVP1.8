// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.checks.combat.hitbox;

import org.bukkit.GameMode;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.entity.Player;
import gg.noob.plunder.checks.Check;

public class HitBoxB extends Check
{
    public HitBoxB() {
        super("HitBox (B)");
    }
    
    @Override
    public boolean handleReceivedPacketCanceled(final Player player, final Packet packet) {
        boolean cancel = false;
        if (packet instanceof PacketPlayInUseEntity && ((PacketPlayInUseEntity)packet).a((World)((CraftWorld)player.getWorld()).getHandle()) instanceof Player) {
            final PacketPlayInUseEntity packetPlayInUseEntity = (PacketPlayInUseEntity)packet;
            final Player target = (Player)packetPlayInUseEntity.a((World)((CraftWorld)player.getWorld()).getHandle());
            if (target.getGameMode() == GameMode.CREATIVE) {
                return false;
            }
            final double x = packetPlayInUseEntity.b().a;
            final double y = packetPlayInUseEntity.b().b;
            final double z = packetPlayInUseEntity.b().c;
            if (Math.abs(x) > 0.4004004004004004 || Math.abs(y) > 0.4004004004004004 || Math.abs(z) > 0.4004004004004004) {
                this.logCheat(player, "X: " + x + " Y: " + y + " Z: " + z);
                cancel = true;
            }
        }
        return cancel;
    }
}
