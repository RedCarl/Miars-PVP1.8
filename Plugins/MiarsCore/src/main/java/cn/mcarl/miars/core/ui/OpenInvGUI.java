package cn.mcarl.miars.core.ui;

import cc.carm.lib.easyplugin.gui.GUI;
import cc.carm.lib.easyplugin.gui.GUIItem;
import cc.carm.lib.easyplugin.gui.GUIType;
import cc.carm.lib.easyplugin.utils.ColorParser;
import cn.mcarl.miars.core.utils.ItemBuilder;
import cn.mcarl.miars.storage.entity.ffa.FInventory;
import cn.mcarl.miars.storage.entity.ffa.FKit;
import cn.mcarl.miars.storage.storage.data.practice.FKitDataStorage;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;

/**
 * @Author: carl0
 * @DATE: 2023/1/5 23:53
 */
public class OpenInvGUI extends GUI {

    final Player player;
    final FInventory fInventory;

    public OpenInvGUI(Player player, FInventory fInventory, String name) {
        super(GUIType.SIX_BY_NINE, "&7"+name+"‘s inventory");
        this.player = player;
        this.fInventory = fInventory;
        load();
    }

    public void load(){
        for (int i:fInventory.getItemCote().keySet()){
            ItemStack item = fInventory.getItemCote().get(i);
            setItem(i,new GUIItem(item));
        }

        for (int i:fInventory.getBackpack().keySet()){
            ItemStack item = fInventory.getBackpack().get(i);
            setItem(i,new GUIItem(item));
        }
    }


    public static void open(Player player, FInventory fInventory, String name) {
        player.closeInventory();
        OpenInvGUI gui = new OpenInvGUI(player,fInventory,name);
        gui.openGUI(player);
    }
}
