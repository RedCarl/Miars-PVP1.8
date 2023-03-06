package cn.mcarl.miars.megawalls.classes.stater.hunter;

import cn.mcarl.miars.megawalls.classes.Classes;
import cn.mcarl.miars.megawalls.classes.Skill;
import cn.mcarl.miars.megawalls.game.entitiy.GamePlayer;
import cn.mcarl.miars.megawalls.stats.ClassesStats;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;
import java.util.Map.Entry;

public class ThirdSkill extends Skill {
   public static final Map<String, PotionEffect> potions = new HashMap();

   public ThirdSkill(Classes classes) {
      super("自然之力", classes);
      potions.put("速度I 11秒", new PotionEffect(PotionEffectType.SPEED, 220, 0));
      potions.put("抗性提升I 6秒", new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 120, 0));
      potions.put("急迫I 15秒", new PotionEffect(PotionEffectType.FAST_DIGGING, 300, 0));
      potions.put("力量I 6秒", new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 120, 0));
      potions.put("生命恢复II 10秒", new PotionEffect(PotionEffectType.REGENERATION, 200, 0));
      potions.put("速度II 6秒", new PotionEffect(PotionEffectType.SPEED, 120, 0));
   }

   @Override
   public int maxedLevel() {
      return 3;
   }

   @Override
   public double getAttribute(int level) {
      return switch (level) {
         case 2 -> 40.0D;
         case 3 -> 30.0D;
         default -> 50.0D;
      };
   }

   @Override
   public List<String> getInfo(int level) {
      List<String> lore = new ArrayList<>();
      if (level == 1) {
         lore.add(" &8▪ &7每&a" + this.getAttribute(level) + "&7秒获得一个随机增益效果。");
         lore.add("   &7可能是持续11秒的速度I,6秒的");
         lore.add("   &7抗性提升I,15秒的急迫I,6秒的");
         lore.add("   &7力量I,10秒的生命恢复II或者6秒");
         lore.add("   &7的速度II。高墙倒下后将不再获得");
         lore.add("   &7急迫I效果。");
         return lore;
      } else {
         lore.add(" &8▪ &7每&8" + this.getAttribute(level - 1) + " ➜");
         lore.add("   &a" + this.getAttribute(level) + "&7秒获得一个随机增益效果。");
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
   public boolean use(GamePlayer gamePlayer, ClassesStats classesStats) {
      if (Hunter.skill3Cooldown.getOrDefault(gamePlayer, 0) > 0) {
         return false;
      } else if (Hunter.skill3Seconds.getOrDefault(gamePlayer, 0) > 0) {
         return false;
      } else {
         List<Entry<String, PotionEffect>> list = new ArrayList<>(potions.entrySet());
         Entry<String, PotionEffect> entry = list.get((new Random()).nextInt(list.size() - 1));
         Player player = gamePlayer.getPlayer();
         player.addPotionEffect(entry.getValue());
         player.sendMessage("&a你的自然之力技能让你获得&e" + entry.getKey() + "&a效果。");
         Hunter.skill3.put(gamePlayer, entry.getKey());
         Hunter.skill3Seconds.put(gamePlayer, entry.getValue().getDuration() / 20);
         Hunter.skill3Cooldown.put(gamePlayer, (int)this.getAttribute(this.getPlayerLevel(gamePlayer)));
         return true;
      }
   }

   @Override
   public String getSkillTip(GamePlayer gamePlayer) {
      return this.getClasses().getNameColor() + "&l" + this.getName() + " " + (Hunter.skill3.containsKey(gamePlayer) ? "&7(" + (String)Hunter.skill3.get(gamePlayer) + ") &e&l" + Hunter.skill3Seconds.get(gamePlayer) : "&c&l" + Hunter.skill3Cooldown.getOrDefault(gamePlayer, 0) + "秒");
   }
}
