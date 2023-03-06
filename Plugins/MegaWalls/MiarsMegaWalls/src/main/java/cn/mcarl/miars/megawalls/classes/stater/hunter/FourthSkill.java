package cn.mcarl.miars.megawalls.classes.stater.hunter;

import cn.mcarl.miars.megawalls.MiarsMegaWalls;
import cn.mcarl.miars.megawalls.classes.Classes;
import cn.mcarl.miars.megawalls.classes.CollectSkill;
import cn.mcarl.miars.megawalls.game.entitiy.GamePlayer;
import cn.mcarl.miars.megawalls.stats.ClassesStats;
import cn.mcarl.miars.megawalls.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class FourthSkill extends CollectSkill {
   public FourthSkill(Classes classes) {
      super("黄金眼镜", classes);
   }

   @Override
   public int maxedLevel() {
      return 3;
   }

   @Override
   public double getAttribute(int level) {
      return switch (level) {
         case 2 -> 0.5D;
         case 3 -> 0.75D;
         default -> 0.25D;
      };
   }

   public List<String> getInfo(int level) {
      List<String> lore = new ArrayList<>();
      if (level == 1) {
         lore.add(" &8▪ &7获得永久夜视效果,并有&a" + StringUtils.percent(this.getAttribute(level)) + "&7的");
         lore.add("    &7几率在箱子中获得一个金苹果。");
         return lore;
      } else {
         lore.add(" &8▪ &7获得永久夜视效果,并有&8" + StringUtils.percent(this.getAttribute(level - 1)) + " ➜ &a" + StringUtils.percent(this.getAttribute(level)) + "&7的");
         lore.add("    &7几率在箱子中获得一个金苹果。");
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


   public void onBlockBreak(final ClassesStats classesStats, final BlockBreakEvent e) {
      Bukkit.getScheduler().runTaskLater(MiarsMegaWalls.getInstance(), () -> {
         if (e.getBlock().getType() == Material.TRAPPED_CHEST && (double)MiarsMegaWalls.getRandom().nextInt(300) <= FourthSkill.this.getAttribute(classesStats.getSkill4Level()) * 100.0D) {
            Chest chest = (Chest)e.getBlock().getState();
            chest.getBlockInventory().addItem(new ItemStack(Material.GOLDEN_APPLE));
         }

      }, 3L);
   }
}
