// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.data;

import gg.noob.plunder.util.AABB;
import gg.noob.plunder.util.BlockUtils;
import java.util.HashMap;
import java.util.Iterator;
import net.minecraft.server.v1_8_R3.PacketPlayOutTransaction;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.block.Block;
import org.bukkit.Material;
import java.util.AbstractMap;
import gg.noob.plunder.Plunder;
import gg.noob.plunder.util.MathUtils;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.World;
import java.util.Collections;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.server.v1_8_R3.Packet;
import gg.noob.plunder.checks.Check;
import java.util.Set;
import gg.noob.plunder.util.ReachData;
import gg.noob.plunder.util.VelocityData;
import gg.noob.plunder.util.Pair;
import java.util.Deque;
import java.util.Map;
import org.bukkit.Location;
import java.util.concurrent.ConcurrentLinkedQueue;
import gg.noob.plunder.util.PlayerReachEntity;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.Queue;
import java.util.List;
import net.minecraft.server.v1_8_R3.Tuple;
import gg.noob.plunder.util.MouseFilter;
import gg.noob.plunder.util.EvictingList;
import java.util.LinkedList;

public class PlayerData
{
    public String clientBrand;
    public long lastBypass;
    public long lastBlockBreak;
    public long lastBlockBreakCanceled;
    public LinkedList<Float> yawGcdList;
    public LinkedList<Float> pitchGcdList;
    public int lastHighRateTicks;
    public boolean bridge;
    public boolean cinematicMode;
    public float yawGCD;
    public float pitchGCD;
    public float lastYawGCD;
    public float lastPitchGCD;
    public boolean jumped;
    public final EvictingList<Integer> sensitivitySamples;
    public boolean accurateYawData;
    public float expiermentalDeltaX;
    public float expiermentalDeltaY;
    public MouseFilter mxaxis;
    public MouseFilter myaxis;
    public boolean lastDamageByPlayer;
    public boolean receivedTransactionVelocityOnce;
    public boolean receivedTransactionVelocityTwice;
    public int onWebTicks;
    public int inVehicleTicks;
    public int deadTicks;
    public boolean moved;
    public double outlier;
    public double kurtosis;
    public double skewness;
    public double std;
    public double median;
    public double currentCps;
    public double averageCps;
    public Tuple<List<Double>, List<Double>> outlierTuple;
    public int movementTicks;
    public List<Integer> movements;
    public double lastPosX;
    public double lastPosY;
    public double lastPosZ;
    public float lastPitch;
    public boolean lastGroundBoolean;
    public float lastYaw;
    public long lastRespawn;
    public int fenceTicks;
    public boolean receivedVelocity;
    public Queue<BiConsumer<Integer, Double>> pingQueue;
    public ConcurrentHashMap<Integer, PlayerReachEntity> entityMap;
    public ConcurrentLinkedQueue<Integer> playerAttackQueue;
    public boolean lastPosition;
    public boolean position;
    public short lastSyncTransactionSendId;
    public Location lastTeleportLoc;
    public Location lastNotTeleportLoc;
    public Map<Integer, Deque<Pair<Long, Location>>> recentMoveMap;
    public List<VelocityData> allVelocityData;
    public int sprintTicks;
    public boolean tpUnknown;
    public Queue<ReachData> reachData;
    public boolean alerts;
    public int backTicks;
    public int fallBackTicks;
    public long backTime;
    public long fallBackTime;
    public long lastFast;
    public Set<Check> checks;
    public boolean canCheck;
    public Packet lastReceivedPacket;
    public Packet lastSentPacket;
    public long lastReceivedPacketTime;
    public long lastSentPacketTime;
    public int transactionIDCounter;
    public float smoothYaw;
    public float smoothPitch;
    public float lsmoothYaw;
    public float lsmoothPitch;
    public Tuple<List<Float>, List<Float>> yawOutliers;
    public Tuple<List<Float>, List<Float>> pitchOutliers;
    public float sensitivityX;
    public float sensitivityY;
    public float currentSensX;
    public float currentSensY;
    public float sensitivityMcp;
    public float yawMode;
    public float pitchMode;
    public int sensXPercent;
    public int sensYPercent;
    public float smoothCamFilterX;
    public float smoothCamFilterY;
    public float smoothCamYaw;
    public float smoothCamPitch;
    public final List<Short> didWeSendThatTrans;
    public AtomicInteger lastTransactionSent;
    public double fallDistance;
    public int lastNonMoveTicks;
    public int aboveBlockTicks;
    public int lastAttackTicks;
    public boolean checkingPacket;
    public int ping;
    public int waterTicks;
    public long lastBlockDigTicks;
    public long lastBlockPlacedTicks;
    public long lastBlockPlacedCanceledTicks;
    public int velXTicks;
    public int velYTicks;
    public int velZTicks;
    public boolean bouncedOnSlime;
    public Location lastSlimeLocation;
    public int airTicks;
    public int groundTicks;
    public int clientAirTicks;
    public int clientGroundTicks;
    public int airTicks2;
    public int groundTicks2;
    public int clientAirTicks2;
    public int clientGroundTicks2;
    public int solidBlockBehindTicks;
    public int nearClimbTicks;
    public int lastOffset;
    public double lastDistance;
    public int velocityGroundTicks;
    public int flyingTicks;
    public Location groundLoc;
    public Location lastGroundLoc;
    public Location lastLastGroundLoc;
    public Location lastLastLastGroundLoc;
    public Location lastLastLastLastGroundLoc;
    public Location lastMovePlayerLoc;
    public Location lastMovePacket;
    public Location lastLastMovePacket;
    public double stairsTicks;
    public double pistonTicks;
    public long lastLag;
    public int setBackTicks;
    public long LastVelMS;
    public boolean sprint;
    public boolean lastSprint;
    public int lastVelocityTicks;
    public int movePacketTicks;
    public long lastDelayedPacket;
    public long lastPlayerPacket;
    public int previousSwingTicks;
    public Location setbackLocation;
    public long lastTeleport;
    public long lastTeleportDid;
    public int lastTeleportTicks;
    public long lastAirTime;
    public long lastPacket;
    public Location lastGround;
    public long lastGroundTime;
    public long lastAimTime;
    public int hitSlowdownTicks;
    public int aboveSlimeTicks;
    public int nearSlabTicks;
    public int onIceTicks;
    public int underBlockTicks;
    public String key;
    public double strafe;
    public double forward;
    public boolean inInventory;
    public boolean lastVelocityGround;
    public PacketPlayInFlying lastLocationPacket;
    public PacketPlayInFlying currentLocationPacket;
    public double deltaX;
    public double deltaZ;
    public double deltaXZ;
    public double deltaY;
    public float deltaYaw;
    public float deltaPitch;
    public double lastDeltaX;
    public double lastDeltaZ;
    public double lastDeltaXZ;
    public double lastDeltaY;
    public float lastDeltaYaw;
    public float lastDeltaPitch;
    public long lastMove;
    public long lastDelayedMove;
    public Map<Short, Long> transactionMap;
    public Map<Short, Runnable> syncTransactionMap;
    public Map<Short, Pair<Long, Vector>> asyncTransactionMap;
    public Map<Short, Pair<Long, Vector>> asyncTransactionTwiceMap;
    private long lastServerTransaction;
    public boolean receivedTransaction;
    public Queue<Integer> connectionFrequency;
    public int averageTransactionPing;
    public int lastReceivedTransactionId;
    public Vector lastVelocity;
    public int transactionPing;
    public int lastTransactionPing;
    public int asyncTransactionPing;
    public int lastAsyncTransactionPing;
    public int asyncTransactionTwicePing;
    public int lastAsyncTransactionTwicePing;
    public int lastTargetTicks;
    public Entity target;
    public Entity lastTarget;
    public long lastClientTransaction;
    public double distanceTarget;
    public boolean banned;
    public List<Pair<Location, Integer>> targetLocations;
    public int ticks;
    public double movingX;
    public double movingY;
    public double movingZ;
    public float movingYaw;
    public float movingPitch;
    public Location lastMoveLoc;
    public Location lastLastMoveLoc;
    public Player player;
    public int lastTransactionReceived;
    public boolean onGround;
    public boolean inLiquid;
    public boolean onStairSlab;
    public boolean onIce;
    public boolean onClimbable;
    public boolean underBlock;
    public boolean fallFlying;
    public boolean doingTeleport;
    public int moveTicks;
    public int halfBlockTicks;
    private List<Runnable> processPacketRunnables;
    public int liquidTicks;
    public short lastTransactionID;
    public short lastTransactionIDAsync;
    public float friction;
    public boolean serverGround;
    public boolean nearGround;
    public boolean blockBelow;
    public boolean blockNear;
    public boolean roseBush;
    public int lastDoingBlockUpdateTicks;
    public boolean lastRoseBush;
    public boolean lastBlockBelow;
    public boolean lastBlockNear;
    public int blockTicks;
    public long lastVelocityTaken;
    public long lastAttackTime;
    public Entity lastAttacked;
    public LivingEntity lastHitEntity;
    public boolean backing;
    public int fakeTimer;
    public int sendTransactionCount;
    public int sendAsyncTransactionCount;
    public int sendAsyncTransactionTwiceCount;
    public int sendKeepAliveCount;
    public int receivedTransactionCount;
    public long lastDamageTime;
    public int lastFallDamageTicks;
    public boolean hasSentPreWavePacket;
    public int receivedAsyncTransactionCount;
    public int receivedAsyncTransactionTwiceCount;
    public int receivedKeepAliveCount;
    public long lastReceivedKeepAlive;
    public long lastSendTransactionTimeAsync;
    public long lastSendTransactionTwiceTimeAsync;
    public long lastReceivedTransactionTimeAsync;
    public long lastReceivedTransactionTwiceTimeAsync;
    public int transactionPingAsync;
    public boolean step;
    public List<Float> patterns;
    public float lastRange;
    public double lagBuffer;
    public int lostTransactionPacketsCount;
    public int lostAsyncTransactionPacketsCount;
    public int lostAsyncTransactionTwicePacketsCount;
    public int lastNoTransVelocityTicks;
    public int lastNoDamageVelocityTicks;
    
