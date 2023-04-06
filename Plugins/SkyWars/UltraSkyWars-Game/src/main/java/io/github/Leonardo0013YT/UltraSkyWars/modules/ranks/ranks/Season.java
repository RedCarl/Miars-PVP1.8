package io.github.Leonardo0013YT.UltraSkyWars.modules.ranks.ranks;

import io.github.Leonardo0013YT.UltraSkyWars.modules.ranks.InjectionEloRank;

import java.util.HashMap;

public class Season {

    private int season;
    private HashMap<Integer, SeasonDivision> divisions = new HashMap<>();

    public Season(InjectionEloRank ier, String path, int season) {
        this.season = season;
        int actSeason = ier.getRankeds().getInt("data.season");
        if (ier.getRankeds().isSet(path + "divisions")) {
            for (String d : ier.getRankeds().getConfig().getConfigurationSection(path + "divisions").getKeys(false)) {
                int order = ier.getRankeds().getInt("divisions." + d + ".order");
                divisions.put(order, new SeasonDivision(ier, path + "divisions." + d, season, actSeason > season));
            }
        }
    }

    public void execute() {

    }

    public int getSeason() {
        return season;
    }

    public HashMap<Integer, SeasonDivision> getDivisions() {
        return divisions;
    }
}