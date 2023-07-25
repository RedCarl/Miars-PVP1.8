//package cn.mcarl.miars.megawalls.classes.mythic.mole;
//
//import cn.mcarl.miars.core.MiarsCore;
//import cn.mcarl.miars.megawalls.classes.Classes;
//import cn.mcarl.miars.megawalls.classes.Skill;
//import cn.mcarl.miars.megawalls.game.entity.GamePlayer;
//import cn.mcarl.miars.megawalls.game.manager.GameManager;
//import cn.mcarl.miars.megawalls.game.manager.GamePlayerManager;
//import cn.mcarl.miars.megawalls.stats.ClassesStats;
//import cn.mcarl.miars.megawalls.utils.LocationUtils;
//import cn.mcarl.miars.megawalls.utils.PlayerUtils;
//import org.bukkit.Location;
//import org.bukkit.Material;
//import org.bukkit.block.Block;
//import org.bukkit.entity.Entity;
//import org.bukkit.entity.Player;
//import org.bukkit.scheduler.BukkitRunnable;
//import org.bukkit.util.Vector;
//
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//
//public class MainSkill extends Skill {
//   public MainSkill(Classes classes) {
//      super("挖挖挖", classes);
//   }
//
//   public int maxedLevel() {
//      return 5;
//   }
//
//   public double getAttribute(int level) {
//      switch(level) {
//         case 1:
//            return 4.0D;
//         case 2:
//            return 5.0D;
//         case 3:
//            return 6.0D;
//         case 4:
//            return 7.0D;
//         case 5:
//            return 8.0D;
//         default:
//            return 4.0D;
//      }
//   }
//
//   public List<String> getInfo(int level) {
//      List<String> lore = new ArrayList();
//      if (level == 1) {
//         lore.add(" &8▪ &7向前冲刺最多&a" + this.getAttribute(level) + "&7格方块,");
//         lore.add("   &7能打碎所有你前进方向的所有方块。");
//         lore.add("   &7所有被该技能击中的敌人将");
//         lore.add("   &7受到&a" + this.getAttribute(level) + "&7点伤害。");
//         return lore;
//      } else {
//         lore.add(" &8▪ &7向前冲刺最多&8" + this.getAttribute(level - 1) + " ➜ &a" + this.getAttribute(level) + "&7格方块,");
//         lore.add("   &7能打碎所有你前进方向的所有方块。");
//         lore.add("   &7所有被该技能击中的敌人将");
//         lore.add("   &7受到&8" + this.getAttribute(level - 1) + " ➜ &a" + this.getAttribute(level) + "&7点伤害。");
//         return lore;
//      }
//   }
//
//   @Override
//   public void upgrade(GamePlayer gamePlayer) {
//
//   }
//
//   @Override
//   public int getPlayerLevel(GamePlayer gamePlayer) {
//      return gamePlayer.getPlayerStats().getClassesStats(this.getClasses()).getSkillLevel();
//   }
//
//   public boolean use(final GamePlayer gamePlayer, ClassesStats classesStats) {
//      if (Mole.skillCooldown.getOrDefault(gamePlayer, 0) > 1) {
//         gamePlayer.getPlayer().sendMessage("&c技能正在冷却中！");
//      } else {
//         final Player player = gamePlayer.getPlayer();
//         final Location from = player.getLocation().clone();
//         final int level = classesStats.getSkillLevel();
//         new BukkitRunnable() {
//            int ticks = 0;
//            final List<Player> damaged = new ArrayList<>();
//
//            @Override
//            public void run() {
//               if (gamePlayer.isOnline() && !player.isDead() && player.getLocation().distance(from) <= MainSkill.this.getAttribute(level) && this.ticks < 60) {
//                  player.setVelocity(player.getEyeLocation().getDirection().multiply(0.8D));
//                  Iterator var1 = LocationUtils.getSphere(player.getLocation(), 2).iterator();
//
//                  while(true) {
//                     Block block1;
//                     do {
//                        do {
//                           if (!var1.hasNext()) {
//                              var1 = MainSkill.this.getNearbyPlayers(player, 5).iterator();
//
//                              while(var1.hasNext()) {
//                                 Player nearby = (Player)var1.next();
//                                 if (!this.damaged.contains(nearby)) {
//                                    nearby.damage(MainSkill.this.getAttribute(level), player);
//                                    this.damaged.add(nearby);
//                                 }
//                              }
//
//                              ++this.ticks;
//                              return;
//                           }
//
//                           block1 = (Block)var1.next();
//                        } while(GameManager.getInstance().getGameInfo().isUnbreakable(block1.getLocation()));
//                     } while((block1.getType() == Material.FURNACE || block1.getType() == Material.BURNING_FURNACE || block1.getType() == Material.TRAPPED_CHEST || block1.getType() == Material.BARRIER) && !gamePlayer.isProtectedBlock(block1));
//
//                     if (block1.getType() != Material.BEDROCK) {
//                        block1.breakNaturally();
//                        Mole.skillCooldown.put(gamePlayer, 1);
//                     }
//                  }
//               } else {
//                  player.setVelocity(new Vector(0.0D, 0.0D, 0.0D));
//                  this.cancel();
//               }
//            }
//         }.runTaskTimer(MiarsCore.getInstance(), 0L, 1L);
//         Mole.skillCooldown.put(gamePlayer, 2);
//      }
//      return true;
//   }
//
//   private List<Player> getNearbyPlayers(Player player, int radius) {
//      List<Player> players = new ArrayList<>();
//
//      for (Player other : PlayerUtils.getNearbyPlayers(player, radius)) {
//         GamePlayer gameOther = GamePlayerManager.getInstance().getGamePlayer(other);
//         if (!gameOther.isSpectator() && !GameManager.getInstance().getGamePlayerTeam(GamePlayerManager.getInstance().getGamePlayer(player)).isInTeam(gameOther)) {
//            players.add(other);
//         }
//      }
//
//      return players;
//   }
//}
