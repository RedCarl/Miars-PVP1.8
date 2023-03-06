package cn.mcarl.miars.megawalls.classes.stater.cow;

import cn.mcarl.miars.megawalls.MiarsMegaWalls;
import cn.mcarl.miars.megawalls.classes.Classes;
import cn.mcarl.miars.megawalls.classes.Skill;
import cn.mcarl.miars.megawalls.game.entitiy.GamePlayer;
import cn.mcarl.miars.megawalls.game.manager.GameManager;
import cn.mcarl.miars.megawalls.stats.ClassesStats;
import cn.mcarl.miars.megawalls.utils.ParticleEffect;
import cn.mcarl.miars.megawalls.utils.PlayerUtils;
import cn.mcarl.miars.megawalls.utils.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainSkill extends Skill {
   public MainSkill(Classes classes) {
      super("抚慰之哞", classes);
   }

   @Override
   public int maxedLevel() {
      return 5;
   }

   @Override
   public double getAttribute(int level) {
      return switch (level) {
         case 2 -> 1.9D;
         case 3 -> 2.1D;
         case 4 -> 2.3D;
         case 5 -> 2.5D;
         default -> 1.7D;
      };
   }

   public int getRegenLevel(int level) {
      return switch (level) {
         case 3, 4, 5 -> 2;
         default -> 1;
      };
   }

   @Override
   public List<String> getInfo(int level) {
      List<String> lore = new ArrayList<>();
      if (level == 1) {
         lore.add(" &8▪ &7哞,");
         lore.add("   &7自身获得抗性提升I和生命");
         lore.add("   &7恢复I效果,周围的队友获得");
         lore.add("   &7持续&a" + this.getAttribute(level) + "&7秒的生命恢复&a" + StringUtils.level(this.getRegenLevel(level)) + "&7效果。");
         return lore;
      } else {
         lore.add(" &8▪ &7哞,");
         lore.add("   &7自身获得抗性提升I和生命");
         lore.add("   &7恢复I效果,周围的队友获得");
         lore.add("   &7持续&8" + this.getAttribute(level - 1) + " ➜ &a" + this.getAttribute(level) + "&7秒的生命恢复&8" + StringUtils.level(this.getRegenLevel(level - 1)) + " ➜ &a" + StringUtils.level(this.getRegenLevel(level)) + "&7效果。");
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

   public boolean use(final GamePlayer gamePlayer, ClassesStats classesStats) {
      int level = classesStats.getSkillLevel();
      Player player = gamePlayer.getPlayer();
      if (player.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)) {
         player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
      }

      player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, (int)(this.getAttribute(level) * 20.0D), 0));
      if (player.hasPotionEffect(PotionEffectType.REGENERATION)) {
         player.removePotionEffect(PotionEffectType.REGENERATION);
      }

      player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, (int)(this.getAttribute(level) * 20.0D), 0));

      Player teammate;
      for(Iterator<Player> var5 = this.getTeammates(player, 5).iterator(); var5.hasNext(); teammate.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, (int)(this.getAttribute(level) * 20.0D), this.getRegenLevel(level)))) {
         teammate = var5.next();
         if (teammate.hasPotionEffect(PotionEffectType.REGENERATION)) {
            teammate.removePotionEffect(PotionEffectType.REGENERATION);
         }
      }

      new BukkitRunnable() {
         int step = 0;
         @Override
         public void run() {
            ++this.step;
            if (this.step != 40 && gamePlayer.isOnline()) {
               ParticleEffect.HEART.display(0.2F, 0.5F, 0.2F, 1.0F, 1, gamePlayer.getPlayer().getLocation().add(0.0D, 1.0D, 0.0D), 30.0D);
            } else {
               this.cancel();
            }

         }
      }.runTaskTimer(MiarsMegaWalls.getInstance(), 0L, 1L);
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
