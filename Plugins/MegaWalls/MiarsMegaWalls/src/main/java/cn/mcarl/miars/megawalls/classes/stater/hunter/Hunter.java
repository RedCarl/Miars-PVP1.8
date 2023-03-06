package cn.mcarl.miars.megawalls.classes.stater.hunter;

import cn.mcarl.miars.megawalls.MiarsMegaWalls;
import cn.mcarl.miars.megawalls.classes.*;
import cn.mcarl.miars.megawalls.game.entitiy.GamePlayer;
import cn.mcarl.miars.megawalls.game.event.WallFallEvent;
import cn.mcarl.miars.megawalls.game.manager.GameManager;
import cn.mcarl.miars.megawalls.utils.ItemBuilder;
import cn.mcarl.miars.megawalls.utils.PlayerUtils;
import net.citizensnpcs.api.CitizensAPI;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

public class Hunter extends Classes {
   public static final Map<GamePlayer, Integer> skill = new HashMap();
   public static final Map<GamePlayer, Integer> skill2Cooldown = new HashMap();
   public static final Map<GamePlayer, Integer> skill3Cooldown = new HashMap();
   public static final Map<GamePlayer, String> skill3 = new HashMap();
   public static final Map<GamePlayer, Integer> skill3Seconds = new HashMap();

