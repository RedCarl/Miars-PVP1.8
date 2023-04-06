package io.github.Leonardo0013YT.UltraSkyWars.modules.ranks;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.config.Settings;
import io.github.Leonardo0013YT.UltraSkyWars.interfaces.Injection;
import io.github.Leonardo0013YT.UltraSkyWars.modules.ranks.listeners.EloListener;
import io.github.Leonardo0013YT.UltraSkyWars.modules.ranks.managers.EloRankManager;
import io.github.Leonardo0013YT.UltraSkyWars.modules.ranks.managers.SeasonManager;
import io.github.Leonardo0013YT.UltraSkyWars.modules.ranks.menus.RankedMenu;
import io.github.Leonardo0013YT.UltraSkyWars.modules.ranks.placeholders.Placeholders;
import org.bukkit.Bukkit;

public class InjectionEloRank implements Injection {

    private Injection instance;
    private EloRankManager erm;
    private SeasonManager sm;
    private Settings eloRank, rankeds;
    private RankedMenu rem;

    @Override
    public void loadInjection(UltraSkyWars main) {
        instance = this;
        eloRank = new Settings("modules/eloRank", false, false);
        rankeds = new Settings("modules/rankeds", false, false);
        erm = new EloRankManager(main, this);
        sm = new SeasonManager(this);
        rem = new RankedMenu(this);
        Bukkit.getServer().getPluginManager().registerEvents(new EloListener(main, this), main);
        Bukkit.getScheduler().runTaskTimerAsynchronously(main, () -> sm.reduce(), 20 * 5, 20 * 5);
    }

    @Override
    public void reload() {
        eloRank.reload();
        erm.reload();
    }

    @Override
    public void disable() {

    }

    public Settings getRankeds() {
        return rankeds;
    }

    public Settings getEloRank() {
        return eloRank;
    }

    public RankedMenu getRem() {
        return rem;
    }

    public SeasonManager getSm() {
        return sm;
    }

    public EloRankManager getErm() {
        return erm;
    }

    public Injection getInstance() {
        return instance;
    }

    public void loadPlaceholders() {
        new Placeholders(this).register();
    }


}