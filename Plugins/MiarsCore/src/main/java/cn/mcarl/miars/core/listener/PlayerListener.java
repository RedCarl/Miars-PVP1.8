package cn.mcarl.miars.core.listener;

import cn.mcarl.miars.core.utils.MiarsUtil;
import cn.mcarl.miars.storage.storage.data.MPlayerDataStorage;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
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
        MiarsUtil.initPlayerNametag(player);

        // 禁止玩家进入消息
        e.setJoinMessage(null);
    }

    @EventHandler
    public void PlayerQuitEvent(PlayerQuitEvent e){
        Player player = e.getPlayer();

        MPlayerDataStorage.getInstance().clearUserCacheData(player);

        // 禁止玩家进入消息
        e.setQuitMessage(null);
    }


//    @EventHandler
//    public void ServerCommandEvent(ServerCommandEvent e){
//        System.out.println(e.getCommand());
//        if ("pl".equalsIgnoreCase(e.getCommand()) || "plugins".equalsIgnoreCase(e.getCommand())){
//            MiarsUtil.getPlugins(e.getSender());
//            e.setCancelled(true);
//        }
//        if ("ver".equalsIgnoreCase(e.getCommand()) || "version".equalsIgnoreCase(e.getCommand())){
//            MiarsUtil.getVersion(e.getSender());
//            e.setCancelled(true);
//        }
//        if ("bukkit:pl".equalsIgnoreCase(e.getCommand()) || "bukkit:plugins".equalsIgnoreCase(e.getCommand())){
//            MiarsUtil.getPlugins(e.getSender());
//            e.setCancelled(true);
//        }
//        if ("bukkit:ver".equalsIgnoreCase(e.getCommand()) || "bukkit:version".equalsIgnoreCase(e.getCommand())){
//            MiarsUtil.getVersion(e.getSender());
//            e.setCancelled(true);
//        }
//    }
//
//    @EventHandler
//    public void PlayerCommandSendEvent(PlayerCommandSendEvent e){
//        System.out.println(e.getCommands());
//        for (String s:e.getCommands()){
//            if (s.toLowerCase().contains("pl") || s.toLowerCase().contains("plugins")){
//                MiarsUtil.getPlugins(e.getPlayer());
//            }
//            if (s.toLowerCase().contains("ver") || s.toLowerCase().contains("version")){
//                MiarsUtil.getVersion(e.getPlayer());
//            }
//        }
//    }
}
