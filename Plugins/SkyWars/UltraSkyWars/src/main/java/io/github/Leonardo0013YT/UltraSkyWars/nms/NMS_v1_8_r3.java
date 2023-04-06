package io.github.Leonardo0013YT.UltraSkyWars.nms;

import io.github.Leonardo0013YT.UltraSkyWars.interfaces.NMS;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collection;

public class NMS_v1_8_r3 implements NMS {

    @Override
    public Horse spawnHorse(Location loc, Player p) {
        Horse horse = loc.getWorld().spawn(loc, Horse.class);
        horse.setVariant(Horse.Variant.SKELETON_HORSE);
        horse.setOwner(p);
        horse.getInventory().setSaddle(new ItemStack(Material.SADDLE));
        horse.setDomestication(horse.getMaxDomestication());
        horse.setAdult();
        return horse;
    }

    @Override
    public Collection<Integer> spawn(Player p, Location loc, ItemStack head) {
        EntityGiantZombie ev = new EntityGiantZombie(((CraftWorld) loc.getWorld()).getHandle());
        EntityArmorStand ar = new EntityArmorStand(((CraftWorld) loc.getWorld()).getHandle());
        EntityBat bat = new EntityBat(((CraftWorld) loc.getWorld()).getHandle());
        Location l = loc.clone().add(-2, 9, 3.5);
        ar.setLocation(loc.getX(), loc.getY(), loc.getZ(), 0, 0);
        ev.setLocation(loc.getX(), loc.getY(), loc.getZ(), 0, 0);
        bat.setLocation(l.getX(), l.getY(), l.getZ(), 0, 0);
        ar.setInvisible(true);
        ev.setInvisible(true);
        bat.setInvisible(true);
        PacketPlayOutEntityEquipment equipment = new PacketPlayOutEntityEquipment(ev.getId(), 0, CraftItemStack.asNMSCopy(head));
        PacketPlayOutSpawnEntityLiving ent = new PacketPlayOutSpawnEntityLiving(ev);
        PacketPlayOutSpawnEntityLiving ent2 = new PacketPlayOutSpawnEntityLiving(ar);
        PacketPlayOutSpawnEntityLiving ent3 = new PacketPlayOutSpawnEntityLiving(bat);
        PacketPlayOutAttachEntity attach = new PacketPlayOutAttachEntity(1, bat, ar);
        PlayerConnection pc = ((CraftPlayer) p).getHandle().playerConnection;
        pc.sendPacket(ent);
        pc.sendPacket(ent2);
        pc.sendPacket(ent3);
        pc.sendPacket(attach);
        pc.sendPacket(equipment);
        return Arrays.asList(ev.getId(), ar.getId(), bat.getId());
    }

    @Override
    public void destroy(Player p, Collection<Integer> id) {
        for (int i : id) {
            PacketPlayOutEntityDestroy ent = new PacketPlayOutEntityDestroy(i);
            ((CraftPlayer) p).getHandle().playerConnection.sendPacket(ent);
        }
    }

    @Override
    public void followPlayer(Player player, LivingEntity entity, double d) {
        float f = (float) d;
        ((EntityInsentient) ((CraftEntity) entity).getHandle()).getNavigation().a(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), f);
    }

    @Override
    public void displayParticle(Player player, Location location, float offsetX, float offsetY, float offsetZ, int speed, String enumParticle, int amount) {
        PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.valueOf(enumParticle), false, (float) location.getX(), (float) location.getY(), (float) location.getZ(), offsetX, offsetY, offsetZ, speed, amount);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }

    @Override
    public void broadcastParticle(Location location, float offsetX, float offsetY, float offsetZ, int speed, String enumParticle, int amount, double range) {
        PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.valueOf(enumParticle), false, (float) location.getX(), (float) location.getY(), (float) location.getZ(), offsetX, offsetY, offsetZ, speed, amount);
        if (location.getWorld() == null) return;
        Collection<Entity> ents;
        try {
            ents = location.getWorld().getNearbyEntities(location, range, range, range);
        } catch (Exception ignored) {
            return;
        }
        if (ents.isEmpty()) return;
        ents.removeIf(e -> !e.getType().equals(EntityType.PLAYER));
        for (Entity p : ents) {
            ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
        }
    }

    @Override
    public boolean isParticle(String particle) {
        try {
            EnumParticle.valueOf(particle);
        } catch (EnumConstantNotPresentException | IllegalArgumentException e) {
            return false;
        }
        return true;
    }

}