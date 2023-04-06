package io.github.Leonardo0013YT.UltraSkyWars.managers;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.cosmetics.kits.Kit;
import io.github.Leonardo0013YT.UltraSkyWars.cosmetics.kits.KitLevel;
import io.github.Leonardo0013YT.UltraSkyWars.data.SWPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class KitManager {

    private HashMap<Integer, Kit> kits = new HashMap<>();
    private UltraSkyWars plugin;
    private int lastPage, lastID;

    public KitManager(UltraSkyWars plugin) {
        this.plugin = plugin;
    }

    public void loadKits() {
        kits.clear();
        if (plugin.getKits().getConfig().getConfigurationSection("kits") == null) {
            return;
        }
        for (String kit : plugin.getKits().getConfig().getConfigurationSection("kits").getKeys(false)) {
            loadKit(kit);
        }
    }

    public void loadKit(String name) {
        int id = plugin.getKits().getInt("kits." + name + ".id");
        kits.put(id, new Kit(plugin, "kits." + name));
        if (lastID < id) {
            lastID = id;
        }
    }

    public int getNextId() {
        return lastID + 1;
    }

    public HashMap<Integer, Kit> getKits() {
        return kits;
    }

    public Kit getKitByItem(Player p, ItemStack item) {
        for (Kit k : kits.values()) {
            if (k.getKitLevelByItem(p, item) != null) {
                return k;
            }
        }
        return null;
    }

    public void setLastID(int lastID) {
        this.lastID = lastID;
    }

    public KitLevel getKitLevelByItem(Kit k, Player p, ItemStack item) {
        return k.getKitLevelByItem(p, item);
    }

    public int getKitsSize() {
        return kits.size();
    }

    public String getSelected(SWPlayer sw, String type) {
        switch (type) {
            case "SOLO":
                if (kits.containsKey(sw.getSoloKit())) {
                    Kit kit = kits.get(sw.getSoloKit());
                    if (kit.getLevels().containsKey(sw.getSoloKitLevel())) {
                        return kits.get(sw.getSoloKit()).getName();
                    }
                }
                break;
            case "TEAM":
                if (kits.containsKey(sw.getTeamKit())) {
                    Kit kit = kits.get(sw.getTeamKit());
                    if (kit.getLevels().containsKey(sw.getTeamKitLevel())) {
                        return kits.get(sw.getTeamKit()).getName();
                    }
                }
                break;
            case "RANKED":
                if (kits.containsKey(sw.getRankedKit())) {
                    Kit kit = kits.get(sw.getRankedKit());
                    if (kit.getLevels().containsKey(sw.getRankedKitLevel())) {
                        return kits.get(sw.getRankedKit()).getName();
                    }
                }
                break;
        }
        return plugin.getLang().get("messages.noSelected");
    }

    public int getLastPage() {
        return lastPage;
    }

    public void setLastPage(int lastPage) {
        if (getLastPage() < lastPage) {
            this.lastPage = lastPage;
        }
    }

}