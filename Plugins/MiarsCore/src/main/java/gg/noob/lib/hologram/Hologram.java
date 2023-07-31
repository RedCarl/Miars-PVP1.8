package gg.noob.lib.hologram;

import cn.mcarl.miars.core.MiarsCore;
import gg.noob.lib.util.nms.NMS_1_8_R3;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

@Getter
public class Hologram {

    private Map<UUID, List<HologramLine>> lines = new HashMap<>();
    private BaseHologram baseHologram;
    private Location location;
    private int updatePerSeconds;
    private int displayRange = 48;

    public Hologram(BaseHologram baseHologram) {
        baseHologram.setHologram(this);

        this.baseHologram = baseHologram;
        this.location = baseHologram.getLocation();
        this.updatePerSeconds = baseHologram.getUpdatePerSeconds();
    }

    public void updateViewers() {
        for (Player viewer : baseHologram.getViewers()) {
            List<HologramLine> list = new ArrayList<>();
            int i = 0;
            Location location = this.location.clone();
            for (String text : baseHologram.getTexts(viewer)) {
                int id = lines.containsKey(viewer.getUniqueId()) ? (lines.get(viewer.getUniqueId())).get(i).getEntityId() : NMS_1_8_R3.getFreeEntityId();
                int clickId = lines.containsKey(viewer.getUniqueId()) ? (lines.get(viewer.getUniqueId())).get(i).getClickId() : NMS_1_8_R3.getFreeEntityId();

                list.add(new HologramLine(id, clickId, text, location.clone()));

                i++;

                location.add(0, -0.3, 0);
            }

            lines.put(viewer.getUniqueId(), list);
        }
    }

    public void run() {
        if (updatePerSeconds != -1) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    for (Player player : baseHologram.getViewers()) {
                        update(player);
                    }

                }
            }.runTaskTimer(MiarsCore.getInstance(), updatePerSeconds * 20L, updatePerSeconds * 20L);
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                updateViewers();

                for (Player player : baseHologram.getViewers()) {
                    for (HologramLine line : lines.get(player.getUniqueId())) {
                        NMS_1_8_R3.updateFakeEntityCustomName(player, "", line.getClickId());
                    }
                }
            }
        }.runTaskTimer(MiarsCore.getInstance(), 1L, 1L);
    }

    public void show(Player player) {
        updateViewers();

        for (HologramLine line : lines.get(player.getUniqueId())) {
            int id = line.getEntityId();
            int clickId = line.getClickId();
            String text = line.getText();
            Location location = line.getLocation();

            NMS_1_8_R3.showFakeEntityArmorStand(player, location.clone(), id, true, true, true);
            NMS_1_8_R3.showFakeEntityArmorStand(player, location.clone(), clickId, true, true, true);
            NMS_1_8_R3.updateFakeEntityCustomName(player, text, id);

        }

    }

    public void update(Player player) {
        List<String> newTexts = baseHologram.getTexts(player);
        // newTexts.removeIf(text -> text.equalsIgnoreCase(""));
        if (newTexts.size() != lines.get(player.getUniqueId()).size()) {
            throw new IllegalStateException("New texts size is not same with old texts size");
        }

        if (!isInDisplayRange(player)) {
            return;
        }

        updateViewers();

        if (!baseHologram.getViewers().contains(player)) {
            return;
        }

        int i = 0;
        for (HologramLine line : lines.get(player.getUniqueId())) {
            int id = line.getEntityId();
            String text = newTexts.get(i);

            NMS_1_8_R3.updateFakeEntityCustomName(player, text, id);

            i++;
        }

    }

    public void destroy(Player player) {
        if (!baseHologram.getViewers().contains(player)) {
            return;
        }

        for (HologramLine line : lines.get(player.getUniqueId())) {
            NMS_1_8_R3.hideFakeEntities(player, line.getEntityId());
            NMS_1_8_R3.hideFakeEntities(player, line.getClickId());
        }
    }

    public void remove(Player player) {
        lines.remove(player.getUniqueId());
    }

    public boolean isInDisplayRange(Player player) {
        return isInDisplayRange(player.getLocation());
    }

    public boolean isInDisplayRange(Location location) {
        return location.distanceSquared(this.location) < (displayRange * displayRange);
    }
}
