// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.checks.movement.speed;

import java.util.Map;
import gg.noob.plunder.Plunder;
import gg.noob.plunder.util.BlockUtils;
import java.util.Iterator;
import gg.noob.plunder.util.PlayerUtils;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.inventory.ItemStack;
import org.bukkit.enchantments.Enchantment;
import net.minecraft.server.v1_8_R3.AxisAlignedBB;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import gg.noob.plunder.util.AABB;
import org.bukkit.util.Vector;
import java.util.UUID;
import java.util.Set;
import gg.noob.plunder.data.PlayerData;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftHumanEntity;
import org.bukkit.Material;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import gg.noob.plunder.checks.Check;

public class SpeedC extends Check
{
    private double prevSpeed;
    private double discrepancies;
    private int noMoves;
    private double lastNegativeDiscrepancies;
    private double negativeDiscrepanciesCumulative;
    private int slotSwitchQuirkTicks;
    private long lastSlotSwitchTick;
    private int count;
    
    public SpeedC() {
        super("Speed (C)");
        this.prevSpeed = 0.0;
        this.discrepancies = 0.0;
        this.noMoves = 0;
        this.lastNegativeDiscrepancies = 0.0;
        this.negativeDiscrepanciesCumulative = 0.0;
        this.slotSwitchQuirkTicks = 0;
        this.lastSlotSwitchTick = 0L;
        this.count = 0;
        this.setMaxViolations(13);
        this.setViolationsRemoveOneTime(1000L);
    }
    
    @Override
    public void handleLocationUpdate(final Player player, final Location to, final Location from, final PacketPlayInFlying packetPlayInFlying) {
        final PlayerData data = this.getPlayerData();
        final boolean cancel = false;
        final boolean b = packetPlayInFlying.g();
        double lastSpeed;
        double speed;
        if (b) {
            lastSpeed = this.prevSpeed;
            if (this.noMoves > 0) {
                lastSpeed = Math.min(lastSpeed, 0.03);
            }
            speed = Math.sqrt(Math.pow(to.getX() - from.getX(), 2.0) + Math.pow(to.getZ() - from.getZ(), 2.0));
        }
        else {
            speed = (lastSpeed = this.prevSpeed - (this.lastNegativeDiscrepancies + 1.0E-6));
        }
        if (b) {
            this.noMoves = 0;
        }
        else {
            ++this.noMoves;
        }
        final boolean flying = player.getAllowFlight();
        final boolean swimming = data.getLiquidTicks() > 0;
        final float friction = this.computeFriction(data);
        final float maxForce = this.computeMaximumInputForce(data, true);
        final float maxForceNoItemUse = this.computeMaximumInputForce(data, false);
        final Set<Material> touchedBlocks = this.getCollisionBox(player, from.toVector()).getMaterials(player.getWorld());
        if (data.isTakingVelocity() || System.currentTimeMillis() - data.getLastTeleport() < 1000L || player.getAllowFlight()) {
            this.prepareNextMove(data, this.noMoves, speed, touchedBlocks);
            return;
        }
        final double lastSpeedCompare = lastSpeed;
        double handleMultipliers = 1.0;
        if (data.hasHitSlowdown()) {
            handleMultipliers *= 0.6;
        }
        if (touchedBlocks.contains(Material.SOUL_SAND) && !flying) {
            handleMultipliers *= 0.4;
        }
        if (touchedBlocks.contains(Material.WEB) && !flying) {
            handleMultipliers *= 0.25;
        }
        if (touchedBlocks.contains(Material.SLIME_BLOCK) && data.getDeltaY() < 0.1 && !player.isSneaking()) {
            handleMultipliers *= 0.4 + Math.abs(data.getDeltaY()) * 0.2;
        }
        double handleAdders = 0.001;
        final long distance = System.currentTimeMillis() - data.getLastGroundTime();
        if (distance > 600L) {}
        final boolean jump = distance < 600L;
        final boolean leftGround = distance > 550L;
        if (jump) {
            handleAdders += 0.5;
            if (leftGround) {
                handleAdders += 0.95;
            }
        }
        if (swimming && !flying) {
            handleAdders += 0.01;
        }
        final double expected = friction * lastSpeedCompare * handleMultipliers + (maxForce + handleAdders + 1.0E-6);
        final double expectedNoItemUse = friction * lastSpeedCompare * handleMultipliers + (maxForceNoItemUse + handleAdders + 1.0E-6);
        final long slotSwitchTick = (this.lastSlotSwitchTick == 0L) ? data.getTicks() : this.lastSlotSwitchTick;
        final boolean switchedSlot = slotSwitchTick != data.getTicks();
        if (switchedSlot) {
            this.slotSwitchQuirkTicks = 0;
        }
        this.lastSlotSwitchTick = slotSwitchTick;
        final Discrepancy discrepancyBase = new Discrepancy(expected, speed);
        final Discrepancy discrepancyNoItemUse = new Discrepancy(expectedNoItemUse, speed);
        Discrepancy discrepancy;
        if (((CraftHumanEntity)player).getHandle().g != null && Math.abs(discrepancyNoItemUse.value) < Math.abs(discrepancyBase.value)) {
            if (this.slotSwitchQuirkTicks < 1) {
                discrepancy = discrepancyNoItemUse;
                ++this.slotSwitchQuirkTicks;
            }
            else {
                discrepancy = discrepancyBase;
            }
        }
        else {
            discrepancy = discrepancyBase;
        }
        if (b) {
            final double haltDistanceExpected = this.negativeDiscrepanciesCumulative;
            this.lastNegativeDiscrepancies = 0.0;
            if (discrepancy.value < 0.0 || speed > haltDistanceExpected) {
                this.discrepancies = Math.max(this.discrepancies + discrepancy.value, 0.0);
            }
            final double totalDiscrepancy = this.discrepancies;
            if (discrepancy.value > 0.0 && totalDiscrepancy > 0.1 && data.getPistonTicks() <= 0.0 && !data.isDoingBlockUpdate() && !data.isPlacingBlocks()) {
                if (++this.count > 8) {
                    this.logCheat(player, "Discrepancy: " + discrepancy.value * 10.0 + " Distance: " + (System.currentTimeMillis() - data.getLastGroundTime()));
                    this.discrepancies = 0.0;
                    this.back();
                    this.count = 0;
                }
            }
            else {
                this.count = 0;
            }
            this.lastNegativeDiscrepancies = 0.0;
            this.negativeDiscrepanciesCumulative = 0.0;
        }
        else {
            this.lastNegativeDiscrepancies = discrepancy.value;
            final double val = this.negativeDiscrepanciesCumulative;
            this.negativeDiscrepanciesCumulative = Math.min(val + speed, 0.03 + speed);
        }
        this.prepareNextMove(data, this.noMoves, speed, touchedBlocks);
    }
    
