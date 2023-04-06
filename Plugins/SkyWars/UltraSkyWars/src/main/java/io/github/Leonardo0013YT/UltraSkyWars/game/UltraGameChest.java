package io.github.Leonardo0013YT.UltraSkyWars.game;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.utils.Utils;
import io.github.Leonardo0013YT.UltraSkyWars.xseries.XMaterial;
import org.bukkit.Location;
import org.bukkit.block.Chest;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.Inventory;
import org.bukkit.metadata.FixedMetadataValue;

public class UltraGameChest {

    private final Chest chest;
    private final Location loc;
    private boolean isEmpty, spawned = false;
    private ArmorStand timer, info;

    public UltraGameChest(Location l) {
        this.loc = l;
        this.chest = (Chest) l.getBlock().getState();
        this.isEmpty = false;
    }

    public void delete() {
        if (timer != null) {
            timer.remove();
            timer = null;
        }
        if (info != null) {
            info.remove();
            info = null;
        }
        this.isEmpty = false;
        this.spawned = false;
    }

    public void hide() {
        if (timer != null) {
            timer.setCustomNameVisible(false);
        }
        if (info != null) {
            info.setCustomNameVisible(false);
        }
        this.spawned = false;
    }

    public void spawn(boolean isEmpty) {
        UltraSkyWars plugin = UltraSkyWars.get();
        if (timer == null) {
            this.timer = Utils.getChestStand(loc.clone().add(0.5, 0.2, 0.5));
            timer.setCustomNameVisible(true);
            timer.setCustomName(plugin.getLang().get("timer"));
            timer.setMetadata("CHEST_ARMOR", new FixedMetadataValue(plugin, ""));
            this.spawned = true;
        }
        if (!this.isEmpty && isEmpty) {
            if (info == null) {
                this.info = Utils.getChestStand(loc.clone().add(0.5, -0.15, 0.5));
            }
            timer.teleport(timer.getLocation().add(0, 0.25, 0));
            info.setMetadata("CHEST_ARMOR", new FixedMetadataValue(plugin, ""));
            info.setCustomNameVisible(true);
            info.setCustomName(plugin.getLang().get("chestEmpty"));
        } else if (this.isEmpty && !isEmpty) {
            if (info != null) {
                timer.teleport(timer.getLocation().add(0, -0.25, 0));
                info.setCustomNameVisible(false);
            }
        }
        this.isEmpty = isEmpty;
    }

    public boolean isSpawned() {
        return spawned;
    }

    public void updateTimer(String text) {
        timer.setCustomName(text);
    }

    public Chest getChest() {
        return chest;
    }

    public Inventory getInv() {
        if ((!loc.getBlock().getType().equals(XMaterial.ENDER_CHEST.parseMaterial()) && !loc.getBlock().getType().equals(XMaterial.TRAPPED_CHEST.parseMaterial()) && !loc.getBlock().getType().equals(XMaterial.CHEST.parseMaterial())) || chest == null) {
            return null;
        }
        return chest.getInventory();
    }

    public Location getLoc() {
        return loc;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

}