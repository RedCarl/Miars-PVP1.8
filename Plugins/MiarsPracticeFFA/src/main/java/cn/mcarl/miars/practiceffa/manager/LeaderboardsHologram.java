package cn.mcarl.miars.practiceffa.manager;

import cc.carm.lib.easyplugin.utils.ColorParser;
import cn.mcarl.miars.practiceffa.conf.PluginConfig;
import cn.mcarl.miars.storage.entity.MPlayer;
import cn.mcarl.miars.storage.entity.MRank;
import cn.mcarl.miars.storage.entity.practice.DailyStreak;
import cn.mcarl.miars.storage.entity.practice.enums.practice.FKitType;
import cn.mcarl.miars.storage.entity.practice.enums.practice.QueueType;
import cn.mcarl.miars.storage.storage.data.MPlayerDataStorage;
import cn.mcarl.miars.storage.storage.data.MRankDataStorage;
import cn.mcarl.miars.storage.storage.data.practice.PracticeDailyStreakDataStorage;
import gg.noob.lib.hologram.BaseHologram;
import gg.noob.lib.hologram.Hologram;
import gg.noob.lib.hologram.HologramLine;
import gg.noob.lib.hologram.click.ClickType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.*;

public class LeaderboardsHologram implements BaseHologram {
  private Hologram hologram;
  private Map<UUID, FKitType> kitType = new HashMap<>();
  private Map<UUID, Long> clickTime = new HashMap<>();

  @Override
  public List<String> getTexts(Player player) {
    List<String> texts = new ArrayList<>();
    texts.add("&bDaily Winstreak");
    texts.add("");
    FKitType fKitType = kitType.get(player.getUniqueId());

    // Type Name
    texts.add("&a● "+(fKitType==null?"Global":fKitType.getName())+" ●");
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

  @Override
  public Location getLocation() {
    return PluginConfig.LEADERBOARDS_HOLOGRAM.DAILY_WINSTREAK.getNotNull();
  }

  @Override
  public int getUpdatePerSeconds() {
    return 60;
  }

  @Override
  public List<Player> getViewers() {
    return new ArrayList<>(Bukkit.getOnlinePlayers());
  }

  @Override
  public void onClick(Player player, HologramLine hologramLine, ClickType clickType) {

    // 防止一直点击
    if (clickTime.containsKey(player.getUniqueId())){
      if ((System.currentTimeMillis() - clickTime.get(player.getUniqueId()))/1000 <= 3){
        player.sendMessage(ColorParser.parse("&7Please do not click frequently."));
        return;
      }
    }
    clickTime.put(player.getUniqueId(),System.currentTimeMillis());


    // 判断是否为空
    if (kitType.get(player.getUniqueId())==null){
      kitType.put(player.getUniqueId(),FKitType.NO_DEBUFF);
    }else {
      List<FKitType> types = List.of(FKitType.values());

      for (int i = 0; i < types.size(); i++) {
        if (types.get(i)==kitType.get(player.getUniqueId())){
          // 判断是否是最后一个，如果是最后一个就设置为null
          if (i+1 == types.size()){
            kitType.remove(player.getUniqueId());
          }else {
            kitType.put(player.getUniqueId(),types.get(i+1));
          }

          break;
        }
      }
    }

    player.playSound(player.getLocation(), Sound.CLICK, 10.0F, 2.0F);
    this.hologram.update(player);
  }

  @Override
  public void setHologram(Hologram hologram) {
    this.hologram = hologram;
  }
}
