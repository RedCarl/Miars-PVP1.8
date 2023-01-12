package cn.mcarl.bungee.conf;

import cc.carm.lib.configuration.core.ConfigurationRoot;
import cc.carm.lib.configuration.core.annotation.HeaderComment;
import cc.carm.lib.configuration.core.value.ConfigValue;
import cc.carm.lib.configuration.core.value.type.ConfiguredValue;

public class PluginConfig extends ConfigurationRoot {

    @HeaderComment({"本服务器的信息,用于注册Bungee"})
    public static final class SERVER_INFO extends ConfigurationRoot{

        @HeaderComment({"代理端口"})
        public static final ConfigValue<Integer> PORT = ConfiguredValue.of(Integer.class, 6235);
    }
}
