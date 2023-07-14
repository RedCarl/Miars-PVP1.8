// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.checks.combat.aurabot;

import java.util.HashMap;
import net.minecraft.server.v1_8_R3.Item;
import net.minecraft.server.v1_8_R3.Items;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import gg.noob.plunder.Plunder;
import gg.noob.plunder.util.Pair;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import java.util.Iterator;
import net.minecraft.server.v1_8_R3.WorldServer;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import org.bukkit.Location;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutAnimation;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityHeadRotation;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityTeleport;
import gg.noob.plunder.util.PlayerUtils;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityEquipment;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityEffect;
import net.minecraft.server.v1_8_R3.MobEffect;
import org.bukkit.potion.PotionEffectType;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import net.minecraft.server.v1_8_R3.MathHelper;
import java.util.Random;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.PlayerInteractManager;
import com.mojang.authlib.GameProfile;
import me.blade.gg.core.util.CaptchaUtils;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import java.util.List;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.ArrayList;
import gg.noob.lib.util.Callback;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.entity.Player;
import java.util.UUID;
import java.util.Map;
import gg.noob.plunder.checks.Check;

public class AuraBotA extends Check
{
    private static final Map<UUID, Integer> runningAuraBotPlayers;
    private static final Map<UUID, Integer> lastAttackedBotCount;
    
    public AuraBotA() {
        super("AuraBot (A)");
    }
    
    @Override
    public void handleReceivedPacket(final Player player, final Packet packet) {
        if (packet instanceof PacketPlayInUseEntity) {
            final PacketPlayInUseEntity packetPlayInUseEntity = (PacketPlayInUseEntity)packet;
            if (packetPlayInUseEntity.a() != PacketPlayInUseEntity.EnumEntityUseAction.ATTACK) {
                return;
            }
            final Entity entity = packetPlayInUseEntity.a(((CraftWorld)player.getWorld()).getHandle());
            if (!AuraBotA.runningAuraBotPlayers.containsKey(player.getUniqueId())) {
                return;
            }
            if (AuraBotA.runningAuraBotPlayers.get(player.getUniqueId()) != entity.getId()) {
                return;
            }
            AuraBotA.lastAttackedBotCount.put(player.getUniqueId(), AuraBotA.lastAttackedBotCount.getOrDefault(player.getUniqueId(), 0) + 1);
        }
    }
    
