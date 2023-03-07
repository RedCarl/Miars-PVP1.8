package cn.mcarl.miars.core.listener;

import cn.mcarl.miars.core.conf.PluginConfig;
import cn.mcarl.miars.core.utils.MiarsUtil;
import cn.mcarl.miars.storage.storage.data.MPlayerDataStorage;
import net.citizensnpcs.Citizens;
import net.citizensnpcs.api.CitizensAPI;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
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

        if (PluginConfig.SITE.NAME_TAG.get()){
            MiarsUtil.initPlayerNametag(player);
        }

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


    @EventHandler(priority = EventPriority.LOWEST)
    public void EntityDamageEvent(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player damager){
            if (e.getEntity() instanceof Player player) {
                if (e.getDamage() == 0.0D || e.isCancelled() || e.getEntity().hasMetadata("NPC")) {
                    return;
                }
                Location location = player.getLocation();
                location.getWorld().playEffect(location, Effect.STEP_SOUND, 152);
            }
        }
    }
}
