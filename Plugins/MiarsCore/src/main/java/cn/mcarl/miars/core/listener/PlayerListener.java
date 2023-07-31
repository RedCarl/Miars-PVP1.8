package cn.mcarl.miars.core.listener;

import cc.carm.lib.easyplugin.utils.ColorParser;
import cn.mcarl.miars.core.MiarsCore;
import cn.mcarl.miars.storage.storage.data.MPlayerDataStorage;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * @Author: carl0
 * @DATE: 2022/11/30 20:48
 */
public class PlayerListener implements Listener {

    @EventHandler
    public void PlayerJoinEvent(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        // 初始化玩家数据
        MPlayerDataStorage.getInstance().checkMPlayer(player);

        player.setGameMode(GameMode.SURVIVAL);

        MiarsCore.getInstance().getTabHeaderAndFooter().show(player);

        // 禁止玩家进入消息
        e.setJoinMessage(null);
    }

    @EventHandler
    public void PlayerQuitEvent(PlayerQuitEvent e){
        Player player = e.getPlayer();

        // DataCache
        MPlayerDataStorage.getInstance().clearUserCacheData(player);

        // Tab
        MiarsCore.getInstance().getTabHeaderAndFooter().hide(player);

        // NameTag
        MiarsCore.getNametagAPI().clearNametag(player);

        // 禁止玩家进入消息
        e.setQuitMessage(null);
    }

    @EventHandler
    public void PlayerGameModeChangeEvent(PlayerGameModeChangeEvent e){
        Player player = e.getPlayer();
        player.sendMessage(ColorParser.parse("&7You changed the mode to &b"+e.getNewGameMode().name()+" &7 by &b"+e.getPlayer().getName()+" &7("+(player.isOnline()?"&aOnline":"&cOffline")+"&7)"));
    }
}