    public PlayerData(final Player player) {
        this.clientBrand = "Vanilla";
        this.lastBypass = System.currentTimeMillis();
        this.lastBlockBreak = -1L;
        this.lastBlockBreakCanceled = -1L;
        this.yawGcdList = new EvictingList<Float>(45);
        this.pitchGcdList = new EvictingList<Float>(45);
        this.lastHighRateTicks = 0;
        this.bridge = false;
        this.jumped = false;
        this.sensitivitySamples = new EvictingList<Integer>(50);
        this.mxaxis = new MouseFilter();
        this.myaxis = new MouseFilter();
        this.lastDamageByPlayer = false;
        this.receivedTransactionVelocityOnce = false;
        this.receivedTransactionVelocityTwice = false;
        this.onWebTicks = 0;
        this.inVehicleTicks = 0;
        this.deadTicks = 0;
        this.moved = false;
        this.movements = new ArrayList<Integer>();
        this.lastPosX = Double.MAX_VALUE;
        this.lastPosY = Double.MAX_VALUE;
        this.lastPosZ = Double.MAX_VALUE;
        this.lastPitch = Float.MAX_VALUE;
        this.lastGroundBoolean = false;
        this.lastYaw = Float.MAX_VALUE;
        this.lastRespawn = 0L;
        this.fenceTicks = 0;
        this.receivedVelocity = true;
        this.pingQueue = new ConcurrentLinkedQueue<BiConsumer<Integer, Double>>();
        this.entityMap = new ConcurrentHashMap<Integer, PlayerReachEntity>();
        this.playerAttackQueue = new ConcurrentLinkedQueue<Integer>();
        this.lastSyncTransactionSendId = 1;
        this.recentMoveMap = this.createCache(TimeUnit.HOURS.toMillis(1L), null);
        this.allVelocityData = new ArrayList<VelocityData>();
        this.sprintTicks = 0;
        this.tpUnknown = false;
        this.reachData = new ConcurrentLinkedQueue<ReachData>();
        this.alerts = false;
        this.backTicks = 0;
        this.fallBackTicks = 0;
        this.backTime = 0L;
        this.fallBackTime = 0L;
        this.lastFast = 0L;
        this.checks = new HashSet<Check>();
        this.canCheck = false;
        this.lastReceivedPacketTime = System.currentTimeMillis();
        this.lastSentPacketTime = System.currentTimeMillis();
        this.transactionIDCounter = 0;
        this.didWeSendThatTrans = Collections.synchronizedList(new ArrayList<Short>());
        this.lastTransactionSent = new AtomicInteger(0);
        this.fallDistance = 0.0;
        this.lastNonMoveTicks = 0;
        this.aboveBlockTicks = 0;
        this.lastAttackTicks = 600;
        this.checkingPacket = false;
        this.ping = 0;
        this.waterTicks = 0;
        this.lastBlockDigTicks = 0L;
        this.lastBlockPlacedTicks = 0L;
        this.lastBlockPlacedCanceledTicks = 0L;
        this.bouncedOnSlime = false;
        this.airTicks = 0;
        this.groundTicks = 0;
        this.clientAirTicks = 0;
        this.clientGroundTicks = 0;
        this.airTicks2 = 0;
        this.groundTicks2 = 0;
        this.clientAirTicks2 = 0;
        this.clientGroundTicks2 = 0;
        this.solidBlockBehindTicks = 0;
        this.nearClimbTicks = 0;
        this.lastOffset = 0;
        this.lastDistance = 0.0;
        this.velocityGroundTicks = 0;
        this.flyingTicks = 0;
        this.stairsTicks = 0.0;
        this.pistonTicks = 0.0;
        this.lastLag = System.currentTimeMillis();
        this.setBackTicks = 0;
        this.LastVelMS = 0L;
        this.sprint = false;
        this.lastSprint = false;
        this.lastVelocityTicks = 0;
        this.movePacketTicks = 0;
        this.previousSwingTicks = 0;
        this.lastTeleport = -1L;
        this.lastTeleportDid = -1L;
        this.lastTeleportTicks = 0;
        this.lastPacket = 0L;
        this.lastGroundTime = System.currentTimeMillis();
        this.lastAimTime = System.currentTimeMillis();
        this.hitSlowdownTicks = 0;
        this.aboveSlimeTicks = 0;
        this.nearSlabTicks = 0;
        this.onIceTicks = 0;
        this.underBlockTicks = 0;
        this.key = "";
        this.forward = 0.0;
        this.inInventory = false;
        this.lastVelocityGround = false;
        this.transactionMap = new ConcurrentHashMap<Short, Long>();
        this.syncTransactionMap = new ConcurrentHashMap<Short, Runnable>();
        this.asyncTransactionMap = new ConcurrentHashMap<Short, Pair<Long, Vector>>();
        this.asyncTransactionTwiceMap = new ConcurrentHashMap<Short, Pair<Long, Vector>>();
        this.connectionFrequency = new ConcurrentLinkedQueue<Integer>();
        this.lastReceivedTransactionId = -32768;
        this.lastVelocity = new Vector(0, 0, 0);
        this.lastTargetTicks = 0;
        this.banned = false;
        this.targetLocations = new ArrayList<Pair<Location, Integer>>();
        this.lastMoveLoc = new Location((World)Bukkit.getWorlds().get(0), 0.0, 0.0, 0.0);
        this.lastLastMoveLoc = new Location((World)Bukkit.getWorlds().get(0), 0.0, 0.0, 0.0);
        this.lastTransactionReceived = 0;
        this.onGround = false;
        this.inLiquid = false;
        this.onStairSlab = false;
        this.onIce = false;
        this.onClimbable = false;
        this.underBlock = false;
        this.fallFlying = false;
        this.doingTeleport = false;
        this.moveTicks = 0;
        this.halfBlockTicks = 0;
        this.processPacketRunnables = new ArrayList<Runnable>();
        this.liquidTicks = 0;
        this.lastTransactionID = 1;
        this.lastTransactionIDAsync = -32750;
        this.friction = 0.0f;
        this.serverGround = false;
        this.nearGround = false;
        this.blockBelow = false;
        this.blockNear = false;
        this.roseBush = false;
        this.lastDoingBlockUpdateTicks = 0;
        this.lastRoseBush = false;
        this.lastBlockBelow = false;
        this.lastBlockNear = false;
        this.blockTicks = 0;
        this.lastAttackTime = 0L;
        this.backing = false;
        this.fakeTimer = 20;
        this.sendTransactionCount = 0;
        this.sendAsyncTransactionCount = 0;
        this.sendAsyncTransactionTwiceCount = 0;
        this.sendKeepAliveCount = 0;
        this.receivedTransactionCount = 0;
        this.lastDamageTime = -1L;
        this.lastFallDamageTicks = 0;
        this.hasSentPreWavePacket = false;
        this.receivedAsyncTransactionCount = 0;
        this.receivedAsyncTransactionTwiceCount = 0;
        this.receivedKeepAliveCount = 0;
        this.lastReceivedKeepAlive = System.currentTimeMillis();
        this.lastSendTransactionTimeAsync = -1L;
        this.lastSendTransactionTwiceTimeAsync = -1L;
        this.lastReceivedTransactionTimeAsync = -1L;
        this.lastReceivedTransactionTwiceTimeAsync = -1L;
        this.transactionPingAsync = 0;
        this.step = false;
        this.patterns = Lists.newArrayList();
        this.lagBuffer = 0.0;
        this.lostTransactionPacketsCount = 0;
        this.lostAsyncTransactionPacketsCount = 0;
        this.lostAsyncTransactionTwicePacketsCount = 0;
        this.lastNoTransVelocityTicks = 0;
        this.lastNoDamageVelocityTicks = 0;
        this.player = player;
        this.lastGround = player.getLocation();
    }
    
    public boolean isVelocityTaken() {
        return this.velXTicks > 0 || this.velYTicks > 0 || this.velZTicks > 0;
    }
    
    public void reduceVelocity() {
        this.velXTicks = Math.max(0, this.velXTicks - 1);
        this.velYTicks = Math.max(0, this.velYTicks - 1);
        this.velZTicks = Math.max(0, this.velZTicks - 1);
    }
    
    public boolean hasHitSlowdown() {
        return this.ticks - this.hitSlowdownTicks <= 2;
    }
    
    public int getMoveTicks() {
        return (int)Math.floor(Math.min(this.transactionPing, this.averageTransactionPing) / 125.0);
    }
    
    public boolean hasLag() {
        return System.currentTimeMillis() - this.lastLag < 250L;
    }
    
    public boolean isLagging() {
        return this.lastMove != 0L && this.lastDelayedMove != 0L && System.currentTimeMillis() - this.lastDelayedMove < 110L;
    }
    
    public <K, V> Map<K, V> createCache(final Long l, final Long l2) {
        final CacheBuilder cacheBuilder = CacheBuilder.newBuilder();
        if (l != null) {
            cacheBuilder.expireAfterAccess((long)l, TimeUnit.MILLISECONDS);
        }
        if (l2 != null) {
            cacheBuilder.expireAfterWrite((long)l2, TimeUnit.MILLISECONDS);
        }
        return (Map<K, V>)cacheBuilder.build().asMap();
    }
    
    public int getMaxPingTicks() {
        return Math.min(100, this.shouldHaveReceivedPing() ? ((int)Math.ceil(MathUtils.highest(this.transactionPing, this.lastTransactionPing, this.averageTransactionPing) / 50.0)) : Math.max(40, this.ticks)) + 1;
    }
    
    public boolean shouldHaveReceivedPing() {
        return this.receivedTransaction && this.lostTransactionPacketsCount < 3;
    }
    
    public boolean isDoingBlockUpdate() {
        return this.ticks - this.lastDoingBlockUpdateTicks < 4;
    }
    
    public boolean isPlacingBlocks() {
        return this.ticks - this.lastBlockPlacedTicks < 6L;
    }
    
    public boolean isDiggingBlocks() {
        return this.ticks - this.lastBlockDigTicks < 5L;
    }
    
    public boolean isTakingVelocity(final int ticks) {
        return this.ticks - this.lastNoTransVelocityTicks < ticks;
    }
    
    public void executeBack() {
        if (Plunder.getInstance().getLastGround().containsKey(this.player.getUniqueId())) {
            final Location lastGround = Plunder.getInstance().getLastGround().get(this.player.getUniqueId()).getValue();
            final int lastTeleportTicks = this.getLastTeleportTicks();
            final long lastTeleport = this.getLastTeleport();
            final long lastTeleportDid = this.getLastTeleportDid();
            this.setLastBypass(System.currentTimeMillis());
            this.player.teleport(lastGround);
            this.setLastTeleportTicks(lastTeleportTicks);
            this.setLastTeleport(lastTeleport);
            this.setLastTeleportDid(lastTeleportDid);
            this.setDoingTeleport(false);
            Plunder.getInstance().getLastGround().put(this.player.getUniqueId(), new AbstractMap.SimpleEntry<Long, Location>(System.currentTimeMillis(), lastGround));
            this.setLastGround(lastGround);
        }
    }
    
