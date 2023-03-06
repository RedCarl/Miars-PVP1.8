package cn.mcarl.miars.megawalls.classes.novice.him;

import cn.mcarl.miars.megawalls.classes.Classes;
import cn.mcarl.miars.megawalls.classes.Skill;
import cn.mcarl.miars.megawalls.game.entitiy.GamePlayer;

import java.util.ArrayList;
import java.util.List;

public class MainSkill extends Skill {
   public MainSkill(Classes classes) {
      super("雷神之怒", classes);
   }

   @Override
   public int maxedLevel() {
      return 5;
   }

   @Override
   public double getAttribute(int level) {
      return switch (level) {
         case 2 -> 3.3D;
         case 3 -> 3.7D;
         case 4 -> 4.1D;
         case 5 -> 4.5D;
         default -> 2.9D;
      };
   }

   @Override
   public List<String> getInfo(int level) {
      List<String> lore = new ArrayList();
      lore.add(" &8▪ &7释放雷神之怒,");
      if (level == 1) {
         lore.add("   &7对附近的敌人造成&a" + this.getAttribute(level) + "&7点伤害。");
         lore.add(" ");
         lore.add("&7冷却时间:&a1秒");
      } else {
         lore.add("   &7对附近的敌人造成&8" + this.getAttribute(level - 1) + " ➜ &a" + this.getAttribute(level) + "&7点伤害。");
      }
      return lore;
   }

   @Override
   public void upgrade(GamePlayer gamePlayer) {
   }

   @Override
   public int getPlayerLevel(GamePlayer gamePlayer) {
      return 0;
   }
}
