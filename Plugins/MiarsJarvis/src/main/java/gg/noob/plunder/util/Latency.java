// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.util;

import org.bukkit.ChatColor;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import com.comphenix.protocol.events.PacketListener;
import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.entity.Player;
import java.util.AbstractMap;
import org.bukkit.GameMode;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.plugin.Plugin;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.PacketType;
import gg.noob.plunder.Plunder;
import com.comphenix.protocol.ProtocolLibrary;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.Map;
import org.bukkit.event.Listener;

public class Latency implements Listener
{
    public static Map<UUID, Map.Entry<Integer, Long>> packetTicks;
    public static Map<UUID, Long> lastPacket;
    public List<UUID> blacklist;
    private static Map<UUID, Integer> packets;
    private Map<UUID, Pair<Long, Integer>> count;
    
    public Latency() {
        Latency.packetTicks = new HashMap<UUID, Map.Entry<Integer, Long>>();
        Latency.lastPacket = new HashMap<UUID, Long>();
        this.blacklist = new ArrayList<UUID>();
        Latency.packets = new HashMap<UUID, Integer>();
        this.count = new HashMap<UUID, Pair<Long, Integer>>();
        ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener)new PacketAdapter(Plunder.getInstance(), new PacketType[] { PacketType.Play.Client.FLYING }) {
            public void onPacketReceiving(final PacketEvent event) {
                final Player player = event.getPlayer();
                final PacketContainer packet = event.getPacket();
                final UUID u = player.getUniqueId();
                if (player.getGameMode().equals((Object)GameMode.CREATIVE)) {
                    return;
                }
                int wrongCount = Latency.this.count.getOrDefault(player.getUniqueId(), new Pair<Long, Integer>(System.currentTimeMillis(), 0)).getY();
                long lastResetTime = Latency.this.count.getOrDefault(player.getUniqueId(), new Pair<Long, Integer>(System.currentTimeMillis(), 0)).getX();
                int Count = 0;
                long Time = System.currentTimeMillis();
                if (Latency.packetTicks.containsKey(u)) {
                    Count = Latency.packetTicks.get(u).getKey();
                    Time = Latency.packetTicks.get(u).getValue();
                }
                if (Latency.lastPacket.containsKey(u)) {
                    final long MS = System.currentTimeMillis() - Latency.lastPacket.get(u);
                    if (MS >= 100L) {
                        Latency.this.blacklist.add(u);
                    }
                    else if (MS > 1L && Latency.this.blacklist.contains(u)) {
                        Latency.this.blacklist.remove(u);
                    }
                }
                if (!Latency.this.blacklist.contains(u)) {
                    ++Count;
                    if (Latency.packetTicks.containsKey(u) && System.currentTimeMillis() - Time > 1000L) {
                        Latency.packets.put(u, Count);
                        Count = 0;
                        Time = System.currentTimeMillis();
                        if (System.currentTimeMillis() - lastResetTime > 15000L) {
                            wrongCount = 0;
                            lastResetTime = System.currentTimeMillis();
                        }
                    }
                }
                if (Count <= 46 || ++wrongCount > 10) {}
                Latency.packetTicks.put(u, new AbstractMap.SimpleEntry<Integer, Long>(Count, Time));
                Latency.lastPacket.put(u, System.currentTimeMillis());
                Latency.this.count.put(player.getUniqueId(), new Pair<Long, Integer>(lastResetTime, wrongCount));
            }
        });
    }
    
    public static Integer getLag(final Player p) {
        if (Latency.packets.containsKey(p.getUniqueId())) {
            return Latency.packets.get(p.getUniqueId());
        }
        return 0;
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void PlayerJoin(final PlayerJoinEvent e) {
        final Player p = e.getPlayer();
        final UUID u = p.getUniqueId();
        this.blacklist.add(u);
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onLogout(final PlayerQuitEvent e) {
        final Player p = e.getPlayer();
        final UUID u = p.getUniqueId();
        Latency.packetTicks.remove(u);
        Latency.lastPacket.remove(u);
        this.blacklist.remove(u);
        Latency.packets.remove(u);
        this.count.remove(u);
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void PlayerChangedWorld(final PlayerChangedWorldEvent e) {
        final Player p = e.getPlayer();
        final UUID u = p.getUniqueId();
        this.blacklist.add(u);
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void PlayerRespawn(final PlayerRespawnEvent e) {
        final Player p = e.getPlayer();
        final UUID u = p.getUniqueId();
        this.blacklist.add(u);
    }
    
    public static ChatColor getPingColor(final int ping) {
        if (ping < 50) {
            return ChatColor.GREEN;
        }
        if (ping < 150) {
            return ChatColor.YELLOW;
        }
        return ChatColor.RED;
    }
}
