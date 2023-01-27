package cn.mcarl.miars.practiceffa.ui;

import cn.mcarl.miars.core.utils.ItemBuilder;
import cn.mcarl.miars.storage.enums.FKitType;
import cn.mcarl.miars.storage.enums.QueueType;
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

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    /**
     * 确认按钮
     */
    public static ItemStack getConfirmItem() {
        return new ItemBuilder(Material.STAINED_GLASS_PANE)
                .setName("&a确认")
                .setData((short) 5)
                .toItemStack();
    }

    /**
     * 取消按钮
     */
    public static ItemStack getCancelItem() {
        return new ItemBuilder(Material.STAINED_GLASS_PANE)
                .setName("&c取消")
                .setData((short) 14)
                .toItemStack();
    }

    /**
     * 分割线
     */
    public static ItemStack getLineItem() {
        return new ItemBuilder(Material.STAINED_GLASS_PANE)
                .setName("&r")
                .setData((short) 15)
                .toItemStack();
    }

    /**
     * 游戏模式 (图标)
     */
    public static ItemStack getPracticeTypeItem(Player player,FKitType fKitType, QueueType queueType) {

        String[] lore = new String[0];
        if (queueType == null){

        }else if (queueType.equals(QueueType.UNRANKED)){
            lore = new String[]{
                    "",
                    "&7在战斗中: &c{unranked_in_fights}",
                    "&7在匹配中: &c{unranked_in_queue}",
                    "",
                    "&7每日连胜: &c{unranked_daily_streak}",
                    "&c1. &7{unranked_daily_streak_1_name} &7(&c{unranked_daily_streak_1_value}&7)",
                    "&c2. &7{unranked_daily_streak_2_name} &7(&c{unranked_daily_streak_2_value}&7)",
                    "&c3. &7{unranked_daily_streak_3_name} &7(&c{unranked_daily_streak_3_value}&7)",
                    "",
                    "&c点击加入匹配队列"
            };
        }else if (queueType.equals(QueueType.RANKED)){

            lore = new String[]{
                    "",
                    "&7在战斗中: &c{ranked_in_fights}",
                    "&7在匹配中: &c{ranked_in_queue}",
                    "",
                    "&7每日连胜: &c{ranked_daily_streak}",
                    "&c1. &7{ranked_daily_streak_1_name} &7(&c{ranked_daily_streak_1_value}&7)",
                    "&c2. &7{ranked_daily_streak_2_name} &7(&c{ranked_daily_streak_2_value}&7)",
                    "&c3. &7{ranked_daily_streak_3_name} &7(&c{ranked_daily_streak_3_value}&7)",
                    "",
                    "&c点击加入匹配队列"
            };
        }

        switch (fKitType){
            case NO_DEBUFF -> {
                return new ItemBuilder(Material.POTION)
                        .setData((short) 16421)
                        .setName("&a&lNo DeBuff")
                        .setLore(getLoreInfo(player,fKitType,queueType,lore))
                        .addFlag(ItemFlag.HIDE_POTION_EFFECTS)
                        .addFlag(ItemFlag.HIDE_ATTRIBUTES)
                        .addFlag(ItemFlag.HIDE_DESTROYS)
                        .toItemStack();
            }
            case FFAGAME -> {
                return new ItemBuilder(Material.FISHING_ROD)
                        .setName("&a&lFFAGAME")
                        .setLore(getLoreInfo(player,fKitType,queueType,lore))
                        .toItemStack();
            }
        }

        return new ItemBuilder(Material.BARRIER)
                .setName("&c&lERROR")
                .toItemStack();
    }

    private static String[] getLoreInfo(Player player,FKitType fKitType, QueueType queueType, String...strings){

        List<String> lore = new ArrayList<>();
        for (String s:strings){

            if (queueType!=null && queueType.equals(QueueType.RANKED)){
                s=s
                        .replace("{ranked_in_fights}","0")
                        .replace("{ranked_in_queue}","0")
                        .replace("{ranked_daily_streak}","0")
                        .replace("{ranked_daily_streak_1_name}","null")
                        .replace("{ranked_daily_streak_1_value}","0")
                        .replace("{ranked_daily_streak_2_name}","null")
                        .replace("{ranked_daily_streak_2_value}","0")
                        .replace("{ranked_daily_streak_3_name}","null")
                        .replace("{ranked_daily_streak_3_value}","0")
                ;
            }else {
                s=s
                        .replace("{unranked_in_fights}","0")
                        .replace("{unranked_in_queue}","0")
                        .replace("{unranked_daily_streak}","0")
                        .replace("{unranked_daily_streak_1_name}","null")
                        .replace("{unranked_daily_streak_1_value}","0")
                        .replace("{unranked_daily_streak_2_name}","null")
                        .replace("{unranked_daily_streak_2_value}","0")
                        .replace("{unranked_daily_streak_3_name}","null")
                        .replace("{unranked_daily_streak_3_value}","0")
                ;
            }

            lore.add(s);
        }

        return lore.toArray(new String[0]);
    }

}
