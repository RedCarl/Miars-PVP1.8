package cn.mcarl.miars.megawalls.classes.stater.cow;


import cn.mcarl.miars.megawalls.classes.Classes;
import cn.mcarl.miars.megawalls.classes.Skill;
import cn.mcarl.miars.megawalls.game.entitiy.GamePlayer;
import cn.mcarl.miars.megawalls.game.manager.GameManager;
import cn.mcarl.miars.megawalls.stats.ClassesStats;
import cn.mcarl.miars.megawalls.utils.PlayerUtils;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class ThirdSkill extends Skill {
   public ThirdSkill(Classes classes) {
      super("提神一抿", classes);
   }

   @Override
   public int maxedLevel() {
      return 3;
   }

   @Override
   public double getAttribute(int level) {
      return switch (level) {
         case 2 -> 7.5D;
         case 3 -> 10.0D;
         default -> 5.0D;
      };
   }

   @Override
   public List<String> getInfo(int level) {
      List<String> lore = new ArrayList<>();
      if (level == 1) {
         lore.add(" &8▪ &7喝牛奶将会给予你和周围队友");
         lore.add("   &7生命恢复I效果,持续&a" + this.getAttribute(level) + "&7秒,");
         lore.add("   &7同时补充饥饿度和饱食度。");
         return lore;
      } else {
         lore.add(" &8▪ &7喝牛奶将会给予你和周围队友");
         lore.add("   &7生命恢复I效果,持续&8" + this.getAttribute(level - 1) + " ➜ &a" + this.getAttribute(level) + "&7秒,");
         lore.add("   &7同时补充饥饿度和饱食度。");
         return lore;
      }
   }

   @Override
   public void upgrade(GamePlayer gamePlayer) {

   }

   @Override
   public int getPlayerLevel(GamePlayer gamePlayer) {
      return gamePlayer.getPlayerStats().getClassesStats(this.getClasses()).getSkill3Level();
   }

   @Override
   public boolean use(GamePlayer gamePlayer, ClassesStats kitStats) {
      Player player = gamePlayer.getPlayer();
      if (player.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)) {
         player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
      }

      player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, (int)(this.getAttribute(kitStats.getSkill3Level()) * 20.0D), 0));
      if (player.hasPotionEffect(PotionEffectType.REGENERATION)) {
         player.removePotionEffect(PotionEffectType.REGENERATION);
      }

      player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, (int)(this.getAttribute(kitStats.getSkill3Level()) * 20.0D), 0));
      player.setFoodLevel(20);

      for (Player teammate : this.getTeammates(player, 5)) {
         if (teammate.hasPotionEffect(PotionEffectType.REGENERATION)) {
            teammate.removePotionEffect(PotionEffectType.REGENERATION);
         }

         teammate.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, (int) (this.getAttribute(kitStats.getSkill3Level()) * 20.0D), 0));
         teammate.setFoodLevel(20);
      }

      return true;
   }

   private List<Player> getTeammates(Player player, int radius) {
      List<Player> players = new ArrayList<>();

      for (Player other : PlayerUtils.getNearbyPlayers(player, radius)) {
         GamePlayer gameOther = GamePlayer.get(other.getUniqueId());
         if (!gameOther.isSpectator() && GameManager.getInstance().getGamePlayerTeam(GamePlayer.get(player.getUniqueId())).isInTeam(gameOther)) {
            players.add(other);
         }
      }

      return players;
   }
}
