// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.util;

import net.minecraft.server.v1_8_R3.AxisAlignedBB;
import net.minecraft.server.v1_8_R3.BlockPosition;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import net.minecraft.server.v1_8_R3.PacketPlayOutMultiBlockChange;
import net.minecraft.server.v1_8_R3.PacketPlayOutBlockChange;
import net.minecraft.server.v1_8_R3.PacketPlayOutCloseWindow;
import net.minecraft.server.v1_8_R3.PacketPlayOutOpenWindow;
import net.minecraft.server.v1_8_R3.PacketPlayOutKeepAlive;
import io.netty.channel.ChannelPromise;
import net.minecraft.server.v1_8_R3.Entity;
import java.util.Iterator;
import gg.noob.plunder.data.PlayerData;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInCloseWindow;
import net.minecraft.server.v1_8_R3.PacketPlayInClientCommand;
import net.minecraft.server.v1_8_R3.PacketPlayInKeepAlive;
import org.bukkit.event.Event;
import org.bukkit.Bukkit;
import gg.noob.plunder.event.PacketUseEntityEvent;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;
import gg.noob.plunder.checks.Check;
import org.bukkit.Location;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import gg.noob.plunder.Plunder;
import io.netty.channel.ChannelHandlerContext;
import org.bukkit.entity.Player;
import io.netty.channel.ChannelDuplexHandler;

public class PacketHandler extends ChannelDuplexHandler
{
    private Player player;
    
    public PacketHandler(final Player player) {
        this.player = player;
    }
    
