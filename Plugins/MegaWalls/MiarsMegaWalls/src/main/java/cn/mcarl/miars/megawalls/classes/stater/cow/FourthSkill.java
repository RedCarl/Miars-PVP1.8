package cn.mcarl.miars.megawalls.classes.stater.cow;

import cn.mcarl.miars.megawalls.MiarsMegaWalls;
import cn.mcarl.miars.megawalls.classes.Classes;
import cn.mcarl.miars.megawalls.classes.CollectSkill;
import cn.mcarl.miars.megawalls.game.entitiy.GamePlayer;
import cn.mcarl.miars.megawalls.stats.ClassesStats;
import cn.mcarl.miars.megawalls.utils.StringUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class FourthSkill extends CollectSkill {
   public FourthSkill(Classes classes) {
      super("超级巴氏杀菌", classes);
   }

   @Override
   public int maxedLevel() {
      return 3;
   }

   @Override
   public double getAttribute(int level) {
      return switch (level) {
         case 2 -> 0.02D;
         case 3 -> 0.03D;
         default -> 0.01D;
      };
   }

   @Override
   public List<String> getInfo(int level) {
      List<String> lore = new ArrayList<>();
      if (level == 1) {
         lore.add(" &8▪ &7石头有&a" + StringUtils.percent(this.getAttribute(level)) + "&7掉落牛奶桶。");
         lore.add("   &7饮用牛奶桶可获得抗性提升I和生命恢复I,");
         lore.add("   &7持续5秒。牛奶桶可被丢出。");
         lore.add("   &7对于非奶牛职业的玩家,生命恢复加倍。");
         return lore;
      } else {
         lore.add(" &8▪ &7石头有&8" + StringUtils.percent(this.getAttribute(level - 1)) + " ➜ &a" + StringUtils.percent(this.getAttribute(level)) + "&7掉落牛奶桶。");
         lore.add("   &7饮用牛奶桶可获得抗性提升I和生命恢复I,");
         lore.add("   &7持续5秒。牛奶桶可被丢出。");
         lore.add("   &7对于非奶牛职业的玩家,生命恢复加倍。");
         return lore;
      }
   }

   @Override
   public void upgrade(GamePlayer gamePlayer) {

   }

   @Override
   public int getPlayerLevel(GamePlayer gamePlayer) {
      return gamePlayer.getPlayerStats().getClassesStats(this.getClasses()).getSkill4Level();
   }

   public void onBlockBreak(ClassesStats classesStats, BlockBreakEvent e) {
      if (e.getBlock().getType() == Material.STONE && (double) MiarsMegaWalls.getRandom().nextInt(100) <= this.getAttribute(classesStats.getSkill4Level()) * 100.0D) {
         e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.COW_IDLE, 1.0F, 1.0F);
         e.getBlock().getWorld().dropItem(e.getBlock().getLocation(), new ItemStack(Material.MILK_BUCKET));
      }

   }
}
