package cn.mcarl.miars.megawalls.conf;

import cc.carm.lib.configuration.core.ConfigurationRoot;
import cc.carm.lib.configuration.core.annotation.HeaderComment;
import cc.carm.lib.configuration.core.value.ConfigValue;
import cc.carm.lib.configuration.core.value.type.ConfiguredValue;

public class PluginConfig extends ConfigurationRoot {


    @HeaderComment({"当前服务器模式 LOBBY GAME"})
    public static final ConfigValue<String> MODE = ConfiguredValue.of(String.class, "GAME");
    @HeaderComment({"当前房间名称，大厅就填大厅"})
    public static final ConfigValue<String> NAME = ConfiguredValue.of(String.class, "超级简陋的房间");
    @HeaderComment({"单个队伍最大人数限制"})
    public static final ConfigValue<Integer> TEAM_LIMIT = ConfiguredValue.of(Integer.class, 25);
    @HeaderComment({"多少人后准备开始游戏"})
    public static final ConfigValue<Integer> READY_PLAYERS = ConfiguredValue.of(Integer.class, 25);
}
