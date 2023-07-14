package cn.mcarl.miars.practiceffa.kits;

import cn.mcarl.miars.storage.entity.ffa.FInventory;
import cn.mcarl.miars.storage.entity.practice.enums.practice.FKitType;
import cn.mcarl.miars.storage.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class Bow {
    public static FInventory get(){

        FInventory inv = new FInventory();

        ItemStack speed = new ItemBuilder(Material.POTION, 1).setData((short) 8258).toItemStack();
        // 物品栏
        Map<Integer,ItemStack> itemCote = new HashMap<>();

        itemCote.put(0,
                new ItemBuilder(Material.WOOD_SWORD)
                        .setUnbreakable()
                        .toItemStack()
        );
        itemCote.put(1,
                new ItemBuilder(Material.BOW, 1)
                        .addEnchant(Enchantment.ARROW_DAMAGE,2,true)
                        .addEnchant(Enchantment.ARROW_INFINITE,1,true)
                        .toItemStack()
        );
        itemCote.put(2, new ItemBuilder(Material.GOLDEN_CARROT, 64).toItemStack());
        itemCote.put(3, speed);
        itemCote.put(4, speed);
        inv.setItemCote(itemCote);

        // 背包
        Map<Integer,ItemStack> backpack = new HashMap<>();

        backpack.put(9, new ItemBuilder(Material.ARROW, 1).toItemStack());

        inv.setBackpack(backpack);

        // 穿戴
        inv.setBoots(
                new ItemBuilder(Material.LEATHER_BOOTS)
                        .setUnbreakable()
                        .toItemStack()
        );
        inv.setLeggings(
                new ItemBuilder(Material.LEATHER_LEGGINGS)
                        .setUnbreakable()
                        .toItemStack()
        );
        inv.setChestPlate(
                new ItemBuilder(Material.LEATHER_CHESTPLATE)
                        .setUnbreakable()
                        .toItemStack()
        );
        inv.setHelmet(
                new ItemBuilder(Material.LEATHER_HELMET)
                        .setUnbreakable()
                        .toItemStack()
        );

        // 类型定义
        inv.setType(FKitType.BOW);

        return inv;
    }



}