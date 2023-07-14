// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.checks.other.scaffold;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftMagicNumbers;
import net.minecraft.server.v1_8_R3.PacketPlayInBlockPlace;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.entity.Player;
import gg.noob.plunder.checks.Check;

public class ScaffoldA extends Check
{
    public ScaffoldA() {
        super("Scaffold (A)");
        this.setMaxViolations(3);
    }
    
    @Override
    public void handleReceivedPacket(final Player player, final Packet packet) {
        if (packet instanceof PacketPlayInBlockPlace) {
            final PacketPlayInBlockPlace packetPlayInBlockPlace = (PacketPlayInBlockPlace)packet;
            final int face = packetPlayInBlockPlace.getFace();
            if (face != 255 && packetPlayInBlockPlace.getItemStack() != null) {
                final Material type = CraftMagicNumbers.getMaterial(packetPlayInBlockPlace.getItemStack().getItem());
                if (type != Material.AIR) {
                    final float x = packetPlayInBlockPlace.d();
                    final float y = packetPlayInBlockPlace.e();
                    final float z = packetPlayInBlockPlace.f();
                    if (x > 1.0f || y > 1.0f || z > 1.0f) {
                        this.logCheat(String.format("X: %.2f Y: %.2f Z: %.2f", x, y, z));
                    }
                }
            }
        }
    }
}
