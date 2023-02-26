package cn.mcarl.miars.lobby.manager;

import cn.mcarl.miars.core.ui.RanksGUI;
import cn.mcarl.miars.core.ui.ServerMenuGUI;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @Author: carl0
 * @DATE: 2023/1/5 21:06
 */
public class ItemInteractManager {
    private static final ItemInteractManager instance = new ItemInteractManager();
    public static ItemInteractManager getInstance() {
        return instance;
    }

    public void init(ItemStack i, Player p){

        NBTItem nbtItem = new NBTItem(i);

        //GUI
        switch (nbtItem.getString("gui")){
            case "RanksGUI" -> {
                RanksGUI.open(p);
            }
            case "ServerMenu" -> {
                ServerMenuGUI.open(p,"游戏选择");
            }
        }
    }
}
