package com.andrei1058.bedwars.proxy.configuration;

import com.andrei1058.bedwars.proxy.BedWarsProxy;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.ArrayList;

public class SignsConfig extends PluginConfig{

    public SignsConfig() {
        super(BedWarsProxy.getPlugin(), "signs", BedWarsProxy.getPlugin().getDataFolder().getPath());

        YamlConfiguration yml = getYml();
        yml.options().copyDefaults(true);
        yml.addDefault(ConfigPath.SIGNS_LIST_PATH, new ArrayList<>());
        yml.addDefault(ConfigPath.SIGNS_SETTINGS_STATIC_SHOW_PLAYING, false);
        save();
    }
}
