package cn.mcarl.miars.practiceffa.ui.editkit;

import cc.carm.lib.easyplugin.gui.GUI;
import cc.carm.lib.easyplugin.gui.GUIItem;
import cc.carm.lib.easyplugin.gui.GUIType;
import cc.carm.lib.easyplugin.utils.ColorParser;
import cn.mcarl.miars.core.publics.GUIUtils;
import cn.mcarl.miars.practiceffa.utils.FFAUtil;
import cn.mcarl.miars.storage.utils.ItemBuilder;
import cn.mcarl.miars.storage.entity.ffa.FInventory;
import cn.mcarl.miars.storage.entity.ffa.FKit;
import cn.mcarl.miars.storage.storage.data.practice.FKitDataStorage;
import org.bukkit.Material;
import org.bukkit.Sound;
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
        super(GUIType.SIX_BY_NINE, "&0Archer "+fKit.getName());
        this.player = player;
        this.fKit = fKit;
        load();
    }

    public void load(){

        init();

        setItem(new GUIItem(GUIUtils.getLineItem()),36,37,38,39,40,41,42,43,44);

        // 删除
        setItem(45,new GUIItem(
                new ItemBuilder(Material.BARRIER)
                        .setName("&cDelete Loadout")
                        .toItemStack()
        ){
            @Override
            public void onClick(Player clicker, ClickType type) {
                if (fKit.getId()!=null){
                    FKitDataStorage.getInstance().deleteFKitDataById(fKit.getId());
                }
                player.closeInventory();
                player.sendMessage(ColorParser.parse("&cLoadout delete."));
            }
        });

        // 重置
        setItem(46,new GUIItem(
                new ItemBuilder(Material.REDSTONE)
                        .setName("&6Reset Loadout")
                        .toItemStack()
        ){
            @Override
            public void onClick(Player clicker, ClickType type) {
                fKit.setInventory(FFAUtil.getByFKitType(fKit.getType()));
                init();
                updateView();
                clicker.playSound(clicker.getLocation(), Sound.ORB_PICKUP,1,1);
            }
        });

        // 保存
        setItem(53,new GUIItem(
                new ItemBuilder(Material.INK_SACK)
                        .setName("&aSave Loadout")
                        .setData((short) 10)
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
                player.sendMessage(ColorParser.parse("&7Data save..."));
            }
        });
    }

    public void init(){
        for (int i = 0; i <= 35; i++) {
            setItem(i,new GUIItem(new ItemStack(Material.AIR)){
                @Override
                public void rawClickAction(InventoryClickEvent event) {
                    event.setCancelled(false);

                    if (event.isShiftClick() || event.getHotbarButton()!=-1){
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

                    if (event.isShiftClick() || event.getHotbarButton()!=-1){
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
                    if (event.isShiftClick() || event.getHotbarButton()!=-1){
                        event.setCancelled(true);
                    }
                }
            });
        }

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
        FKitEditGUI gui = new FKitEditGUI(player,fKit);
        gui.openGUI(player);
    }
}
