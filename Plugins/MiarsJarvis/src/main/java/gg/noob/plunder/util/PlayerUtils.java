// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.util;

import com.google.common.collect.Sets;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import java.util.ArrayList;
import java.util.Collection;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.enchantments.Enchantment;
import gg.noob.plunder.data.PlayerData;
import gg.noob.plunder.Plunder;
import org.bukkit.Location;
import java.util.Iterator;
import org.bukkit.potion.PotionEffect;
import org.bukkit.inventory.ItemStack;
import net.minecraft.server.v1_8_R3.NBTTagList;
import org.bukkit.GameMode;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.Material;
import com.google.common.collect.ImmutableSet;
import java.lang.reflect.Field;

public class PlayerUtils
{
    private static Field checkMovement;
    private static ImmutableSet<Material> ground;
    
    public static boolean checkMovement(final Player player) {
        try {
            return PlayerUtils.checkMovement.getBoolean(((CraftPlayer)player).getHandle().playerConnection);
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
            return true;
        }
    }
    
    public static float getTotalHeight(final float initial) {
        float nextCalc = initial;
        float total = initial;
        int count = 0;
        while ((nextCalc = (nextCalc - 0.08f) * 0.98f) > 0.005) {
            total += nextCalc;
            if (count++ > 15) {
                return total * 4.0f;
            }
        }
        return total;
    }
    
    public static float getJumpHeight(final Player player) {
        float baseHeight = 0.42f;
        if (player.hasPotionEffect(PotionEffectType.JUMP)) {
            baseHeight += getPotionEffectLevel(player, PotionEffectType.JUMP) * 0.1f;
        }
        return baseHeight;
    }
    
    public static boolean hasInvalidJumpBoost(final Player player) {
        return getPotionEffectLevel(player, PotionEffectType.JUMP) >= 128 || getPotionEffectLevel(player, PotionEffectType.JUMP) < 0;
    }
    
    public static boolean checkID(final String id) {
        if (id.contains("FDP") || id.contains("Tenacity") || id.contains("Infinite") || id.contains("Aero") || id.contains("Sigma") || id.contains("Jello") || id.contains("Kazer")) {
            return true;
        }
        boolean f = false;
        boolean d = false;
        boolean p = false;
        for (int i = 0; i < id.length(); ++i) {
            final String str = id.substring(i, i + 1);
            if (i == 0 && str.equalsIgnoreCase("F")) {
                f = true;
            }
            if (i == 2 && str.equalsIgnoreCase("D")) {
                d = true;
            }
            if (i == 4 && str.equalsIgnoreCase("P")) {
                p = true;
            }
        }
        if (f && d && p) {
            return true;
        }
        int streak = 0;
        int streakCount = 0;
        int maxStreak = 0;
        for (int j = 0; j < id.length(); ++j) {
            final String str2 = id.substring(j, j + 1);
            if (str2.equalsIgnoreCase("L")) {
                ++streak;
            }
            else {
                if (streak > maxStreak) {
                    maxStreak = streak;
                }
                if (streak > 0) {
                    ++streakCount;
                }
                streak = 0;
            }
        }
        return (streakCount > 1 && maxStreak > 2) || maxStreak > 4;
    }
    
    public static void reset(final Player player) {
        player.setGameMode(GameMode.SURVIVAL);
        player.setAllowFlight(false);
        ((CraftPlayer)player).getHandle().inventory.b(new NBTTagList());
        player.setSprinting(false);
        player.setFoodLevel(20);
        player.setSaturation(3.0f);
        player.setExhaustion(0.0f);
        player.setMaxHealth(20.0);
        player.setHealth(player.getMaxHealth());
        player.setFireTicks(0);
        player.setFallDistance(0.0f);
        player.setLevel(0);
        player.setExp(0.0f);
        player.setWalkSpeed(0.2f);
        player.setFlySpeed(0.1f);
        player.getInventory().clear();
        player.getInventory().setHelmet((ItemStack)null);
        player.getInventory().setChestplate((ItemStack)null);
        player.getInventory().setLeggings((ItemStack)null);
        player.getInventory().setBoots((ItemStack)null);
        player.updateInventory();
        for (final PotionEffect potion : player.getActivePotionEffects()) {
            player.removePotionEffect(potion.getType());
        }
    }
    
    public static int getPing(final Player player) {
        return ((CraftPlayer)player).getHandle().ping;
    }
    
    public static Pair<Float, Float> lookAt(final Location loc, final Location targetLoc) {
        final Location point = loc.clone();
        final Location npcLoc = targetLoc.clone();
        final double xDiff = point.getX() - npcLoc.getX();
        final double yDiff = point.getY() - npcLoc.getY();
        final double zDiff = point.getZ() - npcLoc.getZ();
        final double DistanceXZ = Math.sqrt(xDiff * xDiff + zDiff * zDiff);
        final double DistanceY = Math.sqrt(DistanceXZ * DistanceXZ + yDiff * yDiff);
        float newYaw = (float)(Math.acos(xDiff / DistanceXZ) * 180.0 / 3.141592653589793);
        final float newPitch = (float)(Math.acos(yDiff / DistanceY) * 180.0 / 3.141592653589793 - 90.0);
        if (zDiff < 0.0) {
            newYaw += Math.abs(180.0f - newYaw) * 2.0f;
        }
        newYaw -= 90.0f;
        return new Pair<Float, Float>(newYaw, newPitch);
    }
    
