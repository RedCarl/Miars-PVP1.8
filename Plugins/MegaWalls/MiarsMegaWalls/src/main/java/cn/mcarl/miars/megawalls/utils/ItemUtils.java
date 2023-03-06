package cn.mcarl.miars.megawalls.utils;

import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class ItemUtils {
   public static byte[] write(ItemStack itemStack) {
      try {
         ByteArrayOutputStream out = new ByteArrayOutputStream();
         BukkitObjectOutputStream bukkitOut = new BukkitObjectOutputStream(out);
         bukkitOut.writeObject(itemStack);
         bukkitOut.close();
         return out.toByteArray();
      } catch (Exception var3) {
         var3.printStackTrace();
         return new byte[0];
      }
   }

   public static ItemStack read(byte[] bytes) {
      try {
         BukkitObjectInputStream bukkitIn = new BukkitObjectInputStream(new ByteArrayInputStream(bytes));
         return (ItemStack)bukkitIn.readObject();
      } catch (Exception var2) {
         var2.printStackTrace();
         return null;
      }
   }

   public static boolean isSword(ItemStack is) {
      switch(is.getType()) {
      case WOOD_SWORD:
      case IRON_SWORD:
      case GOLD_SWORD:
      case DIAMOND_SWORD:
         return true;
      default:
         return false;
      }
   }

   public static boolean isHelmet(ItemStack is) {
      switch(is.getType()) {
      case LEATHER_HELMET:
      case CHAINMAIL_HELMET:
      case IRON_HELMET:
      case GOLD_HELMET:
      case DIAMOND_HELMET:
         return true;
      default:
         return false;
      }
   }

   public static boolean isChestplate(ItemStack is) {
      switch(is.getType()) {
      case LEATHER_CHESTPLATE:
      case CHAINMAIL_CHESTPLATE:
      case IRON_CHESTPLATE:
      case GOLD_CHESTPLATE:
      case DIAMOND_CHESTPLATE:
         return true;
      default:
         return false;
      }
   }

   public static boolean isLeggings(ItemStack is) {
      switch(is.getType()) {
      case LEATHER_LEGGINGS:
      case CHAINMAIL_LEGGINGS:
      case IRON_LEGGINGS:
      case GOLD_LEGGINGS:
      case DIAMOND_LEGGINGS:
         return true;
      default:
         return false;
      }
   }

   public static boolean isBoots(ItemStack is) {
      switch(is.getType()) {
      case LEATHER_BOOTS:
      case CHAINMAIL_BOOTS:
      case IRON_BOOTS:
      case GOLD_BOOTS:
      case DIAMOND_BOOTS:
         return true;
      default:
         return false;
      }
   }
}
