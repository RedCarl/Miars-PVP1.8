package io.github.Leonardo0013YT.UltraSkyWars.api;

import io.github.Leonardo0013YT.UltraSkyWars.config.Settings;

public class UltraSkyWarsInjectionAPI {

    public static Settings createNewFile(String name, boolean defaults, boolean comments) {
        return new Settings(name, defaults);
    }

}