package cn.mcarl.miars.megawalls.conf;

import cc.carm.lib.configuration.core.ConfigurationRoot;
import cc.carm.lib.configuration.core.annotation.HeaderComment;
import cc.carm.lib.configuration.core.value.ConfigValue;
import cc.carm.lib.configuration.core.value.type.ConfiguredValue;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

public class PluginConfig extends ConfigurationRoot {


    @HeaderComment({"当前服务器模式 LOBBY GAME"})
    public static final ConfigValue<String> MODE = ConfiguredValue.of(String.class, "GAME");
    @HeaderComment({"当前房间名称，大厅就填大厅"})
    public static final ConfigValue<String> NAME = ConfiguredValue.of(String.class, "超级简陋的房间");
    @HeaderComment({"单个队伍最大人数限制"})
    public static final ConfigValue<Integer> TEAM_LIMIT = ConfiguredValue.of(Integer.class, 25);
    @HeaderComment({"多少人后准备开始游戏"})
    public static final ConfigValue<Integer> READY_PLAYERS = ConfiguredValue.of(Integer.class, 25);
    @HeaderComment({"等待阶段时间 (秒)"})
    public static final ConfigValue<Long> WAIT_TIME = ConfiguredValue.of(Long.class, 300L);
    @HeaderComment({"准备阶段时间 (秒) 已经分发到战场的铁笼里面"})
    public static final ConfigValue<Long> READY_TIME = ConfiguredValue.of(Long.class, 10L);
    @HeaderComment({"墙倒塌倒计时 (秒)"})
    public static final ConfigValue<Long> WALL_TIME = ConfiguredValue.of(Long.class, 360L);
    @HeaderComment({"凋灵暴怒 (秒)"})
    public static final ConfigValue<Long> WITHER_FURY = ConfiguredValue.of(Long.class, 480L);
    @HeaderComment({"凋灵掉血 (秒)"})
    public static final ConfigValue<Long> WITHER_TIME = ConfiguredValue.of(Long.class, 3L);
    @HeaderComment({"凋灵掉血 (血)"})
    public static final ConfigValue<Long> WITHER_LOSS = ConfiguredValue.of(Long.class, 2L);
    @HeaderComment({"游戏结束 (秒)"})
    public static final ConfigValue<Long> GAME_OVER = ConfiguredValue.of(Long.class, 1800L);
    @HeaderComment({"踢出玩家并重启服务器 (秒)"})
    public static final ConfigValue<Long> KICK_PLAYER = ConfiguredValue.of(Long.class, 5L);
    @HeaderComment({"凋灵的血量"})
    public static final ConfigValue<Integer> WITHER_HP = ConfiguredValue.of(Integer.class, 500);
    @HeaderComment({"出生点保护范围"})
    public static final ConfigValue<Integer> SPAWN_PROTECT = ConfiguredValue.of(Integer.class, 5);

    @HeaderComment({"巨墙"})
    public static final class WALL extends ConfigurationRoot {
        @HeaderComment({"最矮的位置"})
        public static final ConfigValue<Location> MIN = ConfiguredValue.of(Location.class, new Location(Bukkit.getWorld("world"),-725,38,-89));
        @HeaderComment({"最高的位置"})
        public static final ConfigValue<Location> MAX = ConfiguredValue.of(Location.class, new Location(Bukkit.getWorld("world"),-826,73,-190));
    }

    @HeaderComment({"等待大厅"})
    public static final class LOBBY extends ConfigurationRoot {
        @HeaderComment({"出生点"})
        public static final ConfigValue<Location> SPAWN = ConfiguredValue.of(Location.class, new Location(Bukkit.getWorld("world"),-775.5,104,-139.5));
    }

    @HeaderComment({"红队"})
    public static final class RED_TEAM extends ConfigurationRoot {
        @HeaderComment({"出生点"})
        public static final ConfigValue<Location> SPAWN = ConfiguredValue.of(Location.class, new Location(Bukkit.getWorld("world"),-656.5,41,-83.5,180,0));
        @HeaderComment({"出生点大门"})
        public static final class DOOR extends ConfigurationRoot {
            @HeaderComment({"最矮的位置"})
            public static final ConfigValue<Location> MIN = ConfiguredValue.of(Location.class, new Location(Bukkit.getWorld("world"),-648,41,-89));
            @HeaderComment({"最高的位置"})
            public static final ConfigValue<Location> MAX = ConfiguredValue.of(Location.class, new Location(Bukkit.getWorld("world"),-666,46,-89));
            @HeaderComment({"门的材质，默认屏障"})
            public static final ConfigValue<String> MATERIAL = ConfiguredValue.of(String.class, "BARRIER");
        }
        @HeaderComment({"凋灵位置"})
        public static final ConfigValue<Location> WITHER_SPAWN = ConfiguredValue.of(Location.class, new Location(Bukkit.getWorld("world"),-640.5,59,-120.5,180,0));
        @HeaderComment({"城堡"})
        public static final class CASTLE extends ConfigurationRoot {
            @HeaderComment({"最矮的位置"})
            public static final ConfigValue<Location> MIN = ConfiguredValue.of(Location.class, new Location(Bukkit.getWorld("world"),-659,49,-112));
            @HeaderComment({"最高的位置"})
            public static final ConfigValue<Location> MAX = ConfiguredValue.of(Location.class, new Location(Bukkit.getWorld("world"),-624,120,-166));
        }
    }

