package cn.mcarl.miars.practiceffa.ui;

import cc.carm.lib.easyplugin.gui.GUI;
import cc.carm.lib.easyplugin.gui.GUIItem;
import cc.carm.lib.easyplugin.gui.GUIType;
import cc.carm.lib.easyplugin.utils.ColorParser;
import cn.mcarl.miars.core.publics.GUIUtils;
import cn.mcarl.miars.storage.entity.MPlayer;
import cn.mcarl.miars.storage.entity.MRank;
import cn.mcarl.miars.storage.storage.data.MPlayerDataStorage;
import cn.mcarl.miars.storage.storage.data.MRankDataStorage;
import cn.mcarl.miars.storage.utils.jsonmessage.JSONMessage;
import cn.mcarl.miars.practiceffa.MiarsPracticeFFA;
import cn.mcarl.miars.practiceffa.entity.GamePlayer;
import cn.mcarl.miars.storage.entity.practice.ArenaState;
import cn.mcarl.miars.storage.entity.practice.Duel;
import cn.mcarl.miars.storage.storage.data.practice.FPlayerDataStorage;
import cn.mcarl.miars.storage.storage.data.practice.PracticeGameDataStorage;
import cn.mcarl.miars.storage.storage.data.practice.PracticeQueueDataStorage;
import cn.mcarl.miars.storage.entity.practice.enums.practice.FKitType;
import cn.mcarl.miars.storage.entity.practice.enums.practice.QueueType;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

/**
 * @Author: carl0
 * @DATE: 2023/1/5 19:54
 */
public class SelectPracticeGUI extends GUI {

    final Player player;

    public SelectPracticeGUI(Player player,QueueType queueType,Player duel) {
        super(GUIType.THREE_BY_NINE, "&0"+queueType.getName()+" Queue");
        this.player = player;

        for (int j = 0; j < 27; j++) {
            setItem(new GUIItem(GUIUtils.getLineItem()),j);
        }

        new BukkitRunnable() {
            @Override
            public void run() {

                if (getOpenedGUI(player)==null){
                    cancel();
                }

                setItem(queueType,duel);

                updateView();
            }
        }.runTaskTimerAsynchronously(MiarsPracticeFFA.getInstance(),0,20L);
    }

    public void setItem(QueueType queueType,Player duel){
        int i = 10;
        for (FKitType ft:FKitType.values()) {
            setItem(i,new GUIItem(CommunityGUIItem.getPracticeTypeItem(player,ft, queueType)){
                @Override
                public void onClick(Player clicker, ClickType type) {

                    if (duel==null){
                        // 开始匹配
                        PracticeQueueDataStorage.getInstance().addQueue(ft,queueType,FPlayerDataStorage.getInstance().getFPlayer(player));

                        // 初始化背包
                        GamePlayer.get(player).initData();
                    }else {
                        if (
                                PracticeQueueDataStorage.getInstance().addDuel(
                                        new Duel(
                                                ft,clicker.getName(),duel.getName(),System.currentTimeMillis(),0
                                        )
                                )
                        ){
                            MPlayer mPlayer = MPlayerDataStorage.getInstance().getMPlayer(duel);
                            MRank mRank = MRankDataStorage.getInstance().getMRank(mPlayer.getRank());
                            player.sendMessage(ColorParser.parse("&r"));
                            player.sendMessage(ColorParser.parse("&b&lSent Request"));
                            player.sendMessage(ColorParser.parse("&b&l ● &fTo: &c"+mRank.getNameColor()+duel.getName()+" &7(&b"+((CraftPlayer) duel).getHandle().ping+" ms&7)"));
                            player.sendMessage(ColorParser.parse("&b&l ● &fKit: &b"+ft.getName()));
                            player.sendMessage(ColorParser.parse("&r"));

                            mPlayer = MPlayerDataStorage.getInstance().getMPlayer(player);
                            mRank = MRankDataStorage.getInstance().getMRank(mPlayer.getRank());
                            duel.sendMessage(ColorParser.parse("&r"));
                            duel.sendMessage(ColorParser.parse("&b&lDuel Request"));
                            duel.sendMessage(ColorParser.parse("&b&l ● &fFrom: "+mRank.getNameColor()+player.getName()+" &7(&b"+((CraftPlayer) player).getHandle().ping+" ms&7)"));
                            duel.sendMessage(ColorParser.parse("&b&l ● &fKit: &b"+ft.getName()));
                            JSONMessage.create()
                                    .then(ColorParser.parse("&a&l [CLICK TO ACCEPT]"))
                                    .tooltip(ColorParser.parse("&7Click to accept"))
                                    .runCommand("/duel accept "+player.getName()+" "+ft.name())
                                    .then(ColorParser.parse("\n&r"))
                                    .send(duel);
                            duel.sendMessage(ColorParser.parse("&r"));
                        }else {
                            clicker.sendMessage(ColorParser.parse("&7请不要重复申请，耐心等待对方回应。"));
                        }

                    }


                    player.closeInventory();
                }
            });
            i++;
        }
    }

    public static void open(Player player,QueueType queueType,Player duel) {

        // 排位本赛季赢得10场胜利
        if (queueType.equals(QueueType.RANKED)){
            List<ArenaState> list = PracticeGameDataStorage.getInstance().getArenaDataByWin(player.getName());
            if (!(list.size()>=10)){
                int last = (10-list.size());
                player.sendMessage(ColorParser.parse("&fYou have to win &b10 Unranked Matches &fto play ranked! If you don't want to, you can buy any rank at &bstore.kazer.gg&f."));
                return;
            }
        }

       // player.closeInventory();
        SelectPracticeGUI gui = new SelectPracticeGUI(player,queueType,duel);
        gui.openGUI(player);
    }
}
