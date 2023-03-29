package cn.mcarl.miars.core.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * @Author: carl0
 * @DATE: 2023/2/7 13:04
 */
public class GUIUtils {

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
     * 关闭按钮
     */
    public static ItemStack getCloseItem() {
        return new ItemBuilder(Material.BARRIER)
                .setName("&c关闭")
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

}
