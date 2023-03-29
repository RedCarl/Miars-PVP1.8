package cn.mcarl.miars.practiceffa.kits;

import cn.mcarl.miars.core.publics.items.GoldHead;
import cn.mcarl.miars.core.utils.ItemBuilder;
import cn.mcarl.miars.storage.entity.ffa.FInventory;
import cn.mcarl.miars.storage.enums.practice.FKitType;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class BuildUHC {
    public static FInventory get(){

        FInventory inv = new FInventory();

        // 物品栏
        Map<Integer,ItemStack> itemCote = new HashMap<>();

        itemCote.put(0,
                new ItemBuilder(Material.DIAMOND_SWORD)
                        .addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 3)
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
                new ItemBuilder(Material.BOW, 1)
                        .addUnsafeEnchantment(Enchantment.ARROW_DAMAGE,3)
                        .toItemStack()
        );
        itemCote.put(3, new ItemBuilder(Material.COOKED_BEEF, 64).toItemStack());
        itemCote.put(4, new ItemBuilder(Material.GOLDEN_APPLE, 6).toItemStack());
        itemCote.put(5, new GoldHead().get(3));
        itemCote.put(6, new ItemBuilder(Material.DIAMOND_PICKAXE, 1).toItemStack());
        itemCote.put(7, new ItemBuilder(Material.DIAMOND_AXE, 1).toItemStack());
        itemCote.put(8, new ItemBuilder(Material.WOOD, 64).toItemStack());

        inv.setItemCote(itemCote);

        // 背包
        Map<Integer,ItemStack> backpack = new HashMap<>();

        backpack.put(9, new ItemBuilder(Material.ARROW, 16).toItemStack());
        backpack.put(10, new ItemBuilder(Material.COBBLESTONE, 64).toItemStack());
        backpack.put(11, new ItemBuilder(Material.WATER_BUCKET, 1).toItemStack());
        backpack.put(12, new ItemBuilder(Material.WATER_BUCKET, 1).toItemStack());
        backpack.put(13, new ItemBuilder(Material.LAVA_BUCKET, 1).toItemStack());
        backpack.put(14, new ItemBuilder(Material.LAVA_BUCKET, 1).toItemStack());

        inv.setBackpack(backpack);

        // 穿戴
        inv.setBoots(
                new ItemBuilder(Material.DIAMOND_BOOTS)
                        .addUnsafeEnchantment(Enchantment.PROTECTION_PROJECTILE, 2)
                        .setUnbreakable()
                        .toItemStack()
        );
        inv.setLeggings(
                new ItemBuilder(Material.DIAMOND_LEGGINGS)
                        .addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
                        .setUnbreakable()
                        .toItemStack()
        );
        inv.setChestPlate(
                new ItemBuilder(Material.DIAMOND_CHESTPLATE)
                        .addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
                        .setUnbreakable()
                        .toItemStack()
        );
        inv.setHelmet(
                new ItemBuilder(Material.DIAMOND_HELMET)
                        .addUnsafeEnchantment(Enchantment.PROTECTION_PROJECTILE, 2)
                        .setUnbreakable()
                        .toItemStack()
        );

        // 类型定义
        inv.setType(FKitType.BUILD_UHC);

        return inv;
    }



}