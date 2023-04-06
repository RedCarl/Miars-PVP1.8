package io.github.Leonardo0013YT.UltraSkyWars.objects;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.cosmetics.Balloon;
import io.github.Leonardo0013YT.UltraSkyWars.cosmetics.Glass;
import io.github.Leonardo0013YT.UltraSkyWars.utils.Utils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PreviewCosmetic {

    private Location playerLocation;
    private String type;
    private ArmorStand player;
    private ArmorStand cosmetic;
    private Location cosmeticLocation;
    private HashMap<UUID, PreviewSession> preview = new HashMap<>();

    public PreviewCosmetic(String type, Location playerLocation, Location cosmeticLocation) {
        this.type = type;
        this.playerLocation = playerLocation;
        this.player = Utils.getArmorStand(playerLocation);
        this.cosmetic = Utils.getArmorStand(cosmeticLocation);
        this.cosmeticLocation = cosmeticLocation;
    }

    public void addPreview(Player p) {
        if (player == null || player.isDead()) {
            this.player = Utils.getArmorStand(playerLocation);
        }
        preview.put(p.getUniqueId(), new PreviewSession(0, p));
        p.teleport(player);
        p.closeInventory();
        p.getInventory().clear();
        p.getInventory().setArmorContents(null);
        p.setAllowFlight(true);
        p.setFlying(true);
        p.setFlySpeed(0f);
        p.setWalkSpeed(0f);
        p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 999999, 999999));
        new BukkitRunnable() {
            @Override
            public void run() {
                UltraSkyWars.get().getCos().getPlayerPreview().remove(p.getUniqueId());
                removePreview(p);
            }
        }.runTaskLater(UltraSkyWars.get(), 5 * 20);
    }

    public void removePreview(Player p) {
        PreviewSession ps = preview.get(p.getUniqueId());
        UltraSkyWars plugin = UltraSkyWars.get();
        if (type.equals("balloon")) {
            plugin.getVc().getNMS().destroy(p, ps.getEntityId());
        }
        if (type.equals("glass")) {
            Glass glass = plugin.getCos().getGlass(ps.getId());
            for (Map.Entry<Vector, GlassBlock> entry : glass.getPreview().entrySet()) {
                Vector v = entry.getKey();
                p.sendBlockChange(cosmeticLocation.clone().add(v), Material.AIR, (byte) 0);
            }
        }
        ps.reset();
        preview.remove(p.getUniqueId());
    }

    public void execute(Player p, int id) {
        PreviewSession ps = preview.get(p.getUniqueId());
        ps.setId(id);
        UltraSkyWars plugin = UltraSkyWars.get();
        if (type.equals("glass")) {
            Glass glass = plugin.getCos().getGlass(id);
            if (glass.getPreview().isEmpty()) {
                glass.setPreview(plugin.getWc().getEdit().getBlocks(glass.getSchematic()));
            }
            for (Map.Entry<Vector, GlassBlock> entry : glass.getPreview().entrySet()) {
                Vector v = entry.getKey();
                GlassBlock g = entry.getValue();
                p.sendBlockChange(cosmeticLocation.clone().add(v), g.getMaterial(), (byte) g.getData());
            }
        }
        if (type.equals("balloon")) {
            Balloon balloon = plugin.getCos().getBalloon(id);
            plugin.getVc().getNMS().spawn(p, cosmeticLocation, balloon.getActualHead()).forEach(ps::addEntityId);
        }
    }

}