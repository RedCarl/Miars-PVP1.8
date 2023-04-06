package io.github.Leonardo0013YT.UltraSkyWars.addons.placeholders;

import io.github.Leonardo0013YT.UltraSkyWars.interfaces.PlaceholderAddon;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

public class PlaceholderAPIAddon implements PlaceholderAddon {

    @Override
    public String parsePlaceholders(Player p, String value) {
        return PlaceholderAPI.setPlaceholders(p, value);
    }

}