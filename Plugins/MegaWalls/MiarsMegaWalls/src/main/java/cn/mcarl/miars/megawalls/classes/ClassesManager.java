package cn.mcarl.miars.megawalls.classes;

import cn.mcarl.miars.megawalls.MiarsMegaWalls;
import cn.mcarl.miars.megawalls.classes.novice.him.HIM;
import cn.mcarl.miars.megawalls.classes.stater.cow.Cow;
import cn.mcarl.miars.megawalls.classes.stater.hunter.Hunter;
import cn.mcarl.miars.megawalls.classes.stater.shark.Shark;
import cn.mcarl.miars.megawalls.game.entitiy.GamePlayer;
import cn.mcarl.miars.megawalls.stats.ClassesStats;
import cn.mcarl.miars.megawalls.utils.ItemUtils;
import cn.mcarl.miars.megawalls.utils.ParticleEffect;
import com.google.gson.JsonElement;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class ClassesManager {
   private static final List<Classes> classesList = new ArrayList<>();
   private static boolean registered = false;

   public static Classes getClassesByName(String ClassesName) {

      for (Classes classes : classesList) {
         if (classes.getName().equalsIgnoreCase(ClassesName)) {
            return classes;
         }
      }

      return null;
   }

   public static void registerClasses(Classes classes) {
      classesList.add(classes);
   }

   public static boolean registerAll() {
      if (registered) {
         return false;
      } else {
         registerClasses(new Cow());
         registerClasses(new Hunter());
//         registerClasses(new Shark());

         registerClasses(new HIM());
//         registerClasses(new Zombie());
//         registerClasses(new Enderman());
//         registerClasses(new Skeleton());
//         registerClasses(new Shaman());
//         registerClasses(new Puppet());
//         registerClasses(new Spider());
//         registerClasses(new Pigman());
//         registerClasses(new Squid());
//         registerClasses(new Dreadlord());
//         registerClasses(new Blaze());
//         registerClasses(new Arcane());
//         registerClasses(new Creeper());
//         registerClasses(new Pirate());
//         registerClasses(new Mole());
//         registerClasses(new Werewolf());
//         registerClasses(new Phoenix());
//         registerClasses(new Random());
         registered = true;
         return true;
      }
   }

   public static List<Classes> getNormalClasses() {
      List<Classes> list = new ArrayList<>();

      for (Classes classes : classesList) {
         if (classes.getClassesType() == ClassesType.NORMAL) {
            list.add(classes);
         }
      }

      return list;
   }

   public static List<Classes> getNoviceClasses() {
      List<Classes> list = new ArrayList<>();

      for (Classes classes : classesList) {
         if (classes.getClassesType() == ClassesType.NOVICE) {
            list.add(classes);
         }
      }

      return list;
   }
   public static List<Classes> getStaterClasses() {
      List<Classes> list = new ArrayList<>();

      for (Classes classes : classesList) {
         if (classes.getClassesType() == ClassesType.STATER) {
            list.add(classes);
         }
      }

      return list;
   }

   public static List<Classes> getMythicClasses() {
      List<Classes> list = new ArrayList<>();

      for (Classes classes : classesList) {
         if (classes.getClassesType() == ClassesType.MYTHIC) {
            list.add(classes);
         }
      }

      return list;
   }

   public static List<Classes> getClasses() {
      return classesList;
   }

   public static List<Classes> sort(List<Classes> list) {
      list.sort((classes1, classes2) -> classes1.getClassesType() == classes2.getClassesType() ? classes1.getDifficulty().getPriority() - classes2.getDifficulty().getPriority() : classes1.getClassesType().getPriority() - classes2.getClassesType().getPriority());
      return list;
   }

   public static Classes getSelected(GamePlayer gamePlayer) {
      return getClassesByName(gamePlayer.getPlayerStats().getClasses());
   }

   public static boolean isClassesItem(ItemStack itemStack) {
      if (itemStack.hasItemMeta() && itemStack.getItemMeta().getDisplayName() != null) {
         String displayname = itemStack.getItemMeta().getDisplayName();
         return displayname.contains("剑") || displayname.contains("弓") || displayname.contains("镐") || displayname.contains("斧") || displayname.contains("锹") || displayname.contains("牛排") || displayname.contains("末影箱") || displayname.contains("头盔") || displayname.contains("胸甲") || displayname.contains("护腿") || displayname.contains("靴子") || displayname.contains("药水") || displayname.contains("箭") || displayname.contains("指南针") || displayname.contains("面包") || displayname.contains("鲑鱼") || displayname.contains("钓竿") || displayname.contains("金苹果");
      } else {
         return false;
      }
   }

   public static void playSkillEffect(final Player player) {
      (new BukkitRunnable() {
         double height;
         int ticks;
         @Override
         public void run() {
            if (player.isOnline() && this.ticks < 8) {
               for(int degree = 0; degree < 360; ++degree) {
                  Location location = player.getLocation().clone();
                  double radians = Math.toRadians((double)degree);
                  double x = Math.cos(radians) + (double)this.ticks;
                  double z = Math.sin(radians) + (double)this.ticks;
                  ParticleEffect.FLAME.display(0.0F, 0.0F, 0.0F, 0.0F, 1, location.clone().add(x, 0.0D, z).add(0.0D, this.height, 0.0D), 30.0D);
               }

               ++this.height;
               ++this.ticks;
            } else {
               this.cancel();
            }

         }
      }).runTaskTimer(MiarsMegaWalls.getInstance(), 0L, 1L);
   }

   public static void giveItems(GamePlayer gamePlayer) {
      Player player = gamePlayer.getPlayer();
      ClassesStats classesStats = gamePlayer.getPlayerStats().getClassesStats(getSelected(gamePlayer));
      int level = classesStats.getEquipLevel();
      Iterator var4;
      ItemStack itemStack;
      if (classesStats.getInventory() == null) {
         var4 = getSelected(gamePlayer).getEquipmentPackage().getEquipments(level).iterator();

         while(var4.hasNext()) {
            itemStack = (ItemStack)var4.next();
            if (ItemUtils.isHelmet(itemStack)) {
               player.getInventory().setHelmet(itemStack);
            } else if (ItemUtils.isChestplate(itemStack)) {
               player.getInventory().setChestplate(itemStack);
            } else if (ItemUtils.isLeggings(itemStack)) {
               player.getInventory().setLeggings(itemStack);
            } else if (ItemUtils.isBoots(itemStack)) {
               player.getInventory().setBoots(itemStack);
            } else {
               player.getInventory().addItem(itemStack);
            }
         }
      } else {
         var4 = classesStats.getInventory().entrySet().iterator();

         while(var4.hasNext()) {
            Map.Entry entry = (Map.Entry)var4.next();
            int slot = Integer.parseInt((String)entry.getKey());
            player.getInventory().setItem(slot, ItemUtils.read(Base64.getDecoder().decode(((JsonElement) entry.getValue()).getAsString())));
         }

         var4 = getSelected(gamePlayer).getEquipmentPackage().getEquipments(level).iterator();

         while(var4.hasNext()) {
            itemStack = (ItemStack)var4.next();
            if (ItemUtils.isHelmet(itemStack)) {
               player.getInventory().setHelmet(itemStack);
            } else if (ItemUtils.isChestplate(itemStack)) {
               player.getInventory().setChestplate(itemStack);
            } else if (ItemUtils.isLeggings(itemStack)) {
               player.getInventory().setLeggings(itemStack);
            } else if (ItemUtils.isBoots(itemStack)) {
               player.getInventory().setBoots(itemStack);
            }
         }
      }

   }
}
