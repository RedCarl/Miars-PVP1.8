package cn.mcarl.miars.practiceffa.kits;

import cn.mcarl.miars.core.utils.ItemBuilder;
import cn.mcarl.miars.storage.entity.ffa.FInventory;
import cn.mcarl.miars.storage.enums.practice.FKitType;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class Queue {
    public static FInventory get(){
        FInventory inv = new FInventory();

        // 物品栏
        Map<Integer,ItemStack> itemCote = new HashMap<>();

        itemCote.put(4,
                new ItemBuilder(Material.INK_SACK)
                        .setName("&c取消匹配 &7(右键取消)")
                        .setData((short) 1)
                        .setNbtBoolean("stopClick",true)
                        .setNbtString("queue","cancel")
                        .setUnbreakable()
                        .toItemStack()
        );

        inv.setItemCote(itemCote);

        // 背包
        Map<Integer,ItemStack> backpack = new HashMap<>();

        inv.setBackpack(backpack);

        // 类型定义
        inv.setType(FKitType.PRACTICE);

        return inv;
    }
}