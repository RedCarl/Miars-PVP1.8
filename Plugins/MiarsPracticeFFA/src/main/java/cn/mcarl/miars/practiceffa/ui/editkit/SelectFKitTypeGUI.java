package cn.mcarl.miars.practiceffa.ui.editkit;

import cc.carm.lib.easyplugin.gui.GUI;
import cc.carm.lib.easyplugin.gui.GUIItem;
import cc.carm.lib.easyplugin.gui.GUIType;
import cn.mcarl.miars.core.publics.GUIUtils;
import cn.mcarl.miars.practiceffa.MiarsPracticeFFA;
import cn.mcarl.miars.practiceffa.ui.CommunityGUIItem;
import cn.mcarl.miars.storage.entity.practice.enums.practice.FKitType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @Author: carl0
 * @DATE: 2023/1/5 19:54
 */
public class SelectFKitTypeGUI extends GUI {

    final Player player;

    public SelectFKitTypeGUI(Player player) {
        super(GUIType.THREE_BY_NINE, "&0Edit Kit");
        this.player = player;

        for (int j = 0; j < 27; j++) {
            setItem(new GUIItem(GUIUtils.getLineItem()),j);
        }

        int i = 10;
        for (FKitType ft:FKitType.values()) {
            setItem(i,new GUIItem(CommunityGUIItem.getPracticeTypeItem(player,ft, null)){
                @Override
                public void onClick(Player clicker, ClickType type) {
                    SelectFKitEditGUI.open(player,ft);
                }
            });
            i++;
        }

    }

    public static void open(Player player) {
        player.closeInventory();
        SelectFKitTypeGUI gui = new SelectFKitTypeGUI(player);
        gui.openGUI(player);
    }
}
