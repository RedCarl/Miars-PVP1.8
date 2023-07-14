package gg.noob.lib.hologram;

import gg.noob.lib.hologram.click.ClickType;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

public interface BaseHologram {

    void setHologram(Hologram hologram);

    List<String> getTexts(Player player);

    Location getLocation();

    int getUpdatePerSeconds();

    List<Player> getViewers();

    void onClick(Player player, HologramLine line, ClickType clickType);

}
