package cn.mcarl.miars.megawalls.utils;

import cn.mcarl.miars.megawalls.MiarsMegaWalls;
import cn.mcarl.miars.megawalls.game.manager.GameManager;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.citizensnpcs.api.CitizensAPI;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import net.skinsrestorer.api.PlayerWrapper;
import net.skinsrestorer.api.SkinsRestorerAPI;
import net.skinsrestorer.api.exception.SkinRequestException;
import net.skinsrestorer.api.property.GenericProperty;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class PlayerUtils {
   public static List<Player> getNearbyPlayers(Entity entity, double radius) {
      List<Player> players = new ArrayList<>();

      for (Entity e : entity.getNearbyEntities(radius, radius, radius)) {
         if (!CitizensAPI.getNPCRegistry().isNPC(e) && e instanceof Player) {
            players.add((Player) e);
         }
      }

      return players;
   }

   public static List<Player> getNearbyPlayers(Location location, double radius) {
      List<Player> players = new ArrayList<>();

      for (Entity e : location.getWorld().getNearbyEntities(location, radius, radius, radius)) {
         if (!CitizensAPI.getNPCRegistry().isNPC(e) && e instanceof Player && e.getLocation().distance(location) <= radius) {
            players.add((Player) e);
         }
      }

      return players;
   }

   public static void skinChange(Player player,String value, String signature) {
      SkinsRestorerAPI api = MiarsMegaWalls.getInstance().getSkinsRestorerAPI();
      api.applySkin(new PlayerWrapper(player),api.createPlatformProperty("textures", value, signature));
   }

   private static void sendPacket(Packet<?> packet) {
      Iterator var1 = Bukkit.getOnlinePlayers().iterator();

      while(var1.hasNext()) {
         Player pls = (Player)var1.next();
         ((CraftPlayer)pls).getHandle().playerConnection.sendPacket(packet);
      }

   }

   public static void heal(Player player, double heal) {
      player.setHealth(Math.min(player.getHealth() + heal, player.getMaxHealth()));
   }

   public static void food(Player player, int level) {
      player.setFoodLevel(Math.min(player.getFoodLevel() + level, 20));
   }
}