    public void executeFallBack() {
        if (Plunder.getInstance().getLastGround().containsKey(this.player.getUniqueId())) {
            final Location lastGround = Plunder.getInstance().getLastGround().get(this.player.getUniqueId()).getValue();
            final int lastTeleportTicks = this.getLastTeleportTicks();
            final long lastTeleport = this.getLastTeleport();
            final long lastTeleportDid = this.getLastTeleportDid();
            this.setLastBypass(System.currentTimeMillis());
            final Block block = this.player.getWorld().getBlockAt(this.player.getLocation().clone().add(0.0, -1.0, 0.0));
            if (block.getType() == Material.AIR) {
                this.player.teleport(block.getLocation());
                this.player.setVelocity(new Vector(0, -1, 0));
            }
            else {
                this.player.teleport(lastGround);
            }
            this.setLastTeleportTicks(lastTeleportTicks);
            this.setLastTeleport(lastTeleport);
            this.setLastTeleportDid(lastTeleportDid);
            this.setDoingTeleport(false);
            Plunder.getInstance().getLastGround().put(this.player.getUniqueId(), new AbstractMap.SimpleEntry<Long, Location>(System.currentTimeMillis(), lastGround));
            this.setLastGround(lastGround);
        }
    }
    
    public void handleTransaction() {
        ++this.sendTransactionCount;
        this.lastServerTransaction = System.currentTimeMillis();
        ++this.lastTransactionID;
        if (this.lastTransactionID == 32767) {
            this.lastTransactionID = 1;
            this.syncTransactionMap.clear();
            this.lastSyncTransactionSendId = 1;
            this.transactionMap.clear();
            for (final PlayerReachEntity playerReachEntity : this.entityMap.values()) {
                playerReachEntity.lastTransactionHung = 1;
            }
        }
        ((CraftPlayer)this.player).getHandle().playerConnection.sendPacket((Packet)new PacketPlayOutTransaction(0, this.lastTransactionID, false));
        this.transactionMap.put(this.lastTransactionID, System.currentTimeMillis());
    }
    
    public boolean isAttacking() {
        return System.currentTimeMillis() - this.lastAttackTime < 500L;
    }
    
    public int getPingTicks() {
        return Math.min(300, this.shouldHaveReceivedPing() ? ((int)Math.ceil(this.getTransactionPing() / 50.0)) : ((int)MathUtils.highest(40, this.ticks))) + 1;
    }
    
    public void checkTransaction() {
        final Map<Short, Long> map1 = new HashMap<Short, Long>(this.transactionMap);
        for (final short id : map1.keySet()) {
            final long time = map1.get(id);
            if (System.currentTimeMillis() - time > TimeUnit.SECONDS.toMillis(7L)) {
                this.transactionMap.remove(id);
                ++this.lostTransactionPacketsCount;
            }
        }
        final Map<Short, Pair<Long, Vector>> map2 = new HashMap<Short, Pair<Long, Vector>>(this.asyncTransactionMap);
        for (final short id2 : map2.keySet()) {
            final Pair<Long, Vector> pair = map2.get(id2);
            final long time2 = pair.getX();
            if (System.currentTimeMillis() - time2 > TimeUnit.SECONDS.toMillis(7L)) {
                this.asyncTransactionMap.remove(id2);
                ++this.lostAsyncTransactionPacketsCount;
            }
        }
        final Map<Short, Pair<Long, Vector>> map3 = new HashMap<Short, Pair<Long, Vector>>(this.asyncTransactionTwiceMap);
        for (final short id3 : map3.keySet()) {
            final Pair<Long, Vector> pair2 = map3.get(id3);
            final long time3 = pair2.getX();
            if (System.currentTimeMillis() - time3 > TimeUnit.SECONDS.toMillis(7L)) {
                this.asyncTransactionTwiceMap.remove(id3);
                ++this.lostAsyncTransactionTwicePacketsCount;
            }
        }
    }
    
    public void sendTransaction(final Vector velocity) {
        ++this.sendAsyncTransactionCount;
        --this.lastTransactionIDAsync;
        if (this.lastTransactionIDAsync == -32767) {
            this.lastTransactionIDAsync = -1;
            this.asyncTransactionMap.clear();
        }
        ((CraftPlayer)this.player).getHandle().playerConnection.sendPacket((Packet)new PacketPlayOutTransaction(0, this.lastTransactionIDAsync, false));
        this.asyncTransactionMap.put(this.lastTransactionIDAsync, new Pair<Long, Vector>(System.currentTimeMillis(), velocity));
        this.lastSendTransactionTimeAsync = System.currentTimeMillis();
    }
    
    public void sendTransactionTwice(final Vector velocity) {
        ++this.sendAsyncTransactionTwiceCount;
        --this.lastTransactionIDAsync;
        ((CraftPlayer)this.player).getHandle().playerConnection.sendPacket((Packet)new PacketPlayOutTransaction(0, this.lastTransactionIDAsync, false));
        this.asyncTransactionTwiceMap.put(this.lastTransactionIDAsync, new Pair<Long, Vector>(System.currentTimeMillis(), velocity));
        if (this.lastTransactionIDAsync == -32767) {
            this.lastTransactionIDAsync = -1;
            this.asyncTransactionTwiceMap.clear();
        }
        this.lastSendTransactionTwiceTimeAsync = System.currentTimeMillis();
    }
    
    public boolean isTakingVelocity() {
        return this.ticks - this.lastNoTransVelocityTicks < 10;
    }
    
    public boolean isTeleporting() {
        return System.currentTimeMillis() - this.lastTeleportDid < 50L || this.doingTeleport || this.ticks - this.lastTeleportTicks < 1 || System.currentTimeMillis() - this.lastTeleport < 50L;
    }
    
    public boolean isTeleporting(final int ticks) {
        return System.currentTimeMillis() - this.lastTeleportDid < ticks * 50L || this.doingTeleport || this.ticks - this.lastTeleportTicks < ticks || System.currentTimeMillis() - this.lastTeleport < ticks * 50L;
    }
    
    public boolean testStep(final Location from, final Location to) {
        final Vector prevPos = from.toVector();
        final Vector extrapolate = from.toVector();
        extrapolate.setY(extrapolate.getY() + (BlockUtils.isOnGround(to) ? -0.0784 : ((this.getPlayer().getVelocity().getY() - 0.08) * 0.98)));
        AABB box = AABB.playerCollisionBox.clone();
        box.translate(extrapolate);
        final List<AABB> verticalCollision = box.getBlockAABBs(this.player.getWorld(), net.minecraft.server.v1_8_R3.Material.WEB);
        if (verticalCollision.isEmpty() && !this.player.isOnGround()) {
            return false;
        }
        double highestVertical = extrapolate.getY();
        for (final AABB blockAABB : verticalCollision) {
            final double aabbMaxY = blockAABB.getMax().getY();
            if (aabbMaxY > highestVertical) {
                highestVertical = aabbMaxY;
            }
        }
        box = AABB.playerCollisionBox.clone();
        box.translate(to.toVector().clone().setY(highestVertical));
        box.expand(0.0, -1.0E-11, 0.0);
        final List<AABB> horizontalCollision = box.getBlockAABBs(this.player.getWorld(), net.minecraft.server.v1_8_R3.Material.WEB);
        if (horizontalCollision.isEmpty()) {
            return false;
        }
        double expectedY = prevPos.getY();
        double highestPointOnAABB = -1.0;
        for (final AABB blockAABB2 : horizontalCollision) {
            final double blockAABBY = blockAABB2.getMax().getY();
            if (blockAABBY - prevPos.getY() > 0.6) {
                return false;
            }
            if (blockAABBY > expectedY) {
                expectedY = blockAABBY;
            }
            if (blockAABBY <= highestPointOnAABB) {
                continue;
            }
            highestPointOnAABB = blockAABBY;
        }
        return (this.onGround || this.player.isOnGround()) && Math.abs(prevPos.getY() - highestPointOnAABB) > 1.0E-4 && Math.abs(to.getY() - expectedY) < 1.0E-4;
    }
    
    public String getClientBrand() {
        return this.clientBrand;
    }
    
    public long getLastBypass() {
        return this.lastBypass;
    }
    
    public long getLastBlockBreak() {
        return this.lastBlockBreak;
    }
    
    public long getLastBlockBreakCanceled() {
        return this.lastBlockBreakCanceled;
    }
    
    public LinkedList<Float> getYawGcdList() {
        return this.yawGcdList;
    }
    
    public LinkedList<Float> getPitchGcdList() {
        return this.pitchGcdList;
    }
    
    public int getLastHighRateTicks() {
        return this.lastHighRateTicks;
    }
    
    public boolean isBridge() {
        return this.bridge;
    }
    
    public boolean isCinematicMode() {
        return this.cinematicMode;
    }
    
    public float getYawGCD() {
        return this.yawGCD;
    }
    
    public float getPitchGCD() {
        return this.pitchGCD;
    }
    
    public float getLastYawGCD() {
        return this.lastYawGCD;
    }
    
    public float getLastPitchGCD() {
        return this.lastPitchGCD;
    }
    
    public boolean isJumped() {
        return this.jumped;
    }
    
    public EvictingList<Integer> getSensitivitySamples() {
        return this.sensitivitySamples;
    }
    
    public boolean isAccurateYawData() {
        return this.accurateYawData;
    }
    
    public float getExpiermentalDeltaX() {
        return this.expiermentalDeltaX;
    }
    
    public float getExpiermentalDeltaY() {
        return this.expiermentalDeltaY;
    }
    
    public MouseFilter getMxaxis() {
        return this.mxaxis;
    }
    
    public MouseFilter getMyaxis() {
        return this.myaxis;
    }
    
    public boolean isLastDamageByPlayer() {
        return this.lastDamageByPlayer;
    }
    
    public boolean isReceivedTransactionVelocityOnce() {
        return this.receivedTransactionVelocityOnce;
    }
    
    public boolean isReceivedTransactionVelocityTwice() {
        return this.receivedTransactionVelocityTwice;
    }
    
    public int getOnWebTicks() {
        return this.onWebTicks;
    }
    
    public int getInVehicleTicks() {
        return this.inVehicleTicks;
    }
    
    public int getDeadTicks() {
        return this.deadTicks;
    }
    
    public boolean isMoved() {
        return this.moved;
    }
    
    public double getOutlier() {
        return this.outlier;
    }
    
    public double getKurtosis() {
        return this.kurtosis;
    }
    
    public double getSkewness() {
        return this.skewness;
    }
    
    public double getStd() {
        return this.std;
    }
    
    public double getMedian() {
        return this.median;
    }
    
    public double getCurrentCps() {
        return this.currentCps;
    }
    
    public double getAverageCps() {
        return this.averageCps;
    }
    