    public static boolean wasOnSlime(final Player player) {
        final PlayerData user = Plunder.getInstance().getDataManager().getPlayerData(player);
        if (user != null && user.getSetbackLocation() != null) {
            final Location location = user.getSetbackLocation().clone().subtract(0.0, 1.0, 0.0);
            if (BlockUtils.getBlockType(location.getWorld(), location).getId() == 165) {
                return true;
            }
        }
        return false;
    }
    
    public static float yawTo180F(float flub) {
        if ((flub %= 360.0f) >= 180.0f) {
            flub -= 360.0f;
        }
        if (flub < -180.0f) {
            flub += 360.0f;
        }
        return flub;
    }
    
    public static double getBaseSpeed(final PlayerData data) {
        return 0.2806 + getPotionEffectLevel(data.getPlayer(), PotionEffectType.SPEED) * ((data.getClientGroundTicks() > 0) ? 0.062 : 0.04) + (data.getPlayer().getWalkSpeed() - 0.2) * 2.5;
    }
    
    public static float getBaseSpeed(final Player player, final float base) {
        return base + getPotionEffectLevel(player, PotionEffectType.SPEED) * (player.isOnGround() ? 0.062f : 0.04f) + (player.getWalkSpeed() - 0.2f) * 1.6f;
    }
    
    public static double getBaseSpeed(final Player player) {
        return 0.362 + getPotionEffectLevel(player, PotionEffectType.SPEED) * 0.062f + (player.getWalkSpeed() - 0.2f) * 1.6f;
    }
    
    public static double getBaseGroundSpeed(final Player player) {
        return 0.289 + getPotionEffectLevel(player, PotionEffectType.SPEED) * 0.062f + (player.getWalkSpeed() - 0.2f) * 1.6f;
    }
    
    public static boolean isChase(final PlayerData data1, final PlayerData data2) {
        return false;
    }
    
    public static int compressedAngle(final float angle) {
        return (int)(angle * 256.0f / 360.0f);
    }
    
    public static int getEff(final Player p) {
        int enchantmentLevel = 0;
        final ItemStack[] contents;
        final ItemStack[] inv = contents = p.getInventory().getContents();
        for (final ItemStack item : contents) {
            if (item != null && item.getType() != null && item.getEnchantments().containsKey(Enchantment.DIG_SPEED)) {
                return enchantmentLevel = item.getEnchantmentLevel(Enchantment.DIG_SPEED);
            }
        }
        return 0;
    }
    
    public static boolean getAllowFlight2(final BlockPlaceEvent e, final Player p) {
        final Location loc = p.getLocation();
        loc.setY(loc.getY() - 1.0);
        final Material block = BlockUtils.getBlockType(loc.getWorld(), loc);
        return block.equals((Object)Material.AIR);
    }
    
    public static boolean getAllowFlight2(final PlayerMoveEvent e, final Player p) {
        final Location loc = p.getLocation();
        loc.setY(loc.getY() - 1.0);
        final Material block = BlockUtils.getBlockType(loc.getWorld(), loc);
        return block.equals((Object)Material.AIR);
    }
    
    public static boolean isOnGround(final PlayerMoveEvent e, final Player p) {
        final Location loc = p.getLocation();
        loc.setY(loc.getY());
        final Material block = BlockUtils.getBlockType(loc.getWorld(), loc);
        return !block.equals((Object)Material.AIR);
    }
    
    public static boolean isOnGround(final BlockPlaceEvent e, final Player p) {
        final Location loc = p.getLocation();
        loc.setY(loc.getY());
        final Material block = BlockUtils.getBlockType(loc.getWorld(), loc);
        return !block.equals((Object)Material.AIR);
    }
    
    public static boolean getAllowFlight(final PlayerMoveEvent e, final Player p) {
        final Location loc = p.getLocation();
        loc.setY(loc.getY() - 2.0);
        final Material block = BlockUtils.getBlockType(loc.getWorld(), loc);
        return block.equals((Object)Material.AIR);
    }
    
    public static boolean getAllowFlight(final BlockPlaceEvent e, final Player p) {
        final Location loc = p.getLocation();
        loc.setY(loc.getY() - 2.0);
        final Material block = BlockUtils.getBlockType(loc.getWorld(), loc);
        return block.equals((Object)Material.AIR);
    }
    
    public static boolean isNearChest(final Player p) {
        boolean out = false;
        for (final Material b : BlockUtils.getNearbyBlocks(p.getLocation(), 1)) {
            if (BlockUtils.isChest(b)) {
                out = true;
            }
        }
        return out;
    }
    
    public static boolean isNear1_13(final Player p) {
        boolean out = false;
        for (final Material b : BlockUtils.getNearbyBlocks(p.getLocation(), 1)) {
            if (BlockUtils.Block_1_13(b)) {
                out = true;
            }
        }
        return out;
    }
    
    public static boolean isNearBar(final Player p) {
        boolean out = false;
        for (final Material b : BlockUtils.getNearbyBlocks(p.getLocation(), 1)) {
            if (BlockUtils.isBar(b)) {
                out = true;
            }
        }
        return out;
    }
    
    public static boolean isNearSign(final Player p) {
        boolean out = false;
        for (final Material b : BlockUtils.getNearbyBlocks(p.getLocation(), 1)) {
            if (BlockUtils.isSign(b)) {
                out = true;
            }
        }
        return out;
    }
    
    public static boolean isNearWeb(final Player p) {
        boolean out = false;
        for (final Material b : BlockUtils.getNearbyBlocks(p.getLocation(), 1)) {
            if (BlockUtils.isWeb(b)) {
                out = true;
            }
        }
        return out;
    }
    
    public static boolean isNearSolid(final Player p) {
        boolean out = false;
        for (final Material b : BlockUtils.getNearbyBlocks(p.getLocation(), 1)) {
            if (BlockUtils.isSolid(b)) {
                out = true;
            }
        }
        return out;
    }
    
