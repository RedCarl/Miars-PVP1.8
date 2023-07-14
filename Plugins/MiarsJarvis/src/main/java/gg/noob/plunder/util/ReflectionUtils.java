// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.util;

import org.bukkit.util.Vector;
import java.lang.reflect.InvocationTargetException;
import org.bukkit.Location;
import org.bukkit.World;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.material.WoodenStep;
import org.bukkit.material.Step;
import org.bukkit.Material;
import java.util.Collection;
import org.bukkit.entity.Player;
import org.bukkit.block.Block;
import java.lang.reflect.Field;
import gg.noob.plunder.Plunder;
import sun.misc.Unsafe;
import java.lang.reflect.Method;

public class ReflectionUtils
{
    private static String version;
    private static Class<?> iBlockData;
    private static Class<?> blockPosition;
    private static Class<?> worldServer;
    private static Class<?> vanillaBlock;
    public static Class<?> EntityPlayer;
    public static Class<?> Entity;
    public static Class<?> CraftPlayer;
    public static final Class<?> CraftWorld;
    public static final Class<?> World;
    private static final Method getBlocks;
    private static final Method getBlocks1_12;
    private static final Unsafe UNSAFE;
    
    private static Unsafe getUnsafeInstance() {
        try {
            final Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
            unsafeField.setAccessible(true);
            return (Unsafe)unsafeField.get(null);
        }
        catch (NoSuchFieldException | IllegalAccessException ex2) {
            Plunder.getInstance().getLogger().severe("Could not locate Unsafe object!");
            ex2.printStackTrace();
            return null;
        }
    }
    
    public static float getFriction(final Block block) {
        final Object blockNMS = getVanillaBlock(block);
        return (float)getFieldValue(getFieldByName(ReflectionUtils.vanillaBlock, "frictionFactor"), blockNMS);
    }
    
    public static float getFriction(final net.minecraft.server.v1_8_R3.Block blockNMS) {
        return (float)getFieldValue(getFieldByName(ReflectionUtils.vanillaBlock, "frictionFactor"), blockNMS);
    }
    
