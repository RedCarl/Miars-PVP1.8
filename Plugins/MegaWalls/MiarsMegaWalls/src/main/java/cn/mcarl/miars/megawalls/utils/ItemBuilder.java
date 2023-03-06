package cn.mcarl.miars.megawalls.utils;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class ItemBuilder {
   private final ItemStack itemStack;

   public ItemBuilder(Material material) {
      this(material, 1);
   }

   public ItemBuilder(ItemStack itemStack) {
      this.itemStack = itemStack;
   }

   public ItemBuilder(Material material, int amount) {
      this.itemStack = new ItemStack(material, amount);
   }

   public ItemBuilder(Material material, int amount, byte durability) {
      this.itemStack = new ItemStack(material, amount, (short)durability);
   }

   @Override
   public ItemBuilder clone() {
      return new ItemBuilder(this.itemStack);
   }

   public ItemBuilder setDurability(short durability) {
      this.itemStack.setDurability(durability);
      return this;
   }

   public ItemBuilder setUnbreakable(boolean unbreakable) {
      ItemMeta meta = this.itemStack.getItemMeta();
      meta.spigot().setUnbreakable(unbreakable);
      this.itemStack.setItemMeta(meta);
      return this;
   }

   public ItemBuilder setDisplayName(String name) {
      ItemMeta itemMeta = this.itemStack.getItemMeta();
      itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
      this.itemStack.setItemMeta(itemMeta);
      return this;
   }

   public ItemBuilder addUnsafeEnchantment(Enchantment enchantment, int level) {
      this.itemStack.addUnsafeEnchantment(enchantment, level);
      return this;
   }

   public ItemBuilder removeEnchantment(Enchantment enchantment) {
      this.itemStack.removeEnchantment(enchantment);
      return this;
   }

   public ItemBuilder setSkullOwner(String owner) {
      SkullMeta im = (SkullMeta)this.itemStack.getItemMeta();
      im.setOwner(owner);
      this.itemStack.setItemMeta(im);
      return this;
   }

   public ItemBuilder setEnchantMeta(Map<Enchantment, Integer> enchantments) {
      EnchantmentStorageMeta im = (EnchantmentStorageMeta)this.itemStack.getItemMeta();

      for (Entry<Enchantment, Integer> enchantmentIntegerEntry : enchantments.entrySet()) {
         im.addStoredEnchant(enchantmentIntegerEntry.getKey(), enchantmentIntegerEntry.getValue(), true);
      }

      this.itemStack.setItemMeta(im);
      return this;
   }

   public ItemBuilder addEnchantment(Enchantment enchantment, int level) {
      ItemMeta im = this.itemStack.getItemMeta();
      im.addEnchant(enchantment, level, true);
      this.itemStack.setItemMeta(im);
      return this;
   }

   public ItemBuilder setInfinityDurability() {
      this.itemStack.setDurability((short)32767);
      return this;
   }

   public ItemBuilder setLore(String... lore) {
      ItemMeta im = this.itemStack.getItemMeta();
      List<String> lores = new ArrayList<>();

      for (String line : lore) {
         lores.add(ChatColor.translateAlternateColorCodes('&', line));
      }

      im.setLore(lores);
      this.itemStack.setItemMeta(im);
      return this;
   }

   public ItemBuilder setLore(List<String> lore) {
      ItemMeta im = this.itemStack.getItemMeta();
      List<String> lores = new ArrayList<>();

      for (String line : lore) {
         lores.add(ChatColor.translateAlternateColorCodes('&', line));
      }

      im.setLore(lores);
      this.itemStack.setItemMeta(im);
      return this;
   }

   public ItemBuilder setDyeColor(DyeColor color) {
      this.itemStack.setDurability(color.getData());
      return this;
   }

   public ItemBuilder addGlow() {
      ItemMeta im = this.itemStack.getItemMeta();
      im.addEnchant(Enchantment.DURABILITY, 1, true);
      im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
      this.itemStack.setItemMeta(im);
      return this;
   }

   public ItemBuilder addPotion(PotionEffect potionEffect) {
      PotionMeta im = (PotionMeta)this.itemStack.getItemMeta();
      im.addCustomEffect(potionEffect, true);
      this.itemStack.setItemMeta(im);
      return this;
   }

   public ItemStack build() {
      return this.itemStack;
   }
}
