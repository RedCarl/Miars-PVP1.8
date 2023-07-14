package gg.noob.lib.hologram;

import lombok.Getter;
import org.bukkit.Location;

@Getter
public class HologramLine {

    private int entityId;
    private int clickId;
    private String text;
    private Location location;

    public HologramLine(int entityId, int clickId, String text, Location location) {
        this.entityId = entityId;
        this.clickId = clickId;
        this.text = text;
        this.location = location;
    }

}
