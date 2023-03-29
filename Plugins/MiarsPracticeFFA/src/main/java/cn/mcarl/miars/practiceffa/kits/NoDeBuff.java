package cn.mcarl.miars.practiceffa.kits;

import cn.mcarl.miars.core.utils.ItemBuilder;
import cn.mcarl.miars.storage.entity.ffa.FInventory;
import cn.mcarl.miars.storage.enums.practice.FKitType;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class NoDeBuff {
    public static FInventory get(){

        ItemStack heal = new ItemBuilder(Material.POTION, 1).setData((short) 16421).toItemStack();
        ItemStack speed = new ItemBuilder(Material.POTION, 1).setData((short) 8226).toItemStack();
        ItemStack fire = new ItemBuilder(Material.POTION, 1).setData((short) 8259).toItemStack();

        FInventory inv = new FInventory();

        // 物品栏
        Map<Integer,ItemStack> itemCote = new HashMap<>();

        itemCote.put(0, new ItemBuilder(Material.DIAMOND_SWORD).addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 2).addUnsafeEnchantment(Enchantment.DURABILITY, 3).addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 2).setUnbreakable().toItemStack());
        itemCote.put(1, new ItemBuilder(Material.ENDER_PEARL, 16).toItemStack());
        itemCote.put(2, speed);
        itemCote.put(3, fire);

        inv.setItemCote(itemCote);

        // 背包
        Map<Integer,ItemStack> backpack = new HashMap<>();
        for (int i = 4; i <= 35; i++) {
            backpack.put(i, heal);
        }
        backpack.put(8, new ItemBuilder(Material.COOKED_BEEF, 64).toItemStack());
        backpack.put(17, speed);
        backpack.put(26, speed);
        backpack.put(35, speed);
        inv.setBackpack(backpack);

        // 穿戴
        inv.setBoots(
                new ItemBuilder(Material.DIAMOND_BOOTS)
                        .addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
                        .addUnsafeEnchantment(Enchantment.DURABILITY, 3)
                        .addUnsafeEnchantment(Enchantment.PROTECTION_FALL, 4)
                        .setUnbreakable()
                        .toItemStack()
        );
        inv.setLeggings(
                new ItemBuilder(Material.DIAMOND_LEGGINGS)
                        .addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
                        .addUnsafeEnchantment(Enchantment.DURABILITY, 3)
                        .setUnbreakable()
                        .toItemStack()
        );
        inv.setChestPlate(
                new ItemBuilder(Material.DIAMOND_CHESTPLATE)
                        .addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
                        .addUnsafeEnchantment(Enchantment.DURABILITY, 3)
                        .setUnbreakable()
                        .toItemStack()
        );
        inv.setHelmet(
                new ItemBuilder(Material.DIAMOND_HELMET)
                        .addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
                        .addUnsafeEnchantment(Enchantment.DURABILITY, 3)
                        .setUnbreakable()
                        .toItemStack()
        );

        // 类型定义
        inv.setType(FKitType.NO_DEBUFF);

        return inv;
    }



}