    public void channelRead(final ChannelHandlerContext channelHandlerContext, final Object packet) throws Exception {
        if (!this.player.isOnline()) {
            return;
        }
        if (Plunder.getInstance() == null) {
            return;
        }
        final PlayerData data = Plunder.getInstance().getDataManager().getPlayerData(this.player);
        if (data == null) {
            return;
        }
        try {
            if (data.isCanCheck()) {
                boolean cancel = false;
                if (packet instanceof PacketPlayInFlying) {
                    final PacketPlayInFlying packetPlayInFlying = (PacketPlayInFlying)packet;
                    final double lastPosX = data.lastPosX;
                    final double lastPosY = data.lastPosY;
                    final double lastPosZ = data.lastPosZ;
                    final float lastYaw = data.lastYaw;
                    final float lastPitch = data.lastPitch;
                    final boolean lastGround = data.lastGroundBoolean;
                    final Location to = new Location(this.player.getWorld(), packetPlayInFlying.a(), packetPlayInFlying.b(), packetPlayInFlying.c(), packetPlayInFlying.d(), packetPlayInFlying.e());
                    final Location from = new Location(this.player.getWorld(), lastPosX, lastPosY, lastPosZ, lastYaw, lastPitch);
                    if (!packetPlayInFlying.g() || packetPlayInFlying.b() == -999.0) {
                        to.setX(from.getX());
                        to.setY(from.getY());
                        to.setZ(from.getZ());
                    }
                    if (!packetPlayInFlying.h()) {
                        to.setYaw(from.getYaw());
                        to.setPitch(from.getPitch());
                    }
                    final double delta = to.getX() - from.getX() + to.getY() - from.getY() + to.getZ() - from.getZ();
                    final double deltaRota = to.getYaw() - from.getYaw() + to.getPitch() - from.getPitch();
                    dataMoveListen(this.player, data, to, from, packetPlayInFlying);
                    for (final Check check : data.getChecks()) {
                        try {
                            if (delta != 0.0) {
                                check.handleLocationUpdate(this.player, to, from, packetPlayInFlying);
                                check.handleLocationUpdate(this.player, to, from, packetPlayInFlying, lastGround);
                                cancel = (cancel || check.handleLocationUpdateCanceled(this.player, to, from, packetPlayInFlying));
                                cancel = (cancel || check.handleLocationUpdateCanceled(this.player, to, from, packetPlayInFlying, lastGround));
                            }
                            if (deltaRota == 0.0) {
                                continue;
                            }
                            check.handleRotationUpdate(this.player, to, from, packetPlayInFlying);
                            check.handleRotationUpdate(this.player, to, from, packetPlayInFlying, lastGround);
                            cancel = (cancel || check.handleRotationUpdateCanceled(this.player, to, from, packetPlayInFlying));
                            cancel = (cancel || check.handleRotationUpdateCanceled(this.player, to, from, packetPlayInFlying, lastGround));
                        }
                        catch (Exception ex) {
                            System.out.println(ex.getMessage());
                            for (final StackTraceElement stackTraceElement : ex.getStackTrace()) {
                                System.out.println(stackTraceElement.getClassName() + ":" + stackTraceElement.getLineNumber());
                            }
                            alertErrorToStaff(ex);
                        }
                    }
                    if (delta != 0.0) {
                        data.setLastLastLastLastGroundLoc(data.getLastLastGroundLoc());
                        data.setLastLastLastGroundLoc(data.getLastLastGroundLoc());
                        data.setLastLastGroundLoc(data.getLastGroundLoc());
                        data.setLastGroundLoc(data.getGroundLoc());
                        if (packetPlayInFlying.f()) {
                            data.setGroundLoc(to);
                            data.fallFlying = false;
                        }
                        else {
                            data.setGroundLoc(null);
                        }
                    }
                }
                else if (packet instanceof PacketPlayInUseEntity) {
                    final PacketPlayInUseEntity packetNMS = (PacketPlayInUseEntity)packet;
                    if (packetNMS.a() != null && packetNMS.a() == PacketPlayInUseEntity.EnumEntityUseAction.ATTACK) {
                        final PacketPlayInUseEntity.EnumEntityUseAction type = packetNMS.a();
                        data.setHitSlowdownTicks(data.getTicks());
                        final Entity nmsEntity = packetNMS.a((World)((CraftWorld)this.player.getWorld()).getHandle());
                        if (nmsEntity == null) {
                            return;
                        }
                        final org.bukkit.entity.Entity entity = (org.bukkit.entity.Entity)nmsEntity.getBukkitEntity();
                        if (entity == null) {
                            return;
                        }
                        final PacketUseEntityEvent packetUseEntityEvent = new PacketUseEntityEvent(type, this.player, entity, entity.getEntityId());
                        Bukkit.getServer().getPluginManager().callEvent((Event)packetUseEntityEvent);
                        if (packetUseEntityEvent.isCancelled()) {
                            return;
                        }
                    }
                }
                else if (packet instanceof PacketPlayInKeepAlive) {
                    final PlayerData playerData = data;
                    ++playerData.receivedKeepAliveCount;
                    data.lastReceivedKeepAlive = System.currentTimeMillis();
                }
                else if (packet instanceof PacketPlayInClientCommand) {
                    final PacketPlayInClientCommand packetPlayInClientCommand = (PacketPlayInClientCommand)packet;
                    if (packetPlayInClientCommand.a() == PacketPlayInClientCommand.EnumClientCommand.OPEN_INVENTORY_ACHIEVEMENT) {
                        data.inInventory = true;
                    }
                }
                else if (packet instanceof PacketPlayInCloseWindow) {
                    data.inInventory = false;
                }
                for (final Check check2 : data.getChecks()) {
                    check2.handleReceivedPacket(this.player, (Packet)packet);
                    cancel = (cancel || check2.handleReceivedPacketCanceled(this.player, (Packet)packet));
                }
                if (packet instanceof PacketPlayInFlying) {
                    final PacketPlayInFlying packetPlayInFlying = (PacketPlayInFlying)packet;
                    if (packetPlayInFlying.g()) {
                        data.lastPosX = packetPlayInFlying.a();
                        data.lastPosY = packetPlayInFlying.b();
                        data.lastPosZ = packetPlayInFlying.c();
                    }
                    data.lastGroundBoolean = packetPlayInFlying.f();
                    if (packetPlayInFlying.h()) {
                        data.lastYaw = packetPlayInFlying.d();
                        data.lastPitch = packetPlayInFlying.e();
                    }
                }
                if (cancel) {
                    return;
                }
            }
        }
        catch (Exception ex2) {
            System.out.println(ex2.getMessage());
            for (final StackTraceElement stackTraceElement2 : ex2.getStackTrace()) {
                System.out.println(stackTraceElement2.getClassName() + ":" + stackTraceElement2.getLineNumber());
            }
        }
        super.channelRead(channelHandlerContext, packet);
    }
    