    private void prepareNextMove(final PlayerData data, final int noMoves, double currentSpeed, final Set<Material> touchedBlocks) {
        if (touchedBlocks.contains(Material.WEB) && !data.getPlayer().getAllowFlight()) {
            currentSpeed = 0.0;
        }
        final UUID uuid = data.getPlayer().getUniqueId();
        this.prevSpeed = currentSpeed;
        this.noMoves = noMoves;
    }
    
    public AABB getCollisionBox(final Player player, final Vector entityPos) {
        final EntityPlayer nmsPlayer = ((CraftPlayer)player).getHandle();
        final AxisAlignedBB bb = nmsPlayer.getBoundingBox();
        final AABB collisionBox = new AABB(new AABB.Vec3D(bb.a, bb.b, bb.c), new AABB.Vec3D(bb.d, bb.e, bb.f));
        final float collisionBorderSize = nmsPlayer.ao();
        final Location location = player.getLocation();
        final Vector move = entityPos.clone().subtract(location.toVector());
        final AABB box = collisionBox.clone();
        box.min = AABB.Vec3D.fromVector(new Vector(box.min.x, box.min.y, box.min.z).add(move));
        box.max = AABB.Vec3D.fromVector(new Vector(box.max.x, box.max.y, box.max.z).add(move));
        return box;
    }
    
    public AABB getHitbox(final Player player, final Vector entityPos) {
        final EntityPlayer nmsPlayer = ((CraftPlayer)player).getHandle();
        final AxisAlignedBB bb = nmsPlayer.getBoundingBox();
        final AABB collisionBox = new AABB(new AABB.Vec3D(bb.a, bb.b, bb.c), new AABB.Vec3D(bb.d, bb.e, bb.f));
        final float collisionBorderSize = nmsPlayer.ao();
        final AABB box = this.getCollisionBox(player, entityPos);
        final Vector compliment = new Vector(collisionBorderSize, collisionBorderSize, collisionBorderSize);
        box.min = AABB.Vec3D.fromVector(new Vector(box.min.x, box.min.y, box.min.z).subtract(compliment));
        box.max = AABB.Vec3D.fromVector(new Vector(box.max.x, box.max.y, box.max.z).add(compliment));
        return box;
    }
    
    private float computeFriction(final PlayerData data) {
        final Player player = data.getPlayer();
        final boolean flying = player.getAllowFlight();
        final boolean onGround = data.isOnGround();
        if (data.getWaterTicks() > 0 && !flying) {
            float friction = 0.8f;
            float depthStrider = 0.0f;
            final ItemStack boots = player.getInventory().getBoots();
            if (boots != null) {
                depthStrider = (float)boots.getEnchantmentLevel(Enchantment.DEPTH_STRIDER);
                if (depthStrider > 3.0f) {
                    depthStrider = 3.0f;
                }
            }
            if (!onGround) {
                depthStrider *= 0.5f;
            }
            if (depthStrider > 0.0f) {
                friction += (0.546f - friction) * depthStrider / 3.0f;
            }
            return friction;
        }
        if (data.getLiquidTicks() > 0 && !flying) {
            return 0.5f;
        }
        float friction = 0.91f;
        if (onGround) {
            friction *= data.getFriction();
        }
        return friction;
    }
    
