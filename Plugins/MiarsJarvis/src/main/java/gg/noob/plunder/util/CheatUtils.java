// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.util;

import java.util.HashSet;
import java.util.HashMap;
import java.util.ArrayList;
import org.bukkit.entity.LivingEntity;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Iterator;
import gg.noob.plunder.data.PlayerData;
import gg.noob.plunder.Plunder;
import org.bukkit.World;
import org.bukkit.util.NumberConversions;
import org.bukkit.inventory.ItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Block;
import org.bukkit.Location;
import java.util.UUID;
import java.util.Set;
import java.util.Map;
import org.bukkit.Material;
import java.util.List;

public final class CheatUtils
{
    public static final String SPY_METADATA = "ac-spydata";
    private static final List<Material> INSTANT_BREAK;
    private static final List<Material> FOOD;
    private static final List<Material> INTERACTABLE;
    private static final Map<Material, Material> COMBO;
    private static Set<UUID> teleported;
    
    public static double getXDelta(final Location one, final Location two) {
        return Math.abs(one.getX() - two.getX());
    }
    
    public static boolean isDoor(final Block block) {
        return block.getType().equals((Object)Material.IRON_DOOR) || block.getType().equals((Object)Material.IRON_DOOR_BLOCK) || block.getType().equals((Object)Material.WOOD_DOOR) || block.getType().equals((Object)Material.WOODEN_DOOR);
    }
    
    public static boolean isFenceGate(final Block block) {
        return block.getType().equals((Object)Material.FENCE_GATE);
    }
    
    public static boolean isTrapDoor(final Block block) {
        return block.getType().equals((Object)Material.TRAP_DOOR);
    }
    
    public static double getZDelta(final Location one, final Location two) {
        return Math.abs(one.getZ() - two.getZ());
    }
    
    public static double getDistance3D(final Location one, final Location two) {
        double toReturn = 0.0;
        final double xSqr = (two.getX() - one.getX()) * (two.getX() - one.getX());
        final double ySqr = (two.getY() - one.getY()) * (two.getY() - one.getY());
        final double zSqr = (two.getZ() - one.getZ()) * (two.getZ() - one.getZ());
        final double sqrt = Math.sqrt(xSqr + ySqr + zSqr);
        toReturn = Math.abs(sqrt);
        return toReturn;
    }
    
    public static double getVerticalDistance(final Location one, final Location two) {
        double toReturn = 0.0;
        final double ySqr = (two.getY() - one.getY()) * (two.getY() - one.getY());
        final double sqrt = Math.sqrt(ySqr);
        toReturn = Math.abs(sqrt);
        return toReturn;
    }
    
    public static double getHorizontalDistance(final Location one, final Location two) {
        double toReturn = 0.0;
        final double xSqr = (two.getX() - one.getX()) * (two.getX() - one.getX());
        final double zSqr = (two.getZ() - one.getZ()) * (two.getZ() - one.getZ());
        final double sqrt = Math.sqrt(xSqr + zSqr);
        toReturn = Math.abs(sqrt);
        return toReturn;
    }
    
    public static boolean cantStandAtBetter(final Block block) {
        final Block otherBlock = block.getRelative(BlockFace.DOWN);
        final boolean center1 = otherBlock.getType() == Material.AIR;
        final boolean north1 = otherBlock.getRelative(BlockFace.NORTH).getType() == Material.AIR;
        final boolean east1 = otherBlock.getRelative(BlockFace.EAST).getType() == Material.AIR;
        final boolean south1 = otherBlock.getRelative(BlockFace.SOUTH).getType() == Material.AIR;
        final boolean west1 = otherBlock.getRelative(BlockFace.WEST).getType() == Material.AIR;
        final boolean northeast1 = otherBlock.getRelative(BlockFace.NORTH_EAST).getType() == Material.AIR;
        final boolean northwest1 = otherBlock.getRelative(BlockFace.NORTH_WEST).getType() == Material.AIR;
        final boolean southeast1 = otherBlock.getRelative(BlockFace.SOUTH_EAST).getType() == Material.AIR;
        final boolean southwest1 = otherBlock.getRelative(BlockFace.SOUTH_WEST).getType() == Material.AIR;
        final boolean overAir1 = otherBlock.getRelative(BlockFace.DOWN).getType() == Material.AIR || otherBlock.getRelative(BlockFace.DOWN).getType() == Material.WATER || otherBlock.getRelative(BlockFace.DOWN).getType() == Material.LAVA;
        return center1 && north1 && east1 && south1 && west1 && northeast1 && southeast1 && northwest1 && southwest1 && overAir1;
    }
    
