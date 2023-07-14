// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.checks.movement.speed;

import gg.noob.plunder.data.PlayerData;
import org.bukkit.GameMode;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import gg.noob.plunder.checks.Check;

public class SpeedL extends Check
{
    public SpeedL() {
        super("Speed (L)");
    }
    
    @Override
    public void handleLocationUpdate(final Player player, final Location to, final Location from, final PacketPlayInFlying packetPlayInFlying) {
        final PlayerData data = this.getPlayerData();
        if (data == null) {
            return;
        }
        if (player.isInsideVehicle() || System.currentTimeMillis() - data.getLastTeleport() < 1000L || player.getGameMode().equals((Object)GameMode.CREATIVE) || player.getGameMode().equals((Object)GameMode.SPECTATOR)) {
            return;
        }
        final float deltaY = (float)(to.getY() - from.getY());
        if (deltaY < -3.92 || deltaY > 256.0f) {
            this.logCheat("DeltaY: " + deltaY);
            this.back();
        }
    }
}
