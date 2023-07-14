// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.checks.other.scaffold;

import gg.noob.plunder.data.PlayerData;
import org.bukkit.Material;
import gg.noob.plunder.util.BlockUtils;
import net.minecraft.server.v1_8_R3.PacketPlayInBlockPlace;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.entity.Player;
import gg.noob.plunder.checks.Check;

public class ScaffoldD extends Check
{
    private double vl;
    
    public ScaffoldD() {
        super("Scaffold (D)");
        this.vl = 0.0;
        this.setMaxViolations(3);
    }
    
    @Override
    public void handleReceivedPacket(final Player player, final Packet packet) {
        final PlayerData data = this.getPlayerData();
        if (data == null) {
            return;
        }
        if (packet instanceof PacketPlayInBlockPlace) {
            final PacketPlayInBlockPlace packetPlayInBlockPlace = (PacketPlayInBlockPlace)packet;
            if (data.getTicks() < 60 || packetPlayInBlockPlace.getFace() == 255) {
                this.vl = 0.0;
                return;
            }
            final float vecX = packetPlayInBlockPlace.d();
            final float vecY = packetPlayInBlockPlace.e();
            final float vecZ = packetPlayInBlockPlace.f();
            final int faceInt = packetPlayInBlockPlace.getFace();
            final double yaw = Math.abs(data.getLastMoveLoc().getYaw() - data.getLastLastMoveLoc().getYaw());
            if (yaw > 0.0 && BlockUtils.getBlockType(player.getWorld(), packetPlayInBlockPlace.a()) == Material.AIR) {
                if (faceInt >= 0 && faceInt <= 3) {
                    if ((vecX == 0.5 && vecY == 0.5) || (vecZ == 0.5 && vecY == 0.5)) {
                        final double vl = this.vl + 1.0;
                        this.vl = vl;
                        if (vl > 4.0) {
                            this.logCheat();
                        }
                    }
                    else {
                        this.vl -= Math.min(this.vl, 0.5);
                    }
                }
                else {
                    this.vl -= Math.min(this.vl, 0.7200000286102295);
                }
            }
        }
    }
}
