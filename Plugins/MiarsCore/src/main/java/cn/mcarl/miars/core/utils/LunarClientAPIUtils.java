package cn.mcarl.miars.core.utils;

import cn.mcarl.miars.core.impl.lunarclient.LunarClientAPI;
import cn.mcarl.miars.core.impl.lunarclient.nethandler.client.LCPacketTitle;
import cn.mcarl.miars.core.impl.lunarclient.title.TitleType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

public class LunarClientAPIUtils {
  public static void sendTitle(Player player, String title) {
    sendTitle(player, title, 0L, 0L, 0L);
  }

  public static void sendTitle(Player player, String title, long displayTimeMs, long fadeInTimeMs, long fadeOutTimeMs) {
    LunarClientAPI lunarClientAPI = LunarClientAPI.getInstance();
    if (lunarClientAPI.isRunningLunarClient(player)) {
      lunarClientAPI.sendPacket(player, new LCPacketTitle(TitleType.TITLE.name(), title, displayTimeMs, fadeInTimeMs, fadeOutTimeMs));
    } else {
      player.showTitle(new TextComponent(title));
    }
  }

  public static void sendSubTitle(Player player, String title) {
    sendSubTitle(player, title, 0L, 0L, 0L);
  }

  public static void sendSubTitle(Player player, String title, long displayTimeMs, long fadeInTimeMs, long fadeOutTimeMs) {
    LunarClientAPI lunarClientAPI = LunarClientAPI.getInstance();
    if (lunarClientAPI.isRunningLunarClient(player)) {
      lunarClientAPI.sendPacket(player, new LCPacketTitle(TitleType.SUBTITLE.name(), title, displayTimeMs, fadeInTimeMs, fadeOutTimeMs));
    } else {
      player.setSubtitle(new TextComponent(title));
    }
  }

  public static void clearTitle(Player player) {
    LunarClientAPI lunarClientAPI = LunarClientAPI.getInstance();
    if (lunarClientAPI.isRunningLunarClient(player)) {
      lunarClientAPI.sendPacket(player, new LCPacketTitle(TitleType.TITLE.name(), "", 0L, 0L, 0L));
      lunarClientAPI.sendPacket(player, new LCPacketTitle(TitleType.SUBTITLE.name(), "", 0L, 0L, 0L));
    } else {
      player.resetTitle();
    }
  }
}