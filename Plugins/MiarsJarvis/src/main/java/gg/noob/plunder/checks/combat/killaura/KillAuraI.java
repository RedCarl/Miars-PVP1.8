// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.checks.combat.killaura;

import gg.noob.plunder.data.PlayerData;
import org.bukkit.GameMode;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import gg.noob.plunder.checks.Check;

public class KillAuraI extends Check
{
    private int buffer;
    
    public KillAuraI() {
        super("KillAura (I)");
        this.buffer = 0;
        this.setMaxViolations(5);
    }
    
    @Override
    public void handleRotationUpdate(final Player player, final Location to, final Location from, final PacketPlayInFlying packetPlayInFlying) {
        final PlayerData data = this.getPlayerData();
        if (data == null) {
            return;
        }
        if (player.getGameMode().equals((Object)GameMode.SPECTATOR)) {
            return;
        }
        if (packetPlayInFlying.h() && System.currentTimeMillis() - data.lastAttackTime < 500L) {
            final float pitch = to.getPitch();
            if (pitch == 0.0f && System.currentTimeMillis() - data.getLastTeleport() > 1000L) {
                if (this.buffer++ > 0) {
                    this.logCheat();
                    this.buffer = 0;
                }
            }
            else {
                this.buffer = 0;
            }
        }
    }
}