    public static Method getMethod(final Class<?> object, final String method, final Class<?>... args) {
        try {
            final Method methodObject = object.getMethod(method, args);
            methodObject.setAccessible(true);
            return methodObject;
        }
        catch (NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static Object getInvokedMethod(final Method method, final Object object, final Object... args) {
        try {
            method.setAccessible(true);
            return method.invoke(object, args);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static Field getField(final Class<?> object, final String field) {
        try {
            final Field fieldObject = object.getField(field);
            fieldObject.setAccessible(true);
            return fieldObject;
        }
        catch (NoSuchFieldException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static <T> void setUnsafe(final Object object, final Field field, final T value) {
        ReflectionUtils.UNSAFE.putObject(object, ReflectionUtils.UNSAFE.objectFieldOffset(field), value);
    }
    
    public static Object getInvokedField(final Field field, final Object object) {
        try {
            field.setAccessible(true);
            return field.get(object);
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static Class<?> getNMSClass(final String string) {
        return getClass("net.minecraft.server." + ReflectionUtils.version + "." + string);
    }
    
    public static Collection<?> getCollidingBlocks(final Player player, final Object axisAlignedBB) {
        final Object world = getInvokedMethod(getMethod(ReflectionUtils.CraftWorld, "getHandle", (Class<?>[])new Class[0]), player.getWorld(), new Object[0]);
        return (Collection<?>)(isNewVersion() ? getInvokedMethod(ReflectionUtils.getBlocks1_12, world, null, axisAlignedBB) : getInvokedMethod(ReflectionUtils.getBlocks, world, axisAlignedBB));
    }
    
    public static Boolean getCollidingBlocks1(final Player player, final Object axisAlignedBB) {
        final Object world = getInvokedMethod(getMethod(ReflectionUtils.CraftWorld, "getHandle", (Class<?>[])new Class[0]), player.getWorld(), new Object[0]);
        return (Boolean)(isNewVersion() ? getInvokedMethod(ReflectionUtils.getBlocks1_12, world, null, axisAlignedBB) : getInvokedMethod(ReflectionUtils.getBlocks, world, axisAlignedBB));
    }
    
    public static Object getBoundingBox(final Player player) {
        return isBukkitVerison("1_7") ? getInvokedField(getField(ReflectionUtils.Entity, "boundingBox"), getEntityPlayer(player)) : getInvokedMethod(getMethod(ReflectionUtils.EntityPlayer, "getBoundingBox", (Class<?>[])new Class[0]), getEntityPlayer(player), new Object[0]);
    }
    
    public static Object expandBoundingBox(final Object box, final double x, final double y, final double z) {
        return getInvokedMethod(getMethod(box.getClass(), "grow", Double.TYPE, Double.TYPE, Double.TYPE), box, x, y, z);
    }
    
    public static Object modifyBoundingBox(final Object box, final double minX, final double minY, final double minZ, final double maxX, final double maxY, final double maxZ) {
        final double newminX = (double)getInvokedField(getField(box.getClass(), "a"), box) + minX;
        final double newminY = (double)getInvokedField(getField(box.getClass(), "b"), box) + minY;
        final double newminZ = (double)getInvokedField(getField(box.getClass(), "c"), box) + minZ;
        final double newmaxX = (double)getInvokedField(getField(box.getClass(), "d"), box) + maxX;
        final double newmaxY = (double)getInvokedField(getField(box.getClass(), "e"), box) + maxY;
        final double newmaxZ = (double)getInvokedField(getField(box.getClass(), "f"), box) + maxZ;
        try {
            return getNMSClass("AxisAlignedBB").getConstructor(Double.TYPE, Double.TYPE, Double.TYPE, Double.TYPE, Double.TYPE, Double.TYPE).newInstance(newminX, newminY, newminZ, newmaxX, newmaxY, newmaxZ);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static Object getEntityPlayer(final Player player) {
        return getInvokedMethod(getMethod(ReflectionUtils.CraftPlayer, "getHandle", (Class<?>[])new Class[0]), player, new Object[0]);
    }
    
    public static BoundingBox getBlockBoundingBox(final Block block) {
        try {
            if (!isBukkitVerison("1_7") && ReflectionUtils.blockPosition != null) {
                final Object bPos = ReflectionUtils.blockPosition.getConstructor(Integer.TYPE, Integer.TYPE, Integer.TYPE).newInstance(block.getLocation().getBlockX(), block.getLocation().getBlockY(), block.getLocation().getBlockZ());
                final Object world = getWorldHandle(block.getWorld());
                final Object data = getMethodValue(getMethod(world.getClass(), "getType", ReflectionUtils.blockPosition), world, bPos);
                final Object blockNMS = getMethodValue(getMethod(getNMSClass("IBlockData"), "getBlock", (Class<?>[])new Class[0]), data, new Object[0]);
                if (!isNewVersion()) {
                    if (getMethodValueNoST(getMethodNoST(blockNMS.getClass(), "a", ReflectionUtils.World, ReflectionUtils.blockPosition, ReflectionUtils.iBlockData), blockNMS, world, bPos, data) != null && !BlockUtils.isSlab(block.getType())) {
                        BoundingBox box = toBoundingBox(getMethodValue(getMethod(blockNMS.getClass(), "a", ReflectionUtils.World, ReflectionUtils.blockPosition, ReflectionUtils.iBlockData), blockNMS, world, bPos, data));
                        if (block.getType().equals((Object)Material.STEP)) {
                            final Step slab = (Step)block.getType().getNewData(block.getData());
                            box.minY = (float)block.getY();
                            box.maxY = (float)block.getY();
                            if (slab.isInverted()) {
                                box = box.add(0.0f, 0.5f, 0.0f, 0.0f, 1.0f, 0.0f);
                            }
                            else {
                                box = box.add(0.0f, 0.0f, 0.0f, 0.0f, 0.5f, 0.0f);
                            }
                        }
                        else if (block.getType().equals((Object)Material.WOOD_STEP)) {
                            final WoodenStep slab2 = (WoodenStep)block.getType().getNewData(block.getData());
                            box.minY = (float)block.getY();
                            box.maxY = (float)block.getY();
                            if (slab2.isInverted()) {
                                box = box.add(0.0f, 0.5f, 0.0f, 0.0f, 1.0f, 0.0f);
                            }
                            else {
                                box = box.add(0.0f, 0.0f, 0.0f, 0.0f, 0.5f, 0.0f);
                            }
                        }
                        return box;
                    }
                }
                else {
                    if (getMethodValueNoST(getMethodNoST(blockNMS.getClass(), "a", ReflectionUtils.iBlockData, getNMSClass("IBlockAccess"), ReflectionUtils.blockPosition), blockNMS, data, world, bPos) != null) {
                        return toBoundingBox(getMethodValue(getMethod(blockNMS.getClass(), "a", ReflectionUtils.iBlockData, getNMSClass("IBlockAccess"), ReflectionUtils.blockPosition), blockNMS, data, world, bPos)).add((float)block.getX(), (float)block.getY(), (float)block.getZ(), (float)block.getX(), (float)block.getY(), (float)block.getZ());
                    }
                    if (getMethodValueNoST(getMethodNoST(ReflectionUtils.vanillaBlock, "a", ReflectionUtils.iBlockData, getNMSClass("IBlockAccess"), ReflectionUtils.blockPosition), blockNMS, data, world, bPos) != null) {
                        return toBoundingBox(getMethodValue(getMethod(ReflectionUtils.vanillaBlock, "a", ReflectionUtils.iBlockData, getNMSClass("IBlockAccess"), ReflectionUtils.blockPosition), blockNMS, data, world, bPos)).add((float)block.getX(), (float)block.getY(), (float)block.getZ(), (float)block.getX(), (float)block.getY(), (float)block.getZ());
                    }
                    return new BoundingBox((float)block.getX(), (float)block.getY(), (float)block.getZ(), (float)block.getX(), (float)block.getY(), (float)block.getZ());
                }
            }
            else {
                final Object blockNMS2 = getVanillaBlock(block);
                final Object world = getWorldHandle(block.getWorld());
                if (getMethodValueNoST(getMethodNoST(ReflectionUtils.vanillaBlock, "a", getNMSClass("World"), Integer.TYPE, Integer.TYPE, Integer.TYPE), blockNMS2, world, block.getLocation().getBlockX(), block.getLocation().getBlockY(), block.getLocation().getBlockZ()) != null) {
                    return toBoundingBox(getMethodValue(getMethod(ReflectionUtils.vanillaBlock, "a", getNMSClass("World"), Integer.TYPE, Integer.TYPE, Integer.TYPE), blockNMS2, world, block.getLocation().getBlockX(), block.getLocation().getBlockY(), block.getLocation().getBlockZ()));
                }
                return new BoundingBox((float)block.getX(), (float)block.getY(), (float)block.getZ(), (float)block.getX(), (float)block.getY(), (float)block.getZ());
            }
        }
        catch (Exception e) {
            Bukkit.getLogger().log(Level.SEVERE, "Error occured with block: " + block.getType().toString());
            e.printStackTrace();
        }
        return null;
    }
    
    private static Object getVanillaBlock(final Block block) {
        if (!isBukkitVerison("1_7") && ReflectionUtils.iBlockData != null) {
            final Object getType = getBlockData(block);
            return getMethodValue(getMethod(ReflectionUtils.iBlockData, "getBlock", (Class<?>[])new Class[0]), getType, new Object[0]);
        }
        final Object world = getWorldHandle(block.getWorld());
        return getMethodValue(getMethod(ReflectionUtils.worldServer, "getType", Integer.TYPE, Integer.TYPE, Integer.TYPE), world, block.getLocation().getBlockX(), block.getLocation().getBlockY(), block.getLocation().getBlockZ());
    }
    
    private static Object getWorldHandle(final World world) {
        return getMethodValue(getMethod(ReflectionUtils.CraftWorld, "getHandle", (Class<?>[])new Class[0]), world, new Object[0]);
    }
    
    private static Object getBlockData(final Block block) {
        final Location loc = block.getLocation();
        try {
            if (!isBukkitVerison("1_7")) {
                final Object bPos = ReflectionUtils.blockPosition.getConstructor(Integer.TYPE, Integer.TYPE, Integer.TYPE).newInstance(block.getLocation().getBlockX(), block.getLocation().getBlockY(), block.getLocation().getBlockZ());
                final Object world = getWorldHandle(block.getWorld());
                return getMethodValue(getMethod(ReflectionUtils.worldServer, "getType", ReflectionUtils.blockPosition), world, bPos);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    private static boolean isBukkitVerison(final String version) {
        final String bukkit = Bukkit.getServer().getClass().getPackage().getName().substring(23);
        return bukkit.contains(version);
    }
    
    private static boolean isNewVersion() {
        return isBukkitVerison("1_9") || isBukkitVerison("1_1");
    }
    
    private static Class<?> getCBClass(final String string) {
        return getClass("org.bukkit.craftbukkit." + ReflectionUtils.version + "." + string);
    }
    
    public static Class<?> getClass(final String string) {
        try {
            return Class.forName(string);
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static Field getFieldByName(final Class<?> clazz, final String fieldName) {
        try {
            final Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static Object newBoundingBox(final double minX, final double minY, final double minZ, final double maxX, final double maxY, final double maxZ) {
        try {
            return isBukkitVerison("1_7") ? getMethodValue(getMethod(getNMSClass("AxisAlignedBB"), "a", Double.TYPE, Double.TYPE, Double.TYPE, Double.TYPE, Double.TYPE, Double.TYPE), null, minX, minY, minZ, maxX, maxY, maxZ) : getNMSClass("AxisAlignedBB").getConstructor(Double.TYPE, Double.TYPE, Double.TYPE, Double.TYPE, Double.TYPE, Double.TYPE).newInstance(minX, minY, minZ, maxX, maxY, maxZ);
        }
        catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    static Object getMethodValue(final Method method, final Object object, final Object... args) {
        try {
            return method.invoke(object, args);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static Object getFieldValue(final Field field, final Object object) {
        try {
            field.setAccessible(true);
            return field.get(object);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private static Object getMethodValueNoST(final Method method, final Object object, final Object... args) {
        try {
            return method.invoke(object, args);
        }
        catch (Exception e) {
            return null;
        }
    }
    
    private static Vector getBoxMin(final Object box) {
        final double x = (double)getFieldValue(getFieldByName(box.getClass(), "a"), box);
        final double y = (double)getFieldValue(getFieldByName(box.getClass(), "b"), box);
        final double z = (double)getFieldValue(getFieldByName(box.getClass(), "c"), box);
        return new Vector(x, y, z);
    }
    
    private static Vector getBoxMax(final Object box) {
        final double x = (double)getFieldValue(getFieldByName(box.getClass(), "d"), box);
        final double y = (double)getFieldValue(getFieldByName(box.getClass(), "e"), box);
        final double z = (double)getFieldValue(getFieldByName(box.getClass(), "f"), box);
        return new Vector(x, y, z);
    }
    
    public static BoundingBox toBoundingBox(final Object aaBB) {
        final Vector min = getBoxMin(aaBB);
        final Vector max = getBoxMax(aaBB);
        return new BoundingBox((float)min.getX(), (float)min.getY(), (float)min.getZ(), (float)max.getX(), (float)max.getY(), (float)max.getZ());
    }
    
    private static Method getMethodNoST(final Class<?> clazz, final String methodName, final Class<?>... args) {
        try {
            final Method method = clazz.getMethod(methodName, args);
            method.setAccessible(true);
            return method;
        }
        catch (Exception ex) {
            return null;
        }
    }
    
    static {
        ReflectionUtils.version = Bukkit.getServer().getClass().getPackage().getName().substring(23);
        ReflectionUtils.worldServer = getNMSClass("WorldServer");
        ReflectionUtils.vanillaBlock = getNMSClass("Block");
        ReflectionUtils.EntityPlayer = getNMSClass("EntityPlayer");
        ReflectionUtils.Entity = getNMSClass("Entity");
        ReflectionUtils.CraftPlayer = getCBClass("entity.CraftPlayer");
        CraftWorld = getCBClass("CraftWorld");
        World = getNMSClass("World");
        getBlocks = getMethod(ReflectionUtils.World, "a", getNMSClass("AxisAlignedBB"));
        getBlocks1_12 = getMethod(ReflectionUtils.World, "getCubes", getNMSClass("Entity"), getNMSClass("AxisAlignedBB"));
        UNSAFE = getUnsafeInstance();
    }
}
