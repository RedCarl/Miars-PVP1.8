package cn.mcarl.miars.megawalls.classes.stater.shark;

import cn.mcarl.miars.megawalls.classes.Classes;
import cn.mcarl.miars.megawalls.classes.ClassesInfo;
import cn.mcarl.miars.megawalls.classes.ClassesType;
import cn.mcarl.miars.megawalls.classes.EquipmentPackage;
import cn.mcarl.miars.megawalls.game.entitiy.GamePlayer;
import cn.mcarl.miars.megawalls.utils.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Shark extends Classes {

   public Shark() {
      super(
              "Shark",
              "鲨鱼",
              ChatColor.DARK_AQUA,
              Material.WATER_BUCKET,
              (byte)0,
              ClassesType.STATER,
              new ClassesInfo.Orientation[]{
                      ClassesInfo.Orientation.WARRIOR,
                      ClassesInfo.Orientation.CONTROL
              },
              ClassesInfo.Difficulty.THREE
      );
       this.setDefaultSkin("eyJ0aW1lc3RhbXAiOjE1NDk0MzQxOTQzMzUsInByb2ZpbGVJZCI6ImRkZWQ1NmUxZWY4YjQwZmU4YWQxNjI5MjBmN2FlY2RhIiwicHJvZmlsZU5hbWUiOiJEaXNjb3JkQXBwIiwic2lnbmF0dXJlUmVxdWlyZWQiOnRydWUsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9hZjIxOTRmNWUyNjk0OTIyMDgyOTgxZmIxZmI2MWZjMmM0Mzc1Y2YzYzU1ZmE1MWUyY2UwZjRjZGJhOGMyYjc1In19fQ==", "IgU2zgW5by8CYCFko8PJy8Km2L11rY/xYXYvJLEq1oUOcX+l6i8reGUXIPPTRHhVyYeqbWccYDhXv2LCKiyBuMi1ZOOliL6mXLMElOymgaZ1mnI+dW/IdfwFBoqLLXIzrptamcX2q0MhuI/+eg+qVr2YoE5hICo7kp4CGMMNe78g4aa9JPHPncpOvZm2wFs4/6OUPnPQjMuPtdCkG3/M+8gaQau1cksXu5gzUSj4U4IInrFD1u+Hu6cTOQcpMsH43EUDhp9c7arZkTV60hFQf556xuJfksCCKLaPlf2CoPRUyrVFiKO04xW24/rcdII+XKXa4f3MPYAftUhlSCuOz89PZ/uB/+IyhYTVfw6dXOiJdRlveH0EoqKYwSzkrKvLvCYELlzESlG18smYV9BPzJ3RiiGrBt0vaAD8fVZU5qbyZunLyQX/kQAtLz+igVHdAM8huYJ0lKUA18ZRbvnn1OnY9/IY6fSmuvmlW10YmNNQxdMZxbIh+zmLYDPQzXkh+QmyifAlervuXS1IbmVVCdeQuHWDobJZBd4DdxHOFXm4O4cd7xefBIgP3S/2PUdpQZd2MghmBNQeGMj5lQ9+V/1tOforanRQ5tOpn1Qk3SSKYud2c1O0jCYrmxHxS1bODDgvfkCeJzTiAFJZY29/xaawcFpWJeZrBhV8MSrBXTo=");
      this.setEquipmentPackage(new EquipmentPackage(this) {
         @Override
         public List<ItemStack> getEquipments(int level) {
            List<ItemStack> items = new ArrayList<>();
            
            
            
             switch(level) {
                 case 1:
                     items.add((new ItemBuilder(Material.IRON_SWORD)).setDisplayName(Shark.this.nameColor + Shark.this.getDisplayName() + " 剑").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                     items.add((new ItemBuilder(Material.IRON_BOOTS)).setDisplayName(Shark.this.nameColor + Shark.this.getDisplayName() + " 靴子").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.DEPTH_STRIDER, 1).build());
                     items.add((new ItemBuilder(Material.COOKED_FISH, 2)).setDisplayName(Shark.this.nameColor + Shark.this.getDisplayName() + " 烤制 鱼").build());
                     items.add((new ItemBuilder(Material.POTION, 1, (byte)5)).setDisplayName(Shark.this.nameColor + Shark.this.getDisplayName() + " 治疗药水 (8❤)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 2)).build());
                     break;
                 case 2:
                     items.add((new ItemBuilder(Material.IRON_SWORD)).setDisplayName(Shark.this.nameColor + Shark.this.getDisplayName() + " 剑").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                     items.add((new ItemBuilder(Material.IRON_BOOTS)).setDisplayName(Shark.this.nameColor + Shark.this.getDisplayName() + " 靴子").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.DEPTH_STRIDER, 1).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).build());
                     items.add((new ItemBuilder(Material.COOKED_FISH, 4)).setDisplayName(Shark.this.nameColor + Shark.this.getDisplayName() + " 烤制 鱼").build());
                     items.add((new ItemBuilder(Material.POTION, 1, (byte)5)).setDisplayName(Shark.this.nameColor + Shark.this.getDisplayName() + " 治疗药水 (8❤)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 2)).build());
                     items.add((new ItemBuilder(Material.POTION, 1, (byte)2)).setDisplayName(Shark.this.nameColor + Shark.this.getDisplayName() + " 速度药水 II (0:15秒)").addPotion(new PotionEffect(PotionEffectType.SPEED, 300, 1)).build());
                     break;
                 case 3:
                     items.add((new ItemBuilder(Material.IRON_SWORD)).setDisplayName(Shark.this.nameColor + Shark.this.getDisplayName() + " 剑").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                     items.add((new ItemBuilder(Material.IRON_BOOTS)).setDisplayName(Shark.this.nameColor + Shark.this.getDisplayName() + " 靴子").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.DEPTH_STRIDER, 2).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).build());
                     items.add((new ItemBuilder(Material.COOKED_FISH, 4)).setDisplayName(Shark.this.nameColor + Shark.this.getDisplayName() + " 烤制 鱼").build());
                     items.add((new ItemBuilder(Material.POTION, 1, (byte)5)).setDisplayName(Shark.this.nameColor + Shark.this.getDisplayName() + " 治疗药水 (8❤)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 2)).build());
                     items.add((new ItemBuilder(Material.POTION, 2, (byte)2)).setDisplayName(Shark.this.nameColor + Shark.this.getDisplayName() + " 速度药水 II (0:15秒)").addPotion(new PotionEffect(PotionEffectType.SPEED, 300, 1)).build());
                     break;
                 case 4:
                     items.add((new ItemBuilder(Material.DIAMOND_SWORD)).setDisplayName(Shark.this.nameColor + Shark.this.getDisplayName() + " 剑").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                     items.add((new ItemBuilder(Material.IRON_BOOTS)).setDisplayName(Shark.this.nameColor + Shark.this.getDisplayName() + " 靴子").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.DEPTH_STRIDER, 2).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2).build());
                     items.add((new ItemBuilder(Material.COOKED_FISH, 6)).setDisplayName(Shark.this.nameColor + Shark.this.getDisplayName() + " 烤制 鱼").build());
                     items.add((new ItemBuilder(Material.POTION, 2, (byte)5)).setDisplayName(Shark.this.nameColor + Shark.this.getDisplayName() + " 治疗药水 (8❤)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 2)).build());
                     items.add((new ItemBuilder(Material.POTION, 2, (byte)2)).setDisplayName(Shark.this.nameColor + Shark.this.getDisplayName() + " 速度药水 II (0:15秒)").addPotion(new PotionEffect(PotionEffectType.SPEED, 300, 1)).build());
                     break;
                 case 5:
                     items.add((new ItemBuilder(Material.DIAMOND_SWORD)).setDisplayName(Shark.this.nameColor + Shark.this.getDisplayName() + " 剑").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                     items.add((new ItemBuilder(Material.DIAMOND_BOOTS)).setDisplayName(Shark.this.nameColor + Shark.this.getDisplayName() + " 靴子").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.DEPTH_STRIDER, 3).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2).build());
                     items.add((new ItemBuilder(Material.COOKED_FISH, 6)).setDisplayName(Shark.this.nameColor + Shark.this.getDisplayName() + " 烤制 鱼").build());
                     items.add((new ItemBuilder(Material.POTION, 2, (byte)5)).setDisplayName(Shark.this.nameColor + Shark.this.getDisplayName() + " 治疗药水 (8❤)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 2)).build());
                     items.add((new ItemBuilder(Material.POTION, 2, (byte)2)).setDisplayName(Shark.this.nameColor + Shark.this.getDisplayName() + " 速度药水 II (0:15秒)").addPotion(new PotionEffect(PotionEffectType.SPEED, 300, 1)).build());
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
                  lore.add(" &8▪ &7铁 靴子");
                  lore.add("    &8▪ 耐久 X");
                  lore.add("    &8▪ 深海探索者 I");
                  lore.add(" &8▪ &7治疗药水 8❤");
                  lore.add(" &8▪ &7烤制 鱼 &8x2");
               }
               case 2 -> {
                  lore.add(" &8▪ &7铁 靴子");
                  lore.add("    &8▪ 耐久 X");
                  lore.add("    &8▪ 深海探索者 I");
                  lore.add("    &a+ 保护 I");
                  lore.add(" &a+ &7速度药水 II");
                  lore.add("    &8▪ 0:15");
                  lore.add(" &8▪ &7烤制 鱼 &8x2 ➜ &ax4");
               }
               case 3 -> {
                  lore.add(" &8▪ &7铁 靴子");
                  lore.add("    &8▪ 耐久 X");
                  lore.add("    &8▪ 深海探索者 I ➜ &aII");
                  lore.add("    &a▪ 保护 I");
                  lore.add(" &a▪ &7速度药水 II &8x1 ➜ &ax2");
                  lore.add("    &8▪ 0:15");
               }
               case 4 -> {
                  lore.add(" &8▪ &7铁 ➜ &a钻石 &7剑");
                  lore.add("    &8▪ 耐久 X");
                  lore.add(" &8▪ &7铁 靴子");
                  lore.add("    &8▪ 耐久 X");
                  lore.add("    &8▪ 深海探索者 II");
                  lore.add("    &a▪ 保护 I ➜ &aII");
                  lore.add(" &a▪ &7治疗药水 8❤ &8x1 ➜ &ax2");
                  lore.add(" &8▪ &7烤制 鱼 &8x4 ➜ &ax6");
               }
               case 5 -> {
                  lore.add(" &8▪ &7铁 &8➜ &a钻石 &7靴子");
                  lore.add("    &8▪ 耐久 X");
                  lore.add("    &8▪ 深海探索者 II ➜ &aIII");
                  lore.add("    &a▪ 保护 II");
               }
            }

            return lore;
         }
      });
   }

   @Override
   public List<String> getInfo() {
      List<String> lore = new ArrayList<>();
      lore.add("&7你想要在掌控局面的同时造成大量伤害。");
      lore.add("&7你喜欢在此之中灌输无尽的恐惧。");
      return lore;
   }

   @Override
   public int unlockCost() {
      return 40000;
   }

   @Override
   public int energyMelee() {
      return 10;
   }

   @Override
   public int energyBow() {
      return 10;
   }

   public String getSkillTip(GamePlayer gamePlayer) {
       return null;
   }
}
