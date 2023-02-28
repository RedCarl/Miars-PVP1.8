package cn.mcarl.miars.megawalls.utils;

import net.citizensnpcs.api.CitizensAPI;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PlayerUtils {
   public static List<Player> getNearbyPlayers(Entity entity, double radius) {
      List<Player> players = new ArrayList();
      Iterator var4 = entity.getNearbyEntities(radius, radius, radius).iterator();

      while(var4.hasNext()) {
         Entity e = (Entity)var4.next();
         if (!CitizensAPI.getNPCRegistry().isNPC(e) && e instanceof Player) {
            players.add((Player)e);
         }
      }

      return players;
   }

   public static List<Player> getNearbyPlayers(Location location, double radius) {
      List<Player> players = new ArrayList();
      Iterator var4 = location.getWorld().getNearbyEntities(location, radius, radius, radius).iterator();

      while(var4.hasNext()) {
         Entity e = (Entity)var4.next();
         if (!CitizensAPI.getNPCRegistry().isNPC(e) && e instanceof Player && e.getLocation().distance(location) <= radius) {
            players.add((Player)e);
         }
      }

      return players;
   }
}
