package cn.mcarl.miars.megawalls.stats;

import cn.mcarl.miars.megawalls.classes.Classes;
import cn.mcarl.miars.megawalls.classes.ClassesManager;
import cn.mcarl.miars.megawalls.game.entitiy.GamePlayer;
import cn.mcarl.miars.megawalls.game.manager.GamePlayerManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;

import java.util.*;
import java.util.Map.Entry;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerStats {
   private static Gson gson = new Gson();
   private int id;
   private UUID uuid;
   private int kills;
   private int finalKills;
   private int wins;
   private int games;
   private int coins;
   private int mythicDust;
   private String classes;
   private Map<Classes, ClassesStats> classesStatsMap = new HashMap<>();

   public PlayerStats(GamePlayer gamePlayer) {
      this.uuid = gamePlayer.getUuid();
   }

   public void update(){
      this.classes = "";
      this.kills = 0;
      this.finalKills = 0;
      this.wins = 0;
      this.games = 0;
      this.coins = 0;
      this.mythicDust = 0;
      for (String s: new String[]{"Cow", "Hunter", "Shark"}) {
         Classes c = ClassesManager.getClassesByName(s);
         ClassesStats classesStats = new ClassesStats(GamePlayerManager.getInstance().getGamePlayer(Bukkit.getPlayer(this.uuid)), c);
         classesStats.setEnderChest(3);
         classesStats.setEquipLevel(1);
         this.classesStatsMap.put(c,classesStats);
      }
   }


   public ClassesStats getClassesStats(Classes classes) {
      return this.classesStatsMap.getOrDefault(classes, null);
   }

}