    public static boolean isNearAllowedPhase(final Player p) {
        boolean out = false;
        for (final Material b : BlockUtils.getNearbyBlocks(p.getLocation(), 1)) {
            if (BlockUtils.allowedPhase(b)) {
                out = true;
            }
        }
        return out;
    }
    
    public static boolean isNearGrass(final Player p) {
        boolean out = false;
        for (final Material b : BlockUtils.getNearbyBlocks(p.getLocation(), 1)) {
            if (BlockUtils.isGrass(b)) {
                out = true;
            }
        }
        return out;
    }
    
    public static boolean isNearLog(final Player p) {
        boolean out = false;
        for (final Material b : BlockUtils.getNearbyBlocks(p.getLocation(), 1)) {
            if (BlockUtils.isLog(b)) {
                out = true;
            }
        }
        return out;
    }
    
    public static boolean isNearAllowed(final Player p) {
        boolean out = false;
        for (final Material b : BlockUtils.getNearbyBlocks(p.getLocation(), 1)) {
            if (BlockUtils.isAllowed(b)) {
                out = true;
            }
        }
        return out;
    }
    
    public static boolean isNearLessThanABlock(final Player p) {
        boolean out = false;
        for (final Material b : BlockUtils.getNearbyBlocks(p.getLocation(), 1)) {
            if (BlockUtils.isLessThanBlock(b)) {
                out = true;
            }
        }
        return out;
    }
    
    public static boolean isNearPiston(final Player p) {
        boolean out = false;
        for (final Material b : BlockUtils.getNearbyBlocks(p.getLocation(), 1)) {
            if (BlockUtils.isPiston(b)) {
                out = true;
            }
        }
        return out;
    }
    
    public static boolean isNearPressure(final Player p) {
        boolean out = false;
        for (final Material b : BlockUtils.getNearbyBlocks(p.getLocation(), 1)) {
            if (BlockUtils.isPressure(b)) {
                out = true;
            }
        }
        return out;
    }
    
    public static boolean isNearSlab(final Player p) {
        boolean out = false;
        for (final Material b : BlockUtils.getNearbyBlocks(p.getLocation(), 1)) {
            if (BlockUtils.isSlab(b)) {
                out = true;
            }
        }
        return out;
    }
    
    public static boolean isNearAir(final Player p) {
        boolean out = false;
        for (final Material b : BlockUtils.getNearbyBlocks(p.getLocation(), 1)) {
            if (BlockUtils.isAir(b)) {
                out = true;
            }
        }
        return out;
    }
    
    public static boolean isNotNearAir(final Player p) {
        boolean out = false;
        for (final Material b : BlockUtils.getNearbyBlocks(p.getLocation(), 1)) {
            if (!BlockUtils.isAir(b)) {
                out = true;
            }
        }
        return out;
    }
    
    public static boolean isNearB_1_13(final Player p) {
        boolean out = false;
        for (final Material b : BlockUtils.getNearbyBlocks(p.getLocation(), 1)) {
            if (!BlockUtils.B_1_13(b)) {
                out = true;
            }
        }
        return out;
    }
    
    public static boolean isNearHalfBlock(final Player p) {
        boolean out = false;
        for (final Material b : BlockUtils.getNearbyBlocks(p.getLocation(), 1)) {
            if (BlockUtils.isHalfBlock(b)) {
                out = true;
            }
        }
        return out;
    }
    
    public static boolean isNearIce(final Player p) {
        boolean out = false;
        for (final Material b : BlockUtils.getNearbyBlocks(p.getLocation(), 1)) {
            if (BlockUtils.isIce(b)) {
                out = true;
            }
        }
        return out;
    }
    
    public static boolean isNearPiston(final Location loc) {
        boolean out = false;
        for (final Material b : BlockUtils.getNearbyBlocks(loc, 3)) {
            if (BlockUtils.isPiston(b)) {
                out = true;
            }
        }
        return out;
    }
    
    public static boolean isNearSlime(final Location loc) {
        boolean out = false;
        for (final Material b : BlockUtils.getNearbyBlocks(loc, 3)) {
            if (BlockUtils.isSlime(b)) {
                out = true;
            }
        }
        return out;
    }
    
    public static boolean isNearSlime(final Player p) {
        boolean out = false;
        for (final Material b : BlockUtils.getNearbyBlocks(p.getLocation(), 1)) {
            if (BlockUtils.isSlime(b)) {
                out = true;
            }
        }
        return out;
    }
    
    public static boolean isNearClimable(final Player p) {
        boolean out = false;
        for (final Material b : BlockUtils.getNearbyBlocks(p.getLocation(), 1)) {
            if (BlockUtils.isClimbableBlock(b)) {
                out = true;
            }
        }
        return out;
    }
    
    public static boolean onGround2(final Player p) {
        return BlockUtils.getBlockType(p.getWorld(), p.getLocation()) != Material.AIR;
    }
    
    public static int hasDepthStrider(final Player player) {
        if (player.getInventory().getBoots() != null && player.getInventory().getBoots().containsEnchantment(Enchantment.getByName("DEPTH_STRIDER"))) {
            return player.getInventory().getBoots().getEnchantments().get(Enchantment.getByName("DEPTH_STRIDER"));
        }
        return 0;
    }
    
