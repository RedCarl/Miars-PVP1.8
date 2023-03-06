//package cn.mcarl.miars.megawalls.classes.mythic.mole;
//
//import cn.wolfmc.megawalls.MegaWalls;
//import cn.wolfmc.megawalls.classes.Classes;
//import cn.wolfmc.megawalls.classes.Skill;
//import cn.wolfmc.megawalls.game.GamePlayer;
//import cn.wolfmc.megawalls.stats.KitStatsContainer;
//import cn.wolfmc.megawalls.util.StringUtils;
//import org.bukkit.entity.Player;
//import org.bukkit.potion.PotionEffect;
//import org.bukkit.potion.PotionEffectType;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class SecondSkill extends Skill {
//   public SecondSkill(Classes classes) {
//      super("捷径", classes);
//   }
//
//   public int maxedLevel() {
//      return 3;
//   }
//
//   public double getAttribute(int level) {
//      switch(level) {
//         case 1:
//            return 0.1D;
//         case 2:
//            return 0.2D;
//         case 3:
//            return 0.3D;
//         default:
//            return 0.1D;
//      }
//   }
//
//   public int getSpeed(int level) {
//      switch(level) {
//         case 1:
//         case 2:
//            return 1;
//         case 3:
//            return 2;
//         default:
//            return 1;
//      }
//   }
//
//   public int getDig(int level) {
//      switch(level) {
//         case 1:
//            return 1;
//         case 2:
//         case 3:
//            return 2;
//         default:
//            return 1;
//      }
//   }
//
//   public List<String> getInfo(int level) {
//      List<String> lore = new ArrayList();
//      if (level == 1) {
//         lore.add(" &8▪ &7挖掘可开采的方块时,有&a" + StringUtils.percent(this.getAttribute(level)) + "&7的几率");
//         lore.add("   &7获得速度&a" + StringUtils.level(this.getSpeed(level)) + "&7和急迫&a" + StringUtils.level(this.getDig(level)) + "&7效果,持续4秒。");
//         return lore;
//      } else {
//         lore.add(" &8▪ &7挖掘可开采的方块时,有&8" + StringUtils.percent(this.getAttribute(level - 1)) + " ➜ &a" + StringUtils.percent(this.getAttribute(level)) + "&7的几率");
//         lore.add("   &7获得速度" + (this.getSpeed(level - 1) == this.getSpeed(level) ? "&8" + StringUtils.level(this.getSpeed(level)) : "&8" + StringUtils.level(this.getSpeed(level - 1)) + " ➜ &a" + StringUtils.level(this.getSpeed(level))) + "&7和急迫" + (this.getDig(level - 1) == this.getDig(level) ? "&8" + StringUtils.level(this.getDig(level)) : "&8" + StringUtils.level(this.getDig(level - 1)) + " ➜ &a" + StringUtils.level(this.getDig(level))) + "&7效果,持续4秒。");
//         return lore;
//      }
//   }
//
//   public void upgrade(GamePlayer gamePlayer) {
//      KitStatsContainer kitStats = gamePlayer.getPlayerStats().getKitStats(this.getClasses());
//      kitStats.addSkill2Level();
//   }
//
//   public int getPlayerLevel(GamePlayer gamePlayer) {
//      return gamePlayer.getPlayerStats().getKitStats(this.getClasses()).getSkill2Level();
//   }
//
//   public boolean use(GamePlayer gamePlayer, KitStatsContainer kitStats) {
//      int level = kitStats.getSkill2Level();
//      if ((double) MegaWalls.getRandom().nextInt(100) <= this.getAttribute(level) * 100.0D) {
//         Player player = gamePlayer.getPlayer();
//         if (player.hasPotionEffect(PotionEffectType.SPEED)) {
//            player.removePotionEffect(PotionEffectType.SPEED);
//         }
//
//         player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 80, this.getSpeed(level) - 1));
//         if (player.hasPotionEffect(PotionEffectType.FAST_DIGGING)) {
//            player.removePotionEffect(PotionEffectType.FAST_DIGGING);
//         }
//
//         player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 80, this.getDig(level) - 1));
//      }
//
//      return true;
//   }
//}
