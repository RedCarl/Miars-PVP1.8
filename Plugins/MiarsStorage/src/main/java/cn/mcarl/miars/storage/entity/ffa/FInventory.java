package cn.mcarl.miars.storage.entity.ffa;

import cn.mcarl.miars.storage.enums.FKitType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FInventory {

    /**
     * 库存类型
     */
    private FKitType type;

    /**
     * 头盔 39
     */
    private ItemStack helmet;

    /**
     * 胸甲 38
     */
    private ItemStack chestPlate;

    /**
     * 裤腿 37
     */
    private ItemStack leggings;

    /**
     * 靴子 36
     */
    private ItemStack boots;

    /**
     * 背包
     */
    private Map<Integer,ItemStack> backpack;

    /**
     * 物品栏
     */
    private Map<Integer,ItemStack> itemCote;
}
