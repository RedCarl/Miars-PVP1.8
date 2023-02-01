package cn.mcarl.miars.practice.listener;

import cn.mcarl.miars.practice.manager.ArenaManager;
import cn.mcarl.miars.storage.entity.practice.Arena;
import cn.mcarl.miars.storage.entity.practice.ArenaState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerListener implements Listener {

    @EventHandler
    public void PlayerJoinEvent(PlayerJoinEvent e){
        Player player = e.getPlayer();

        // 判断是否是管理员，如果不是判断是否有房间
        if (!player.hasPermission("group.admin")){
            ArenaState state = ArenaManager.getInstance().getArenaStateByPlayer(player);
            if (state==null){
                player.kickPlayer("&c意外的错误");
            }else {
                Arena arena = ArenaManager.getInstance().getArenaById(state.getId());
                if (state.getA() == player){
                    player.teleport(arena.getLoc1());
                }else {
                    player.teleport(arena.getLoc2());
                }
            }
        }
    }


    @EventHandler
    public void PlayerMoveEvent(PlayerMoveEvent e){

    }
}
