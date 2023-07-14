// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.checks.other.pingspoof;

import gg.noob.plunder.data.PlayerData;
import java.util.concurrent.TimeUnit;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.entity.Player;
import gg.noob.plunder.checks.Check;

public class PingSpoofB extends Check
{
    private int count;
    
    public PingSpoofB() {
        super("Ping Spoof (B)");
        this.count = 0;
        this.setBan(false);
    }
    
    @Override
    public void handleReceivedPacket(final Player player, final Packet packet) {
        final PlayerData data = this.getPlayerData();
        if (data == null) {
            return;
        }
        if (packet instanceof PacketPlayInFlying) {
            if (data.getTicks() < 60 || player.isDead()) {
                return;
            }
            final int sendKeepSize = data.sendKeepAliveCount;
            final int receivedKeepSize = data.receivedKeepAliveCount;
            if (receivedKeepSize > sendKeepSize + 5) {
                if (++this.count > 10) {
                    this.logCheat();
                    this.disconnect("Send too many keep alive packets");
                }
            }
            else if (System.currentTimeMillis() - data.getLastReceivedKeepAlive() > TimeUnit.SECONDS.toMillis(7L)) {
                if (++this.count > 10) {
                    this.logCheat();
                    this.disconnect("Not apply keep alive packets");
                }
            }
            else {
                this.count = 0;
            }
        }
    }
}
