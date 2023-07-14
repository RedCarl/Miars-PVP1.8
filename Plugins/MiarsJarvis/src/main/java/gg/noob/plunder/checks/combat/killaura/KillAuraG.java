// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.checks.combat.killaura;

import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import gg.noob.plunder.data.PlayerData;
import gg.noob.plunder.util.MathUtils;
import java.util.Comparator;
import java.util.Collection;
import java.util.ArrayList;
import org.bukkit.Location;
import gg.noob.plunder.util.Pair;
import java.util.Deque;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.entity.Player;
import gg.noob.plunder.checks.Check;

public class KillAuraG extends Check
{
    public KillAuraG() {
        super("KillAura (G)");
        this.setMaxViolations(5);
    }
    
    @Override
    public void handleReceivedPacket(final Player player, final Packet packet) {
        final PlayerData data = this.getPlayerData();
        if (packet instanceof PacketPlayInFlying && ((PacketPlayInFlying)packet).h() && System.currentTimeMillis() > 1000L && System.currentTimeMillis() - data.getLastAttackTime() <= 1000L && data.getLastAttacked() != null) {
            final Location playerLocation = player.getLocation();
            final Queue<Pair<Long, Location>> queue = data.getRecentMoveMap().get(data.getLastAttacked().getEntityId());
            if (queue != null) {
                final int n2 = data.getTransactionPing();
                final List<Pair<Long, Location>> list = new ArrayList<Pair<Long, Location>>(queue);
                final ArrayList<Pair<Long, Location>> arrayList = new ArrayList<Pair<Long, Location>>();
                final long l2 = System.currentTimeMillis() - 125L - n2;
                final Iterator<Pair<Long, Location>> iterator = list.iterator();
                Pair<Long, Location> packetLocation2 = iterator.next();
                while (iterator.hasNext()) {
                    final Pair<Long, Location> packetLocation3 = iterator.next();
                    final long l3 = packetLocation3.getX() - l2;
                    if (l3 > 0L) {
                        arrayList.add(packetLocation2);
                        if (l3 > 100L) {
                            packetLocation2 = packetLocation3;
                            break;
                        }
                    }
                    packetLocation2 = packetLocation3;
                }
                if (arrayList.isEmpty()) {
                    arrayList.add(packetLocation2);
                }
                final int n3;
                if ((n3 = list.indexOf(arrayList.stream().min(Comparator.comparingLong(Pair::getX)).get())) > 0) {
                    arrayList.add(list.get(n3 - 1));
                }
                for (final Pair<Long, Location> packetLocation4 : arrayList) {
                    final float[] arrf = MathUtils.getRotationFromPosition(playerLocation, packetLocation4.getY());
                    final double d = MathUtils.getDistanceBetweenAngles(playerLocation.getYaw(), arrf[0]);
                    final double d2 = MathUtils.getDistanceBetweenAngles(playerLocation.getPitch(), arrf[1]);
                    if (d != 0.0 && d2 != 0.0) {
                        continue;
                    }
                    if (d == 0.0) {
                        this.logCheat(player);
                    }
                    if (d2 != 0.0) {
                        continue;
                    }
                    this.logCheat(player);
                }
            }
        }
    }
}
