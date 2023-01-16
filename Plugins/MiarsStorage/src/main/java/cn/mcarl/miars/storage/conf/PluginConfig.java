package cn.mcarl.miars.storage.conf;

import cc.carm.lib.configuration.core.ConfigurationRoot;
import cc.carm.lib.configuration.core.annotation.HeaderComment;
import cc.carm.lib.configuration.core.value.ConfigValue;
import cc.carm.lib.configuration.core.value.type.ConfiguredValue;

public class PluginConfig extends ConfigurationRoot {

    @HeaderComment({"数据库连接信息"})
    public static final class DATABASE extends ConfigurationRoot {

        @HeaderComment({"驱动器"})
        public static final ConfigValue<String> DRIVER_NAME = ConfiguredValue.of(String.class, "com.mysql.jdbc.Driver");

        @HeaderComment({"地址"})
        public static final ConfigValue<String> URL = ConfiguredValue.of(String.class, "jdbc:mysql://127.0.0.1:3306/minecraft");

        @HeaderComment({"账户"})
        public static final ConfigValue<String> USERNAME = ConfiguredValue.of(String.class, "root");

        @HeaderComment({"密码"})
        public static final ConfigValue<String> PASSWORD = ConfiguredValue.of(String.class, "root");

        @HeaderComment({"表名称"})
        public static final ConfigValue<String> TABLE_NAME = ConfiguredValue.of(String.class, "ud_data");

    }
    @HeaderComment({"Redis连接信息"})
    public static final class REDIS extends ConfigurationRoot {

        @HeaderComment({"地址"})
        public static final ConfigValue<String> URL = ConfiguredValue.of(String.class, "127.0.0.1:6379");

        @HeaderComment({"密码"})
        public static final ConfigValue<String> PASSWORD = ConfiguredValue.of(String.class, "630669");

    }
}
