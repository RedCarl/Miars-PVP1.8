package cn.mcarl.miars.practiceffa.ui;

import cc.carm.lib.easyplugin.gui.GUI;
import cc.carm.lib.easyplugin.gui.GUIItem;
import cc.carm.lib.easyplugin.gui.GUIType;
import cn.mcarl.miars.core.publics.GUIUtils;
import cn.mcarl.miars.storage.entity.practice.enums.practice.FKitType;
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

        int i = 0;
        for (FKitType ft:FKitType.values()) {
            setItem(i,new GUIItem(CommunityGUIItem.getPracticeTypeItem(player,ft,null)){
                @Override
                public void onClick(Player clicker, ClickType type) {
                    SelectFKitEditGUI.open(player,ft);
                }
            });
            i++;
        }

        setItem(8,new GUIItem(GUIUtils.getCancelItem()){
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
