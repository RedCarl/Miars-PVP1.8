package cn.mcarl.bungee.auth.conf;

import cc.carm.lib.configuration.core.ConfigurationRoot;
import cc.carm.lib.configuration.core.annotation.HeaderComment;
import cc.carm.lib.configuration.core.value.ConfigValue;
import cc.carm.lib.configuration.core.value.type.ConfiguredValue;

public class PluginConfig extends ConfigurationRoot {

    @HeaderComment({"用户验证系统配置"})
    public static final class SERVER_INFO extends ConfigurationRoot{

        @HeaderComment({"代理端口"})
        public static final ConfigValue<Integer> PORT = ConfiguredValue.of(Integer.class, 6235);
    }
}