   public Hunter() {
      super("Hunter", "猎人", ChatColor.GREEN, Material.BOW, (byte)0, ClassesType.STATER, new ClassesInfo.Orientation[]{ClassesInfo.Orientation.REMOTE, ClassesInfo.Orientation.WARRIOR}, ClassesInfo.Difficulty.THREE);
      this.setMainSkill(new MainSkill(this));
      this.setSecondSkill(new SecondSkill(this));
      this.setThirdSkill(new ThirdSkill(this));
      this.setCollectSkill(new FourthSkill(this));
      this.setDefaultSkin("eyJ0aW1lc3RhbXAiOjE1NDk0Mzc4MTQ1NzMsInByb2ZpbGVJZCI6ImFkMWM2Yjk1YTA5ODRmNTE4MWJhOTgyMzY0OTllM2JkIiwicHJvZmlsZU5hbWUiOiJGdXJrYW5iejAwIiwic2lnbmF0dXJlUmVxdWlyZWQiOnRydWUsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS84NGVlMWYwNjYxMTg5ZmUwY2Q1OTA1ZjlhOTRlNDAyNDBlNWE1YjZiZmY3YjA5M2E1MDc2YzJkZGRmYTZkZDQ2In19fQ==", "XrI4Gs7fKBlZG8VV1+Eg5b8hY9P+I7ugNZe4MjDR+kdQNr8gzsBJvWn9l76gkx64XFA7IW7JT2D8OIDqOhn2X3nbxIm9ug0P+1K3A8pHDf9r9NrgMqKKajTHIz6LscYMfUCwpCJ/G0LWMzXUobxkYZnjNdN5ckHuVL178XGiy8yZ6aUUakGrfdej01O3oThc9YqcDgSpaiqFKzsuUBe1bK9rK1yAu4B4/An/nSoo7DDwehAxFm7RRmrk8eoSUyOgCjicZnp/hJcG+FY3AFs9SwFYn7TzJ+ddh85jSjqLQP+GUSMhtXOBxgWijMAilrew8Rp6WN3bgy241NBCnwYkq7pkf6O/QLBIWdlMALUNu6DuJLHYhl7jOEHLTN/BcyCOLl4Vrh+fqd5GT01yAvUyiMUg+cuEouxz31i3Ss5hley6+v2HIRGUFZpJba7hf7u0376XQukEVSif6FBWqsJMUXRbe04DZLY7gBJo46ZaEqeV0zgD9f4OSN8alUVYNe7ingvDH/mWvz7aDYpzfHditABxC/YH8GtOuypdQrVptSHyyF++Tm+O6EMb6QGQjAYQCzc9AxJvw8s601xOJd9Sd6fxNB2+d6oEnGNeAR52QBeG7EsvnrXLBfwTKQuW52n2/4tXV2AsjatJBg4auBy5oRp+czbJW5DBry3tqvy6AWQ=");
      this.setEquipmentPackage(new EquipmentPackage(this) {
          @Override
         public List<ItemStack> getEquipments(int level) {
            List<ItemStack> items = new ArrayList<>();
            items.add((new ItemBuilder(Material.COMPASS)).setDisplayName(Hunter.this.nameColor + Hunter.this.getDisplayName() + " 指南针").build());
            items.add((new ItemBuilder(Material.DIAMOND_PICKAXE)).setDisplayName(Hunter.this.nameColor + Hunter.this.getDisplayName() + " 镐").setUnbreakable(true).addEnchantment(Enchantment.DIG_SPEED, 3).addEnchantment(Enchantment.DURABILITY, 3).build());
            items.add((new ItemBuilder(Material.ENDER_CHEST)).setDisplayName(Hunter.this.nameColor + Hunter.this.getDisplayName() + " 末影箱").build());
              switch (level) {
                  case 1 -> {
                      items.add((new ItemBuilder(Material.IRON_SWORD)).setDisplayName(Hunter.this.nameColor + Hunter.this.getDisplayName() + " 剑").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                      items.add((new ItemBuilder(Material.BOW)).setDisplayName(Hunter.this.nameColor + Hunter.this.getDisplayName() + " 弓").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.ARROW_INFINITE, 1).build());
                      items.add((new ItemBuilder(Material.IRON_HELMET)).setDisplayName(Hunter.this.nameColor + Hunter.this.getDisplayName() + " 头盔").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                      items.add((new ItemBuilder(Material.IRON_BOOTS)).setDisplayName(Hunter.this.nameColor + Hunter.this.getDisplayName() + " 靴子").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                      items.add((new ItemBuilder(Material.GOLDEN_APPLE)).setDisplayName(Hunter.this.nameColor + Hunter.this.getDisplayName() + " 金苹果").build());
                      items.add((new ItemBuilder(Material.ARROW)).setDisplayName(Hunter.this.nameColor + Hunter.this.getDisplayName() + " 箭").build());
                      items.add((new ItemBuilder(Material.COOKED_BEEF)).setDisplayName(Hunter.this.nameColor + Hunter.this.getDisplayName() + " 牛排").build());
                  }
                  case 2 -> {
                      items.add((new ItemBuilder(Material.IRON_SWORD)).setDisplayName(Hunter.this.nameColor + Hunter.this.getDisplayName() + " 剑").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                      items.add((new ItemBuilder(Material.BOW)).setDisplayName(Hunter.this.nameColor + Hunter.this.getDisplayName() + " 弓").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.ARROW_INFINITE, 1).build());
                      items.add((new ItemBuilder(Material.IRON_HELMET)).setDisplayName(Hunter.this.nameColor + Hunter.this.getDisplayName() + " 头盔").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_PROJECTILE, 1).build());
                      items.add((new ItemBuilder(Material.IRON_BOOTS)).setDisplayName(Hunter.this.nameColor + Hunter.this.getDisplayName() + " 靴子").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).build());
                      items.add((new ItemBuilder(Material.GOLDEN_APPLE)).setDisplayName(Hunter.this.nameColor + Hunter.this.getDisplayName() + " 金苹果").build());
                      items.add((new ItemBuilder(Material.ARROW)).setDisplayName(Hunter.this.nameColor + Hunter.this.getDisplayName() + " 箭").build());
                      items.add((new ItemBuilder(Material.COOKED_BEEF, 2)).setDisplayName(Hunter.this.nameColor + Hunter.this.getDisplayName() + " 牛排").build());
                  }
                  case 3 -> {
                      items.add((new ItemBuilder(Material.IRON_SWORD)).setDisplayName(Hunter.this.nameColor + Hunter.this.getDisplayName() + " 剑").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                      items.add((new ItemBuilder(Material.BOW)).setDisplayName(Hunter.this.nameColor + Hunter.this.getDisplayName() + " 弓").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.ARROW_DAMAGE, 1).addEnchantment(Enchantment.ARROW_INFINITE, 1).build());
                      items.add((new ItemBuilder(Material.IRON_HELMET)).setDisplayName(Hunter.this.nameColor + Hunter.this.getDisplayName() + " 头盔").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).addEnchantment(Enchantment.PROTECTION_PROJECTILE, 1).build());
                      items.add((new ItemBuilder(Material.IRON_BOOTS)).setDisplayName(Hunter.this.nameColor + Hunter.this.getDisplayName() + " 靴子").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).build());
                      items.add((new ItemBuilder(Material.GOLDEN_APPLE, 2)).setDisplayName(Hunter.this.nameColor + Hunter.this.getDisplayName() + " 金苹果").build());
                      items.add((new ItemBuilder(Material.ARROW)).setDisplayName(Hunter.this.nameColor + Hunter.this.getDisplayName() + " 箭").build());
                      items.add((new ItemBuilder(Material.COOKED_BEEF, 2)).setDisplayName(Hunter.this.nameColor + Hunter.this.getDisplayName() + " 牛排").build());
                  }
                  case 4 -> {
                      items.add((new ItemBuilder(Material.IRON_SWORD)).setDisplayName(Hunter.this.nameColor + Hunter.this.getDisplayName() + " 剑").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                      items.add((new ItemBuilder(Material.BOW)).setDisplayName(Hunter.this.nameColor + Hunter.this.getDisplayName() + " 弓").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.ARROW_DAMAGE, 1).addEnchantment(Enchantment.ARROW_INFINITE, 1).build());
                      items.add((new ItemBuilder(Material.DIAMOND_HELMET)).setDisplayName(Hunter.this.nameColor + Hunter.this.getDisplayName() + " 头盔").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).addEnchantment(Enchantment.PROTECTION_PROJECTILE, 1).build());
                      items.add((new ItemBuilder(Material.IRON_BOOTS)).setDisplayName(Hunter.this.nameColor + Hunter.this.getDisplayName() + " 靴子").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2).build());
                      items.add((new ItemBuilder(Material.GOLDEN_APPLE, 2)).setDisplayName(Hunter.this.nameColor + Hunter.this.getDisplayName() + " 金苹果").build());
                      items.add((new ItemBuilder(Material.ARROW)).setDisplayName(Hunter.this.nameColor + Hunter.this.getDisplayName() + " 箭").build());
                      items.add((new ItemBuilder(Material.COOKED_BEEF, 3)).setDisplayName(Hunter.this.nameColor + Hunter.this.getDisplayName() + " 牛排").build());
                  }
                  case 5 -> {
                      items.add((new ItemBuilder(Material.IRON_SWORD)).setDisplayName(Hunter.this.nameColor + Hunter.this.getDisplayName() + " 剑").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.DAMAGE_ALL, 1).build());
                      items.add((new ItemBuilder(Material.BOW)).setDisplayName(Hunter.this.nameColor + Hunter.this.getDisplayName() + " 弓").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.ARROW_DAMAGE, 1).addEnchantment(Enchantment.ARROW_INFINITE, 1).build());
                      items.add((new ItemBuilder(Material.DIAMOND_HELMET)).setDisplayName(Hunter.this.nameColor + Hunter.this.getDisplayName() + " 头盔").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2).addEnchantment(Enchantment.PROTECTION_PROJECTILE, 2).build());
                      items.add((new ItemBuilder(Material.IRON_BOOTS)).setDisplayName(Hunter.this.nameColor + Hunter.this.getDisplayName() + " 靴子").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2).build());
                      items.add((new ItemBuilder(Material.GOLDEN_APPLE, 3)).setDisplayName(Hunter.this.nameColor + Hunter.this.getDisplayName() + " 金苹果").build());
                      items.add((new ItemBuilder(Material.ARROW)).setDisplayName(Hunter.this.nameColor + Hunter.this.getDisplayName() + " 箭").build());
                      items.add((new ItemBuilder(Material.COOKED_BEEF, 3)).setDisplayName(Hunter.this.nameColor + Hunter.this.getDisplayName() + " 牛排").build());
                      items.add((new ItemBuilder(Material.CARROT_STICK)).setDisplayName(Hunter.this.nameColor + Hunter.this.getDisplayName() + " 胡萝卜钓竿").build());
                  }
              }

            return items;
         }

         @Override
         public List<String> getInfo(int level) {
            List<String> lore = new ArrayList<>();
             switch (level) {
                 case 1 -> {
                     lore.add(" &8▪ &7铁 剑");
                     lore.add("    &8▪ 耐久 X");
                     lore.add(" &8▪ &7弓");
                     lore.add("    &8▪ 耐久 X");
                     lore.add("    &8▪ 无限 I");
                     lore.add(" &8▪ &7铁 头盔");
                     lore.add("    &8▪ 耐久 X");
                     lore.add(" &8▪ &7铁 靴子");
                     lore.add("    &8▪ 耐久 X");
                     lore.add(" &8▪ &7金苹果");
                     lore.add(" &8▪ &7箭");
                     lore.add(" &8▪ &7牛排");
                 }
                 case 2 -> {
                     lore.add(" &8▪ &7铁 头盔");
                     lore.add("    &8▪ 耐久 X");
                     lore.add("    &a+ 弹射物保护 I");
                     lore.add(" &8▪ &7铁 靴子");
                     lore.add("    &8▪ 耐久 X");
                     lore.add("    &a+ 保护 I");
                     lore.add(" &8▪ &7牛排 &8x1 ➜ &ax2");
                 }
                 case 3 -> {
                     lore.add(" &8▪ &7弓");
                     lore.add("    &8▪ 耐久 X");
                     lore.add("    &a+ 力量 I");
                     lore.add("    &8▪ 无限 I");
                     lore.add(" &8▪ &7铁 头盔");
                     lore.add("    &8▪ 耐久 X");
                     lore.add("    &a+ 保护 I");
                     lore.add("    &8▪ 弹射物保护 I");
                     lore.add(" &8▪ &7金苹果 &8x1 ➜ &ax2");
                 }
                 case 4 -> {
                     lore.add(" &8▪ &7铁 &8➜ &a钻石 &7头盔");
                     lore.add("    &8▪ 耐久 X");
                     lore.add("    &8▪ 保护 I");
                     lore.add("    &8▪ 弹射物保护 I");
                     lore.add(" &8▪ &7铁 靴子");
                     lore.add("    &8▪ 耐久 X");
                     lore.add("    &8▪ 保护 I ➜ &aII");
                     lore.add(" &8▪ &7牛排 &8x2 ➜ &ax3");
                 }
                 case 5 -> {
                     lore.add(" &8▪ &7铁 剑");
                     lore.add("    &8▪ 耐久 X");
                     lore.add("    &a+ 锋利 I");
                     lore.add(" &8▪ &7钻石 头盔");
                     lore.add("    &8▪ 耐久 X");
                     lore.add("    &8▪ 保护 I ➜ &aII");
                     lore.add("    &8▪ 弹射物保护 I ➜ &aII");
                     lore.add(" &8▪ &7金苹果 &8x2 ➜ &ax3");
                     lore.add(" &a+ &7胡萝卜钓竿");
                 }
             }

            return lore;
         }
      });
   }

   @Override
   public List<String> getInfo() {
      List<String> lore = new ArrayList<>();
      lore.add("&7你喜欢精准箭法.");
      lore.add("&7你想要一些小宠物来帮助你.");
      lore.add("&7你想拥有夜视能力.");
      return lore;
   }

   @Override
   public int unlockCost() {
      return 40000;
   }

   @Override
   public int energyMelee() {
      return 4;
   }

   @Override
   public int energyBow() {
      return 8;
   }

   public String getSkillTip(GamePlayer gamePlayer) {
      return this.mainSkill.getSkillTip(gamePlayer) + " " + this.secondSkill.getSkillTip(gamePlayer) + " " + this.thirdSkill.getSkillTip(gamePlayer);
   }

   public void run() {
      GamePlayer.getOnlinePlayers().stream().filter((gamePlayer) -> !gamePlayer.isSpectator() && ClassesManager.getSelected(gamePlayer).equals(this)).forEach((gamePlayer) -> {
         if (skill.getOrDefault(gamePlayer, 0) > 0) {
            skill.put(gamePlayer, skill.get(gamePlayer) - 1);
            if (skill.get(gamePlayer) == 0) {
               gamePlayer.sendMessage("&a你的鹰眼效果已失效。");
            }
         }

         if (skill2Cooldown.getOrDefault(gamePlayer, 0) > 0) {
            skill2Cooldown.put(gamePlayer, skill2Cooldown.get(gamePlayer) - 1);
         }

         if (skill3Cooldown.getOrDefault(gamePlayer, (int)this.thirdSkill.getAttribute(this.thirdSkill.getPlayerLevel(gamePlayer))) > 0) {
            skill3Cooldown.put(gamePlayer, skill3Cooldown.getOrDefault(gamePlayer, (int)this.thirdSkill.getAttribute(this.thirdSkill.getPlayerLevel(gamePlayer))) - 1);
            if (skill3Cooldown.get(gamePlayer) == 0) {
               this.thirdSkill.use(gamePlayer, gamePlayer.getPlayerStats().getClassesStats(this));
            }
         }

         if (skill3Seconds.getOrDefault(gamePlayer, 0) > 0) {
            skill3Seconds.put(gamePlayer, skill3Seconds.get(gamePlayer) - 1);
            if (skill3Seconds.get(gamePlayer) == 0) {
               skill3.remove(gamePlayer);
            }
         }

         gamePlayer.sendActionBar(this.getSkillTip(gamePlayer));
         gamePlayer.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0));
      });
   }

   @EventHandler
   public void onWallFall(WallFallEvent e) {
      ThirdSkill.potions.remove("急迫I 15秒");
   }

   @EventHandler
   public void onProjectileLaunch(final ProjectileLaunchEvent e) {
      if (e.getEntity() instanceof Arrow && e.getEntity().getShooter() instanceof Player) {
         final GamePlayer gamePlayer = GamePlayer.get(((Player)e.getEntity().getShooter()).getUniqueId());
         Classes classes = ClassesManager.getSelected(gamePlayer);
         if (classes.equals(this)) {
            if (e.getEntity().getType() == EntityType.ARROW) {
               e.getEntity().setMetadata("Arrow", MiarsMegaWalls.getFixedMetadataValue());
            }

            boolean max = e.getEntity().getVelocity().getX() > 2.0D || e.getEntity().getVelocity().getX() < -2.0D;

             if (e.getEntity().getVelocity().getY() > 2.0D || e.getEntity().getVelocity().getY() < -2.0D) {
               max = true;
            }

            if (e.getEntity().getVelocity().getZ() > 2.0D || e.getEntity().getVelocity().getZ() < -2.0D) {
               max = true;
            }

            if (skill.getOrDefault(gamePlayer, 0) > 0 && max) {
               e.getEntity().setMetadata("NoAddEnergy", MiarsMegaWalls.getFixedMetadataValue());
               new BukkitRunnable() {
                   @Override
                  public void run() {
                     if (gamePlayer.isOnline() && !e.getEntity().isDead() && !e.getEntity().isOnGround()) {
                        Player target = null;
                        Iterator<Player> var2 = PlayerUtils.getNearbyPlayers(e.getEntity().getLocation(), 8.0D).iterator();

                        while(true) {
                           Player other;
                           GamePlayer gameOther;
                           do {
                              do {
                                 do {
                                    if (!var2.hasNext()) {
                                       if (target != null) {
                                          Location loc = e.getEntity().getLocation().clone();
                                          Location targetLoc = target.getLocation().clone();
                                          e.getEntity().setVelocity((new Vector(targetLoc.getX() - loc.getX(), targetLoc.getY() - loc.getY(), targetLoc.getZ() - loc.getZ())).multiply(0.8D));
                                          PlayerUtils.heal(gamePlayer.getPlayer(), 0.5D);
                                       }

                                       return;
                                    }

                                    other = (Player)var2.next();
                                    gameOther = GamePlayer.get(other.getUniqueId());
                                 } while(Objects.requireNonNull(gameOther).isSpectator());
                              } while(GameManager.getInstance().getGamePlayerTeam(gamePlayer).isInTeam(gameOther));
                           } while(target != null && other.getLocation().distance(e.getEntity().getLocation()) >= target.getLocation().distance(e.getEntity().getLocation()));

                           target = other;
                        }
                     } else {
                        this.cancel();
                     }
                  }
               }.runTaskTimer(MiarsMegaWalls.getInstance(), 0L, 1L);
            }
         }
      }

   }

   @EventHandler(
           priority = EventPriority.LOWEST
   )
   public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
      if (!CitizensAPI.getNPCRegistry().isNPC(e.getEntity()) && !e.isCancelled() && e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
         GamePlayer player = GamePlayer.get(e.getEntity().getUniqueId());
         if (player == null) {
            return;
         }

         if (player.isSpectator() || GameManager.getInstance().getGamePlayerTeam(player).isInTeam(GamePlayer.get(e.getDamager().getUniqueId()))) {
            return;
         }

         Classes classes = ClassesManager.getSelected(player);
         if (classes.equals(this)) {
            this.secondSkill.use(player, player.getPlayerStats().getClassesStats(classes));
            //startAddEnergyTimer(player, 5, 3);
         }
      }

   }
}
