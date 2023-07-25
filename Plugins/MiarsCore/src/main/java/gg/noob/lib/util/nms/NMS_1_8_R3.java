package gg.noob.lib.util.nms;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.comphenix.protocol.wrappers.WrappedWatchableObject;
import com.google.common.collect.Iterators;
import com.google.common.collect.Maps;
import gg.noob.lib.util.ClientVersionUtil;
import gg.noob.lib.util.EntityUtils;
import gg.noob.lib.util.reflect.*;
import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import net.minecraft.server.v1_8_R3.MathHelper;
import org.apache.commons.lang.Validate;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class NMS_1_8_R3 {

    public static final Map<String, Pair<Integer, Float>> mapEntityTypes = Maps.newHashMap();

    // UTILITY
    private static final Class<?> ENTITY_CLASS;
    private static final Class<?> ITEM_STACK_CLASS;
    private static final ReflectMethod CRAFT_ITEM_NMS_COPY_METHOD;
    // DATA WATCHER
    private static final Class<?> DATA_WATCHER_CLASS;
    private static final ReflectConstructor DATA_WATCHER_CONSTRUCTOR;
    private static final ReflectMethod DATA_WATCHER_A_METHOD;
    // MATH HELPER
    private static final Class<?> MATH_HELPER_CLASS;
    private static final ReflectMethod MATH_HELPER_FLOOR_METHOD;
    private static final ReflectMethod MATH_HELPER_D_METHOD;
    // PACKETS
    private static final ReflectConstructor PACKET_SPAWN_ENTITY_CONSTRUCTOR;
    private static final ReflectConstructor PACKET_SPAWN_ENTITY_LIVING_CONSTRUCTOR;
    private static final ReflectConstructor PACKET_ENTITY_METADATA_CONSTRUCTOR;
    private static final ReflectConstructor PACKET_ENTITY_TELEPORT_CONSTRUCTOR;
    private static final ReflectConstructor PACKET_ATTACH_ENTITY_CONSTRUCTOR;
    private static final ReflectConstructor PACKET_ENTITY_EQUIPMENT_CONSTRUCTOR;
    private static final ReflectConstructor PACKET_ENTITY_DESTROY_CONSTRUCTOR;
    // SEND PACKET
    public static final Class<?> PACKET_CLASS;
    public static final ReflectMethod CRAFT_PLAYER_GET_HANDLE_METHOD;
    public static final ReflectMethod PLAYER_CONNECTION_SEND_PACKET_METHOD;
    public static ReflectField<?> ENTITY_PLAYER_CONNECTION_FIELD;
    // PACKET LISTENER
    public static ReflectField<?> PLAYER_CONNECTION_NETWORK_MANAGER_FIELD;
    public static ReflectField<?> NETWORK_MANAGER_CHANNEL_FIELD;

    static {
        // UTILITY
        ENTITY_CLASS = ReflectionUtil.getNMSClass("Entity");
        ITEM_STACK_CLASS = ReflectionUtil.getNMSClass("ItemStack");
        CRAFT_ITEM_NMS_COPY_METHOD = new ReflectMethod(ReflectionUtil.getObcClass("inventory.CraftItemStack"), "asNMSCopy", ItemStack.class);
        // DATA WATCHER
        DATA_WATCHER_CLASS = ReflectionUtil.getNMSClass("DataWatcher");
        DATA_WATCHER_CONSTRUCTOR = new ReflectConstructor(DATA_WATCHER_CLASS, ENTITY_CLASS);
        DATA_WATCHER_A_METHOD = new ReflectMethod(DATA_WATCHER_CLASS, "a", int.class, Object.class);
        // MATH HELPER
        MATH_HELPER_CLASS = ReflectionUtil.getNMSClass("MathHelper");
        MATH_HELPER_FLOOR_METHOD = new ReflectMethod(MATH_HELPER_CLASS, "floor", double.class);
        MATH_HELPER_D_METHOD = new ReflectMethod(MATH_HELPER_CLASS, "d", float.class);
        // PACKETS
        PACKET_SPAWN_ENTITY_CONSTRUCTOR = new ReflectConstructor(ReflectionUtil.getNMSClass("PacketPlayOutSpawnEntity"));
        PACKET_SPAWN_ENTITY_LIVING_CONSTRUCTOR = new ReflectConstructor(ReflectionUtil.getNMSClass("PacketPlayOutSpawnEntityLiving"));
        PACKET_ENTITY_METADATA_CONSTRUCTOR = new ReflectConstructor(ReflectionUtil.getNMSClass("PacketPlayOutEntityMetadata"), int.class, DATA_WATCHER_CLASS, boolean.class);
        PACKET_ENTITY_TELEPORT_CONSTRUCTOR = new ReflectConstructor(ReflectionUtil.getNMSClass("PacketPlayOutEntityTeleport"));
        PACKET_ATTACH_ENTITY_CONSTRUCTOR = new ReflectConstructor(ReflectionUtil.getNMSClass("PacketPlayOutAttachEntity"));
        PACKET_ENTITY_EQUIPMENT_CONSTRUCTOR = new ReflectConstructor(ReflectionUtil.getNMSClass("PacketPlayOutEntityEquipment"), int.class, int.class, ITEM_STACK_CLASS);
        PACKET_ENTITY_DESTROY_CONSTRUCTOR = new ReflectConstructor(ReflectionUtil.getNMSClass("PacketPlayOutEntityDestroy"), int[].class);

        // SEND PACKET
        Class<?> entityPlayerClass;
        Class<?> playerConnectionClass;
        Class<?> craftPlayerClass;
        Class<?> networkManagerClass;
        if (Version.afterOrEqual(17)) {
            entityPlayerClass = ReflectionUtil.getNMClass("server.level.EntityPlayer");
            playerConnectionClass = ReflectionUtil.getNMClass("server.network.PlayerConnection");
            craftPlayerClass = ReflectionUtil.getObcClass("entity.CraftPlayer");
            networkManagerClass = ReflectionUtil.getNMClass("network.NetworkManager");
            PACKET_CLASS = ReflectionUtil.getNMClass("network.protocol.Packet");
            // Because spigot
            for (Field field : entityPlayerClass.getFields()) {
                if (field.getType().isAssignableFrom(playerConnectionClass) && ENTITY_PLAYER_CONNECTION_FIELD == null) {
                    ENTITY_PLAYER_CONNECTION_FIELD = new ReflectField<>(entityPlayerClass, field.getName());
                    break;
                }
            }
            for (Field field : networkManagerClass.getFields()) {
                if (field.getType().isAssignableFrom(Channel.class) && NETWORK_MANAGER_CHANNEL_FIELD == null) {
                    NETWORK_MANAGER_CHANNEL_FIELD = new ReflectField<>(networkManagerClass, field.getName());
                    break;
                }
            }
            for (Field field : playerConnectionClass.getFields()) {
                if (field.getType().isAssignableFrom(networkManagerClass) && PLAYER_CONNECTION_NETWORK_MANAGER_FIELD == null) {
                    PLAYER_CONNECTION_NETWORK_MANAGER_FIELD = new ReflectField<>(playerConnectionClass, field.getName());
                    break;
                }
            }
        } else {
            entityPlayerClass = ReflectionUtil.getNMSClass("EntityPlayer");
            playerConnectionClass = ReflectionUtil.getNMSClass("PlayerConnection");
            craftPlayerClass = ReflectionUtil.getObcClass("entity.CraftPlayer");
            networkManagerClass = ReflectionUtil.getNMSClass("NetworkManager");
            PACKET_CLASS = ReflectionUtil.getNMSClass("Packet");
            ENTITY_PLAYER_CONNECTION_FIELD = new ReflectField<>(entityPlayerClass, "playerConnection");
            PLAYER_CONNECTION_NETWORK_MANAGER_FIELD = new ReflectField<>(playerConnectionClass, "networkManager");
            NETWORK_MANAGER_CHANNEL_FIELD = new ReflectField<>(networkManagerClass, "channel");
        }
        CRAFT_PLAYER_GET_HANDLE_METHOD = new ReflectMethod(craftPlayerClass, "getHandle");
        if (Version.afterOrEqual(18)) {
            PLAYER_CONNECTION_SEND_PACKET_METHOD = new ReflectMethod(playerConnectionClass, "a", PACKET_CLASS);
        } else {
            PLAYER_CONNECTION_SEND_PACKET_METHOD = new ReflectMethod(playerConnectionClass, "sendPacket", PACKET_CLASS);
        }

        if (Version.beforeOrEqual(12)) {
            // Entities
            mapEntityTypes.put("BAT", new Pair<>(65, 0.9f));
            mapEntityTypes.put("BLAZE", new Pair<>(61, 1.8f));
            mapEntityTypes.put("CAVE_SPIDER", new Pair<>(59, 0.5f));
            mapEntityTypes.put("CHICKEN", new Pair<>(93, 0.7f));
            mapEntityTypes.put("COW", new Pair<>(92, 1.4f));
            mapEntityTypes.put("CREEPER", new Pair<>(50, 1.7f));
            mapEntityTypes.put("DONKEY", new Pair<>(31, 1.39648f));
            mapEntityTypes.put("ELDER_GUARDIAN", new Pair<>(4, 2.9f));
            mapEntityTypes.put("ENDER_DRAGON", new Pair<>(63, 8.0f));
            mapEntityTypes.put("ENDERMAN", new Pair<>(58, 2.9f));
            mapEntityTypes.put("ENDERMITE", new Pair<>(67, 0.3f));
            mapEntityTypes.put("EVOKER", new Pair<>(34, 1.95f));
            mapEntityTypes.put("HORSE", new Pair<>(100, 1.6f));
            mapEntityTypes.put("HUSK", new Pair<>(23, 1.95f));
            mapEntityTypes.put("ILLUSIONER", new Pair<>(37, 1.95f));
            mapEntityTypes.put("LLAMA", new Pair<>(103, 1.87f));
            mapEntityTypes.put("MAGMA_CUBE", new Pair<>(62, 0.51000005f));
            mapEntityTypes.put("MULE", new Pair<>(32, 1.6f));
            mapEntityTypes.put("MUSHROOM_COW", new Pair<>(96, 1.4f));
            mapEntityTypes.put("OCELOT", new Pair<>(98, 0.7f));
            mapEntityTypes.put("PARROT", new Pair<>(105, 0.9f));
            mapEntityTypes.put("PIG", new Pair<>(90, 0.9f));
            mapEntityTypes.put("PIG_ZOMBIE", new Pair<>(57, 1.8f));
            mapEntityTypes.put("POLAR_BEAR", new Pair<>(102, 1.4f));
            mapEntityTypes.put("RABBIT", new Pair<>(101, 0.5f));
            mapEntityTypes.put("SHEEP", new Pair<>(91, 1.3f));
            mapEntityTypes.put("SILVERFISH", new Pair<>(60, 0.3f));
            mapEntityTypes.put("SKELETON", new Pair<>(51, 1.99f));
            mapEntityTypes.put("SKELETON_HORSE", new Pair<>(28, 1.6f));
            mapEntityTypes.put("SLIME", new Pair<>(55, 0.51000005f));
            mapEntityTypes.put("SNOWMAN", new Pair<>(97, 1.9f));
            mapEntityTypes.put("GUARDIAN", new Pair<>(68, 0.85f));
            mapEntityTypes.put("SPIDER", new Pair<>(52, 0.9f));
            mapEntityTypes.put("SQUID", new Pair<>(94, 1.8f));
            mapEntityTypes.put("STRAY", new Pair<>(6, 1.99f));
            mapEntityTypes.put("VEX", new Pair<>(35, 0.95f));
            mapEntityTypes.put("VILLAGER", new Pair<>(120, 1.95f));
            mapEntityTypes.put("IRON_GOLEM", new Pair<>(99, 2.7f));
            mapEntityTypes.put("VINDICATOR", new Pair<>(36, 1.95f));
            mapEntityTypes.put("WITCH", new Pair<>(66, 1.95f));
            mapEntityTypes.put("WITHER", new Pair<>(64, 3.5f));
            mapEntityTypes.put("WITHER_SKELETON", new Pair<>(5, 2.4f));
            mapEntityTypes.put("WOLF", new Pair<>(95, 0.85f));
            mapEntityTypes.put("ZOMBIE", new Pair<>(54, 1.95f));
            mapEntityTypes.put("ZOMBIE_HORSE", new Pair<>(29, 1.6f));
            mapEntityTypes.put("ZOMBIE_VILLAGER", new Pair<>(27, 1.95f));

            // Objects
            mapEntityTypes.put("ENDER_CRYSTAL", new Pair<>(200, 2.0f));
            mapEntityTypes.put("ARROW", new Pair<>(10, 0.5f));
            mapEntityTypes.put("SNOWBALL", new Pair<>(11, 0.25f));
            mapEntityTypes.put("EGG", new Pair<>(7, 0.25f));
            mapEntityTypes.put("FIREBALL", new Pair<>(12, 1.0f));
            mapEntityTypes.put("SMALL_FIREBALL", new Pair<>(13, 0.3125f));
            mapEntityTypes.put("ENDER_PEARL", new Pair<>(14, 0.25f));
            mapEntityTypes.put("ENDER_SIGNAL", new Pair<>(15, 0.25f));
            mapEntityTypes.put("FIREWORK", new Pair<>(22, 0.25f));
        } else {
            // Entities
            mapEntityTypes.put("BAT", new Pair<>(3, 0.9f));
            mapEntityTypes.put("BLAZE", new Pair<>(4, 1.8f));
            mapEntityTypes.put("CAVE_SPIDER", new Pair<>(6, 0.5f));
            mapEntityTypes.put("CHICKEN", new Pair<>(7, 0.7f));
            mapEntityTypes.put("COD", new Pair<>(8, 0.3f));
            mapEntityTypes.put("COW", new Pair<>(9, 1.4f));
            mapEntityTypes.put("CREEPER", new Pair<>(10, 1.7f));
            mapEntityTypes.put("DONKEY", new Pair<>(11, 1.39648f));
            mapEntityTypes.put("DOLPHIN", new Pair<>(12, 0.6f));
            mapEntityTypes.put("DROWNED", new Pair<>(14, 1.95f));
            mapEntityTypes.put("ELDER_GUARDIAN", new Pair<>(15, 2.9f));
            mapEntityTypes.put("ENDER_DRAGON", new Pair<>(17, 8.0f));
            mapEntityTypes.put("ENDERMAN", new Pair<>(18, 2.9f));
            mapEntityTypes.put("ENDERMITE", new Pair<>(19, 0.3f));
            mapEntityTypes.put("EVOKER", new Pair<>(21, 1.95f));
            mapEntityTypes.put("HORSE", new Pair<>(28, 1.6f));
            mapEntityTypes.put("HUSK", new Pair<>(30, 1.95f));
            mapEntityTypes.put("ILLUSIONER", new Pair<>(31, 1.95f));
            mapEntityTypes.put("LLAMA", new Pair<>(36, 1.87f));
            mapEntityTypes.put("MAGMA_CUBE", new Pair<>(38, 0.51000005f));
            mapEntityTypes.put("MULE", new Pair<>(46, 1.6f));
            mapEntityTypes.put("MUSHROOM_COW", new Pair<>(47, 1.4f));
            mapEntityTypes.put("OCELOT", new Pair<>(48, 0.7f));
            mapEntityTypes.put("PARROT", new Pair<>(50, 0.9f));
            mapEntityTypes.put("PIG", new Pair<>(51, 0.9f));
            mapEntityTypes.put("PUFFERFISH", new Pair<>(52, 0.7f));
            mapEntityTypes.put("PIG_ZOMBIE", new Pair<>(53, 1.8f));
            mapEntityTypes.put("POLAR_BEAR", new Pair<>(54, 1.4f));
            mapEntityTypes.put("RABBIT", new Pair<>(56, 0.5f));
            mapEntityTypes.put("SALMON", new Pair<>(57, 0.4f));
            mapEntityTypes.put("SHEEP", new Pair<>(58, 1.3f));
            mapEntityTypes.put("SILVERFISH", new Pair<>(61, 0.3f));
            mapEntityTypes.put("SKELETON", new Pair<>(62, 1.99f));
            mapEntityTypes.put("SKELETON_HORSE", new Pair<>(63, 1.6f));
            mapEntityTypes.put("SLIME", new Pair<>(64, 0.51000005f));
            mapEntityTypes.put("SNOWMAN", new Pair<>(66, 1.9f));
            mapEntityTypes.put("GUARDIAN", new Pair<>(68, 0.85f));
            mapEntityTypes.put("SPIDER", new Pair<>(69, 0.9f));
            mapEntityTypes.put("SQUID", new Pair<>(70, 1.8f));
            mapEntityTypes.put("STRAY", new Pair<>(71, 1.99f));
            mapEntityTypes.put("TROPICAL_FISH", new Pair<>(72, 0.4f));
            mapEntityTypes.put("TURTLE", new Pair<>(73, 0.4f));
            mapEntityTypes.put("VEX", new Pair<>(78, 0.95f));
            mapEntityTypes.put("VILLAGER", new Pair<>(79, 1.95f));
            mapEntityTypes.put("IRON_GOLEM", new Pair<>(80, 2.7f));
            mapEntityTypes.put("VINDICATOR", new Pair<>(81, 1.95f));
            mapEntityTypes.put("WITCH", new Pair<>(82, 1.95f));
            mapEntityTypes.put("WITHER", new Pair<>(83, 3.5f));
            mapEntityTypes.put("WITHER_SKELETON", new Pair<>(84, 2.4f));
            mapEntityTypes.put("WOLF", new Pair<>(86, 0.85f));
            mapEntityTypes.put("ZOMBIE", new Pair<>(87, 1.95f));
            mapEntityTypes.put("ZOMBIE_HORSE", new Pair<>(88, 1.6f));
            mapEntityTypes.put("ZOMBIE_VILLAGER", new Pair<>(89, 1.95f));
            mapEntityTypes.put("PHANTOM", new Pair<>(90, 0.5f));

            // Objects
            mapEntityTypes.put("ENDER_CRYSTAL", new Pair<>(51, 2.0f));
            mapEntityTypes.put("ARROW", new Pair<>(60, 0.5f));
            mapEntityTypes.put("SNOWBALL", new Pair<>(61, 0.25f));
            mapEntityTypes.put("EGG", new Pair<>(62, 0.25f));
            mapEntityTypes.put("FIREBALL", new Pair<>(63, 1.0f));
            mapEntityTypes.put("SMALL_FIREBALL", new Pair<>(64, 0.3125f));
            mapEntityTypes.put("ENDER_PEARL", new Pair<>(65, 0.25f));
            mapEntityTypes.put("ENDER_SIGNAL", new Pair<>(72, 0.25f));
            mapEntityTypes.put("FIREWORK", new Pair<>(76, 0.25f));
        }
    }

    public static int getFreeEntityId() {
        return EntityUtils.getFakeEntityId();
    }

    public static void showFakeEntity(Player player, Location location, EntityType entityType, int entityId) {
        Validate.notNull(entityType);
        showFakeEntity(player, location, getEntityTypeId(entityType), entityId);
    }


    public static void showFakeEntityLiving(Player player, Location location, EntityType entityType, int entityId) {
        Object dataWatcher = DATA_WATCHER_CONSTRUCTOR.newInstance(ENTITY_CLASS.cast(null));
        DATA_WATCHER_A_METHOD.invoke(dataWatcher, 15, (byte) 0);
        showFakeEntityLiving(player, location, getEntityTypeId(entityType), entityId, dataWatcher);
    }


    public static void showFakeEntityArmorStand(Player player, Location location, int entityId, boolean invisible, boolean small, boolean clickable) {
        Object dataWatcher = DATA_WATCHER_CONSTRUCTOR.newInstance(ENTITY_CLASS.cast(null));
        DATA_WATCHER_A_METHOD.invoke(dataWatcher, 0, (byte) (invisible ? 0x20 : 0x00)); // Invisible
        byte data = 0x08;
        if (small) {
            data += 0x01;
        }
        if (!clickable) {
            data += 0x10;
        }
        if (ClientVersionUtil.getProtocolVersion(player) <= 20) {
            data += (byte) (data | 2);
            location.add(0, -1, 0);
        } else {
            location.add(0, -2, 0);
        }
        DATA_WATCHER_A_METHOD.invoke(dataWatcher, 10, data);
        showFakeEntityLiving(player, location, 30, entityId, dataWatcher);
    }

    public static void showFakeEntityZombie(Player player, Location location, int entityId, boolean invisible, boolean small, boolean clickable) {
        Object dataWatcher = DATA_WATCHER_CONSTRUCTOR.newInstance(ENTITY_CLASS.cast(null));
        DATA_WATCHER_A_METHOD.invoke(dataWatcher, 0, (byte) (invisible ? 0x20 : 0x00)); // Invisible
        byte data = 0x08;
        if (small) {
            data += 0x01;
        }
        if (!clickable) {
            data += 0x10;
        }
        DATA_WATCHER_A_METHOD.invoke(dataWatcher, 10, data);
        showFakeEntityLiving(player, location, EntityType.ZOMBIE.getTypeId(), entityId, dataWatcher);
    }


    public static void updateFakeEntityHorse(Player player, int entityId, String text) {
        final PacketContainer container = new PacketContainer(PacketType.Play.Server.ENTITY_METADATA);
        container.getIntegers().write(0, entityId);
        final WrappedDataWatcher wrappedDataWatcher = new WrappedDataWatcher();

        wrappedDataWatcher.setObject(2, text);
        final List<WrappedWatchableObject> watchableObjects = Arrays.asList(Iterators.toArray(wrappedDataWatcher.iterator(), WrappedWatchableObject.class));
        container.getWatchableCollectionModifier().write(0, watchableObjects);
        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, container);
        } catch (Exception ex) {
            //
        }
    }

    public static void showOldFakeArmorStand(Player player, Location location, int horseId, String text) {
        int skullId = getFreeEntityId();
        PacketContainer skullPacket=createWitherSkull(location, skullId);
        PacketContainer horsePacket=createHorse(location, horseId, text);
        PacketContainer attachPacket=new PacketContainer(PacketType.Play.Server.ATTACH_ENTITY);
        attachPacket.getIntegers().write(1, horseId);
        attachPacket.getIntegers().write(2, skullId);

        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, skullPacket);
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, horsePacket);
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, attachPacket);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public static PacketContainer createHorse(Location location, int id, String text) {
        PacketContainer horse = new PacketContainer(PacketType.Play.Server.SPAWN_ENTITY_LIVING);
        horse.getIntegers().write(0, id);
        horse.getIntegers().write(1, 100);
        horse.getIntegers().write(2, (int) (location.getX() * 32.0));
        horse.getIntegers().write(3, (int) (location.getY() * 32.0));
        horse.getIntegers().write(4, (int) (location.getZ() * 32.0));
        WrappedDataWatcher watcher = new WrappedDataWatcher();
        watcher.setObject(0, (byte) 0x20);
        watcher.setObject(1, 300);
        watcher.setObject(2, ChatColor.translateAlternateColorCodes('&', text));
        watcher.setObject(3, (byte) 1);
        watcher.setObject(10, (byte) (0x08 + 0x01));
        watcher.setObject(12, (byte) 1);
        horse.getDataWatcherModifier().write(0, watcher);

        return horse;
    }

    public static PacketContainer createWitherSkull(Location location, int id) {
        PacketContainer skull=new PacketContainer(PacketType.Play.Server.SPAWN_ENTITY);
        skull.getIntegers().write(0, id);
        skull.getIntegers().write(1, (int) (location.getX() * 32.0));
        skull.getIntegers().write(2, MathHelper.floor((location.getY() - 0.13 + 55.0) * 32.0));
        skull.getIntegers().write(3, (int) (location.getZ() * 32.0));
        skull.getIntegers().write(9, 66);

        return skull;
    }


    public static void showFakeEntityItem(Player player, Location location, ItemStack itemStack, int entityId) {
        Validate.notNull(player);
        Validate.notNull(location);
        Validate.notNull(itemStack);

        Object nmsItemStack = CRAFT_ITEM_NMS_COPY_METHOD.invokeStatic(itemStack);
        Object dataWatcher = DATA_WATCHER_CONSTRUCTOR.newInstance(ENTITY_CLASS.cast(null));
        if (nmsItemStack == null || dataWatcher == null) {
            return;
        }
        DATA_WATCHER_A_METHOD.invoke(dataWatcher, 10, nmsItemStack);
        showFakeEntity(player, location, 2, entityId);
        sendPacket(player, PACKET_ENTITY_METADATA_CONSTRUCTOR.newInstance(entityId, dataWatcher, true));
        teleportFakeEntity(player, location, entityId);
    }


    public static void updateFakeEntityCustomName(Player player, String name, int entityId) {
        Validate.notNull(player);
        Validate.notNull(name);

        Object dataWatcher = DATA_WATCHER_CONSTRUCTOR.newInstance(ENTITY_CLASS.cast(null));
        DATA_WATCHER_A_METHOD.invoke(dataWatcher, 2, name); // Custom Name
        DATA_WATCHER_A_METHOD.invoke(dataWatcher, 3, (byte) (ChatColor.stripColor(name).isEmpty() ? 0 : 1)); // Custom Name Visible
        sendPacket(player, PACKET_ENTITY_METADATA_CONSTRUCTOR.newInstance(entityId, dataWatcher, true));
    }


    public static void teleportFakeEntity(Player player, Location location, int entityId) {
        Validate.notNull(player);
        Validate.notNull(location);

        Object teleport = PACKET_ENTITY_TELEPORT_CONSTRUCTOR.newInstance();
        if (teleport == null) {
            return;
        }
        ReflectionUtil.setFieldValue(teleport, "a", entityId);
        ReflectionUtil.setFieldValue(teleport, "b", MATH_HELPER_FLOOR_METHOD.invokeStatic(location.getX() * 32.0D));
        ReflectionUtil.setFieldValue(teleport, "c", MATH_HELPER_FLOOR_METHOD.invokeStatic(location.getY() * 32.0D));
        ReflectionUtil.setFieldValue(teleport, "d", MATH_HELPER_FLOOR_METHOD.invokeStatic(location.getZ() * 32.0D));
        ReflectionUtil.setFieldValue(teleport, "e", (byte) ((int) (location.getYaw() * 256.0F / 360.0F)));
        ReflectionUtil.setFieldValue(teleport, "f", (byte) ((int) (location.getPitch() * 256.0F / 360.0F)));
        ReflectionUtil.setFieldValue(teleport, "g", false);
        sendPacket(player, teleport);
    }


    public static void helmetFakeEntity(Player player, ItemStack itemStack, int entityId) {
        Validate.notNull(player);
        Validate.notNull(itemStack);
        Object nmsItemStack = CRAFT_ITEM_NMS_COPY_METHOD.invokeStatic(itemStack);
        if (nmsItemStack == null) {
            return;
        }
        Object packet = PACKET_ENTITY_EQUIPMENT_CONSTRUCTOR.newInstance(entityId, 4, nmsItemStack);
        if (packet == null) {
            return;
        }
        sendPacket(player, packet);
    }


    public static void attachFakeEntity(Player player, int vehicleId, int entityId) {
        Validate.notNull(player);
        Object packet = PACKET_ATTACH_ENTITY_CONSTRUCTOR.newInstance();
        if (packet == null) {
            return;
        }
        ReflectionUtil.setFieldValue(packet, "a", 0);
        ReflectionUtil.setFieldValue(packet, "b", entityId);
        ReflectionUtil.setFieldValue(packet, "c", vehicleId);
        sendPacket(player, packet);
    }

    @SuppressWarnings("RedundantCast")

    public static void hideFakeEntities(Player player, int... entityIds) {
        Validate.notNull(player);
        sendPacket(player, PACKET_ENTITY_DESTROY_CONSTRUCTOR.newInstance((Object) entityIds));
    }

    public static void showFakeEntity(Player player, Location location, int entityTypeId, int entityId) {
        Validate.notNull(player);
        Validate.notNull(location);

        Object spawn = PACKET_SPAWN_ENTITY_CONSTRUCTOR.newInstance();
        if (spawn == null) {
            return;
        }
        ReflectionUtil.setFieldValue(spawn, "a", entityId);
        ReflectionUtil.setFieldValue(spawn, "b", MATH_HELPER_FLOOR_METHOD.invokeStatic(location.getX() * 32.0D));
        ReflectionUtil.setFieldValue(spawn, "c", MATH_HELPER_FLOOR_METHOD.invokeStatic(location.getY() * 32.0D));
        ReflectionUtil.setFieldValue(spawn, "d", MATH_HELPER_FLOOR_METHOD.invokeStatic(location.getZ() * 32.0D));
        ReflectionUtil.setFieldValue(spawn, "h", MATH_HELPER_D_METHOD.invokeStatic(location.getPitch() * 256.0F / 360.0F));
        ReflectionUtil.setFieldValue(spawn, "i", MATH_HELPER_D_METHOD.invokeStatic(location.getYaw() * 256.0F / 360.0F));
        ReflectionUtil.setFieldValue(spawn, "j", entityTypeId);
        sendPacket(player, spawn);
    }

    private static void showFakeEntityLiving(Player player, Location location, int entityTypeId, int entityId, Object dataWatcher) {
        Validate.notNull(player);
        Validate.notNull(location);
        if (dataWatcher == null || !DATA_WATCHER_CLASS.isAssignableFrom(dataWatcher.getClass())) {
            return;
        }

        Object spawn = PACKET_SPAWN_ENTITY_LIVING_CONSTRUCTOR.newInstance();
        if (spawn == null) {
            return;
        }
        ReflectionUtil.setFieldValue(spawn, "a", entityId);
        ReflectionUtil.setFieldValue(spawn, "b", entityTypeId);
        ReflectionUtil.setFieldValue(spawn, "c", MATH_HELPER_FLOOR_METHOD.invokeStatic(location.getX() * 32.0D));
        ReflectionUtil.setFieldValue(spawn, "d", MATH_HELPER_FLOOR_METHOD.invokeStatic(location.getY() * 32.0D));
        ReflectionUtil.setFieldValue(spawn, "e", MATH_HELPER_FLOOR_METHOD.invokeStatic(location.getZ() * 32.0D));
        ReflectionUtil.setFieldValue(spawn, "i", (byte) ((int) (location.getYaw() * 256.0F / 360.0F)));
        ReflectionUtil.setFieldValue(spawn, "j", (byte) ((int) (location.getPitch() * 256.0F / 360.0F)));
        ReflectionUtil.setFieldValue(spawn, "k", (byte) ((int) (location.getYaw() * 256.0F / 360.0F)));
        ReflectionUtil.setFieldValue(spawn, "l", dataWatcher);
        sendPacket(player, spawn);
    }

    public static Object getPlayerConnection(Player player) {
        Object entityPlayer = CRAFT_PLAYER_GET_HANDLE_METHOD.invoke(player);
        return ENTITY_PLAYER_CONNECTION_FIELD.getValue(entityPlayer);
    }

    public static void sendPacket(Player player, Object packet) {
        if (packet == null || !PACKET_CLASS.isAssignableFrom(packet.getClass())) {
            return;
        }
        Object playerConnection = getPlayerConnection(player);
        PLAYER_CONNECTION_SEND_PACKET_METHOD.invoke(playerConnection, packet);
    }

    public static ChannelPipeline getPipeline(Player player) {
        Object playerConnection = getPlayerConnection(player);
        Object networkManager = PLAYER_CONNECTION_NETWORK_MANAGER_FIELD.getValue(playerConnection);
        Channel channel = (Channel) NETWORK_MANAGER_CHANNEL_FIELD.getValue(networkManager);
        return channel.pipeline();
    }

    public static int getEntityTypeId(EntityType type) {
        if (type == null) {
            return -1;
        }
        String name = type.name();
        if (mapEntityTypes.containsKey(name)) {
            return mapEntityTypes.get(name).getKey();
        }
        return -1;
    }

    public static float getEntityHeight(EntityType type) {
        if (type == null) {
            return 0.0f;
        }
        String name = type.name();
        if (mapEntityTypes.containsKey(name)) {
            return mapEntityTypes.get(name).getValue();
        }
        return 0.0f;
    }
}
