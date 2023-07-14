// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.checks.other.badpackets;

import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import gg.noob.plunder.checks.Check;

public class BadPacketsD extends Check
{
    public BadPacketsD() {
        super("Bad Packets (D)");
        this.setMaxViolations(1);
    }
    
    @Override
    public boolean handleRotationUpdateCanceled(final Player player, final Location to, final Location from, final PacketPlayInFlying packetPlayInFlying) {
        final double x = to.getPitch();
        if (x > 90.0 || x < -90.0) {
            this.logCheat("Head went back too far Pitch: " + x);
            return true;
        }
        return false;
    }
}