    public void write(final ChannelHandlerContext channelHandlerContext, final Object packet, final ChannelPromise channelPromise) throws Exception {
        try {
            if (!this.player.isOnline()) {
                return;
            }
            if (Plunder.getInstance() == null) {
                return;
            }
            final PlayerData data = Plunder.getInstance().getDataManager().getPlayerData(this.player);
            if (data == null) {
                return;
            }
            if (data.isCanCheck()) {
                boolean cancel = false;
                if (packet instanceof PacketPlayOutKeepAlive) {
                    final PlayerData playerData = data;
                    ++playerData.sendKeepAliveCount;
                }
                else if (packet instanceof PacketPlayOutOpenWindow) {
                    data.inInventory = true;
                }
                else if (packet instanceof PacketPlayOutCloseWindow) {
                    data.inInventory = false;
                }
                else if (packet instanceof PacketPlayOutBlockChange) {
                    data.setLastDoingBlockUpdateTicks(data.getTicks());
                }
                else if (packet instanceof PacketPlayOutMultiBlockChange) {
                    data.setLastDoingBlockUpdateTicks(data.getTicks());
                }
                for (final Check check : data.getChecks()) {
                    try {
                        check.handleSentPacket(this.player, (Packet)packet);
                        cancel = (cancel || check.handleSentPacketCanceled(this.player, (Packet)packet));
                    }
                    catch (Exception ex) {
                        System.out.println(ex.getMessage());
                        for (final StackTraceElement stackTraceElement : ex.getStackTrace()) {
                            System.out.println(stackTraceElement.getClassName() + ":" + stackTraceElement.getLineNumber());
                        }
                        alertErrorToStaff(ex);
                    }
                }
                if (cancel) {
                    return;
                }
            }
        }
        catch (Exception ex2) {
            System.out.println(ex2.getMessage());
            for (final StackTraceElement stackTraceElement2 : ex2.getStackTrace()) {
                System.out.println(stackTraceElement2.getClassName() + ":" + stackTraceElement2.getLineNumber());
            }
            alertErrorToStaff(ex2);
        }
        super.write(channelHandlerContext, packet, channelPromise);
    }
    
    public static void alertErrorToStaff(final Exception ex) {
    }
    
