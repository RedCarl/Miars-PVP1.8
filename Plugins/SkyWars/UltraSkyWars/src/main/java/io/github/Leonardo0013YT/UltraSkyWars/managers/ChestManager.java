package io.github.Leonardo0013YT.UltraSkyWars.managers;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.chests.ChestType;
import lombok.Getter;

import java.util.HashMap;

public class ChestManager {

    @Getter
    private final HashMap<String, ChestType> chests = new HashMap<>();
    @Getter
    private String defaultChest;

    public ChestManager() {
        reload();
    }

    public void reload() {
        UltraSkyWars plugin = UltraSkyWars.get();
        if (!plugin.getChestType().isSet("types")) {
            return;
        }
        defaultChest = plugin.getChestType().getOrDefault("defaultType", "NORMAL");
        for (String s : plugin.getChestType().getConfig().getConfigurationSection("types").getKeys(false)) {
            chests.put(s.toUpperCase(), new ChestType(plugin, s));
        }
        if (!chests.containsKey(defaultChest)) {
            defaultChest = chests.keySet().stream().findFirst().orElse("NORMAL");
        }
    }

}