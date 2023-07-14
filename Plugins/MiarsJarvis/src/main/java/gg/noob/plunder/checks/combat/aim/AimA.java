// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.checks.combat.aim;

import gg.noob.plunder.data.PlayerData;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import gg.noob.plunder.checks.Check;

public class AimA extends Check
{
    private int verbose;
    private double lastDeltaPitch;
    
    public AimA() {
        super("Aim (A)");
        this.setMaxViolations(5);
    }
    
    @Override
    public void handleRotationUpdate(final Player player, final Location to, final Location from, final PacketPlayInFlying packetPlayInFlying) {
        final PlayerData data = this.getPlayerData();
        if (System.currentTimeMillis() - data.getLastAttackTime() > 500L) {
            return;
        }
        final double pitch = Math.abs(to.getPitch() - from.getPitch());
        final double deltaPitch = this.lastDeltaPitch;
        this.lastDeltaPitch = pitch;
        final double pitchAcceleration = Math.abs(pitch - deltaPitch);
        final double offset = Math.pow(2.0, 24.0);
        final double gcd = (double)this.gcd((long)(pitch * offset), (long)(deltaPitch * offset));
        final double simple = gcd / offset;
        final double magic = pitch % simple;
        if (pitch > 0.0 && magic > 1.0E-4 && pitchAcceleration > 2.0 && simple < 0.006 && simple > 0.0) {
            if (this.verbose++ > 3) {
                this.logCheat("GCD: " + simple + " Accel: " + pitchAcceleration + " Magic: " + magic);
                this.verbose = 0;
            }
        }
        else {
            this.verbose = 0;
        }
    }
    
    private long gcd(final long a, final long b) {
        if (b <= 16384L) {
            return a;
        }
        return this.gcd(b, a % b);
    }
}
