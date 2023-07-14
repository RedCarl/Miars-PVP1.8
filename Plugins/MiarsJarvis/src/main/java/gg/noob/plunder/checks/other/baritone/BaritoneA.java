// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.checks.other.baritone;

import gg.noob.plunder.util.MathUtils;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import org.bukkit.Location;
import gg.noob.plunder.data.PlayerData;
import org.bukkit.GameMode;
import net.minecraft.server.v1_8_R3.PacketPlayInBlockDig;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.entity.Player;
import gg.noob.plunder.checks.Check;

public class BaritoneA extends Check
{
    private double lastDivisor;
    private float lastDeltaPitch;
    private boolean checking;
    private boolean waitForBreak;
    private int buffer;
    
    public BaritoneA() {
        super("Baritone (A)");
        this.lastDeltaPitch = 0.0f;
        this.buffer = 0;
    }
    
    @Override
    public void handleReceivedPacket(final Player player, final Packet packet) {
        final PlayerData data = this.getPlayerData();
        if (packet instanceof PacketPlayInBlockDig) {
            final PacketPlayInBlockDig packetPlayInBlockDig = (PacketPlayInBlockDig)packet;
            final PacketPlayInBlockDig.EnumPlayerDigType digType = packetPlayInBlockDig.c();
            if (player.getGameMode() == GameMode.CREATIVE || data.isTeleporting(3)) {
                return;
            }
            if (digType == PacketPlayInBlockDig.EnumPlayerDigType.START_DESTROY_BLOCK) {
                this.waitForBreak = false;
                if (this.checking) {
                    if ((this.lastDivisor > 0.0 && this.lastDivisor < 0.009) || this.lastDivisor > 10.0) {
                        this.waitForBreak = true;
                    }
                    else {
                        this.buffer = Math.max(0, this.buffer - 2);
                    }
                }
                this.checking = false;
            }
            else if (digType == PacketPlayInBlockDig.EnumPlayerDigType.STOP_DESTROY_BLOCK) {
                if (this.waitForBreak && this.buffer++ > 10) {
                    this.logCheat("Divisor: " + this.lastDivisor);
                }
                this.waitForBreak = false;
            }
        }
    }
    
    @Override
    public void handleRotationUpdate(final Player player, final Location to, final Location from, final PacketPlayInFlying packetPlayInFlying) {
        final PlayerData data = this.getPlayerData();
        if (data.isTeleporting(3)) {
            return;
        }
        final float deltaPitch = Math.abs(to.getPitch() - from.getPitch());
        if (deltaPitch > 0.1 && this.lastDeltaPitch > 0.1) {
            final long expanded = (long)(deltaPitch * MathUtils.EXPANDER);
            final long lastExpanded = (long)(this.lastDeltaPitch * MathUtils.EXPANDER);
            final long gcd = MathUtils.getGcd(expanded, lastExpanded);
            this.lastDivisor = gcd / MathUtils.EXPANDER;
            this.checking = true;
        }
        this.lastDeltaPitch = deltaPitch;
    }
}
