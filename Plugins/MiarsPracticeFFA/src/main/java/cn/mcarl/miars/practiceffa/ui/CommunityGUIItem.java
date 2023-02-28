package cn.mcarl.miars.practiceffa.ui;

import cn.mcarl.miars.core.utils.ItemBuilder;
import cn.mcarl.miars.core.utils.ToolUtils;
import cn.mcarl.miars.storage.entity.practice.ArenaState;
import cn.mcarl.miars.storage.entity.practice.DailyStreak;
import cn.mcarl.miars.storage.entity.practice.QueueInfo;
import cn.mcarl.miars.storage.enums.FKitType;
import cn.mcarl.miars.storage.enums.QueueType;
import cn.mcarl.miars.storage.storage.data.practice.*;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import javax.xml.crypto.Data;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: carl0
 * @DATE: 2023/1/5 20:08
 */
public class CommunityGUIItem {

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yy HH:mm:ss");

    /**
     * 游戏模式 (图标)
     */
    public static ItemStack getPracticeTypeItem(Player player,FKitType fKitType, QueueType queueType) {

        String[] lore = new String[0];

        List<QueueInfo> queueInfos = new ArrayList<>();
        List<ArenaState> arenaStates = new ArrayList<>();
        List<DailyStreak> dailyStreaks = new ArrayList<>();
        DailyStreak dailyStreak = null;

        if (queueType != null){
            lore = new String[]{
                    "",
                    "&7在战斗中: &c{in_fights}",
                    "&7在匹配中: &c{in_queue}",
                    "",
                    "&7每日连胜: &c{daily_streak}",
                    "&c1. &7{daily_streak_1_name} &7(&c{daily_streak_1_value}&7)",
                    "&c2. &7{daily_streak_2_name} &7(&c{daily_streak_2_value}&7)",
                    "&c3. &7{daily_streak_3_name} &7(&c{daily_streak_3_value}&7)",
                    "",
                    "&c点击加入匹配队列"
            };
            queueInfos = PracticeQueueDataStorage.getInstance().getQueueInfos(fKitType,queueType);
            arenaStates = PracticeArenaStateDataStorage.getInstance().getArenaStateByQueueAndFig(fKitType,queueType);
            dailyStreaks = PracticeDailyStreakDataStorage.getInstance().getDailyStreaksTop(queueType,fKitType);
            dailyStreak = PracticeDailyStreakDataStorage.getInstance().getDailyStreaksByPlayer(player,queueType,fKitType);

        }


        switch (fKitType){
            case NO_DEBUFF -> {
                return new ItemBuilder(Material.POTION)
                        .setData((short) 16421)
                        .setName("&a&lNo DeBuff")
                        .setLore(getLoreInfo(
                                String.valueOf(dailyStreak!=null ? dailyStreak.getStreak() : 0),
                                String.valueOf(queueInfos.size()!=0 ? queueInfos.get(0).getPlayers().size() : 0),
                                String.valueOf(arenaStates.size()),
                                dailyStreaks,
                                lore
                        ))
                        .addFlag(ItemFlag.HIDE_POTION_EFFECTS)
                        .addFlag(ItemFlag.HIDE_ATTRIBUTES)
                        .addFlag(ItemFlag.HIDE_DESTROYS)
                        .toItemStack();
            }
            case FFAGAME -> {
                return new ItemBuilder(Material.FISHING_ROD)
                        .setName("&a&lFFAGAME")
                        .setLore(getLoreInfo(
                                String.valueOf(dailyStreak!=null ? dailyStreak.getStreak() : 0),
                                String.valueOf(queueInfos.size()!=0 ? queueInfos.get(0).getPlayers().size() : 0),
                                String.valueOf(arenaStates.size()),
                                dailyStreaks,
                                lore
                        ))
                        .toItemStack();
            }
        }

        return new ItemBuilder(Material.BARRIER)
                .setName("&c&lERROR")
                .toItemStack();
    }

    private static String[] getLoreInfo(
            String streak,
            String queue,
            String fights,
            List<DailyStreak> top,
            String...strings
    ){

        List<String> lore = new ArrayList<>();
        for (String s:strings){

            s=s
                    .replace("{in_fights}",fights) // 正在战斗
                    .replace("{in_queue}",queue) // 正在匹配
                    .replace("{daily_streak}", streak) // 连胜
                    .replace("{daily_streak_1_name}",top.size()>=1?top.get(0).getName():"暂无")
                    .replace("{daily_streak_1_value}",String.valueOf(top.size()>=1?top.get(0).getStreak():"暂无"))
                    .replace("{daily_streak_2_name}",top.size()>=2?top.get(1).getName():"暂无")
                    .replace("{daily_streak_2_value}",String.valueOf(top.size()>=2?top.get(1).getStreak():"暂无"))
                    .replace("{daily_streak_3_name}",top.size()>=3?top.get(2).getName():"暂无")
                    .replace("{daily_streak_3_value}",String.valueOf(top.size()>=3?top.get(2).getStreak():"暂无"))
            ;
            
            lore.add(s);
        }

        return lore.toArray(new String[0]);
    }

}
