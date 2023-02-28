package cn.mcarl.miars.megawalls.utils;

import cn.mcarl.miars.megawalls.game.entitiy.mobs.TeamWither;
import net.minecraft.server.v1_8_R3.Entity;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import java.lang.reflect.Field;
import java.util.Map;

public enum EntityTypes {
   Wither("Wither", 64, TeamWither.class);
//   Wolf("Wolf", 95, ShamanWolf.class),
//   Blaze("Blaze", 61, CustomBlaze.class),
//   Creeper("Creeper", 50, CustomCreeper.class),
//   Fireball("Fireball", 12, CustomCannonball.class),
//   Bat("Bat", 65, CustomBat.class),
//   Pig("Pig", 90, CustomPig.class),
//   BoomSheep("BoomSheep", 91, BoomSheep.class),
//   Spider("Spider", 52, CustomSpider.class);

   private EntityTypes(String name, int id, Class<? extends Entity> custom) {
      addToMaps(custom, name, id);
   }

   public static void spawnEntity(Entity entity, Location loc) {
      entity.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
      ((CraftWorld)loc.getWorld()).getHandle().addEntity(entity, SpawnReason.CUSTOM);
   }

   private static Object getPrivateField(String fieldName, Class clazz, Object object) {
      Object o = null;

      try {
         Field field = clazz.getDeclaredField(fieldName);
         field.setAccessible(true);
         o = field.get(object);
      } catch (NoSuchFieldException var5) {
         var5.printStackTrace();
      } catch (IllegalAccessException var6) {
         var6.printStackTrace();
      }

      return o;
   }

   private static void addToMaps(Class clazz, String name, int id) {
      ((Map)getPrivateField("c", net.minecraft.server.v1_8_R3.EntityTypes.class, (Object)null)).put(name, clazz);
      ((Map)getPrivateField("d", net.minecraft.server.v1_8_R3.EntityTypes.class, (Object)null)).put(clazz, name);
      ((Map)getPrivateField("f", net.minecraft.server.v1_8_R3.EntityTypes.class, (Object)null)).put(clazz, id);
   }
}
