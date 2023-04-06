package cn.mcarl.miars.skypvp.manager;

import cc.carm.lib.configuration.core.source.ConfigurationProvider;
import cc.carm.lib.mineconfiguration.bukkit.MineConfiguration;
import cn.mcarl.miars.skypvp.conf.LuckyConfig;
import cn.mcarl.miars.skypvp.conf.PluginConfig;

import java.io.File;

public class ConfigManager {

    private final ConfigurationProvider<?> configProvider;
    private final ConfigurationProvider<?> luckyConfigProvider;

    public ConfigManager(File dataFolder) {
        this.configProvider = MineConfiguration.from(new File(dataFolder, "config.yml"));
        this.configProvider.initialize(PluginConfig.class);


        this.luckyConfigProvider = MineConfiguration.from(new File(dataFolder, "lucky.yml"));
        this.luckyConfigProvider.initialize(LuckyConfig.class);
    }

    public ConfigurationProvider<?> getConfigProvider() {
        return configProvider;
    }


    public ConfigurationProvider<?> getLuckyConfigProvider() {
        return luckyConfigProvider;
    }

    public void reload() {
        try {
            getConfigProvider().reload();
            getLuckyConfigProvider().reload();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void save() {
        try {
            getConfigProvider().save();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}