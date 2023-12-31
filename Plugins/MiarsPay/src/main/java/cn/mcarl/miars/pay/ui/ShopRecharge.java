package cn.mcarl.miars.pay.ui;

import cc.carm.lib.easyplugin.gui.GUI;
import cc.carm.lib.easyplugin.gui.GUIItem;
import cc.carm.lib.easyplugin.gui.GUIType;
import cn.mcarl.miars.core.publics.GUIUtils;
import cn.mcarl.miars.pay.ui.common.ShopFoot;
import org.bukkit.entity.Player;

import java.util.List;

public class ShopRecharge extends GUI {
    Player player;
    public ShopRecharge(Player player) {
        super(GUIType.SIX_BY_NINE, "&0商城 / 充值系统");

        this.player = player;

        load();
    }

    public void load(){

        //底部
        setItem(new GUIItem(GUIUtils.getLineItem()),36,37,38,39,40,41,42,43,44);
        List<GUIItem> guiItems = new ShopFoot(player).get();
        for (int i = 0; i < guiItems.size(); i++) {
            setItem(45+i,guiItems.get(i));
        }
    }


    public static void open(Player player) {
        ShopRecharge gui = new ShopRecharge(player);
        gui.openGUI(player);
    }
}
