package cn.mcarl.miars.practiceffa.conf;

import cc.carm.lib.configuration.core.ConfigurationRoot;
import cc.carm.lib.configuration.core.annotation.HeaderComment;
import cc.carm.lib.configuration.core.value.ConfigValue;
import cc.carm.lib.configuration.core.value.type.ConfiguredList;
import cc.carm.lib.configuration.core.value.type.ConfiguredValue;
import cn.mcarl.miars.core.utils.MiarsUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class PluginConfig extends ConfigurationRoot {

    @HeaderComment({"FFA竞技场配置"})
    public static final class FFA_SITE extends ConfigurationRoot {
        @HeaderComment({"出生点"})
        public static final ConfigValue<Location> LOCATION = ConfiguredValue.of(Location.class, new Location(Bukkit.getWorld("world"),0,4,0));
        @HeaderComment({"竞技场出生点半径"})
        public static final ConfigValue<Integer> RADIUS = ConfiguredValue.of(Integer.class, 3);
        @HeaderComment({"竞技场建筑区域半径"})
        public static final ConfigValue<Integer> BUILD_RADIUS = ConfiguredValue.of(Integer.class, 10);
        @HeaderComment({"玻璃的检测范围"})
        public static final ConfigValue<Integer> BORDER_RADIUS = ConfiguredValue.of(Integer.class, 3);
        @HeaderComment({"TICK的速度"})
        public static final ConfigValue<Integer> BORDER_TICK = ConfiguredValue.of(Integer.class, 3);
    }
    @HeaderComment({"全息图排行榜"})
    public static final class LEADERBOARDS_HOLOGRAM extends ConfigurationRoot {
        @HeaderComment({"每日胜利场数"})
        public static final ConfigValue<Location> DAILY_WINSTREAK = ConfiguredValue.of(Location.class, new Location(Bukkit.getWorld("world"),0,4,0));
    }
}
