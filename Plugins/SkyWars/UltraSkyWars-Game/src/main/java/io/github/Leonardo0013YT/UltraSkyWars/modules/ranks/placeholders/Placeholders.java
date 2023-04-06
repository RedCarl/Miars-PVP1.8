package io.github.Leonardo0013YT.UltraSkyWars.modules.ranks.placeholders;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.data.SWPlayer;
import io.github.Leonardo0013YT.UltraSkyWars.modules.ranks.InjectionEloRank;
import io.github.Leonardo0013YT.UltraSkyWars.modules.ranks.ranks.EloRank;
import io.github.Leonardo0013YT.UltraSkyWars.utils.Utils;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class Placeholders extends PlaceholderExpansion {

    private InjectionEloRank plugin;

    public Placeholders(InjectionEloRank plugin) {
        this.plugin = plugin;
    }

    public String getIdentifier() {
        return "uswranked";
    }

    public String getAuthor() {
        return "Leonardo0013YT";
    }

    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player p, String id) {
        SWPlayer sw = UltraSkyWars.get().getDb().getSWPlayer(p);
        if (sw == null) {
            return "";
        }
        if (id.equals("progress")) {
            EloRank er = plugin.getErm().getEloRank(p);
            int elo = sw.getElo() - er.getMin();
            int max = er.getMax() - er.getMin();
            return Utils.getProgressBar(elo, max, UltraSkyWars.get().getCm().getProgressBarAmount());
        }
        if (id.equals("rank")) {
            return plugin.getErm().getEloRankChat(p);
        }
        return null;
    }

}