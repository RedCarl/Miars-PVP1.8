package cn.mcarl.miars.practiceffa.ui;

import cn.mcarl.miars.storage.entity.MPlayer;
import cn.mcarl.miars.storage.entity.MRank;
import cn.mcarl.miars.storage.storage.data.MPlayerDataStorage;
import cn.mcarl.miars.storage.storage.data.MRankDataStorage;
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

        if (queueType!=null){
            switch (queueType){
                case RANKED -> {
                    lore = new String[]{
                            "",
                            "&fIn Fight: &b{in_fights}",
                            "&fQueued: &b{in_queue}",
                            "&fYour Elo: &b{elo}",
                            "",
                            "&f1. &7{elo_1_name} &f- &b{elo_1_value} elo",
                            "&f2. &7{elo_2_name} &f- &b{elo_2_value} elo",
                            "&f3. &7{elo_3_name} &f- &b{elo_3_value} elo",
                            "",
                            "&7Click here to queue "+queueType.getColor()+"{queue_mode}&7."
                    };
                    queueInfos = PracticeQueueDataStorage.getInstance().getQueueInfos(fKitType,queueType);
                    dailyStreaks = PracticeDailyStreakDataStorage.getInstance().getDailyStreaksTop(queueType,fKitType);
                    dailyStreak = PracticeDailyStreakDataStorage.getInstance().getDailyStreaksByPlayer(player,queueType,fKitType,dailyStreaks);
                    fights = PracticeArenaStateDataStorage.getInstance().getArenaStateByQueueAndFig(fKitType,queueType);
                }
                case UNRANKED -> {
                    lore = new String[]{
                            "",
                            "&fIn Fight: &b{in_fights}",
                            "&fQueued: &b{in_queue}",
                            "&fYour Winstreak: &b{daily_streak}",
                            "",
                            "&f1. &7{daily_streak_1_name} &f- &b{daily_streak_1_value} wins",
                            "&f2. &7{daily_streak_2_name} &f- &b{daily_streak_2_value} wins",
                            "&f3. &7{daily_streak_3_name} &f- &b{daily_streak_3_value} wins",
                            "",
                            "&7Click here to queue "+queueType.getColor()+"{queue_mode}&7."
                    };
                    queueInfos = PracticeQueueDataStorage.getInstance().getQueueInfos(fKitType,queueType);
                    dailyStreaks = PracticeDailyStreakDataStorage.getInstance().getDailyStreaksTop(queueType,fKitType);
                    dailyStreak = PracticeDailyStreakDataStorage.getInstance().getDailyStreaksByPlayer(player,queueType,fKitType,dailyStreaks);
                    fights = PracticeArenaStateDataStorage.getInstance().getArenaStateByQueueAndFig(fKitType,queueType);
                }
            }
        }

        switch (fKitType){
            case NO_DEBUFF -> {
                String name = "No DeBuff";
                return new ItemBuilder(Material.POTION)
                        .setData((short) 16421)
                        .setName((queueType==null?"&a":queueType.getColor())+name)
                        .setLore(getLoreInfo(
                                String.valueOf(dailyStreak!=null ? dailyStreak.getStreak() : 0),
                                String.valueOf(queueInfos.size()!=0 ? queueInfos.get(0).getPlayers().size() : 0),
                                String.valueOf(fights),
                                name,
                                dailyStreaks,
                                lore
                        ))
                        .addFlag(ItemFlag.HIDE_POTION_EFFECTS)
                        .addFlag(ItemFlag.HIDE_ATTRIBUTES)
                        .addFlag(ItemFlag.HIDE_DESTROYS)
                        .toItemStack();
            }
            case BUILD_UHC -> {
                String name = "Build UHC";
                return new ItemBuilder(Material.LAVA_BUCKET)
                        .setName((queueType==null?"&a":queueType.getColor())+name)
                        .setLore(getLoreInfo(
                                String.valueOf(dailyStreak!=null ? dailyStreak.getStreak() : 0),
                                String.valueOf(queueInfos.size()!=0 ? queueInfos.get(0).getPlayers().size() : 0),
                                String.valueOf(fights),
                                name,
                                dailyStreaks,
                                lore
                        ))
                        .toItemStack();
            }
            case BOXING -> {
                String name = "Boxing";
                return new ItemBuilder(Material.DIAMOND_SWORD)
                        .setName((queueType==null?"&a":queueType.getColor())+name)
                        .setLore(getLoreInfo(
                                String.valueOf(dailyStreak!=null ? dailyStreak.getStreak() : 0),
                                String.valueOf(queueInfos.size()!=0 ? queueInfos.get(0).getPlayers().size() : 0),
                                String.valueOf(fights),
                                name,
                                dailyStreaks,
                                lore
                        ))
                        .toItemStack();
            }
            case SUMO -> {
                String name = "Sumo";
                return new ItemBuilder(Material.LEASH)
                        .setName((queueType==null?"&a":queueType.getColor())+name)
                        .setLore(getLoreInfo(
                                String.valueOf(dailyStreak!=null ? dailyStreak.getStreak() : 0),
                                String.valueOf(queueInfos.size()!=0 ? queueInfos.get(0).getPlayers().size() : 0),
                                String.valueOf(fights),
                                name,
                                dailyStreaks,
                                lore
                        ))
                        .toItemStack();
            }
            case BOW -> {
                String name = "Bow";
                return new ItemBuilder(Material.BOW)
                        .setName((queueType==null?"&a":queueType.getColor())+name)
                        .setLore(getLoreInfo(
                                String.valueOf(dailyStreak!=null ? dailyStreak.getStreak() : 0),
                                String.valueOf(queueInfos.size()!=0 ? queueInfos.get(0).getPlayers().size() : 0),
                                String.valueOf(fights),
                                name,
                                dailyStreaks,
                                lore
                        ))
                        .toItemStack();
            }
            case COMBO -> {
                String name = "Combo";
                return new ItemBuilder(Material.RAW_FISH)
                        .setName((queueType==null?"&a":queueType.getColor())+name)
                        .setData((short) 3)
                        .setLore(getLoreInfo(
                                String.valueOf(dailyStreak!=null ? dailyStreak.getStreak() : 0),
                                String.valueOf(queueInfos.size()!=0 ? queueInfos.get(0).getPlayers().size() : 0),
                                String.valueOf(fights),
                                name,
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
            String queueMode,
            List<DailyStreak> top,
            String...strings
    ){

        List<String> lore = new ArrayList<>();

        for (String s:strings){
            MRank mRank1 = new MRank();
            MRank mRank2 = new MRank();
            MRank mRank3 = new MRank();
            if (top.size()>=1){
                MPlayer mPlayer1 = MPlayerDataStorage.getInstance().getMPlayer(top.get(0).getUuid());
                mRank1 = MRankDataStorage.getInstance().getMRank(mPlayer1.getRank());
            }

            if (top.size()>=2){
                MPlayer mPlayer2 = MPlayerDataStorage.getInstance().getMPlayer(top.get(1).getUuid());
                mRank2 = MRankDataStorage.getInstance().getMRank(mPlayer2.getRank());
            }

            if (top.size()>=3){
                MPlayer mPlayer3 = MPlayerDataStorage.getInstance().getMPlayer(top.get(2).getUuid());
                mRank3 = MRankDataStorage.getInstance().getMRank(mPlayer3.getRank());
            }

            s=s
                    .replace("{in_fights}",fights) // 正在战斗
                    .replace("{in_queue}",queue) // 正在匹配
                    .replace("{daily_streak}", streak) // 连胜
                    .replace("{queue_mode}", queueMode) // 匹配模式

                    .replace("{daily_streak_1_name}",top.size()>=1?(mRank1.getNameColor()+top.get(0).getName()):"*")
                    .replace("{daily_streak_1_value}",String.valueOf(top.size()>=1?top.get(0).getStreak():"*"))
                    .replace("{daily_streak_2_name}",top.size()>=2?(mRank2.getNameColor()+top.get(1).getName()):"*")
                    .replace("{daily_streak_2_value}",String.valueOf(top.size()>=2?top.get(1).getStreak():"*"))
                    .replace("{daily_streak_3_name}",top.size()>=3?(mRank3.getNameColor()+top.get(2).getName()):"*")
                    .replace("{daily_streak_3_value}",String.valueOf(top.size()>=3?top.get(2).getStreak():"*"))
            ;
            
            lore.add(s);
        }

        return lore.toArray(new String[0]);
    }

}
