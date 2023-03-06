package cn.mcarl.miars.skypvp.manager;

import cc.carm.lib.configuration.core.source.ConfigurationProvider;
import cc.carm.lib.mineconfiguration.bukkit.MineConfiguration;
import cn.mcarl.miars.skypvp.conf.PluginConfig;

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

    public void reload() {
        try {
            getConfigProvider().reload();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}