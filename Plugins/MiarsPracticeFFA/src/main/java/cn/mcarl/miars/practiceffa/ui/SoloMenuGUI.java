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
        super(GUIType.THREE_BY_NINE, "&0Game Menu");
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
        return new GUIItem(new ItemBuilder(Material.IRON_SWORD)
                .setName("&a&lUnranked")
                .setLore(
                        "&7Casual 1v1s with",
                        "&7no loss penalty.",
                        "",
                        "&fPlaying: &a"+ PracticeArenaStateDataStorage.getInstance().getGamePlayersByQueueType(QueueType.UNRANKED),
                        "",
                        "&aClick to play!"
                )
                .toItemStack()){
            @Override
            public void onClick(Player clicker, ClickType type) {
                SelectPracticeGUI.open(clicker,QueueType.UNRANKED,null);
            }
        };
    }



    public GUIItem setRankItem(){
        return new GUIItem(new ItemBuilder(Material.DIAMOND_SWORD)
                .setName("&b&lRanked")
                .setLore(
                        "&7Ranked 1v1s with",
                        "&7no loss penalty.",
                        "",
                        "&fPlaying: &b"+ PracticeArenaStateDataStorage.getInstance().getGamePlayersByQueueType(QueueType.RANKED),
                        "",
                        "&bClick to play!"
                )
                .toItemStack()){
            @Override
            public void onClick(Player clicker, ClickType type) {
                SelectPracticeGUI.open(clicker,QueueType.RANKED,null);
            }
        };
    }


    public static void open(Player player) {
        SoloMenuGUI gui = new SoloMenuGUI(player);
        gui.openGUI(player);
    }

}
