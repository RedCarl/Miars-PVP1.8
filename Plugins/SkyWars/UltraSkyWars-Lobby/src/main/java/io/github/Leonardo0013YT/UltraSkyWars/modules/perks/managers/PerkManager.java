package io.github.Leonardo0013YT.UltraSkyWars.modules.perks.managers;

import io.github.Leonardo0013YT.UltraSkyWars.enums.PerkType;
import io.github.Leonardo0013YT.UltraSkyWars.modules.perks.InjectionPerks;
import io.github.Leonardo0013YT.UltraSkyWars.modules.perks.perks.NormalPerk;
import io.github.Leonardo0013YT.UltraSkyWars.modules.perks.perks.PotionPerk;
import io.github.Leonardo0013YT.UltraSkyWars.superclass.Perk;

import java.util.HashMap;
import java.util.Map;

public class PerkManager {

    private final InjectionPerks plugin;
    private final HashMap<PerkType, Perk> perks = new HashMap<>();
    private final HashMap<Integer, PerkType> perksID = new HashMap<>();

    public PerkManager(InjectionPerks plugin) {
        this.plugin = plugin;
        loadPerks();
    }

    public void loadPerks() {
        perks.clear();
        perks.put(PerkType.FALL_REDUCTION, new NormalPerk(plugin, "perks.fallreduction"));
        perks.put(PerkType.FIRE_DAMAGE_REDUCTION, new NormalPerk(plugin, "perks.firedamagereduction"));
        perks.put(PerkType.SHOT_DAMAGE_REDUCTION, new NormalPerk(plugin, "perks.shotdamagereduction"));
        perks.put(PerkType.ANNO_O_MITE, new NormalPerk(plugin, "perks.anno_o_mite"));
        perks.put(PerkType.ARROW_RECOVERY, new NormalPerk(plugin, "perks.arrowrecovery"));
        perks.put(PerkType.BLAZING_ARROWS, new NormalPerk(plugin, "perks.blazingarrows"));
        perks.put(PerkType.BULLDOZER, new PotionPerk(plugin, "perks.bulldozer", 5, 0));
        perks.put(PerkType.ENDER_MASTERY, new NormalPerk(plugin, "perks.endermastery"));
        perks.put(PerkType.JUGGERNAUT, new PotionPerk(plugin, "perks.juggernaut", 4, 0));
        perks.put(PerkType.KNOWLEDGE, new NormalPerk(plugin, "perks.knowledge"));
        perks.put(PerkType.MINING_EXPERTISE, new NormalPerk(plugin, "perks.miningexpertise"));
        perks.put(PerkType.SPEED_START, new PotionPerk(plugin, "perks.speedstart", 10, 0));
        perks.put(PerkType.RESISTENCE_START, new PotionPerk(plugin, "perks.resistencestart", 20, 0));
        perks.put(PerkType.NOURISHMENT, new NormalPerk(plugin, "perks.nourishment"));
        perks.put(PerkType.SAVIOR, new PotionPerk(plugin, "perks.savior", 4, 0));
        for (Map.Entry<PerkType, Perk> entry : perks.entrySet()) {
            perksID.put(entry.getValue().getId(), entry.getKey());
        }
    }

    public Perk getPerkByID(int id) {
        return perks.get(perksID.get(id));
    }

    public Perk getPerk(PerkType type) {
        return perks.get(type);
    }

    public Perk getPerkByName(String name) {
        for (Perk perk : perks.values()) {
            if (perk.getName().equals(name)) {
                return perk;
            }
        }
        return null;
    }

    public HashMap<PerkType, Perk> getPerks() {
        return perks;
    }
}