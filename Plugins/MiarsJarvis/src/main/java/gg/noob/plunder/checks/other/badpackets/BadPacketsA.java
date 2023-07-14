// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.checks.other.badpackets;

import net.minecraft.server.v1_8_R3.PacketPlayInBlockPlace;
import net.minecraft.server.v1_8_R3.PacketPlayInHeldItemSlot;
import java.util.AbstractMap;
import net.minecraft.server.v1_8_R3.PacketPlayInArmAnimation;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.UUID;
import java.util.Map;
import gg.noob.plunder.checks.Check;

public class BadPacketsA extends Check
{
    public Map<UUID, Map.Entry<Integer, Long>> crashTicks;
    public Map<UUID, Map.Entry<Integer, Long>> crash2Ticks;
    public Map<UUID, Map.Entry<Integer, Long>> crash3Ticks;
    
    public BadPacketsA() {
        super("Bad Packets (A)");
        this.setBan(false);
        this.crashTicks = new HashMap<UUID, Map.Entry<Integer, Long>>();
        this.crash2Ticks = new HashMap<UUID, Map.Entry<Integer, Long>>();
        this.crash3Ticks = new HashMap<UUID, Map.Entry<Integer, Long>>();
    }
    
    @Override
    public void handleReceivedPacket(final Player player, final Packet packet) {
        final UUID u = player.getUniqueId();
        if (packet instanceof PacketPlayInArmAnimation) {
            int Count = 0;
            long Time = System.currentTimeMillis();
            if (this.crashTicks.containsKey(u)) {
                Count = this.crashTicks.get(u).getKey();
                Time = this.crashTicks.get(u).getValue();
            }
            ++Count;
            if (this.crashTicks.containsKey(u) && System.currentTimeMillis() - Time > 100L) {
                Count = 0;
                Time = System.currentTimeMillis();
            }
            if (Count > 2000) {
                this.logCheat(player);
                this.disconnect("Too many arm animation packets");
            }
            this.crashTicks.put(u, new AbstractMap.SimpleEntry<Integer, Long>(Count, Time));
        }
        else if (packet instanceof PacketPlayInHeldItemSlot) {
            int Count = 0;
            long Time = System.currentTimeMillis();
            if (this.crash2Ticks.containsKey(u)) {
                Count = this.crash2Ticks.get(u).getKey();
                Time = this.crash2Ticks.get(u).getValue();
            }
            ++Count;
            if (this.crash2Ticks.containsKey(u) && System.currentTimeMillis() - Time > 100L) {
                Count = 0;
                Time = System.currentTimeMillis();
            }
            if (Count > 2000) {
                this.logCheat(player);
                this.disconnect("Too many held item slot packets");
            }
            this.crash2Ticks.put(u, new AbstractMap.SimpleEntry<Integer, Long>(Count, Time));
        }
        else if (packet instanceof PacketPlayInBlockPlace) {
            int Count = 0;
            long Time = System.currentTimeMillis();
            if (this.crash3Ticks.containsKey(u)) {
                Count = this.crash3Ticks.get(u).getKey();
                Time = this.crash3Ticks.get(u).getValue();
            }
            ++Count;
            if (this.crash3Ticks.containsKey(u) && System.currentTimeMillis() - Time > 100L) {
                Count = 0;
                Time = System.currentTimeMillis();
            }
            if (Count > 2000) {
                this.logCheat(player);
                this.disconnect("Too many block place packets");
            }
            this.crash3Ticks.put(u, new AbstractMap.SimpleEntry<Integer, Long>(Count, Time));
        }
    }
}
