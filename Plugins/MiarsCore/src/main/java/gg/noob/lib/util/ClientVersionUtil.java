package gg.noob.lib.util;

import org.bukkit.entity.*;
import org.bukkit.*;
import com.viaversion.viaversion.api.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.*;
import org.bukkit.plugin.*;

public class ClientVersionUtil
{
    public static int getProtocolVersion(final Player player) {
        final PluginManager pluginManager = Bukkit.getPluginManager();
        if (pluginManager.getPlugin("ViaVersion") != null) {
            return Via.getAPI().getPlayerVersion(player.getUniqueId());
        }
        return 0;
    }
}
