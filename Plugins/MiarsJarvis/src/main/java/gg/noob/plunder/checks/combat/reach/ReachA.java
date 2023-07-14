// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.checks.combat.reach;

import java.util.Iterator;
import java.util.List;
import gg.noob.plunder.data.PlayerData;
import gg.noob.plunder.util.MathUtils;
import java.util.Arrays;
import gg.noob.plunder.util.ReachUtils;
import gg.noob.plunder.util.PlayerUtils;
import org.bukkit.util.Vector;
import gg.noob.plunder.util.AABB;
import gg.noob.plunder.util.PlayerReachEntity;
import org.bukkit.Location;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import org.bukkit.entity.Player;
import gg.noob.plunder.checks.Check;

public class ReachA extends Check
{
    private static final double range = 3.01;
    private static final double sightThreshold = 3.0;
    private static final double sightDecay = 0.25;
    private static final boolean sightEnabled = true;
    private double sightBuffer;
    private double buffer;
    
    public ReachA() {
        super("Reach (A)");
        this.sightBuffer = 0.0;
        this.buffer = 0.0;
        this.setMaxViolations(11);
    }
    
    public void tickFlying(final Player player, final PacketPlayInFlying packetPlayInFlying) {
        final PlayerData data = this.getPlayerData();
        final Location to = new Location(player.getWorld(), packetPlayInFlying.a(), packetPlayInFlying.b(), packetPlayInFlying.c(), packetPlayInFlying.d(), packetPlayInFlying.e());
        final Location from = new Location(player.getWorld(), data.lastPosX, data.lastPosY, data.lastPosZ, data.lastYaw, data.lastPitch);
        if (!packetPlayInFlying.g() || packetPlayInFlying.b() == -999.0) {
            to.setX(from.getX());
            to.setY(from.getY());
            to.setZ(from.getZ());
        }
        if (!packetPlayInFlying.h()) {
            to.setYaw(from.getYaw());
            to.setPitch(from.getPitch());
        }
        double maxReach = 3.01;
        if (!this.isStranger()) {
            maxReach += 0.1;
        }
        for (Integer attackQueue = data.playerAttackQueue.poll(); attackQueue != null; attackQueue = data.playerAttackQueue.poll()) {
            final PlayerReachEntity reachEntity = data.entityMap.get(attackQueue);
            if (data.getTarget() != null && data.getTarget().getEntityId() == attackQueue && data.getTicks() - data.getLastTargetTicks() >= 5) {
                if (reachEntity != null && reachEntity.serverPos.distance(AABB.Vec3D.fromLocation(to)) <= 8.0 && reachEntity.serverPos.distance(AABB.Vec3D.fromLocation(from)) <= 8.0 && new Location(player.getWorld(), reachEntity.serverPos.getX(), reachEntity.serverPos.getY(), reachEntity.serverPos.getZ()).getChunk().isLoaded() && to.getChunk().isLoaded() && from.getChunk().isLoaded()) {
                    if (!data.isTeleporting(5)) {
                        final AABB targetBox = reachEntity.getPossibleCollisionBoxes();
                        targetBox.expand(0.1025);
                        if (!data.lastPosition) {
                            targetBox.expand(0.03);
                        }
                        double minDistance = Double.MAX_VALUE;
                        Vector debugIntercept = new Vector(0, 0, 0);
                        Vector debugEyePos = new Vector(0, 0, 0);
                        final AABB.Vec3D debugServerPos = reachEntity.serverPos;
                        final List<Vector> possibleLookDirs = Arrays.asList(ReachUtils.getLook(PlayerUtils.yawTo180F(to.getYaw()), to.getPitch()), ReachUtils.getLook(PlayerUtils.yawTo180F(to.getYaw()), to.getPitch()));
                        for (final Vector lookVec : possibleLookDirs) {
                            for (final double eye : Arrays.asList(1.54, 1.62)) {
                                final Vector eyePos = new Vector(from.getX(), from.getY() + eye, from.getZ());
                                final Vector endReachPos = eyePos.clone().add(new Vector(lookVec.getX() * 6.0, lookVec.getY() * 6.0, lookVec.getZ() * 6.0));
                                final Vector intercept = ReachUtils.calculateIntercept(targetBox, eyePos, endReachPos);
                                if (ReachUtils.isVecInside(targetBox, eyePos)) {
                                    minDistance = 0.0;
                                    break;
                                }
                                if (intercept == null) {
                                    continue;
                                }
                                minDistance = Math.min(eyePos.distance(intercept), minDistance);
                                debugIntercept = intercept;
                                debugEyePos = eyePos;
                            }
                        }
                        final boolean reaching = minDistance > maxReach && minDistance < 7.0;
                        if (reaching) {
                            final double buffer = this.buffer + 1.0;
                            this.buffer = buffer;
                            if (buffer > 3.0) {
                                this.logCheat("Distance: %s \nClient Self Location: %s \nClient Target Location: %s \nServer Self Location: %s \nServer Target Location: %s \nYaw: %s Pitch: %s \nLast Transaction: %s Last Transaction ID: %s Last Transaction Sync ID: %s \nReach Entity Transaction Hung: %s", MathUtils.round(minDistance, 2), debugEyePos.toString(), debugIntercept.toString(), player.getLocation().toVector().toString(), debugServerPos.toString(), PlayerUtils.yawTo180F(data.getLastYaw()), data.getLastPitch(), System.currentTimeMillis() - data.getLastClientTransaction(), data.getLastTransactionID(), data.getLastSyncTransactionSendId(), reachEntity.lastTransactionHung);
                            }
                        }
                        else if (this.buffer > 0.0) {
                            this.buffer -= 1.0E-4;
                        }
                        if (minDistance == Double.MAX_VALUE) {
                            double threshold = 3.0;
                            if (!this.isStranger()) {
                                threshold *= 2.0;
                            }
                            final double sightBuffer = this.sightBuffer + 1.0;
                            this.sightBuffer = sightBuffer;
                            if (sightBuffer > threshold) {
                                this.logCheat("Distance: Max Value");
                                this.sightBuffer = 0.0;
                            }
                        }
                        else if (this.sightBuffer > 0.0) {
                            this.sightBuffer -= 0.25;
                        }
                    }
                }
            }
        }
        for (final PlayerReachEntity entity : data.entityMap.values()) {
            entity.onMovement();
        }
    }
}
