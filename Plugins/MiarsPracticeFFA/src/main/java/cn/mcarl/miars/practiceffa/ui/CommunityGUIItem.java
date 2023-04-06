package cn.mcarl.miars.practiceffa.ui;

import cn.mcarl.miars.storage.utils.ItemBuilder;
import cn.mcarl.miars.storage.entity.practice.DailyStreak;
import cn.mcarl.miars.storage.entity.practice.QueueInfo;
import cn.mcarl.miars.storage.entity.practice.enums.practice.FKitType;
import cn.mcarl.miars.storage.entity.practice.enums.practice.QueueType;
import cn.mcarl.miars.storage.storage.data.practice.*;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

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
        List<DailyStreak> dailyStreaks = new ArrayList<>();
        DailyStreak dailyStreak = null;
        int fights = 0;

        if (queueType != null){
            lore = new String[]{
                    "",
                    "&7In Fight: &6{in_fights}",
                    "&7In Queue: &e{in_queue}",
                    "",
                    "&7Daily Streak: &f{daily_streak}",
                    "&61. &7{daily_streak_1_name} &7(&c{daily_streak_1_value}&7)",
                    "&62. &7{daily_streak_2_name} &7(&c{daily_streak_2_value}&7)",
                    "&63. &7{daily_streak_3_name} &7(&c{daily_streak_3_value}&7)",
                    "",
                    "&eClick join queue."
            };
            queueInfos = PracticeQueueDataStorage.getInstance().getQueueInfos(fKitType,queueType);
            dailyStreaks = PracticeDailyStreakDataStorage.getInstance().getDailyStreaksTop(queueType,fKitType);
            dailyStreak = PracticeDailyStreakDataStorage.getInstance().getDailyStreaksByPlayer(player,queueType,fKitType);
            fights = PracticeArenaStateDataStorage.getInstance().getArenaStateByQueueAndFig(fKitType,queueType);
        }


        switch (fKitType){
            case NO_DEBUFF -> {
                return new ItemBuilder(Material.POTION)
                        .setData((short) 16421)
                        .setName("&a&lNo DeBuff")
                        .setLore(getLoreInfo(
                                String.valueOf(dailyStreak!=null ? dailyStreak.getStreak() : 0),
                                String.valueOf(queueInfos.size()!=0 ? queueInfos.get(0).getPlayers().size() : 0),
                                String.valueOf(fights),
                                dailyStreaks,
                                lore
                        ))
                        .addFlag(ItemFlag.HIDE_POTION_EFFECTS)
                        .addFlag(ItemFlag.HIDE_ATTRIBUTES)
                        .addFlag(ItemFlag.HIDE_DESTROYS)
                        .toItemStack();
            }
            case BUILD_UHC -> {
                return new ItemBuilder(Material.LAVA_BUCKET)
                        .setName("&a&lBuild UHC")
                        .setLore(getLoreInfo(
                                String.valueOf(dailyStreak!=null ? dailyStreak.getStreak() : 0),
                                String.valueOf(queueInfos.size()!=0 ? queueInfos.get(0).getPlayers().size() : 0),
                                String.valueOf(fights),
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
