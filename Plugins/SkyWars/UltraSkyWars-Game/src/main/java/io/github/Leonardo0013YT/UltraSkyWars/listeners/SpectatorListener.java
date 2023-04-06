package io.github.Leonardo0013YT.UltraSkyWars.listeners;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.superclass.Game;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockCanBuildEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class SpectatorListener implements Listener {

    public UltraSkyWars plugin;

    public SpectatorListener(UltraSkyWars plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        Player p = e.getPlayer();
        Game game = plugin.getGm().getGameByPlayer(p);
        if (game == null) {
            return;
        }
        if (game.getSpectators().contains(p)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        Player p = e.getPlayer();
        Game game = plugin.getGm().getGameByPlayer(p);
        if (game == null) {
            return;
        }
        if (game.getSpectators().contains(p)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onChest(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        Game game = plugin.getGm().getGameByPlayer(p);
        if (game != null) {
            if (game.getSpectators().contains(p)) {
                if (e.getAction().equals(Action.PHYSICAL)) {
                    e.setCancelled(true);
                    e.setUseInteractedBlock(Event.Result.DENY);
                }
                if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                    Material m = e.getClickedBlock().getType();
                    if (m.name().contains("CHEST") || m.name().endsWith("DOOR")) {
                        e.setCancelled(true);
                        e.setUseInteractedBlock(Event.Result.DENY);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        Game game = plugin.getGm().getGameByPlayer(p);
        if (game != null) {
            if (game.getSpectators().contains(p)) {
                if (e.getClick().isShiftClick()) {
                    Inventory clicked = e.getClickedInventory();
                    if (clicked == e.getWhoClicked().getInventory()) {
                        ItemStack clickedOn = e.getCurrentItem();
                        if (clickedOn != null) {
                            e.setCancelled(true);
                        }
                    }
                }
                Inventory clicked = e.getClickedInventory();
                if (clicked != e.getWhoClicked().getInventory()) {
                    ItemStack onCursor = e.getCursor();
                    if (onCursor != null) {
                        e.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onDrag(InventoryDragEvent e) {
        Player p = (Player) e.getWhoClicked();
        Game game = plugin.getGm().getGameByPlayer(p);
        if (game != null) {
            if (game.getSpectators().contains(p)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    protected void onBlockCanBuild(BlockCanBuildEvent e) {
        if (!e.isBuildable()) {
            Location blockL = e.getBlock().getLocation();
            boolean allowed = false;
            for (Player target : Bukkit.getServer().getOnlinePlayers()) {
                Game game = plugin.getGm().getGameByPlayer(target);
                if (game != null) {
                    if (target.getWorld().equals(e.getBlock().getWorld()) && game.getSpectators().contains(target)) {
                        Location playerL = target.getLocation();
                        if (playerL.getX() > blockL.getBlockX() - 2 && playerL.getX() < blockL.getBlockX() + 2) {
                            if (playerL.getZ() > blockL.getBlockZ() - 2 && playerL.getZ() < blockL.getBlockZ() + 2) {
                                if (playerL.getY() > blockL.getBlockY() - 3 && playerL.getY() < blockL.getBlockY() + 2) {
                                    if (game.getSpectators().contains(target)) {
                                        allowed = true;
                                        target.teleport(e.getBlock().getLocation().add(0, 10, 0), TeleportCause.PLUGIN);
                                    } else {
                                        allowed = false;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            e.setBuildable(allowed);
        }
    }

    @EventHandler
    public void onPlayer(BlockPlaceEvent e) {
        Location location = e.getBlock().getLocation();
        for (Player on : Bukkit.getOnlinePlayers()) {
            Game game = plugin.getGm().getGameByPlayer(on);
            if (game != null) {
                if (game.getSpectators().contains(on)) {
                    Location location2 = on.getLocation();
                    if (location2.getX() > location.getBlockX() - 2 && location2.getX() < location.getBlockX() + 2 && location2.getZ() > location.getBlockZ() - 2 && location2.getZ() < location.getBlockZ() + 2 && location2.getY() > location.getBlockY() - 4 && location2.getY() < location.getBlockY() + 2) {
                        on.teleport(on.getLocation().add(5, 5, 5));
                    }
                }
            }
        }
    }

    @EventHandler
    protected void onEntityDamageEvent(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
            Player d = (Player) e.getDamager();
            Player en = (Player) e.getEntity();
            Game game = plugin.getGm().getGameByPlayer(d);
            if (game != null) {
                if ((!e.getDamager().hasMetadata("NPC") && game.getSpectators().contains(d) || (!e.getEntity().hasMetadata("NPC") && game.getSpectators().contains(en)))) {
                    e.setCancelled(true);
                }
            }
        } else if (!(e.getEntity() instanceof Player) && e.getDamager() instanceof Player) {
            Player d = (Player) e.getDamager();
            Game game = plugin.getGm().getGameByPlayer(d);
            if (game != null) {
                if (!e.getDamager().hasMetadata("NPC") && game.getSpectators().contains(d)) {
                    e.setCancelled(true);
                }
            }
        } else if (e.getEntity() instanceof Player && !(e.getDamager() instanceof Player)) {
            Player en = (Player) e.getEntity();
            Game game = plugin.getGm().getGameByPlayer(en);
            if (game != null) {
                if (!e.getEntity().hasMetadata("NPC") && game.getSpectators().contains(en)) {
                    e.setCancelled(true);
                }
            }
        }

        if (e.getDamager() instanceof Projectile
                && !(e.getDamager() instanceof ThrownPotion)
                && e.getEntity() instanceof Player
                && !e.getEntity().hasMetadata("NPC")) {
            Player spectatorInvolved = (Player) e.getEntity();
            Game game = plugin.getGm().getGameByPlayer(spectatorInvolved);
            if (game == null) {
                return;
            }
            if (!game.getSpectators().contains(spectatorInvolved)) {
                return;
            }
            e.setCancelled(true);
            e.getDamager().remove();
            boolean wasFlying = spectatorInvolved.isFlying();
            Location initialSpectatorLocation = spectatorInvolved.getLocation();

            Vector initialProjectileVelocity = e.getDamager().getVelocity();
            Location initialProjectileLocation = e.getDamager().getLocation();
            Projectile proj = (Projectile) e.getDamager();

            if (spectatorInvolved != proj.getShooter()) {
                spectatorInvolved.setAllowFlight(true);
                spectatorInvolved.setFlying(true);
                spectatorInvolved.teleport(initialSpectatorLocation.clone().add(0, 6, 0), TeleportCause.PLUGIN);

                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    if (proj instanceof Arrow) {
                        Arrow arrow = initialProjectileLocation.getWorld().spawn(initialProjectileLocation, Arrow.class);
                        arrow.setBounce(false);
                        arrow.setVelocity(initialProjectileVelocity);
                        arrow.setShooter(proj.getShooter());
                    } else if (proj instanceof Snowball) {
                        Snowball snowball = initialProjectileLocation.getWorld().spawn(initialProjectileLocation, Snowball.class);
                        snowball.setVelocity(initialProjectileVelocity);
                        snowball.setShooter(proj.getShooter());
                    } else if (proj instanceof Egg) {
                        Egg egg = initialProjectileLocation.getWorld().spawn(initialProjectileLocation, Egg.class);
                        egg.setVelocity(initialProjectileVelocity);
                        egg.setShooter(proj.getShooter());
                    } else if (proj instanceof EnderPearl) {
                        Player p = (Player) proj.getShooter();
                        p.launchProjectile(EnderPearl.class, initialProjectileVelocity);
                    }
                }, 1L);

                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    spectatorInvolved.teleport(new Location(initialSpectatorLocation.getWorld(), initialSpectatorLocation.getX(), initialSpectatorLocation.getY(), initialSpectatorLocation.getZ(), spectatorInvolved.getLocation().getYaw(), spectatorInvolved.getLocation().getPitch()), TeleportCause.PLUGIN);
                    spectatorInvolved.setAllowFlight(true);
                    spectatorInvolved.setFlying(wasFlying);
                }, 5L);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    protected void onPotionSplash(PotionSplashEvent e) {
        ArrayList<UUID> spectatorsAffected = new ArrayList<>();
        for (LivingEntity player : e.getAffectedEntities()) {
            if (player instanceof Player) {
                Player p = (Player) player;
                Game game = plugin.getGm().getGameByPlayer(p);
                if (game != null) {
                    if (!player.hasMetadata("NPC") && game.getSpectators().contains(p)) {
                        spectatorsAffected.add(player.getUniqueId());
                    }
                }
            }
        }
        if (!spectatorsAffected.isEmpty()) {
            boolean teleportationNeeded = false;
            for (Entity entity : e.getEntity().getNearbyEntities(2, 2, 2)) {
                if (entity instanceof Player) {
                    Player p = (Player) entity;
                    Game game = plugin.getGm().getGameByPlayer(p);
                    if (game != null) {
                        if (!entity.hasMetadata("NPC") && game.getSpectators().contains(p)) {
                            teleportationNeeded = true;
                        }
                    }
                }
            }
            HashMap<UUID, Boolean> oldFlyMode = new HashMap<>();
            for (UUID spectatorUUID : spectatorsAffected) {
                Player spectator = Bukkit.getServer().getPlayer(spectatorUUID);
                e.setIntensity(spectator, 0);
                if (teleportationNeeded) {
                    oldFlyMode.put(spectator.getUniqueId(), spectator.isFlying());
                    spectator.setAllowFlight(true);
                    spectator.setFlying(true);

                    spectator.teleport(spectator.getLocation().add(0, 10, 0), TeleportCause.PLUGIN);
                }
            }
            if (teleportationNeeded) {
                Location initialProjectileLocation = e.getEntity().getLocation();
                Vector initialProjectileVelocity = e.getEntity().getVelocity();
                Bukkit.getServer().getScheduler().runTaskLater(plugin, () -> {
                    ThrownPotion clonedEntity = (ThrownPotion) e.getEntity().getWorld().spawnEntity(initialProjectileLocation, e.getEntity().getType());
                    clonedEntity.setShooter(e.getEntity().getShooter());
                    clonedEntity.setTicksLived(e.getEntity().getTicksLived());
                    clonedEntity.setFallDistance(e.getEntity().getFallDistance());
                    clonedEntity.setBounce(e.getEntity().doesBounce());
                    if (e.getEntity().getPassenger() != null) {
                        clonedEntity.setPassenger(e.getEntity().getPassenger());
                    }
                    clonedEntity.setItem(e.getEntity().getItem());
                    clonedEntity.setVelocity(initialProjectileVelocity);
                    e.getEntity().remove();
                }, 1L);

                Bukkit.getServer().getScheduler().runTaskLater(plugin, () -> {
                    for (UUID spectatorUUID : spectatorsAffected) {
                        Player spectator = Bukkit.getServer().getPlayer(spectatorUUID);

                        spectator.teleport(spectator.getLocation().add(0, -10, 0), TeleportCause.PLUGIN);
                        spectator.setAllowFlight(true);
                        spectator.setFlying(oldFlyMode.get(spectatorUUID));
                    }
                }, 5L);

                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    protected void onPlayerPickupItem(PlayerPickupItemEvent e) {
        Player p = e.getPlayer();
        Game game = plugin.getGm().getGameByPlayer(p);
        if (game != null) {
            if (game.getSpectators().contains(p)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    protected void onEntityTarget(EntityTargetEvent e) {
        if (e.getTarget() == null) {
            return;
        }
        if (e.getTarget() instanceof Player) {
            Player p = (Player) e.getTarget();
            Game game = plugin.getGm().getGameByPlayer(p);
            if (game != null) {
                if (!e.getTarget().hasMetadata("NPC") && game.getSpectators().contains(p)) {
                    e.setCancelled(true);
                }
            }
        }
        if (e.getTarget() instanceof Player) {
            Player p = (Player) e.getTarget();
            Game game = plugin.getGm().getGameByPlayer(p);
            if (game != null) {
                if (game.getSpectators().contains(p)) {
                    if (e.getEntity() instanceof ExperienceOrb) {
                        repellExpOrb((Player) e.getTarget(), (ExperienceOrb) e.getEntity());
                        e.setCancelled(true);
                        e.setTarget(null);
                    }
                }
            }
        }
    }

    @EventHandler
    protected void onBlockDamage(BlockDamageEvent e) {
        Player p = e.getPlayer();
        Game game = plugin.getGm().getGameByPlayer(p);
        if (game != null) {
            if (game.getSpectators().contains(p)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    protected void onEntityDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            Game game = plugin.getGm().getGameByPlayer(p);
            if (game != null) {
                if (!e.getEntity().hasMetadata("NPC") && game.getSpectators().contains(p)) {
                    e.setCancelled(true);
                    e.getEntity().setFireTicks(0);
                }
            }
        }
    }

    @EventHandler
    protected void onFoodLevelChange(FoodLevelChangeEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            Game game = plugin.getGm().getGameByPlayer(p);
            if (game != null) {
                if (!e.getEntity().hasMetadata("NPC") && game.getSpectators().contains(p)) {
                    e.setCancelled(true);
                    p.setFoodLevel(20);
                    p.setSaturation(20);
                }
            }
        }
    }

    @EventHandler
    public void onVehicleEnter(VehicleEnterEvent e) {
        if (e.getEntered() instanceof Player) {
            Player p = (Player) e.getEntered();
            Game game = plugin.getGm().getGameByPlayer(p);
            if (game != null) {
                if (game.getSpectators().contains(p)) {
                    e.setCancelled(true);
                }
            }
        }
    }

    void repellExpOrb(Player player, ExperienceOrb orb) {
        Location pLoc = player.getLocation();
        Location oLoc = orb.getLocation();
        Vector dir = oLoc.toVector().subtract(pLoc.toVector());
        double dx = Math.abs(dir.getX());
        double dz = Math.abs(dir.getZ());
        if ((dx == 0.0) && (dz == 0.0)) {
            dir.setX(0.001);
        }
        if ((dx < 3.0) && (dz < 3.0)) {
            Vector nDir = dir.normalize();
            Vector newV = nDir.clone().multiply(0.3);
            newV.setY(0);
            orb.setVelocity(newV);
            if ((dx < 1.0) && (dz < 1.0)) {
                orb.teleport(oLoc.clone().add(nDir.multiply(1.0)), TeleportCause.PLUGIN);
            }
            if ((dx < 0.5) && (dz < 0.5)) {
                orb.remove();
            }
        }
    }

}
