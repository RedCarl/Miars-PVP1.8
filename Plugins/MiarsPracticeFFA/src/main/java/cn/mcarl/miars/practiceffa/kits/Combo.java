package cn.mcarl.miars.practiceffa.kits;

import cn.mcarl.miars.storage.entity.ffa.FInventory;
import cn.mcarl.miars.storage.entity.practice.enums.practice.FKitType;
import cn.mcarl.miars.storage.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class Combo {
    public static FInventory get(){

        FInventory inv = new FInventory();

        ItemStack speed = new ItemBuilder(Material.POTION, 1).setData((short) 8226).toItemStack();

        // 物品栏
        Map<Integer,ItemStack> itemCote = new HashMap<>();

        itemCote.put(0,
                new ItemBuilder(Material.DIAMOND_SWORD)
                        .addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 2)
                        .addUnsafeEnchantment(Enchantment.DURABILITY, 3)
                        .toItemStack()
        );
        itemCote.put(1, new ItemBuilder(Material.GOLDEN_APPLE, 64).setData((short) 1).toItemStack());
        itemCote.put(2, speed);

        inv.setItemCote(itemCote);

        // 背包
        Map<Integer,ItemStack> backpack = new HashMap<>();

        backpack.put(9,
                new ItemBuilder(Material.DIAMOND_BOOTS)
                        .addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4)
                        .addUnsafeEnchantment(Enchantment.DURABILITY, 3)
                        .toItemStack()
        );
        backpack.put(10,
                new ItemBuilder(Material.DIAMOND_LEGGINGS)
                        .addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4)
                        .addUnsafeEnchantment(Enchantment.DURABILITY, 3)
                        .toItemStack()
        );
        backpack.put(11,
                new ItemBuilder(Material.DIAMOND_CHESTPLATE)
                        .addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4)
                        .addUnsafeEnchantment(Enchantment.DURABILITY, 3)
                        .toItemStack()
        );
        backpack.put(12,
                new ItemBuilder(Material.DIAMOND_HELMET)
                        .addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4)
                        .addUnsafeEnchantment(Enchantment.DURABILITY, 3)
                        .toItemStack()
        );
        for (int i = 0; i < 7; i++) {
            backpack.put(i+13,speed);
        }

        inv.setBackpack(backpack);

        // 穿戴
        inv.setBoots(
                new ItemBuilder(Material.DIAMOND_BOOTS)
                        .addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4)
                        .addUnsafeEnchantment(Enchantment.DURABILITY, 3)
                        .toItemStack()
        );
        inv.setLeggings(
                new ItemBuilder(Material.DIAMOND_LEGGINGS)
                        .addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4)
                        .addUnsafeEnchantment(Enchantment.DURABILITY, 3)
                        .toItemStack()
        );
        inv.setChestPlate(
                new ItemBuilder(Material.DIAMOND_CHESTPLATE)
                        .addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4)
                        .addUnsafeEnchantment(Enchantment.DURABILITY, 3)
                        .toItemStack()
        );
        inv.setHelmet(
                new ItemBuilder(Material.DIAMOND_HELMET)
                        .addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4)
                        .addUnsafeEnchantment(Enchantment.DURABILITY, 3)
                        .toItemStack()
        );

        // 类型定义
        inv.setType(FKitType.COMBO);

        return inv;
    }



}