    public static boolean isOnGround4(final Player player) {
        if (BlockUtils.getBlockType(player.getWorld(), player.getLocation().clone().add(0.0, -1.0, 0.0)) != Material.AIR) {
            return true;
        }
        Location a = player.getLocation().clone();
        a.setY(a.getY() - 0.5);
        if (BlockUtils.getBlockType(player.getWorld(), a) != Material.AIR) {
            return true;
        }
        a = player.getLocation().clone();
        a.setY(a.getY() + 0.5);
        return BlockUtils.getBlockType(player.getWorld(), player.getLocation().clone().add(0.0, -1.0, 0.0)) != Material.AIR || isBlock(BlockUtils.getBlockType(player.getWorld(), player.getLocation().clone().add(0.0, -1.0, 0.0)), new Material[] { Material.FENCE, Material.FENCE_GATE, Material.COBBLE_WALL, Material.LADDER });
    }
    
    public static int getDistanceToGround(final Player p) {
        final Location loc = p.getLocation().clone();
        final double y = loc.getBlockY();
        int distance = 0;
        for (double i = y; i >= 0.0; --i) {
            loc.setY(i);
            if (BlockUtils.getBlockType(loc.getWorld(), loc).isSolid()) {
                break;
            }
            ++distance;
        }
        return distance;
    }
    
    private static boolean isGround(final Material material) {
        return PlayerUtils.ground.contains((Object)material);
    }
    
