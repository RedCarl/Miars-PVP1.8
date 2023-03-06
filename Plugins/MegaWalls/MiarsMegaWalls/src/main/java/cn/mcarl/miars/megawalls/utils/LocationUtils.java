package cn.mcarl.miars.megawalls.utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class LocationUtils {
   public static Location getLocation(Location location, double toX, double toY, double toZ) {
      return new Location(location.getWorld(), location.getX() + toX, location.getY() + toY, location.getZ() + toZ);
   }

   public static Location getFixedLocation(Location location, BlockFace blockFace) {
      for(int i = 0; i < location.getWorld().getMaxHeight() && location.getBlock().isEmpty(); ++i) {
         location.add(0.0D, blockFace == BlockFace.UP ? 1.0D : -1.0D, 0.0D);
      }

      return location.add(0.0D, blockFace == BlockFace.UP ? -1.0D : 1.0D, 0.0D);
   }

   public static List<Location> getCircle(Location location, double radius, int points) {
      List<Location> locations = new ArrayList();
      double increment = 6.283185307179586D / (double)points;

      for(int i = 0; i < points; ++i) {
         double angle = (double)i * increment;
         double x = location.getX() + Math.cos(angle) * radius;
         double z = location.getZ() + Math.sin(angle) * radius;
         locations.add(new Location(location.getWorld(), x, location.getY(), z));
      }

      return locations;
   }

   public static List<Block> getSphere(Location location, int radius) {
      List<Block> blocks = new ArrayList();
      int X = location.getBlockX();
      int Y = location.getBlockY();
      int Z = location.getBlockZ();
      int radiusSquared = radius * radius;

      for(int x = X - radius; x <= X + radius; ++x) {
         for(int y = Y - radius; y <= Y + radius; ++y) {
            for(int z = Z - radius; z <= Z + radius; ++z) {
               if ((X - x) * (X - x) + (Z - z) * (Z - z) <= radiusSquared) {
                  blocks.add(location.getWorld().getBlockAt(x, y, z));
               }
            }
         }
      }

      return blocks;
   }

   public static List<Block> getCube(Location location, int radius) {
      List<Block> blocks = new ArrayList();
      int X = location.getBlockX() - radius / 2;
      int Y = location.getBlockY() - radius / 2;
      int Z = location.getBlockZ() - radius / 2;

      for(int x = X; x < X + radius; ++x) {
         for(int y = Y; y < Y + radius; ++y) {
            for(int z = Z; z < Z + radius; ++z) {
               blocks.add(location.getWorld().getBlockAt(x, y, z));
            }
         }
      }

      return blocks;
   }

   public static Vector getBackVector(Location location) {
      float f1 = (float)(location.getZ() + 1.0D * Math.sin(Math.toRadians((double)(location.getYaw() + 90.0F))));
      float f2 = (float)(location.getX() + 1.0D * Math.cos(Math.toRadians((double)(location.getYaw() + 90.0F))));
      return new Vector((double)f2 - location.getX(), 0.0D, (double)f1 - location.getZ());
   }

   public static boolean isSafeSpot(Location location) {
      Block blockCenter = location.getWorld().getBlockAt(location.getBlockX(), location.getBlockY(), location.getBlockZ());
      Block blockAbove = location.getWorld().getBlockAt(location.getBlockX(), location.getBlockY() + 1, location.getBlockZ());
      Block blockBelow = location.getWorld().getBlockAt(location.getBlockX(), location.getBlockY() - 1, location.getBlockZ());
      if (!blockCenter.getType().isTransparent() && (!blockCenter.isLiquid() || blockCenter.getType().equals(Material.LAVA) || blockCenter.getType().equals(Material.STATIONARY_LAVA)) || !blockAbove.getType().isTransparent() && (!blockAbove.isLiquid() || blockAbove.getType().equals(Material.LAVA) || blockCenter.getType().equals(Material.STATIONARY_LAVA))) {
         return false;
      } else {
         return blockBelow.getType().isSolid() || blockBelow.getType().equals(Material.WATER) || blockBelow.getType().equals(Material.STATIONARY_WATER);
      }
   }
}
