package cn.mcarl.miars.lobby.conf;

import cc.carm.lib.configuration.core.ConfigurationRoot;
import cc.carm.lib.configuration.core.annotation.HeaderComment;
import cc.carm.lib.configuration.core.value.ConfigValue;
import cc.carm.lib.configuration.core.value.type.ConfiguredValue;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class PluginConfig extends ConfigurationRoot {

    @HeaderComment({"大厅配置"})
    public static final class LOBBY_SITE extends ConfigurationRoot {
        @HeaderComment({"出生点"})
        public static final ConfigValue<Location> LOCATION = ConfiguredValue.of(Location.class, new Location(Bukkit.getWorld("world"),-79.5,153,-78.5));
        @HeaderComment({"玩家的高度低于多少将他传送至出生点"})
        public static final ConfigValue<Integer> HEIGHT = ConfiguredValue.of(Integer.class, 0);
    }
}
