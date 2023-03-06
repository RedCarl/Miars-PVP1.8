package cn.mcarl.miars.megawalls.classes.novice.him;

import cn.mcarl.miars.megawalls.classes.Classes;
import cn.mcarl.miars.megawalls.classes.Skill;
import cn.mcarl.miars.megawalls.game.entitiy.GamePlayer;

import java.util.ArrayList;
import java.util.List;

public class ThirdSkill extends Skill {
   public ThirdSkill(Classes classes) {
      super("飓风", classes);
   }

   @Override
   public int maxedLevel() {
      return 3;
   }

   @Override
   public double getAttribute(int level) {
      return switch (level) {
         case 2 -> 4.0D;
         case 3 -> 3.0D;
         default -> 5.0D;
      };
   }

   @Override
   public List<String> getInfo(int level) {
      List<String> lore = new ArrayList<>();
      if (level == 1) {
         lore.add(" &8▪ &7每&a" + this.getAttribute(level) + "&7次攻击,获得");
      } else {
         lore.add(" &8▪ &7每&8" + this.getAttribute(level - 1) + " ➜ &a" + this.getAttribute(level) + "&7次攻击,获得");
      }
      lore.add("   &7速度 II 和生命恢复 II 持续3秒");
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
