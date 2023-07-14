package cn.mcarl.miars.faction.listener;

import cc.carm.lib.easyplugin.utils.ColorParser;
import cn.mcarl.miars.core.utils.Cube;
import cn.mcarl.miars.faction.conf.PluginConfig;
import cn.mcarl.miars.storage.entity.MPlayer;
import cn.mcarl.miars.storage.entity.MRank;
import cn.mcarl.miars.storage.storage.data.MPlayerDataStorage;
import cn.mcarl.miars.storage.storage.data.MRankDataStorage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerListener implements Listener {

    @EventHandler
    public void PlayerJoinEvent(PlayerJoinEvent e){
        Player player = e.getPlayer();

        player.teleport(PluginConfig.PROTECTED_REGION.SPAWN.get());

    }

    Map<UUID, Long> theEndBackTime = new ConcurrentHashMap<>();
    Cube theEndBack = new Cube(
            new Location(Bukkit.getWorld(PluginConfig.PROTECTED_REGION.WORLD_NAME.get()+"_the_end"), 0, 0, 2),
            new Location(Bukkit.getWorld(PluginConfig.PROTECTED_REGION.WORLD_NAME.get()+"_the_end"), 2, 256, 0)
    );
    @EventHandler
    public void PlayerMoveEvent(PlayerMoveEvent e){
        Player player = e.getPlayer();
        //回城地毯
        if (player.getWorld().getName().equals(PluginConfig.PROTECTED_REGION.WORLD_NAME.get()+"_the_end")) {
            if (!theEndBackTime.containsKey(player.getUniqueId())) {
                if (theEndBack.isPlayerInCube(player)) {
                    theEndBackTime.put(player.getUniqueId(), System.currentTimeMillis());
                }
            }
        }
    }

    @EventHandler
    public void AsyncPlayerChatEvent(AsyncPlayerChatEvent e){
        MPlayer mPlayer = MPlayerDataStorage.getInstance().getMPlayer(e.getPlayer());
        MRank mRank = MRankDataStorage.getInstance().getMRank(mPlayer.getRank());
        e.setFormat(ColorParser.parse(mRank.getPrefix()+mRank.getNameColor()+"%1$s&f: %2$s"));
    }
}
