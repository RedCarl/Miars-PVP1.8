package cn.mcarl.miars.practiceffa.kits;

import cn.mcarl.miars.core.utils.ItemBuilder;
import cn.mcarl.miars.storage.entity.ffa.FInventory;
import cn.mcarl.miars.storage.enums.FKitType;
import cn.mcarl.miars.storage.enums.QueueType;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class Practice {
    public static FInventory get(){
        FInventory inv = new FInventory();

        // 物品栏
        Map<Integer,ItemStack> itemCote = new HashMap<>();

        itemCote.put(0,
                new ItemBuilder(Material.DIAMOND_SWORD)
                        .setName("§c排位模式 §7(右键选择)")
                        .addFlag(ItemFlag.HIDE_UNBREAKABLE)
                        .addFlag(ItemFlag.HIDE_ATTRIBUTES)
                        .addFlag(ItemFlag.HIDE_DESTROYS)
                        .addFlag(ItemFlag.HIDE_ENCHANTS)
                        .addFlag(ItemFlag.HIDE_POTION_EFFECTS)
                        .addFlag(ItemFlag.HIDE_PLACED_ON)
                        .setNbtBoolean("stopClick",true)
                        .setNbtString("gui","SelectPracticeGUI")
                        .setNbtString("queue_type", QueueType.RANKED.name())
                        .setUnbreakable()
                        .toItemStack()
        );
        itemCote.put(1,
                new ItemBuilder(Material.IRON_SWORD)
                        .setName("§c匹配模式 §7(右键选择)")
                        .addFlag(ItemFlag.HIDE_UNBREAKABLE)
                        .addFlag(ItemFlag.HIDE_ATTRIBUTES)
                        .addFlag(ItemFlag.HIDE_DESTROYS)
                        .addFlag(ItemFlag.HIDE_ENCHANTS)
                        .addFlag(ItemFlag.HIDE_POTION_EFFECTS)
                        .addFlag(ItemFlag.HIDE_PLACED_ON)
                        .setNbtBoolean("stopClick",true)
                        .setNbtString("gui","SelectPracticeGUI")
                        .setNbtString("queue_type", QueueType.UNRANKED.name())
                        .setUnbreakable()
                        .toItemStack()
        );
        itemCote.put(2,
                new ItemBuilder(Material.GOLD_SWORD)
                        .setName("§c团队模式 §7(右键选择)")
                        .addFlag(ItemFlag.HIDE_UNBREAKABLE)
                        .addFlag(ItemFlag.HIDE_ATTRIBUTES)
                        .addFlag(ItemFlag.HIDE_DESTROYS)
                        .addFlag(ItemFlag.HIDE_ENCHANTS)
                        .addFlag(ItemFlag.HIDE_POTION_EFFECTS)
                        .addFlag(ItemFlag.HIDE_PLACED_ON)
                        .setNbtBoolean("stopClick",true)
                        .setUnbreakable()
                        .toItemStack()
        );
        itemCote.put(3,
                new ItemBuilder(Material.WOOD_SWORD)
                        .setName("§c人机模式 §7(右键选择)")
                        .addFlag(ItemFlag.HIDE_UNBREAKABLE)
                        .addFlag(ItemFlag.HIDE_ATTRIBUTES)
                        .addFlag(ItemFlag.HIDE_DESTROYS)
                        .addFlag(ItemFlag.HIDE_ENCHANTS)
                        .addFlag(ItemFlag.HIDE_POTION_EFFECTS)
                        .addFlag(ItemFlag.HIDE_PLACED_ON)
                        .setNbtBoolean("stopClick",true)
                        .setUnbreakable()
                        .toItemStack()
        );
        itemCote.put(5,
                new ItemBuilder(Material.BOOK)
                        .setName("§c编辑模式 §7(右键选择)")
                        .addFlag(ItemFlag.HIDE_UNBREAKABLE)
                        .addFlag(ItemFlag.HIDE_ATTRIBUTES)
                        .addFlag(ItemFlag.HIDE_DESTROYS)
                        .addFlag(ItemFlag.HIDE_ENCHANTS)
                        .addFlag(ItemFlag.HIDE_POTION_EFFECTS)
                        .addFlag(ItemFlag.HIDE_PLACED_ON)
                        .setNbtBoolean("stopClick",true)
                        .setNbtString("gui","SelectFKitTypeGUI")
                        .toItemStack()
        );
        itemCote.put(8,
                new ItemBuilder(Material.BED)
                        .setName("§c返回大厅 §7(右键选择)")
                        .addFlag(ItemFlag.HIDE_UNBREAKABLE)
                        .addFlag(ItemFlag.HIDE_ATTRIBUTES)
                        .addFlag(ItemFlag.HIDE_DESTROYS)
                        .addFlag(ItemFlag.HIDE_ENCHANTS)
                        .addFlag(ItemFlag.HIDE_POTION_EFFECTS)
                        .addFlag(ItemFlag.HIDE_PLACED_ON)
                        .setNbtBoolean("stopClick",true)
                        .setNbtString("server","lobby")
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