package cn.mcarl.miars.megawalls.classes.novice.him;

import cn.mcarl.miars.megawalls.classes.Classes;
import cn.mcarl.miars.megawalls.classes.ClassesInfo;
import cn.mcarl.miars.megawalls.classes.ClassesType;
import cn.mcarl.miars.megawalls.classes.EquipmentPackage;
import cn.mcarl.miars.megawalls.utils.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class HIM extends Classes {
   public HIM() {
      super("HIM", "HIM", ChatColor.YELLOW, Material.DIAMOND_SWORD, (byte)0, ClassesType.NOVICE, new ClassesInfo.Orientation[]{ClassesInfo.Orientation.WARRIOR, ClassesInfo.Orientation.HURT}, ClassesInfo.Difficulty.ONE);
      this.setMainSkill(new MainSkill(this));
      this.setSecondSkill(new SecondSkill(this));
      this.setThirdSkill(new ThirdSkill(this));
      this.setCollectSkill(new FourthSkill(this));
      this.setDefaultSkin("eyJ0aW1lc3RhbXAiOjE1MzMzNDI3NTE3MTksInByb2ZpbGVJZCI6IjU2Njc1YjIyMzJmMDRlZTA4OTE3OWU5YzkyMDZjZmU4IiwicHJvZmlsZU5hbWUiOiJUaGVJbmRyYSIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmM2NWVkMjgyOWM4M2UxMTlhODBkZmIyMjIxNjQ0M2U4NzhlZjEwNjQ5YzRhMzU0Zjc0YmY0NWFkMDZiYzFhNyJ9fX0=", "gvRz+z/AM/p9iJeifKTCEjIVoQbQq0OHPstiFFdX9cE6uxw+C0X0JuwoxHVEUkvKlzPQJGVPLh5VSQY4NL3wCEkyOwoN3FDWIewjzhwtmyM4BlzbHDRT6C/4ICFw3azLZi6f7EkPNcL9O8yks+ebyLxXEgJEFSmY7nMsjdRuLOVu7X9UoSbKLwNR8Rua9LEKtdhpVcQx+rLD9T4VRZctfhunPDVnXfQTqvq4gC1lb6nQPIwSnVCdH5eY4bnOO1n/vV7enOO0mMgjtQLxFFM1OKoBRDxh70pNgAmVUxunUA1xrfG+pZF5HM6nMh1FDKD7NTNrZ7O4EWpawT2Q8+EVLgdcMYNMlIW1xb4pOMceDpaAlOw3LOfROGK6cz4OpevuXU3WaXb97cuq8B0SrlVJI7xnL3sgEzyf+1MIQ6O0NBo7SAa0vVXEVu+Y65bawZh0HYpwngkTkMJR8wulNQ2HcKzSClZKsqP7M/qneHCJ4lnQsGFXj5LEp1EkJZ7WGTcIo5L4Fo7hl0FiGtTdesBEBc/OQOB2dIZfOZFInRaTMALuaB3VrAg+atYtTeU9twl0qmrq/0L5FbszkTh6xqpnGmlNWuRx1yKuG8+lD3f0sejHIPXC/Akw3YQjWFUTs3e8h18zD6mtZxu4CKFc6hVsnMsGr0tEIa/eASkUGJSNy0s=");
      this.setEquipmentPackage(new EquipmentPackage(this) {
         @Override
         public List<ItemStack> getEquipments(int level) {
            List<ItemStack> items = new ArrayList<>();
            items.add((new ItemBuilder(Material.COMPASS)).setDisplayName(HIM.this.nameColor + HIM.this.getDisplayName() + " 指南针").build());
            items.add((new ItemBuilder(Material.DIAMOND_PICKAXE)).setDisplayName(HIM.this.nameColor + HIM.this.getDisplayName() + " 镐").setUnbreakable(true).addEnchantment(Enchantment.DIG_SPEED, 3).addEnchantment(Enchantment.DURABILITY, 3).build());
            items.add((new ItemBuilder(Material.ENDER_CHEST)).setDisplayName(HIM.this.nameColor + HIM.this.getDisplayName() + " 末影箱").build());
            switch (level) {
               case 1 -> {
                  items.add((new ItemBuilder(Material.IRON_SWORD)).setDisplayName(HIM.this.nameColor + HIM.this.getDisplayName() + " 剑").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                  items.add((new ItemBuilder(Material.COOKED_BEEF)).setDisplayName(HIM.this.nameColor + HIM.this.getDisplayName() + " 牛排").build());
               }
               case 2 -> {
                  items.add((new ItemBuilder(Material.IRON_SWORD)).setDisplayName(HIM.this.nameColor + HIM.this.getDisplayName() + " 剑").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                  items.add((new ItemBuilder(Material.COOKED_BEEF, 3)).setDisplayName(HIM.this.nameColor + HIM.this.getDisplayName() + " 牛排").build());
                  items.add((new ItemBuilder(Material.POTION, 1, (byte) 2)).setDisplayName(HIM.this.nameColor + HIM.this.getDisplayName() + " 速度药水 II (0:15秒)").addPotion(new PotionEffect(PotionEffectType.SPEED, 300, 1)).build());
               }
               case 3 -> {
                  items.add((new ItemBuilder(Material.IRON_SWORD)).setDisplayName(HIM.this.nameColor + HIM.this.getDisplayName() + " 剑").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                  items.add((new ItemBuilder(Material.COOKED_BEEF, 3)).setDisplayName(HIM.this.nameColor + HIM.this.getDisplayName() + " 牛排").build());
                  items.add((new ItemBuilder(Material.POTION, 2, (byte) 2)).setDisplayName(HIM.this.nameColor + HIM.this.getDisplayName() + " 速度药水 II (0:15秒)").addPotion(new PotionEffect(PotionEffectType.SPEED, 300, 1)).build());
                  items.add((new ItemBuilder(Material.POTION, 1, (byte) 5)).setDisplayName(HIM.this.nameColor + HIM.this.getDisplayName() + " 治疗药水 (8❤)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 2)).build());
                  items.add((new ItemBuilder(Material.IRON_HELMET)).setDisplayName(HIM.this.nameColor + HIM.this.getDisplayName() + " 头盔").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
               }
               case 4 -> {
                  items.add((new ItemBuilder(Material.IRON_SWORD)).setDisplayName(HIM.this.nameColor + HIM.this.getDisplayName() + " 剑").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                  items.add((new ItemBuilder(Material.COOKED_BEEF, 3)).setDisplayName(HIM.this.nameColor + HIM.this.getDisplayName() + " 牛排").build());
                  items.add((new ItemBuilder(Material.POTION, 2, (byte) 2)).setDisplayName(HIM.this.nameColor + HIM.this.getDisplayName() + " 速度药水 II (0:15秒)").addPotion(new PotionEffect(PotionEffectType.SPEED, 300, 1)).build());
                  items.add((new ItemBuilder(Material.POTION, 2, (byte) 5)).setDisplayName(HIM.this.nameColor + HIM.this.getDisplayName() + " 治疗药水 (8❤)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 2)).build());
                  items.add((new ItemBuilder(Material.IRON_HELMET)).setDisplayName(HIM.this.nameColor + HIM.this.getDisplayName() + " 头盔").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.WATER_WORKER, 1).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).build());
               }
               case 5 -> {
                  items.add((new ItemBuilder(Material.DIAMOND_SWORD)).setDisplayName(HIM.this.nameColor + HIM.this.getDisplayName() + " 剑").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                  items.add((new ItemBuilder(Material.COOKED_BEEF, 3)).setDisplayName(HIM.this.nameColor + HIM.this.getDisplayName() + " 牛排").build());
                  items.add((new ItemBuilder(Material.POTION, 2, (byte) 2)).setDisplayName(HIM.this.nameColor + HIM.this.getDisplayName() + " 速度药水 II (0:15秒)").addPotion(new PotionEffect(PotionEffectType.SPEED, 300, 1)).build());
                  items.add((new ItemBuilder(Material.POTION, 2, (byte) 5)).setDisplayName(HIM.this.nameColor + HIM.this.getDisplayName() + " 治疗药水 (8❤)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 2)).build());
                  items.add((new ItemBuilder(Material.IRON_HELMET)).setDisplayName(HIM.this.nameColor + HIM.this.getDisplayName() + "头盔").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.WATER_WORKER, 1).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2).addEnchantment(Enchantment.PROTECTION_PROJECTILE, 1).build());
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
                  lore.add(" &8▪ &7牛排");
               }
               case 2 -> {
                  lore.add(" &a+ &7速度药水 II");
                  lore.add("    &8▪ 0:15");
                  lore.add(" &8▪ &7牛排 &8x1 ➜ &ax3");
               }
               case 3 -> {
                  lore.add(" &a+ &7铁 头盔");
                  lore.add("    &8▪ 耐久 X");
                  lore.add(" &a+ &7治疗药水 8❤");
                  lore.add(" &8▪ &7速度药水 II &8x1 ➜ &ax2");
                  lore.add("    &8▪ 0:15");
               }
               case 4 -> {
                  lore.add(" &8▪ &7铁 头盔");
                  lore.add("    &8▪ 耐久 X");
                  lore.add("    &a+ 水下速掘 I");
                  lore.add("    &a+ 保护 I");
                  lore.add(" &8▪ &7治疗药水 8❤ &8x1 ➜ &ax2");
                  lore.add("    &8▪ 0:15");
               }
               case 5 -> {
                  lore.add(" &8▪ &7铁 &8➜ &a钻石 &7剑");
                  lore.add("    &8▪ 耐久 X");
                  lore.add(" &8▪ &7铁 头盔");
                  lore.add("    &8▪ 耐久 X");
                  lore.add("    &8▪ 水下速掘 I");
                  lore.add("    &8▪ 保护 I &8➜ &aII");
                  lore.add("    &a+ 弹射保护 I");
                  lore.add(" &8▪ &7治疗药水 8❤ &8x1 ➜ &ax2");
                  lore.add("    &8▪ 0:15");
               }
            }

            return lore;
         }
      });
   }

   @Override
   public List<String> getInfo() {
      return new ArrayList<>();
   }

   @Override
   public int unlockCost() {
      return 0;
   }

   @Override
   public int energyMelee() {
      return 25;
   }

   @Override
   public int energyBow() {
      return 25;
   }
}
