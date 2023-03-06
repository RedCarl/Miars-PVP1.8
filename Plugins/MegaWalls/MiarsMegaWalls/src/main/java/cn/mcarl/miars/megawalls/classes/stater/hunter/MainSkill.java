package cn.mcarl.miars.megawalls.classes.stater.hunter;

import cn.mcarl.miars.megawalls.classes.Classes;
import cn.mcarl.miars.megawalls.classes.Skill;
import cn.mcarl.miars.megawalls.game.entitiy.GamePlayer;
import cn.mcarl.miars.megawalls.stats.ClassesStats;
import org.bukkit.Sound;

import java.util.ArrayList;
import java.util.List;

public class MainSkill extends Skill {
   public MainSkill(Classes classes) {
      super("鹰眼", classes);
   }

   @Override
   public int maxedLevel() {
      return 5;
   }

   @Override
   public double getAttribute(int level) {
      return switch (level) {
         case 2 -> 9.2D;
         case 3 -> 10.8D;
         case 4 -> 12.4D;
         case 5 -> 14.0D;
         default -> 7.6D;
      };
   }

   @Override
   public List<String> getInfo(int level) {
      List<String> lore = new ArrayList<>();
      if (level == 1) {
         lore.add(" &8▪ &7为拉满的弓箭附加追踪效果,持续&a" + this.getAttribute(level) + "&7秒。");
         lore.add("   &7每次命中恢复0.5血量,但不会增加能量。");
         return lore;
      } else {
         lore.add(" &8▪ &7为拉满的弓箭附加追踪效果,持续&8" + this.getAttribute(level - 1) + " ➜ &a" + this.getAttribute(level) + "&7秒。");
         lore.add("   &7每次命中恢复0.5血量,但不会增加能量。");
         return lore;
      }
   }

   @Override
   public void upgrade(GamePlayer gamePlayer) {
      ClassesStats classesStats = gamePlayer.getPlayerStats().getClassesStats(this.getClasses());
   }

   @Override
   public int getPlayerLevel(GamePlayer gamePlayer) {
      return gamePlayer.getPlayerStats().getClassesStats(this.getClasses()).getSkillLevel();
   }

   @Override
   public boolean use(GamePlayer gamePlayer, ClassesStats classesStats) {
      if (Hunter.skill.getOrDefault(gamePlayer, 0) > 0) {
         return false;
      } else {
         gamePlayer.playSound(Sound.ORB_PICKUP, 1.0F, 1.0F);
         Hunter.skill.put(gamePlayer, (int)this.getAttribute(this.getPlayerLevel(gamePlayer)));
         return true;
      }
   }

   @Override
   public String getSkillTip(GamePlayer gamePlayer) {
      return this.getClasses().getNameColor() + "&l" + this.getName() + " " + (Hunter.skill.getOrDefault(gamePlayer, 0) == 0 ? (gamePlayer.getEnergy() == 100 ? "&8[&a&l✔&8]" : "&8[&c&l✘&8]") : "&e&l" + Hunter.skill.get(gamePlayer));
   }
}
