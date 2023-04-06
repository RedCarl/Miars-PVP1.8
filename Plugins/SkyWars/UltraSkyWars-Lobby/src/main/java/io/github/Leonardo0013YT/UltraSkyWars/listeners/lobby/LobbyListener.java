package io.github.Leonardo0013YT.UltraSkyWars.listeners.lobby;

import cn.mcarl.miars.core.publics.items.Ranks;
import cn.mcarl.miars.core.publics.items.ServerMenu;
import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.api.events.data.USWPlayerLoadEvent;
import io.github.Leonardo0013YT.UltraSkyWars.data.SWPlayer;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

public class LobbyListener implements Listener {

    private UltraSkyWars plugin;

    public LobbyListener(UltraSkyWars plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        e.setJoinMessage(null);
        plugin.getDb().loadPlayer(p);
    }

    @EventHandler
    public void loadPlayer(USWPlayerLoadEvent e) {
        Player p = e.getPlayer();
        if (p == null || !p.isOnline()) return;
        plugin.getLvl().checkUpgrade(p);
        plugin.getSb().update(p);
        givePlayerItems(p);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        if (!plugin.getCm().isMainLobby()) return;
        World w = p.getLocation().getWorld();
        if (w.getName().equals(plugin.getCm().getLobbyWorld())) {
            e.getRecipients().clear();
            e.getRecipients().addAll(w.getPlayers());
            e.setFormat(formatMainLobby(p, plugin.getIjm().isEloRankInjection(), e.getMessage()).replaceAll("%", "%%"));
        }
    }

    private String formatMainLobby(Player p, boolean rank, String msg) {
        SWPlayer sw = plugin.getDb().getSWPlayer(p);
        String format = plugin.getLang().get(p, "chat.mainLobby");
        if (rank) {
            format = format.replace("<rank>", plugin.getIjm().getEloRank().getErm().getEloRankChat(p));
        }
        return plugin.getAdm().parsePlaceholders(p, format.replaceAll("<levelprefix>", (sw.isShowLevel()) ? plugin.getLvl().getLevelPrefix(p) : "").replace("<msg>", msg).replaceAll("<player>", p.getName()));
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (e.getAction().equals(Action.PHYSICAL)) {
            return;
        }
        if (p.getItemInHand() == null || p.getItemInHand().getType().equals(Material.AIR)) {
            return;
        }
        ItemStack item = p.getItemInHand();
        if (item.equals(plugin.getIm().getLobby())) {
            e.setCancelled(true);
            p.chat(plugin.getCm().getItemLobbyCMD());
            changeSlot(p);
        }
    }

    public void givePlayerItems(Player p) {
        if (plugin.getCm().isItemLobbyEnabled()) {
            p.getInventory().setItem(plugin.getCm().getItemLobbySlot(), plugin.getIm().getLobby());
            new ServerMenu().give(p,0);
            new Ranks().give(p,1);
        }
    }

    private void changeSlot(Player p) {
        if (!plugin.getVc().is1_13to16()) return;
        for (int i = 0; i < 9; i++) {
            ItemStack item = p.getInventory().getItem(i);
            if (item == null || item.getType().equals(Material.AIR)) {
                p.getInventory().setHeldItemSlot(i);
                break;
            }
        }
    }

}