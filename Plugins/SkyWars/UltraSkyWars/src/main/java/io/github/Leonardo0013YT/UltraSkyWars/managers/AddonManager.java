package io.github.Leonardo0013YT.UltraSkyWars.managers;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.addons.placeholders.PlaceholderAPIAddon;
import io.github.Leonardo0013YT.UltraSkyWars.interfaces.HologramAddon;
import io.github.Leonardo0013YT.UltraSkyWars.interfaces.PlaceholderAddon;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class AddonManager {

//    private EconomyAddon economy;
//    private HologramAddon ha;
//    private NametagAddon tag;
    private PlaceholderAddon placeholder;
    private HologramAddon ha;

    public boolean check(String pluginName) {
        UltraSkyWars plugin = UltraSkyWars.get();
        if (plugin.getConfig().isSet("addons." + pluginName) && plugin.getConfig().getBoolean("addons." + pluginName)) {
            if (Bukkit.getPluginManager().isPluginEnabled(pluginName)) {
                plugin.sendLogMessage("Hooked into §a" + pluginName + "§e!");
                return true;
            } else {
                plugin.getConfig().set("addons." + pluginName, false);
                plugin.saveConfig();
                return false;
            }
        }
        return false;
    }

    public void reload() {
        UltraSkyWars plugin = UltraSkyWars.get();
//        if (check("LeaderHeads")) {
//            new SkyWarsElo(plugin);
//            new SkyWarsSoloKills(plugin);
//            new SkyWarsTeamKills(plugin);
//            new SkyWarsRankedKills(plugin);
//            new SkyWarsSoloWins(plugin);
//            new SkyWarsTeamWins(plugin);
//            new SkyWarsRankedWins(plugin);
//            new SkyWarsSoloDeaths(plugin);
//            new SkyWarsTeamDeaths(plugin);
//            new SkyWarsRankedDeaths(plugin);
//            new SkyWarsCoins(plugin);
//            new SkyWarsKills(plugin);
//            new SkyWarsWins(plugin);
//            new SkyWarsDeaths(plugin);
//        }
//        if (check("SlimeWorldManager")) {
//            slime = new SlimeWorldManagerAddon(plugin);
//        }
//        if (check("MVdWPlaceholderAPI")) {
//            placeholder = new MVdWPlaceholderAPIAddon();
//        }
        if (check("PlaceholderAPI")) {
            placeholder = new PlaceholderAPIAddon();
        }
//        if (check("TAB")) {
//            tag = new TabAPI();
//        }
//        if (check("NametagEdit")) {
//            tag = new NametagEditAddon();
//        }
//        if (check("Vault")) {
//            economy = new VaultAddon();
//        }
//        if (check("PlayerPoints")) {
//            economy = new PlayerPointsAddon();
//        }
//        if (check("Coins")) {
//            economy = new CoinsAddon();
//        }
//        if (!plugin.isDisabled()) {
//            new BukkitRunnable() {
//                @Override
//                public void run() {
//                    if (check("TrHologram")) {
//                        remove();
//                        ha = new TrHologramAddon();
//                        reloadHologram();
//                    }
//                    if (check("Holograms")) {
//                        remove();
//                        ha = new HologramsAddon();
//                        reloadHologram();
//                    }
//                    if (check("HolographicDisplays")) {
//                        remove();
//                        ha = new HolographicDisplaysAddon();
//                        reloadHologram();
//                    }
//                }
//            }.runTaskLater(plugin, 80);
//        }
    }
//
//    public void reloadHologram() {
//        UltraSkyWars plugin = UltraSkyWars.get();
//        if (plugin.getIjm().isSoulWellInjection()) {
//            plugin.getIjm().getSoulwell().getSwm().reload();
//        }
//        if (plugin.getIjm().isCubeletsInjection()) {
//            plugin.getIjm().getCubelets().getCbm().reload();
//        }
//    }
//
    public void addCoins(Player p, double amount) {
        UltraSkyWars plugin = UltraSkyWars.get();
//        if (economy != null) {
//            economy.addCoins(p, amount);
//        } else {
//            SWPlayer sw = plugin.getDb().getSWPlayer(p);
//            if (sw == null) {
//                return;
//            }
//            sw.addCoins((int) amount);
//        }
//        Utils.updateSB(p);
    }

    public void setCoins(Player p, double amount) {
        UltraSkyWars plugin = UltraSkyWars.get();
//        if (economy != null) {
//            economy.setCoins(p, amount);
//        } else {
//            SWPlayer sw = plugin.getDb().getSWPlayer(p);
//            if (sw == null) {
//                return;
//            }
//            sw.setCoins((int) amount);
//        }
//        Utils.updateSB(p);
    }

    public void removeCoins(Player p, double amount) {
        UltraSkyWars plugin = UltraSkyWars.get();
//        if (economy != null) {
//            economy.removeCoins(p, amount);
//        } else {
//            SWPlayer sw = plugin.getDb().getSWPlayer(p);
//            if (sw == null) {
//                return;
//            }
//            sw.removeCoins((int) amount);
//        }
//        Utils.updateSB(p);
    }

    public double getCoins(Player p) {
        UltraSkyWars plugin = UltraSkyWars.get();
//        if (economy != null) {
//            return economy.getCoins(p);
//        } else {
//            SWPlayer sw = plugin.getDb().getSWPlayer(p);
//            if (sw == null) {
//                return 0;
//            }
//            return sw.getCoins();
//        }
        return 0;
    }

    public void createHologram(Location spawn, List<String> lines) {
        if (ha != null) {
            ha.createHologram(spawn, lines);
        }
    }

    public void createHologram(Location spawn, List<String> lines, ItemStack item) {
        if (ha != null) {
            ha.createHologram(spawn, lines, item);
        }
    }

    public void remove() {
        if (ha != null) {
            ha.remove();
        }
    }

    public void deleteHologram(Location spawn) {
        if (ha != null) {
            ha.deleteHologram(spawn);
        }
    }

    public boolean hasHologram(Location spawn) {
        if (ha != null) {
            return ha.hasHologram(spawn);
        }
        return false;
    }
    public boolean hasHologramPlugin() {
        return !(ha == null);
    }

    public void addPlayerNameTag(Player p) {
//        if (tag != null) {
//            tag.addPlayerNameTag(p);
//        }
    }

    public void resetPlayerNameTag(Player p) {
//        if (tag != null) {
//            tag.resetPlayerNameTag(p);
//        }
    }

    public String getPlayerPrefix(Player p) {
//        if (tag != null) {
//            return tag.getPrefix(p);
//        }
        return "";
    }

    public String getPlayerSuffix(Player p) {
//        if (tag != null) {
//            return tag.getSuffix(p);
//        }
        return "";
    }

    public String parsePlaceholders(Player p, String value) {
        UltraSkyWars plugin = UltraSkyWars.get();
        if (placeholder != null) {
            return placeholder.parsePlaceholders(p, value);
        }
        return value;
    }


}