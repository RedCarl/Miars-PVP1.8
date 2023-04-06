package io.github.Leonardo0013YT.UltraSkyWars.managers;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.modules.cubelets.InjectionCubelets;
import io.github.Leonardo0013YT.UltraSkyWars.modules.lprotection.InjectionLProtection;
import io.github.Leonardo0013YT.UltraSkyWars.modules.parties.InjectionParty;
import io.github.Leonardo0013YT.UltraSkyWars.modules.perks.InjectionPerks;
import io.github.Leonardo0013YT.UltraSkyWars.modules.pwt.InjectionPWT;
import io.github.Leonardo0013YT.UltraSkyWars.modules.ranks.InjectionEloRank;
import io.github.Leonardo0013YT.UltraSkyWars.modules.signs.InjectionSigns;
import io.github.Leonardo0013YT.UltraSkyWars.modules.soulwell.InjectionSoulWell;

import java.io.File;

public class InjectionManager {

    private UltraSkyWars plugin;
    private InjectionEloRank eloRank;
    private InjectionPerks perks;
    private InjectionCubelets cubelets;
    private InjectionSigns signs;
    private InjectionPWT pwts;
    private InjectionLProtection lProtection;
    private InjectionParty party;
    private InjectionSoulWell soulwell;

    public InjectionManager(UltraSkyWars plugin) {
        this.plugin = plugin;
    }

    public void loadInjections() {

        soulwell = new InjectionSoulWell();
        soulwell.loadInjection(plugin);

        party = new InjectionParty();
        party.loadInjection(plugin);

        lProtection = new InjectionLProtection();
        lProtection.loadInjection(plugin);

        eloRank = new InjectionEloRank();
        eloRank.loadInjection(plugin);
        if (plugin.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            eloRank.loadPlaceholders();
        }

        perks = new InjectionPerks();
        perks.loadInjection(plugin);

        cubelets = new InjectionCubelets();
        cubelets.loadInjection(plugin);

        signs = new InjectionSigns();
        signs.loadInjection(plugin);

        pwts = new InjectionPWT();
        pwts.loadInjection(plugin);

        plugin.getCm().reloadInjections();
    }

    public void reload() {
        soulwell = new InjectionSoulWell();
        soulwell.loadInjection(plugin);

        party = new InjectionParty();
        party.loadInjection(plugin);

        lProtection = new InjectionLProtection();
        lProtection.loadInjection(plugin);

        eloRank = new InjectionEloRank();
        eloRank.loadInjection(plugin);
        if (plugin.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            eloRank.loadPlaceholders();
        }

        perks = new InjectionPerks();
        perks.loadInjection(plugin);

        cubelets = new InjectionCubelets();
        cubelets.loadInjection(plugin);

        signs = new InjectionSigns();
        signs.loadInjection(plugin);

        pwts = new InjectionPWT();
        pwts.loadInjection(plugin);

        plugin.getCm().reloadInjections();
    }

    public boolean isSoulWellInjection() {
        return soulwell != null;
    }

    public boolean isEloRankInjection() {
        return eloRank != null;
    }

    public boolean isPerksInjection() {
        return perks != null;
    }

    public boolean isCubeletsInjection() {
        return cubelets != null;
    }

    public boolean isSignsInjection() {
        return signs != null;
    }

    public boolean isParty() {
        return party != null;
    }

    public InjectionSigns getSigns() {
        return signs;
    }

    public InjectionCubelets getCubelets() {
        return cubelets;
    }

    public InjectionEloRank getEloRank() {
        return eloRank;
    }

    public InjectionPerks getPerks() {
        return perks;
    }

    public InjectionParty getParty() {
        return party;
    }

    public InjectionSoulWell getSoulwell() {
        return soulwell;
    }
}