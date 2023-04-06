package cn.mcarl.miars.practiceffa.ui;

import cc.carm.lib.easyplugin.gui.GUI;
import cc.carm.lib.easyplugin.gui.GUIItem;
import cc.carm.lib.easyplugin.gui.GUIType;
import cn.mcarl.miars.core.publics.GUIUtils;
import cn.mcarl.miars.storage.utils.ItemBuilder;
import cn.mcarl.miars.storage.entity.practice.enums.practice.QueueType;
import cn.mcarl.miars.storage.storage.data.practice.PracticeArenaStateDataStorage;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

public class SoloMenuGUI extends GUI {
    final Player player;

    public SoloMenuGUI(Player player) {
        super(GUIType.THREE_BY_NINE, "&0Solo Menu");
        this.player = player;
        load();
    }

    public void load(){

        for (int j = 0; j < 27; j++) {
            setItem(new GUIItem(GUIUtils.getLineItem()),j);
        }


        setItem(11,setUnRankItem());
        setItem(15,setRankItem());

    }

    public GUIItem setUnRankItem(){
        return new GUIItem(new ItemBuilder(Material.GLASS_BOTTLE)
                .setName("&6&k|&r &eUnRanked &k|")
                .setLore(
                        "",
                        "&7Playing: &e"+ PracticeArenaStateDataStorage.getInstance().getGamePlayersByQueueType(QueueType.UNRANKED),
                        "&7Click to Join Ranked Model."
                )
                .toItemStack()){
            @Override
            public void onClick(Player clicker, ClickType type) {
                SelectPracticeGUI.open(clicker,QueueType.UNRANKED,null);
            }
        };
    }



    public GUIItem setRankItem(){
        return new GUIItem(new ItemBuilder(Material.EXP_BOTTLE)
                .setName("&e&k|&r &6Ranked &k|")
                .setLore(
                        "",
                        "&7Playing: &6"+ PracticeArenaStateDataStorage.getInstance().getGamePlayersByQueueType(QueueType.RANKED),
                        "&7Click to Join Ranked Model."
                )
                .toItemStack()){
            @Override
            public void onClick(Player clicker, ClickType type) {
                SelectPracticeGUI.open(clicker,QueueType.RANKED,null);
            }
        };
    }


    public static void open(Player player) {
        player.closeInventory();
        SoloMenuGUI gui = new SoloMenuGUI(player);
        gui.openGUI(player);
    }

}
