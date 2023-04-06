package io.github.Leonardo0013YT.UltraSkyWars.controllers;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;

import java.util.ArrayList;
import java.util.List;

public class ChestController {

    private UltraSkyWars plugin;
    private List<Integer> randomLoc = new ArrayList<>();

    public ChestController(UltraSkyWars plugin) {
        this.plugin = plugin;
        for (int i = 0; i < 27; i++) {
            randomLoc.add(i);
        }
    }

    public List<Integer> getRandomLoc() {
        return randomLoc;
    }
}