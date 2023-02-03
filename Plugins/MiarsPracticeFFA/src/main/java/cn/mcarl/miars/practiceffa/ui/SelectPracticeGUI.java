package cn.mcarl.miars.practiceffa.ui;

import cc.carm.lib.easyplugin.gui.GUI;
import cc.carm.lib.easyplugin.gui.GUIItem;
import cc.carm.lib.easyplugin.gui.GUIType;
import cn.mcarl.miars.practiceffa.MiarsPracticeFFA;
import cn.mcarl.miars.practiceffa.kits.FFAGame;
import cn.mcarl.miars.practiceffa.kits.NoDeBuff;
import cn.mcarl.miars.practiceffa.manager.PlayerInventoryManager;
import cn.mcarl.miars.storage.entity.ffa.FKit;
import cn.mcarl.miars.storage.storage.data.practice.FKitDataStorage;
import cn.mcarl.miars.storage.storage.data.practice.FPlayerDataStorage;
import cn.mcarl.miars.storage.storage.data.practice.PracticeQueueDataStorage;
import cn.mcarl.miars.storage.enums.FKitType;
import cn.mcarl.miars.storage.enums.QueueType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.Date;

/**
 * @Author: carl0
 * @DATE: 2023/1/5 19:54
 */
public class SelectPracticeGUI extends GUI {

    final Player player;

    public SelectPracticeGUI(Player player,QueueType queueType) {
        super(GUIType.ONE_BY_NINE, "&0["+queueType+"] 请选择模式...");
        this.player = player;


        new BukkitRunnable() {
            @Override
            public void run() {

                if (getOpenedGUI(player)==null){
                    cancel();
                }

                setFFAGameItem(0,queueType);
                setNODeBuffItem(1,queueType);

                updateView();
            }
        }.runTaskTimerAsynchronously(MiarsPracticeFFA.getInstance(),0,20L);


        setItem(8,new GUIItem(CommunityGUIItem.getCancelItem()){
            @Override
            public void onClick(Player clicker, ClickType type) {
                player.closeInventory();
            }
        });

    }

    // FFAGAME
    public void setFFAGameItem(int slot,QueueType queueType){
        GUIItem guiItem = new GUIItem(CommunityGUIItem.getPracticeTypeItem(player,FKitType.FFAGAME, QueueType.UNRANKED)){
            @Override
            public void onClick(Player clicker, ClickType type) {
                // 开始匹配
                PracticeQueueDataStorage.getInstance().addQueue(FKitType.FFAGAME,queueType, FPlayerDataStorage.getInstance().getFPlayer(player));
                player.closeInventory();

                // 初始化背包
                PlayerInventoryManager.getInstance().setQueue(player);

                // 初始化Kit
                if (FKitDataStorage.getInstance().getFKitData(player,FKitType.FFAGAME).size()==0){
                    FKitDataStorage.getInstance().putFKitData(new FKit(
                            null,
                            player.getUniqueId().toString(),
                            FKitType.FFAGAME,
                            "Default",
                            FFAGame.get(),
                            0,
                            null,
                            new Date(System.currentTimeMillis())

                    ));
                }
            }
        };

        setItem(slot,guiItem);
    }

    // NO_DEBUFF
    public void setNODeBuffItem(int slot,QueueType queueType){
        GUIItem guiItem = new GUIItem(CommunityGUIItem.getPracticeTypeItem(player,FKitType.NO_DEBUFF, QueueType.UNRANKED)){
            @Override
            public void onClick(Player clicker, ClickType type) {
                // 开始匹配
                PracticeQueueDataStorage.getInstance().addQueue(FKitType.NO_DEBUFF,queueType,FPlayerDataStorage.getInstance().getFPlayer(player));
                player.closeInventory();

                // 初始化背包
                PlayerInventoryManager.getInstance().setQueue(player);

                // 初始化Kit
                if (FKitDataStorage.getInstance().getFKitData(player,FKitType.NO_DEBUFF).size()==0) {
                    FKitDataStorage.getInstance().putFKitData(new FKit(
                            null,
                            player.getUniqueId().toString(),
                            FKitType.NO_DEBUFF,
                            "Default",
                            NoDeBuff.get(),
                            0,
                            null,
                            new Date(System.currentTimeMillis())

                    ));
                }
            }
        };

        setItem(slot,guiItem);
    }



    public static void open(Player player,QueueType queueType) {
        player.closeInventory();
        SelectPracticeGUI gui = new SelectPracticeGUI(player,queueType);
        gui.openGUI(player);
    }
}
