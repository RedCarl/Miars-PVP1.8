package cn.mcarl.miars.faction.conf;

import cc.carm.lib.configuration.core.ConfigurationRoot;
import cc.carm.lib.configuration.core.annotation.HeaderComment;
import cc.carm.lib.configuration.core.value.ConfigValue;
import cc.carm.lib.configuration.core.value.type.ConfiguredValue;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class PluginConfig extends ConfigurationRoot {

    @HeaderComment({"主城配置"})
    public static final class PROTECTED_REGION extends ConfigurationRoot {
        @HeaderComment({"主世界名称"})
        public static final ConfigValue<String> WORLD_NAME = ConfiguredValue.of(String.class, "faction");
        @HeaderComment({"主城出生点"})
        public static final ConfigValue<Location> SPAWN = ConfiguredValue.of(Location.class, new Location(Bukkit.getWorld("faction_spawn"),3.5,150,-0.5));
    }

}