    public static boolean cantStandAtSingle(final Block block) {
        final Block otherBlock = block.getRelative(BlockFace.DOWN);
        final boolean center = otherBlock.getType() == Material.AIR;
        return center;
    }
    
    public static boolean cantStandAtWater(final Location loc) {
        final boolean isHover = BlockUtils.getBlockType(loc.getWorld(), loc.clone()) == Material.AIR;
        final boolean n = BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(1.0, -1.0, 0.0)) == Material.WATER || BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(1.0, -1.0, 0.0)) == Material.STATIONARY_WATER;
        final boolean s = BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(-1.0, -1.0, 0.0)) == Material.WATER || BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(-1.0, -1.0, 0.0)) == Material.STATIONARY_WATER;
        final boolean e = BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(0.0, -1.0, 1.0)) == Material.WATER || BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(0.0, -1.0, 1.0)) == Material.STATIONARY_WATER;
        final boolean w = BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(0.0, -1.0, -1.0)) == Material.WATER || BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(0.0, -1.0, -1.0)) == Material.STATIONARY_WATER;
        final boolean ne = BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(1.0, -1.0, 1.0)) == Material.WATER || BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(1.0, -1.0, 1.0)) == Material.STATIONARY_WATER;
        final boolean nw = BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(1.0, -1.0, -1.0)) == Material.WATER || BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(1.0, -1.0, -1.0)) == Material.STATIONARY_WATER;
        final boolean se = BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(-1.0, -1.0, 1.0)) == Material.WATER || BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(-1.0, -1.0, 1.0)) == Material.STATIONARY_WATER;
        final boolean sw = BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(-1.0, -1.0, -1.0)) == Material.WATER || BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(-1.0, -1.0, -1.0)) == Material.STATIONARY_WATER;
        return n && s && e && w && ne && nw && se && sw && isHover;
    }
    
    public static boolean canStandWithin(final Material type) {
        final boolean isSand = type == Material.SAND;
        final boolean isGravel = type == Material.GRAVEL;
        final boolean solid = type.isSolid() && !type.name().toLowerCase().contains("door") && !type.name().toLowerCase().contains("fence") && !type.name().toLowerCase().contains("bars") && !type.name().toLowerCase().contains("sign");
        return !isSand && !isGravel && !solid;
    }
    
    public static Vector getRotation(final Location one, final Location two) {
        final double dx = two.getX() - one.getX();
        final double dy = two.getY() - one.getY();
        final double dz = two.getZ() - one.getZ();
        final double distanceXZ = Math.sqrt(dx * dx + dz * dz);
        final float yaw = (float)(Math.atan2(dz, dx) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float)(-(Math.atan2(dy, distanceXZ) * 180.0 / 3.141592653589793));
        return new Vector(yaw, pitch, 0.0f);
    }
    
    public static double clamp180(double theta) {
        theta %= 360.0;
        if (theta >= 180.0) {
            theta -= 360.0;
        }
        if (theta < -180.0) {
            theta += 360.0;
        }
        return theta;
    }
    
    public static int getLevelForEnchantment(final Player player, final String enchantment) {
        try {
            final Enchantment theEnchantment = Enchantment.getByName(enchantment);
            ItemStack[] arrayOfItemStack;
            for (int j = (arrayOfItemStack = player.getInventory().getArmorContents()).length, i = 0; i < j; ++i) {
                final ItemStack item = arrayOfItemStack[i];
                if (item.containsEnchantment(theEnchantment)) {
                    return item.getEnchantmentLevel(theEnchantment);
                }
            }
        }
        catch (Exception e) {
            return -1;
        }
        return -1;
    }
    
    public static boolean cantStandAt(final Block block) {
        return !canStand(block) && cantStandClose(block) && cantStandFar(block);
    }
    
    public static boolean cantStandAtExp(final Location location) {
        return cantStandAt(new Location(location.getWorld(), fixXAxis(location.getX()), location.getY() - 0.01, (double)location.getBlockZ()).getBlock());
    }
    
    public static boolean cantStandClose(final Block block) {
        return !canStand(block.getRelative(BlockFace.NORTH)) && !canStand(block.getRelative(BlockFace.EAST)) && !canStand(block.getRelative(BlockFace.SOUTH)) && !canStand(block.getRelative(BlockFace.WEST));
    }
    
    public static boolean cantStandFar(final Block block) {
        return !canStand(block.getRelative(BlockFace.NORTH_WEST)) && !canStand(block.getRelative(BlockFace.NORTH_EAST)) && !canStand(block.getRelative(BlockFace.SOUTH_WEST)) && !canStand(block.getRelative(BlockFace.SOUTH_EAST));
    }
    
    public static boolean canStand(final Block block) {
        return !block.isLiquid() && block.getType() != Material.AIR;
    }
    
    public static boolean isFullyInWater(final Location player) {
        final double touchedX = fixXAxis(player.getX());
        return BlockUtils.isLiquid(BlockUtils.getBlockType(player.getWorld(), new Location(player.getWorld(), touchedX, player.getY(), (double)player.getBlockZ()))) && BlockUtils.isLiquid(BlockUtils.getBlockType(player.getWorld(), new Location(player.getWorld(), touchedX, (double)Math.round(player.getY()), (double)player.getBlockZ())));
    }
    
    public static double fixXAxis(final double x) {
        double touchedX = x;
        final double rem = touchedX - Math.round(touchedX) + 0.01;
        if (rem < 0.3) {
            touchedX = NumberConversions.floor(x) - 1;
        }
        return touchedX;
    }
    
    public static boolean isOnGround(final Location location, final int down) {
        final double posX = location.getX();
        final double posZ = location.getZ();
        final double fracX = (MathUtils.getFraction(posX) > 0.0) ? Math.abs(MathUtils.getFraction(posX)) : (1.0 - Math.abs(MathUtils.getFraction(posX)));
        final double fracZ = (MathUtils.getFraction(posZ) > 0.0) ? Math.abs(MathUtils.getFraction(posZ)) : (1.0 - Math.abs(MathUtils.getFraction(posZ)));
        final int blockX = location.getBlockX();
        final int blockY = location.getBlockY() - down;
        final int blockZ = location.getBlockZ();
        final World world = location.getWorld();
        if (BlockUtils.isSolid(BlockUtils.getBlockType(world, blockX, blockY, blockZ))) {
            return true;
        }
        if (fracX < 0.3) {
            if (BlockUtils.isSolid(BlockUtils.getBlockType(world, blockX - 1, blockY, blockZ))) {
                return true;
            }
            if (fracZ < 0.3) {
                return BlockUtils.isSolid(BlockUtils.getBlockType(world, blockX - 1, blockY, blockZ - 1)) || BlockUtils.isSolid(BlockUtils.getBlockType(world, blockX, blockY, blockZ - 1)) || BlockUtils.isSolid(BlockUtils.getBlockType(world, blockX + 1, blockY, blockZ - 1));
            }
            if (fracZ > 0.7) {
                return BlockUtils.isSolid(BlockUtils.getBlockType(world, blockX - 1, blockY, blockZ + 1)) || BlockUtils.isSolid(BlockUtils.getBlockType(world, blockX, blockY, blockZ + 1)) || BlockUtils.isSolid(BlockUtils.getBlockType(world, blockX + 1, blockY, blockZ + 1));
            }
        }
        else if (fracX > 0.7) {
            if (BlockUtils.isSolid(BlockUtils.getBlockType(world, blockX + 1, blockY, blockZ))) {
                return true;
            }
            if (fracZ < 0.3) {
                return BlockUtils.isSolid(BlockUtils.getBlockType(world, blockX - 1, blockY, blockZ - 1)) || BlockUtils.isSolid(BlockUtils.getBlockType(world, blockX, blockY, blockZ - 1)) || BlockUtils.isSolid(BlockUtils.getBlockType(world, blockX + 1, blockY, blockZ - 1));
            }
            if (fracZ > 0.7) {
                return BlockUtils.isSolid(BlockUtils.getBlockType(world, blockX - 1, blockY, blockZ + 1)) || BlockUtils.isSolid(BlockUtils.getBlockType(world, blockX, blockY, blockZ + 1)) || BlockUtils.isSolid(BlockUtils.getBlockType(world, blockX + 1, blockY, blockZ + 1));
            }
        }
        else {
            if (fracZ < 0.3) {
                return BlockUtils.isSolid(BlockUtils.getBlockType(world, blockX, blockY, blockZ - 1));
            }
            return fracZ > 0.7 && BlockUtils.isSolid(BlockUtils.getBlockType(world, blockX, blockY, blockZ + 1));
        }
        return false;
    }
    
    public static boolean isHoveringOverWater(final Location player, final int blocks) {
        for (int i = player.getBlockY(); i > player.getBlockY() - blocks; --i) {
            final Material type = BlockUtils.getBlockType(player.getWorld(), player.getBlockX(), i, player.getBlockZ());
            if (type != Material.AIR) {
                return BlockUtils.isLiquid(type);
            }
        }
        return false;
    }
    
    public static boolean isHoveringOverWater(final Location player) {
        return isHoveringOverWater(player, 25);
    }
    
    public static boolean isInstantBreak(final Material m) {
        return CheatUtils.INSTANT_BREAK.contains(m);
    }
    
    public static boolean isFood(final Material m) {
        return CheatUtils.FOOD.contains(m);
    }
    
    public static String getCardinalDirection(final Player player) {
        double rotation = (player.getLocation().getYaw() - 90.0f) % 360.0f;
        if (rotation < 0.0) {
            rotation += 360.0;
        }
        if (0.0 <= rotation && rotation < 22.5) {
            return "N";
        }
        if (22.5 <= rotation && rotation < 67.5) {
            return "NE";
        }
        if (67.5 <= rotation && rotation < 112.5) {
            return "E";
        }
        if (112.5 <= rotation && rotation < 157.5) {
            return "SE";
        }
        if (157.5 <= rotation && rotation < 202.5) {
            return "S";
        }
        if (202.5 <= rotation && rotation < 247.5) {
            return "SW";
        }
        if (247.5 <= rotation && rotation < 292.5) {
            return "W";
        }
        if (292.5 <= rotation && rotation < 337.5) {
            return "NW";
        }
        if (337.5 <= rotation && rotation < 360.0) {
            return "N";
        }
        return null;
    }
    
    public static boolean isSlab(final Material type) {
        switch (type) {
            case STEP:
            case WOOD_STEP: {
                return true;
            }
            default: {
                return false;
            }
        }
    }
    
    public static boolean isStair(final Material type) {
        switch (type) {
            case COMMAND:
            case COBBLESTONE_STAIRS:
            case BRICK_STAIRS:
            case ACACIA_STAIRS:
            case BIRCH_WOOD_STAIRS:
            case DARK_OAK_STAIRS:
            case JUNGLE_WOOD_STAIRS:
            case NETHER_BRICK_STAIRS:
            case QUARTZ_STAIRS:
            case SANDSTONE_STAIRS:
            case SMOOTH_STAIRS:
            case SPRUCE_WOOD_STAIRS:
            case WOOD_STAIRS: {
                return true;
            }
            default: {
                return false;
            }
        }
    }
    
    public static boolean isInteractable(final Material m) {
        return CheatUtils.INTERACTABLE.contains(m);
    }
    
    public static boolean sprintFly(final Player player) {
        final PlayerData data = Plunder.getInstance().getDataManager().getPlayerData(player);
        return data != null && (data.getSprintTicks() > 0 || player.getAllowFlight());
    }
    
    public static boolean isOnLilyPad(final Player player) {
        final Material block = BlockUtils.getBlockType(player.getWorld(), player.getLocation());
        final Material lily = Material.WATER_LILY;
        return block == lily || BlockUtils.getBlockType(player.getWorld(), player.getLocation().clone().add(1.0, 0.0, 0.0)) == lily || BlockUtils.getBlockType(player.getWorld(), player.getLocation().clone().add(-1.0, 0.0, 0.0)) == lily || BlockUtils.getBlockType(player.getWorld(), player.getLocation().clone().add(0.0, 0.0, 1.0)) == lily || BlockUtils.getBlockType(player.getWorld(), player.getLocation().clone().add(0.0, 0.0, -1.0)) == lily;
    }
    
    public static boolean isSubmersed(final Player player) {
        return player.getLocation().getBlock().isLiquid() && player.getLocation().getBlock().getRelative(BlockFace.UP).isLiquid();
    }
    
    public static boolean isInWater(final Player player) {
        return player.getLocation().getBlock().isLiquid() || player.getLocation().getBlock().getRelative(BlockFace.DOWN).isLiquid() || player.getLocation().getBlock().getRelative(BlockFace.UP).isLiquid();
    }
    
    public static boolean isClimbableBlock(final Block block) {
        return block.getType() == Material.VINE || block.getType() == Material.LADDER || block.getType() == Material.WATER || block.getType() == Material.STATIONARY_WATER;
    }
    
    public static boolean isInt(final String string) {
        try {
            Integer.parseInt(string);
            return true;
        }
        catch (Exception ex) {
            return false;
        }
    }
    
    public static boolean isDouble(final String string) {
        try {
            Double.parseDouble(string);
            return true;
        }
        catch (Exception ex) {
            return false;
        }
    }
    
    public static boolean blocksNear(final Player player) {
        return blocksNear(player.getLocation());
    }
    
    public static boolean blocksNear(final Location loc) {
        boolean nearBlocks = false;
        for (final Material block : BlockUtils.getSurrounding(loc, true)) {
            if (block != Material.AIR) {
                nearBlocks = true;
                break;
            }
        }
        for (final Material block : BlockUtils.getSurrounding(loc, false)) {
            if (block != Material.AIR) {
                nearBlocks = true;
                break;
            }
        }
        loc.setY(loc.getY() - 0.5);
        if (BlockUtils.getBlockType(loc.getWorld(), loc) != Material.AIR) {
            nearBlocks = true;
        }
        if (isBlock(BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(0.0, -0.3, 0.0)), new Material[] { Material.FENCE, Material.FENCE_GATE, Material.COBBLE_WALL, Material.LADDER })) {
            nearBlocks = true;
        }
        return nearBlocks;
    }
    
    public static boolean slabsNear(final Location loc) {
        boolean nearBlocks = false;
        for (final Material bl : BlockUtils.getSurrounding(loc, true)) {
            if (bl.equals((Object)Material.STEP) || bl.equals((Object)Material.DOUBLE_STEP) || bl.equals((Object)Material.WOOD_DOUBLE_STEP) || bl.equals((Object)Material.WOOD_STEP)) {
                nearBlocks = true;
                break;
            }
        }
        for (final Material bl : BlockUtils.getSurrounding(loc, false)) {
            if (bl.equals((Object)Material.STEP) || bl.equals((Object)Material.DOUBLE_STEP) || bl.equals((Object)Material.WOOD_DOUBLE_STEP) || bl.equals((Object)Material.WOOD_STEP)) {
                nearBlocks = true;
                break;
            }
        }
        if (isBlock(BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(0.0, -1.0, 0.0)), new Material[] { Material.STEP, Material.DOUBLE_STEP, Material.WOOD_DOUBLE_STEP, Material.WOOD_STEP })) {
            nearBlocks = true;
        }
        return nearBlocks;
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
    
    public static String[] getCommands(final String command) {
        return command.replaceAll("COMMAND\\[", "").replaceAll("]", "").split(";");
    }
    
    public static String removeWhitespace(final String string) {
        return string.replaceAll(" ", "");
    }
    
    public static boolean hasArmorEnchantment(final Player player, final Enchantment e) {
        ItemStack[] arrayOfItemStack;
        for (int j = (arrayOfItemStack = player.getInventory().getArmorContents()).length, i = 0; i < j; ++i) {
            final ItemStack is = arrayOfItemStack[i];
            if (is != null && is.containsEnchantment(e)) {
                return true;
            }
        }
        return false;
    }
    
    public static String listToCommaString(final List<String> list) {
        final StringBuilder b = new StringBuilder();
        for (int i = 0; i < list.size(); ++i) {
            b.append(list.get(i));
            if (i < list.size() - 1) {
                b.append(",");
            }
        }
        return b.toString();
    }
    
    public static long lifeToSeconds(final String string) {
        if (string.equals("0") || string.equals("")) {
            return 0L;
        }
        final String[] lifeMatch = { "d", "h", "m", "s" };
        final int[] lifeInterval = { 86400, 3600, 60, 1 };
        long seconds = 0L;
        for (int i = 0; i < lifeMatch.length; ++i) {
            final Matcher matcher = Pattern.compile("([0-9]*)" + lifeMatch[i]).matcher(string);
            while (matcher.find()) {
                seconds += Integer.parseInt(matcher.group(1)) * lifeInterval[i];
            }
        }
        return seconds;
    }
    
    public static double[] cursor(final Player player, final LivingEntity entity) {
        final Location entityLoc = entity.getLocation().add(0.0, entity.getEyeHeight(), 0.0);
        final Location playerLoc = player.getLocation().add(0.0, player.getEyeHeight(), 0.0);
        final Vector playerRotation = new Vector(playerLoc.getYaw(), playerLoc.getPitch(), 0.0f);
        final Vector expectedRotation = getRotation(playerLoc, entityLoc);
        final double deltaYaw = clamp180(playerRotation.getX() - expectedRotation.getX());
        final double deltaPitch = clamp180(playerRotation.getY() - expectedRotation.getY());
        final double horizontalDistance = getHorizontalDistance(playerLoc, entityLoc);
        final double distance = getDistance3D(playerLoc, entityLoc);
        final double offsetX = deltaYaw * horizontalDistance * distance;
        final double offsetY = deltaPitch * Math.abs(Math.sqrt(entityLoc.getY() - playerLoc.getY())) * distance;
        return new double[] { Math.abs(offsetX), Math.abs(offsetY) };
    }
    
    public static double getAimbotoffset(final Location playerLocLoc, final double playerEyeHeight, final LivingEntity entity) {
        final Location entityLoc = entity.getLocation().add(0.0, entity.getEyeHeight(), 0.0);
        final Location playerLoc = playerLocLoc.add(0.0, playerEyeHeight, 0.0);
        final Vector playerRotation = new Vector(playerLoc.getYaw(), playerLoc.getPitch(), 0.0f);
        final Vector expectedRotation = getRotation(playerLoc, entityLoc);
        final double deltaYaw = clamp180(playerRotation.getX() - expectedRotation.getX());
        final double horizontalDistance = getHorizontalDistance(playerLoc, entityLoc);
        final double distance = getDistance3D(playerLoc, entityLoc);
        final double offsetX = deltaYaw * horizontalDistance * distance;
        return offsetX;
    }
    
    public static double getAimbotoffset2(final Location playerLocLoc, final double playerEyeHeight, final LivingEntity entity) {
        final Location entityLoc = entity.getLocation().add(0.0, entity.getEyeHeight(), 0.0);
        final Location playerLoc = playerLocLoc.add(0.0, playerEyeHeight, 0.0);
        final Vector playerRotation = new Vector(playerLoc.getYaw(), playerLoc.getPitch(), 0.0f);
        final Vector expectedRotation = getRotation(playerLoc, entityLoc);
        final double deltaPitch = clamp180(playerRotation.getY() - expectedRotation.getY());
        final double distance = getDistance3D(playerLoc, entityLoc);
        final double offsetY = deltaPitch * Math.abs(Math.sqrt(entityLoc.getY() - playerLoc.getY())) * distance;
        return offsetY;
    }
    
    public static double[] getOffsetsOffCursor(final Player player, final LivingEntity entity) {
        final Location entityLoc = entity.getLocation().add(0.0, entity.getEyeHeight(), 0.0);
        final Location playerLoc = player.getLocation().add(0.0, player.getEyeHeight(), 0.0);
        final Vector playerRotation = new Vector(playerLoc.getYaw(), playerLoc.getPitch(), 0.0f);
        final Vector expectedRotation = getRotation(playerLoc, entityLoc);
        final double deltaYaw = clamp180(playerRotation.getX() - expectedRotation.getX());
        final double deltaPitch = clamp180(playerRotation.getY() - expectedRotation.getY());
        final double horizontalDistance = getHorizontalDistance(playerLoc, entityLoc);
        final double distance = getDistance3D(playerLoc, entityLoc);
        final double offsetX = deltaYaw * horizontalDistance * distance;
        final double offsetY = deltaPitch * Math.abs(Math.sqrt(entityLoc.getY() - playerLoc.getY())) * distance;
        return new double[] { Math.abs(offsetX), Math.abs(offsetY) };
    }
    
    public static double getOffsetOffCursor(final Player player, final LivingEntity entity) {
        double offset = 0.0;
        final double[] offsets = getOffsetsOffCursor(player, entity);
        offset += offsets[0];
        offset += offsets[1];
        return offset;
    }
    
    static {
        INSTANT_BREAK = new ArrayList<Material>();
        FOOD = new ArrayList<Material>();
        INTERACTABLE = new ArrayList<Material>();
        COMBO = new HashMap<Material, Material>();
        CheatUtils.teleported = new HashSet<UUID>();
        CheatUtils.INSTANT_BREAK.add(Material.RED_MUSHROOM);
        CheatUtils.INSTANT_BREAK.add(Material.RED_ROSE);
        CheatUtils.INSTANT_BREAK.add(Material.BROWN_MUSHROOM);
        CheatUtils.INSTANT_BREAK.add(Material.YELLOW_FLOWER);
        CheatUtils.INSTANT_BREAK.add(Material.REDSTONE);
        CheatUtils.INSTANT_BREAK.add(Material.REDSTONE_TORCH_OFF);
        CheatUtils.INSTANT_BREAK.add(Material.REDSTONE_TORCH_ON);
        CheatUtils.INSTANT_BREAK.add(Material.REDSTONE_WIRE);
        CheatUtils.INSTANT_BREAK.add(Material.LONG_GRASS);
        CheatUtils.INSTANT_BREAK.add(Material.PAINTING);
        CheatUtils.INSTANT_BREAK.add(Material.WHEAT);
        CheatUtils.INSTANT_BREAK.add(Material.SUGAR_CANE);
        CheatUtils.INSTANT_BREAK.add(Material.SUGAR_CANE_BLOCK);
        CheatUtils.INSTANT_BREAK.add(Material.DIODE);
        CheatUtils.INSTANT_BREAK.add(Material.DIODE_BLOCK_OFF);
        CheatUtils.INSTANT_BREAK.add(Material.DIODE_BLOCK_ON);
        CheatUtils.INSTANT_BREAK.add(Material.SAPLING);
        CheatUtils.INSTANT_BREAK.add(Material.TORCH);
        CheatUtils.INSTANT_BREAK.add(Material.CROPS);
        CheatUtils.INSTANT_BREAK.add(Material.SNOW);
        CheatUtils.INSTANT_BREAK.add(Material.TNT);
        CheatUtils.INSTANT_BREAK.add(Material.POTATO);
        CheatUtils.INSTANT_BREAK.add(Material.CARROT);
        CheatUtils.INTERACTABLE.add(Material.STONE_BUTTON);
        CheatUtils.INTERACTABLE.add(Material.LEVER);
        CheatUtils.INTERACTABLE.add(Material.CHEST);
        CheatUtils.FOOD.add(Material.COOKED_BEEF);
        CheatUtils.FOOD.add(Material.COOKED_CHICKEN);
        CheatUtils.FOOD.add(Material.COOKED_FISH);
        CheatUtils.FOOD.add(Material.GRILLED_PORK);
        CheatUtils.FOOD.add(Material.PORK);
        CheatUtils.FOOD.add(Material.MUSHROOM_SOUP);
        CheatUtils.FOOD.add(Material.RAW_BEEF);
        CheatUtils.FOOD.add(Material.RAW_CHICKEN);
        CheatUtils.FOOD.add(Material.RAW_FISH);
        CheatUtils.FOOD.add(Material.APPLE);
        CheatUtils.FOOD.add(Material.GOLDEN_APPLE);
        CheatUtils.FOOD.add(Material.MELON);
        CheatUtils.FOOD.add(Material.COOKIE);
        CheatUtils.FOOD.add(Material.BREAD);
        CheatUtils.FOOD.add(Material.SPIDER_EYE);
        CheatUtils.FOOD.add(Material.ROTTEN_FLESH);
        CheatUtils.FOOD.add(Material.POTATO_ITEM);
        CheatUtils.COMBO.put(Material.SHEARS, Material.WOOL);
        CheatUtils.COMBO.put(Material.IRON_SWORD, Material.WEB);
        CheatUtils.COMBO.put(Material.DIAMOND_SWORD, Material.WEB);
        CheatUtils.COMBO.put(Material.STONE_SWORD, Material.WEB);
        CheatUtils.COMBO.put(Material.WOOD_SWORD, Material.WEB);
    }
}