    public static boolean isOnGround(final Location loc) {
        final double diff = 0.3;
        return !isGround(BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(0.0, -0.1, 0.0))) || !isGround(BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(0.3, -0.1, 0.0))) || !isGround(BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(-0.3, -0.1, 0.0))) || !isGround(BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(0.0, -0.1, 0.3))) || !isGround(BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(0.0, -0.1, -0.3))) || !isGround(BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(0.3, -0.1, 0.3))) || !isGround(BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(0.3, -0.1, -0.3))) || !isGround(BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(-0.3, -0.1, 0.3))) || !isGround(BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(-0.3, -0.1, -0.3))) || (BlockUtils.getBlockHeight(BlockUtils.getBlockType(loc.getWorld(), loc.clone().subtract(0.0, 0.5, 0.0))) != 0.0 && (!isGround(BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(0.3, BlockUtils.getBlockHeight(BlockUtils.getBlockType(loc.getWorld(), loc)) - 0.1, 0.0))) || !isGround(BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(-0.3, BlockUtils.getBlockHeight(BlockUtils.getBlockType(loc.getWorld(), loc)) - 0.1, 0.0))) || !isGround(BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(0.0, BlockUtils.getBlockHeight(BlockUtils.getBlockType(loc.getWorld(), loc)) - 0.1, 0.3))) || !isGround(BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(0.0, BlockUtils.getBlockHeight(BlockUtils.getBlockType(loc.getWorld(), loc)) - 0.1, -0.3))) || !isGround(BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(0.3, BlockUtils.getBlockHeight(BlockUtils.getBlockType(loc.getWorld(), loc)) - 0.1, 0.3))) || !isGround(BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(0.3, BlockUtils.getBlockHeight(BlockUtils.getBlockType(loc.getWorld(), loc)) - 0.1, -0.3))) || !isGround(BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(-0.3, BlockUtils.getBlockHeight(BlockUtils.getBlockType(loc.getWorld(), loc)) - 0.1, 0.3))) || !isGround(BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(-0.3, BlockUtils.getBlockHeight(BlockUtils.getBlockType(loc.getWorld(), loc)) - 0.1, -0.3)))));
    }
    
    public static boolean hasPistonNear(final Player player) {
        final Object box = ReflectionUtils.getMethodValue(ReflectionUtils.getMethod(ReflectionUtils.getBoundingBox(player).getClass(), "grow", Double.TYPE, Double.TYPE, Double.TYPE), ReflectionUtils.getBoundingBox(player), 2.0, 3.0, 2.0);
        final Collection<?> collidingBlocks = ReflectionUtils.getCollidingBlocks(player, box);
        for (final Object object : collidingBlocks) {
            final double x = (double)ReflectionUtils.getFieldValue(ReflectionUtils.getFieldByName(object.getClass(), "a"), object);
            final double y = (double)ReflectionUtils.getFieldValue(ReflectionUtils.getFieldByName(object.getClass(), "b"), object);
            final double z = (double)ReflectionUtils.getFieldValue(ReflectionUtils.getFieldByName(object.getClass(), "c"), object);
            final Material block = BlockUtils.getBlockType(player.getWorld(), new Location(player.getWorld(), x, y, z));
            if (BlockUtils.isPiston(block)) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isInWater(final Location loc) {
        final double diff = 0.3;
        return BlockUtils.isLiquid(BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(0.0, 0.0, 0.0))) || BlockUtils.isLiquid(BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(0.3, 0.0, 0.0))) || BlockUtils.isLiquid(BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(-0.3, 0.0, 0.0))) || BlockUtils.isLiquid(BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(0.0, 0.0, 0.3))) || BlockUtils.isLiquid(BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(0.0, 0.0, -0.3))) || BlockUtils.isLiquid(BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(0.3, 0.0, 0.3))) || BlockUtils.isLiquid(BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(0.3, 0.0, -0.3))) || BlockUtils.isLiquid(BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(-0.3, 0.0, 0.3))) || BlockUtils.isLiquid(BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(-0.3, 0.0, -0.3))) || (BlockUtils.getBlockHeight(BlockUtils.getBlockType(loc.getWorld(), loc.clone().subtract(0.0, 0.5, 0.0))) != 0.0 && (BlockUtils.isLiquid(BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(0.3, 0.0, 0.0))) || BlockUtils.isLiquid(BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(-0.3, 0.0, 0.0))) || BlockUtils.isLiquid(BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(0.0, 0.0, 0.3))) || BlockUtils.isLiquid(BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(0.0, 0.0, -0.3))) || BlockUtils.isLiquid(BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(0.3, 0.0, 0.3))) || BlockUtils.isLiquid(BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(0.3, 0.0, -0.3))) || BlockUtils.isLiquid(BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(-0.3, 0.0, 0.3))) || BlockUtils.isLiquid(BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(-0.3, 0.0, -0.3)))));
    }
    
    public static boolean isOnSlime(final Location loc) {
        final double diff = 0.3;
        return BlockUtils.isSlime(BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(0.0, 0.0, 0.0))) || BlockUtils.isSlime(BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(0.3, 0.0, 0.0))) || BlockUtils.isSlime(BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(-0.3, 0.0, 0.0))) || BlockUtils.isSlime(BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(0.0, 0.0, 0.3))) || BlockUtils.isSlime(BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(0.0, 0.0, -0.3))) || BlockUtils.isSlime(BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(0.3, 0.0, 0.3))) || BlockUtils.isSlime(BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(0.3, 0.0, -0.3))) || BlockUtils.isSlime(BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(-0.3, 0.0, 0.3))) || BlockUtils.isSlime(BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(-0.3, 0.0, -0.3)));
    }
    
    public static boolean isOnSlab(final Location loc) {
        final double diff = 0.3;
        return BlockUtils.isSlab(BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(0.0, 0.0, 0.0))) || BlockUtils.isSlab(BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(0.3, 0.0, 0.0))) || BlockUtils.isSlab(BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(-0.3, 0.0, 0.0))) || BlockUtils.isSlab(BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(0.0, 0.0, 0.3))) || BlockUtils.isSlab(BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(0.0, 0.0, -0.3))) || BlockUtils.isSlab(BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(0.3, 0.0, 0.3))) || BlockUtils.isSlab(BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(0.3, 0.0, -0.3))) || BlockUtils.isSlab(BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(-0.3, 0.0, 0.3))) || BlockUtils.isSlab(BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(-0.3, 0.0, -0.3)));
    }
    
    public static boolean isOnStair(final Location loc) {
        final double diff = 0.3;
        return BlockUtils.isStair(BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(0.0, 0.0, 0.0))) || BlockUtils.isStair(BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(0.3, 0.0, 0.0))) || BlockUtils.isStair(BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(-0.3, 0.0, 0.0))) || BlockUtils.isStair(BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(0.0, 0.0, 0.3))) || BlockUtils.isStair(BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(0.0, 0.0, -0.3))) || BlockUtils.isStair(BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(0.3, 0.0, 0.3))) || BlockUtils.isStair(BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(0.3, 0.0, -0.3))) || BlockUtils.isStair(BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(-0.3, 0.0, 0.3))) || BlockUtils.isStair(BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(-0.3, 0.0, -0.3)));
    }
    
    public static boolean isOnFence(final Location loc) {
        final double diff = 0.3;
        return BlockUtils.isStair(BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(0.0, 0.0, 0.0))) || BlockUtils.isFence(BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(0.3, 0.0, 0.0))) || BlockUtils.isFence(BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(-0.3, 0.0, 0.0))) || BlockUtils.isFence(BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(0.0, 0.0, 0.3))) || BlockUtils.isFence(BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(0.0, 0.0, -0.3))) || BlockUtils.isFence(BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(0.3, 0.0, 0.3))) || BlockUtils.isFence(BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(0.3, 0.0, -0.3))) || BlockUtils.isFence(BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(-0.3, 0.0, 0.3))) || BlockUtils.isFence(BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(-0.3, 0.0, -0.3)));
    }
    
    public static boolean isOnPressure(final Location loc) {
        final double diff = 0.3;
        return BlockUtils.isStair(BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(0.0, 0.0, 0.0))) || BlockUtils.isStair(BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(0.3, 0.0, 0.0))) || BlockUtils.isStair(BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(-0.3, 0.0, 0.0))) || BlockUtils.isStair(BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(0.0, 0.0, 0.3))) || BlockUtils.isStair(BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(0.0, 0.0, -0.3))) || BlockUtils.isStair(BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(0.3, 0.0, 0.3))) || BlockUtils.isStair(BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(0.3, 0.0, -0.3))) || BlockUtils.isStair(BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(-0.3, 0.0, 0.3))) || BlockUtils.isStair(BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(-0.3, 0.0, -0.3)));
    }
    
    public static boolean isOnClimbable(final Player player, final int blocks) {
        if (blocks == 0) {
            for (final Material block : getSurrounding(BlockUtils.getBlockType(player.getWorld(), player.getLocation()), false, player.getLocation())) {
                if (block == Material.LADDER || block == Material.VINE) {
                    return true;
                }
            }
        }
        else {
            for (final Material block : getSurrounding(BlockUtils.getBlockType(player.getWorld(), player.getLocation().clone().add(0.0, 1.0, 0.0)), false, player.getLocation().clone().add(0.0, 1.0, 0.0))) {
                if (block == Material.LADDER || block == Material.VINE) {
                    return true;
                }
            }
        }
        return BlockUtils.getBlockType(player.getWorld(), player.getLocation()) == Material.LADDER || BlockUtils.getBlockType(player.getWorld(), player.getLocation()) == Material.VINE;
    }
    
    public static boolean isInWeb(final Player player) {
        return BlockUtils.getBlockType(player.getLocation().getWorld(), player.getLocation()) == Material.WEB || BlockUtils.getBlockType(player.getLocation().getWorld(), player.getLocation().clone().add(0.0, -1.0, 0.0)) == Material.WEB || BlockUtils.getBlockType(player.getWorld(), player.getLocation().clone().add(0.0, 1.0, 0.0)) == Material.WEB;
    }
    
    public static ArrayList<Material> getSurrounding(final Material material, final boolean diagonals, final Location location) {
        final ArrayList<Material> blocks = new ArrayList<Material>();
        if (diagonals) {
            for (int x = -1; x <= 1; ++x) {
                for (int y = -1; y <= 1; ++y) {
                    for (int z = -1; z <= 1; ++z) {
                        if (x != 0 || y != 0 || z != 0) {
                            blocks.add(BlockUtils.getBlockType(location.getWorld(), x, y, z));
                        }
                    }
                }
            }
        }
        else {
            blocks.add(BlockUtils.getBlockType(location.getWorld(), location.clone().add(0.0, 1.0, 0.0)));
            blocks.add(BlockUtils.getBlockType(location.getWorld(), location.clone().add(0.0, 1.0, 0.0)));
            blocks.add(BlockUtils.getBlockType(location.getWorld(), location.clone().add(1.0, 0.0, 0.0)));
            blocks.add(BlockUtils.getBlockType(location.getWorld(), location.clone().add(-1.0, 0.0, 0.0)));
            blocks.add(BlockUtils.getBlockType(location.getWorld(), location.clone().add(0.0, 0.0, 1.0)));
            blocks.add(BlockUtils.getBlockType(location.getWorld(), location.clone().add(0.0, 0.0, -1.0)));
        }
        return blocks;
    }
    
    public static Location getEyeLocation(final Player player) {
        final Location eye = player.getLocation();
        eye.setY(eye.getY() + player.getEyeHeight());
        return eye;
    }
    
    public static boolean isBlock(final Material type, final Material[] materials) {
        final Material[] arrayOfMaterial = materials;
        for (int j = materials.length, i = 0; i < j; ++i) {
            final Material m = arrayOfMaterial[i];
            if (m == type) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isAir(final Player player) {
        final Material b = BlockUtils.getBlockType(player.getWorld(), player.getLocation().clone().add(0.0, -1.0, 0.0));
        return b.equals((Object)Material.AIR) && BlockUtils.getBlockType(player.getWorld(), player.getLocation().clone().add(1.0, 0.0, 0.0)).equals((Object)Material.AIR) && BlockUtils.getBlockType(player.getWorld(), player.getLocation().clone().add(-1.0, 0.0, 0.0)).equals((Object)Material.AIR) && BlockUtils.getBlockType(player.getWorld(), player.getLocation().clone().add(0.0, 0.0, 1.0)).equals((Object)Material.AIR) && BlockUtils.getBlockType(player.getWorld(), player.getLocation().clone().add(-1.0, 0.0, 0.0)).equals((Object)Material.AIR);
    }
    
    public static int getPotionEffectLevel(final Player player, final PotionEffectType pet) {
        for (final PotionEffect pe : player.getActivePotionEffects()) {
            if (pe.getType().getName().equals(pet.getName())) {
                return pe.getAmplifier() + 1;
            }
        }
        return 0;
    }
    
    public static boolean isOnTheGround(final Player player) {
        return isOnTheGround(player, 0.25);
    }
    
    public static boolean isOnTheGround(final Player player, final double yExpanded) {
        final Object box = ReflectionUtils.modifyBoundingBox(ReflectionUtils.getBoundingBox(player), 0.0, -yExpanded, 0.0, 0.0, 0.0, 0.0);
        return ReflectionUtils.getCollidingBlocks(player, box).size() > 0;
    }
    
    public static boolean isNotSpider(final Player player) {
        return isOnTheGround(player, 1.25);
    }
    
    public static boolean isInSlime(final Player player) {
        final Object box = ReflectionUtils.getBoundingBox(player);
        final double minX = (double)ReflectionUtils.getInvokedField(ReflectionUtils.getField(box.getClass(), "a"), box);
        final double minY = (double)ReflectionUtils.getInvokedField(ReflectionUtils.getField(box.getClass(), "b"), box);
        final double minZ = (double)ReflectionUtils.getInvokedField(ReflectionUtils.getField(box.getClass(), "c"), box);
        final double maxX = (double)ReflectionUtils.getInvokedField(ReflectionUtils.getField(box.getClass(), "d"), box);
        final double maxY = (double)ReflectionUtils.getInvokedField(ReflectionUtils.getField(box.getClass(), "e"), box);
        final double maxZ = (double)ReflectionUtils.getInvokedField(ReflectionUtils.getField(box.getClass(), "f"), box);
        for (double x = minX; x < maxX; ++x) {
            for (double y = minY; y < maxY; ++y) {
                for (double z = minZ; z < maxZ; ++z) {
                    final Material block = BlockUtils.getBlockType(player.getWorld(), new Location(player.getWorld(), x, y, z));
                    if (BlockUtils.isSlime(block)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public static boolean isInLiquid(final Player player) {
        final Object box = ReflectionUtils.getBoundingBox(player);
        final double minX = (double)ReflectionUtils.getInvokedField(ReflectionUtils.getField(box.getClass(), "a"), box);
        final double minY = (double)ReflectionUtils.getInvokedField(ReflectionUtils.getField(box.getClass(), "b"), box);
        final double minZ = (double)ReflectionUtils.getInvokedField(ReflectionUtils.getField(box.getClass(), "c"), box);
        final double maxX = (double)ReflectionUtils.getInvokedField(ReflectionUtils.getField(box.getClass(), "d"), box);
        final double maxY = (double)ReflectionUtils.getInvokedField(ReflectionUtils.getField(box.getClass(), "e"), box);
        final double maxZ = (double)ReflectionUtils.getInvokedField(ReflectionUtils.getField(box.getClass(), "f"), box);
        for (double x = minX; x < maxX; ++x) {
            for (double y = minY; y < maxY; ++y) {
                for (double z = minZ; z < maxZ; ++z) {
                    final Material block = BlockUtils.getBlockType(player.getWorld(), new Location(player.getWorld(), x, y, z));
                    if (BlockUtils.isLiquid(block)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public static boolean isInStairs(final Player player) {
        final Object box = ReflectionUtils.modifyBoundingBox(ReflectionUtils.getBoundingBox(player), 0.0, -0.5, 0.0, 0.0, 0.0, 0.0);
        final double minX = (double)ReflectionUtils.getInvokedField(ReflectionUtils.getField(box.getClass(), "a"), box);
        final double minY = (double)ReflectionUtils.getInvokedField(ReflectionUtils.getField(box.getClass(), "b"), box);
        final double minZ = (double)ReflectionUtils.getInvokedField(ReflectionUtils.getField(box.getClass(), "c"), box);
        final double maxX = (double)ReflectionUtils.getInvokedField(ReflectionUtils.getField(box.getClass(), "d"), box);
        final double maxY = (double)ReflectionUtils.getInvokedField(ReflectionUtils.getField(box.getClass(), "e"), box);
        final double maxZ = (double)ReflectionUtils.getInvokedField(ReflectionUtils.getField(box.getClass(), "f"), box);
        for (double x = minX; x < maxX; ++x) {
            for (double y = minY; y < maxY; ++y) {
                for (double z = minZ; z < maxZ; ++z) {
                    final Material block = BlockUtils.getBlockType(player.getWorld(), new Location(player.getWorld(), x, y, z));
                    if (BlockUtils.isStair(block) || BlockUtils.isSlab(block) || block.equals((Object)Material.SKULL) || block.equals((Object)Material.CAKE_BLOCK)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public static boolean isOnClimbable(final Player player) {
        return BlockUtils.isClimbableBlock(BlockUtils.getBlockType(player.getWorld(), player.getLocation())) || BlockUtils.isClimbableBlock(BlockUtils.getBlockType(player.getWorld(), player.getLocation().add(0.0, 1.0, 0.0)));
    }
    
    public static boolean inUnderBlock(final Player player) {
        final Object box = ReflectionUtils.modifyBoundingBox(ReflectionUtils.getBoundingBox(player), 0.0, 0.0, 0.0, 0.0, 1.0, 0.0);
        final double minX = (double)ReflectionUtils.getInvokedField(ReflectionUtils.getField(box.getClass(), "a"), box);
        final double minY = (double)ReflectionUtils.getInvokedField(ReflectionUtils.getField(box.getClass(), "b"), box);
        final double minZ = (double)ReflectionUtils.getInvokedField(ReflectionUtils.getField(box.getClass(), "c"), box);
        final double maxX = (double)ReflectionUtils.getInvokedField(ReflectionUtils.getField(box.getClass(), "d"), box);
        final double maxY = (double)ReflectionUtils.getInvokedField(ReflectionUtils.getField(box.getClass(), "e"), box);
        final double maxZ = (double)ReflectionUtils.getInvokedField(ReflectionUtils.getField(box.getClass(), "f"), box);
        for (double x = minX; x < maxX; ++x) {
            for (double y = minY; y < maxY; ++y) {
                for (double z = minZ; z < maxZ; ++z) {
                    final Material block = BlockUtils.getBlockType(player.getWorld(), new Location(player.getWorld(), x, y, z));
                    if (block.isSolid()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public static boolean isOnAllowedPhase(final Player player) {
        final Object box = ReflectionUtils.modifyBoundingBox(ReflectionUtils.getBoundingBox(player), 0.0, -0.5, 0.0, 0.0, 0.0, 0.0);
        final double minX = (double)ReflectionUtils.getInvokedField(ReflectionUtils.getField(box.getClass(), "a"), box);
        final double minY = (double)ReflectionUtils.getInvokedField(ReflectionUtils.getField(box.getClass(), "b"), box);
        final double minZ = (double)ReflectionUtils.getInvokedField(ReflectionUtils.getField(box.getClass(), "c"), box);
        final double maxX = (double)ReflectionUtils.getInvokedField(ReflectionUtils.getField(box.getClass(), "d"), box);
        final double maxY = (double)ReflectionUtils.getInvokedField(ReflectionUtils.getField(box.getClass(), "e"), box);
        final double maxZ = (double)ReflectionUtils.getInvokedField(ReflectionUtils.getField(box.getClass(), "f"), box);
        for (double x = minX; x < maxX; ++x) {
            for (double y = minY; y < maxY; ++y) {
                for (double z = minZ; z < maxZ; ++z) {
                    final Material block = BlockUtils.getBlockType(player.getWorld(), new Location(player.getWorld(), x, y, z));
                    if (BlockUtils.allowedPhase(block)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public static boolean isOnIce(final Player player) {
        final Object box = ReflectionUtils.modifyBoundingBox(ReflectionUtils.getBoundingBox(player), 0.0, -0.5, 0.0, 0.0, 0.0, 0.0);
        final double minX = (double)ReflectionUtils.getInvokedField(ReflectionUtils.getField(box.getClass(), "a"), box);
        final double minY = (double)ReflectionUtils.getInvokedField(ReflectionUtils.getField(box.getClass(), "b"), box);
        final double minZ = (double)ReflectionUtils.getInvokedField(ReflectionUtils.getField(box.getClass(), "c"), box);
        final double maxX = (double)ReflectionUtils.getInvokedField(ReflectionUtils.getField(box.getClass(), "d"), box);
        final double maxY = (double)ReflectionUtils.getInvokedField(ReflectionUtils.getField(box.getClass(), "e"), box);
        final double maxZ = (double)ReflectionUtils.getInvokedField(ReflectionUtils.getField(box.getClass(), "f"), box);
        for (double x = minX; x < maxX; ++x) {
            for (double y = minY; y < maxY; ++y) {
                for (double z = minZ; z < maxZ; ++z) {
                    final Material block = BlockUtils.getBlockType(player.getWorld(), new Location(player.getWorld(), x, y, z));
                    if (block.equals((Object)Material.ICE) || block.equals((Object)Material.PACKED_ICE)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public static boolean isInWater(final Player player) {
        final Material m = BlockUtils.getBlockType(player.getWorld(), player.getLocation());
        return m == Material.STATIONARY_WATER || m == Material.WATER;
    }
    
    public static boolean isPartiallyStuck(final Player player) {
        if (player.getLocation().clone() == null) {
            return false;
        }
        final Material block = BlockUtils.getBlockType(player.getWorld(), player.getLocation().clone());
        return !CheatUtils.isSlab(block) && !CheatUtils.isStair(block) && (BlockUtils.getBlockType(player.getWorld(), player.getLocation().clone().add(0.0, -1.0, 0.0)).isSolid() || BlockUtils.getBlockType(player.getWorld(), player.getLocation().clone().add(0.0, 1.0, 0.0)).isSolid() || (BlockUtils.getBlockType(player.getWorld(), player.getLocation().clone().add(0.0, 1.0, 0.0).add(0.0, -1.0, 0.0)).isSolid() || BlockUtils.getBlockType(player.getWorld(), player.getLocation().clone().add(0.0, 1.0, 0.0).add(0.0, 1.0, 0.0)).isSolid()) || block.isSolid());
    }
    
    public static boolean isFullyStuck(final Player player) {
        final Material block1 = BlockUtils.getBlockType(player.getWorld(), player.getLocation().clone());
        final Material block2 = BlockUtils.getBlockType(player.getWorld(), player.getLocation().clone().add(0.0, 1.0, 0.0));
        return (block1.isSolid() && block2.isSolid()) || BlockUtils.getBlockType(player.getWorld(), player.getLocation().clone().add(0.0, -1.0, 0.0)).isSolid() || (BlockUtils.getBlockType(player.getWorld(), player.getLocation().clone().add(0.0, 1.0, 0.0)).isSolid() && BlockUtils.getBlockType(player.getWorld(), player.getLocation().add(0.0, 1.0, 0.0).add(0.0, -1.0, 0.0)).isSolid()) || BlockUtils.getBlockType(player.getWorld(), player.getLocation().add(0.0, 1.0, 0.0).add(0.0, 1.0, 0.0)).isSolid();
    }
    
    public static boolean isInGround(final Player player) {
        if (BlockUtils.getBlockType(player.getWorld(), player.getLocation().clone().add(0.0, -1.0, 0.0)) != Material.AIR) {
            return true;
        }
        Location a = player.getLocation().clone();
        a.setY(a.getY() - 0.5);
        if (BlockUtils.getBlockType(player.getWorld(), a) != Material.AIR) {
            return true;
        }
        a = player.getLocation().clone();
        a.setY(a.getY() + 0.5);
        return BlockUtils.getBlockType(player.getWorld(), player.getLocation().clone().add(0.0, -1.0, 0.0)) != Material.AIR || CheatUtils.isBlock(BlockUtils.getBlockType(player.getWorld(), player.getLocation().clone().add(0.0, -1.0, 0.0)), new Material[] { Material.FENCE, Material.FENCE_GATE, Material.COBBLE_WALL, Material.LADDER });
    }
    
    static {
        PlayerUtils.checkMovement = null;
        try {
            (PlayerUtils.checkMovement = PlayerConnection.class.getDeclaredField("checkMovement")).setAccessible(true);
        }
        catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        PlayerUtils.ground = (ImmutableSet<Material>)Sets.immutableEnumSet((Enum)Material.SUGAR_CANE, (Enum[])new Material[] { Material.SUGAR_CANE_BLOCK, Material.TORCH, Material.ACTIVATOR_RAIL, Material.AIR, Material.CARROT, Material.CROPS, Material.DEAD_BUSH, Material.DETECTOR_RAIL, Material.DIODE_BLOCK_OFF, Material.DIODE_BLOCK_ON, Material.DOUBLE_PLANT, Material.FIRE, Material.GOLD_PLATE, Material.IRON_PLATE, Material.LAVA, Material.LEVER, Material.LONG_GRASS, Material.MELON_STEM, Material.NETHER_WARTS, Material.PORTAL, Material.POTATO, Material.POWERED_RAIL, Material.PUMPKIN_STEM, Material.RAILS, Material.RED_ROSE, Material.REDSTONE_COMPARATOR_OFF, Material.REDSTONE_COMPARATOR_ON, Material.REDSTONE_TORCH_OFF, Material.REDSTONE_TORCH_ON, Material.REDSTONE_WIRE, Material.SAPLING, Material.SEEDS, Material.SIGN, Material.SIGN_POST, Material.STATIONARY_LAVA, Material.STATIONARY_WATER, Material.STONE_BUTTON, Material.STONE_PLATE, Material.SUGAR_CANE_BLOCK, Material.TORCH, Material.TRIPWIRE, Material.TRIPWIRE_HOOK, Material.WALL_SIGN, Material.WATER, Material.WEB, Material.WOOD_BUTTON, Material.WOOD_PLATE, Material.YELLOW_FLOWER });
    }
}
