package cn.mcarl.miars.lobby.ui;

import cc.carm.lib.easyplugin.gui.GUI;
import cc.carm.lib.easyplugin.gui.GUIItem;
import cc.carm.lib.easyplugin.gui.GUIType;
import cn.mcarl.miars.pay.enums.PaywayType;
import cn.mcarl.miars.pay.manager.PayManager;
import cn.mcarl.miars.storage.entity.practice.enums.practice.FKitType;
import cn.mcarl.miars.storage.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

public class RechargeSelect extends GUI {
    public RechargeSelect(Player player, double money) {
        super(GUIType.ONE_BY_NINE, "&0支付方式 &7("+money+"元)");


        setItem(2,
                new GUIItem(
                        new ItemBuilder(Material.SKULL_ITEM)
                                .setData((short)3)
                                .setName("&cQQ支付")
                                .setSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzk5MjQ2NjMyOGFlYTE4MjVhMmEzMzZiMTg5NGI3N2QxNmQ3NjE4NjI0YWFhNWZiNjRlODFkYTMxZmUyZjAifX19")
                                .toItemStack()
                ){
                    @Override
                    public void onClick(Player clicker, ClickType type) {
                        player.closeInventory();
                        PayManager.initiatePay(player, PaywayType.TENPAY,money);
                    }
                }
        );

        setItem(4,
                new GUIItem(
                        new ItemBuilder(Material.SKULL_ITEM)
                                .setData((short)3)
                                .setName("&a微信支付")
                                .setSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWY5YTQwMWMzNzRhZDcwODNmMjVhZGNkZDUyYjQ2YTc0NzQyYzQ4YmEyZTM5ZGQ2YmMzZTAwMTAzZjJmOThkOSJ9fX0=")
                                .toItemStack()
                ){
                    @Override
                    public void onClick(Player clicker, ClickType type) {
                        player.closeInventory();
                        PayManager.initiatePay(player, PaywayType.WECHAT_JS,money);
                    }
                }
        );

        setItem(6,
                new GUIItem(
                        new ItemBuilder(Material.SKULL_ITEM)
                                .setData((short)3)
                                .setName("&b支付宝")
                                .setSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDA5YWRkNDZlMWMxZjNkMzBkOThkYjQ1NjNkNTMyNjE3MGUyZjk0ZjRlYTY5YWY2ZDJmNzc1NDk5ZTM3MGVmNCJ9fX0=")
                                .toItemStack()
                ){
                    @Override
                    public void onClick(Player clicker, ClickType type) {
                        player.closeInventory();
                        PayManager.initiatePay(player, PaywayType.ALIPAY,money);
                    }
                }
        );
    }


    public static void open(Player player, double money) {
        RechargeSelect gui = new RechargeSelect(player,money);
        gui.openGUI(player);
    }
}