    private static void dataMoveListen(final Player player, final PlayerData data, final Location to, final Location from, final PacketPlayInFlying packetPlayInFlying) {
        if (from.getX() != to.getX() || from.getY() != to.getY() || from.getZ() != to.getZ()) {
            data.setLastLastMoveLoc(data.getLastMoveLoc());
            data.setLastMoveLoc(player.getLocation());
            data.setOnGround(PlayerUtils.isOnTheGround(player));
            data.onGround = PlayerUtils.isOnGround4(player);
            data.setOnStairSlab(PlayerUtils.isInStairs(player));
            data.onStairSlab = PlayerUtils.isInStairs(player);
            data.setInLiquid(PlayerUtils.isInLiquid(player));
            data.inLiquid = PlayerUtils.isInLiquid(player);
            data.setOnIce(PlayerUtils.isOnIce(player));
            data.onIce = PlayerUtils.isOnIce(player);
            data.setOnClimbable(PlayerUtils.isOnClimbable(player));
            data.onClimbable = PlayerUtils.isOnClimbable(player);
            data.setUnderBlock(PlayerUtils.inUnderBlock(player));
            data.underBlock = PlayerUtils.inUnderBlock(player);
            data.step = data.testStep(from, to);
            ++data.velocityGroundTicks;
            data.liquidTicks = Math.max(0, data.isInLiquid() ? ((data.liquidTicks < 20) ? (data.liquidTicks + 1) : data.liquidTicks) : (data.liquidTicks - 1));
            data.blockTicks = Math.max(0, data.isUnderBlock() ? ((data.blockTicks < 20) ? (data.blockTicks + 1) : data.blockTicks) : (data.blockTicks - 1));
        }
        data.checkTransaction();
        data.setMoveTicks(data.getMovementTicks() + 1);
        if (PlayerUtils.checkMovement(player)) {
            ++data.moveTicks;
        }
        else {
            data.moveTicks = 0;
        }
        if (data.moveTicks == 0) {
            data.doingTeleport = true;
        }
        else if (data.isDoingTeleport()) {
            data.doingTeleport = false;
            data.lastTeleportDid = System.currentTimeMillis();
        }
        final AxisAlignedBB box = ((CraftPlayer)player).getHandle().getBoundingBox();
        final AABB aabb1 = new AABB(new AABB.Vec3D(box.a, box.b, box.c), new AABB.Vec3D(box.d, box.e, box.f)).expand(0.4, 0.0, 0.4).expandMin(0.0, -1.0, 0.0);
        final AABB aabb2 = new AABB(new AABB.Vec3D(box.a, box.b, box.c), new AABB.Vec3D(box.d, box.e, box.f)).expand(1.0, -1.0E-4, 1.0);
        final AABB aabb3 = new AABB(new AABB.Vec3D(box.a, box.b, box.c), new AABB.Vec3D(box.d, box.e, box.f)).offset(0.0, 0.6000000238418579, 0.0);
        final AABB aabb4 = new AABB(new AABB.Vec3D(box.a, box.b, box.c), new AABB.Vec3D(box.d, box.e, box.f)).expand(0.575, 0.0, 0.575).expandMin(0.0, -1.0, 0.0);
        boolean foundBelow = false;
        boolean foundNear = false;
        boolean foundAbove = false;
        boolean foundNearGround = false;
        boolean foundServerGround = false;
        boolean foundRoseBush = false;
        boolean foundWeb = false;
        boolean foundClimbable = false;
        boolean foundSlab = false;
        boolean foundIce = false;
        boolean foundSlime = false;
        boolean foundStairs = false;
        boolean foundPiston = false;
        boolean foundFence = false;
        boolean foundHalf = false;
        for (final Pair<Block, net.minecraft.server.v1_8_R3.Block> block : BlockUtils.getNearbyBlocksBukkitAndNMS(to, 2)) {
            if (block.getX() != null) {
                if (block.getY() == null) {
                    continue;
                }
                for (final AABB blockAABB : BlockUtils.getCollisionBoxes(block.getY(), block.getX().getLocation())) {
                    final Block bukkitBlock = block.getX();
                    final Material bukkitType = bukkitBlock.getType();
                    if (aabb1.isIntersected(blockAABB)) {
                        foundBelow = true;
                        foundWeb = (bukkitType == Material.WEB || foundWeb);
                        foundSlab = (BlockUtils.isSlab(bukkitType) || foundSlab);
                        foundIce = (BlockUtils.isIce(bukkitType) || foundIce);
                        foundSlime = (bukkitType == Material.SLIME_BLOCK || foundSlime);
                        foundStairs = (BlockUtils.isStair(bukkitType) || foundStairs);
                        foundPiston = (BlockUtils.isPiston(bukkitType) || foundPiston);
                        foundFence = (BlockUtils.isFence(bukkitType) || foundFence);
                        foundHalf = (BlockUtils.isHalfBlock(bukkitType) || foundHalf);
                        if (bukkitType.isSolid()) {
                            if (new AABB(new AABB.Vec3D(box.a, box.b, box.c), new AABB.Vec3D(box.d, box.e, box.f)).offset(0.0, -0.49, 0.0).expandMax(0.0, -1.2, 0.0).expandMin(0.0, -0.8, 0.0).expand(0.2, 0.0, 0.2).isIntersected(blockAABB)) {
                                foundNearGround = true;
                            }
                            if (new AABB(new AABB.Vec3D(box.a, box.b, box.c), new AABB.Vec3D(box.d, box.e, box.f)).offset(0.0, -0.49, 0.0).expandMax(0.0, -1.2, 0.0).isColliding(blockAABB)) {
                                foundServerGround = true;
                            }
                        }
                    }
                    if (aabb2.isIntersected(blockAABB)) {
                        foundNear = true;
                    }
                    if (aabb3.isColliding(blockAABB)) {
                        foundAbove = true;
                    }
                    if (aabb4.isIntersected(blockAABB)) {
                        foundClimbable = (BlockUtils.isClimbableBlock(bukkitType) || foundClimbable);
                    }
                }
                if (block.getX().getType() != Material.DOUBLE_PLANT || block.getX().getData() != 4) {
                    continue;
                }
                foundRoseBush = true;
            }
        }
        final Location frictionLoc = player.getLocation().clone().add(0.0, -1.0, 0.0);
        data.setFriction(((CraftWorld)player.getWorld()).getHandle().getType(new BlockPosition(frictionLoc.getBlockX(), frictionLoc.getBlockY(), frictionLoc.getBlockZ())).getBlock().frictionFactor);
        data.setLastBlockBelow(data.isBlockBelow());
        data.setLastBlockNear(data.isBlockNear());
        data.setLastRoseBush(data.isRoseBush());
        data.setBlockBelow(foundBelow);
        data.setBlockNear(foundNear);
        data.setRoseBush(foundRoseBush);
        data.setNearGround(foundNearGround);
        data.setServerGround(foundServerGround);
        if (player.isDead()) {
            data.deadTicks = 20;
        }
        else {
            --data.deadTicks;
        }
        if (player.isInsideVehicle()) {
            data.inVehicleTicks = 20;
        }
        else {
            --data.inVehicleTicks;
        }
        if (player.isFlying()) {
            data.flyingTicks = 20;
        }
        else {
            --data.flyingTicks;
        }
        if (foundNear) {
            if (data.getSolidBlockBehindTicks() < 20) {
                data.setSolidBlockBehindTicks(20);
            }
        }
        else {
            data.setSolidBlockBehindTicks(data.getSolidBlockBehindTicks() - 1);
        }
        if (foundWeb) {
            data.onWebTicks = 20;
        }
        else {
            --data.onWebTicks;
        }
        if (foundClimbable) {
            data.nearClimbTicks = 20;
        }
        else {
            --data.nearClimbTicks;
        }
        if (foundSlab) {
            if (data.getNearSlabTicks() < 20) {
                data.setNearSlabTicks(20);
            }
        }
        else {
            data.setNearSlabTicks(data.getNearSlabTicks() - 1);
        }
        if (foundIce) {
            data.setOnIceTicks(40);
        }
        else {
            --data.onIceTicks;
        }
        if (data.isSprint()) {
            if (data.getSprintTicks() < 5) {
                data.setSprintTicks(5);
            }
        }
        else {
            data.setSprintTicks(data.getSprintTicks() - 1);
        }
        if (foundAbove) {
            if (data.getUnderBlockTicks() < 20) {
                data.setUnderBlockTicks(20);
            }
        }
        else {
            data.setUnderBlockTicks(data.getUnderBlockTicks() - 1);
        }
        if (foundSlime) {
            if (data.getAboveSlimeTicks() < 20) {
                data.setAboveSlimeTicks(20);
            }
        }
        else {
            data.setAboveSlimeTicks(data.getAboveSlimeTicks() - 1);
        }
        if (foundStairs) {
            if (data.getStairsTicks() < 20.0) {
                data.setStairsTicks(20.0);
            }
        }
        else {
            data.setStairsTicks(data.getStairsTicks() - 1.0);
        }
        if (foundPiston) {
            if (data.getPistonTicks() < 20.0) {
                data.setPistonTicks(20.0);
            }
        }
        else {
            data.setPistonTicks(data.getPistonTicks() - 1.0);
        }
        if (foundFence) {
            if (data.getFenceTicks() < 20) {
                data.setFenceTicks(20);
            }
        }
        else {
            data.setFenceTicks(data.getFenceTicks() - 1);
        }
        if (foundHalf) {
            if (data.getHalfBlockTicks() < 20) {
                data.setHalfBlockTicks(20);
            }
        }
        else {
            data.setHalfBlockTicks(data.getHalfBlockTicks() - 1);
        }
        if (!packetPlayInFlying.f() && !data.isTeleporting()) {
            data.jumped = (!data.jumped && data.getLastGroundLoc() != null && data.deltaY >= 0.0);
        }
        else {
            data.jumped = false;
        }
        final Location playerLocNow = player.getLocation().clone();
        playerLocNow.setY(playerLocNow.getY() - 1.0);
        final double distance = MathUtils.getVerticalDistance(from, to);
        final boolean onGround = PlayerUtils.isOnGround4(player);
        if (!onGround && from.getY() > to.getY()) {
            data.setFallDistance(data.getFallDistance() + distance);
        }
        else {
            data.setFallDistance(0.0);
        }
        if (PlayerUtils.isOnGround(player.getLocation().add(0.0, 2.0, 0.0))) {
            data.setAboveBlockTicks(data.getAboveBlockTicks() + 2);
        }
        else if (data.getAboveBlockTicks() > 0) {
            data.setAboveBlockTicks(data.getAboveBlockTicks() - 1);
        }
        if (PlayerUtils.isInWater(player.getLocation()) || PlayerUtils.isInWater(player.getLocation().add(0.0, 1.0, 0.0))) {
            if (data.getWaterTicks() < 20) {
                data.setWaterTicks(data.getWaterTicks() + 1);
            }
        }
        else if (data.getWaterTicks() > 0) {
            data.setWaterTicks(data.getWaterTicks() - 1);
        }
        final Location playerLocNow2 = player.getLocation().clone();
        playerLocNow2.setY(playerLocNow2.getY() - 1.0);
        if (data.isServerGround()) {
            data.setGroundTicks(data.getGroundTicks() + 1);
            data.setAirTicks(0);
        }
        else {
            data.setAirTicks(data.getAirTicks() + 1);
            data.setGroundTicks(0);
            data.setLastAirTime(System.currentTimeMillis());
        }
        if (packetPlayInFlying.f()) {
            data.setClientGroundTicks(data.getClientGroundTicks() + 1);
            data.setClientAirTicks(0);
        }
        else {
            data.setClientAirTicks(data.getClientAirTicks() + 1);
            data.setClientGroundTicks(0);
        }
        if (data.isServerGround()) {
            data.groundTicks2 = 10;
            --data.airTicks2;
        }
        else {
            data.airTicks2 = 10;
            --data.groundTicks2;
        }
        if (packetPlayInFlying.f()) {
            data.clientGroundTicks2 = 10;
            --data.clientAirTicks2;
        }
        else {
            data.clientAirTicks2 = 10;
            --data.clientGroundTicks2;
        }
        if (data.getAboveSlimeTicks() > 0) {
            data.setBouncedOnSlime(true);
            data.setLastSlimeLocation(playerLocNow2);
        }
        if (data.isBouncedOnSlime() && (data.getAboveSlimeTicks() <= 0 || MathUtils.getHorizontalDistance(playerLocNow2, data.getLastSlimeLocation()) > 70.0)) {
            data.setBouncedOnSlime(false);
        }
    }
}
