// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.checks.other.badpackets;

import gg.noob.plunder.util.MathUtils;
import org.bukkit.Location;
import gg.noob.plunder.data.PlayerData;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.entity.Player;
import gg.noob.plunder.checks.Check;

public class BadPacketsE extends Check
{
    public BadPacketsE() {
        super("Bad Packets (E)");
        this.setBan(false);
        this.setMaxViolations(1);
        this.setLogData(false);
    }
    
    @Override
    public boolean handleReceivedPacketCanceled(final Player player, final Packet packet) {
        final PlayerData data = this.getPlayerData();
        boolean cancel = false;
        if (packet instanceof PacketPlayInFlying) {
            final PacketPlayInFlying packetPlayInFlying = (PacketPlayInFlying)packet;
            if (Math.abs(packetPlayInFlying.b()) > 1.0E9) {
                this.disconnect("Invalid Y");
                cancel = true;
            }
            if (Double.isInfinite(packetPlayInFlying.a()) || Double.isInfinite(packetPlayInFlying.b()) || Double.isInfinite(packetPlayInFlying.c()) || Double.isInfinite(packetPlayInFlying.d()) || Float.isInfinite(packetPlayInFlying.d()) || Float.isInfinite(packetPlayInFlying.e()) || Double.isNaN(packetPlayInFlying.a()) || Double.isNaN(packetPlayInFlying.b()) || Double.isNaN(packetPlayInFlying.c()) || Float.isNaN(packetPlayInFlying.d()) || Float.isNaN(packetPlayInFlying.e())) {
                this.disconnect("Invalid Location");
                cancel = true;
            }
        }
        return cancel;
    }
    
    @Override
    public boolean handleLocationUpdateCanceled(final Player player, final Location to, final Location from, final PacketPlayInFlying packetPlayInFlying) {
        final PlayerData data = this.getPlayerData();
        boolean cancel = false;
        final double gcd = MathUtils.gcd((float)to.getY(), (float)from.getY());
        if (String.valueOf(gcd).contains("E")) {
            cancel = true;
            this.disconnect("Hack-Pack From Walmart");
        }
        return cancel;
    }
}