    public Tuple<List<Double>, List<Double>> getOutlierTuple() {
        return this.outlierTuple;
    }
    
    public int getMovementTicks() {
        return this.movementTicks;
    }
    
    public List<Integer> getMovements() {
        return this.movements;
    }
    
    public double getLastPosX() {
        return this.lastPosX;
    }
    
    public double getLastPosY() {
        return this.lastPosY;
    }
    
    public double getLastPosZ() {
        return this.lastPosZ;
    }
    
    public float getLastPitch() {
        return this.lastPitch;
    }
    
    public boolean isLastGroundBoolean() {
        return this.lastGroundBoolean;
    }
    
    public float getLastYaw() {
        return this.lastYaw;
    }
    
    public long getLastRespawn() {
        return this.lastRespawn;
    }
    
    public int getFenceTicks() {
        return this.fenceTicks;
    }
    
    public boolean isReceivedVelocity() {
        return this.receivedVelocity;
    }
    
    public Queue<BiConsumer<Integer, Double>> getPingQueue() {
        return this.pingQueue;
    }
    
    public ConcurrentHashMap<Integer, PlayerReachEntity> getEntityMap() {
        return this.entityMap;
    }
    
    public ConcurrentLinkedQueue<Integer> getPlayerAttackQueue() {
        return this.playerAttackQueue;
    }
    
    public boolean isLastPosition() {
        return this.lastPosition;
    }
    
    public boolean isPosition() {
        return this.position;
    }
    
    public short getLastSyncTransactionSendId() {
        return this.lastSyncTransactionSendId;
    }
    
    public Location getLastTeleportLoc() {
        return this.lastTeleportLoc;
    }
    
    public Location getLastNotTeleportLoc() {
        return this.lastNotTeleportLoc;
    }
    
    public Map<Integer, Deque<Pair<Long, Location>>> getRecentMoveMap() {
        return this.recentMoveMap;
    }
    
    public List<VelocityData> getAllVelocityData() {
        return this.allVelocityData;
    }
    
    public int getSprintTicks() {
        return this.sprintTicks;
    }
    
    public boolean isTpUnknown() {
        return this.tpUnknown;
    }
    
    public Queue<ReachData> getReachData() {
        return this.reachData;
    }
    
    public boolean isAlerts() {
        return this.alerts;
    }
    
    public int getBackTicks() {
        return this.backTicks;
    }
    
    public int getFallBackTicks() {
        return this.fallBackTicks;
    }
    
    public long getBackTime() {
        return this.backTime;
    }
    
    public long getFallBackTime() {
        return this.fallBackTime;
    }
    
    public long getLastFast() {
        return this.lastFast;
    }
    
    public Set<Check> getChecks() {
        return this.checks;
    }
    
    public boolean isCanCheck() {
        return this.canCheck;
    }
    
    public Packet getLastReceivedPacket() {
        return this.lastReceivedPacket;
    }
    
    public Packet getLastSentPacket() {
        return this.lastSentPacket;
    }
    
    public long getLastReceivedPacketTime() {
        return this.lastReceivedPacketTime;
    }
    
    public long getLastSentPacketTime() {
        return this.lastSentPacketTime;
    }
    
    public int getTransactionIDCounter() {
        return this.transactionIDCounter;
    }
    
    public float getSmoothYaw() {
        return this.smoothYaw;
    }
    
    public float getSmoothPitch() {
        return this.smoothPitch;
    }
    
    public float getLsmoothYaw() {
        return this.lsmoothYaw;
    }
    
    public float getLsmoothPitch() {
        return this.lsmoothPitch;
    }
    
    public Tuple<List<Float>, List<Float>> getYawOutliers() {
        return this.yawOutliers;
    }
    
    public Tuple<List<Float>, List<Float>> getPitchOutliers() {
        return this.pitchOutliers;
    }
    
    public float getSensitivityX() {
        return this.sensitivityX;
    }
    
    public float getSensitivityY() {
        return this.sensitivityY;
    }
    
    public float getCurrentSensX() {
        return this.currentSensX;
    }
    
    public float getCurrentSensY() {
        return this.currentSensY;
    }
    
    public float getSensitivityMcp() {
        return this.sensitivityMcp;
    }
    
    public float getYawMode() {
        return this.yawMode;
    }
    
    public float getPitchMode() {
        return this.pitchMode;
    }
    
    public int getSensXPercent() {
        return this.sensXPercent;
    }
    
    public int getSensYPercent() {
        return this.sensYPercent;
    }
    
    public float getSmoothCamFilterX() {
        return this.smoothCamFilterX;
    }
    
    public float getSmoothCamFilterY() {
        return this.smoothCamFilterY;
    }
    
    public float getSmoothCamYaw() {
        return this.smoothCamYaw;
    }
    
    public float getSmoothCamPitch() {
        return this.smoothCamPitch;
    }
    
    public List<Short> getDidWeSendThatTrans() {
        return this.didWeSendThatTrans;
    }
    
    public AtomicInteger getLastTransactionSent() {
        return this.lastTransactionSent;
    }
    
    public double getFallDistance() {
        return this.fallDistance;
    }
    
    public int getLastNonMoveTicks() {
        return this.lastNonMoveTicks;
    }
    
    public int getAboveBlockTicks() {
        return this.aboveBlockTicks;
    }
    
    public int getLastAttackTicks() {
        return this.lastAttackTicks;
    }
    
    public boolean isCheckingPacket() {
        return this.checkingPacket;
    }
    
    public int getPing() {
        return this.ping;
    }
    
    public int getWaterTicks() {
        return this.waterTicks;
    }
    
    public long getLastBlockDigTicks() {
        return this.lastBlockDigTicks;
    }
    
    public long getLastBlockPlacedTicks() {
        return this.lastBlockPlacedTicks;
    }
    
    public long getLastBlockPlacedCanceledTicks() {
        return this.lastBlockPlacedCanceledTicks;
    }
    
    public int getVelXTicks() {
        return this.velXTicks;
    }
    
    public int getVelYTicks() {
        return this.velYTicks;
    }
    
    public int getVelZTicks() {
        return this.velZTicks;
    }
    
    public boolean isBouncedOnSlime() {
        return this.bouncedOnSlime;
    }
    
    public Location getLastSlimeLocation() {
        return this.lastSlimeLocation;
    }
    
    public int getAirTicks() {
        return this.airTicks;
    }
    
    public int getGroundTicks() {
        return this.groundTicks;
    }
    
    public int getClientAirTicks() {
        return this.clientAirTicks;
    }
    
    public int getClientGroundTicks() {
        return this.clientGroundTicks;
    }
    
    public int getAirTicks2() {
        return this.airTicks2;
    }
    
    public int getGroundTicks2() {
        return this.groundTicks2;
    }
    
    public int getClientAirTicks2() {
        return this.clientAirTicks2;
    }
    
    public int getClientGroundTicks2() {
        return this.clientGroundTicks2;
    }
    
    public int getSolidBlockBehindTicks() {
        return this.solidBlockBehindTicks;
    }
    
    public int getNearClimbTicks() {
        return this.nearClimbTicks;
    }
    
    public int getLastOffset() {
        return this.lastOffset;
    }
    
    public double getLastDistance() {
        return this.lastDistance;
    }
    
    public int getVelocityGroundTicks() {
        return this.velocityGroundTicks;
    }
    
    public int getFlyingTicks() {
        return this.flyingTicks;
    }
    
    public Location getGroundLoc() {
        return this.groundLoc;
    }
    
    public Location getLastGroundLoc() {
        return this.lastGroundLoc;
    }
    
    public Location getLastLastGroundLoc() {
        return this.lastLastGroundLoc;
    }
    
    public Location getLastLastLastGroundLoc() {
        return this.lastLastLastGroundLoc;
    }
    
    public Location getLastLastLastLastGroundLoc() {
        return this.lastLastLastLastGroundLoc;
    }
    
    public Location getLastMovePlayerLoc() {
        return this.lastMovePlayerLoc;
    }
    
    public Location getLastMovePacket() {
        return this.lastMovePacket;
    }
    
    public Location getLastLastMovePacket() {
        return this.lastLastMovePacket;
    }
    
    public double getStairsTicks() {
        return this.stairsTicks;
    }
    
    public double getPistonTicks() {
        return this.pistonTicks;
    }
    
    public long getLastLag() {
        return this.lastLag;
    }
    
    public int getSetBackTicks() {
        return this.setBackTicks;
    }
    
    public long getLastVelMS() {
        return this.LastVelMS;
    }
    
    public boolean isSprint() {
        return this.sprint;
    }
    
    public boolean isLastSprint() {
        return this.lastSprint;
    }
    
    public int getLastVelocityTicks() {
        return this.lastVelocityTicks;
    }
    
    public int getMovePacketTicks() {
        return this.movePacketTicks;
    }
    
    public long getLastDelayedPacket() {
        return this.lastDelayedPacket;
    }
    
    public long getLastPlayerPacket() {
        return this.lastPlayerPacket;
    }
    
    public int getPreviousSwingTicks() {
        return this.previousSwingTicks;
    }
    
    public Location getSetbackLocation() {
        return this.setbackLocation;
    }
    
    public long getLastTeleport() {
        return this.lastTeleport;
    }
    
    public long getLastTeleportDid() {
        return this.lastTeleportDid;
    }
    
    public int getLastTeleportTicks() {
        return this.lastTeleportTicks;
    }
    
    public long getLastAirTime() {
        return this.lastAirTime;
    }
    
    public long getLastPacket() {
        return this.lastPacket;
    }
    
    public Location getLastGround() {
        return this.lastGround;
    }
    
    public long getLastGroundTime() {
        return this.lastGroundTime;
    }
    
    public long getLastAimTime() {
        return this.lastAimTime;
    }
    
    public int getHitSlowdownTicks() {
        return this.hitSlowdownTicks;
    }
    
    public int getAboveSlimeTicks() {
        return this.aboveSlimeTicks;
    }
    
    public int getNearSlabTicks() {
        return this.nearSlabTicks;
    }
    
    public int getOnIceTicks() {
        return this.onIceTicks;
    }
    
    public int getUnderBlockTicks() {
        return this.underBlockTicks;
    }
    
    public String getKey() {
        return this.key;
    }
    
    public double getStrafe() {
        return this.strafe;
    }
    
    public double getForward() {
        return this.forward;
    }
    
    public boolean isInInventory() {
        return this.inInventory;
    }
    
    public boolean isLastVelocityGround() {
        return this.lastVelocityGround;
    }
    
    public PacketPlayInFlying getLastLocationPacket() {
        return this.lastLocationPacket;
    }
    
