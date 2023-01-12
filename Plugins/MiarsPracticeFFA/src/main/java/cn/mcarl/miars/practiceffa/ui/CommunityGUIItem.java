package cn.mcarl.miars.practiceffa.ui;

import cn.mcarl.miars.core.utils.ItemBuilder;
import cn.mcarl.miars.storage.enums.FKitType;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

/**
 * @Author: carl0
 * @DATE: 2023/1/5 20:08
 */
public class CommunityGUIItem {

    /**
     * 确认按钮
     */
    public static ItemStack getConfirmItem() {
        return new ItemBuilder(Material.STAINED_GLASS_PANE)
                .setName("&a确认")
                .setData((short) 5)
                .toItemStack();
    }

    /**
     * 取消按钮
     */
    public static ItemStack getCancelItem() {
        return new ItemBuilder(Material.STAINED_GLASS_PANE)
                .setName("&c取消")
                .setData((short) 14)
                .toItemStack();
    }

    /**
     * 分割线
     */
    public static ItemStack getLineItem() {
        return new ItemBuilder(Material.STAINED_GLASS_PANE)
                .setName("&r")
                .setData((short) 15)
                .toItemStack();
    }

    /**
     * 游戏模式
     */
    public static ItemStack getKitTypeItem(FKitType type) {

        switch (type){
            case NO_DEBUFF -> {
                return new ItemBuilder(Material.POTION)
                        .setData((short) 16421)
                        .setName("&cNoDeBuff")
                        .setItemFlags(ItemFlag.HIDE_POTION_EFFECTS)
                        .toItemStack();
            }
            case FFAGAME -> {
                return new ItemBuilder(Material.FISHING_ROD)
                        .setName("&cFFAGAME")
                        .toItemStack();
            }
        }

        return new ItemBuilder(Material.BARRIER)
                .setName("&cERROR")
                .toItemStack();
    }

}
