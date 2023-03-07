package cn.mcarl.miars.skypvp.conf;

import cc.carm.lib.configuration.core.ConfigurationRoot;
import cc.carm.lib.configuration.core.annotation.HeaderComment;
import cc.carm.lib.configuration.core.value.ConfigValue;
import cc.carm.lib.configuration.core.value.type.ConfiguredList;
import cc.carm.lib.configuration.core.value.type.ConfiguredValue;
import cn.mcarl.miars.core.utils.MiarsUtil;
import cn.mcarl.miars.skypvp.entitiy.RewardItem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.List;

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


    @HeaderComment({"幸运方块配置"})
    public static final class LUCKY extends ConfigurationRoot {
        @HeaderComment({"普通幸运方块"})
        public static final class NORMAL extends ConfigurationRoot {
            @HeaderComment({"多久刷新一次 (秒)"})
            public static final ConfigValue<Long> REFRESH = ConfiguredValue.of(Long.class, 10L);
            @HeaderComment({"位置"})
            public static final ConfiguredList<Location> LOCATION = ConfigValue.builder()
                    .asList(Location.class).fromObject()
                    .parseValue(MiarsUtil::locationParse)
                    .serializeValue(Location::serialize)
                    .defaults(
                            new Location(Bukkit.getWorld("world"),0,0,0),
                            new Location(Bukkit.getWorld("world"),1,1,1)
                    )
                    .build();
            @HeaderComment({"奖励内容"})
            public static final ConfiguredList<RewardItem> REWARD = ConfigValue.builder()
                    .asList(RewardItem.class).fromObject()
                    .parseValue(RewardItem::deserialize)
                    .serializeValue(RewardItem::serialize)
                    .defaults(
                            new RewardItem("没有就不填",Material.DIAMOND_SWORD.name(), (short) 0,"钻石剑",List.of("事实上"),50),
                            new RewardItem("没有就不填",Material.STONE.name(), (short) 0,"石头",List.of("啊啊啊"),60)
                    )
                    .build();
        }
        @HeaderComment({"稀有幸运方块"})
        public static final class RARE extends ConfigurationRoot {
            @HeaderComment({"多久刷新一次 (秒)"})
            public static final ConfigValue<Long> REFRESH = ConfiguredValue.of(Long.class, 10L);
            @HeaderComment({"位置"})
            public static final ConfiguredList<Location> LOCATION = ConfigValue.builder()
                    .asList(Location.class).fromObject()
                    .parseValue(MiarsUtil::locationParse)
                    .serializeValue(Location::serialize)
                    .defaults(
                            new Location(Bukkit.getWorld("world"),0,0,0),
                            new Location(Bukkit.getWorld("world"),1,1,1)
                    )
                    .build();
            @HeaderComment({"奖励内容"})
            public static final ConfiguredList<RewardItem> REWARD = ConfigValue.builder()
                    .asList(RewardItem.class).fromObject()
                    .parseValue(RewardItem::deserialize)
                    .serializeValue(RewardItem::serialize)
                    .defaults(
                            new RewardItem("没有就不填",Material.DIAMOND_SWORD.name(), (short) 0,"钻石剑",List.of("事实上"),50),
                            new RewardItem("没有就不填",Material.STONE.name(), (short) 0,"石头",List.of("啊啊啊"),60)
                    )
                    .build();
        }
        @HeaderComment({"史诗幸运方块"})
        public static final class EPIC extends ConfigurationRoot {
            @HeaderComment({"多久刷新一次 (秒)"})
            public static final ConfigValue<Long> REFRESH = ConfiguredValue.of(Long.class, 10L);
            @HeaderComment({"位置"})
            public static final ConfiguredList<Location> LOCATION = ConfigValue.builder()
                    .asList(Location.class).fromObject()
                    .parseValue(MiarsUtil::locationParse)
                    .serializeValue(Location::serialize)
                    .defaults(
                            new Location(Bukkit.getWorld("world"),0,0,0),
                            new Location(Bukkit.getWorld("world"),1,1,1)
                    )
                    .build();
            @HeaderComment({"奖励内容"})
            public static final ConfiguredList<RewardItem> REWARD = ConfigValue.builder()
                    .asList(RewardItem.class).fromObject()
                    .parseValue(RewardItem::deserialize)
                    .serializeValue(RewardItem::serialize)
                    .defaults(
                            new RewardItem("没有就不填",Material.DIAMOND_SWORD.name(), (short) 0,"钻石剑",List.of("事实上"),50),
                            new RewardItem("没有就不填",Material.STONE.name(), (short) 0,"石头",List.of("啊啊啊"),60)
                    )
                    .build();
        }
        @HeaderComment({"传奇幸运方块"})
        public static final class LEGENDARY extends ConfigurationRoot {
            @HeaderComment({"多久刷新一次 (秒)"})
            public static final ConfigValue<Long> REFRESH = ConfiguredValue.of(Long.class, 10L);
            @HeaderComment({"位置"})
            public static final ConfiguredList<Location> LOCATION = ConfigValue.builder()
                    .asList(Location.class).fromObject()
                    .parseValue(MiarsUtil::locationParse)
                    .serializeValue(Location::serialize)
                    .defaults(
                            new Location(Bukkit.getWorld("world"),0,0,0),
                            new Location(Bukkit.getWorld("world"),1,1,1)
                    )
                    .build();
            @HeaderComment({"奖励内容"})
            public static final ConfiguredList<RewardItem> REWARD = ConfigValue.builder()
                    .asList(RewardItem.class).fromObject()
                    .parseValue(RewardItem::deserialize)
                    .serializeValue(RewardItem::serialize)
                    .defaults(
                            new RewardItem("没有就不填",Material.DIAMOND_SWORD.name(), (short) 0,"钻石剑",List.of("事实上"),50),
                            new RewardItem("没有就不填",Material.STONE.name(), (short) 0,"石头",List.of("啊啊啊"),60)
                    )
                    .build();
        }


    }

}
