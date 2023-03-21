package cn.mcarl.miars.practiceffa.ui;

import cc.carm.lib.easyplugin.gui.GUI;
import cc.carm.lib.easyplugin.gui.GUIItem;
import cc.carm.lib.easyplugin.gui.GUIType;
import cc.carm.lib.easyplugin.utils.ColorParser;
import cn.mcarl.miars.core.utils.GUIUtils;
import cn.mcarl.miars.practiceffa.MiarsPracticeFFA;
import cn.mcarl.miars.practiceffa.kits.BuildUHC;
import cn.mcarl.miars.practiceffa.kits.FFAGame;
import cn.mcarl.miars.practiceffa.kits.NoDeBuff;
import cn.mcarl.miars.practiceffa.manager.PlayerInventoryManager;
import cn.mcarl.miars.storage.entity.ffa.FInventory;
import cn.mcarl.miars.storage.entity.ffa.FKit;
import cn.mcarl.miars.storage.entity.practice.ArenaState;
import cn.mcarl.miars.storage.storage.data.practice.FKitDataStorage;
import cn.mcarl.miars.storage.storage.data.practice.FPlayerDataStorage;
import cn.mcarl.miars.storage.storage.data.practice.PracticeGameDataStorage;
import cn.mcarl.miars.storage.storage.data.practice.PracticeQueueDataStorage;
import cn.mcarl.miars.storage.enums.practice.FKitType;
import cn.mcarl.miars.storage.enums.practice.QueueType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.Date;
import java.util.List;

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

                setNODeBuffItem(queueType);

                updateView();
            }
        }.runTaskTimerAsynchronously(MiarsPracticeFFA.getInstance(),0,20L);


        setItem(8,new GUIItem(GUIUtils.getCancelItem()){
            @Override
            public void onClick(Player clicker, ClickType type) {
                player.closeInventory();
            }
        });

    }

    public void setNODeBuffItem(QueueType queueType){
        int i = 0;
        for (FKitType ft:FKitType.values()) {
            if (ft==FKitType.PRACTICE || ft==FKitType.FFAGAME){
                continue;
            }
            setItem(i,new GUIItem(CommunityGUIItem.getPracticeTypeItem(player,ft, queueType)){
                @Override
                public void onClick(Player clicker, ClickType type) {
                    // 开始匹配
                    PracticeQueueDataStorage.getInstance().addQueue(ft,queueType,FPlayerDataStorage.getInstance().getFPlayer(player));
                    player.closeInventory();

                    // 初始化背包
                    PlayerInventoryManager.getInstance().setQueue(player);

                    // 初始化Kit
                    if (FKitDataStorage.getInstance().getFKitData(player,ft).size()==0) {
                        FKitDataStorage.getInstance().putFKitData(new FKit(
                                null,
                                player.getUniqueId().toString(),
                                ft,
                                "Default",
                                getFI(ft),
                                0,
                                null,
                                new Date(System.currentTimeMillis())

                        ));
                    }
                }
            });
            i++;
        }
    }


    public FInventory getFI(FKitType ft){
        switch (ft){
            case FFAGAME -> {
                return FFAGame.get();
            }
            case NO_DEBUFF -> {
                return NoDeBuff.get();
            }
            case BUILD_UHC -> {
                return BuildUHC.get();
            }
        }

        return null;
    }



    public static void open(Player player,QueueType queueType) {

        // 排位本赛季赢得10场胜利
        if (queueType.equals(QueueType.RANKED)){
            List<ArenaState> list = PracticeGameDataStorage.getInstance().getArenaDataByWin(player.getName());
            if (!(list.size()>=10)){
                player.sendMessage(ColorParser.parse("&7请在本赛季再赢得 &c"+(10-list.size())+" &7场匹配比赛，才有资格进入排位赛。"));
                return;
            }
        }

        player.closeInventory();
        SelectPracticeGUI gui = new SelectPracticeGUI(player,queueType);
        gui.openGUI(player);
    }
}
