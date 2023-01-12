package cn.mcarl.miars.storage.entity;

import cn.mcarl.miars.storage.enums.FKitType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * 因为ItemStack 无法直接fastjson序列化，使用该类作为转换
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FInventoryByte {

    /**
     * 库存类型
     */
    private FKitType type;

    /**
     * 头盔 39
     */
    private byte[] helmet;

    /**
     * 胸甲 38
     */
    private byte[] chestPlate;

    /**
     * 裤腿 37
     */
    private byte[] leggings;

    /**
     * 靴子 36
     */
    private byte[] boots;

    /**
     * 背包
     */
    private Map<Integer,byte[]> backpack;

    /**
     * 物品栏
     */
    private Map<Integer,byte[]> itemCote;

}
