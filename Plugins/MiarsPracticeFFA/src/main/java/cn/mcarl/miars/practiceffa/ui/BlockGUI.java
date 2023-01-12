package cn.mcarl.miars.practiceffa.ui;

import cc.carm.lib.easyplugin.gui.GUI;
import cc.carm.lib.easyplugin.gui.GUIItem;
import cc.carm.lib.easyplugin.gui.GUIType;
import cn.mcarl.miars.core.utils.ItemFacAPI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * @Author: carl0
 * @DATE: 2023/1/7 23:16
 */
public class BlockGUI extends GUI {
    public BlockGUI() {
        super(GUIType.SIX_BY_NINE, "&0快拿走它们！");


        for (int i = 0; i <= 15; i++) {
            //设置物品
            ItemStack WOOL = ItemFacAPI.getItemStackWithDurability(Material.WOOL,(short)i);
            WOOL.setAmount(64);
            setItem(i,new GUIItem(WOOL){
                @Override
                public void rawClickAction(InventoryClickEvent event) {
                    event.setCancelled(false);
                }
            });
        }
        for (int i = 0; i <= 15; i++) {
            //设置物品
            ItemStack GLASS = ItemFacAPI.getItemStackWithDurability(Material.STAINED_GLASS,(short)i);
            GLASS.setAmount(64);
            setItem(16+i,new GUIItem(GLASS){
                @Override
                public void rawClickAction(InventoryClickEvent event) {
                    event.setCancelled(false);
                }
            });
        }

        for (int i = 0; i <= 15; i++) {
            //设置物品
            ItemStack CLAY = ItemFacAPI.getItemStackWithDurability(Material.STAINED_CLAY,(short)i);
            CLAY.setAmount(64);
            setItem(31+i,new GUIItem(CLAY){
                @Override
                public void rawClickAction(InventoryClickEvent event) {
                    event.setCancelled(false);
                }
            });
        }
    }


    public static void open(Player player) {
        player.closeInventory();
        BlockGUI gui = new BlockGUI();
        gui.openGUI(player);
    }
}
