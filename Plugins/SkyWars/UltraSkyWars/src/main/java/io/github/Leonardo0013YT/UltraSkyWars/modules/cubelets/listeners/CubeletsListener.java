package io.github.Leonardo0013YT.UltraSkyWars.modules.cubelets.listeners;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.data.SWPlayer;
import io.github.Leonardo0013YT.UltraSkyWars.modules.cubelets.InjectionCubelets;
import io.github.Leonardo0013YT.UltraSkyWars.modules.cubelets.cubelets.Cubelets;
import io.github.Leonardo0013YT.UltraSkyWars.utils.Utils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CubeletsListener implements Listener {

    private UltraSkyWars plugin;
    private InjectionCubelets injectionCubelets;

    public CubeletsListener(UltraSkyWars plugin, InjectionCubelets injectionCubelets) {
        this.plugin = plugin;
        this.injectionCubelets = injectionCubelets;
    }

    @EventHandler
    public void onInteractAtEntity(PlayerInteractAtEntityEvent e) {
        Player p = e.getPlayer();
        if (e.getRightClicked() instanceof ArmorStand) {
            if (injectionCubelets.getEntities().contains(e.getRightClicked())) {
                e.setCancelled(true);
            }
            return;
        }
        Entity b = e.getRightClicked();
        if (plugin.getCm().isCubeletsEnabled()) {
            if (injectionCubelets.getCbm().getCubelets().containsKey(b.getLocation())) {
                if (!plugin.getGm().isPlayerInGame(p)) {
                    if (plugin.getLvl().isEmpty()) {
                        p.sendMessage(plugin.getLang().get(p, "messages.noRewards"));
                        e.setCancelled(true);
                        return;
                    }
                    SWPlayer pl = plugin.getDb().getSWPlayer(p);
                    if (pl.getCubelets() <= 0) {
                        e.setCancelled(true);
                        p.sendMessage(plugin.getLang().get(p, "messages.noCubelets"));
                        return;
                    }
                    Cubelets sw = injectionCubelets.getCbm().getCubelets().get(b.getLocation());
                    if (sw.isInUse() || (sw.getNow() != null && !sw.getNow().equals(p))) {
                        e.setCancelled(true);
                        p.sendMessage(plugin.getLang().get(p, "messages.cubeletsUse"));
                        return;
                    }
                    e.setCancelled(true);
                    pl.removeCubelets(1);
                    Utils.updateSB(p);
                    sw.setNow(p);
                    sw.setInUse(true);
                    sw.execute();
                }
            }
        }
    }

    @EventHandler
    public void onMenu(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getView().getTitle().equals(plugin.getLang().get(p, "menus.cubelets.title"))) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR) || e.getSlotType().equals(InventoryType.SlotType.OUTSIDE)) {
                return;
            }
            ItemStack item = e.getCurrentItem();
            if (!item.hasItemMeta()) {
                return;
            }
            if (!item.getItemMeta().hasDisplayName()) {
                return;
            }
            ItemMeta im = item.getItemMeta();
            String display = im.getDisplayName();
            if (display.equals(plugin.getLang().get(p, "menus.cubelets.fireworks.nameItem"))) {
                if (!p.hasPermission(injectionCubelets.getCubelets().get(null, "animations.fireworks.perm"))) {
                    p.sendMessage(plugin.getLang().get(p, "messages.noPermission"));
                    return;
                }
                int id = injectionCubelets.getCubelets().getInt("animations.fireworks.id");
                SWPlayer sw = plugin.getDb().getSWPlayer(p);
                if (sw.getCubeAnimation() == id) {
                    p.sendMessage(plugin.getLang().get(p, "messages.alreadySelect"));
                } else {
                    sw.setCubeAnimation(id);
                    p.sendMessage(plugin.getLang().get(p, "messages.selected"));
                    plugin.getGem().createCubeletsAnimationMenu(p);
                }
            }
            if (display.equals(plugin.getLang().get(p, "menus.cubelets.head.nameItem"))) {
                if (!p.hasPermission(injectionCubelets.getCubelets().get(null, "animations.head.perm"))) {
                    p.sendMessage(plugin.getLang().get(p, "messages.noPermission"));
                    return;
                }
                int id = injectionCubelets.getCubelets().getInt("animations.head.id");
                SWPlayer sw = plugin.getDb().getSWPlayer(p);
                if (sw.getCubeAnimation() == id) {
                    p.sendMessage(plugin.getLang().get(p, "messages.alreadySelect"));
                } else {
                    sw.setCubeAnimation(id);
                    p.sendMessage(plugin.getLang().get(p, "messages.selected"));
                    plugin.getGem().createCubeletsAnimationMenu(p);
                }
            }
            if (display.equals(plugin.getLang().get(p, "menus.cubelets.flames.nameItem"))) {
                if (!p.hasPermission(injectionCubelets.getCubelets().get(null, "animations.flames.perm"))) {
                    p.sendMessage(plugin.getLang().get(p, "messages.noPermission"));
                    return;
                }
                int id = injectionCubelets.getCubelets().getInt("animations.flames.id");
                SWPlayer sw = plugin.getDb().getSWPlayer(p);
                if (sw.getCubeAnimation() == id) {
                    p.sendMessage(plugin.getLang().get(p, "messages.alreadySelect"));
                } else {
                    sw.setCubeAnimation(id);
                    p.sendMessage(plugin.getLang().get(p, "messages.selected"));
                    plugin.getGem().createCubeletsAnimationMenu(p);
                }
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (e.getAction().equals(Action.PHYSICAL)) {
            return;
        }
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            Block b = e.getClickedBlock();
            if (plugin.getCm().isCubeletsEnabled()) {
                if (injectionCubelets.getCbm().getCubelets().containsKey(b.getLocation())) {
                    if (!plugin.getGm().isPlayerInGame(p)) {
                        if (plugin.getLvl().isEmpty()) {
                            p.sendMessage(plugin.getLang().get(p, "messages.noRewards"));
                            e.setCancelled(true);
                            return;
                        }
                        SWPlayer pl = plugin.getDb().getSWPlayer(p);
                        if (pl.getCubelets() <= 0) {
                            e.setCancelled(true);
                            p.sendMessage(plugin.getLang().get(p, "messages.noCubelets"));
                            return;
                        }
                        Cubelets sw = injectionCubelets.getCbm().getCubelets().get(b.getLocation());
                        if (sw.isInUse() || (sw.getNow() != null && !sw.getNow().equals(p))) {
                            e.setCancelled(true);
                            p.sendMessage(plugin.getLang().get(p, "messages.cubeletsUse"));
                            return;
                        }
                        e.setCancelled(true);
                        pl.removeCubelets(1);
                        Utils.updateSB(p);
                        sw.setNow(p);
                        sw.setInUse(true);
                        sw.execute();
                    }
                }
            }
        }
    }

}