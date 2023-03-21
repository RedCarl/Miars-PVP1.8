package cn.mcarl.miars.practiceffa.kits;

import cn.mcarl.miars.core.utils.ItemBuilder;
import cn.mcarl.miars.storage.entity.ffa.FInventory;
import cn.mcarl.miars.storage.enums.practice.FKitType;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class FFAGame {
    public static FInventory get(){
        FInventory inv = new FInventory();

        // 物品栏
        Map<Integer,ItemStack> itemCote = new HashMap<>();

        itemCote.put(0,
                new ItemBuilder(Material.DIAMOND_SWORD)
                        .addFlag(ItemFlag.HIDE_UNBREAKABLE)
                        .addFlag(ItemFlag.HIDE_ENCHANTS)
                        .addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 2)
                        .setUnbreakable()
                        .toItemStack()
        );
        itemCote.put(1,
                new ItemBuilder(Material.FISHING_ROD, 1)
                        .addFlag(ItemFlag.HIDE_UNBREAKABLE)
                        .addFlag(ItemFlag.HIDE_ENCHANTS)
                        .setUnbreakable()
                        .toItemStack()
        );
        itemCote.put(2,
                new ItemBuilder(Material.GOLDEN_APPLE, 6)
                        .addFlag(ItemFlag.HIDE_UNBREAKABLE)
                        .addFlag(ItemFlag.HIDE_ENCHANTS)
                        .toItemStack()
        );
        itemCote.put(3,
                new ItemBuilder(Material.COOKED_BEEF, 64)
                        .addFlag(ItemFlag.HIDE_UNBREAKABLE)
                        .addFlag(ItemFlag.HIDE_ENCHANTS)
                        .toItemStack()
        );

        inv.setItemCote(itemCote);

        // 背包
        Map<Integer,ItemStack> backpack = new HashMap<>();

        inv.setBackpack(backpack);

        // 穿戴
        inv.setBoots(
                new ItemBuilder(Material.DIAMOND_BOOTS)
                        .addFlag(ItemFlag.HIDE_UNBREAKABLE)
                        .addFlag(ItemFlag.HIDE_ENCHANTS)
                        .addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
                        .setUnbreakable()
                        .toItemStack()
        );
        inv.setLeggings(
                new ItemBuilder(Material.DIAMOND_LEGGINGS)
                        .addFlag(ItemFlag.HIDE_UNBREAKABLE)
                        .addFlag(ItemFlag.HIDE_ENCHANTS)
                        .addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
                        .setUnbreakable()
                        .toItemStack()
        );
        inv.setChestPlate(
                new ItemBuilder(Material.DIAMOND_CHESTPLATE)
                        .addFlag(ItemFlag.HIDE_UNBREAKABLE)
                        .addFlag(ItemFlag.HIDE_ENCHANTS)
                        .addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
                        .setUnbreakable()
                        .toItemStack()
        );
        inv.setHelmet(
                new ItemBuilder(Material.DIAMOND_HELMET)
                        .addFlag(ItemFlag.HIDE_UNBREAKABLE)
                        .addFlag(ItemFlag.HIDE_ENCHANTS)
                        .addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
                        .setUnbreakable()
                        .toItemStack()
        );

        // 类型定义
        inv.setType(FKitType.FFAGAME);

        return inv;
    }
}