package io.github.Leonardo0013YT.UltraSkyWars.listeners;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.enums.HealthType;
import io.github.Leonardo0013YT.UltraSkyWars.enums.ProjectileType;
import io.github.Leonardo0013YT.UltraSkyWars.superclass.Game;
import io.github.Leonardo0013YT.UltraSkyWars.utils.Tagged;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

import java.util.ArrayList;
import java.util.List;

public class SpecialListener implements Listener {

    @EventHandler
    public void onHealth(EntityRegainHealthEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            UltraSkyWars plugin = UltraSkyWars.get();
            Game game = plugin.getGm().getGameByPlayer(p);
            if (game == null) {
                return;
            }
            if (plugin.getTgm().hasTag(p)) {
                Tagged tag = plugin.getTgm().getTagged(p);
                tag.removeDamage(e.getAmount());
            }
            if (game.getHealthType().equals(HealthType.UHC)) {
                if (e.getRegainReason().equals(EntityRegainHealthEvent.RegainReason.EATING) || e.getRegainReason().equals(EntityRegainHealthEvent.RegainReason.SATIATED)) {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onHit(ProjectileHitEvent e) {
        if (e.getEntity().getShooter() instanceof Player) {
            Projectile proj = e.getEntity();
            Player p = (Player) proj.getShooter();
            if (proj.hasMetadata("TYPE")) {
                String type = proj.getMetadata("TYPE").get(0).asString();
                ProjectileType pt = ProjectileType.valueOf(type);
                if (proj instanceof Arrow) {
                    if (pt.isDestructor()) {
                        for (Block b : getAroundBlock(proj.getLocation())) {
                            b.setType(Material.AIR);
                        }
                    }
                    if (pt.isExplosive()) {
                        World w = proj.getWorld();
                        w.createExplosion(proj.getLocation(), 3, true);
                    }
                    if (pt.isTeleporter()) {
                        p.teleport(proj.getLocation().add(0, 1, 0));
                    }
                }
                proj.removeMetadata("TYPE", UltraSkyWars.get());
            }
        }
    }

    public List<Block> getAroundBlock(Location loc) {
        List<Block> blocks = new ArrayList<>();
        Location l = loc.clone();
        if (!l.getBlock().getType().equals(Material.AIR)) {
            blocks.add(l.getBlock());
        }
        if (!l.clone().add(0, 1, 0).getBlock().getType().equals(Material.AIR)) {
            blocks.add(l.clone().add(0, 1, 0).getBlock());
        }
        if (!l.clone().add(1, 0, 0).getBlock().getType().equals(Material.AIR)) {
            blocks.add(l.clone().add(1, 0, 0).getBlock());
        }
        if (!l.clone().add(0, 0, 1).getBlock().getType().equals(Material.AIR)) {
            blocks.add(l.clone().add(0, 0, 1).getBlock());
        }
        if (!l.clone().add(-1, 0, 0).getBlock().getType().equals(Material.AIR)) {
            blocks.add(l.clone().add(-1, 0, 0).getBlock());
        }
        if (!l.clone().add(0, 0, -1).getBlock().getType().equals(Material.AIR)) {
            blocks.add(l.clone().add(0, 0, -1).getBlock());
        }
        if (!l.clone().add(0, -1, 0).getBlock().getType().equals(Material.AIR)) {
            blocks.add(l.clone().add(0, -1, 0).getBlock());
        }
        return blocks;
    }

}