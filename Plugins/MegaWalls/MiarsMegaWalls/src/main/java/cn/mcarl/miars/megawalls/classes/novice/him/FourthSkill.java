package cn.mcarl.miars.megawalls.classes.novice.him;

import cn.mcarl.miars.megawalls.classes.Classes;
import cn.mcarl.miars.megawalls.classes.CollectSkill;
import cn.mcarl.miars.megawalls.game.entitiy.GamePlayer;
import cn.mcarl.miars.megawalls.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class FourthSkill extends CollectSkill {
   public FourthSkill(Classes classes) {
      super("宝藏猎人", classes);
   }

   @Override
   public int maxedLevel() {
      return 3;
   }

   @Override
   public double getAttribute(int level) {
      return switch (level) {
         case 2 -> 1.7D;
         case 3 -> 2.0D;
         default -> 1.4D;
      };
   }

   @Override
   public List<String> getInfo(int level) {
      List<String> lore = new ArrayList();
      if (level == 1) {
         lore.add(" &8▪ &7挖矿时找到宝箱的几率提升&a" + StringUtils.percent(this.getAttribute(level)) + "&7。");
      } else {
         lore.add(" &8▪ &7挖矿时找到宝箱的几率提升&8" + StringUtils.percent(this.getAttribute(level - 1)) + " ➜ &a" + StringUtils.percent(this.getAttribute(level)) + "&7。");
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
