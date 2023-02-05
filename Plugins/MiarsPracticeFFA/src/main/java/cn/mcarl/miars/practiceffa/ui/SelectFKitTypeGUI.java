package cn.mcarl.miars.practiceffa.ui;

import cc.carm.lib.easyplugin.gui.GUI;
import cc.carm.lib.easyplugin.gui.GUIItem;
import cc.carm.lib.easyplugin.gui.GUIType;
import cn.mcarl.miars.storage.enums.FKitType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

/**
 * @Author: carl0
 * @DATE: 2023/1/5 19:54
 */
public class SelectFKitTypeGUI extends GUI {

    final Player player;

    public SelectFKitTypeGUI(Player player) {
        super(GUIType.ONE_BY_NINE, "&0请选择模式...");
        this.player = player;

        setItem(0,new GUIItem(CommunityGUIItem.getPracticeTypeItem(player,FKitType.FFAGAME,null)){
            @Override
            public void onClick(Player clicker, ClickType type) {
                SelectFKitEditGUI.open(player,FKitType.FFAGAME);
            }
        });


        setItem(1,new GUIItem(CommunityGUIItem.getPracticeTypeItem(player,FKitType.NO_DEBUFF,null)){
            @Override
            public void onClick(Player clicker, ClickType type) {
                SelectFKitEditGUI.open(player,FKitType.NO_DEBUFF);
            }
        });

        setItem(8,new GUIItem(CommunityGUIItem.getCancelItem()){
            @Override
            public void onClick(Player clicker, ClickType type) {
                player.closeInventory();
            }
        });

    }

    public static void open(Player player) {
        player.closeInventory();
        SelectFKitTypeGUI gui = new SelectFKitTypeGUI(player);
        gui.openGUI(player);
    }
}