    private float computeMaximumInputForce(final PlayerData data, final boolean considerItemUse) {
        final Player player = data.getPlayer();
        float initForce = 0.98f;
        if (player.isSneaking()) {
            initForce *= (float)0.3;
        }
        final boolean usingItem = considerItemUse && ((CraftHumanEntity)player).getHandle().g != null;
        if (usingItem) {
            initForce *= (float)0.2;
        }
        final boolean flying = player.getAllowFlight();
        final boolean sprinting = data.getSprintTicks() > 0 && !usingItem && !player.isSneaking() && (flying || data.getLiquidTicks() <= 0);
        float speedEffectMultiplier = 1.0f;
        for (final PotionEffect effect : player.getActivePotionEffects()) {
            if (!effect.getType().equals((Object)PotionEffectType.SPEED)) {
                continue;
            }
            final int level = effect.getAmplifier() + 1;
            speedEffectMultiplier += level * 0.2f;
        }
        final boolean onGround = data.isOnGround();
        float multiplier;
        if (data.getWaterTicks() > 0 && !flying) {
            float force = 0.02f;
            float depthStrider = 0.0f;
            final ItemStack boots = player.getInventory().getBoots();
            if (boots != null) {
                depthStrider = (float)boots.getEnchantmentLevel(Enchantment.DEPTH_STRIDER);
                if (depthStrider > 3.0f) {
                    depthStrider = 3.0f;
                }
            }
            if (!onGround) {
                depthStrider *= 0.5f;
            }
            if (depthStrider > 0.0f) {
                force += (0.1f - force) * depthStrider / 3.0f;
            }
            multiplier = force;
        }
        else if (PlayerUtils.isInLiquid(player) && !flying) {
            multiplier = 0.02f;
        }
        else if (onGround) {
            final float newFriction = this.computeFriction(data);
            multiplier = 0.016277136f / (newFriction * newFriction * newFriction);
            final float groundMultiplier = 5.0f * player.getWalkSpeed() * speedEffectMultiplier;
            multiplier *= groundMultiplier;
        }
        else {
            final float flyMultiplier = 10.0f * player.getFlySpeed();
            multiplier = (flying ? 0.05f : 0.02f) * flyMultiplier;
        }
        float diagonal = (float)Math.sqrt(2.0f * initForce * initForce);
        if (diagonal < 1.0f) {
            diagonal = 1.0f;
        }
        final float componentForce = initForce * multiplier / diagonal;
        final float finalForce = (float)Math.sqrt(2.0f * componentForce * componentForce);
        return (float)(finalForce * (sprinting ? (flying ? 2.0 : 1.3) : 1.0));
    }
    
    private boolean testJumped(final PlayerData data, final Location to, final Location from) {
        final Player player = data.getPlayer();
        final EntityPlayer nmsPlayer = ((CraftPlayer)player).getHandle();
        int jumpBoostLvl = 0;
        for (final PotionEffect pEffect : player.getActivePotionEffects()) {
            if (pEffect.getType().equals((Object)PotionEffectType.JUMP)) {
                final byte amp = (byte)pEffect.getAmplifier();
                jumpBoostLvl = amp + 1;
                break;
            }
        }
        float expectedDY = Math.max(0.42f + jumpBoostLvl * 0.1f, 0.0f);
        final boolean leftGround = player.isOnGround() && !BlockUtils.isOnGround(player.getLocation());
        final float dY = (float)(to.getY() - from.getY());
        AABB box = AABB.playerCollisionBox.clone();
        box.expand(-1.0E-6, -1.0E-6, -1.0E-6);
        box.translate(to.toVector().add(new Vector(0.0f, expectedDY, 0.0f)));
        final boolean collidedNow = !box.getMaterials(to.getWorld()).isEmpty();
        box = AABB.playerCollisionBox.clone();
        box.expand(-1.0E-6, -1.0E-6, -1.0E-6);
        box.translate(from.toVector().add(new Vector(0.0f, expectedDY, 0.0f)));
        final boolean collidedBefore = !box.getMaterials(to.getWorld()).isEmpty();
        if (collidedNow && !collidedBefore && leftGround && dY == 0.0f) {
            expectedDY = 0.0f;
        }
        expectedDY *= (float)((data.getSolidBlockBehindTicks() > 0) ? 0.05 : 0.0);
        final boolean kbSimilarToJump = Plunder.getInstance().getLastVelocity().containsKey(player.getUniqueId()) && (Math.abs(Plunder.getInstance().getLastVelocity().get(player.getUniqueId()).getValue().getY() - expectedDY) < 0.001 || BlockUtils.isSolidBlockBehindPlayer(player));
        return !kbSimilarToJump && ((expectedDY == 0.0f && data.isOnGround()) || leftGround) && (dY == expectedDY || data.getSolidBlockBehindTicks() > 0);
    }
    
    private class Discrepancy
    {
        double value;
        
        Discrepancy(final double expectedSpeed, final double currentSpeed) {
            this.value = currentSpeed - expectedSpeed;
        }
    }
}
