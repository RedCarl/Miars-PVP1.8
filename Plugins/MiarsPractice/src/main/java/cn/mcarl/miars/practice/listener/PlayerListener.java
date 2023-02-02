package cn.mcarl.miars.practice.listener;

import cn.mcarl.miars.core.utils.ItemBuilder;
import cn.mcarl.miars.core.utils.ToolUtils;
import cn.mcarl.miars.practice.conf.PluginConfig;
import cn.mcarl.miars.practice.manager.ArenaManager;
import cn.mcarl.miars.practice.manager.ScoreBoardManager;
import cn.mcarl.miars.storage.entity.practice.Arena;
import cn.mcarl.miars.storage.entity.practice.ArenaState;
import cn.mcarl.miars.storage.enums.FKitType;
import cn.mcarl.miars.storage.utils.BukkitUtils;
import com.google.gson.Gson;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerListener implements Listener {

    private final Gson gson = new Gson();
    @EventHandler
    public void PlayerJoinEvent(PlayerJoinEvent e){
        Player player = e.getPlayer();
        ToolUtils.playerInitialize(player);

        ArenaState state = ArenaManager.getInstance().getArenaStateByPlayer(player);
        if (state==null){
            player.kickPlayer("&c意外的错误");
        }else {
            Arena arena = ArenaManager.getInstance().getArenaById(state.getId());
            if (state.getA().equals(player.getName())){
                player.teleport(arena.getLoc1());
            }else {
                player.teleport(arena.getLoc2());
            }

            // 初始化玩家记分板
            ScoreBoardManager.getInstance().joinPlayer(player);

        }
    }


    @EventHandler
    public void PlayerMoveEvent(PlayerMoveEvent e){
        Player player = e.getPlayer();
        ArenaState state = ArenaManager.getInstance().getArenaStateByPlayer(player);

        // 判断玩家是否跌落出竞技场
        if (!player.hasPermission("miars.admin") && player.getLocation().getY()<0){
            Arena arena = ArenaManager.getInstance().getArenaById(state.getId());
            if (state.getA().equals(player.getName())){
                player.teleport(arena.getLoc1());
            }else {
                player.teleport(arena.getLoc2());
            }
        }
    }

    @EventHandler
    public void PlayerDeathEvent(PlayerDeathEvent e){
        if (e.getEntity().getKiller() != null) {
            Player deathPlayer = e.getEntity(); // 死亡的玩家
            Player attackPlayer = e.getEntity().getKiller(); // 击杀的玩家
            Location location = deathPlayer.getLocation();
            deathPlayer.spigot().respawn();
            deathPlayer.teleport(location);

            ArenaState state = ArenaManager.getInstance().getArenaStateByPlayer(attackPlayer);
            state.setEndTime(System.currentTimeMillis());
            if (deathPlayer.getName().equals(state.getA())){
                state.setAFInventory(BukkitUtils.ItemStackConvertByte(ToolUtils.playerToFInv(deathPlayer, FKitType.valueOf(PluginConfig.PRACTICE_SITE.MODE.get()))));
                state.setBFInventory(BukkitUtils.ItemStackConvertByte(ToolUtils.playerToFInv(attackPlayer, FKitType.valueOf(PluginConfig.PRACTICE_SITE.MODE.get()))));
            }else {
                state.setAFInventory(BukkitUtils.ItemStackConvertByte(ToolUtils.playerToFInv(attackPlayer, FKitType.valueOf(PluginConfig.PRACTICE_SITE.MODE.get()))));
                state.setBFInventory(BukkitUtils.ItemStackConvertByte(ToolUtils.playerToFInv(deathPlayer, FKitType.valueOf(PluginConfig.PRACTICE_SITE.MODE.get()))));
            }
            state.setWin(attackPlayer.getName());
        }


        e.setDeathMessage(null);
        e.setKeepInventory(true);
    }

    @EventHandler
    public void PlayerRespawnEvent(PlayerRespawnEvent e){
        Player player = e.getPlayer();
        ToolUtils.playerInitialize(player);
    }
}
