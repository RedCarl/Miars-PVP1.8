package io.github.Leonardo0013YT.UltraSkyWars.controllers;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.WorldEditUtils_Old;
import io.github.Leonardo0013YT.UltraSkyWars.interfaces.WorldEdit;

public class WorldController {

    private WorldEdit edit;

    public WorldController(UltraSkyWars plugin) {
        edit = new WorldEditUtils_Old(plugin);
    }

    public WorldEdit getEdit() {
        return edit;
    }
}