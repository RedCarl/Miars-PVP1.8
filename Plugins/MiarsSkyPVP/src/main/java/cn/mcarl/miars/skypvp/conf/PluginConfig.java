package cn.mcarl.miars.skypvp.conf;

import cc.carm.lib.configuration.core.ConfigurationRoot;
import cc.carm.lib.configuration.core.annotation.HeaderComment;
import cc.carm.lib.configuration.core.value.ConfigValue;
import cc.carm.lib.configuration.core.value.type.ConfiguredValue;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class PluginConfig extends ConfigurationRoot {

    @HeaderComment({"保护区"})
    public static final class PROTECTED_REGION extends ConfigurationRoot {
        @HeaderComment({"出生点"})
        public static final ConfigValue<Location> SPAWN = ConfiguredValue.of(Location.class, new Location(Bukkit.getWorld("world"),3.5,150,-0.5));
        @HeaderComment({"最小位置"})
        public static final ConfigValue<Location> MIN = ConfiguredValue.of(Location.class, new Location(Bukkit.getWorld("world"),-32.5,0,-29));
        @HeaderComment({"最大位置"})
        public static final ConfigValue<Location> MAX = ConfiguredValue.of(Location.class, new Location(Bukkit.getWorld("world"),36.5,256,28.5));
    }

    @HeaderComment({"挂机池"})
    public static final class AFK_REGION extends ConfigurationRoot {
        @HeaderComment({"最小位置"})
        public static final ConfigValue<Location> MIN = ConfiguredValue.of(Location.class, new Location(Bukkit.getWorld("world"),-32.5,0,-29));
        @HeaderComment({"最大位置"})
        public static final ConfigValue<Location> MAX = ConfiguredValue.of(Location.class, new Location(Bukkit.getWorld("world"),36.5,256,28.5));
    }

}
