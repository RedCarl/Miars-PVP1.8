package cn.mcarl.miars.practiceffa.manager;

import cc.carm.lib.configuration.core.source.ConfigurationProvider;
import cc.carm.lib.mineconfiguration.bukkit.MineConfiguration;
import cn.mcarl.miars.practiceffa.conf.PluginConfig;

import java.io.File;

public class ConfigManager {

    private final ConfigurationProvider<?> configProvider;

    public ConfigManager(File dataFolder) {
        this.configProvider = MineConfiguration.from(new File(dataFolder, "config.yml"));
        this.configProvider.initialize(PluginConfig.class);
    }

    public ConfigurationProvider<?> getConfigProvider() {
        return configProvider;
    }

    public void reload() throws Exception {
        getConfigProvider().reload();
    }

}