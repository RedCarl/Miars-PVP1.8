package cn.mcarl.miars.core.conf;

import cc.carm.lib.configuration.core.ConfigurationRoot;
import cc.carm.lib.configuration.core.annotation.HeaderComment;
import cc.carm.lib.configuration.core.value.ConfigValue;
import cc.carm.lib.configuration.core.value.type.ConfiguredValue;

public class PluginConfig extends ConfigurationRoot {

    @HeaderComment({"本服务器的信息,用于注册Bungee"})
    public static final class SERVER_INFO extends ConfigurationRoot {

        @HeaderComment({"服务器名称"})
        public static final ConfigValue<String> NAME = ConfiguredValue.of(String.class, "server name");
        @HeaderComment({"代理URL"})
        public static final ConfigValue<String> URL = ConfiguredValue.of(String.class, "proxy url");
    }
    @HeaderComment({"插件配置文件"})
    public static final class SITE extends ConfigurationRoot {

        @HeaderComment({"禁止爆炸破坏方块"})
        public static final ConfigValue<Boolean> DISABLED_EXPLODE_DESTROY = ConfiguredValue.of(Boolean.class, true);

        @HeaderComment({"禁止天气更改"})
        public static final ConfigValue<Boolean> DISABLED_WEATHER_CHANGE = ConfiguredValue.of(Boolean.class, true);

        @HeaderComment({"禁止时间变化"})
        public static final ConfigValue<Boolean> DISABLED_TIME_CHANGE = ConfiguredValue.of(Boolean.class, true);
    }



}
