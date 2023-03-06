package cn.mcarl.miars.megawalls.classes;

import cc.carm.lib.easyplugin.utils.ColorParser;
import cn.mcarl.miars.megawalls.game.entitiy.GamePlayer;
import cn.mcarl.miars.megawalls.stats.ClassesStats;
import org.bukkit.Material;

public abstract class Skill implements Upgradeable {
   public static final String TICK = ColorParser.parse("&8[&a&l✔&8]");
   public static final String FORK = ColorParser.parse("&8[&c&l✘&8]");
   private final String name;
   private final Classes classes;

   public Skill(String name, Classes classes) {
      this.name = name;
      this.classes = classes;
   }

   @Override
   public Material getIconType() {
      return this.getClasses().getIconType();
   }

   @Override
   public byte getIconData() {
      return this.getClasses().getIconData();
   }

   @Override
   public int getCost(int level) {
      if (this.maxedLevel() == 5) {
         return switch (level) {
            case 2 -> 1000;
            case 3 -> 2000;
            case 4 -> 4000;
            case 5 -> 7500;
            default -> 999999;
         };
      } else {
         return switch (level) {
            case 2 -> 2000;
            case 3 -> 5000;
            default -> 999999;
         };
      }
   }

   @Override
   public String getName() {
      return this.name;
   }

   public Classes getClasses() {
      return this.classes;
   }
   public boolean use(GamePlayer gamePlayer, ClassesStats classesStats) {
      return true;
   }
   public String getSkillTip(GamePlayer gamePlayer) {
      return this.classes.getNameColor() + this.name + (gamePlayer.getEnergy() == 100 ? TICK : FORK);
   }
}
