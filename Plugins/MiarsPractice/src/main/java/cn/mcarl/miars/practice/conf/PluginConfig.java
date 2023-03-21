package cn.mcarl.miars.practice.conf;

import cc.carm.lib.configuration.core.ConfigurationRoot;
import cc.carm.lib.configuration.core.annotation.HeaderComment;
import cc.carm.lib.configuration.core.value.ConfigValue;
import cc.carm.lib.configuration.core.value.type.ConfiguredValue;
import cn.mcarl.miars.storage.enums.practice.FKitType;

public class PluginConfig extends ConfigurationRoot {

    @HeaderComment({"竞技场配置"})
    public static final class PRACTICE_SITE extends ConfigurationRoot {
        @HeaderComment({"模式"})
        public static final ConfigValue<String> MODE = ConfiguredValue.of(String.class, FKitType.NO_DEBUFF.name());
    }

}
