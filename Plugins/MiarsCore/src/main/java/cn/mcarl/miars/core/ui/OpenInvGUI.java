package cn.mcarl.miars.core.ui;

import cc.carm.lib.easyplugin.gui.GUI;
import cc.carm.lib.easyplugin.gui.GUIItem;
import cc.carm.lib.easyplugin.gui.GUIType;
import cc.carm.lib.easyplugin.utils.ColorParser;
import cn.mcarl.miars.core.utils.ItemBuilder;
import cn.mcarl.miars.storage.entity.ffa.FInventory;
import cn.mcarl.miars.storage.entity.ffa.FKit;
import cn.mcarl.miars.storage.entity.practice.ArenaState;
import cn.mcarl.miars.storage.entity.practice.PlayerState;
import cn.mcarl.miars.storage.storage.data.practice.FKitDataStorage;
import cn.mcarl.miars.storage.utils.BukkitUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: carl0
 * @DATE: 2023/1/5 23:53
 */
public class OpenInvGUI extends GUI {

    final Player player;
    final ArenaState state;
    final String name;
    final PlayerState playerState;
    final FInventory fInventory;

    public OpenInvGUI(Player player, ArenaState state, String name) {
        super(GUIType.SIX_BY_NINE, "&7"+name+" 的信息");
        this.player = player;
        this.name = name;
        this.playerState = name.equals(state.getPlayerA()) ? state.getPlayerStateA() : state.getPlayerStateB();
        this.state = state;
        this.fInventory = BukkitUtils.byteConvertItemStack(name.equals(state.getPlayerA()) ? state.getAFInventory() : state.getBFInventory());
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


        setItem(45,getViewOtherItem());

        setItem(48,getPlayerHp());
        setItem(49,getPlayerFoot());
        setItem(50,getHealthPotions());


        setItem(53,getViewOtherItem());
    }

    public GUIItem getViewOtherItem(){
        String nameV = this.name.equals(state.getPlayerA())?state.getPlayerB():state.getPlayerA();

        return new GUIItem(
                new ItemBuilder(Material.ARROW)
                        .setName("&a"+nameV+" 的信息")
                        .setLore("&7点击查看预览")
                        .toItemStack()
        ){};
    }

    public GUIItem getPlayerHp(){
        return new GUIItem(
                new ItemBuilder(Material.SKULL_ITEM)
                        .setName("&7血量: &a"+playerState.getHealth())
                        .toItemStack()
        );
    }

    public GUIItem getPlayerFoot(){
        return new GUIItem(
                new ItemBuilder(Material.RAW_BEEF)
                        .setName("&7饥饿: &a"+playerState.getHunger())
                        .toItemStack()
        );
    }

    public GUIItem getHealthPotions(){
        int i = 0;
        ItemStack heal = new ItemBuilder(Material.POTION, 1).setData((short) 16421).toItemStack();
        for (ItemStack itemStack:fInventory.getBackpack().values()){
            if (itemStack.equals(heal)){
                i++;
            }
        }
        return new GUIItem(
                new ItemBuilder(Material.RAW_BEEF)
                        .setName("&7治疗药水: &a"+i)
                        .toItemStack()
        );
    }


    public static void open(Player player, ArenaState state, String name) {
        player.closeInventory();
        OpenInvGUI gui = new OpenInvGUI(player,state,name);
        gui.openGUI(player);
    }
}
