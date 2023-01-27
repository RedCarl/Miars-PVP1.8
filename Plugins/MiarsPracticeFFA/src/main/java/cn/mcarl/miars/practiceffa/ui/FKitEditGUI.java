package cn.mcarl.miars.practiceffa.ui;

import cc.carm.lib.easyplugin.gui.GUI;
import cc.carm.lib.easyplugin.gui.GUIItem;
import cc.carm.lib.easyplugin.gui.GUIType;
import cc.carm.lib.easyplugin.utils.ColorParser;
import cn.mcarl.miars.core.utils.ItemBuilder;
import cn.mcarl.miars.storage.entity.ffa.FInventory;
import cn.mcarl.miars.storage.entity.ffa.FKit;
import cn.mcarl.miars.storage.storage.data.FKitDataStorage;
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
public class FKitEditGUI extends GUI {

    final Player player;
    final FKit fKit;

    public FKitEditGUI(Player player,FKit fKit) {
        super(GUIType.SIX_BY_NINE, "&0编辑好后记得保存!");
        this.player = player;
        this.fKit = fKit;
        load();
    }

    public void load(){
        for (int i = 0; i <= 35; i++) {
            setItem(i,new GUIItem(new ItemStack(Material.AIR)){
                @Override
                public void rawClickAction(InventoryClickEvent event) {
                    event.setCancelled(false);

                    if (event.isShiftClick()){
                        event.setCancelled(true);
                    }
                }
            });
        }

        for (int i:fKit.getInventory().getItemCote().keySet()){
            ItemStack item = fKit.getInventory().getItemCote().get(i);
            setItem(i,new GUIItem(item){
                @Override
                public void rawClickAction(InventoryClickEvent event) {
                    event.setCancelled(false);

                    if (event.isShiftClick()){
                        event.setCancelled(true);
                    }
                }
            });
        }

        for (int i:fKit.getInventory().getBackpack().keySet()){
            ItemStack item = fKit.getInventory().getBackpack().get(i);
            setItem(i,new GUIItem(item){
                @Override
                public void rawClickAction(InventoryClickEvent event) {
                    event.setCancelled(false);

                    if (event.isShiftClick()){
                        event.setCancelled(true);
                    }
                }
            });
        }

        setItem(new GUIItem(CommunityGUIItem.getLineItem()),36,37,38,39,40,41,42,43,44);

        // 保存
        setItem(53,new GUIItem(
                new ItemBuilder(Material.PAPER)
                        .setName("&a保存数据")
                        .toItemStack()
        ){
            @Override
            public void onClick(Player clicker, ClickType type) {

                // 背包内容
                FInventory inv = fKit.getInventory();
                inv.getItemCote().clear();
                inv.getBackpack().clear();

                int slot = 0;
                int itemCoteSize = 9;
                int backpackSize = 27;
                for (ItemStack i:player.getOpenInventory().getTopInventory().getContents()){

                    if (itemCoteSize>0){// 物品栏

                        if (i!=null){
                            inv.getItemCote().put(slot, i);
                        }

                        itemCoteSize--;
                    }else if (backpackSize>0){// 背包

                        if (i!=null){
                            inv.getBackpack().put(slot, i);
                        }

                        backpackSize--;
                    }

                    slot++;
                }

                fKit.setInventory(inv);

                FKitDataStorage.getInstance().putFKitData(fKit);
                player.closeInventory();
                player.sendMessage(ColorParser.parse("&7数据保存成功..."));
            }
        });
    }

    @Override
    public void onDrag(InventoryDragEvent event) {
        if (event.getInventory().getTitle().equals(getGUIName())){
            for (Integer i:event.getRawSlots()){
                if (i>35){
                    event.setCancelled(true);
                }
            }
        }
    }

    public static void open(Player player, FKit fKit) {
        player.closeInventory();
        FKitEditGUI gui = new FKitEditGUI(player,fKit);
        gui.openGUI(player);
    }
}
