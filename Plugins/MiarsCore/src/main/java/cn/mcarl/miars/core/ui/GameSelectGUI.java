package cn.mcarl.miars.core.ui;

import cc.carm.lib.easyplugin.gui.GUI;
import cc.carm.lib.easyplugin.gui.GUIItem;
import cc.carm.lib.easyplugin.gui.GUIType;
import cn.mcarl.miars.core.publics.GUIUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

public class GameSelectGUI extends GUI {
    public GameSelectGUI(Player player,String type) {
        super(GUIType.FOUR_BY_NINE, "&0游玩");


        setItem(31, new GUIItem(GUIUtils.getCloseItem()){
            @Override
            public void onClick(Player clicker, ClickType type) {
                clicker.closeInventory();
            }
        });
    }
    public static void open(Player player,String type) {
        player.closeInventory();
        GameSelectGUI gui = new GameSelectGUI(player,type);
        gui.openGUI(player);
    }
}