    public PacketPlayInFlying getCurrentLocationPacket() {
        return this.currentLocationPacket;
    }
    
    public double getDeltaX() {
        return this.deltaX;
    }
    
    public double getDeltaZ() {
        return this.deltaZ;
    }
    
    public double getDeltaXZ() {
        return this.deltaXZ;
    }
    
    public double getDeltaY() {
        return this.deltaY;
    }
    
    public float getDeltaYaw() {
        return this.deltaYaw;
    }
    
    public float getDeltaPitch() {
        return this.deltaPitch;
    }
    
    public double getLastDeltaX() {
        return this.lastDeltaX;
    }
    
    public double getLastDeltaZ() {
        return this.lastDeltaZ;
    }
    
    public double getLastDeltaXZ() {
        return this.lastDeltaXZ;
    }
    
    public double getLastDeltaY() {
        return this.lastDeltaY;
    }
    
    public float getLastDeltaYaw() {
        return this.lastDeltaYaw;
    }
    
    public float getLastDeltaPitch() {
        return this.lastDeltaPitch;
    }
    
    public long getLastMove() {
        return this.lastMove;
    }
    
    public long getLastDelayedMove() {
        return this.lastDelayedMove;
    }
    
    public Map<Short, Long> getTransactionMap() {
        return this.transactionMap;
    }
    
    public Map<Short, Runnable> getSyncTransactionMap() {
        return this.syncTransactionMap;
    }
    
    public Map<Short, Pair<Long, Vector>> getAsyncTransactionMap() {
        return this.asyncTransactionMap;
    }
    
    public Map<Short, Pair<Long, Vector>> getAsyncTransactionTwiceMap() {
        return this.asyncTransactionTwiceMap;
    }
    
    public long getLastServerTransaction() {
        return this.lastServerTransaction;
    }
    
    public boolean isReceivedTransaction() {
        return this.receivedTransaction;
    }
    
    public Queue<Integer> getConnectionFrequency() {
        return this.connectionFrequency;
    }
    
    public int getAverageTransactionPing() {
        return this.averageTransactionPing;
    }
    
    public int getLastReceivedTransactionId() {
        return this.lastReceivedTransactionId;
    }
    
    public Vector getLastVelocity() {
        return this.lastVelocity;
    }
    
    public int getTransactionPing() {
        return this.transactionPing;
    }
    
    public int getLastTransactionPing() {
        return this.lastTransactionPing;
    }
    
    public int getAsyncTransactionPing() {
        return this.asyncTransactionPing;
    }
    
    public int getLastAsyncTransactionPing() {
        return this.lastAsyncTransactionPing;
    }
    
    public int getAsyncTransactionTwicePing() {
        return this.asyncTransactionTwicePing;
    }
    
    public int getLastAsyncTransactionTwicePing() {
        return this.lastAsyncTransactionTwicePing;
    }
    
    public int getLastTargetTicks() {
        return this.lastTargetTicks;
    }
    
    public Entity getTarget() {
        return this.target;
    }
    
    public Entity getLastTarget() {
        return this.lastTarget;
    }
    
    public long getLastClientTransaction() {
        return this.lastClientTransaction;
    }
    
    public double getDistanceTarget() {
        return this.distanceTarget;
    }
    
    public boolean isBanned() {
        return this.banned;
    }
    
    public List<Pair<Location, Integer>> getTargetLocations() {
        return this.targetLocations;
    }
    
    public int getTicks() {
        return this.ticks;
    }
    
    public double getMovingX() {
        return this.movingX;
    }
    
    public double getMovingY() {
        return this.movingY;
    }
    
    public double getMovingZ() {
        return this.movingZ;
    }
    
    public float getMovingYaw() {
        return this.movingYaw;
    }
    
    public float getMovingPitch() {
        return this.movingPitch;
    }
    
    public Location getLastMoveLoc() {
        return this.lastMoveLoc;
    }
    
    public Location getLastLastMoveLoc() {
        return this.lastLastMoveLoc;
    }
    
    public Player getPlayer() {
        return this.player;
    }
    
    public int getLastTransactionReceived() {
        return this.lastTransactionReceived;
    }
    
    public boolean isOnGround() {
        return this.onGround;
    }
    
    public boolean isInLiquid() {
        return this.inLiquid;
    }
    
    public boolean isOnStairSlab() {
        return this.onStairSlab;
    }
    
    public boolean isOnIce() {
        return this.onIce;
    }
    
    public boolean isOnClimbable() {
        return this.onClimbable;
    }
    
    public boolean isUnderBlock() {
        return this.underBlock;
    }
    
    public boolean isFallFlying() {
        return this.fallFlying;
    }
    
    public boolean isDoingTeleport() {
        return this.doingTeleport;
    }
    
    public int getHalfBlockTicks() {
        return this.halfBlockTicks;
    }
    
    public int getLiquidTicks() {
        return this.liquidTicks;
    }
    
    public short getLastTransactionID() {
        return this.lastTransactionID;
    }
    
    public short getLastTransactionIDAsync() {
        return this.lastTransactionIDAsync;
    }
    
    public float getFriction() {
        return this.friction;
    }
    
    public boolean isServerGround() {
        return this.serverGround;
    }
    
    public boolean isNearGround() {
        return this.nearGround;
    }
    
    public boolean isBlockBelow() {
        return this.blockBelow;
    }
    
    public boolean isBlockNear() {
        return this.blockNear;
    }
    
    public boolean isRoseBush() {
        return this.roseBush;
    }
    
    public int getLastDoingBlockUpdateTicks() {
        return this.lastDoingBlockUpdateTicks;
    }
    
    public boolean isLastRoseBush() {
        return this.lastRoseBush;
    }
    
    public boolean isLastBlockBelow() {
        return this.lastBlockBelow;
    }
    
    public boolean isLastBlockNear() {
        return this.lastBlockNear;
    }
    
    public int getBlockTicks() {
        return this.blockTicks;
    }
    
    public long getLastVelocityTaken() {
        return this.lastVelocityTaken;
    }
    
    public long getLastAttackTime() {
        return this.lastAttackTime;
    }
    
    public Entity getLastAttacked() {
        return this.lastAttacked;
    }
    
    public LivingEntity getLastHitEntity() {
        return this.lastHitEntity;
    }
    
    public boolean isBacking() {
        return this.backing;
    }
    
    public int getFakeTimer() {
        return this.fakeTimer;
    }
    
    public int getSendTransactionCount() {
        return this.sendTransactionCount;
    }
    
    public int getSendAsyncTransactionCount() {
        return this.sendAsyncTransactionCount;
    }
    
    public int getSendAsyncTransactionTwiceCount() {
        return this.sendAsyncTransactionTwiceCount;
    }
    
    public int getSendKeepAliveCount() {
        return this.sendKeepAliveCount;
    }
    
    public int getReceivedTransactionCount() {
        return this.receivedTransactionCount;
    }
    
    public long getLastDamageTime() {
        return this.lastDamageTime;
    }
    
    public int getLastFallDamageTicks() {
        return this.lastFallDamageTicks;
    }
    
    public boolean isHasSentPreWavePacket() {
        return this.hasSentPreWavePacket;
    }
    
    public int getReceivedAsyncTransactionCount() {
        return this.receivedAsyncTransactionCount;
    }
    
    public int getReceivedAsyncTransactionTwiceCount() {
        return this.receivedAsyncTransactionTwiceCount;
    }
    
    public int getReceivedKeepAliveCount() {
        return this.receivedKeepAliveCount;
    }
    
    public long getLastReceivedKeepAlive() {
        return this.lastReceivedKeepAlive;
    }
    
    public long getLastSendTransactionTimeAsync() {
        return this.lastSendTransactionTimeAsync;
    }
    
    public long getLastSendTransactionTwiceTimeAsync() {
        return this.lastSendTransactionTwiceTimeAsync;
    }
    
    public long getLastReceivedTransactionTimeAsync() {
        return this.lastReceivedTransactionTimeAsync;
    }
    
    public long getLastReceivedTransactionTwiceTimeAsync() {
        return this.lastReceivedTransactionTwiceTimeAsync;
    }
    
    public int getTransactionPingAsync() {
        return this.transactionPingAsync;
    }
    
    public boolean isStep() {
        return this.step;
    }
    
    public List<Float> getPatterns() {
        return this.patterns;
    }
    
    public float getLastRange() {
        return this.lastRange;
    }
    
    public double getLagBuffer() {
        return this.lagBuffer;
    }
    
    public int getLostTransactionPacketsCount() {
        return this.lostTransactionPacketsCount;
    }
    
    public int getLostAsyncTransactionPacketsCount() {
        return this.lostAsyncTransactionPacketsCount;
    }
    
    public int getLostAsyncTransactionTwicePacketsCount() {
        return this.lostAsyncTransactionTwicePacketsCount;
    }
    
    public int getLastNoTransVelocityTicks() {
        return this.lastNoTransVelocityTicks;
    }
    
    public int getLastNoDamageVelocityTicks() {
        return this.lastNoDamageVelocityTicks;
    }
    
    public void setClientBrand(final String clientBrand) {
        this.clientBrand = clientBrand;
    }
    
    public void setLastBypass(final long lastBypass) {
        this.lastBypass = lastBypass;
    }
    
    public void setLastBlockBreak(final long lastBlockBreak) {
        this.lastBlockBreak = lastBlockBreak;
    }
    
    public void setLastBlockBreakCanceled(final long lastBlockBreakCanceled) {
        this.lastBlockBreakCanceled = lastBlockBreakCanceled;
    }
    
    public void setYawGcdList(final LinkedList<Float> yawGcdList) {
        this.yawGcdList = yawGcdList;
    }
    
    public void setPitchGcdList(final LinkedList<Float> pitchGcdList) {
        this.pitchGcdList = pitchGcdList;
    }
    
    public void setLastHighRateTicks(final int lastHighRateTicks) {
        this.lastHighRateTicks = lastHighRateTicks;
    }
    
    public void setBridge(final boolean bridge) {
        this.bridge = bridge;
    }
    
    public void setCinematicMode(final boolean cinematicMode) {
        this.cinematicMode = cinematicMode;
    }
    
    public void setYawGCD(final float yawGCD) {
        this.yawGCD = yawGCD;
    }
    
    public void setPitchGCD(final float pitchGCD) {
        this.pitchGCD = pitchGCD;
    }
    
    public void setLastYawGCD(final float lastYawGCD) {
        this.lastYawGCD = lastYawGCD;
    }
    
    public void setLastPitchGCD(final float lastPitchGCD) {
        this.lastPitchGCD = lastPitchGCD;
    }
    
    public void setJumped(final boolean jumped) {
        this.jumped = jumped;
    }
    
