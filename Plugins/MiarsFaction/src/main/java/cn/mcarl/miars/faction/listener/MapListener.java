package cn.mcarl.miars.faction.listener;

import cn.mcarl.miars.faction.utils.Map;
import com.boydti.fawe.bukkit.chat.FancyMessage;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class MapListener implements Listener {

    @EventHandler
    public void PlayerInteractEvent(PlayerInteractEvent e) {
        Player player = e.getPlayer();

        //地图系统
        if (player.getItemInHand().getType().equals(Material.EMPTY_MAP)) {
            for (FancyMessage fancyMessage : new Map().getMap(player, player.getLocation().getYaw())) {
                fancyMessage.send(player);
            }
        }
    }
}
