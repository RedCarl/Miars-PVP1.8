//package cn.mcarl.miars.megawalls.classes.mythic.mole;
//
//import cn.wolfmc.megawalls.MegaWalls;
//import cn.wolfmc.megawalls.classes.Classes;
//import cn.wolfmc.megawalls.classes.Skill;
//import cn.wolfmc.megawalls.game.GamePlayer;
//import cn.wolfmc.megawalls.stats.KitStatsContainer;
//import cn.wolfmc.megawalls.util.ItemBuilder;
//import cn.wolfmc.megawalls.util.StringUtils;
//import org.bukkit.Material;
//import org.bukkit.entity.Player;
//import org.bukkit.inventory.ItemStack;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Random;
//
//public class ThirdSkill extends Skill {
//   public ThirdSkill(Classes classes) {
//      super("垃圾食品", classes);
//   }
//
//   public int maxedLevel() {
//      return 3;
//   }
//
//   public double getAttribute(int level) {
//      switch(level) {
//         case 1:
//            return 0.07D;
//         case 2:
//            return 0.14D;
//         case 3:
//            return 0.21D;
//         default:
//            return 0.07D;
//      }
//   }
//
//   public List<String> getInfo(int level) {
//      List<String> lore = new ArrayList();
//      if (level == 1) {
//         lore.add(" &8▪ &7用锹挖掘时,有&a" + StringUtils.percent(this.getAttribute(level)) + "&7几率掉落");
//         lore.add("   &7垃圾食品。你有20%几率回");
//         lore.add("   &7收吃掉的金苹果。");
//         return lore;
//      } else {
//         lore.add(" &8▪ &7用锹挖掘时,有&8" + StringUtils.percent(this.getAttribute(level - 1)) + " ➜ &a" + StringUtils.percent(this.getAttribute(level)) + "&7几率掉落");
//         lore.add("   &7垃圾食品。你有20%几率回");
//         lore.add("   &7收吃掉的金苹果。");
//         return lore;
//      }
//   }
//
//   public void upgrade(GamePlayer gamePlayer) {
//      KitStatsContainer kitStats = gamePlayer.getPlayerStats().getKitStats(this.getClasses());
//      kitStats.addSkill3Level();
//   }
//
//   public int getPlayerLevel(GamePlayer gamePlayer) {
//      return gamePlayer.getPlayerStats().getKitStats(this.getClasses()).getSkill3Level();
//   }
//
//   public boolean use(GamePlayer gamePlayer, KitStatsContainer kitStats) {
//      Player player = gamePlayer.getPlayer();
//      if (MegaWalls.getRandom().nextInt(10000) <= 20) {
//         player.getInventory().addItem(new ItemStack[]{(new ItemBuilder(Material.GOLDEN_APPLE)).setDisplayName("&e吃掉的金苹果").build()});
//      }
//
//      if ((double)MegaWalls.getRandom().nextInt(600) <= this.getAttribute(kitStats.getSkill3Level()) * 100.0D) {
//         if ((new Random()).nextInt(1) == 1) {
//            player.getInventory().addItem(new ItemStack[]{(new ItemBuilder(Material.PUMPKIN_PIE)).setDisplayName("&e垃圾派").build()});
//         } else {
//            player.getInventory().addItem(new ItemStack[]{(new ItemBuilder(Material.COOKIE)).setDisplayName("&e垃圾曲奇").build()});
//         }
//      }
//
//      return true;
//   }
//}