    public void setAccurateYawData(final boolean accurateYawData) {
        this.accurateYawData = accurateYawData;
    }
    
    public void setExpiermentalDeltaX(final float expiermentalDeltaX) {
        this.expiermentalDeltaX = expiermentalDeltaX;
    }
    
    public void setExpiermentalDeltaY(final float expiermentalDeltaY) {
        this.expiermentalDeltaY = expiermentalDeltaY;
    }
    
    public void setMxaxis(final MouseFilter mxaxis) {
        this.mxaxis = mxaxis;
    }
    
    public void setMyaxis(final MouseFilter myaxis) {
        this.myaxis = myaxis;
    }
    
    public void setLastDamageByPlayer(final boolean lastDamageByPlayer) {
        this.lastDamageByPlayer = lastDamageByPlayer;
    }
    
    public void setReceivedTransactionVelocityOnce(final boolean receivedTransactionVelocityOnce) {
        this.receivedTransactionVelocityOnce = receivedTransactionVelocityOnce;
    }
    
    public void setReceivedTransactionVelocityTwice(final boolean receivedTransactionVelocityTwice) {
        this.receivedTransactionVelocityTwice = receivedTransactionVelocityTwice;
    }
    
    public void setOnWebTicks(final int onWebTicks) {
        this.onWebTicks = onWebTicks;
    }
    
    public void setInVehicleTicks(final int inVehicleTicks) {
        this.inVehicleTicks = inVehicleTicks;
    }
    
    public void setDeadTicks(final int deadTicks) {
        this.deadTicks = deadTicks;
    }
    
    public void setMoved(final boolean moved) {
        this.moved = moved;
    }
    
    public void setOutlier(final double outlier) {
        this.outlier = outlier;
    }
    
    public void setKurtosis(final double kurtosis) {
        this.kurtosis = kurtosis;
    }
    
    public void setSkewness(final double skewness) {
        this.skewness = skewness;
    }
    
    public void setStd(final double std) {
        this.std = std;
    }
    
    public void setMedian(final double median) {
        this.median = median;
    }
    
    public void setCurrentCps(final double currentCps) {
        this.currentCps = currentCps;
    }
    
    public void setAverageCps(final double averageCps) {
        this.averageCps = averageCps;
    }
    
    public void setOutlierTuple(final Tuple<List<Double>, List<Double>> outlierTuple) {
        this.outlierTuple = outlierTuple;
    }
    
    public void setMovementTicks(final int movementTicks) {
        this.movementTicks = movementTicks;
    }
    
    public void setMovements(final List<Integer> movements) {
        this.movements = movements;
    }
    
    public void setLastPosX(final double lastPosX) {
        this.lastPosX = lastPosX;
    }
    
    public void setLastPosY(final double lastPosY) {
        this.lastPosY = lastPosY;
    }
    
    public void setLastPosZ(final double lastPosZ) {
        this.lastPosZ = lastPosZ;
    }
    
    public void setLastPitch(final float lastPitch) {
        this.lastPitch = lastPitch;
    }
    
    public void setLastGroundBoolean(final boolean lastGroundBoolean) {
        this.lastGroundBoolean = lastGroundBoolean;
    }
    
    public void setLastYaw(final float lastYaw) {
        this.lastYaw = lastYaw;
    }
    
    public void setLastRespawn(final long lastRespawn) {
        this.lastRespawn = lastRespawn;
    }
    
    public void setFenceTicks(final int fenceTicks) {
        this.fenceTicks = fenceTicks;
    }
    
    public void setReceivedVelocity(final boolean receivedVelocity) {
        this.receivedVelocity = receivedVelocity;
    }
    
    public void setPingQueue(final Queue<BiConsumer<Integer, Double>> pingQueue) {
        this.pingQueue = pingQueue;
    }
    
    public void setEntityMap(final ConcurrentHashMap<Integer, PlayerReachEntity> entityMap) {
        this.entityMap = entityMap;
    }
    
    public void setPlayerAttackQueue(final ConcurrentLinkedQueue<Integer> playerAttackQueue) {
        this.playerAttackQueue = playerAttackQueue;
    }
    
    public void setLastPosition(final boolean lastPosition) {
        this.lastPosition = lastPosition;
    }
    
    public void setPosition(final boolean position) {
        this.position = position;
    }
    
    public void setLastSyncTransactionSendId(final short lastSyncTransactionSendId) {
        this.lastSyncTransactionSendId = lastSyncTransactionSendId;
    }
    
    public void setLastTeleportLoc(final Location lastTeleportLoc) {
        this.lastTeleportLoc = lastTeleportLoc;
    }
    
    public void setLastNotTeleportLoc(final Location lastNotTeleportLoc) {
        this.lastNotTeleportLoc = lastNotTeleportLoc;
    }
    
    public void setRecentMoveMap(final Map<Integer, Deque<Pair<Long, Location>>> recentMoveMap) {
        this.recentMoveMap = recentMoveMap;
    }
    
    public void setAllVelocityData(final List<VelocityData> allVelocityData) {
        this.allVelocityData = allVelocityData;
    }
    
    public void setSprintTicks(final int sprintTicks) {
        this.sprintTicks = sprintTicks;
    }
    
    public void setTpUnknown(final boolean tpUnknown) {
        this.tpUnknown = tpUnknown;
    }
    
    public void setReachData(final Queue<ReachData> reachData) {
        this.reachData = reachData;
    }
    
    public void setAlerts(final boolean alerts) {
        this.alerts = alerts;
    }
    
    public void setBackTicks(final int backTicks) {
        this.backTicks = backTicks;
    }
    
    public void setFallBackTicks(final int fallBackTicks) {
        this.fallBackTicks = fallBackTicks;
    }
    
    public void setBackTime(final long backTime) {
        this.backTime = backTime;
    }
    
    public void setFallBackTime(final long fallBackTime) {
        this.fallBackTime = fallBackTime;
    }
    
    public void setLastFast(final long lastFast) {
        this.lastFast = lastFast;
    }
    
    public void setChecks(final Set<Check> checks) {
        this.checks = checks;
    }
    
    public void setCanCheck(final boolean canCheck) {
        this.canCheck = canCheck;
    }
    
    public void setLastReceivedPacket(final Packet lastReceivedPacket) {
        this.lastReceivedPacket = lastReceivedPacket;
    }
    
    public void setLastSentPacket(final Packet lastSentPacket) {
        this.lastSentPacket = lastSentPacket;
    }
    
    public void setLastReceivedPacketTime(final long lastReceivedPacketTime) {
        this.lastReceivedPacketTime = lastReceivedPacketTime;
    }
    
    public void setLastSentPacketTime(final long lastSentPacketTime) {
        this.lastSentPacketTime = lastSentPacketTime;
    }
    
    public void setTransactionIDCounter(final int transactionIDCounter) {
        this.transactionIDCounter = transactionIDCounter;
    }
    
    public void setSmoothYaw(final float smoothYaw) {
        this.smoothYaw = smoothYaw;
    }
    
    public void setSmoothPitch(final float smoothPitch) {
        this.smoothPitch = smoothPitch;
    }
    
    public void setLsmoothYaw(final float lsmoothYaw) {
        this.lsmoothYaw = lsmoothYaw;
    }
    
    public void setLsmoothPitch(final float lsmoothPitch) {
        this.lsmoothPitch = lsmoothPitch;
    }
    
    public void setYawOutliers(final Tuple<List<Float>, List<Float>> yawOutliers) {
        this.yawOutliers = yawOutliers;
    }
    
    public void setPitchOutliers(final Tuple<List<Float>, List<Float>> pitchOutliers) {
        this.pitchOutliers = pitchOutliers;
    }
    
    public void setSensitivityX(final float sensitivityX) {
        this.sensitivityX = sensitivityX;
    }
    
    public void setSensitivityY(final float sensitivityY) {
        this.sensitivityY = sensitivityY;
    }
    
    public void setCurrentSensX(final float currentSensX) {
        this.currentSensX = currentSensX;
    }
    
    public void setCurrentSensY(final float currentSensY) {
        this.currentSensY = currentSensY;
    }
    
    public void setSensitivityMcp(final float sensitivityMcp) {
        this.sensitivityMcp = sensitivityMcp;
    }
    
    public void setYawMode(final float yawMode) {
        this.yawMode = yawMode;
    }
    
    public void setPitchMode(final float pitchMode) {
        this.pitchMode = pitchMode;
    }
    
    public void setSensXPercent(final int sensXPercent) {
        this.sensXPercent = sensXPercent;
    }
    
    public void setSensYPercent(final int sensYPercent) {
        this.sensYPercent = sensYPercent;
    }
    
    public void setSmoothCamFilterX(final float smoothCamFilterX) {
        this.smoothCamFilterX = smoothCamFilterX;
    }
    
    public void setSmoothCamFilterY(final float smoothCamFilterY) {
        this.smoothCamFilterY = smoothCamFilterY;
    }
    
    public void setSmoothCamYaw(final float smoothCamYaw) {
        this.smoothCamYaw = smoothCamYaw;
    }
    
    public void setSmoothCamPitch(final float smoothCamPitch) {
        this.smoothCamPitch = smoothCamPitch;
    }
    
    public void setLastTransactionSent(final AtomicInteger lastTransactionSent) {
        this.lastTransactionSent = lastTransactionSent;
    }
    
    public void setFallDistance(final double fallDistance) {
        this.fallDistance = fallDistance;
    }
    
    public void setLastNonMoveTicks(final int lastNonMoveTicks) {
        this.lastNonMoveTicks = lastNonMoveTicks;
    }
    
    public void setAboveBlockTicks(final int aboveBlockTicks) {
        this.aboveBlockTicks = aboveBlockTicks;
    }
    
    public void setLastAttackTicks(final int lastAttackTicks) {
        this.lastAttackTicks = lastAttackTicks;
    }
    
    public void setCheckingPacket(final boolean checkingPacket) {
        this.checkingPacket = checkingPacket;
    }
    
    public void setPing(final int ping) {
        this.ping = ping;
    }
    
    public void setWaterTicks(final int waterTicks) {
        this.waterTicks = waterTicks;
    }
    
    public void setLastBlockDigTicks(final long lastBlockDigTicks) {
        this.lastBlockDigTicks = lastBlockDigTicks;
    }
    
    public void setLastBlockPlacedTicks(final long lastBlockPlacedTicks) {
        this.lastBlockPlacedTicks = lastBlockPlacedTicks;
    }
    
    public void setLastBlockPlacedCanceledTicks(final long lastBlockPlacedCanceledTicks) {
        this.lastBlockPlacedCanceledTicks = lastBlockPlacedCanceledTicks;
    }
    
