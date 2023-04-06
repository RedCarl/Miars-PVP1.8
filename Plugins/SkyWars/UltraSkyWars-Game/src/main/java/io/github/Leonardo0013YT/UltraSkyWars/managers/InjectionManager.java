package io.github.Leonardo0013YT.UltraSkyWars.managers;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.modules.SpecialItems.InjectionSpecialItems;
import io.github.Leonardo0013YT.UltraSkyWars.modules.challenges.InjectionChallenges;
import io.github.Leonardo0013YT.UltraSkyWars.modules.cubelets.InjectionCubelets;
import io.github.Leonardo0013YT.UltraSkyWars.modules.mobfriends.InjectionMobFriends;
import io.github.Leonardo0013YT.UltraSkyWars.modules.parties.InjectionParty;
import io.github.Leonardo0013YT.UltraSkyWars.modules.perks.InjectionPerks;
import io.github.Leonardo0013YT.UltraSkyWars.modules.pwt.InjectionPWT;
import io.github.Leonardo0013YT.UltraSkyWars.modules.ranks.InjectionEloRank;
import io.github.Leonardo0013YT.UltraSkyWars.modules.soulwell.InjectionSoulWell;
import org.bukkit.Bukkit;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
public class InjectionManager {

    private UltraSkyWars plugin;
    private InjectionEloRank eloRank;
    private InjectionPerks perks;
    private InjectionCubelets cubelets;
    private InjectionMobFriends mobFriends;
    private InjectionPWT pwts;
    private InjectionSoulWell soulwell;
    private InjectionChallenges challenges;
    private InjectionParty party;
    private InjectionSpecialItems specialItems;

    public InjectionManager(UltraSkyWars plugin) {
        this.plugin = plugin;
    }

    public void loadInjections() {
        specialItems = new InjectionSpecialItems();
        specialItems.loadInjection(plugin);

        soulwell = new InjectionSoulWell();
        soulwell.loadInjection(plugin);

        party = new InjectionParty();
        party.loadInjection(plugin);

        challenges = new InjectionChallenges();
        challenges.loadInjection(plugin);

        eloRank = new InjectionEloRank();
        eloRank.loadInjection(plugin);

        perks = new InjectionPerks();
        perks.loadInjection(plugin);

        cubelets = new InjectionCubelets();
        cubelets.loadInjection(plugin);

        mobFriends = new InjectionMobFriends();
        mobFriends.loadInjection(plugin);

        pwts = new InjectionPWT();
        pwts.loadInjection(plugin);

        plugin.getCm().reloadInjections();
    }

    public void reload() {
        specialItems = new InjectionSpecialItems();
        specialItems.loadInjection(plugin);

        soulwell = new InjectionSoulWell();
        soulwell.loadInjection(plugin);

        party = new InjectionParty();
        party.loadInjection(plugin);

        challenges = new InjectionChallenges();
        challenges.loadInjection(plugin);

        eloRank = new InjectionEloRank();
        eloRank.loadInjection(plugin);

        perks = new InjectionPerks();
        perks.loadInjection(plugin);

        cubelets = new InjectionCubelets();
        cubelets.loadInjection(plugin);

        mobFriends = new InjectionMobFriends();
        mobFriends.loadInjection(plugin);

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

    public boolean isChallenges() {
        return challenges != null;
    }

    public boolean isParty() {
        return party != null;
    }

    public InjectionChallenges getChallenges() {
        return challenges;
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