    public static void sendBot(final Player checker, final Player executor, final Callback<Integer> callback) {
        if (AuraBotA.runningAuraBotPlayers.containsKey(checker.getUniqueId())) {
            return;
        }
        final List<Player> viewers = new ArrayList<Player>();
        viewers.add(checker);
        if (!checker.getUniqueId().equals(executor.getUniqueId())) {
            viewers.add(executor);
        }
        AuraBotA.lastAttackedBotCount.remove(checker.getUniqueId());
        new BukkitRunnable() {
            @Override
            public void run() {
                final int rotateCount = 16;
                final Location location = checker.getLocation().clone();
                final MinecraftServer server = ((CraftServer)Bukkit.getServer()).getServer();
                final WorldServer worldServer = ((CraftWorld)location.getWorld()).getHandle();
                final EntityPlayer npc = new EntityPlayer(server, worldServer, new GameProfile(UUID.randomUUID(), CaptchaUtils.summon()), new PlayerInteractManager(worldServer));
                final int id = npc.getId();
                AuraBotA.runningAuraBotPlayers.put(checker.getUniqueId(), id);
                npc.ping = new Random().nextInt(200) + 10;
                npc.getBukkitEntity().setHealth(new Random().nextInt(16) + 2);
                npc.getBukkitEntity().setItemInHand(AuraBotA.getRandomSword());
                double horizontalAdd = 3.0;
                double verticalAdd = 2.0;
                for (int i = 0; i < 360 * rotateCount; i += 18) {
                    if (!checker.isOnline()) {
                        AuraBotA.runningAuraBotPlayers.remove(checker.getUniqueId());
                        break;
                    }
                    location.setX(checker.getLocation().getX() + -MathHelper.sin(i * 3.1415927f / 180.0f) * horizontalAdd);
                    location.setY(checker.getLocation().getY() + verticalAdd);
                    location.setZ(checker.getLocation().getZ() + MathHelper.cos(i * 3.1415927f / 180.0f) * horizontalAdd);
                    for (final Player player : viewers) {
                        if (player == null) {
                            continue;
                        }
                        final PlayerConnection connection = ((CraftPlayer)player).getHandle().playerConnection;
                        if (i == 0) {
                            npc.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
                            connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, new EntityPlayer[] { npc }));
                            connection.sendPacket(new PacketPlayOutNamedEntitySpawn(npc));
                            npc.getDataWatcher().watch(6, (Object)(float)npc.getBukkitEntity().getHealth());
                            connection.sendPacket(new PacketPlayOutEntityMetadata(id, npc.getDataWatcher(), false));
                            connection.sendPacket(new PacketPlayOutEntityEffect(npc.getId(), new MobEffect(PotionEffectType.SPEED.getId(), 80)));
                            connection.sendPacket(new PacketPlayOutEntityEquipment(id, 4, AuraBotA.getRandomHelmet()));
                            connection.sendPacket(new PacketPlayOutEntityEquipment(id, 3, AuraBotA.getRandomChestPlate()));
                            connection.sendPacket(new PacketPlayOutEntityEquipment(id, 2, AuraBotA.getRandomLeggings()));
                            connection.sendPacket(new PacketPlayOutEntityEquipment(id, 1, AuraBotA.getRandomBoots()));
                        }
                        else {
                            final Pair<Float, Float> lookAt = PlayerUtils.lookAt(checker.getEyeLocation(), location);
                            final float yaw = lookAt.getX();
                            final float pitch = lookAt.getY();
                            connection.sendPacket(new PacketPlayOutEntityTeleport(npc.getId(), MathHelper.floor(location.getX() * 32.0), MathHelper.floor(location.getY() * 32.0), MathHelper.floor(location.getZ() * 32.0), (byte)(PlayerUtils.compressedAngle(yaw) * 256.0f / 360.0f), (byte)(pitch * 256.0f / 360.0f), i % 360 != 0));
                            connection.sendPacket(new PacketPlayOutEntityHeadRotation(npc, (byte)PlayerUtils.compressedAngle(yaw)));
                            if (i % 360 == 0) {
                                connection.sendPacket(new PacketPlayOutAnimation(npc, 1));
                            }
                            if (i % 36 != 0) {
                                continue;
                            }
                            connection.sendPacket(new PacketPlayOutAnimation(npc, 0));
                        }
                    }
                    try {
                        Thread.sleep(50L);
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    horizontalAdd -= 3.0E-4;
                    verticalAdd += 1.0E-4;
                }
                for (final Player player2 : viewers) {
                    if (player2 == null) {
                        continue;
                    }
                    final PlayerConnection connection2 = ((CraftPlayer)player2).getHandle().playerConnection;
                    connection2.sendPacket(new PacketPlayOutEntityDestroy(id));
                    connection2.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, npc));
                }
                AuraBotA.runningAuraBotPlayers.remove(checker.getUniqueId());
                final int count = AuraBotA.lastAttackedBotCount.getOrDefault(checker.getUniqueId(), 0);
                callback.callback(count);
            }
        }.runTaskAsynchronously(Plunder.getInstance());
    }
    
    public static ItemStack getRandomSword() {
        final int random = new Random().nextInt(5);
        Material type = null;
        switch (random) {
            case 0: {
                type = Material.WOOD_SWORD;
                break;
            }
            case 1: {
                type = Material.GOLD_SWORD;
                break;
            }
            case 2: {
                type = Material.STONE_SWORD;
                break;
            }
            case 3: {
                type = Material.IRON_SWORD;
                break;
            }
            case 4: {
                type = Material.DIAMOND_SWORD;
                break;
            }
            default: {
                type = null;
                break;
            }
        }
        final ItemStack itemStack = new ItemStack(type);
        itemStack.addEnchantment(Enchantment.DURABILITY, 1);
        return itemStack;
    }
    
    public static net.minecraft.server.v1_8_R3.ItemStack getRandomHelmet() {
        final int random = new Random().nextInt(5);
        Item item = null;
        switch (random) {
            case 0: {
                item = Items.LEATHER_HELMET;
                break;
            }
            case 1: {
                item = Items.CHAINMAIL_HELMET;
                break;
            }
            case 2: {
                item = Items.GOLDEN_HELMET;
                break;
            }
            case 3: {
                item = Items.IRON_HELMET;
                break;
            }
            case 4: {
                item = Items.DIAMOND_HELMET;
                break;
            }
            default: {
                item = null;
                break;
            }
        }
        final net.minecraft.server.v1_8_R3.ItemStack itemStack = new net.minecraft.server.v1_8_R3.ItemStack(item);
        itemStack.addEnchantment(net.minecraft.server.v1_8_R3.Enchantment.DURABILITY, 1);
        return itemStack;
    }
    
    public static net.minecraft.server.v1_8_R3.ItemStack getRandomChestPlate() {
        final int random = new Random().nextInt(5);
        Item item = null;
        switch (random) {
            case 0: {
                item = Items.LEATHER_CHESTPLATE;
                break;
            }
            case 1: {
                item = Items.CHAINMAIL_CHESTPLATE;
                break;
            }
            case 2: {
                item = Items.GOLDEN_CHESTPLATE;
                break;
            }
            case 3: {
                item = Items.IRON_CHESTPLATE;
                break;
            }
            case 4: {
                item = Items.DIAMOND_CHESTPLATE;
                break;
            }
            default: {
                item = null;
                break;
            }
        }
        final net.minecraft.server.v1_8_R3.ItemStack itemStack = new net.minecraft.server.v1_8_R3.ItemStack(item);
        itemStack.addEnchantment(net.minecraft.server.v1_8_R3.Enchantment.DURABILITY, 1);
        return itemStack;
    }
    
    public static net.minecraft.server.v1_8_R3.ItemStack getRandomLeggings() {
        final int random = new Random().nextInt(5);
        Item item = null;
        switch (random) {
            case 0: {
                item = Items.LEATHER_LEGGINGS;
                break;
            }
            case 1: {
                item = Items.CHAINMAIL_LEGGINGS;
                break;
            }
            case 2: {
                item = Items.GOLDEN_LEGGINGS;
                break;
            }
            case 3: {
                item = Items.IRON_LEGGINGS;
                break;
            }
            case 4: {
                item = Items.DIAMOND_LEGGINGS;
                break;
            }
            default: {
                item = null;
                break;
            }
        }
        final net.minecraft.server.v1_8_R3.ItemStack itemStack = new net.minecraft.server.v1_8_R3.ItemStack(item);
        itemStack.addEnchantment(net.minecraft.server.v1_8_R3.Enchantment.DURABILITY, 1);
        return itemStack;
    }
    
    public static net.minecraft.server.v1_8_R3.ItemStack getRandomBoots() {
        final int random = new Random().nextInt(5);
        Item item = null;
        switch (random) {
            case 0: {
                item = Items.LEATHER_BOOTS;
                break;
            }
            case 1: {
                item = Items.CHAINMAIL_BOOTS;
                break;
            }
            case 2: {
                item = Items.GOLDEN_BOOTS;
                break;
            }
            case 3: {
                item = Items.IRON_LEGGINGS;
                break;
            }
            case 4: {
                item = Items.DIAMOND_BOOTS;
                break;
            }
            default: {
                item = null;
                break;
            }
        }
        final net.minecraft.server.v1_8_R3.ItemStack itemStack = new net.minecraft.server.v1_8_R3.ItemStack(item);
        itemStack.addEnchantment(net.minecraft.server.v1_8_R3.Enchantment.DURABILITY, 1);
        return itemStack;
    }
    
    static {
        runningAuraBotPlayers = new HashMap<UUID, Integer>();
        lastAttackedBotCount = new HashMap<UUID, Integer>();
    }
}
