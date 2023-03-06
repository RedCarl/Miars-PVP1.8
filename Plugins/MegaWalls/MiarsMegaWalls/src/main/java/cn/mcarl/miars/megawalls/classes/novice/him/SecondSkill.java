package cn.mcarl.miars.megawalls.classes.novice.him;

import cn.mcarl.miars.megawalls.classes.Classes;
import cn.mcarl.miars.megawalls.classes.Skill;
import cn.mcarl.miars.megawalls.game.entitiy.GamePlayer;

import java.util.ArrayList;
import java.util.List;

public class SecondSkill extends Skill {
   public SecondSkill(Classes classes) {
      super("力量", classes);
   }

   @Override
   public int maxedLevel() {
      return 3;
   }

   @Override
   public double getAttribute(int level) {
      return switch (level) {
         case 2 -> 5.0D;
         case 3 -> 6.0D;
         default -> 4.0D;
      };
   }

   @Override
   public List<String> getInfo(int level) {
      List<String> lore = new ArrayList();
      if (level == 1) {
         lore.add(" &8▪ &7击杀一名敌人后,获得85%的近战");
         lore.add("   &7伤害加成,持续&a" + this.getAttribute(level) + "&7秒。");
         return lore;
      } else {
         lore.add(" &8▪ &7击杀一名敌人后,获得85%的近战");
         lore.add("   &7伤害加成,持续&8" + this.getAttribute(level - 1) + " ➜ &a" + this.getAttribute(level) + "&7秒。");
         return lore;
      }
   }

   @Override
   public void upgrade(GamePlayer gamePlayer) {
   }

   @Override
   public int getPlayerLevel(GamePlayer gamePlayer) {
      return 0;
   }
}
