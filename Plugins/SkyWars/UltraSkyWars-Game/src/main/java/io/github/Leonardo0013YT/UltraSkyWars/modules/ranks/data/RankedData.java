package io.github.Leonardo0013YT.UltraSkyWars.modules.ranks.data;

import java.util.HashMap;

public class RankedData {

    public HashMap<String, Integer> ranked = new HashMap<>();

    public HashMap<String, Integer> getRanked() {
        return ranked;
    }

    public void setRanked(HashMap<String, Integer> ranked) {
        this.ranked = ranked;
    }
}