// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.checks.other.scaffold;

import gg.noob.plunder.data.PlayerData;
import gg.noob.plunder.util.MathUtils;
import org.bukkit.util.Vector;
import net.minecraft.server.v1_8_R3.EnumDirection;
import net.minecraft.server.v1_8_R3.PacketPlayInBlockPlace;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.entity.Player;
import gg.noob.plunder.checks.Check;

public class ScaffoldK extends Check
{
    private int buffer;
    
    public ScaffoldK() {
        super("Scaffold (K)");
        this.buffer = 0;
    }
    
    @Override
    public void handleReceivedPacket(final Player player, final Packet packet) {
        final PlayerData data = this.getPlayerData();
        if (packet instanceof PacketPlayInBlockPlace) {
            final PacketPlayInBlockPlace packetPlayInBlockPlace = (PacketPlayInBlockPlace)packet;
            final EnumDirection face = EnumDirection.fromType1(packetPlayInBlockPlace.getFace());
            final Vector dir = new Vector(face.getAdjacentX(), 0, face.getAdjacentZ());
            final Vector opposite = new Vector(face.opposite().getAdjacentX(), 0, face.opposite().getAdjacentZ());
            final Vector delta = new Vector(data.getDeltaX(), data.getDeltaY(), data.getDeltaZ());
            final double dist = delta.distance(dir);
            final double dist2 = opposite.distance(MathUtils.getDirection(data.getLastMoveLoc().getYaw(), data.getLastMoveLoc().getPitch()).setY(0));
            final boolean check = dist <= 1.0 && dist > 0.7 && dist2 >= 0.5 && dist2 < 1.0;
            if (check && face.getAdjacentY() == 0 && data.isBridge() && player.getItemInHand().getType().isBlock() && data.getSprintTicks() > 0) {
                if ((this.buffer += 4) > 15) {
                    this.logCheat("dist=%.3f dist2=%.3f placeVec=%s", dist, dist2, dir.toString());
                    this.buffer = 14;
                }
            }
            else if (this.buffer > 0) {
                --this.buffer;
            }
        }
    }
}
