package io.github.Leonardo0013YT.UltraSkyWars.modules.signs.listeners;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.modules.signs.InjectionSigns;
import io.github.Leonardo0013YT.UltraSkyWars.modules.signs.signs.GameSign;
import io.github.Leonardo0013YT.UltraSkyWars.superclass.Game;
import io.github.Leonardo0013YT.UltraSkyWars.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;

public class SignsListener implements Listener {

    private UltraSkyWars plugin;
    private InjectionSigns signsInjection;

    public SignsListener(UltraSkyWars plugin, InjectionSigns signsInjection) {
        this.plugin = plugin;
        this.signsInjection = signsInjection;
    }

    @EventHandler
    public void onSign(SignChangeEvent e) {
        Player p = e.getPlayer();
        if (!p.hasPermission("usw.admin")) {
            return;
        }
        if (e.getLine(0).equalsIgnoreCase("[SW]")) {
            if (e.getLine(1) == null) {
                return;
            }
            String type = e.getLine(1).toUpperCase();
            if (plugin.getGm().getModes().contains(type)) {
                Location loc = e.getBlock().getLocation();
                String l = Utils.getLocationString(loc);
                ArrayList<String> locs = new ArrayList<>(signsInjection.getSigns().getListOrDefault("signs." + type, new ArrayList<>()));
                if (locs.contains(l)) {
                    p.sendMessage("§cThis sign is already added.");
                    return;
                }
                locs.add(l);
                setSign(type, locs);
                p.sendMessage(plugin.getLang().get(p, "messages.signs.addSign").replace("<type>", type.toLowerCase()));
            } else {
                p.sendMessage(plugin.getLang().get(p, "messages.signs.noType"));
            }
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        if (!p.hasPermission("usw.admin")) {
            return;
        }
        Location loc = e.getBlock().getLocation();
        if (signsInjection.getSim().getGameSignByLoc(loc) != null) {
            GameSign us = signsInjection.getSim().getGameSignByLoc(loc);
            String l = Utils.getLocationString(loc);
            String type = us.getType().toUpperCase();
            ArrayList<String> locs = new ArrayList<>(signsInjection.getSigns().getListOrDefault("signs." + type, new ArrayList<>()));
            if (!locs.contains(l)) {
                return;
            }
            locs.remove(l);
            setSign(type, locs);
            p.sendMessage(plugin.getLang().get(p, "messages.removedSing"));
        }
    }

    private void setSign(String type, ArrayList<String> locs) {
        signsInjection.getSigns().set("signs." + type, locs);
        signsInjection.getSigns().save();
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            signsInjection.getSim().reload();
            plugin.getGm().getGames().values().forEach(Game::updateSign);
            if (plugin.getCm().isBungeeModeLobby()) {
                plugin.getGm().getModes().forEach(mode -> {
                    //plugin.getBm().sendMessage("usw:callback", mode);
                });
            }
        }, 2L);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (e.getAction().equals(Action.PHYSICAL)) {
            return;
        }
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            Block b = e.getClickedBlock();
            if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                if (b.getState() instanceof Sign) {
                    if (plugin.getCm().isSignsRight() && !plugin.getGm().isPlayerInGame(p)) {
                        if (sendToServer(e, p, b)) return;
                    }
                }
            }
            if (e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
                if (b.getState() instanceof Sign) {
                    if (p.hasPermission("usw.admin") && p.isSneaking()) return;
                    if (plugin.getCm().isSignsLeft() && !plugin.getGm().isPlayerInGame(p)) {
                        sendToServer(e, p, b);
                    }
                }
            }
        }
    }

    private boolean sendToServer(PlayerInteractEvent e, Player p, Block b) {
        GameSign us = signsInjection.getSim().getGameSignByLoc(b.getLocation());
        if (us == null) {
            return true;
        }
        if (us.isOccupied()) {
            e.setCancelled(true);
            if (!us.getData().getServer().equals("")) {
                plugin.sendToServer(p, us.getData().getServer());
            } else {
                plugin.getGm().addPlayerGame(p, plugin.getGm().getGameID(us.getData().getMap()));
            }
        }
        return false;
    }

}