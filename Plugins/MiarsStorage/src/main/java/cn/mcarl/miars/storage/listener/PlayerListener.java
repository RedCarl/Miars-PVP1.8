package cn.mcarl.miars.storage.listener;

import cn.mcarl.miars.storage.conf.PluginConfig;
import cn.mcarl.miars.storage.storage.data.VaultDataStorage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e){
        Player player = e.getPlayer();
        VaultDataStorage.getInstance().clearCacheData(player.getUniqueId(), PluginConfig.VAULT.KEY.get());
    }
}
