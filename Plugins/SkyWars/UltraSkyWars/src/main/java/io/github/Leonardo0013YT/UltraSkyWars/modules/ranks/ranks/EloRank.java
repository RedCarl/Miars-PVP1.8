package io.github.Leonardo0013YT.UltraSkyWars.modules.ranks.ranks;

import io.github.Leonardo0013YT.UltraSkyWars.modules.ranks.InjectionEloRank;

public class EloRank {

    private int min, max, order;
    private String color;
    private String prefix;
    private String corto;
    private String name;

    public EloRank(String color, String corto, String prefix) {
        this.color = color;
        this.corto = corto;
        this.prefix = prefix;
        this.name = "Default";
        this.max = 0;
        this.min = 0;
        this.order = 0;
    }

    public EloRank(InjectionEloRank plugin, String path, String name) {
        this.name = name;
        this.min = plugin.getEloRank().getInt(path + ".min");
        this.max = plugin.getEloRank().getInt(path + ".max");
        this.order = plugin.getEloRank().getInt(path + ".order");
        this.color = plugin.getEloRank().get(null, path + ".color");
        this.prefix = plugin.getEloRank().get(null, path + ".prefix");
        this.corto = plugin.getEloRank().get(null, path + ".short");
    }

    public String getPrefix() {
        return prefix;
    }

    public String getCorto() {
        return corto;
    }

    public String getColor() {
        return color;
    }

    public int getOrder() {
        return order;
    }

    public int getMax() {
        return max;
    }

    public int getMin() {
        return min;
    }

    public String getName() {
        return name;
    }
}