    @HeaderComment({"绿队"})
    public static final class GREEN_TEAM extends ConfigurationRoot {
        @HeaderComment({"出生点"})
        public static final ConfigValue<Location> SPAWN = ConfiguredValue.of(Location.class, new Location(Bukkit.getWorld("world"),-830.5,41,-21.5,-90,0));
        @HeaderComment({"出生点大门"})
        public static final class DOOR extends ConfigurationRoot {
            @HeaderComment({"最矮的位置"})
            public static final ConfigValue<Location> MIN = ConfiguredValue.of(Location.class, new Location(Bukkit.getWorld("world"),-826,46,-12));
            @HeaderComment({"最高的位置"})
            public static final ConfigValue<Location> MAX = ConfiguredValue.of(Location.class, new Location(Bukkit.getWorld("world"),-826,41,-30));
            @HeaderComment({"门的材质，默认屏障"})

            public static final ConfigValue<String> MATERIAL = ConfiguredValue.of(String.class, "BARRIER");
        }
        @HeaderComment({"凋灵位置"})
        public static final ConfigValue<Location> WITHER_SPAWN = ConfiguredValue.of(Location.class, new Location(Bukkit.getWorld("world"),-793.5,59,-4.5,-90,0));
        @HeaderComment({"城堡"})
        public static final class CASTLE extends ConfigurationRoot {
            @HeaderComment({"最矮的位置"})
            public static final ConfigValue<Location> MIN = ConfiguredValue.of(Location.class, new Location(Bukkit.getWorld("world"),-803,49,-23));
            @HeaderComment({"最高的位置"})
            public static final ConfigValue<Location> MAX = ConfiguredValue.of(Location.class, new Location(Bukkit.getWorld("world"),-749,131,12));
        }
    }

    @HeaderComment({"黄队"})
    public static final class YELLOW_TEAM extends ConfigurationRoot {
        @HeaderComment({"出生点"})
        public static final ConfigValue<Location> SPAWN = ConfiguredValue.of(Location.class, new Location(Bukkit.getWorld("world"), -894.5, 41, -194.5, 0, 0));

        @HeaderComment({"出生点大门"})
        public static final class DOOR extends ConfigurationRoot {
            @HeaderComment({"最矮的位置"})
            public static final ConfigValue<Location> MIN = ConfiguredValue.of(Location.class, new Location(Bukkit.getWorld("world"), -885, 41, -190));
            @HeaderComment({"最高的位置"})
            public static final ConfigValue<Location> MAX = ConfiguredValue.of(Location.class, new Location(Bukkit.getWorld("world"), -903, 46, -190));
            @HeaderComment({"门的材质，默认屏障"})

            public static final ConfigValue<String> MATERIAL = ConfiguredValue.of(String.class, "BARRIER");
        }

        @HeaderComment({"凋灵位置"})
        public static final ConfigValue<Location> WITHER_SPAWN = ConfiguredValue.of(Location.class, new Location(Bukkit.getWorld("world"), -909.5, 59, -157.5, 0, 0));

        @HeaderComment({"城堡"})
        public static final class CASTLE extends ConfigurationRoot {
            @HeaderComment({"最矮的位置"})
            public static final ConfigValue<Location> MIN = ConfiguredValue.of(Location.class, new Location(Bukkit.getWorld("world"), -892, 49, -167));
            @HeaderComment({"最高的位置"})
            public static final ConfigValue<Location> MAX = ConfiguredValue.of(Location.class, new Location(Bukkit.getWorld("world"), -927, 121, -113));
        }
    }

    @HeaderComment({"蓝队"})
    public static final class BLUE_TEAM extends ConfigurationRoot {
        @HeaderComment({"出生点"})
        public static final ConfigValue<Location> SPAWN = ConfiguredValue.of(Location.class, new Location(Bukkit.getWorld("world"), -719.5, 41, -257.5, 90, 0));

        @HeaderComment({"出生点大门"})
        public static final class DOOR extends ConfigurationRoot {
            @HeaderComment({"最矮的位置"})
            public static final ConfigValue<Location> MIN = ConfiguredValue.of(Location.class, new Location(Bukkit.getWorld("world"), -725, 41, -249));
            @HeaderComment({"最高的位置"})
            public static final ConfigValue<Location> MAX = ConfiguredValue.of(Location.class, new Location(Bukkit.getWorld("world"), -725, 46, -267));
            @HeaderComment({"门的材质，默认屏障"})

            public static final ConfigValue<String> MATERIAL = ConfiguredValue.of(String.class, "BARRIER");
        }

        @HeaderComment({"凋灵位置"})
        public static final ConfigValue<Location> WITHER_SPAWN = ConfiguredValue.of(Location.class, new Location(Bukkit.getWorld("world"), -756.5, 59, -273.5, 90, 0));

        @HeaderComment({"城堡"})
        public static final class CASTLE extends ConfigurationRoot {
            @HeaderComment({"最矮的位置"})
            public static final ConfigValue<Location> MIN = ConfiguredValue.of(Location.class, new Location(Bukkit.getWorld("world"), -748, 49, -256));
            @HeaderComment({"最高的位置"})
            public static final ConfigValue<Location> MAX = ConfiguredValue.of(Location.class, new Location(Bukkit.getWorld("world"), -802, 121, -291));
        }
    }
}