    public void setVelXTicks(final int velXTicks) {
        this.velXTicks = velXTicks;
    }
    
    public void setVelYTicks(final int velYTicks) {
        this.velYTicks = velYTicks;
    }
    
    public void setVelZTicks(final int velZTicks) {
        this.velZTicks = velZTicks;
    }
    
    public void setBouncedOnSlime(final boolean bouncedOnSlime) {
        this.bouncedOnSlime = bouncedOnSlime;
    }
    
    public void setLastSlimeLocation(final Location lastSlimeLocation) {
        this.lastSlimeLocation = lastSlimeLocation;
    }
    
    public void setAirTicks(final int airTicks) {
        this.airTicks = airTicks;
    }
    
    public void setGroundTicks(final int groundTicks) {
        this.groundTicks = groundTicks;
    }
    
    public void setClientAirTicks(final int clientAirTicks) {
        this.clientAirTicks = clientAirTicks;
    }
    
    public void setClientGroundTicks(final int clientGroundTicks) {
        this.clientGroundTicks = clientGroundTicks;
    }
    
    public void setAirTicks2(final int airTicks2) {
        this.airTicks2 = airTicks2;
    }
    
    public void setGroundTicks2(final int groundTicks2) {
        this.groundTicks2 = groundTicks2;
    }
    
    public void setClientAirTicks2(final int clientAirTicks2) {
        this.clientAirTicks2 = clientAirTicks2;
    }
    
    public void setClientGroundTicks2(final int clientGroundTicks2) {
        this.clientGroundTicks2 = clientGroundTicks2;
    }
    
    public void setSolidBlockBehindTicks(final int solidBlockBehindTicks) {
        this.solidBlockBehindTicks = solidBlockBehindTicks;
    }
    
    public void setNearClimbTicks(final int nearClimbTicks) {
        this.nearClimbTicks = nearClimbTicks;
    }
    
    public void setLastOffset(final int lastOffset) {
        this.lastOffset = lastOffset;
    }
    
    public void setLastDistance(final double lastDistance) {
        this.lastDistance = lastDistance;
    }
    
    public void setVelocityGroundTicks(final int velocityGroundTicks) {
        this.velocityGroundTicks = velocityGroundTicks;
    }
    
    public void setFlyingTicks(final int flyingTicks) {
        this.flyingTicks = flyingTicks;
    }
    
    public void setGroundLoc(final Location groundLoc) {
        this.groundLoc = groundLoc;
    }
    
    public void setLastGroundLoc(final Location lastGroundLoc) {
        this.lastGroundLoc = lastGroundLoc;
    }
    
    public void setLastLastGroundLoc(final Location lastLastGroundLoc) {
        this.lastLastGroundLoc = lastLastGroundLoc;
    }
    
    public void setLastLastLastGroundLoc(final Location lastLastLastGroundLoc) {
        this.lastLastLastGroundLoc = lastLastLastGroundLoc;
    }
    
    public void setLastLastLastLastGroundLoc(final Location lastLastLastLastGroundLoc) {
        this.lastLastLastLastGroundLoc = lastLastLastLastGroundLoc;
    }
    
    public void setLastMovePlayerLoc(final Location lastMovePlayerLoc) {
        this.lastMovePlayerLoc = lastMovePlayerLoc;
    }
    
    public void setLastMovePacket(final Location lastMovePacket) {
        this.lastMovePacket = lastMovePacket;
    }
    
    public void setLastLastMovePacket(final Location lastLastMovePacket) {
        this.lastLastMovePacket = lastLastMovePacket;
    }
    
    public void setStairsTicks(final double stairsTicks) {
        this.stairsTicks = stairsTicks;
    }
    
    public void setPistonTicks(final double pistonTicks) {
        this.pistonTicks = pistonTicks;
    }
    
    public void setLastLag(final long lastLag) {
        this.lastLag = lastLag;
    }
    
    public void setSetBackTicks(final int setBackTicks) {
        this.setBackTicks = setBackTicks;
    }
    
    public void setLastVelMS(final long LastVelMS) {
        this.LastVelMS = LastVelMS;
    }
    
    public void setSprint(final boolean sprint) {
        this.sprint = sprint;
    }
    
    public void setLastSprint(final boolean lastSprint) {
        this.lastSprint = lastSprint;
    }
    
    public void setLastVelocityTicks(final int lastVelocityTicks) {
        this.lastVelocityTicks = lastVelocityTicks;
    }
    
    public void setMovePacketTicks(final int movePacketTicks) {
        this.movePacketTicks = movePacketTicks;
    }
    
    public void setLastDelayedPacket(final long lastDelayedPacket) {
        this.lastDelayedPacket = lastDelayedPacket;
    }
    
    public void setLastPlayerPacket(final long lastPlayerPacket) {
        this.lastPlayerPacket = lastPlayerPacket;
    }
    
    public void setPreviousSwingTicks(final int previousSwingTicks) {
        this.previousSwingTicks = previousSwingTicks;
    }
    
    public void setSetbackLocation(final Location setbackLocation) {
        this.setbackLocation = setbackLocation;
    }
    
    public void setLastTeleport(final long lastTeleport) {
        this.lastTeleport = lastTeleport;
    }
    
    public void setLastTeleportDid(final long lastTeleportDid) {
        this.lastTeleportDid = lastTeleportDid;
    }
    
    public void setLastTeleportTicks(final int lastTeleportTicks) {
        this.lastTeleportTicks = lastTeleportTicks;
    }
    
    public void setLastAirTime(final long lastAirTime) {
        this.lastAirTime = lastAirTime;
    }
    
    public void setLastPacket(final long lastPacket) {
        this.lastPacket = lastPacket;
    }
    
    public void setLastGround(final Location lastGround) {
        this.lastGround = lastGround;
    }
    
    public void setLastGroundTime(final long lastGroundTime) {
        this.lastGroundTime = lastGroundTime;
    }
    
    public void setLastAimTime(final long lastAimTime) {
        this.lastAimTime = lastAimTime;
    }
    
    public void setHitSlowdownTicks(final int hitSlowdownTicks) {
        this.hitSlowdownTicks = hitSlowdownTicks;
    }
    
    public void setAboveSlimeTicks(final int aboveSlimeTicks) {
        this.aboveSlimeTicks = aboveSlimeTicks;
    }
    
    public void setNearSlabTicks(final int nearSlabTicks) {
        this.nearSlabTicks = nearSlabTicks;
    }
    
    public void setOnIceTicks(final int onIceTicks) {
        this.onIceTicks = onIceTicks;
    }
    
    public void setUnderBlockTicks(final int underBlockTicks) {
        this.underBlockTicks = underBlockTicks;
    }
    
    public void setKey(final String key) {
        this.key = key;
    }
    
    public void setStrafe(final double strafe) {
        this.strafe = strafe;
    }
    
    public void setForward(final double forward) {
        this.forward = forward;
    }
    
    public void setInInventory(final boolean inInventory) {
        this.inInventory = inInventory;
    }
    
    public void setLastVelocityGround(final boolean lastVelocityGround) {
        this.lastVelocityGround = lastVelocityGround;
    }
    
    public void setLastLocationPacket(final PacketPlayInFlying lastLocationPacket) {
        this.lastLocationPacket = lastLocationPacket;
    }
    
    public void setCurrentLocationPacket(final PacketPlayInFlying currentLocationPacket) {
        this.currentLocationPacket = currentLocationPacket;
    }
    
    public void setDeltaX(final double deltaX) {
        this.deltaX = deltaX;
    }
    
    public void setDeltaZ(final double deltaZ) {
        this.deltaZ = deltaZ;
    }
    
    public void setDeltaXZ(final double deltaXZ) {
        this.deltaXZ = deltaXZ;
    }
    
    public void setDeltaY(final double deltaY) {
        this.deltaY = deltaY;
    }
    
    public void setDeltaYaw(final float deltaYaw) {
        this.deltaYaw = deltaYaw;
    }
    
    public void setDeltaPitch(final float deltaPitch) {
        this.deltaPitch = deltaPitch;
    }
    
    public void setLastDeltaX(final double lastDeltaX) {
        this.lastDeltaX = lastDeltaX;
    }
    
    public void setLastDeltaZ(final double lastDeltaZ) {
        this.lastDeltaZ = lastDeltaZ;
    }
    
    public void setLastDeltaXZ(final double lastDeltaXZ) {
        this.lastDeltaXZ = lastDeltaXZ;
    }
    
    public void setLastDeltaY(final double lastDeltaY) {
        this.lastDeltaY = lastDeltaY;
    }
    
    public void setLastDeltaYaw(final float lastDeltaYaw) {
        this.lastDeltaYaw = lastDeltaYaw;
    }
    
    public void setLastDeltaPitch(final float lastDeltaPitch) {
        this.lastDeltaPitch = lastDeltaPitch;
    }
    
    public void setLastMove(final long lastMove) {
        this.lastMove = lastMove;
    }
    
    public void setLastDelayedMove(final long lastDelayedMove) {
        this.lastDelayedMove = lastDelayedMove;
    }
    
    public void setTransactionMap(final Map<Short, Long> transactionMap) {
        this.transactionMap = transactionMap;
    }
    
    public void setSyncTransactionMap(final Map<Short, Runnable> syncTransactionMap) {
        this.syncTransactionMap = syncTransactionMap;
    }
    
    public void setAsyncTransactionMap(final Map<Short, Pair<Long, Vector>> asyncTransactionMap) {
        this.asyncTransactionMap = asyncTransactionMap;
    }
    
    public void setAsyncTransactionTwiceMap(final Map<Short, Pair<Long, Vector>> asyncTransactionTwiceMap) {
        this.asyncTransactionTwiceMap = asyncTransactionTwiceMap;
    }
    
    public void setLastServerTransaction(final long lastServerTransaction) {
        this.lastServerTransaction = lastServerTransaction;
    }
    
    public void setReceivedTransaction(final boolean receivedTransaction) {
        this.receivedTransaction = receivedTransaction;
    }
    
    public void setConnectionFrequency(final Queue<Integer> connectionFrequency) {
        this.connectionFrequency = connectionFrequency;
    }
    
    public void setAverageTransactionPing(final int averageTransactionPing) {
        this.averageTransactionPing = averageTransactionPing;
    }
    
    public void setLastReceivedTransactionId(final int lastReceivedTransactionId) {
        this.lastReceivedTransactionId = lastReceivedTransactionId;
    }
    
    public void setLastVelocity(final Vector lastVelocity) {
        this.lastVelocity = lastVelocity;
    }
    
    public void setTransactionPing(final int transactionPing) {
        this.transactionPing = transactionPing;
    }
    
