package io.github.Leonardo0013YT.UltraSkyWars.listeners;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.api.events.RedisMessageEvent;
import io.github.Leonardo0013YT.UltraSkyWars.api.events.specials.RedisPartyMessageEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class GeneralListener implements Listener {

    private UltraSkyWars plugin;

    public GeneralListener(UltraSkyWars plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        if (plugin.getCm().isBungeeModeEnabled() && plugin.getCm().isBungeeModeGame()) {
            e.setQuitMessage(null);
        }
        remove(p);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onKick(PlayerKickEvent e) {
        Player p = e.getPlayer();
        remove(p);
    }

    @EventHandler
    public void onMessage(RedisMessageEvent e) {
        String channel = e.getChannel();
        String message = e.getMessage();
        if (!channel.startsWith("usw:")) return;
        if (channel.equals("usw:parties")) {
            Bukkit.getServer().getPluginManager().callEvent(new RedisPartyMessageEvent(message));
        }
        if (channel.equals("usw:party")) {
            String[] msg = message.split(":");
            String action = msg[0];
            if (action.equalsIgnoreCase("SEND")) {
                UUID uuid = UUID.fromString(msg[1]);
                String server = msg[2];
                Player on = Bukkit.getPlayer(uuid);
                if (on != null) {
                    plugin.sendToServer(on, server);
                    on.sendMessage(plugin.getLang().get(on, "parties.joinGame"));
                }
            }
        }
        if (channel.equals("usw:gameupdate")) {
            String[] msg = message.split(":");
            String server = msg[0], map = msg[1], type = msg[3], state = msg[2], color = msg[6];
            int players = Integer.parseInt(msg[4]), max = Integer.parseInt(msg[5]);
            plugin.getGm().updateGame(server, map, color, state, type, players, max);
            plugin.getGem().updateInventories(type, "none");
            plugin.sendDebugMessage("§aChannel", "GameUpdate", "§eMensaje Recibido", message);
        }
    }

    private void remove(Player p) {
        plugin.getSb().remove(p);
        plugin.getLvl().remove(p);
        plugin.getCos().remove(p);
        if (plugin.getIjm().isSoulWellInjection()) {
            plugin.getIjm().getSoulwell().getSwm().removeSession(p);
        }
        plugin.getDb().savePlayer(p);
    }

}