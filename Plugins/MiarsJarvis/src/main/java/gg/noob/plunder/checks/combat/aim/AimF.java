// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.checks.combat.aim;

import gg.noob.lib.util.LinkedList;
import gg.noob.plunder.checks.Check;
import gg.noob.plunder.data.PlayerData;
import gg.noob.plunder.util.MathUtils;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Deque;

public class AimF extends Check
{
    private Deque<Float> samples;
    private double buffer;
    
    public AimF() {
        super("Aim (F)");
        this.samples = (Deque<Float>)new LinkedList();
        this.buffer = 0.0;
        this.setMaxViolations(5);
    }
    
    @Override
    public void handleRotationUpdate(final Player player, final Location to, final Location from, final PacketPlayInFlying packetPlayInFlying) {
        final PlayerData data = this.getPlayerData();
        if (data.isTeleporting(3)) {
            return;
        }
        final float deltaYaw = Math.abs(to.getYaw() - from.getYaw());
        final float deltaPitch = Math.abs(to.getPitch() - from.getPitch());
        final boolean attacking = System.currentTimeMillis() - data.getLastAttackTime() < 500L;
        if (deltaYaw > 0.0 && deltaPitch > 0.0 && deltaYaw < 30.0f && deltaPitch < 30.0f && attacking) {
            this.samples.add(deltaPitch);
        }
        if (this.samples.size() == 120) {
            final int distinct = MathUtils.getDistinct(this.samples);
            final int duplicates = this.samples.size() - distinct;
            final double average = this.samples.stream().mapToDouble(Float::floatValue).average().orElse(0.0);
            if (duplicates <= 9 && average < 30.0 && distinct > 130) {
                final double buffer = this.buffer + 1.0;
                this.buffer = buffer;
                if (buffer > 3.0) {
                    this.logCheat("Duplicates: " + duplicates);
                    this.buffer = 0.0;
                }
            }
            else {
                this.buffer = Math.max(this.buffer - 3.0, 0.0);
            }
            this.samples.clear();
        }
    }
}
