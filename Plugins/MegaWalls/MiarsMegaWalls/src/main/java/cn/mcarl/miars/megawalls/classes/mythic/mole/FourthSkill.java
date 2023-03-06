//package cn.mcarl.miars.megawalls.classes.mythic.mole;
//
//import cn.wolfmc.megawalls.MegaWalls;
//import cn.wolfmc.megawalls.classes.Classes;
//import cn.wolfmc.megawalls.classes.CollectSkill;
//import cn.wolfmc.megawalls.game.ChestManager;
//import cn.wolfmc.megawalls.game.GamePlayer;
//import cn.wolfmc.megawalls.stats.KitStatsContainer;
//import cn.wolfmc.megawalls.util.StringUtils;
//import org.bukkit.Bukkit;
//import org.bukkit.Material;
//import org.bukkit.block.Block;
//import org.bukkit.block.Chest;
//import org.bukkit.entity.Firework;
//import org.bukkit.event.block.BlockBreakEvent;
//import org.bukkit.inventory.ItemStack;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class FourthSkill extends CollectSkill {
//   public FourthSkill(Classes classes) {
//      super("囤铁", classes);
//   }
//
//   public int maxedLevel() {
//      return 3;
//   }
//
//   public double getAttribute(int level) {
//      switch(level) {
//         case 1:
//            return 1.0D;
//         case 2:
//            return 2.0D;
//         case 3:
//            return 3.0D;
//         default:
//            return 1.0D;
//      }
//   }
//
//   public List<String> getInfo(int level) {
//      List<String> lore = new ArrayList();
//      if (level == 1) {
//         lore.add(" &8▪ &7找到宝箱时,有&a" + StringUtils.percent(this.getAttribute(level)) + "&7的几率");
//         lore.add("   &7额外获得铁锭。");
//         return lore;
//      } else {
//         lore.add(" &8▪ &7找到宝箱时,有&8" + StringUtils.percent(this.getAttribute(level - 1)) + " ➜ &a" + StringUtils.percent(this.getAttribute(level)) + "&7的几率");
//         lore.add("   &7额外获得铁锭。");
//         return lore;
//      }
//   }
//
//   public void upgrade(GamePlayer gamePlayer) {
//      KitStatsContainer kitStats = gamePlayer.getPlayerStats().getKitStats(this.getClasses());
//      kitStats.addSkill4Level();
//   }
//
//   public int getPlayerLevel(GamePlayer gamePlayer) {
//      return gamePlayer.getPlayerStats().getKitStats(this.getClasses()).getSkill4Level();
//   }
//
//   public void onBlockBreak(final KitStatsContainer kitStats, final BlockBreakEvent e) {
//      GamePlayer gamePlayer = GamePlayer.get(e.getPlayer().getUniqueId());
//      this.getClasses().getSecondSkill().use(gamePlayer, kitStats);
//      this.getClasses().getThirdSkill().use(gamePlayer, kitStats);
//      if ((e.getBlock().getType() == Material.STONE || e.getBlock().getType() == Material.COBBLESTONE || e.getBlock().getType() == Material.COAL_ORE || e.getBlock().getType() == Material.IRON_ORE || e.getBlock().getType() == Material.GOLD_ORE || e.getBlock().getType() == Material.LOG || e.getBlock().getType() == Material.LOG_2 || e.getBlock().getType() == Material.DIRT || e.getBlock().getType() == Material.GRASS || e.getBlock().getType() == Material.SAND || e.getBlock().getType() == Material.GRAVEL || e.getBlock().getType() == Material.MYCEL) && MegaWalls.getRandom().nextInt(MiarsMegaWalls.getInstance().getGame().isWallsFall() ? 1000 : 150) <= 6) {
//         e.setCancelled(true);
//         Block block = e.getBlock();
//         block.setType(Material.TRAPPED_CHEST);
//         gamePlayer.addProtectedBlock(block);
//         Chest chest = (Chest)block.getState();
//         block.getWorld().spawn(block.getLocation(), Firework.class);
//         ChestManager.fillInventory(chest.getBlockInventory());
//      }
//
//      Bukkit.getScheduler().runTaskLater(MiarsMegaWalls.getInstance(), new Runnable() {
//         public void run() {
//            if (e.getBlock().getType() == Material.TRAPPED_CHEST && (double)MegaWalls.getRandom().nextInt(100) <= FourthSkill.this.getAttribute(kitStats.getSkill4Level()) * 100.0D) {
//               Chest chest = (Chest)e.getBlock().getState();
//               chest.getBlockInventory().addItem(new ItemStack[]{new ItemStack(Material.IRON_INGOT, 8)});
//            }
//
//         }
//      }, 3L);
//   }
//}
