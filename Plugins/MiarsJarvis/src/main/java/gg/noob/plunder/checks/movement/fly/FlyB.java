// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.checks.movement.fly;

import java.util.Iterator;
import gg.noob.plunder.data.PlayerData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import java.util.Map;
import gg.noob.plunder.Plunder;
import org.bukkit.Location;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.entity.Player;
import gg.noob.plunder.checks.Check;

public class FlyB extends Check
{
    private int count;
    
    public FlyB() {
        super("Fly (B)");
        this.count = 0;
    }
    
    @Override
    public void handleReceivedPacket(final Player player, final Packet packet) {
        final PlayerData data = this.getPlayerData();
        if (packet instanceof PacketPlayInFlying) {
            final PacketPlayInFlying packetPlayInFlying = (PacketPlayInFlying)packet;
            final Location to = new Location(player.getWorld(), packetPlayInFlying.a(), packetPlayInFlying.b(), packetPlayInFlying.c(), packetPlayInFlying.d(), packetPlayInFlying.e());
            final Location from = player.getLocation();
            if (data.getNearClimbTicks() <= 0 && player.getMaximumNoDamageTicks() >= 10 && !data.isTakingVelocity() && data.getLiquidTicks() > 0 && player.getAllowFlight() && player.isInsideVehicle() && player.isOnGround() && player.getVelocity().getY() >= 0.0 && to.getY() <= from.getY()) {
                if (!Plunder.getInstance().getLastGround().containsKey(player.getUniqueId())) {
                    return;
                }
                final double distance = to.getY() - Plunder.getInstance().getLastGround().get(player.getUniqueId()).getValue().getY();
                double limit = 2.0;
                if (player.hasPotionEffect(PotionEffectType.JUMP)) {
                    for (final PotionEffect effect : player.getActivePotionEffects()) {
                        if (effect.getType().equals((Object)PotionEffectType.JUMP)) {
                            final int level = effect.getAmplifier() + 1;
                            limit += Math.pow(level + 4.2, 2.0) / 16.0;
                            break;
                        }
                    }
                }
                if (distance > limit) {
                    if (++this.count >= 10) {
                        this.logCheat(player, "Distance: " + distance);
                        this.back();
                    }
                }
                else {
                    this.count = 0;
                }
            }
            else {
                this.count = 0;
            }
        }
    }
}
