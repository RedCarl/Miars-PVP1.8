//package cn.mcarl.miars.megawalls.classes.mythic.mole;
//
//import cn.mcarl.miars.megawalls.classes.*;
//import cn.mcarl.miars.megawalls.game.entitiy.GamePlayer;
//import cn.mcarl.miars.megawalls.game.manager.GameManager;
//import cn.mcarl.miars.megawalls.game.manager.GamePlayerManager;
//import cn.mcarl.miars.megawalls.utils.ItemBuilder;
//import org.bukkit.ChatColor;
//import org.bukkit.Material;
//import org.bukkit.enchantments.Enchantment;
//import org.bukkit.event.EventHandler;
//import org.bukkit.event.EventPriority;
//import org.bukkit.event.player.PlayerItemConsumeEvent;
//import org.bukkit.inventory.ItemStack;
//import org.bukkit.potion.PotionEffect;
//import org.bukkit.potion.PotionEffectType;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class Mole extends Classes {
//   public static final Map<GamePlayer, Integer> skillCooldown = new HashMap();
//
//   public Mole() {
//      super("Mole", "鼹鼠", ChatColor.YELLOW, Material.GOLD_SPADE, (byte)0, ClassesType.MYTHIC, new ClassesInfo.Orientation[]{ClassesInfo.Orientation.AGILITY, ClassesInfo.Orientation.CONTROL}, ClassesInfo.Difficulty.THREE);
//      this.setMainSkill(new MainSkill(this));
//      this.setSecondSkill(new SecondSkill(this));
//      this.setThirdSkill(new ThirdSkill(this));
//      this.setCollectSkill(new FourthSkill(this));
//      this.setDefaultSkin("eyJ0aW1lc3RhbXAiOjE1NDg4MTY2MjY2NTUsInByb2ZpbGVJZCI6IjdkYTJhYjNhOTNjYTQ4ZWU4MzA0OGFmYzNiODBlNjhlIiwicHJvZmlsZU5hbWUiOiJHb2xkYXBmZWwiLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzlkNTdjOGI1NzBmYjRkOGRiYWQ2YjU3MjM3MjMwMzQ3ZjdhZjlhOTM4ZGExMDY5NGI5MmIwYmE1NDdiMTgxODYifX19", "lbdM3m3EL7PRLk1sqUKX7/6RbOgJ7KPSAn+xZN5hmHUFSfwiWpvIWcgziSfG4/6QpWzk2hwkC5zX1olHDNS/8LQPh/4iNay8OIJsKUIBh3EnUW2P3Ft5J0ibEqyeNbMQ3L+eAZ++KUjtjDEmwwrkMzian7sknn8LLfKusecwm/PQUWyY6Sgm883jA5rcjtZsewnW0gQ3ZpIaxbeRI7o52EiY7NCkeIyE0wvJZ3XY0bBAPVOX85IFf8GsgKeeg6XhXnc/vZHfQOLrezoXygBrQOm/1/wppK2fFpcERmkLm5VN9ECdcbt9Mt+S3rGM4XBBcwTTT+OgNczI4CZzPyu1mT5ijF3JY3+U1pyRBGpggXcT0czFeutGnbyLKnSmEBKjHTIkmE4n2CjVasEbePAB/Vx3pWg+qZ40yhIelPLhEv36HjPhRZ6KH+zM1+85R3uhcVWbFYT1cYv0+e+P4CEJPq4bpzHA/LZTkagxIWEa1CS+Ffzuft55TW5R5CRKBviiI2x6klU2uvtxa12JiRE8TyujYjRZP5OxSQ9uXybKM6f6PWigKVQoRN32dsjwXoT83IPQYpkpfkT/AehtFM1DYaDVgsXPrZyEAxddYCyrwR43o2qgndpS/4qi/g6M3LTjF03sYnGVy1CPAuvaW2TSauLuplgmqpw1Kcojo3nnJWQ=");
//      this.setEquipmentPackage(new EquipmentPackage(this) {
//         @Override
//         public List<ItemStack> getEquipments(int level) {
//            List<ItemStack> items = new ArrayList();
//            items.add((new ItemBuilder(Material.COMPASS)).setDisplayName(Mole.this.nameColor + Mole.this.getDisplayName() + " 指南针").build());
//            items.add((new ItemBuilder(Material.DIAMOND_PICKAXE)).setDisplayName(Mole.this.nameColor + Mole.this.getDisplayName() + " 镐").setUnbreakable(true).addEnchantment(Enchantment.DIG_SPEED, 3).addEnchantment(Enchantment.DURABILITY, 3).build());
//            items.add((new ItemBuilder(Material.ENDER_CHEST)).setDisplayName(Mole.this.nameColor + Mole.this.getDisplayName() + " 末影箱").build());
//            switch(level) {
//               case 1:
//                  items.add((new ItemBuilder(Material.GOLD_HELMET)).setDisplayName(Mole.this.nameColor + Mole.this.getDisplayName() + " 头盔").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).build());
//                  items.add((new ItemBuilder(Material.IRON_LEGGINGS)).setDisplayName(Mole.this.nameColor + Mole.this.getDisplayName() + " 护腿").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).build());
//                  items.add((new ItemBuilder(Material.POTION, 1, (byte)5)).setDisplayName(Mole.this.nameColor + Mole.this.getDisplayName() + " 治疗药水 (2❤ 和 治疗 I)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 0)).build());
//                  items.add((new ItemBuilder(Material.IRON_SPADE)).setDisplayName(Mole.this.nameColor + Mole.this.getDisplayName() + " 锹").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.DIG_SPEED, 1).addEnchantment(Enchantment.DAMAGE_ALL, 3).build());
//                  items.add((new ItemBuilder(Material.COOKED_BEEF, 1)).setDisplayName(Mole.this.nameColor + Mole.this.getDisplayName() + " 牛排").build());
//                  break;
//               case 2:
//                  items.add((new ItemBuilder(Material.GOLD_HELMET)).setDisplayName(Mole.this.nameColor + Mole.this.getDisplayName() + " 头盔").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).build());
//                  items.add((new ItemBuilder(Material.IRON_LEGGINGS)).setDisplayName(Mole.this.nameColor + Mole.this.getDisplayName() + " 护腿").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3).build());
//                  items.add((new ItemBuilder(Material.POTION, 1, (byte)5)).setDisplayName(Mole.this.nameColor + Mole.this.getDisplayName() + " 治疗药水 (6❤ 和 治疗 I)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 1)).build());
//                  items.add((new ItemBuilder(Material.IRON_SPADE)).setDisplayName(Mole.this.nameColor + Mole.this.getDisplayName() + " 锹").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.DIG_SPEED, 2).addEnchantment(Enchantment.DAMAGE_ALL, 3).build());
//                  items.add((new ItemBuilder(Material.COOKED_BEEF, 2)).setDisplayName(Mole.this.nameColor + Mole.this.getDisplayName() + " 牛排").build());
//                  break;
//               case 3:
//                  items.add((new ItemBuilder(Material.GOLD_HELMET)).setDisplayName(Mole.this.nameColor + Mole.this.getDisplayName() + " 头盔").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).build());
//                  items.add((new ItemBuilder(Material.DIAMOND_LEGGINGS)).setDisplayName(Mole.this.nameColor + Mole.this.getDisplayName() + " 护腿").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).build());
//                  items.add((new ItemBuilder(Material.POTION, 2, (byte)5)).setDisplayName(Mole.this.nameColor + Mole.this.getDisplayName() + " 治疗药水 (6❤ 和 治疗 I)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 1)).build());
//                  items.add((new ItemBuilder(Material.DIAMOND_SPADE)).setDisplayName(Mole.this.nameColor + Mole.this.getDisplayName() + " 锹").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.DIG_SPEED, 1).addEnchantment(Enchantment.DAMAGE_ALL, 2).build());
//                  items.add((new ItemBuilder(Material.COOKED_BEEF, 2)).setDisplayName(Mole.this.nameColor + Mole.this.getDisplayName() + " 牛排").build());
//                  break;
//               case 4:
//                  items.add((new ItemBuilder(Material.GOLD_HELMET)).setDisplayName(Mole.this.nameColor + Mole.this.getDisplayName() + " 头盔").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).build());
//                  items.add((new ItemBuilder(Material.DIAMOND_LEGGINGS)).setDisplayName(Mole.this.nameColor + Mole.this.getDisplayName() + " 护腿").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2).build());
//                  items.add((new ItemBuilder(Material.POTION, 2, (byte)5)).setDisplayName(Mole.this.nameColor + Mole.this.getDisplayName() + " 治疗药水 (8❤ 和 治疗 I)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 2)).build());
//                  items.add((new ItemBuilder(Material.DIAMOND_SPADE)).setDisplayName(Mole.this.nameColor + Mole.this.getDisplayName() + " 锹").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.DIG_SPEED, 1).addEnchantment(Enchantment.DAMAGE_ALL, 2).build());
//                  items.add((new ItemBuilder(Material.COOKED_BEEF, 3)).setDisplayName(Mole.this.nameColor + Mole.this.getDisplayName() + " 牛排").build());
//                  break;
//               case 5:
//                  items.add((new ItemBuilder(Material.GOLD_HELMET)).setDisplayName(Mole.this.nameColor + Mole.this.getDisplayName() + " 头盔").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).build());
//                  items.add((new ItemBuilder(Material.DIAMOND_LEGGINGS)).setDisplayName(Mole.this.nameColor + Mole.this.getDisplayName() + " 护腿").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3).build());
//                  items.add((new ItemBuilder(Material.POTION, 2, (byte)5)).setDisplayName(Mole.this.nameColor + Mole.this.getDisplayName() + " 治疗药水 (8❤ 和 治疗 I)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 2)).build());
//                  items.add((new ItemBuilder(Material.DIAMOND_SPADE)).setDisplayName(Mole.this.nameColor + Mole.this.getDisplayName() + " 锹").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.DIG_SPEED, 2).addEnchantment(Enchantment.DAMAGE_ALL, 2).build());
//                  items.add((new ItemBuilder(Material.COOKED_BEEF, 3)).setDisplayName(Mole.this.nameColor + Mole.this.getDisplayName() + " 牛排").build());
//            }
//
//            return items;
//         }
//
//         @Override
//         public List<String> getInfo(int level) {
//            List<String> lore = new ArrayList();
//            switch(level) {
//               case 1:
//                  lore.add(" &8▪ &7金 头盔");
//                  lore.add("    &8▪ 耐久 X");
//                  lore.add("    &8▪ 保护 I");
//                  lore.add(" &8▪ &7铁 护腿");
//                  lore.add("    &8▪ 耐久 X");
//                  lore.add("    &8▪ 保护 I");
//                  lore.add(" &8▪ &7治疗药水 2❤ 和 治疗 I");
//                  lore.add(" &8▪ &7铁 锹");
//                  lore.add("    &8▪ 耐久 X");
//                  lore.add("    &8▪ 效率 I");
//                  lore.add("    &8▪ 锋利 III");
//                  lore.add(" &8▪ &7牛排");
//                  break;
//               case 2:
//                  lore.add(" &8▪ &7铁 护腿");
//                  lore.add("    &8▪ 耐久 X");
//                  lore.add("    &8▪ 保护 I ➜ &aIII");
//                  lore.add(" &8▪ &7治疗药水 2❤ ➜ &a6❤");
//                  lore.add(" &8▪ &7铁 锹");
//                  lore.add("    &8▪ 耐久 X");
//                  lore.add("    &8▪ 效率 I ➜ &aII");
//                  lore.add("    &8▪ 锋利 III");
//                  lore.add(" &8▪ &7牛排 &8x1 ➜ &ax2");
//                  break;
//               case 3:
//                  lore.add(" &8▪ &7铁 &8➜ &a钻石 &7护腿");
//                  lore.add("    &8▪ 耐久 X");
//                  lore.add("    &8▪ 保护 III ➜ &aI");
//                  lore.add(" &8▪ &7治疗药水 6❤ &8x1 ➜ &ax2");
//                  lore.add(" &a+ &7钻石 锹");
//                  lore.add("    &8▪ 耐久 X");
//                  lore.add("    &8▪ 效率 I");
//                  lore.add("    &8▪ 锋利 II");
//                  break;
//               case 4:
//                  lore.add(" &8▪ &7钻石 护腿");
//                  lore.add("    &8▪ 耐久 X");
//                  lore.add("    &8▪ 保护 I ➜ &aII");
//                  lore.add(" &8▪ &7治疗药水 6❤ ➜ &a8❤");
//                  lore.add(" &8▪ &7牛排 &8x2 ➜ &ax3");
//                  break;
//               case 5:
//                  lore.add(" &8▪ &7钻石 护腿");
//                  lore.add("    &8▪ 耐久 X");
//                  lore.add("    &8▪ 保护 II ➜ &aIII");
//                  lore.add(" &8▪ &7钻石 锹");
//                  lore.add("    &8▪ 耐久 X");
//                  lore.add("    &8▪ 效率 I ➜ &aII");
//                  lore.add("    &8▪ 锋利 II");
//            }
//
//            return lore;
//         }
//      });
//   }
//
//   @Override
//   public List<String> getInfo() {
//      List<String> lore = new ArrayList();
//      lore.add("&7你喜欢用锹战斗.");
//      lore.add("&7你想要学会TNT特技.");
//      lore.add("&7你想在战前准备好一切.");
//      return lore;
//   }
//
//   @Override
//   public int unlockCost() {
//      return 3;
//   }
//
//   @Override
//   public int energyMelee() {
//      return 10;
//   }
//
//   @Override
//   public int energyBow() {
//      return 10;
//   }
//
//   public String getSkillTip(GamePlayer gamePlayer) {
//      return this.mainSkill.getSkillTip(gamePlayer);
//   }
//
//   public void run() {
//      GamePlayer.getOnlinePlayers().stream().filter((gamePlayer) -> {
//         return !gamePlayer.isSpectator() && ClassesManager.getSelected(gamePlayer).equals(this);
//      }).forEach((gamePlayer) -> {
//         gamePlayer.sendActionBar(this.getSkillTip(gamePlayer));
//         if (GameManager.getInstance().getGameInfo().isWitherFury()) {
//            gamePlayer.addEnergy(5, PlayerEnergyChangeEvent.ChangeReason.MAGIC);
//         } else {
//            gamePlayer.addEnergy(3, PlayerEnergyChangeEvent.ChangeReason.MAGIC);
//         }
//
//      });
//   }
//
//   @EventHandler(
//           priority = EventPriority.LOWEST
//   )
//   public void onPlayerItemConsume(PlayerItemConsumeEvent e) {
//      if (e.getItem().getType() == Material.POTION) {
//         GamePlayer gamePlayer = GamePlayerManager.getInstance().getGamePlayer(e.getPlayer());
//         Classes classes = ClassesManager.getSelected(gamePlayer);
//         if (classes.equals(this) && e.getItem().hasItemMeta() && e.getItem().getItemMeta() != null && e.getItem().getItemMeta().getDisplayName() != null && e.getItem().getItemMeta().getDisplayName().contains("治疗药水")) {
//            e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 60, 0));
//         }
//      }
//
//   }
//}
