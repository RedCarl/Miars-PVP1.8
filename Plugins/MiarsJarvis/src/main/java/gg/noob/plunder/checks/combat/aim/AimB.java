// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.checks.combat.aim;

import gg.noob.plunder.data.PlayerData;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import gg.noob.plunder.checks.Check;

public class AimB extends Check
{
    private int streak;
    
    public AimB() {
        super("Aim (B)");
        this.setMaxViolations(5);
    }
    
    @Override
    public void handleRotationUpdate(final Player player, final Location to, final Location from, final PacketPlayInFlying packetPlayInFlying) {
        final PlayerData data = this.getPlayerData();
        final float deltaYaw = Math.abs(to.getYaw() - from.getYaw());
        final float deltaPitch = Math.abs(to.getPitch() - from.getPitch());
        if (deltaYaw > 0.0 && deltaYaw < 30.0f) {
            final boolean attack = System.currentTimeMillis() - data.getLastAttackTime() < 500L;
            if (deltaPitch == 0.0 && attack) {
                if (++this.streak > 10.0f) {
                    this.logCheat();
                    this.streak = 0;
                }
            }
            else {
                this.streak = 0;
            }
        }
        else {
            this.streak = 0;
        }
    }
}
