package io.github.Leonardo0013YT.UltraSkyWars.managers;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.enums.TopType;
import io.github.Leonardo0013YT.UltraSkyWars.tops.Top;
import io.github.Leonardo0013YT.UltraSkyWars.tops.TopPlayer;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TopManager {

    private HashMap<TopType, Top> tops = new HashMap<>();
    private ArrayList<Location> holograms = new ArrayList<>();

    public void addTop(TopType type, List<String> tops) {
        this.tops.put(type, new Top(type, tops));
    }

    public void createTops() {
        UltraSkyWars plugin = UltraSkyWars.get();
        if (!plugin.getAdm().hasHologramPlugin()) {
            return;
        }
        remove();
        holograms.clear();
        if (plugin.getTopCoins() != null) {
            List<String> l = plugin.getLang().getList("holograms.tops.coins");
            ArrayList<String> lines = new ArrayList<>();
            Top top = tops.get(TopType.COINS);
            setLines(plugin, l, lines, top);
            plugin.getAdm().createHologram(plugin.getTopCoins().clone(), lines);
            holograms.add(plugin.getTopCoins().clone());
        }
        if (plugin.getTopKills() != null) {
            List<String> l = plugin.getLang().getList("holograms.tops.kills");
            ArrayList<String> lines = new ArrayList<>();
            Top top = tops.get(TopType.KILLS);
            setLines(plugin, l, lines, top);
            plugin.getAdm().createHologram(plugin.getTopKills().clone(), lines);
            holograms.add(plugin.getTopKills().clone());
        }
        if (plugin.getTopWins() != null) {
            List<String> l = plugin.getLang().getList("holograms.tops.wins");
            ArrayList<String> lines = new ArrayList<>();
            Top top = tops.get(TopType.WINS);
            setLines(plugin, l, lines, top);
            plugin.getAdm().createHologram(plugin.getTopWins().clone(), lines);
            holograms.add(plugin.getTopWins().clone());
        }
        if (plugin.getTopDeaths() != null) {
            List<String> l = plugin.getLang().getList("holograms.tops.deaths");
            ArrayList<String> lines = new ArrayList<>();
            Top top = tops.get(TopType.DEATHS);
            setLines(plugin, l, lines, top);
            plugin.getAdm().createHologram(plugin.getTopDeaths().clone(), lines);
            holograms.add(plugin.getTopDeaths().clone());
        }
        if (plugin.getTopElo() != null) {
            List<String> l = plugin.getLang().getList("holograms.tops.elo");
            ArrayList<String> lines = new ArrayList<>();
            Top top = tops.get(TopType.ELO);
            if (top == null) return;
            for (String s : l) {
                if (s.equals("<top>")) {
                    for (int i : top.getTops().keySet()) {
                        TopPlayer tp = top.getTops().get(i);
                        String[] rank = new String[]{"§7", "§7"};
                        if (plugin.getIjm().isEloRankInjection()) {
                            rank = plugin.getIjm().getEloRank().getErm().getEloRankBetween(tp.getAmount()).split(":");
                        }
                        lines.add(plugin.getLang().get(null, "rankedTopFormat").replaceAll("<rank>", rank[1]).replaceAll("<color>", rank[0]).replaceAll("<amount>", "" + tp.getAmount()).replaceAll("<name>", tp.getName()).replaceAll("<top>", "" + tp.getPosition()));
                    }
                } else {
                    lines.add(s.replaceAll("&", "§"));
                }
            }
            plugin.getAdm().createHologram(plugin.getTopElo().clone(), lines);
            holograms.add(plugin.getTopElo().clone());
        }
    }

    private void setLines(UltraSkyWars plugin, List<String> l, ArrayList<String> lines, Top top) {
        for (String s : l) {
            if (s.equals("<top>")) {
                for (int i : top.getTops().keySet()) {
                    TopPlayer tp = top.getTops().get(i);
                    lines.add(plugin.getLang().get(null, "topFormat").replaceAll("<amount>", "" + tp.getAmount()).replaceAll("<name>", tp.getName()).replaceAll("<top>", "" + tp.getPosition()));
                }
            } else {
                lines.add(s.replaceAll("&", "§"));
            }
        }
    }

    public void remove() {
        UltraSkyWars plugin = UltraSkyWars.get();
        for (Location l : holograms) {
            if (plugin.getAdm().hasHologram(l)) {
                plugin.getAdm().deleteHologram(l);
            }
        }
    }

}