// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.checks.combat.autoclicker;

import java.util.AbstractMap;
import org.bukkit.ChatColor;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.entity.Player;
import java.util.Map;
import gg.noob.plunder.checks.Check;

public class AutoClickerA extends Check
{
    public Map.Entry<Integer, Long> attackTicks;
    public int maxCPS;
    public double vl;
    
    public AutoClickerA() {
        super("AutoClicker (A)");
        this.attackTicks = null;
        this.maxCPS = 0;
        this.vl = 0.0;
        this.setMaxViolations(10);
        this.setBan(false);
    }
    
    @Override
    public void handleReceivedPacket(final Player player, final Packet packet) {
        if (packet instanceof PacketPlayInUseEntity) {
            final PacketPlayInUseEntity packetPlayInUseEntity = (PacketPlayInUseEntity)packet;
            if (packetPlayInUseEntity.a() != PacketPlayInUseEntity.EnumEntityUseAction.ATTACK) {
                return;
            }
            int count = 0;
            long time = System.currentTimeMillis();
            if (this.attackTicks != null) {
                count = this.attackTicks.getKey();
                time = this.attackTicks.getValue();
            }
            ++count;
            if (this.attackTicks != null && System.currentTimeMillis() - time > 1000L) {
                if (count > this.maxCPS) {
                    this.maxCPS = count;
                }
                if (count > 19) {
                    this.logCheat(player, "Click Count: " + count);
                    final double vl = this.vl + 1.0;
                    this.vl = vl;
                    if (vl > 5.0) {
                        this.kick(ChatColor.RED + "Your are clicking too fast!");
                    }
                }
                else {
                    this.vl -= 0.01;
                    if (this.vl < 0.0) {
                        this.vl = 0.0;
                    }
                }
                count = 0;
                time = System.currentTimeMillis();
            }
            this.attackTicks = new AbstractMap.SimpleEntry<Integer, Long>(count, time);
        }
    }
}
