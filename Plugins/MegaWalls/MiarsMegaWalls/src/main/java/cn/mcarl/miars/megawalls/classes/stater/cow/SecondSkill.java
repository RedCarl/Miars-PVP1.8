package cn.mcarl.miars.megawalls.classes.stater.cow;

import cn.mcarl.miars.megawalls.MiarsMegaWalls;
import cn.mcarl.miars.megawalls.classes.Classes;
import cn.mcarl.miars.megawalls.classes.Skill;
import cn.mcarl.miars.megawalls.game.entitiy.GamePlayer;
import cn.mcarl.miars.megawalls.stats.ClassesStats;
import cn.mcarl.miars.megawalls.utils.StringUtils;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SecondSkill extends Skill {
   public SecondSkill(Classes classes) {
      super("奶桶屏障", classes);
   }

   @Override
   public int maxedLevel() {
      return 3;
   }

   @Override
   public double getAttribute(int level) {
      return switch (level) {
         case 2 -> 0.375D;
         case 3 -> 0.5D;
         default -> 0.25D;
      };
   }

   @Override
   public List<String> getInfo(int level) {
      List<String> lore = new ArrayList<>();
      if (level == 1) {
         lore.add(" &8▪ &7当你血量低于20的时候,");
         lore.add("   &7你头顶会不断掉出奶桶,持续20秒");
         lore.add("   &7接下来4次任何来源的伤害都将被减少&a" + StringUtils.percent(this.getAttribute(level)));
         lore.add("   &7每次减伤你额外恢复2血量。");
         return lore;
      } else {
         lore.add(" &8▪ &7当你血量低于20的时候,");
         lore.add("   &7你头顶会不断掉出奶桶,持续20秒");
         lore.add("   &7接下来4次任何来源的伤害都将被减少&8" + StringUtils.percent(this.getAttribute(level - 1)) + " ➜ &a" + StringUtils.percent(this.getAttribute(level)));
         lore.add("   &7每次减伤你额外恢复2血量。");
         lore.add(" ");
         lore.add("&7冷却时间:&a40秒");
         return lore;
      }
   }

   @Override
   public void upgrade(GamePlayer gamePlayer) {

   }

   @Override
   public int getPlayerLevel(GamePlayer gamePlayer) {
      return gamePlayer.getPlayerStats().getClassesStats(this.getClasses()).getSkill2Level();
   }

   @Override
   public boolean use(GamePlayer gamePlayer, ClassesStats classesStats) {
      if (Cow.skill2Cooldown.getOrDefault(gamePlayer, 0) > 0) {
         return false;
      } else {
         final Player player = gamePlayer.getPlayer();
         new BukkitRunnable() {
            private int ticks = 0;
            private final List<Item> items = new ArrayList<>();
            @Override
            public void run() {
               if (player.isOnline() && this.ticks < 400) {
                  Item item = player.getWorld().dropItem(player.getLocation().add(0.0D, 2.0D, 0.0D), new ItemStack(Material.MILK_BUCKET));
                  item.setMetadata(MiarsMegaWalls.getMetadataValue(), MiarsMegaWalls.getFixedMetadataValue());
                  Vector vector = new Vector((MiarsMegaWalls.getRandom().nextDouble() - 0.5D) / 1.7D, 0.35D, (MiarsMegaWalls.getRandom().nextDouble() - 0.5D) / 1.7D);
                  item.setVelocity(vector);
                  this.items.add(item);
                  this.ticks += 5;
               } else {
                  Iterator var1 = this.items.iterator();

                  while(var1.hasNext()) {
                     Item itemx = (Item)var1.next();
                     itemx.remove();
                  }

                  this.cancel();
               }

            }
         }.runTaskTimer(MiarsMegaWalls.getInstance(), 0L, 5L);
         Cow.skill2Cooldown.put(gamePlayer, 40);
         Cow.skill2Damage.put(gamePlayer, 0);
         return true;
      }
   }

   @Override
   public String getSkillTip(GamePlayer gamePlayer) {
      return this.getClasses().getNameColor() + "&l" + this.getName() + " " + (Cow.skill2Cooldown.getOrDefault(gamePlayer, 0) == 0 ? "&8[&a&l✔&8]" : "&c&l" + Cow.skill2Cooldown.get(gamePlayer) + "秒");
   }
}
