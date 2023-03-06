package cn.mcarl.miars.megawalls.classes.stater.cow;

import cn.mcarl.miars.megawalls.classes.*;
import cn.mcarl.miars.megawalls.game.entitiy.GamePlayer;
import cn.mcarl.miars.megawalls.utils.ItemBuilder;
import cn.mcarl.miars.megawalls.utils.PlayerUtils;
import net.citizensnpcs.api.CitizensAPI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cow extends Classes {
   public static final Map<GamePlayer, Integer> skill2Cooldown = new HashMap<>();
   public static final Map<GamePlayer, Integer> skill2Damage = new HashMap<>();

   public Cow() {
      super("Cow", "牛", ChatColor.LIGHT_PURPLE, Material.MILK_BUCKET, (byte)0, ClassesType.STATER, new ClassesInfo.Orientation[]{ClassesInfo.Orientation.TANK, ClassesInfo.Orientation.ASSIST}, ClassesInfo.Difficulty.TWO);
      this.setMainSkill(new MainSkill(this));
      this.setSecondSkill(new SecondSkill(this));
      this.setThirdSkill(new ThirdSkill(this));
      this.setCollectSkill(new FourthSkill(this));
      this.setDefaultSkin("eyJ0aW1lc3RhbXAiOjE1NDk0MzY4MDcwMDQsInByb2ZpbGVJZCI6ImQ5MjEyMTRkYTBkYjQ3MGJhOThmZWMxNzNjYTBmNWY0IiwicHJvZmlsZU5hbWUiOiJrYXNkb18iLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzUxYTBkOGIwOGMzNWNhZTI0YmRlMGE3NDdlMmY0MGQzZDU4NGY5OGY3Mzk4ZWY2ZjMzMzRiMGM4NDFjMTkxODkifX19", "jxQCtzpkHbujfZEKMlTXSKIn9D1/1KLqYfuGR+Lgqf6mS4vzkKyL+29i92wiUkWCXS1H3MFKHtAYdOsNPhL/pLzfzHyXkT8RAhKzbH4z4UL2V08uymXKIef5VnjD/Qok4az/IWby6HFFTEH7NedwutLQ+fNZ8DyV2BIfQp9ZHc92l444fPy9TZx9hBXrNnkzs5yYO1XDPeBDfutiD9flen8W2F6EnjgsHg838ACh5zYa0kU7tatHEUC0gPFNEVlDD+TwF3Y/6i3RKpLu8ufuMhBDRHDU2kn0DGvkEtXSo5ruUp+pDiR460eV4LbPaxX1lN3vjxSjhTb7RmGU4POKhDEJ7XW9BjvGKECnNxyOs9VTt9ydLHNrg78gl3Nr12CoGYq3nKSretbzjlxTIehzqxpJbWytdDMZ5R8AeqMAQtaaHCueKOOXFjC3Ym29/B764Dku834XujpN3tBlyMWGHYORp+4HO5u6ijNt9lFedhezhlWHSH4UH3i2J0HjiUJ/98pvpJGFw5P4bXGwjL/IT1JK2d+CoOgU4CuDo6MBWZTGXxDqo/ov2Xnkvyp7u2j79VN80RM3M+79YEoPvzoLozPUOS9dc+AZppCQVlTi7df0RuBurh364QTqMOPhX0SaiYoNT+U4zFsYSVpVWhoFk0TR+Sw93XEPV3c8EumsNGc=");
      this.setEquipmentPackage(new EquipmentPackage(this) {
         @Override
         public List<ItemStack> getEquipments(int level) {
            List<ItemStack> items = new ArrayList<>();
            items.add((new ItemBuilder(Material.COMPASS)).setDisplayName(Cow.this.nameColor + Cow.this.getDisplayName() + " 指南针").build());
            items.add((new ItemBuilder(Material.DIAMOND_PICKAXE)).setDisplayName(Cow.this.nameColor + Cow.this.getDisplayName() + " 镐").setUnbreakable(true).addEnchantment(Enchantment.DIG_SPEED, 3).addEnchantment(Enchantment.DURABILITY, 3).build());
            items.add((new ItemBuilder(Material.ENDER_CHEST)).setDisplayName(Cow.this.nameColor + Cow.this.getDisplayName() + " 末影箱").build());
            switch (level) {
               case 1 -> {
                  items.add((new ItemBuilder(Material.IRON_SWORD)).setDisplayName(Cow.this.nameColor + Cow.this.getDisplayName() + " 剑").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                  items.add((new ItemBuilder(Material.IRON_CHESTPLATE)).setDisplayName(Cow.this.nameColor + Cow.this.getDisplayName() + " 胸甲").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                  items.add((new ItemBuilder(Material.MILK_BUCKET)).build());
                  items.add((new ItemBuilder(Material.BREAD, 2)).setDisplayName(Cow.this.nameColor + Cow.this.getDisplayName() + " 面包").build());
                  items.add((new ItemBuilder(Material.POTION, 1, (byte) 5)).setDisplayName(Cow.this.nameColor + Cow.this.getDisplayName() + " 治疗药水 (8❤)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 2)).build());
               }
               case 2 -> {
                  items.add((new ItemBuilder(Material.IRON_SWORD)).setDisplayName(Cow.this.nameColor + Cow.this.getDisplayName() + " 剑").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                  items.add((new ItemBuilder(Material.IRON_CHESTPLATE)).setDisplayName(Cow.this.nameColor + Cow.this.getDisplayName() + " 胸甲").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).build());
                  items.add((new ItemBuilder(Material.MILK_BUCKET)).build());
                  items.add((new ItemBuilder(Material.BREAD, 4)).setDisplayName(Cow.this.nameColor + Cow.this.getDisplayName() + " 面包").build());
                  items.add((new ItemBuilder(Material.POTION, 1, (byte) 5)).setDisplayName(Cow.this.nameColor + Cow.this.getDisplayName() + " 治疗药水 (8❤)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 2)).build());
                  items.add((new ItemBuilder(Material.POTION, 1, (byte) 2)).setDisplayName(Cow.this.nameColor + Cow.this.getDisplayName() + " 速度药水 II (0:15秒)").addPotion(new PotionEffect(PotionEffectType.SPEED, 300, 1)).build());
               }
               case 3 -> {
                  items.add((new ItemBuilder(Material.IRON_SWORD)).setDisplayName(Cow.this.nameColor + Cow.this.getDisplayName() + " 剑").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                  items.add((new ItemBuilder(Material.IRON_CHESTPLATE)).setDisplayName(Cow.this.nameColor + Cow.this.getDisplayName() + " 胸甲").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2).build());
                  items.add((new ItemBuilder(Material.MILK_BUCKET, 2)).build());
                  items.add((new ItemBuilder(Material.BREAD, 4)).setDisplayName(Cow.this.nameColor + Cow.this.getDisplayName() + " 面包").build());
                  items.add((new ItemBuilder(Material.POTION, 1, (byte) 5)).setDisplayName(Cow.this.nameColor + Cow.this.getDisplayName() + " 治疗药水 (8❤)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 2)).build());
                  items.add((new ItemBuilder(Material.POTION, 2, (byte) 2)).setDisplayName(Cow.this.nameColor + Cow.this.getDisplayName() + " 速度药水 II (0:15秒)").addPotion(new PotionEffect(PotionEffectType.SPEED, 300, 1)).build());
               }
               case 4 -> {
                  items.add((new ItemBuilder(Material.IRON_SWORD)).setDisplayName(Cow.this.nameColor + Cow.this.getDisplayName() + " 剑").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                  items.add((new ItemBuilder(Material.IRON_CHESTPLATE)).setDisplayName(Cow.this.nameColor + Cow.this.getDisplayName() + " 胸甲").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3).build());
                  items.add((new ItemBuilder(Material.MILK_BUCKET, 2)).build());
                  items.add((new ItemBuilder(Material.BREAD, 6)).setDisplayName(Cow.this.nameColor + Cow.this.getDisplayName() + " 面包").build());
                  items.add((new ItemBuilder(Material.POTION, 2, (byte) 5)).setDisplayName(Cow.this.nameColor + Cow.this.getDisplayName() + " 治疗药水 (8❤)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 2)).build());
                  items.add((new ItemBuilder(Material.POTION, 2, (byte) 2)).setDisplayName(Cow.this.nameColor + Cow.this.getDisplayName() + " 速度药水 II (0:15秒)").addPotion(new PotionEffect(PotionEffectType.SPEED, 300, 1)).build());
               }
               case 5 -> {
                  items.add((new ItemBuilder(Material.IRON_SWORD)).setDisplayName(Cow.this.nameColor + Cow.this.getDisplayName() + " 剑").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                  items.add((new ItemBuilder(Material.DIAMOND_CHESTPLATE)).setDisplayName(Cow.this.nameColor + Cow.this.getDisplayName() + " 胸甲").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).build());
                  items.add((new ItemBuilder(Material.MILK_BUCKET, 3)).build());
                  items.add((new ItemBuilder(Material.BREAD, 6)).setDisplayName(Cow.this.nameColor + Cow.this.getDisplayName() + " 面包").build());
                  items.add((new ItemBuilder(Material.POTION, 2, (byte) 5)).setDisplayName(Cow.this.nameColor + Cow.this.getDisplayName() + " 治疗药水 (8❤)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 2)).build());
                  items.add((new ItemBuilder(Material.POTION, 2, (byte) 2)).setDisplayName(Cow.this.nameColor + Cow.this.getDisplayName() + " 速度药水 II (0:15秒)").addPotion(new PotionEffect(PotionEffectType.SPEED, 300, 1)).build());
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
                  lore.add(" &8▪ &7铁 胸甲");
                  lore.add("    &8▪ 耐久 X");
                  lore.add(" &8▪ &7治疗药水 8❤");
                  lore.add(" &8▪ &7牛奶桶");
                  lore.add(" &8▪ &7面包 &8x2");
               }
               case 2 -> {
                  lore.add(" &8▪ &7铁 胸甲");
                  lore.add("    &8▪ 耐久 X");
                  lore.add("    &a+ 保护 I");
                  lore.add(" &a+ &7速度药水 II");
                  lore.add("    &8▪ 0:15");
                  lore.add(" &8▪ &7面包 &8x2 ➜ &ax4");
               }
               case 3 -> {
                  lore.add(" &8▪ &7铁 胸甲");
                  lore.add("    &8▪ 耐久 X");
                  lore.add("    &8▪ 保护 I ➜ &aII");
                  lore.add(" &8▪ &7速度药水 &8x1 ➜ &ax2");
                  lore.add("    &8▪ 0:15");
                  lore.add(" &8▪ &7牛奶桶 &8x1 ➜ &ax2");
               }
               case 4 -> {
                  lore.add(" &8▪ &7铁 胸甲");
                  lore.add("    &8▪ 耐久 X");
                  lore.add("    &8▪ 保护 I ➜ &aII");
                  lore.add(" &8▪ &7治疗药水 8❤ &8x1 ➜ &ax2");
                  lore.add(" &8▪ &7面包 &8x4 ➜ &ax6");
               }
               case 5 -> {
                  lore.add(" &8▪ &7铁 &8➜ &a钻石 &7胸甲");
                  lore.add("    &8▪ 耐久 X");
                  lore.add("    &8▪ 保护 III ➜ &aI");
                  lore.add(" &8▪ &7牛奶桶 &8x2 ➜ &ax3");
               }
            }

            return lore;
         }
      });
   }

   @Override
   public List<String> getInfo() {
      List<String> lore = new ArrayList();
      lore.add("&7你想帮助队友.");
      lore.add("&7你还想要防御和生存.");
      lore.add("&7哞~");
      return lore;
   }

   @Override
   public int unlockCost() {
      return 25000;
   }

   @Override
   public int energyMelee() {
      return 20;
   }

   @Override
   public int energyBow() {
      return 15;
   }

   public String getSkillTip(GamePlayer gamePlayer) {
      return this.mainSkill.getSkillTip(gamePlayer) + " " + this.secondSkill.getSkillTip(gamePlayer);
   }

   public void run() {
      GamePlayer.getOnlinePlayers().stream().filter((gamePlayer) -> !gamePlayer.isSpectator() && ClassesManager.getSelected(gamePlayer).equals(this)).forEach((gamePlayer) -> {
         if (skill2Cooldown.getOrDefault(gamePlayer, 0) > 0) {
            skill2Cooldown.put(gamePlayer, skill2Cooldown.get(gamePlayer) - 1);
         }

         gamePlayer.sendActionBar(this.getSkillTip(gamePlayer));
      });
   }

   @EventHandler(
           priority = EventPriority.LOWEST
   )
   public void onPlayerItemConsume(PlayerItemConsumeEvent e) {
      if (e.getItem().getType() == Material.MILK_BUCKET) {
         e.setCancelled(true);
         ItemStack itemStack = e.getPlayer().getItemInHand().clone();
         itemStack.setAmount(itemStack.getAmount() - 1);
         e.getPlayer().setItemInHand(e.getPlayer().getItemInHand().getAmount() > 1 ? itemStack : null);
         GamePlayer gamePlayer = GamePlayer.get(e.getPlayer().getUniqueId());
         Classes classes = ClassesManager.getSelected(gamePlayer);
         if (classes.equals(this)) {
            this.thirdSkill.use(gamePlayer, gamePlayer.getPlayerStats().getClassesStats(classes));
         } else {
            Player player = e.getPlayer();
            if (player.hasPotionEffect(PotionEffectType.REGENERATION)) {
               player.removePotionEffect(PotionEffectType.REGENERATION);
            }

            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 200, 0));
         }
      }

   }

   @EventHandler(
           priority = EventPriority.LOWEST
   )
   public void onEntityDamage(EntityDamageEvent e) {
      if (!CitizensAPI.getNPCRegistry().isNPC(e.getEntity()) && !e.isCancelled() && e.getEntity() instanceof Player) {
         GamePlayer gamePlayer = GamePlayer.get(e.getEntity().getUniqueId());
         Classes classes = ClassesManager.getSelected(gamePlayer);
         if (classes.equals(this)) {
            Player player = (Player)e.getEntity();
            if (player.getHealth() - e.getFinalDamage() < 20.0D) {
               this.secondSkill.use(gamePlayer, gamePlayer.getPlayerStats().getClassesStats(ClassesManager.getSelected(gamePlayer)));
            }

            if (skill2Cooldown.getOrDefault(gamePlayer, 0) > 0 && skill2Damage.getOrDefault(gamePlayer, 0) < 4) {
               e.setDamage(DamageModifier.MAGIC, e.getFinalDamage() - e.getFinalDamage() * this.secondSkill.getAttribute(this.secondSkill.getPlayerLevel(gamePlayer)));
               PlayerUtils.heal(player, 2.0D);
               skill2Damage.put(gamePlayer, (Integer)skill2Damage.getOrDefault(gamePlayer, 0) + 1);
            }
         }
      }

   }
}