    public void setLastTransactionPing(final int lastTransactionPing) {
        this.lastTransactionPing = lastTransactionPing;
    }
    
    public void setAsyncTransactionPing(final int asyncTransactionPing) {
        this.asyncTransactionPing = asyncTransactionPing;
    }
    
    public void setLastAsyncTransactionPing(final int lastAsyncTransactionPing) {
        this.lastAsyncTransactionPing = lastAsyncTransactionPing;
    }
    
    public void setAsyncTransactionTwicePing(final int asyncTransactionTwicePing) {
        this.asyncTransactionTwicePing = asyncTransactionTwicePing;
    }
    
    public void setLastAsyncTransactionTwicePing(final int lastAsyncTransactionTwicePing) {
        this.lastAsyncTransactionTwicePing = lastAsyncTransactionTwicePing;
    }
    
    public void setLastTargetTicks(final int lastTargetTicks) {
        this.lastTargetTicks = lastTargetTicks;
    }
    
    public void setTarget(final Entity target) {
        this.target = target;
    }
    
    public void setLastTarget(final Entity lastTarget) {
        this.lastTarget = lastTarget;
    }
    
    public void setLastClientTransaction(final long lastClientTransaction) {
        this.lastClientTransaction = lastClientTransaction;
    }
    
    public void setDistanceTarget(final double distanceTarget) {
        this.distanceTarget = distanceTarget;
    }
    
    public void setBanned(final boolean banned) {
        this.banned = banned;
    }
    
    public void setTargetLocations(final List<Pair<Location, Integer>> targetLocations) {
        this.targetLocations = targetLocations;
    }
    
    public void setTicks(final int ticks) {
        this.ticks = ticks;
    }
    
    public void setMovingX(final double movingX) {
        this.movingX = movingX;
    }
    
    public void setMovingY(final double movingY) {
        this.movingY = movingY;
    }
    
    public void setMovingZ(final double movingZ) {
        this.movingZ = movingZ;
    }
    
    public void setMovingYaw(final float movingYaw) {
        this.movingYaw = movingYaw;
    }
    
    public void setMovingPitch(final float movingPitch) {
        this.movingPitch = movingPitch;
    }
    
    public void setLastMoveLoc(final Location lastMoveLoc) {
        this.lastMoveLoc = lastMoveLoc;
    }
    
    public void setLastLastMoveLoc(final Location lastLastMoveLoc) {
        this.lastLastMoveLoc = lastLastMoveLoc;
    }
    
    public void setPlayer(final Player player) {
        this.player = player;
    }
    
    public void setLastTransactionReceived(final int lastTransactionReceived) {
        this.lastTransactionReceived = lastTransactionReceived;
    }
    
    public void setOnGround(final boolean onGround) {
        this.onGround = onGround;
    }
    
    public void setInLiquid(final boolean inLiquid) {
        this.inLiquid = inLiquid;
    }
    
    public void setOnStairSlab(final boolean onStairSlab) {
        this.onStairSlab = onStairSlab;
    }
    
    public void setOnIce(final boolean onIce) {
        this.onIce = onIce;
    }
    
    public void setOnClimbable(final boolean onClimbable) {
        this.onClimbable = onClimbable;
    }
    
    public void setUnderBlock(final boolean underBlock) {
        this.underBlock = underBlock;
    }
    
    public void setFallFlying(final boolean fallFlying) {
        this.fallFlying = fallFlying;
    }
    
    public void setDoingTeleport(final boolean doingTeleport) {
        this.doingTeleport = doingTeleport;
    }
    
    public void setMoveTicks(final int moveTicks) {
        this.moveTicks = moveTicks;
    }
    
    public void setHalfBlockTicks(final int halfBlockTicks) {
        this.halfBlockTicks = halfBlockTicks;
    }
    
    public void setProcessPacketRunnables(final List<Runnable> processPacketRunnables) {
        this.processPacketRunnables = processPacketRunnables;
    }
    
    public void setLiquidTicks(final int liquidTicks) {
        this.liquidTicks = liquidTicks;
    }
    
    public void setLastTransactionID(final short lastTransactionID) {
        this.lastTransactionID = lastTransactionID;
    }
    
    public void setLastTransactionIDAsync(final short lastTransactionIDAsync) {
        this.lastTransactionIDAsync = lastTransactionIDAsync;
    }
    
    public void setFriction(final float friction) {
        this.friction = friction;
    }
    
    public void setServerGround(final boolean serverGround) {
        this.serverGround = serverGround;
    }
    
    public void setNearGround(final boolean nearGround) {
        this.nearGround = nearGround;
    }
    
    public void setBlockBelow(final boolean blockBelow) {
        this.blockBelow = blockBelow;
    }
    
    public void setBlockNear(final boolean blockNear) {
        this.blockNear = blockNear;
    }
    
    public void setRoseBush(final boolean roseBush) {
        this.roseBush = roseBush;
    }
    
    public void setLastDoingBlockUpdateTicks(final int lastDoingBlockUpdateTicks) {
        this.lastDoingBlockUpdateTicks = lastDoingBlockUpdateTicks;
    }
    
    public void setLastRoseBush(final boolean lastRoseBush) {
        this.lastRoseBush = lastRoseBush;
    }
    
    public void setLastBlockBelow(final boolean lastBlockBelow) {
        this.lastBlockBelow = lastBlockBelow;
    }
    
    public void setLastBlockNear(final boolean lastBlockNear) {
        this.lastBlockNear = lastBlockNear;
    }
    
    public void setBlockTicks(final int blockTicks) {
        this.blockTicks = blockTicks;
    }
    
    public void setLastVelocityTaken(final long lastVelocityTaken) {
        this.lastVelocityTaken = lastVelocityTaken;
    }
    
    public void setLastAttackTime(final long lastAttackTime) {
        this.lastAttackTime = lastAttackTime;
    }
    
    public void setLastAttacked(final Entity lastAttacked) {
        this.lastAttacked = lastAttacked;
    }
    
    public void setLastHitEntity(final LivingEntity lastHitEntity) {
        this.lastHitEntity = lastHitEntity;
    }
    
    public void setBacking(final boolean backing) {
        this.backing = backing;
    }
    
    public void setFakeTimer(final int fakeTimer) {
        this.fakeTimer = fakeTimer;
    }
    
    public void setSendTransactionCount(final int sendTransactionCount) {
        this.sendTransactionCount = sendTransactionCount;
    }
    
    public void setSendAsyncTransactionCount(final int sendAsyncTransactionCount) {
        this.sendAsyncTransactionCount = sendAsyncTransactionCount;
    }
    
    public void setSendAsyncTransactionTwiceCount(final int sendAsyncTransactionTwiceCount) {
        this.sendAsyncTransactionTwiceCount = sendAsyncTransactionTwiceCount;
    }
    
    public void setSendKeepAliveCount(final int sendKeepAliveCount) {
        this.sendKeepAliveCount = sendKeepAliveCount;
    }
    
    public void setReceivedTransactionCount(final int receivedTransactionCount) {
        this.receivedTransactionCount = receivedTransactionCount;
    }
    
    public void setLastDamageTime(final long lastDamageTime) {
        this.lastDamageTime = lastDamageTime;
    }
    
    public void setLastFallDamageTicks(final int lastFallDamageTicks) {
        this.lastFallDamageTicks = lastFallDamageTicks;
    }
    
    public void setHasSentPreWavePacket(final boolean hasSentPreWavePacket) {
        this.hasSentPreWavePacket = hasSentPreWavePacket;
    }
    
    public void setReceivedAsyncTransactionCount(final int receivedAsyncTransactionCount) {
        this.receivedAsyncTransactionCount = receivedAsyncTransactionCount;
    }
    
    public void setReceivedAsyncTransactionTwiceCount(final int receivedAsyncTransactionTwiceCount) {
        this.receivedAsyncTransactionTwiceCount = receivedAsyncTransactionTwiceCount;
    }
    
    public void setReceivedKeepAliveCount(final int receivedKeepAliveCount) {
        this.receivedKeepAliveCount = receivedKeepAliveCount;
    }
    
    public void setLastReceivedKeepAlive(final long lastReceivedKeepAlive) {
        this.lastReceivedKeepAlive = lastReceivedKeepAlive;
    }
    
    public void setLastSendTransactionTimeAsync(final long lastSendTransactionTimeAsync) {
        this.lastSendTransactionTimeAsync = lastSendTransactionTimeAsync;
    }
    
    public void setLastSendTransactionTwiceTimeAsync(final long lastSendTransactionTwiceTimeAsync) {
        this.lastSendTransactionTwiceTimeAsync = lastSendTransactionTwiceTimeAsync;
    }
    
    public void setLastReceivedTransactionTimeAsync(final long lastReceivedTransactionTimeAsync) {
        this.lastReceivedTransactionTimeAsync = lastReceivedTransactionTimeAsync;
    }
    
    public void setLastReceivedTransactionTwiceTimeAsync(final long lastReceivedTransactionTwiceTimeAsync) {
        this.lastReceivedTransactionTwiceTimeAsync = lastReceivedTransactionTwiceTimeAsync;
    }
    
    public void setTransactionPingAsync(final int transactionPingAsync) {
        this.transactionPingAsync = transactionPingAsync;
    }
    
    public void setStep(final boolean step) {
        this.step = step;
    }
    
    public void setPatterns(final List<Float> patterns) {
        this.patterns = patterns;
    }
    
    public void setLastRange(final float lastRange) {
        this.lastRange = lastRange;
    }
    
    public void setLagBuffer(final double lagBuffer) {
        this.lagBuffer = lagBuffer;
    }
    
    public void setLostTransactionPacketsCount(final int lostTransactionPacketsCount) {
        this.lostTransactionPacketsCount = lostTransactionPacketsCount;
    }
    
    public void setLostAsyncTransactionPacketsCount(final int lostAsyncTransactionPacketsCount) {
        this.lostAsyncTransactionPacketsCount = lostAsyncTransactionPacketsCount;
    }
    
    public void setLostAsyncTransactionTwicePacketsCount(final int lostAsyncTransactionTwicePacketsCount) {
        this.lostAsyncTransactionTwicePacketsCount = lostAsyncTransactionTwicePacketsCount;
    }
    
    public void setLastNoTransVelocityTicks(final int lastNoTransVelocityTicks) {
        this.lastNoTransVelocityTicks = lastNoTransVelocityTicks;
    }
    
    public void setLastNoDamageVelocityTicks(final int lastNoDamageVelocityTicks) {
        this.lastNoDamageVelocityTicks = lastNoDamageVelocityTicks;
    }
    
    public List<Runnable> getProcessPacketRunnables() {
        return this.processPacketRunnables;
    }
}
