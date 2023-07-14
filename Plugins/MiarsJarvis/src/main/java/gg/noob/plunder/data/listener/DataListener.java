/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.comphenix.protocol.PacketType
 *  com.comphenix.protocol.PacketType$Play$Client
 *  com.comphenix.protocol.PacketType$Play$Server
 *  com.comphenix.protocol.ProtocolLibrary
 *  com.comphenix.protocol.events.PacketAdapter
 *  com.comphenix.protocol.events.PacketContainer
 *  com.comphenix.protocol.events.PacketEvent
 *  com.comphenix.protocol.events.PacketListener
 *  com.comphenix.protocol.wrappers.EnumWrappers$EntityUseAction
 *  com.comphenix.protocol.wrappers.EnumWrappers$PlayerAction
 *  io.netty.buffer.Unpooled
 *  net.minecraft.server.v1_8_R3.Entity
 *  net.minecraft.server.v1_8_R3.Packet
 *  net.minecraft.server.v1_8_R3.PacketDataSerializer
 *  net.minecraft.server.v1_8_R3.PacketPlayInFlying
 *  net.minecraft.server.v1_8_R3.PacketPlayInFlying$PacketPlayInPosition
 *  net.minecraft.server.v1_8_R3.PacketPlayInFlying$PacketPlayInPositionLook
 *  net.minecraft.server.v1_8_R3.PacketPlayInUseEntity
 *  net.minecraft.server.v1_8_R3.PacketPlayInUseEntity$EnumEntityUseAction
 *  net.minecraft.server.v1_8_R3.PacketPlayOutEntityVelocity
 *  net.minecraft.server.v1_8_R3.World
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.GameMode
 *  org.bukkit.Location
 *  org.bukkit.block.Block
 *  org.bukkit.craftbukkit.v1_8_R3.CraftWorld
 *  org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.BlockBreakEvent
 *  org.bukkit.event.block.BlockPlaceEvent
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.player.PlayerRespawnEvent
 *  org.bukkit.event.player.PlayerTeleportEvent
 *  org.bukkit.event.player.PlayerTeleportEvent$TeleportCause
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 *  org.bukkit.util.Vector
 */
