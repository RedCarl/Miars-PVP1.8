package cn.mcarl.miars.megawalls.stats;

import cn.mcarl.miars.megawalls.classes.Classes;
import cn.mcarl.miars.megawalls.game.entitiy.GamePlayer;
import cn.mcarl.miars.megawalls.utils.StringUtils;
import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClassesStats {
   private int id;
   private UUID uuid;
   private Classes classes;
   private int level;
   private int equipLevel;
   private int skillLevel;
   private int skill2Level;
   private int skill3Level;
   private int skill4Level;
   private int wins;
   private int games;
   private int finalKills;
   private int finalAssists;
   private int enderChest;
   private int masterPoints;
   private boolean enableGoldTag;
   private long playTime;
   private JsonObject inventory;

   public ClassesStats(GamePlayer gamePlayer, Classes classes) {
      this.uuid = gamePlayer.getUuid();
      this.classes = classes;
   }

   public String upgradePercent() {
      double percent = (double)(this.equipLevel + this.skillLevel + this.skill2Level + this.skill3Level + this.skill4Level + this.enderChest - 8) / 16.0D;
      return (percent >= 1.0D ? "&a&l" : "&e") + StringUtils.percent(percent);
   }
}
