package cn.mcarl.miars.practiceffa.kits;

import cn.mcarl.miars.storage.entity.ffa.FInventory;
import cn.mcarl.miars.storage.entity.practice.enums.practice.FKitType;
import cn.mcarl.miars.storage.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class Sumo {
    public static FInventory get(){

        FInventory inv = new FInventory();

        // 物品栏
        Map<Integer,ItemStack> itemCote = new HashMap<>();
        
        inv.setItemCote(itemCote);


        // 背包
        Map<Integer,ItemStack> backpack = new HashMap<>();

        inv.setBackpack(backpack);

        // 类型定义
        inv.setType(FKitType.SUMO);

        return inv;
    }



}