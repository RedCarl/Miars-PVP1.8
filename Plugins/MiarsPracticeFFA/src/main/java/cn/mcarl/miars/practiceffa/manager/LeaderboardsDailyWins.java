package cn.mcarl.miars.practiceffa.manager;

import cc.carm.lib.easyplugin.utils.ColorParser;
import cn.mcarl.miars.practiceffa.MiarsPracticeFFA;
import cn.mcarl.miars.practiceffa.conf.PluginConfig;
import cn.mcarl.miars.storage.entity.MPlayer;
import cn.mcarl.miars.storage.entity.MRank;
import cn.mcarl.miars.storage.entity.practice.DailyStreak;
import cn.mcarl.miars.storage.entity.practice.enums.practice.FKitType;
import cn.mcarl.miars.storage.entity.practice.enums.practice.QueueType;
import cn.mcarl.miars.storage.storage.data.MPlayerDataStorage;
import cn.mcarl.miars.storage.storage.data.MRankDataStorage;
import cn.mcarl.miars.storage.storage.data.practice.PracticeDailyStreakDataStorage;
import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import eu.decentsoftware.holograms.event.HologramClickEvent;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class LeaderboardsDailyWins implements Listener {

    private Hologram hologram;
    private Location location = PluginConfig.LEADERBOARDS_HOLOGRAM.DAILY_WINSTREAK.getNotNull();

    public LeaderboardsDailyWins(){
        this.hologram = DHAPI.createHologram("LeaderboardsDailyWins",location,getTexts(FKitType.NO_DEBUFF));

        for (FKitType type:FKitType.values()) {
            if (type!=FKitType.NO_DEBUFF){
                DHAPI.addHologramPage(hologram, getTexts(type));
            }
        }

        run();
    }

    @EventHandler
    public void HologramLineClickEvent(HologramClickEvent e){
        Player player = e.getPlayer();
        if (e.getHologram().getName().equals(hologram.getName())){

            int nextPage = hologram.getPlayerPage(player) + 1;
            if (nextPage < 0 || hologram.size() <= nextPage) {
                hologram.show(player,0);
            }else {
                hologram.show(player, nextPage);
            }

            player.playSound(player.getLocation(), Sound.CLICK, 10.0F, 2.0F);
        }
    }

    public List<String> getTexts(FKitType fKitType) {
        List<String> texts = new ArrayList<>();
        texts.add("&bDaily Winstreak");
        texts.add("");

        // Type Name
        texts.add("&a● "+fKitType.getName()+" ●");
        texts.add("");

        // List
        List<DailyStreak> dailyList = PracticeDailyStreakDataStorage.getInstance().getDailyStreaksTop(QueueType.UNRANKED,fKitType);

        for (int i = 0; i < 10; i++) {
            int index = i+1;
            DailyStreak daily;
            String name = "*";
            String wins = "*";

            // 防止数组越界
            if (dailyList.size()>(i+1)){
                daily = dailyList.get(i);
                MPlayer mPlayer = MPlayerDataStorage.getInstance().getMPlayer(daily.getUuid());
                MRank mRank = MRankDataStorage.getInstance().getMRank(mPlayer.getRank());

                name = mRank.getNameColor() + daily.getName();
                wins = String.valueOf(daily.getStreak());
            }

            texts.add("&f" + index + ". &7" + name +" &f- &b" + wins + " wins");
        }

        texts.add("");
        texts.add("&bClick to view next kit");
        return ColorParser.parse(texts);
    }

    public void run(){
        new BukkitRunnable() {
            @Override
            public void run() {

            }
        }.runTaskTimer(MiarsPracticeFFA.getInstance(),0,60);
    }

}
