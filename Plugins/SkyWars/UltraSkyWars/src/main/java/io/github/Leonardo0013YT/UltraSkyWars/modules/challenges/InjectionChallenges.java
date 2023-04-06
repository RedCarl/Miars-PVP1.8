package io.github.Leonardo0013YT.UltraSkyWars.modules.challenges;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.config.Settings;
import io.github.Leonardo0013YT.UltraSkyWars.interfaces.Injection;
import io.github.Leonardo0013YT.UltraSkyWars.modules.challenges.enums.ChallengeType;
import io.github.Leonardo0013YT.UltraSkyWars.modules.challenges.listeners.ArmorListener;
import io.github.Leonardo0013YT.UltraSkyWars.modules.challenges.listeners.ChallengeListener;
import io.github.Leonardo0013YT.UltraSkyWars.modules.challenges.menus.ChallengeMenu;
import org.bukkit.Material;

public class InjectionChallenges implements Injection {

    private ChallengeMenu chm;
    private Settings challenges;

    @Override
    public void loadInjection(UltraSkyWars main) {
        this.chm = new ChallengeMenu(main);
        this.challenges = new Settings("modules/challenges", true, false);
        main.getServer().getPluginManager().registerEvents(new ChallengeListener(main, this), main);
        main.getServer().getPluginManager().registerEvents(new ArmorListener(), main);
        for (ChallengeType type : ChallengeType.values()) {
            String path = "challenges." + type.name();
            type.setMaterial(Material.valueOf(challenges.getOrDefault(path + ".material", type.getMaterial().name())));
            type.setIconSlot(challenges.getIntOrDefault(path + ".iconSlot", type.getIconSlot()));
            type.setStatusSlot(challenges.getIntOrDefault(path + ".statusSlot", type.getStatusSlot()));
            type.setEnabled(challenges.getBoolean(path + ".enabled"));
            type.setOnWinEnabled(challenges.getBoolean(path + ".onWin.enabled"));
            type.setOnWinCommands(challenges.getList(path + ".onWin.commands"));
        }
    }

    @Override
    public void reload() {

    }

    @Override
    public void disable() {

    }

    public Settings getChallenges() {
        return challenges;
    }

    public ChallengeMenu getChm() {
        return chm;
    }
}