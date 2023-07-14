// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.checks.other.pingspoof;

import gg.noob.plunder.data.PlayerData;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.entity.Player;
import gg.noob.plunder.checks.Check;

public class PingSpoofA extends Check
{
    private int count;
    private int flying;
    
    public PingSpoofA() {
        super("Ping Spoof (A)");
        this.count = 0;
        this.flying = 0;
        this.setBan(false);
    }
    
    @Override
    public void handleReceivedPacket(final Player player, final Packet packet) {
        final PlayerData data = this.getPlayerData();
        if (data == null) {
            return;
        }
        if (packet instanceof PacketPlayInFlying) {
            if (data.isTeleporting() || data.getTicks() < 60) {
                return;
            }
            if (++this.flying > 512) {
                data.lostTransactionPacketsCount = Math.min(0, data.lostTransactionPacketsCount - 100);
                data.lostAsyncTransactionPacketsCount = Math.min(0, data.lostAsyncTransactionPacketsCount - 1);
                data.lostAsyncTransactionTwicePacketsCount = Math.min(0, data.lostAsyncTransactionTwicePacketsCount - 1);
                this.flying = 0;
            }
            if (data.getLostTransactionPacketsCount() > 200) {
                if (++this.count > 305.0 + data.ping / 50.0) {
                    this.logCheat();
                    this.disconnect("Not apply transaction packets");
                }
            }
            else if (data.getLostAsyncTransactionPacketsCount() > 5 || data.getLostAsyncTransactionTwicePacketsCount() > 5) {
                if (++this.count > 305.0 + data.ping / 50.0) {
                    this.logCheat();
                    this.disconnect("Not apply async transaction packets");
                }
            }
            else if (data.getReceivedTransactionCount() > data.getSendTransactionCount() + 5) {
                if (++this.count > 305.0 + data.ping / 50.0) {
                    this.logCheat();
                    this.disconnect("Send too many transaction packets");
                }
            }
            else if (data.getReceivedAsyncTransactionCount() > data.getSendAsyncTransactionCount() + 5) {
                if (++this.count > 305.0 + data.ping / 50.0) {
                    this.logCheat();
                    this.disconnect("Send too many async transaction packets");
                }
            }
            else if (data.getReceivedAsyncTransactionTwiceCount() > data.getSendAsyncTransactionTwiceCount() + 5) {
                if (++this.count > 305.0 + data.ping / 50.0) {
                    this.logCheat();
                    this.disconnect("Send too many async transaction twice packets");
                }
            }
            else {
                this.count = 0;
            }
        }
    }
}
