package io.github.Leonardo0013YT.UltraSkyWars.modules.soulwell.managers;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.modules.soulwell.InjectionSoulWell;
import io.github.Leonardo0013YT.UltraSkyWars.modules.soulwell.soulwell.SoulWell;
import io.github.Leonardo0013YT.UltraSkyWars.modules.soulwell.soulwell.SoulWellRow;
import io.github.Leonardo0013YT.UltraSkyWars.modules.soulwell.soulwell.SoulWellSession;
import io.github.Leonardo0013YT.UltraSkyWars.modules.soulwell.soulwell.SoulWellShop;
import io.github.Leonardo0013YT.UltraSkyWars.modules.soulwell.soulwell.upgrades.SoulWellAngelOfDeath;
import io.github.Leonardo0013YT.UltraSkyWars.modules.soulwell.soulwell.upgrades.SoulWellUpgrade;
import io.github.Leonardo0013YT.UltraSkyWars.utils.ItemBuilder;
import io.github.Leonardo0013YT.UltraSkyWars.utils.Utils;
import io.github.Leonardo0013YT.UltraSkyWars.xseries.XMaterial;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Getter
public class SoulWellManager {

    private Map<Location, SoulWell> soulWells = new HashMap<>();
    private Map<UUID, SoulWellSession> sessions = new HashMap<>();
    private String[] colors = new String[]{"BLUE_", "CYAN_", "GREEN_", "LIGHT_BLUE_", "LIME_", "MAGENTA_", "ORANGE_", "PINK_", "PURPLE_", "RED_", "YELLOW_"};
    private Map<Integer, SoulWellRow> rows = new HashMap<>();
    private Map<String, SoulWellShop> shops = new HashMap<>();
    private Map<String, SoulWellUpgrade> extra = new HashMap<>();
    private Map<String, SoulWellUpgrade> max = new HashMap<>();
    private Map<String, SoulWellAngelOfDeath> angel = new HashMap<>();
    private Map<Integer, String> extraLevel = new HashMap<>(), maxLevel = new HashMap<>(), angelLevel = new HashMap<>();
    private ArrayList<String> rarities = new ArrayList<>();
    private UltraSkyWars plugin;
    private InjectionSoulWell is;

    public SoulWellManager(UltraSkyWars plugin, InjectionSoulWell is) {
        this.plugin = plugin;
        this.is = is;
        for (int i = 1; i < 6; i++) {
            rows.put(i, new SoulWellRow(i));
        }
        rarities.add("eww");
        rarities.add("disgusting");
        rarities.add("bah");
        rarities.add("decent");
        rarities.add("salty");
        rarities.add("tasty");
        rarities.add("succulent");
        rarities.add("candy");
        rarities.add("divine");
        rarities.add("heavenly");
        loadSoulWells();
    }

    public void loadSoulWells() {
        soulWells.clear();
        if (plugin.getConfig().isSet("soulwells")) {
            ConfigurationSection soul = plugin.getConfig().getConfigurationSection("soulwells");
            for (String s : soul.getKeys(false)) {
                Location loc = Utils.getStringLocation(plugin.getConfig().getString("soulwells." + s + ".loc"));
                if (loc == null) continue;
                soulWells.put(loc, new SoulWell(plugin, loc));
            }
        }
        if (is.getSoulwell().isSet("shop")) {
            ConfigurationSection soul = is.getSoulwell().getConfig().getConfigurationSection("shop");
            for (String s : soul.getKeys(false)) {
                shops.put(s, new SoulWellShop(is, "shop." + s, s));
            }
        }
        if (is.getSoulwell().isSet("upgrades.extra")) {
            ConfigurationSection soul = is.getSoulwell().getConfig().getConfigurationSection("upgrades.extra");
            for (String s : soul.getKeys(false)) {
                SoulWellUpgrade swu = new SoulWellUpgrade(is, "upgrades.extra." + s, "extra", s);
                extra.put(s, swu);
                extraLevel.put(swu.getLevel(), s);
            }
        }
        if (is.getSoulwell().isSet("upgrades.max")) {
            ConfigurationSection soul = is.getSoulwell().getConfig().getConfigurationSection("upgrades.max");
            for (String s : soul.getKeys(false)) {
                SoulWellUpgrade swu = new SoulWellUpgrade(is, "upgrades.max." + s, "max", s);
                max.put(s, swu);
                maxLevel.put(swu.getLevel(), s);
            }
        }
        if (is.getSoulwell().isSet("angelofdeath")) {
            ConfigurationSection soul = is.getSoulwell().getConfig().getConfigurationSection("angelofdeath");
            for (String s : soul.getKeys(false)) {
                SoulWellAngelOfDeath a = new SoulWellAngelOfDeath(is, "angelofdeath." + s, s);
                angel.put(s, a);
                angelLevel.put(a.getLevel(), s);
            }
        }
    }

    public String getRandomRarity() {
        return rarities.get(ThreadLocalRandom.current().nextInt(0, rarities.size()));
    }

    public SoulWellUpgrade getExtraByLevel(int level) {
        return extra.get(extraLevel.get(level));
    }

    public SoulWellUpgrade getMaxByLevel(int level) {
        return max.get(maxLevel.get(level));
    }

    public SoulWellAngelOfDeath getAngelByLevel(int level) {
        return angel.get(angelLevel.get(level));
    }

    public int getMaxSouls(int level) {
        SoulWellUpgrade swu;
        if (level == 0) {
            swu = max.get(maxLevel.get(1));
        } else {
            swu = max.get(maxLevel.get(level));
        }
        if (swu != null) {
            return swu.getAmount();
        } else {
            return 99999;
        }
    }

    public SoulWellUpgrade getExtraMax(int level) {
        for (SoulWellUpgrade swu : extra.values()) {
            if (swu.getLevel() - 1 == level) return swu;
        }
        return null;
    }

    public SoulWellUpgrade getMaxMax(int level) {
        for (SoulWellUpgrade swu : max.values()) {
            if (swu.getLevel() - 1 == level) return swu;
        }
        return null;
    }

    public void reload() {
        soulWells.values().forEach(SoulWell::reload);
    }

    public void addSession(Player p, SoulWell sw) {
        sessions.put(p.getUniqueId(), new SoulWellSession(plugin, is, p, sw.getLoc()));
    }

    public boolean isSession(Player p) {
        return sessions.containsKey(p.getUniqueId());
    }

    public void removeSession(Player p) {
        if (sessions.containsKey(p.getUniqueId())) {
            sessions.get(p.getUniqueId()).cancel(p);
        }
        sessions.remove(p.getUniqueId());
    }

    public SoulWellSession getSession(Player p) {
        return sessions.get(p.getUniqueId());
    }

    public ItemStack getRandomGlass() {
        return ItemBuilder.item(XMaterial.valueOf(colors[ThreadLocalRandom.current().nextInt(0, 11)] + "STAINED_GLASS_PANE"), 1, "§7", "§7");
    }
}