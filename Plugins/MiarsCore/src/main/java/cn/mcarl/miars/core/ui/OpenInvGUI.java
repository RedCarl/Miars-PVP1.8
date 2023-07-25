package cn.mcarl.miars.core.ui;

import cc.carm.lib.easyplugin.gui.GUI;
import cc.carm.lib.easyplugin.gui.GUIItem;
import cc.carm.lib.easyplugin.gui.GUIType;
import cn.mcarl.miars.core.publics.GUIUtils;
import cn.mcarl.miars.storage.utils.ItemBuilder;
import cn.mcarl.miars.storage.entity.ffa.FInventory;
import cn.mcarl.miars.storage.entity.practice.ArenaState;
import cn.mcarl.miars.storage.entity.practice.PlayerState;
import cn.mcarl.miars.storage.utils.BukkitUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

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
        super(GUIType.SIX_BY_NINE, "&7Inventory of "+name);
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

        for (int i = 36; i < 45; i++) {
            setItem(i, new GUIItem(GUIUtils.getLineItem()));
        }

        setItem(45,new GUIItem(fInventory.getHelmet()));
        setItem(46,new GUIItem(fInventory.getChestPlate()));
        setItem(47,new GUIItem(fInventory.getLeggings()));
        setItem(48,new GUIItem(fInventory.getBoots()));

        setItem(49,new GUIItem(
                new ItemBuilder(Material.SKULL_ITEM)
                        .setData((short) 3)
                        .setSkullOwner(name)
                        .setName("&bPlayer Information")
                        .setLore(
                                "",
                                "&fHealth: &b"+playerState.getHealth()+"/20",
                                "&fHunger: &b"+playerState.getHunger()+"/20"
                        )
                .toItemStack()
        ));
        List<String> potion = new ArrayList<>();
        playerState.getPotionEffects().forEach(i->{
            AtomicReference<String> lore = new AtomicReference<>("");
            i.keySet().forEach(a-> lore.set("&f"+a +" &7- &b"+ i.get(a).toString()));
            potion.add(lore.get());
        });

        setItem(50,new GUIItem(
                new ItemBuilder(Material.POTION)
                        .setName("&bPotion Effects")
                        .setLore(potion)
                .toItemStack()
        ));

        setItem(51,getHealthPotions());
        setItem(52,getMatchStats());
        setItem(53,getViewOtherItem());
    }

    public GUIItem getViewOtherItem(){
        String nameV = this.name.equals(state.getPlayerA())?state.getPlayerB():state.getPlayerA();

        return new GUIItem(
                new ItemBuilder(Material.LEVER)
                        .setName("&bView "+nameV+"'s Inventory")
                        .setLore("&r")
                        .setLore("&eClick to swap over to "+nameV+"'s inventory")
                        .toItemStack()
        ){
            @Override
            public void onClick(Player clicker, ClickType type) {
                OpenInvGUI.open(clicker,state,nameV);
            }
        };
    }

    public GUIItem getPlayerHp(){
        return new GUIItem(
                new ItemBuilder(Material.SKULL_ITEM)
                        .setName("&7血量: &c"+playerState.getHealth())
                        .toItemStack()
        );
    }

    public GUIItem getPlayerFoot(){
        return new GUIItem(
                new ItemBuilder(Material.RAW_BEEF, (int) playerState.getHunger())
                        .setName("&7饥饿: &6"+playerState.getHunger())
                        .toItemStack()
        );
    }

    public GUIItem getHealthPotions(){
        int i = 0;
        ItemStack heal = new ItemBuilder(Material.POTION, 1).setData((short) 16421).toItemStack();
        for (ItemStack itemStack:fInventory.getBackpack().values()){
            if (itemStack!=null && itemStack.equals(heal)){
                i++;
            }
        }
        for (ItemStack itemStack:fInventory.getItemCote().values()){
            if (itemStack!=null && itemStack.equals(heal)){
                i++;
            }
        }
        return new GUIItem(
                new ItemBuilder(Material.POTION, i)
                        .setData((short) 16421)
                        .setName("&bPotion Count")
                        .setLore(
                                "&r",
                                "&a"+name+" &fhad &b"+i+" &fhealth potions left."
                        )
                        .toItemStack()
        );
    }
    public GUIItem getMatchStats(){
        return new GUIItem(
                new ItemBuilder(Material.PAPER)
                        .setName("&bMatch Stats")
                        .setLore(
                                "&r",
                                "&bCombat Stats",
                                "&r",
                                "&bPotion Stats"
                        )
                        .toItemStack()
        );
    }


    public static void open(Player player, ArenaState state, String name) {
        OpenInvGUI gui = new OpenInvGUI(player,state,name);
        gui.openGUI(player);
    }
}