package gg.noob.plunder.data.listener;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.PacketListener;
import com.comphenix.protocol.wrappers.EnumWrappers;
import gg.noob.plunder.Plunder;
import gg.noob.plunder.checks.combat.reach.ReachA;
import gg.noob.plunder.data.DataManager;
import gg.noob.plunder.data.PlayerData;
import gg.noob.plunder.util.AABB;
import gg.noob.plunder.util.DistanceData;
import gg.noob.plunder.util.JavaV;
import gg.noob.plunder.util.MathUtils;
import gg.noob.plunder.util.Pair;
import gg.noob.plunder.util.PlayerReachEntity;
import gg.noob.plunder.util.ReachData;
import gg.noob.plunder.util.VerusCuboid;
import io.netty.buffer.Unpooled;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketDataSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityVelocity;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class DataListener
        implements Listener {
    public static int defaultWait = 15;
    public static Map<UUID, Long> lastMove = new WeakHashMap<UUID, Long>();
    public static int serverTicks = 0;
    public static Map<UUID, Long> keepAlive = new HashMap<UUID, Long>();
    public static HashMap<String, Integer> ticksLeft = new HashMap();
    public static HashMap<String, BukkitRunnable> cooldownTask = new HashMap();

    public DataListener() {
        new BukkitRunnable(){
            Map<UUID, Location> lastMoveLocation = new HashMap<UUID, Location>();

            public void run() {
                DataManager dataManager = Plunder.getInstance().getDataManager();
                for (Player player : Bukkit.getOnlinePlayers()) {
                    Location lastMove = this.lastMoveLocation.get(player.getUniqueId());
                    PlayerData data = dataManager.getPlayerData(player);
                    if (data == null) continue;
                    if (lastMove != null) {
                        data.setMovingX(player.getLocation().getX() - lastMove.getX());
                        data.setMovingY(player.getLocation().getY() - lastMove.getY());
                        data.setMovingZ(player.getLocation().getZ() - lastMove.getZ());
                        data.setMovingYaw(player.getLocation().getYaw() - lastMove.getYaw());
                        data.setMovingPitch(player.getLocation().getPitch() - lastMove.getPitch());
                    }
                    this.lastMoveLocation.put(player.getUniqueId(), player.getLocation());
                }
            }
        }.runTaskTimerAsynchronously((Plugin)Plunder.getInstance(), 2L, 2L);
        new BukkitRunnable(){

            public void run() {
                for (PlayerData data : Plunder.getInstance().getDataManager().getPlayerDataSet()) {
                    org.bukkit.entity.Entity target = data.getTarget();
                    org.bukkit.entity.Entity lastTarget = data.getLastTarget();
                    if (target != null && lastTarget != null) {
                        if (target != lastTarget) {
                            data.getTargetLocations().clear();
                        }
                        if (data.getTargetLocations().size() < 30) {
                            Location location = target.getLocation();
                            data.getTargetLocations().add(new Pair<Location, Integer>(location, data.getTicks()));
                        }
                    }
                    data.setTicks(data.getTicks() + 1);
                }
            }
        }.runTaskTimerAsynchronously((Plugin)Plunder.getInstance(), 1L, 1L);
        ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener)new PacketAdapter((Plugin)Plunder.getInstance(), new PacketType[]{PacketType.Play.Client.ENTITY_ACTION}){

            public void onPacketReceiving(PacketEvent event) {
                Player player = event.getPlayer();
                PacketContainer packet = event.getPacket();
                PlayerData data = Plunder.getInstance().getDataManager().getPlayerData(player);
                if (data == null) {
                    return;
                }
                EnumWrappers.PlayerAction action = (EnumWrappers.PlayerAction)packet.getPlayerActions().read(0);
                data.setLastSprint(data.isSprint());
                if (action == EnumWrappers.PlayerAction.START_SPRINTING) {
                    data.setSprint(true);
                } else if (action == EnumWrappers.PlayerAction.STOP_SPRINTING) {
                    data.setSprint(false);
                }
            }
        });
        ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener)new PacketAdapter((Plugin)Plunder.getInstance(), new PacketType[]{PacketType.Play.Client.POSITION, PacketType.Play.Client.FLYING, PacketType.Play.Client.POSITION_LOOK, PacketType.Play.Client.LOOK}){

            public void onPacketReceiving(PacketEvent event) {
                Player player = event.getPlayer();
                PacketContainer packet = event.getPacket();
                PlayerData data = Plunder.getInstance().getDataManager().getPlayerData(player);
                if (data == null) {
                    return;
                }
                Location loc = new Location(player.getWorld(), ((Double)packet.getDoubles().read(0)).doubleValue(), ((Double)packet.getDoubles().read(1)).doubleValue(), ((Double)packet.getDoubles().read(2)).doubleValue(), ((Float)packet.getFloat().read(0)).floatValue(), ((Float)packet.getFloat().read(1)).floatValue());
                Location lastLoc = data.getLastMovePlayerLoc();
                Location playerLoc = player.getLocation();
                boolean pos = (Boolean)packet.getBooleans().read(1);
                long min = System.currentTimeMillis() - data.lastMove;
                if (min > 110L) {
                    data.lastDelayedMove = System.currentTimeMillis();
                }
                data.lastMove = System.currentTimeMillis();
                data.connectionFrequency.add(50 - (int)min);
                JavaV.trim(data.connectionFrequency, 5);
                if (lastLoc != null && !data.isTeleporting(3)) {
                    data.setLastDeltaX(data.getDeltaX());
                    data.setLastDeltaY(data.getDeltaY());
                    data.setLastDeltaZ(data.getDeltaZ());
                    data.setLastDeltaXZ(data.getDeltaXZ());
                    data.setLastDeltaYaw(data.getDeltaYaw());
                    data.setLastDeltaPitch(data.getDeltaPitch());
                    data.setDeltaX(playerLoc.getX() - lastLoc.getX());
                    data.setDeltaY(playerLoc.getY() - lastLoc.getY());
                    data.setDeltaZ(playerLoc.getZ() - lastLoc.getZ());
                    data.setDeltaXZ(Math.hypot(data.getDeltaX(), data.getDeltaZ()));
                    data.setDeltaYaw(playerLoc.getYaw() - lastLoc.getYaw());
                    data.setDeltaPitch(playerLoc.getPitch() - lastLoc.getPitch());
                    data.setLastLocationPacket(data.getCurrentLocationPacket());
                    data.setCurrentLocationPacket((PacketPlayInFlying)packet.getHandle());
                    double distance = loc.distanceSquared(lastLoc);
                    if (data.lastDistance > 0.0 && distance == 0.0 && !((Boolean)packet.getBooleans().read(1)).booleanValue()) {
                        data.lastOffset = data.ticks;
                    }
                    data.lastDistance = distance;
                }
                ++data.movePacketTicks;
                boolean ground = (Boolean)packet.getBooleans().read(0);
                if (data.getLastMovePacket() != null) {
                    if (!((Boolean)packet.getBooleans().read(1)).booleanValue()) {
                        loc.setX(data.getLastMovePacket().getX());
                        loc.setY(data.getLastMovePacket().getY());
                        loc.setZ(data.getLastMovePacket().getZ());
                    }
                    if (!((Boolean)packet.getBooleans().read(2)).booleanValue()) {
                        loc.setYaw(data.getLastMovePacket().getYaw());
                        loc.setPitch(data.getLastMovePacket().getPitch());
                    }
                }
                data.setTpUnknown(false);
                data.setLastLastMovePacket(data.getLastMovePacket());
                data.setLastMovePacket(loc);
                data.setLastMovePlayerLoc(loc);
                data.moved = !loc.equals((Object)playerLoc);
                data.setLastAttackTicks(data.getLastAttackTicks() + 1);
                long currentMillis = System.currentTimeMillis();
                if (data.getLastAttacked() instanceof Player) {
                    Player target = (Player)data.getLastAttacked();
                    PlayerData targetData = Plunder.getInstance().getDataManager().getPlayerData(target);
                    if (targetData == null) {
                        return;
                    }
                    if (!(data.getLastAttackTicks() > 1 || data.getLastAttacked() == null || System.currentTimeMillis() - data.getLastTeleport() < 1000L || player.isInsideVehicle() || player.getGameMode() == GameMode.CREATIVE || targetData.isTeleporting() || targetData.hasLag() || data.getLastAttacked().isInsideVehicle())) {
                        boolean previousLook = data.getLastLocationPacket().h() || !data.getCurrentLocationPacket().h();
                        Location currentLocation = loc.clone();
                        Location lastLocation = data.getLastLastMovePacket().clone();
                        Deque<Pair<Long, Location>> deque = data.recentMoveMap.get(target.getEntityId());
                        if (deque != null && deque.size() > Math.min(40, 10 + data.getMaxPingTicks())) {
                            data.pingQueue.add((nextTransactionPing, connectionFrequencyVarience) -> {
                                PlayerData currentData = Plunder.getInstance().getDataManager().getPlayerData(player);
                                ArrayList<Pair<Long, Location>> list2 = new ArrayList<Pair<Long, Location>>();
                                int max = Math.max(currentData.ping, nextTransactionPing);
                                int abs = Math.abs(nextTransactionPing - currentData.ping);
                                long n5 = currentMillis - 125L - (long)max - (long)connectionFrequencyVarience.intValue();
                                int n6 = 0;
                                Iterator iterator = deque.iterator();
                                Pair packetLocation = (Pair)iterator.next();
                                while (iterator.hasNext()) {
                                    Pair packetLocation2 = (Pair)iterator.next();
                                    long n7 = (Long)packetLocation2.getX() - n5;
                                    if (n7 > 0L) {
                                        list2.add(packetLocation);
                                        if ((double)(n7 - (long)abs) > 50.0 + connectionFrequencyVarience) {
                                            packetLocation = packetLocation2;
                                            break;
                                        }
                                    } else {
                                        n6 = (int)(-n7);
                                    }
                                    packetLocation = packetLocation2;
                                }
                                if (list2.isEmpty()) {
                                    list2.add(packetLocation);
                                }
                                List<DistanceData> dataList = list2.stream().map(packetLocation1 -> DataListener.this.getReach(packetLocation1, currentLocation, lastLocation)).collect(Collectors.toList());
                                List list = dataList.stream().map(DistanceData::getHorizontal).collect(Collectors.toList());
                                DistanceData distanceData = dataList.stream().min(Comparator.comparingDouble(DistanceData::getReach)).get();
                                ReachData reachData = new ReachData(currentData, currentLocation, lastLocation, distanceData, list2, dataList, previousLook, currentData.getTicks() - currentData.previousSwingTicks + 1, MathUtils.highest(list) - distanceData.getHorizontal(), Math.min(1.0, (double)n6 / 50.0));
                                currentData.getChecks().forEach(check -> check.handleReachData(player, reachData, currentMillis));
                                currentData.reachData.add(reachData);
                            });
                        }
                    }
                }
            }
        });
        ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener)new PacketAdapter((Plugin)Plunder.getInstance(), new PacketType[]{PacketType.Play.Server.ENTITY_VELOCITY, PacketType.Play.Server.ENTITY_TELEPORT}){

            public void onPacketSending(PacketEvent event) {
                Player player = event.getPlayer();
                Packet packet = (Packet)event.getPacket().getHandle();
                PlayerData data = Plunder.getInstance().getDataManager().getPlayerData(player);
                if (data == null) {
                    return;
                }
                if (packet instanceof PacketPlayOutEntityVelocity) {
                    PacketDataSerializer serializer = new PacketDataSerializer(Unpooled.buffer((int)0));
                    try {
                        packet.b(serializer);
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                    int id = serializer.e();
                    if (id != player.getEntityId()) {
                        return;
                    }
                    data.setLastNoTransVelocityTicks(data.getTicks());
                    if (System.currentTimeMillis() - data.getLastDamageTime() > 4L) {
                        data.setLastNoDamageVelocityTicks(data.getTicks());
                        return;
                    }
                    double x = (double)serializer.readShort() / 8000.0;
                    double y = (double)serializer.readShort() / 8000.0;
                    double z = (double)serializer.readShort() / 8000.0;
                    Vector velocity = new Vector(x, y, z);
                    data.sendTransaction(velocity);
                    Plunder.getInstance().onTickEnd.add(() -> data.sendTransactionTwice(velocity));
                }
            }
        });
        ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener)new PacketAdapter((Plugin)Plunder.getInstance(), new PacketType[]{PacketType.Play.Client.TRANSACTION}){

            public void onPacketReceiving(PacketEvent event) {
                Pair<Long, Vector> n4;
                Pair<Long, Vector> n3;
                Player player = event.getPlayer();
                PacketContainer packet = event.getPacket();
                PlayerData data = Plunder.getInstance().getDataManager().getPlayerData(player);
                if (data == null) {
                    return;
                }
                short specialId = (Short)packet.getShorts().read(0);
                Long n2 = data.transactionMap.remove(specialId);
                if (n2 != null) {
                    BiConsumer<Integer, Double> biConsumer;
                    data.setReceivedTransaction(true);
                    double variance = MathUtils.variance(0, data.connectionFrequency);
                    ++data.receivedTransactionCount;
                    data.lastTransactionPing = data.transactionPing;
                    data.transactionPing = (int)(System.currentTimeMillis() - n2);
                    data.averageTransactionPing = (data.averageTransactionPing * 4 + data.transactionPing) / 5;
                    data.lastReceivedTransactionId = specialId;
                    while ((biConsumer = data.pingQueue.poll()) != null) {
                        biConsumer.accept(data.transactionPing, variance);
                    }
                    data.getChecks().forEach(check -> check.handleReachData(player, new ArrayList<ReachData>(data.reachData), System.currentTimeMillis()));
                    data.getChecks().forEach(check -> check.handleAlwaysTransaction(player, n2));
                    Runnable runnable = data.syncTransactionMap.remove(specialId);
                    if (runnable != null) {
                        runnable.run();
                    }
                    data.reachData.clear();
                    data.lastClientTransaction = System.currentTimeMillis();
                }
                if ((n3 = data.asyncTransactionMap.remove(specialId)) != null) {
                    data.receivedTransactionVelocityOnce = true;
                    data.getChecks().forEach(check -> {
                        check.handleTransaction(player, System.currentTimeMillis());
                        check.handleTransaction(player, (Vector)n3.getY());
                        check.handleTransaction(player, System.currentTimeMillis(), data.getLastTransactionID());
                    });
                    ++data.receivedAsyncTransactionCount;
                    data.lastReceivedTransactionTimeAsync = System.currentTimeMillis();
                    data.lastAsyncTransactionPing = data.asyncTransactionPing;
                    data.asyncTransactionPing = (int)(System.currentTimeMillis() - n3.getX());
                }
                if ((n4 = data.asyncTransactionTwiceMap.remove(specialId)) != null) {
                    data.receivedTransactionVelocityTwice = true;
                    data.getChecks().forEach(check -> {
                        check.handleTransactionTwice(player, System.currentTimeMillis());
                        check.handleTransactionTwice(player, (Vector)n4.getY());
                        check.handleTransactionTwice(player, System.currentTimeMillis(), data.getLastTransactionID());
                    });
                    ++data.receivedAsyncTransactionTwiceCount;
                    data.lastReceivedTransactionTwiceTimeAsync = System.currentTimeMillis();
                    data.lastAsyncTransactionTwicePing = data.asyncTransactionTwicePing;
                    data.asyncTransactionTwicePing = (int)(System.currentTimeMillis() - n4.getX());
                }
            }
        });
        ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener)new PacketAdapter((Plugin)Plunder.getInstance(), new PacketType[]{PacketType.Play.Client.USE_ENTITY, PacketType.Play.Client.POSITION, PacketType.Play.Client.FLYING, PacketType.Play.Client.POSITION_LOOK, PacketType.Play.Client.LOOK}){

            public void onPacketReceiving(PacketEvent event) {
                Player player = event.getPlayer();
                PacketContainer packetContainer = event.getPacket();
                Object packet = packetContainer.getHandle();
                PlayerData data = Plunder.getInstance().getDataManager().getPlayerData(player);
                if (data == null) {
                    return;
                }
                if (packet instanceof PacketPlayInUseEntity) {
                    Entity target;
                    if (player.getGameMode() == GameMode.CREATIVE) {
                        return;
                    }
                    if (player.isInsideVehicle()) {
                        return;
                    }
                    if (((PacketPlayInUseEntity)packet).a() == PacketPlayInUseEntity.EnumEntityUseAction.ATTACK && (target = ((PacketPlayInUseEntity)packet).a((World)((CraftWorld)player.getWorld()).getHandle())) != null && player.getLocation().distance(target.getBukkitEntity().getLocation()) < 8.0) {
                        DataListener.this.checkReach(player, target.getId());
                    }
                } else if (packet instanceof PacketPlayInFlying) {
                    if (!data.isTeleporting()) {
                        data.position = packet instanceof PacketPlayInFlying.PacketPlayInPosition || packet instanceof PacketPlayInFlying.PacketPlayInPositionLook;
                        data.getChecks().forEach(check -> {
                            if (check instanceof ReachA) {
                                ReachA reachA = (ReachA)check;
                                reachA.tickFlying(player, (PacketPlayInFlying)packet);
                            }
                        });
                    }
                    data.lastPosition = data.position;
                }
            }
        });
        ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener)new PacketAdapter((Plugin)Plunder.getInstance(), new PacketType[]{PacketType.Play.Server.NAMED_ENTITY_SPAWN, PacketType.Play.Server.ENTITY_DESTROY, PacketType.Play.Server.ENTITY_TELEPORT, PacketType.Play.Server.REL_ENTITY_MOVE, PacketType.Play.Server.REL_ENTITY_MOVE_LOOK}){

            /*
             * Enabled aggressive block sorting
             */
            public void onPacketSending(PacketEvent event) {
                PacketType packetType;
                PlayerData data;
                PacketContainer packet;
                block24: {
                    block23: {
                        PlayerReachEntity reachEntity;
                        block21: {
                            block22: {
                                Player player;
                                block20: {
                                    Deque<Pair<Long, Location>> deque4;
                                    int entityId;
                                    player = event.getPlayer();
                                    packet = event.getPacket();
                                    data = Plunder.getInstance().getDataManager().getPlayerData(player);
                                    if (data == null) {
                                        return;
                                    }
                                    packetType = packet.getType();
                                    if (packetType == PacketType.Play.Server.NAMED_ENTITY_SPAWN) {
                                        entityId = (Integer)packet.getIntegers().read(0);
                                        Deque deque2 = data.recentMoveMap.computeIfAbsent(entityId, id -> new ConcurrentLinkedDeque());
                                        deque2.add(new Pair<Long, Location>(System.currentTimeMillis(), new Location(player.getWorld(), (double)((Integer)packet.getIntegers().read(1)).intValue() / 32.0, (double)((Integer)packet.getIntegers().read(2)).intValue() / 32.0, (double)((Integer)packet.getIntegers().read(3)).intValue() / 32.0)));
                                        JavaV.trim(deque2, 80);
                                    } else if (packetType == PacketType.Play.Server.ENTITY_DESTROY) {
                                        Arrays.stream((int[])packet.getIntegerArrays().read(0)).boxed().collect(Collectors.toList()).forEach(id -> data.recentMoveMap.remove(id));
                                    } else if (packetType == PacketType.Play.Server.ENTITY_TELEPORT) {
                                        entityId = (Integer)packet.getIntegers().read(0);
                                        Deque<Pair<Long, Location>> deque3 = data.recentMoveMap.get(entityId);
                                        if (deque3 != null) {
                                            deque3.add(new Pair<Long, Location>(System.currentTimeMillis(), new Location(player.getWorld(), (double)((Integer)packet.getIntegers().read(1)).intValue() / 32.0, (double)((Integer)packet.getIntegers().read(2)).intValue() / 32.0, (double)((Integer)packet.getIntegers().read(3)).intValue() / 32.0)));
                                            JavaV.trim(deque3, 80);
                                        }
                                    } else if (!(packetType != PacketType.Play.Server.REL_ENTITY_MOVE && packetType != PacketType.Play.Server.REL_ENTITY_MOVE_LOOK || (deque4 = data.recentMoveMap.get(entityId = ((Integer)packet.getIntegers().read(0)).intValue())) == null || deque4.isEmpty())) {
                                        Location peekLast = deque4.peekLast().getY();
                                        Pair<Long, Location> move = new Pair<Long, Location>(System.currentTimeMillis(), new Location(player.getWorld(), peekLast.getX(), peekLast.getY(), peekLast.getZ(), (float)((Byte)packet.getBytes().read(3)).byteValue(), (float)((Byte)packet.getBytes().read(4)).byteValue()).add(new Location(player.getWorld(), (double)((Byte)packet.getBytes().read(0)).byteValue() / 4096.0, (double)((Byte)packet.getBytes().read(1)).byteValue() / 4096.0, (double)((Byte)packet.getBytes().read(2)).byteValue() / 4096.0)));
                                        deque4.add(move);
                                        JavaV.trim(deque4, 80);
                                    }
                                    if (packetType == PacketType.Play.Server.NAMED_ENTITY_SPAWN) break block20;
                                    if (packetType == PacketType.Play.Server.REL_ENTITY_MOVE) break block21;
                                    break block22;
                                }
                                Entity nmsEntity = ((CraftWorld)player.getWorld()).getHandle().a(((Integer)packet.getIntegers().read(0)).intValue());
                                if (nmsEntity == null) {
                                    return;
                                }
                                CraftEntity entity = nmsEntity.getBukkitEntity();
                                if (entity == null) return;
                                if (entity.getType() != EntityType.PLAYER) {
                                    return;
                                }
                                DataListener.this.handleSpawnPlayer(data, (Integer)packet.getIntegers().read(0), new AABB.Vec3D((double)((Integer)packet.getIntegers().read(1)).intValue() / 32.0, (double)((Integer)packet.getIntegers().read(2)).intValue() / 32.0, (double)((Integer)packet.getIntegers().read(3)).intValue() / 32.0));
                                return;
                            }
                            if (packetType == PacketType.Play.Server.REL_ENTITY_MOVE_LOOK || packetType == PacketType.Play.Server.ENTITY_LOOK) break block21;
                            if (packetType == PacketType.Play.Server.ENTITY_TELEPORT) break block23;
                            break block24;
                        }
                        if ((reachEntity = data.entityMap.get(packet.getIntegers().read(0))) == null) return;
                        if (data.getTarget() == null) {
                            return;
                        }
                        if (data.getTarget().getEntityId() != ((Integer)packet.getIntegers().read(0)).intValue()) {
                            return;
                        }
                        if (reachEntity.lastTransactionHung == data.getLastSyncTransactionSendId()) {
                            data.handleTransaction();
                        }
                        reachEntity.lastTransactionHung = data.getLastSyncTransactionSendId();
                        DataListener.this.handleMoveEntity(data, (Integer)packet.getIntegers().read(0), (double)((Byte)packet.getBytes().read(0)).byteValue() / 4096.0, (double)((Byte)packet.getBytes().read(1)).byteValue() / 4096.0, (double)((Byte)packet.getBytes().read(2)).byteValue() / 4096.0, true);
                        return;
                    }
                    PlayerReachEntity reachEntity = data.entityMap.get(packet.getIntegers().read(0));
                    if (reachEntity == null) return;
                    if (data.getTarget() == null) {
                        return;
                    }
                    if (data.getTarget().getEntityId() == ((Integer)packet.getIntegers().read(0)).intValue()) {
                        if (reachEntity.lastTransactionHung == data.getLastSyncTransactionSendId()) {
                            data.handleTransaction();
                        }
                        reachEntity.lastTransactionHung = data.getLastSyncTransactionSendId();
                        AABB.Vec3D pos = new AABB.Vec3D((double)((Integer)packet.getIntegers().read(1)).intValue() / 32.0, (double)((Integer)packet.getIntegers().read(2)).intValue() / 32.0, (double)((Integer)packet.getIntegers().read(3)).intValue() / 32.0);
                        DataListener.this.handleMoveEntity(data, (Integer)packet.getIntegers().read(0), pos.getX(), pos.getY(), pos.getZ(), false);
                        return;
                    }
                }
                if (packetType != PacketType.Play.Server.ENTITY_DESTROY) return;
                short lastTransactionSent = data.getLastSyncTransactionSendId();
                Optional<Object> destroyEntityIds = Optional.ofNullable(packet.getIntegerArrays().read(0));
                if (!destroyEntityIds.isPresent()) {
                    return;
                }
                int[] nArray = (int[])destroyEntityIds.get();
                int n = nArray.length;
                int n2 = 0;
                while (n2 < n) {
                    int integer = nArray[n2];
                    PlayerReachEntity entity2 = data.entityMap.get(integer);
                    if (entity2 != null) {
                        data.entityMap.remove(integer);
                    }
                    ++n2;
                }
            }
        });
        ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener)new PacketAdapter((Plugin)Plunder.getInstance(), new PacketType[]{PacketType.Play.Client.ARM_ANIMATION}){

            public void onPacketReceiving(PacketEvent event) {
                Player player = event.getPlayer();
                PacketContainer packet = event.getPacket();
                PlayerData data = Plunder.getInstance().getDataManager().getPlayerData(player);
                if (data == null) {
                    return;
                }
                data.setPreviousSwingTicks(data.getTicks());
            }
        });
        ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener)new PacketAdapter((Plugin)Plunder.getInstance(), new PacketType[]{PacketType.Play.Client.USE_ENTITY}){

            public void onPacketReceiving(PacketEvent event) {
                Player player = event.getPlayer();
                PacketContainer packet = event.getPacket();
                Entity entityNMS = ((PacketPlayInUseEntity)packet.getHandle()).a((World)((CraftWorld)player.getWorld()).getHandle());
                if (entityNMS == null) {
                    return;
                }
                CraftEntity entity = entityNMS.getBukkitEntity();
                PlayerData data = Plunder.getInstance().getDataManager().getPlayerData(player);
                if (data == null) {
                    return;
                }
                EnumWrappers.EntityUseAction action = (EnumWrappers.EntityUseAction)packet.getEntityUseActions().read(0);
                if (action != EnumWrappers.EntityUseAction.ATTACK) {
                    return;
                }
                data.setLastAttacked((org.bukkit.entity.Entity)entity);
                data.setLastAttackTime(System.currentTimeMillis());
                data.setLastAttackTicks(0);
            }
        });
        ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener)new PacketAdapter((Plugin)Plunder.getInstance(), new PacketType[]{PacketType.Play.Server.KEEP_ALIVE}){

            public void onPacketSending(PacketEvent event) {
                Player player = event.getPlayer();
                PacketContainer packet = event.getPacket();
                PlayerData data = Plunder.getInstance().getDataManager().getPlayerData(player);
                if (data == null) {
                    return;
                }
                keepAlive.put(player.getUniqueId(), System.currentTimeMillis());
            }
        });
        ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener)new PacketAdapter((Plugin)Plunder.getInstance(), new PacketType[]{PacketType.Play.Client.KEEP_ALIVE}){

            public void onPacketReceiving(PacketEvent event) {
                Player player = event.getPlayer();
                PacketContainer packet = event.getPacket();
                PlayerData data = Plunder.getInstance().getDataManager().getPlayerData(player);
                if (data == null) {
                    return;
                }
                if (keepAlive.containsKey(player.getUniqueId())) {
                    data.ping = (int)(System.currentTimeMillis() - keepAlive.getOrDefault(player.getUniqueId(), System.currentTimeMillis()));
                    keepAlive.remove(player.getUniqueId());
                }
            }
        });
        new BukkitRunnable(){

            public void run() {
                try {
                    Plunder.getInstance().getDataManager().getPlayerDataSet().forEach(data -> {
                        if (data.isLagging()) {
                            data.setLastLag(System.currentTimeMillis());
                            data.lagBuffer += 1.0;
                            if (data.getLagBuffer() > 100.0) {
                                Bukkit.getScheduler().runTaskLater((Plugin)Plunder.getInstance(), () -> data.getPlayer().kickPlayer(ChatColor.RED + "Check your internet!"), 1L);
                            }
                        } else if (data.getLagBuffer() > 0.0) {
                            data.lagBuffer -= 0.1;
                        }
                    });
                    ++serverTicks;
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }.runTaskTimer((Plugin)Plunder.getInstance(), 1L, 1L);
    }

    public boolean inTimer(Player player) {
        if (ticksLeft.isEmpty() || !ticksLeft.containsKey(player.getName())) {
            return false;
        }
        return ticksLeft.containsKey(player.getName());
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e) {
        Player player = e.getPlayer();
        PlayerData data = Plunder.getInstance().getDataManager().getPlayerData(player);
        if (data == null) {
            return;
        }
        data.setLastRespawn(System.currentTimeMillis());
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGH)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player) {
            Player player = (Player)e.getDamager();
            PlayerData data = Plunder.getInstance().getDataManager().getPlayerData(player);
            if (data == null) {
                return;
            }
            org.bukkit.entity.Entity entity = e.getEntity();
            data.setLastTarget(data.getTarget());
            data.setTarget(entity);
            if (data.getLastTarget() != null) {
                if (entity.getEntityId() != data.getLastTarget().getEntityId()) {
                    data.setLastTargetTicks(data.getTicks());
                    if (entity instanceof Player) {
                        this.handleSpawnPlayer(data, entity.getEntityId(), AABB.Vec3D.fromLocation(entity.getLocation()));
                    }
                }
            } else {
                data.setLastTargetTicks(data.getTicks());
            }
            data.setDistanceTarget(player.getLocation().toVector().setY(0).distance(entity.getLocation().toVector().setY(0)) - 0.42);
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGH)
    public void onPlayerDamage(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player) {
            Player player = (Player)e.getEntity();
            PlayerData data = Plunder.getInstance().getDataManager().getPlayerData(player);
            if (data == null) {
                return;
            }
            data.setLastDamageByPlayer(e.getDamager() instanceof Player);
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGH)
    public void onTeleport(PlayerTeleportEvent e) {
        PlayerData data = Plunder.getInstance().getDataManager().getPlayerData(e.getPlayer());
        Location to = e.getTo();
        Location from = e.getFrom();
        if (data == null) {
            return;
        }
        if (e.getCause() == PlayerTeleportEvent.TeleportCause.UNKNOWN) {
            data.setTpUnknown(true);
        }
        data.setLastTeleport(System.currentTimeMillis());
        data.setLastTeleportTicks(data.getTicks());
        data.setLastNotTeleportLoc(from);
        data.setLastTeleportLoc(to);
        data.lastPosX = to.getX();
        data.lastPosY = to.getY();
        data.lastPosZ = to.getZ();
        data.lastYaw = to.getYaw();
        data.lastPitch = to.getPitch();
    }

    @EventHandler(priority=EventPriority.HIGHEST)
    public void onBlockPlace(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        PlayerData data = Plunder.getInstance().getDataManager().getPlayerData(player);
        if (data == null) {
            return;
        }
        data.setLastBlockPlacedTicks(data.getTicks());
        if (e.isCancelled()) {
            data.setLastBlockPlacedCanceledTicks(data.getTicks());
        }
    }

    @EventHandler(priority=EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();
        PlayerData data = Plunder.getInstance().getDataManager().getPlayerData(player);
        if (data == null) {
            return;
        }
        data.setLastBlockBreak(System.currentTimeMillis());
        if (e.isCancelled()) {
            data.setLastBlockBreakCanceled(System.currentTimeMillis());
        }
        Block block = e.getBlock();
    }

    public static Map<UUID, Long> getLastMove() {
        return lastMove;
    }

    public static void setLastMove(Map<UUID, Long> lastMove) {
        DataListener.lastMove = lastMove;
    }

    private DistanceData getReach(Pair<Long, Location> packetLocation, Location playerLocation, Location playerLocation2) {
        VerusCuboid expand = new VerusCuboid(packetLocation.getY().getX(), packetLocation.getY().getY(), packetLocation.getY().getZ()).add(new VerusCuboid(-0.3, 0.3, 0.0, 1.8, -0.3, 0.3)).expand(0.1, 0.1, 0.1).expand(0.03125, 0.0, 0.03125);
        double positiveSmaller = MathUtils.positiveSmaller(playerLocation.getX() - expand.cX(), playerLocation2.getX() - expand.cX());
        double positiveSmaller2 = MathUtils.positiveSmaller(playerLocation.getZ() - expand.cZ(), playerLocation2.getZ() - expand.cZ());
        double min = Math.min(expand.distanceXZ(playerLocation2), expand.distanceXZ(playerLocation));
        double doubleValue = MathUtils.getMinimumAngle(packetLocation, playerLocation, playerLocation2);
        double doubleValue2 = MathUtils.getMinimumAngle(packetLocation, playerLocation2);
        double n = 0.0;
        double n2 = 0.0;
        while (doubleValue > 90.0) {
            doubleValue -= 90.0;
            n += min;
            n2 += min;
        }
        double n3 = n + Math.abs(Math.sin(Math.toRadians(doubleValue))) * min;
        double n4 = n2 + Math.abs(Math.sin(Math.toRadians(doubleValue2))) * min;
        double n5 = playerLocation.getPitch() > 0.0f != playerLocation2.getPitch() > 0.0f ? 0.0 : MathUtils.lowestAbs(Float.valueOf(playerLocation.getPitch()), Float.valueOf(playerLocation2.getPitch()));
        double max = Math.max(Math.abs(Math.tan(Math.toRadians(Math.abs(n5 * 0.95)))) * min, Math.abs(Math.sin(Math.toRadians(Math.abs(n5)))) * MathUtils.average(3, min));
        return new DistanceData(expand, positiveSmaller, positiveSmaller2, min, doubleValue, n3, max, MathUtils.hypot(min, max, n3) - 0.001, n4);
    }

    private void handleSpawnPlayer(PlayerData data, int playerID, AABB.Vec3D spawnPosition) {
        data.entityMap.put(playerID, new PlayerReachEntity(spawnPosition.getX(), spawnPosition.getY(), spawnPosition.getZ()));
    }

    private void handleMoveEntity(PlayerData data, int entityId, double deltaX, double deltaY, double deltaZ, boolean isRelative) {
        PlayerReachEntity reachEntity = data.entityMap.get(entityId);
        if (reachEntity != null) {
            if (!data.hasSentPreWavePacket) {
                data.handleTransaction();
            }
            data.hasSentPreWavePacket = true;
            reachEntity.serverPos = isRelative ? reachEntity.serverPos.add(new AABB.Vec3D(deltaX, deltaY, deltaZ)) : new AABB.Vec3D(deltaX, deltaY, deltaZ);
            short lastTrans = data.getLastTransactionID();
            AABB.Vec3D newPos = reachEntity.serverPos;
            data.syncTransactionMap.put(lastTrans, () -> reachEntity.onFirstTransaction(newPos.getX(), newPos.getY(), newPos.getZ()));
            data.syncTransactionMap.put((short)(lastTrans + 1), reachEntity::onSecondTransaction);
            data.lastSyncTransactionSendId = (short)(lastTrans + 1);
        }
    }

    public static void tickEndEvent(PlayerData data) {
        data.handleTransaction();
        data.hasSentPreWavePacket = false;
    }

    public void checkReach(Player player, int entityID) {
        PlayerData data = Plunder.getInstance().getDataManager().getPlayerData(player);
        if (data == null) {
            return;
        }
        if (data.entityMap.containsKey(entityID)) {
            data.playerAttackQueue.add(entityID);
        }
    }
}

