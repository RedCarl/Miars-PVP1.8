package cn.mcarl.bungee.manager;

import cn.mcarl.bungee.conf.PluginConfig;
import cc.carm.lib.configuration.core.source.ConfigurationProvider;
import cc.carm.lib.easyplugin.utils.JarResourceUtils;
import cc.carm.lib.mineconfiguration.bungee.MineConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigManager {

    private final ConfigurationProvider<?> configProvider;

    public ConfigManager(File dataFolder) {
        firstInitialize(dataFolder);
        this.configProvider = MineConfiguration.from(new File(dataFolder, "config.yml"));
        this.configProvider.initialize(PluginConfig.class);
    }

    protected void firstInitialize(File dataFolder) {
        File configFile = new File(dataFolder, "config.yml");
        if (!configFile.exists()) {
            try {
                JarResourceUtils.copyFolderFromJar("en_US", dataFolder, JarResourceUtils.CopyOption.COPY_IF_NOT_EXIST);
                JarResourceUtils.copyFolderFromJar("prefixes", dataFolder, JarResourceUtils.CopyOption.COPY_IF_NOT_EXIST);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public ConfigurationProvider<?> getConfigProvider() {
        return configProvider;
    }

    public void reload() throws Exception {
        getConfigProvider().reload();
    }

}